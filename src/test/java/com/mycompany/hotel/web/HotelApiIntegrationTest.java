package com.mycompany.hotel.web;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Las 4 evidencias solicitadas por el Taller 7 + casos de error del contrato HTTP. */
@SpringBootTest
@AutoConfigureMockMvc
class HotelApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    // --- helpers -----------------------------------------------------------------------

    private String crearCliente(String nombre, String email) throws Exception {
        String body = mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"" + nombre + "\",\"email\":\"" + email + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private String crearSuite(String numero) throws Exception {
        String body = mvc.perform(post("/api/habitaciones/suites").contentType(MediaType.APPLICATION_JSON)
                .content("{\"numero\":\"" + numero + "\",\"precioPorNoche\":350.0,\"capacidadMaxima\":2,"
                        + "\"jacuzziPrivado\":true,\"incluyeMayordomo\":true}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private String crearEstandar(String numero) throws Exception {
        String body = mvc.perform(post("/api/habitaciones/estandar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"numero\":\"" + numero + "\",\"precioPorNoche\":80.0,\"capacidadMaxima\":2,"
                        + "\"camasIndividuales\":2}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private String reservar(String clienteId, String habitacionId, int desdeDias, int hastaDias) throws Exception {
        LocalDate hoy = LocalDate.now();
        String body = mvc.perform(post("/api/clientes/" + clienteId + "/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"habitacionId\":\"" + habitacionId + "\",\"fechaInicio\":\"" + hoy.plusDays(desdeDias)
                        + "\",\"fechaFin\":\"" + hoy.plusDays(hastaDias) + "\",\"politica\":\"FLEXIBLE\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.idReserva");
    }

    private void confirmar(String clienteId, String reservaId) throws Exception {
        mvc.perform(post("/api/clientes/" + clienteId + "/reservas/" + reservaId + "/confirmar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("CONFIRMADA")));
    }

    // --- Evidencia 1: creación de suite ---------------------------------------------------

    @Test
    void evidencia1_crearSuitePresidencial_devuelve201ConIdYLocation() throws Exception {
        mvc.perform(post("/api/habitaciones/suites").contentType(MediaType.APPLICATION_JSON)
                .content("{\"numero\":\"P05-501\",\"precioPorNoche\":350.0,\"capacidadMaxima\":2,"
                        + "\"jacuzziPrivado\":true,\"incluyeMayordomo\":true}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.startsWith("/api/habitaciones/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tipo", is("SUITE")))
                .andExpect(jsonPath("$.jacuzziPrivado", is(true)))
                .andExpect(jsonPath("$.incluyeMayordomo", is(true)))
                .andExpect(jsonPath("$.precioPorNoche", is(350.0)));
    }

    // --- Evidencia 2: consulta polimórfica --------------------------------------------------

    @Test
    void evidencia2_listaPolimorficaConviveEstandarYSuiteConSusCamposPropios() throws Exception {
        crearSuite("P07-701");
        crearEstandar("P02-201");

        mvc.perform(get("/api/habitaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.tipo=='SUITE' && @.numero=='P07-701')].jacuzziPrivado", hasItem(true)))
                .andExpect(jsonPath("$[?(@.tipo=='SUITE' && @.numero=='P07-701')].camasIndividuales", hasSize(0)))
                .andExpect(jsonPath("$[?(@.tipo=='ESTANDAR' && @.numero=='P02-201')].camasIndividuales", hasItem(2)))
                .andExpect(jsonPath("$[?(@.tipo=='ESTANDAR' && @.numero=='P02-201')].jacuzziPrivado", hasSize(0)));
    }

    // --- Evidencia 3: resumen del cliente -------------------------------------------------

    @Test
    void evidencia3_resumenCalculaTotalesYNoTieneCiclos() throws Exception {
        String cliente = crearCliente("Ana Torres", "ana.resumen@correo.com");
        String estandar = crearEstandar("P03-301");
        String suite = crearSuite("P08-801");

        String r1 = reservar(cliente, estandar, 10, 13);   // 3 noches x 80  = 240
        confirmar(cliente, r1);
        String r2 = reservar(cliente, suite, 20, 22);      // 2 noches x 350 = 700
        confirmar(cliente, r2);
        reservar(cliente, estandar, 30, 31);               // pendiente: no cuenta como gasto

        mvc.perform(get("/api/clientes/" + cliente + "/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Ana Torres")))
                .andExpect(jsonPath("$.activo", is(true)))
                .andExpect(jsonPath("$.penalizaciones", is(0)))
                .andExpect(jsonPath("$.totalReservasRealizadas", is(3)))
                .andExpect(jsonPath("$.montoTotalGastado", is(940.0)))
                .andExpect(jsonPath("$.reservasRecientes", hasSize(3)))
                // más reciente primero
                .andExpect(jsonPath("$.reservasRecientes[0].numeroHabitacion", is("P03-301")))
                .andExpect(jsonPath("$.reservasRecientes[1].numeroHabitacion", is("P08-801")))
                // proyección plana: la reserva no vuelve a referenciar al cliente
                .andExpect(jsonPath("$.reservasRecientes[0].cliente").doesNotExist())
                .andExpect(jsonPath("$.reservasRecientes[0].idReserva", notNullValue()))
                .andExpect(jsonPath("$.reservasRecientes[0].costoTotal", is(80.0)))
                .andExpect(jsonPath("$..$ref").isEmpty());
    }

    // --- Evidencia 4: PATCH ------------------------------------------------------------------

    @Test
    void evidencia4_patchSoloCambiaElNombreYConservaEmailEstadoYPenalizaciones() throws Exception {
        String cliente = crearCliente("Nombre Original", "patch@correo.com");

        mvc.perform(patch("/api/clientes/" + cliente).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nuevo Nombre Modificado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Nuevo Nombre Modificado")))
                .andExpect(jsonPath("$.email", is("patch@correo.com")))
                .andExpect(jsonPath("$.activo", is(true)))
                .andExpect(jsonPath("$.penalizaciones", is(0)));

        // Releído de la base de datos
        mvc.perform(get("/api/clientes/" + cliente))
                .andExpect(jsonPath("$.nombre", is("Nuevo Nombre Modificado")))
                .andExpect(jsonPath("$.email", is("patch@correo.com")));
    }

    @Test
    void patch_ignoraCamposProtegidosEnviadosPorElCliente_massAssignment() throws Exception {
        String cliente = crearCliente("Protegido", "protegido@correo.com");

        mvc.perform(patch("/api/clientes/" + cliente).contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"nuevo@correo.com\",\"activo\":false,\"penalizaciones\":99,"
                        + "\"id\":\"00000000-0000-0000-0000-000000000000\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(cliente)))
                .andExpect(jsonPath("$.nombre", is("Protegido")))
                .andExpect(jsonPath("$.email", is("nuevo@correo.com")))
                .andExpect(jsonPath("$.activo", is(true)))
                .andExpect(jsonPath("$.penalizaciones", is(0)));
    }

    @Test
    void patchConBodyVacio_noCambiaNada() throws Exception {
        String cliente = crearCliente("Sin Cambios", "sincambios@correo.com");
        mvc.perform(patch("/api/clientes/" + cliente).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Sin Cambios")))
                .andExpect(jsonPath("$.email", is("sincambios@correo.com")));
    }

    // --- Contrato HTTP: errores ------------------------------------------------------------

    @Test
    void patchConEmailInvalido_devuelve400() throws Exception {
        String cliente = crearCliente("Email Malo", "emailmalo@correo.com");
        mvc.perform(patch("/api/clientes/" + cliente).contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"no-es-un-email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchConEmailDeOtroCliente_devuelve409() throws Exception {
        crearCliente("Uno", "duplicado@correo.com");
        String otro = crearCliente("Dos", "dos@correo.com");
        mvc.perform(patch("/api/clientes/" + otro).contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"duplicado@correo.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void clienteInexistente_devuelve404() throws Exception {
        mvc.perform(get("/api/clientes/00000000-0000-0000-0000-000000000001/resumen"))
                .andExpect(status().isNotFound());
    }

    @Test
    void suiteConNumeroInvalido_devuelve400() throws Exception {
        mvc.perform(post("/api/habitaciones/suites").contentType(MediaType.APPLICATION_JSON)
                .content("{\"numero\":\"501\",\"precioPorNoche\":350.0,\"capacidadMaxima\":2,"
                        + "\"jacuzziPrivado\":true,\"incluyeMayordomo\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void habitacionDuplicada_devuelve409() throws Exception {
        crearEstandar("P04-401");
        mvc.perform(post("/api/habitaciones/estandar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"numero\":\"P04-401\",\"precioPorNoche\":80.0,\"capacidadMaxima\":2,\"camasIndividuales\":1}"))
                .andExpect(status().isConflict());
    }

    @Test
    void reservaSolapada_devuelve409() throws Exception {
        String cliente = crearCliente("Solapa", "solapa@correo.com");
        String habitacion = crearEstandar("P06-601");
        reservar(cliente, habitacion, 10, 15);
        LocalDate hoy = LocalDate.now();
        mvc.perform(post("/api/clientes/" + cliente + "/reservas").contentType(MediaType.APPLICATION_JSON)
                .content("{\"habitacionId\":\"" + habitacion + "\",\"fechaInicio\":\"" + hoy.plusDays(12)
                        + "\",\"fechaFin\":\"" + hoy.plusDays(14) + "\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void confirmarDosVeces_devuelve409_yCancelarConfirmadaDevuelve200() throws Exception {
        String cliente = crearCliente("Doble", "doble@correo.com");
        String habitacion = crearEstandar("P09-901");
        String reserva = reservar(cliente, habitacion, 10, 12);
        confirmar(cliente, reserva);
        mvc.perform(post("/api/clientes/" + cliente + "/reservas/" + reserva + "/confirmar"))
                .andExpect(status().isConflict());
        mvc.perform(post("/api/clientes/" + cliente + "/reservas/" + reserva + "/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reserva.estado", is("CANCELADA")))
                .andExpect(jsonPath("$.recargoAplicado", is(0.0)));
        // Simetría: cancelada ⇒ tampoco se puede volver a cancelar
        mvc.perform(post("/api/clientes/" + cliente + "/reservas/" + reserva + "/cancelar"))
                .andExpect(status().isConflict());
        // y ya no cuenta como gasto
        mvc.perform(get("/api/clientes/" + cliente + "/resumen"))
                .andExpect(jsonPath("$.montoTotalGastado", is(0.0)))
                .andExpect(jsonPath("$.reservasRecientes[0].estado", not("CONFIRMADA")));
    }
}
