package com.dsw02.empleado.api;

import com.dsw02.empleado.api.dto.EmpleadoCreateRequest;
import com.dsw02.empleado.api.dto.EmpleadoAuthProfileResponse;
import com.dsw02.empleado.api.dto.EmpleadoEstadoRequest;
import com.dsw02.empleado.api.dto.EmpleadoEstadoResponse;
import com.dsw02.empleado.api.dto.EmpleadoDepartamentoRequest;
import com.dsw02.empleado.api.dto.EmpleadoResponse;
import com.dsw02.empleado.api.dto.EmpleadoUpdateRequest;
import com.dsw02.empleado.application.ActualizarEmpleadoService;
import com.dsw02.empleado.application.AsignarDepartamentoEmpleadoService;
import com.dsw02.empleado.application.CrearEmpleadoService;
import com.dsw02.empleado.application.EliminarEmpleadoService;
import com.dsw02.empleado.application.ListarEmpleadosService;
import com.dsw02.empleado.application.ObtenerEmpleadoService;
import com.dsw02.empleado.application.ObtenerPerfilAutenticadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empleados")
@Tag(name = "Empleados")
public class EmpleadoController {

    private final CrearEmpleadoService crearEmpleadoService;
    private final ObtenerEmpleadoService obtenerEmpleadoService;
    private final ListarEmpleadosService listarEmpleadosService;
    private final ActualizarEmpleadoService actualizarEmpleadoService;
    private final EliminarEmpleadoService eliminarEmpleadoService;
    private final AsignarDepartamentoEmpleadoService asignarDepartamentoEmpleadoService;
    private final ObtenerPerfilAutenticadoService obtenerPerfilAutenticadoService;
    @Value("${APP_BASIC_USER:admin}")
    private String adminBasicUser;

    public EmpleadoController(
        CrearEmpleadoService crearEmpleadoService,
        ObtenerEmpleadoService obtenerEmpleadoService,
        ListarEmpleadosService listarEmpleadosService,
        ActualizarEmpleadoService actualizarEmpleadoService,
        EliminarEmpleadoService eliminarEmpleadoService,
        AsignarDepartamentoEmpleadoService asignarDepartamentoEmpleadoService,
        ObtenerPerfilAutenticadoService obtenerPerfilAutenticadoService
    ) {
        this.crearEmpleadoService = crearEmpleadoService;
        this.obtenerEmpleadoService = obtenerEmpleadoService;
        this.listarEmpleadosService = listarEmpleadosService;
        this.actualizarEmpleadoService = actualizarEmpleadoService;
        this.eliminarEmpleadoService = eliminarEmpleadoService;
        this.asignarDepartamentoEmpleadoService = asignarDepartamentoEmpleadoService;
        this.obtenerPerfilAutenticadoService = obtenerPerfilAutenticadoService;
    }

    @PostMapping
    @Operation(summary = "Crear empleado")
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(EmpleadoResponse.fromDomain(crearEmpleadoService.crear(request)));
    }

    @GetMapping("/{clave}")
    @Operation(summary = "Consultar empleado por clave")
    public EmpleadoResponse obtener(@PathVariable String clave) {
        return EmpleadoResponse.fromDomain(obtenerEmpleadoService.obtenerPorClave(clave));
    }

    @GetMapping
    @Operation(summary = "Listar empleados")
    public Object listar(
        Principal principal,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        if (adminBasicUser.equals(principal.getName())) {
            return listarEmpleadosService.listar(page, size);
        }

        return listarEmpleadosService.listarReadOnly(page, size);
    }

    @GetMapping("/auth/me")
    @Operation(summary = "Obtener perfil autenticado")
    public EmpleadoAuthProfileResponse perfilAutenticado(Principal principal) {
        if (adminBasicUser.equals(principal.getName())) {
            return EmpleadoAuthProfileResponse.admin(principal.getName());
        }

        return EmpleadoAuthProfileResponse.empleado(obtenerPerfilAutenticadoService.obtenerPorCorreoPrincipal(principal.getName()));
    }

    @PutMapping("/{clave}")
    @Operation(summary = "Actualizar empleado")
    public EmpleadoResponse actualizar(@PathVariable String clave, @Valid @RequestBody EmpleadoUpdateRequest request) {
        return EmpleadoResponse.fromDomain(actualizarEmpleadoService.actualizar(clave, request));
    }

    @DeleteMapping("/{clave}")
    @Operation(summary = "Eliminar empleado")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        eliminarEmpleadoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{clave}/estado")
    @Operation(summary = "Actualizar estado de cuenta")
    public EmpleadoEstadoResponse actualizarEstado(
        @PathVariable String clave,
        @Valid @RequestBody EmpleadoEstadoRequest request
    ) {
        var empleado = actualizarEmpleadoService.actualizarEstado(clave, request);
        return new EmpleadoEstadoResponse(empleado.clave(), empleado.activo());
    }

    @PatchMapping("/{clave}/departamento")
    @Operation(summary = "Asignar o cambiar departamento de empleado")
    public EmpleadoResponse asignarDepartamento(
        @PathVariable String clave,
        @Valid @RequestBody EmpleadoDepartamentoRequest request
    ) {
        return EmpleadoResponse.fromDomain(asignarDepartamentoEmpleadoService.asignar(clave, request));
    }
}
