package com.example.backend.controller;

import com.example.backend.controller.InmuebleController;
import com.example.backend.model.Disponibilidad;
import com.example.backend.model.Inmueble;
import com.example.backend.model.Usuario;
import com.example.backend.service.InmuebleService;
import com.example.backend.service.UsuarioService;
import com.example.backend.service.DisponibilidadService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InmuebleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class Inmueble_controller_Tests {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InmuebleService inmuebleService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private DisponibilidadService disponibilidadService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void buscarConFiltroTipo_Test() throws Exception{
        //Arrange
        Usuario prop1 = new Usuario();
        Usuario prop2 = new Usuario();
        Inmueble.Tipo tipo1 = Inmueble.Tipo.APARTAMENTO;
        Inmueble.Tipo tipo2 = Inmueble.Tipo.VIVIENDA_COMPLETA;
        Inmueble inmueble1 = new Inmueble("calle falsa 123", "Madrid", 100.0, tipo1, "Descripción del inmueble",prop1);
        Inmueble inmueble2 = new Inmueble("calle falsa 456", "Barcelona", 150.0, tipo2, "Descripción del inmueble 2", prop2);

        List <Inmueble> inmuebles = List.of(inmueble1, inmueble2);
        when(inmuebleService.buscarConFiltros(any(), eq(tipo1), eq(false), any(), any())).thenReturn(inmuebles);
        //Act & Assert
        mockMvc.perform(get("/api/inmuebles/buscar")
            .param("tipo", "APARTAMENTO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].direccion").value("calle falsa 123"))
            .andExpect(jsonPath("$[1].direccion").value("calle falsa 456"));
    }

    @Test
    public void buscarFiltroFechas_Test() throws Exception{
        //Arrange
        Usuario prop1 = new Usuario();
        Inmueble.Tipo tipo1 = Inmueble.Tipo.APARTAMENTO;
        Inmueble inmueble1 = new Inmueble("calle falsa 123", "Madrid", 100.0, tipo1, "Descripción del inmueble",prop1);
        Inmueble inmueble2 = new Inmueble("calle falsa 456", "Barcelona", 150.0, tipo1, "Descripción del inmueble 2", prop1);
        List <Inmueble> inmuebles = List.of(inmueble1, inmueble2);
        when(inmuebleService.buscarConFiltros(any(), any(), eq(false), any(), any())).thenReturn(inmuebles);
        //Act & Assert
        mockMvc.perform(get("/api/inmuebles/buscar")
            .param("fechaInicio", "2026-02-01")
            .param("fechaFin", "2026-04-01"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].direccion").value("calle falsa 123"))
            .andExpect(jsonPath("$[1].direccion").value("calle falsa 456"));
    }

    @Test
    public void buscarSinFiltros_Test() throws Exception{
        //Arrange
        Usuario prop1 = new Usuario();
        Inmueble inmueble1 = new Inmueble("calle falsa 123", "Madrid", 100.0, Inmueble.Tipo.APARTAMENTO, "Descripción del inmueble",prop1);
        Inmueble inmueble2 = new Inmueble("calle falsa 456", "Barcelona", 150.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripción del inmueble 2", prop1);
        List <Inmueble> inmuebles = List.of(inmueble1, inmueble2);
        when(inmuebleService.buscarConFiltros(any(), any(), eq(false), any(), any())).thenReturn(inmuebles);
        //Act & Assert
        mockMvc.perform(get("/api/inmuebles/buscar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].direccion").value("calle falsa 123"))
            .andExpect(jsonPath("$[1].direccion").value("calle falsa 456"));
    }

    @Test
    public void obtenerPorPropietario_Test() throws Exception{
        //Arrange
        Usuario prop = new Usuario();
        prop.setId(1L);
        Inmueble inmueble1 = new Inmueble("calle falsa 123", "Madrid", 100.0, Inmueble.Tipo.APARTAMENTO, "Descripción del inmueble",prop);
        Inmueble inmueble2 = new Inmueble("calle falsa 456", "Barcelona", 150.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripción del inmueble 2", prop);
        List <Inmueble> inmuebles = List.of(inmueble1, inmueble2);

        when(inmuebleService.obtenerPorPropietario(1L)).thenReturn(inmuebles);
        //Act & Assert
        mockMvc.perform(get("/api/inmuebles/propietario/{propietarioId}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].direccion").value("calle falsa 123"))
            .andExpect(jsonPath("$[1].direccion").value("calle falsa 456"));

    }
    
    @Test
    public void obtenerPorPropietarioSinInmuebles_Test() throws Exception{
        //Arrange
        Usuario prop = new Usuario();
        prop.setId(1L);

        when(inmuebleService.obtenerPorPropietario(1L)).thenReturn(List.of());
        //Act & Assert
        mockMvc.perform(get("/api/inmuebles/propietario/{propietarioId}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void darDeAlta_Test() throws Exception{
        //Arrange
        Usuario prop = new Usuario();
        prop.setRol(Usuario.Rol.PROPIETARIO);
        prop.setId(1L);

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(2L);

        Map<String, Object> body = new HashMap<>();
        body.put("propietarioId", 1L);
        body.put("direccion", "Calle Falsa 123");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 100.0);
        body.put("tipo", "APARTAMENTO");
        body.put("fechaInicio", "2026-01-01");
        body.put("fechaFin", "2026-06-01");

        when(usuarioService.findById(1L)).thenReturn(Optional.of(prop));
        when(inmuebleService.crearInmueble(any(Inmueble.class))).thenReturn(inmueble);
        //Act & Assert
        mockMvc.perform(post("/api/inmuebles/alta")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Inmueble creado correctamente"))
            .andExpect(jsonPath("$.idInmueble").value(2L));
    }

    @Test
    public void darDeAltaUsuarioInquilino_Test() throws Exception{
        //Arrange
        Usuario prop = new Usuario();
        prop.setRol(Usuario.Rol.INQUILINO);
        prop.setId(1L);

        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(2L);

        Map<String, Object> body = new HashMap<>();
        body.put("propietarioId", 1L);
        body.put("direccion", "Calle Falsa 123");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 100.0);
        body.put("tipo", "APARTAMENTO");
        body.put("fechaInicio", "2026-01-01");
        body.put("fechaFin", "2026-06-01");


         when(usuarioService.findById(1L)).thenReturn(Optional.of(prop));
        //Act & Assert
        mockMvc.perform(post("/api/inmuebles/alta")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("Solo los propietarios pueden dar de alta inmuebles"))
            .andExpect(jsonPath("$.message").value("Solo los propietarios pueden dar de alta inmuebles"));
    }

    @Test
    public void darDeAlta_FaltaCampoObligatorio_Test() throws Exception{
        //Arrange
        Map<String, Object> body = new HashMap<>();
        body.put("propietarioId", 1L);
        body.put("ciudad", "Madrid");
        body.put("direccion", null);
        body.put("precioNoche", 100.0);
        body.put("tipo", "APARTAMENTO");
        body.put("fechaInicio", "2026-01-01");
        body.put("fechaFin", "2026-06-01");
        //Act & Assert
        mockMvc.perform(post("/api/inmuebles/alta")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("El campo 'direccion' es obligatorio"));
    }

    @Test
    public void darDeAlta_PropietarioNOExiste_Test() throws Exception{
        //Arrange
        Map<String, Object> body = new HashMap<>();
        body.put("propietarioId", 1L);
        body.put("direccion", "Calle Falsa 123");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 100.0);
        body.put("tipo", "APARTAMENTO");
        body.put("fechaInicio", "2026-01-01");
        body.put("fechaFin", "2026-06-01");

        when(usuarioService.findById(1L)).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(post("/api/inmuebles/alta")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Propietario no encontrado"));
    }

    @Test
    public void darDeAlta_TipoInvalido_Test() throws Exception{
        //Arrange
        Usuario prop = new Usuario();
        prop.setRol(Usuario.Rol.PROPIETARIO);
        prop.setId(1L);

        Map<String, Object> body = new HashMap<>();
        body.put("propietarioId", 1L);
        body.put("direccion", "Calle Falsa 123");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 100.0);
        body.put("tipo", "CASA"); // Tipo no válido
        body.put("fechaInicio", "2026-01-01");
        body.put("fechaFin", "2026-06-01");

        when(usuarioService.findById(1L)).thenReturn(Optional.of(prop));
        //Act & Assert
        mockMvc.perform(post("/api/inmuebles/alta")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Valor de tipo de inmueble no válido"));
    }

    @Test
    public void darDeAlta_ErrorInterno_Test() throws Exception{
        //Arrange
        Usuario propietario = new Usuario();
        propietario.setId(1L);
        propietario.setRol(Usuario.Rol.PROPIETARIO);

        Map<String, Object> body = new HashMap<>();
        body.put("propietarioId", 1L);
        body.put("direccion", "Calle Falsa 123");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 100.0);
        body.put("tipo", "APARTAMENTO");
        body.put("fechaInicio", "2026-01-01");
        body.put("fechaFin", "2026-06-01");

        when(usuarioService.findById(1L))
                .thenReturn(Optional.of(propietario));

        when(inmuebleService.crearInmueble(any(Inmueble.class)))
                .thenThrow(new RuntimeException("Error BD"));

        //Act & Assert
        mockMvc.perform(post("/api/inmuebles/alta")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void editarInmueble_Test() throws Exception{
        //Arrange
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);

        Disponibilidad disp = new Disponibilidad();
        inmueble.setDisponibilidades(List.of(disp));

        Inmueble guardado = new Inmueble();
        guardado.setIdInmueble(1L);
        guardado.setPrecioNoche(120.0);
        guardado.setDisponibilidades(List.of(disp));

        Map<String, Object> body = new HashMap<>();
        body.put("direccion", "Nueva direccion");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 120.0);
        body.put("descripcion", "Descripcion actualizada");
        body.put("tipo", "APARTAMENTO");
        body.put("reservaDirecta", true);

        when(inmuebleService.obtenerPorId(1L)).thenReturn(Optional.of(inmueble));

        when(inmuebleService.crearInmueble(any(Inmueble.class))).thenReturn(guardado);
        //Act & Assert
        mockMvc.perform(put("/api/inmuebles/alta/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Propiedad actualizada correctamente"))
            .andExpect(jsonPath("$.idInmueble").value(1L));
    }

    @Test
    public void editarInmueble_NoExiste_Test() throws Exception{
        //Arrange
        Map<String, Object> body = new HashMap<>();
        body.put("direccion", "Nueva direccion");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 120.0);
        body.put("descripcion", "Descripcion actualizada");
        body.put("tipo", "APARTAMENTO");

        when(inmuebleService.obtenerPorId(1L)).thenReturn(Optional.empty());
        //Act & Assert
        mockMvc.perform(put("/api/inmuebles/alta/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isNotFound());
            
    }

    @Test
    public void editarInmueble_TipoInvalido_Test() throws Exception{
       //Arrange
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(1L);

        Map<String, Object> body = new HashMap<>();
        body.put("direccion", "Nueva direccion");
        body.put("ciudad", "Madrid");
        body.put("precioNoche", 120.0);
        body.put("descripcion", "Descripcion actualizada");
        body.put("tipo", "TIPO_INVALIDO");

        when(inmuebleService.obtenerPorId(1L)).thenReturn(Optional.of(inmueble));

        //Act & Assert
        mockMvc.perform(put("/api/inmuebles/alta/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Valor de tipo de inmueble no válido"));
    }

    @Test
    public void editarInmueble_ErrorInterno_Test() throws Exception{
        //Arrange
        Inmueble inmueble = new Inmueble();

        Map<String, Object> body = new HashMap<>();
        body.put("direccion", "Nueva direccion");

        when(inmuebleService.obtenerPorId(1L)).thenReturn(Optional.of(inmueble));

        when(inmuebleService.crearInmueble(any(Inmueble.class))).thenThrow(new RuntimeException("Error BD"));
        //Act & Assert
        mockMvc.perform(put("/api/inmuebles/alta/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void listar_Test() throws Exception{
        //Arrange
        Inmueble inmueble1 = new Inmueble("calle falsa 123", "Madrid", 100.0, Inmueble.Tipo.APARTAMENTO, "Descripción del inmueble", new Usuario());
        Inmueble inmueble2 = new Inmueble("calle falsa 456", "Barcelona", 150.0, Inmueble.Tipo.VIVIENDA_COMPLETA, "Descripción del inmueble 2", new Usuario());
        List <Inmueble> inmuebles = List.of(inmueble1, inmueble2);

        when(inmuebleService.obtenerTodos()).thenReturn(inmuebles);
        //Act & Assert
        mockMvc.perform(get("/api/inmuebles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].direccion").value("calle falsa 123"))
            .andExpect(jsonPath("$[1].direccion").value("calle falsa 456"));
    }

    @Test
    public void obtenerInmueble_Test() throws Exception{
        //Arrange
        Inmueble inmueble = new Inmueble("calle falsa 123", "Madrid", 100.0, Inmueble.Tipo.APARTAMENTO, "Descripción del inmueble", new Usuario());

        when(inmuebleService.obtenerPorId(1L)).thenReturn(Optional.of(inmueble));

        //Act & Assert
        mockMvc.perform(get("/api/inmuebles/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.direccion").value("calle falsa 123"));
    }

    @Test
    public void actualizarInmueble_Test() throws Exception{
        //Arrange
        Inmueble inmueble = new Inmueble("calle falsa 123", "Madrid", 100.0, Inmueble.Tipo.APARTAMENTO, "Descripción del inmueble", new Usuario());
        Inmueble inmuebleActualizado = new Inmueble("calle falsa 123", "Madrid", 150.0, Inmueble.Tipo.APARTAMENTO, "Descripción del inmueble", new Usuario());

        when(inmuebleService.actualizarInmueble(eq(1L), any(Inmueble.class))).thenReturn(inmuebleActualizado);
        //Act & Assert
        mockMvc.perform(put("/api/inmuebles/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inmuebleActualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.direccion").value("calle falsa 123"));
    }

    @Test
    public void actualizarInmueble_NotFound_Test() throws Exception{
        //Arrange
        Usuario prop = new Usuario();
        Inmueble guardado = new Inmueble("calle falsa 123", "Madrid", 100.0, Inmueble.Tipo.APARTAMENTO, "Descripcion", prop);
        Long id = 4L;

        when(inmuebleService.actualizarInmueble(eq(id), any(Inmueble.class))).thenThrow(new RuntimeException("Inmueble no encontrado"));
        //Act & Assert
        mockMvc.perform(put("/api/inmuebles/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(guardado)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void eliminarInmueble_Test() throws Exception{
        //Arrange´
        Long id = 1L;
        Inmueble inmueble = new Inmueble();
        inmueble.setIdInmueble(id);

        doNothing().when(inmuebleService).eliminarInmueble(id);
        //Act & Assert
        mockMvc.perform(delete("/api/inmuebles/{id}", id))
            .andExpect(status().isNoContent());
    }

    @Test
    public void eliminarInmueble_NotFound_Test() throws Exception{
        //Arrange
        Long id = 1L;

        doThrow(new RuntimeException("Inmueble no encontrado")).when(inmuebleService).eliminarInmueble(id);
        //Act & Assert
        mockMvc.perform(delete("/api/inmuebles/{id}", id))
            .andExpect(status().isNotFound());

    }

}
