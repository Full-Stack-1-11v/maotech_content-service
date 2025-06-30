package cl.maotech.content_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.repository.ContentRepository;

/**
 * Pruebas unitarias para el servicio ContentService.
 */
@ExtendWith(MockitoExtension.class)
public class ContentServiceTests {

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private ContentService contentService;

    private Content sampleContent1;
    private Content sampleContent2;

    @BeforeEach
    void setUp() {
        sampleContent1 = new Content("Título 1", "Descripción 1", "pdf", "active");
        sampleContent1.setId(1L);
        
        sampleContent2 = new Content("Título 2", "Descripción 2", "video", "inactive");
        sampleContent2.setId(2L);
    }

    @Test
    void getAllContents_debeRetornarTodosLosContenidos() {
        // Given
        List<Content> expectedContents = Arrays.asList(sampleContent1, sampleContent2);
        when(contentRepository.findAll()).thenReturn(expectedContents);

        // When
        List<Content> actualContents = contentService.getAllContents();

        // Then
        assertEquals(expectedContents.size(), actualContents.size());
        assertEquals(expectedContents, actualContents);
        verify(contentRepository).findAll();
    }

    @Test
    void createContent_debeGuardarContenido() {
        // Given
        Content newContent = new Content("Nuevo Título", "Nueva Descripción", "pptx", "active");
        when(contentRepository.save(any(Content.class))).thenReturn(newContent);

        // When
        contentService.createContent(newContent);

        // Then
        verify(contentRepository).save(newContent);
    }

    @Test
    void getContentById_contenidoExistente_debeRetornarContenido() {
        // Given
        when(contentRepository.findById(1L)).thenReturn(Optional.of(sampleContent1));

        // When
        Content result = contentService.getContentById(1L);

        // Then
        assertNotNull(result);
        assertEquals(sampleContent1.getId(), result.getId());
        assertEquals(sampleContent1.getTitle(), result.getTitle());
        verify(contentRepository).findById(1L);
    }

    @Test
    void getContentById_contenidoNoExistente_debeRetornarNull() {
        // Given
        when(contentRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Content result = contentService.getContentById(999L);

        // Then
        assertNull(result);
        verify(contentRepository).findById(999L);
    }

    @Test
    void updateContent_contenidoExistente_debeActualizar() {
        // Given
        Content existingContent = new Content("Título Original", "Descripción Original", "pdf", "active");
        existingContent.setId(1L);
        
        Content updatedData = new Content("Título Actualizado", "Descripción Actualizada", "video", "inactive");
        
        when(contentRepository.findById(1L)).thenReturn(Optional.of(existingContent));
        when(contentRepository.save(any(Content.class))).thenReturn(existingContent);

        // When
        contentService.updateContent(1L, updatedData);

        // Then
        assertEquals("Título Actualizado", existingContent.getTitle());
        assertEquals("Descripción Actualizada", existingContent.getDescription());
        assertEquals("video", existingContent.getType());
        assertEquals("inactive", existingContent.getStatus());
        verify(contentRepository).findById(1L);
        verify(contentRepository).save(existingContent);
    }

    @Test
    void updateContent_contenidoNoExistente_noDebeHacerNada() {
        // Given
        Content updatedData = new Content("Título", "Descripción", "pptx", "active");
        when(contentRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        contentService.updateContent(999L, updatedData);

        // Then
        verify(contentRepository).findById(999L);
        verify(contentRepository, never()).save(any(Content.class));
    }

    @Test
    void deleteContent_debeEliminarContenido() {
        // When
        contentService.deleteContent(1L);

        // Then
        verify(contentRepository).deleteById(1L);
    }

    @Test
    void getContentByTypeAndStatus_debeRetornarContenidoFiltrado() {
        // Given
        List<Content> filteredContents = Arrays.asList(
            new Content("PDF Activo 1", "Descripción 1", "pdf", "active"),
            new Content("PDF Activo 2", "Descripción 2", "pdf", "active")
        );
        when(contentRepository.findByTypeAndStatus("pdf", "active")).thenReturn(filteredContents);

        // When
        List<Content> result = contentService.getContentByTypeAndStatus("pdf", "active");

        // Then
        assertEquals(2, result.size());
        assertEquals(filteredContents, result);
        verify(contentRepository).findByTypeAndStatus("pdf", "active");
    }
}