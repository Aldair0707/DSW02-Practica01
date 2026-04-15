package com.dsw02.empleado.application;

import com.dsw02.empleado.domain.ClaveParser;
import com.dsw02.empleado.domain.CorreoNormalizer;
import com.dsw02.empleado.domain.Empleado;
import com.dsw02.empleado.domain.exception.NotFoundException;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoEntity;
import com.dsw02.empleado.infrastructure.persistence.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class ObtenerPerfilAutenticadoService {

    private final EmpleadoRepository empleadoRepository;
    private final ClaveParser claveParser;
    private final CorreoNormalizer correoNormalizer;

    public ObtenerPerfilAutenticadoService(
        EmpleadoRepository empleadoRepository,
        ClaveParser claveParser,
        CorreoNormalizer correoNormalizer
    ) {
        this.empleadoRepository = empleadoRepository;
        this.claveParser = claveParser;
        this.correoNormalizer = correoNormalizer;
    }

    public Empleado obtenerPorCorreoPrincipal(String principalName) {
        String correo = correoNormalizer.normalize(principalName);
        EmpleadoEntity entity = empleadoRepository.findByCorreoIgnoreCase(correo)
            .orElseThrow(() -> new NotFoundException("Empleado no encontrado para principal autenticado"));

        return new Empleado(
            claveParser.buildClave(entity.getId().getConsecutivo()),
            entity.getNombre(),
            entity.getDireccion(),
            entity.getTelefono(),
            entity.getDepartamentoClave(),
            entity.getCorreo(),
            entity.isActivo()
        );
    }
}
