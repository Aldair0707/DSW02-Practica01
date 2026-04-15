package com.dsw02.empleado.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest extends PostgresIntegrationBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRequireAuthenticationOnBusinessEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/empleados"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("AUTH_INVALIDA"));
    }

    @Test
    void shouldAllowPublicSwaggerAndHealth() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
            .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldForbidPatchEstadoForNonAdminActor() throws Exception {
        String createPayload = """
            {
              "nombre":"Demo",
              "direccion":"Calle Demo 123",
              "telefono":"5551112222",
              "correo":"demo@empresa.com",
              "contrasena":"MiPassword123"
            }
            """;

        mockMvc.perform(post("/api/v1/empleados")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isCreated());

        String patchPayload = """
            {
              "activo": false
            }
            """;

        mockMvc.perform(patch("/api/v1/empleados/EMP-1001/estado")
                .with(httpBasic("demo@empresa.com", "MiPassword123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchPayload))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("NO_AUTORIZADO"));

        mockMvc.perform(get("/api/v1/empleados")
                .with(httpBasic("demo@empresa.com", "MiPassword123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.content[0].correo").exists());
    }

    @Test
    void shouldNotExposeUnversionedListEndpoints() throws Exception {
        mockMvc.perform(get("/empleados")
                .with(httpBasic("admin", "admin123")))
            .andExpect(status().isNotFound());

        mockMvc.perform(get("/departamentos")
                .with(httpBasic("admin", "admin123")))
            .andExpect(status().isNotFound());

        mockMvc.perform(get("/departamentos/DEP-0000/empleados")
                .with(httpBasic("admin", "admin123")))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEmpleadoProfileForActiveEmpleadoCredentials() throws Exception {
        String createPayload = """
            {
              "nombre":"Empleado Auth",
              "direccion":"Calle Auth 123",
              "telefono":"5553334444",
              "correo":"empleado.auth@empresa.com",
              "contrasena":"MiPassword123"
            }
            """;

        mockMvc.perform(post("/api/v1/empleados")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/empleados/auth/me")
                .with(httpBasic("empleado.auth@empresa.com", "MiPassword123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.actorType").value("EMPLEADO"))
            .andExpect(jsonPath("$.username").value("empleado.auth@empresa.com"))
            .andExpect(jsonPath("$.permissions[0]").value("SELF"));
    }

    @Test
    void shouldReturnAdminActorProfileForAdminCredentials() throws Exception {
        mockMvc.perform(get("/api/v1/empleados/auth/me")
                .with(httpBasic("admin", "admin123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.actorType").value("ADMIN"))
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.empleado").isEmpty())
            .andExpect(jsonPath("$.permissions[0]").value("CRUD_EMPLEADOS"));
    }

    @Test
    void shouldAllowReadOnlyListingsAndDenyWritesForEmpleadoActor() throws Exception {
        String createPayload = """
            {
              "nombre":"Empleado Denegado",
              "direccion":"Calle Denegada 123",
              "telefono":"5559990000",
              "correo":"empleado.denegado@empresa.com",
              "contrasena":"MiPassword123"
            }
            """;

        mockMvc.perform(post("/api/v1/empleados")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/empleados")
                .with(httpBasic("empleado.denegado@empresa.com", "MiPassword123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").exists());

        mockMvc.perform(get("/api/v1/departamentos")
                .with(httpBasic("empleado.denegado@empresa.com", "MiPassword123")))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/departamentos")
                .with(httpBasic("empleado.denegado@empresa.com", "MiPassword123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "nombre":"Depto no permitido"
                    }
                    """))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("NO_AUTORIZADO"));
    }

    @Test
    void shouldReturnGeneric401ForInvalidPasswordAndInactiveAccount() throws Exception {
        String createPayload = """
            {
              "nombre":"Empleado Auth Error",
              "direccion":"Calle Error 123",
              "telefono":"5551010101",
              "correo":"empleado.error@empresa.com",
              "contrasena":"MiPassword123"
            }
            """;

        mockMvc.perform(post("/api/v1/empleados")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/empleados/auth/me")
                .with(httpBasic("empleado.error@empresa.com", "PasswordIncorrecta")))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("AUTH_INVALIDA"))
            .andExpect(jsonPath("$.message").value("Credenciales invalidas"));

        mockMvc.perform(patch("/api/v1/empleados/EMP-1001/estado")
                .with(httpBasic("admin", "admin123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "activo": false
                    }
                    """))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/empleados/auth/me")
                .with(httpBasic("empleado.error@empresa.com", "MiPassword123")))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("AUTH_INVALIDA"))
            .andExpect(jsonPath("$.message").value("Credenciales invalidas"));
    }
}
