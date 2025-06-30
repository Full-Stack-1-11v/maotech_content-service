package cl.maotech.content_service.controller;

import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.service.ContentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para el controlador ContentController.
 */
@WebMvcTest(ContentController.class)
public class ContentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllContents_debeRetornarListaVacia() throws Exception {
        // Given
        Mockito.when(contentService.getAllContents()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllContents_debeRetornarListaConContenido() throws Exception {
        // Given
        List<Content> contents = Arrays.asList(
                new Content("Título 1", "Descripción 1", "pdf", "active"),
                new Content("Título 2", "Descripción 2", "video", "inactive")
        );
        Mockito.when(contentService.getAllContents()).thenReturn(contents);

        // When & Then
        mockMvc.perform(get("/content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Título 1")))
                .andExpect(jsonPath("$[1].title", is("Título 2")));
    }

    @Test
    void createContent_debeRetornarCreado() throws Exception {
        // Given
        Content content = new Content("Nuevo Título", "Nueva Descripción", "pptx", "active");

        // When & Then
        mockMvc.perform(post("/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isCreated());

        Mockito.verify(contentService).createContent(any(Content.class));
    }

    @Test
    void getContentById_contenidoExistente_debeRetornarContenido() throws Exception {
        // Given
        Content content = new Content("Título Test", "Descripción Test", "pdf", "active");
        content.setId(1L);
        Mockito.when(contentService.getContentById(1L)).thenReturn(content);

        // When & Then
        mockMvc.perform(get("/content/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Título Test")))
                .andExpect(jsonPath("$.description", is("Descripción Test")))
                .andExpect(jsonPath("$.type", is("pdf")))
                .andExpect(jsonPath("$.status", is("active")));
    }

    @Test
    void getContentById_contenidoNoExistente_debeRetornarNotFound() throws Exception {
        // Given
        Mockito.when(contentService.getContentById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/content/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Contenido no encontrado con la ID: 999")));
    }

    @Test
    void updateContent_contenidoExistente_debeRetornarOk() throws Exception {
        // Given
        Content existingContent = new Content("Título Original", "Descripción Original", "pdf", "inactive");
        existingContent.setId(1L);
        Content updatedContent = new Content("Título Actualizado", "Descripción Actualizada", "video", "active");

        Mockito.when(contentService.getContentById(1L)).thenReturn(existingContent);

        // When & Then
        mockMvc.perform(put("/content/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedContent)))
                .andExpect(status().isOk());

        Mockito.verify(contentService).updateContent(eq(1L), any(Content.class));
    }

    @Test
    void updateContent_contenidoNoExistente_debeRetornarNotFound() throws Exception {
        // Given
        Content updatedContent = new Content("Título Actualizado", "Descripción Actualizada", "pptx", "active");
        Mockito.when(contentService.getContentById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/content/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedContent)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteContent_contenidoExistente_debeRetornarOk() throws Exception {
        // Given
        Content content = new Content("Título a Eliminar", "Descripción", "pdf", "inactive");
        content.setId(1L);
        Mockito.when(contentService.getContentById(1L)).thenReturn(content);

        // When & Then
        mockMvc.perform(delete("/content/1"))
                .andExpect(status().isOk());

        Mockito.verify(contentService).deleteContent(1L);
    }

    @Test
    void deleteContent_contenidoNoExistente_debeRetornarNotFound() throws Exception {
        // Given
        Mockito.when(contentService.getContentById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(delete("/content/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getContentByTypeAndStatus_debeRetornarContenidoFiltrado() throws Exception {
        // Given
        List<Content> filteredContents = Arrays.asList(
                new Content("PDF Activo 1", "Descripción 1", "pdf", "active"),
                new Content("PDF Activo 2", "Descripción 2", "pdf", "active")
        );
        Mockito.when(contentService.getContentByTypeAndStatus("pdf", "active"))
                .thenReturn(filteredContents);

        // When & Then
        mockMvc.perform(get("/content/search")
                        .param("type", "pdf")
                        .param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("pdf")))
                .andExpect(jsonPath("$[0].status", is("active")))
                .andExpect(jsonPath("$[1].type", is("pdf")))
                .andExpect(jsonPath("$[1].status", is("active")));
    }

    @Test
    void getContentByTypeAndStatus_sinResultados_debeRetornarListaVacia() throws Exception {
        // Given
        Mockito.when(contentService.getContentByTypeAndStatus("tipo_inexistente", "estado_inexistente"))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/content/search")
                        .param("type", "tipo_inexistente")
                        .param("status", "estado_inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}