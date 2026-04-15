package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.EmpleadoDepartamentoRequest;
import com.dsw02.empleado.domain.ClaveParser;
import com.dsw02.empleado.domain.Empleado;
import com.dsw02.empleado.domain.exception.DepartamentoNotFoundException;
import com.dsw02.empleado.domain.exception.NotFoundException;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoId;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class AsignarDepartamentoEmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final ClaveParser claveParser;

    public AsignarDepartamentoEmpleadoService(
        EmpleadoRepository empleadoRepository,
        DepartamentoRepository departamentoRepository,
        ClaveParser claveParser
    ) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
        this.claveParser = claveParser;
    }

    public Empleado asignar(String claveEmpleado, EmpleadoDepartamentoRequest request) {
        long consecutivo = claveParser.parseConsecutivo(claveEmpleado);
        EmpleadoId id = new EmpleadoId(ClaveParser.PREFIJO, consecutivo);

        var empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave: " + claveEmpleado));

        if (!departamentoRepository.existsByClave(request.getDepartamentoClave())) {
            throw new DepartamentoNotFoundException("Departamento no encontrado para clave: " + request.getDepartamentoClave());
        }

        empleado.setDepartamentoClave(request.getDepartamentoClave());
        var saved = empleadoRepository.save(empleado);

        return new Empleado(
            claveParser.buildClave(saved.getId().getConsecutivo()),
            saved.getNombre(),
            saved.getDireccion(),
            saved.getTelefono(),
            saved.getDepartamentoClave(),
            saved.getCorreo(),
            saved.isActivo()
        );
    }
}
