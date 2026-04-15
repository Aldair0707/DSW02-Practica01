package com.dsw02.empleado.api;

import com.dsw02.empleado.api.dto.DepartamentoCreateRequest;
import com.dsw02.empleado.api.dto.DepartamentoResponse;
import com.dsw02.empleado.api.dto.DepartamentoUpdateRequest;
import com.dsw02.empleado.api.dto.EmpleadoPageResponse;
import com.dsw02.empleado.application.ActualizarDepartamentoService;
import com.dsw02.empleado.application.CrearDepartamentoService;
import com.dsw02.empleado.application.EliminarDepartamentoService;
import com.dsw02.empleado.application.ListarDepartamentosService;
import com.dsw02.empleado.application.ListarEmpleadosPorDepartamentoService;
import com.dsw02.empleado.application.ObtenerDepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departamentos")
@Tag(name = "Departamentos")
public class DepartamentoController {

    private final CrearDepartamentoService crearDepartamentoService;
    private final ListarDepartamentosService listarDepartamentosService;
    private final ObtenerDepartamentoService obtenerDepartamentoService;
    private final ActualizarDepartamentoService actualizarDepartamentoService;
    private final EliminarDepartamentoService eliminarDepartamentoService;
    private final ListarEmpleadosPorDepartamentoService listarEmpleadosPorDepartamentoService;
    private final String adminBasicUser;

    public DepartamentoController(
        CrearDepartamentoService crearDepartamentoService,
        ListarDepartamentosService listarDepartamentosService,
        ObtenerDepartamentoService obtenerDepartamentoService,
        ActualizarDepartamentoService actualizarDepartamentoService,
        EliminarDepartamentoService eliminarDepartamentoService,
        ListarEmpleadosPorDepartamentoService listarEmpleadosPorDepartamentoService,
        @Value("${APP_BASIC_USER:admin}") String adminBasicUser
    ) {
        this.crearDepartamentoService = crearDepartamentoService;
        this.listarDepartamentosService = listarDepartamentosService;
        this.obtenerDepartamentoService = obtenerDepartamentoService;
        this.actualizarDepartamentoService = actualizarDepartamentoService;
        this.eliminarDepartamentoService = eliminarDepartamentoService;
        this.listarEmpleadosPorDepartamentoService = listarEmpleadosPorDepartamentoService;
        this.adminBasicUser = adminBasicUser;
    }

    @PostMapping
    @Operation(summary = "Crear departamento")
    public ResponseEntity<DepartamentoResponse> crear(@Valid @RequestBody DepartamentoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(DepartamentoResponse.fromDomain(crearDepartamentoService.crear(request)));
    }

    @GetMapping
    @Operation(summary = "Listar departamentos")
    public Object listar(
        Principal principal,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        if (adminBasicUser.equals(principal.getName())) {
            return listarDepartamentosService.listar(page, size);
        }

        return listarDepartamentosService.listarReadOnly(page, size);
    }

    @GetMapping("/{clave}")
    @Operation(summary = "Consultar departamento por clave")
    public DepartamentoResponse obtener(@PathVariable String clave) {
        return DepartamentoResponse.fromDomain(obtenerDepartamentoService.obtenerPorClave(clave));
    }

    @PutMapping("/{clave}")
    @Operation(summary = "Actualizar departamento")
    public DepartamentoResponse actualizar(
        @PathVariable String clave,
        @Valid @RequestBody DepartamentoUpdateRequest request
    ) {
        return DepartamentoResponse.fromDomain(actualizarDepartamentoService.actualizar(clave, request));
    }

    @DeleteMapping("/{clave}")
    @Operation(summary = "Eliminar departamento")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        eliminarDepartamentoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clave}/empleados")
    @Operation(summary = "Listar empleados por departamento")
    public EmpleadoPageResponse listarEmpleadosPorDepartamento(
        @PathVariable String clave,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        return listarEmpleadosPorDepartamentoService.listar(clave, page, size);
    }
}
