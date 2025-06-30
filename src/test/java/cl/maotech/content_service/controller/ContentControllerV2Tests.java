package cl.maotech.content_service.controller;

import cl.maotech.content_service.assemblers.ContentModelAssembler;
import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.service.ContentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para el controlador ContentControllerV2 con HATEOAS.
 */
@WebMvcTest(ContentControllerV2.class)
public class ContentControllerV2Tests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @MockBean
    private ContentModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configurar el mock del assembler para que devuelva EntityModels válidos
        Mockito.when(assembler.toModel(any(Content.class)))
                .thenAnswer(invocation -> {
                    Content content = invocation.getArgument(0);
                    return EntityModel.of(content)
                            .add(linkTo(methodOn(ContentControllerV2.class).getContentById(content.getId())).withSelfRel())
                            .add(linkTo(methodOn(ContentControllerV2.class).getAllContents()).withRel("contents"))
                            .add(linkTo(methodOn(ContentControllerV2.class).updateContent(content.getId(), content)).withRel("update"))
                            .add(linkTo(methodOn(ContentControllerV2.class).deleteContent(content.getId())).withRel("delete"));
                });
    }

    @Test
    void getAllContents_debeRetornarListaVaciaConHATEOAS() throws Exception {
        // Given
        Mockito.when(contentService.getAllContents()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/v2/content"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href", containsString("/v2/content")))
                .andExpect(jsonPath("$._links.create.href", containsString("/v2/content")));
    }

    @Test
    void getAllContents_debeRetornarListaConHATEOAS() throws Exception {
        // Given
        List<Content> contents = Arrays.asList(
                createSampleContent(1L, "Título 1", "pdf", "active"),
                createSampleContent(2L, "Título 2", "video", "inactive")
        );
        Mockito.when(contentService.getAllContents()).thenReturn(contents);

        // When & Then
        mockMvc.perform(get("/v2/content"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href", containsString("/v2/content")))
                .andExpect(jsonPath("$._links.create.href", containsString("/v2/content")));

        Mockito.verify(assembler, Mockito.times(2)).toModel(any(Content.class));
    }

    @Test
    void createContent_debeRetornarCreatedConHATEOAS() throws Exception {
        // Given
        Content content = createSampleContent(null, "Nuevo Título", "pptx", "active");
        Content savedContent = createSampleContent(1L, "Nuevo Título", "pptx", "active");
        
        Mockito.doNothing().when(contentService).createContent(any(Content.class));

        // When & Then
        mockMvc.perform(post("/v2/content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(content)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"));

        Mockito.verify(contentService).createContent(any(Content.class));
        Mockito.verify(assembler).toModel(any(Content.class));
    }

    @Test
    void getContentById_contenidoExistente_debeRetornarConHATEOAS() throws Exception {
        // Given
        Content content = createSampleContent(1L, "Título Test", "pdf", "active");
        Mockito.when(contentService.getContentById(1L)).thenReturn(content);

        // When & Then
        mockMvc.perform(get("/v2/content/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));

        Mockito.verify(assembler).toModel(content);
    }

    @Test
    void getContentById_contenidoNoExistente_debeRetornarNotFound() throws Exception {
        // Given
        Mockito.when(contentService.getContentById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/v2/content/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Contenido no encontrado con la ID: 999")));
    }

    @Test
    void updateContent_contenidoExistente_debeRetornarOkConHATEOAS() throws Exception {
        // Given
        Content existingContent = createSampleContent(1L, "Título Original", "pdf", "inactive");
        Content updatedContent = createSampleContent(1L, "Título Actualizado", "video", "active");

        Mockito.when(contentService.getContentById(1L)).thenReturn(existingContent, updatedContent);
        // Nota: updateContent debería ser void, por eso NO usar doNothing() aquí
        // Si updateContent retorna algo, usar when().thenReturn() en su lugar

        // When & Then
        mockMvc.perform(put("/v2/content/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedContent)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));

        Mockito.verify(contentService).updateContent(eq(1L), any(Content.class));
        Mockito.verify(assembler).toModel(updatedContent);
    }

    @Test
    void patchContent_contenidoExistente_debeRetornarOkConHATEOAS() throws Exception {
        // Given
        Content existingContent = createSampleContent(1L, "Título Original", "pdf", "inactive");
        Content patchData = new Content();
        patchData.setTitle("Título Parcialmente Actualizado");

        Mockito.when(contentService.getContentById(1L)).thenReturn(existingContent);
        // Nota: updateContent debería ser void, por eso NO usar doNothing() aquí

        // When & Then
        mockMvc.perform(patch("/v2/content/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));

        Mockito.verify(contentService).updateContent(eq(1L), any(Content.class));
        Mockito.verify(assembler).toModel(any(Content.class));
    }

    @Test
    void deleteContent_contenidoExistente_debeRetornarNoContent() throws Exception {
        // Given
        Content content = createSampleContent(1L, "Título a Eliminar", "pdf", "inactive");
        Mockito.when(contentService.getContentById(1L)).thenReturn(content);
        Mockito.doNothing().when(contentService).deleteContent(1L);

        // When & Then
        mockMvc.perform(delete("/v2/content/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(contentService).deleteContent(1L);
    }

    @Test
    void getContentByTypeAndStatus_debeRetornarContenidoFiltradoConHATEOAS() throws Exception {
        // Given
        List<Content> filteredContents = Arrays.asList(
                createSampleContent(1L, "PDF Activo 1", "pdf", "active"),
                createSampleContent(2L, "PDF Activo 2", "pdf", "active")
        );
        Mockito.when(contentService.getContentByTypeAndStatus("pdf", "active"))
                .thenReturn(filteredContents);

        // When & Then
        mockMvc.perform(get("/v2/content/search")
                        .param("type", "pdf")
                        .param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href", containsString("/v2/content/search")))
                .andExpect(jsonPath("$._links.all-contents.href", containsString("/v2/content")));

        Mockito.verify(assembler, Mockito.times(2)).toModel(any(Content.class));
    }

    @Test
    void getContentByType_contenidoExistente_debeRetornarOkConHATEOAS() throws Exception {
        // Given
        List<Content> contents = Arrays.asList(
                createSampleContent(1L, "Video 1", "video", "active"),
                createSampleContent(2L, "Video 2", "video", "active")
        );
        Mockito.when(contentService.getContentByTypeAndStatus("video", "active"))
                .thenReturn(contents);

        // When & Then
        mockMvc.perform(get("/v2/content/buscar/tipo/video"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href", containsString("/v2/content/buscar/tipo/video")))
                .andExpect(jsonPath("$._links.all-contents.href", containsString("/v2/content")));

        Mockito.verify(assembler, Mockito.times(2)).toModel(any(Content.class));
    }

    @Test
    void getContentByType_sinResultados_debeRetornarNotFound() throws Exception {
        // Given
        Mockito.when(contentService.getContentByTypeAndStatus("docx", "active"))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/v2/content/buscar/tipo/docx"))
                .andExpect(status().isNotFound());
    }

    @Test
    void exceptionHandler_debeRetornarErrorCorrectamente() throws Exception {
        // Given
        Mockito.when(contentService.getAllContents())
                .thenThrow(new RuntimeException("Error interno"));

        // When & Then
        mockMvc.perform(get("/v2/content"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Error interno")));
    }

    /**
     * Método auxiliar para crear contenido de muestra.
     */
    private Content createSampleContent(Long id, String title, String type, String status) {
        Content content = new Content();
        content.setId(id);
        content.setTitle(title);
        content.setDescription("Descripción de " + title);
        content.setType(type);
        content.setStatus(status);
        return content;
    }
}