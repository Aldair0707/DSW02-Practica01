package com.dsw02.empleado.application;

import com.dsw02.empleado.api.dto.EmpleadoCreateRequest;
import com.dsw02.empleado.domain.ClaveParser;
import com.dsw02.empleado.domain.CorreoNormalizer;
import com.dsw02.empleado.domain.Empleado;
import com.dsw02.empleado.domain.ErrorCode;
import com.dsw02.empleado.domain.exception.BusinessException;
import com.dsw02.empleado.domain.exception.DepartamentoNotFoundException;
import com.dsw02.empleado.infrastructure.persistence.ConsecutivoRepository;
import com.dsw02.empleado.infrastructure.persistence.DepartamentoRepository;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoEntity;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoId;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CrearEmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final ConsecutivoRepository consecutivoRepository;
    private final ClaveParser claveParser;
    private final CorreoNormalizer correoNormalizer;
    private final PasswordEncoder passwordEncoder;
    private final DepartamentoRepository departamentoRepository;

    public CrearEmpleadoService(
        EmpleadoRepository empleadoRepository,
        ConsecutivoRepository consecutivoRepository,
        ClaveParser claveParser,
        CorreoNormalizer correoNormalizer,
        PasswordEncoder passwordEncoder,
        DepartamentoRepository departamentoRepository
    ) {
        this.empleadoRepository = empleadoRepository;
        this.consecutivoRepository = consecutivoRepository;
        this.claveParser = claveParser;
        this.correoNormalizer = correoNormalizer;
        this.passwordEncoder = passwordEncoder;
        this.departamentoRepository = departamentoRepository;
    }

    public Empleado crear(EmpleadoCreateRequest request) {
        // Ignore client-provided clave; authoritative value is always generated server-side.
        String departamentoClave = request.getDepartamentoClave();
        if (departamentoClave == null || departamentoClave.isBlank()) {
            departamentoClave = "DEP-0000";
        } else {
            departamentoClave = departamentoClave.trim();
            if (!departamentoRepository.existsByClave(departamentoClave)) {
                throw new DepartamentoNotFoundException("Departamento no encontrado para clave: " + departamentoClave);
            }
        }

        long consecutivo = consecutivoRepository.siguienteConsecutivo();
        EmpleadoId id = new EmpleadoId(ClaveParser.PREFIJO, consecutivo);

        String correoNormalizado = correoNormalizer.normalize(request.getCorreo());
        if (empleadoRepository.existsByCorreoIgnoreCase(correoNormalizado)) {
            throw new BusinessException(ErrorCode.VALIDACION, "El correo ya esta registrado");
        }

        EmpleadoEntity entity = new EmpleadoEntity(
            id,
            request.getNombre(),
            request.getDireccion(),
            request.getTelefono(),
            correoNormalizado,
            passwordEncoder.encode(request.getContrasena()),
            true,
            departamentoClave
        );
        EmpleadoEntity saved = empleadoRepository.save(entity);

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
