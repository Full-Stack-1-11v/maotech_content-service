package cl.maotech.content_service.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import cl.maotech.content_service.model.Content;

/**
 * Pruebas de integración para el repositorio ContentRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
public class ContentRepositoryTests {

    @Autowired
    private ContentRepository contentRepository;

    private Content content1;
    private Content content2;
    private Content content3;

    @BeforeEach
    void setUp() {
        contentRepository.deleteAll();
        
        content1 = new Content("Documento PDF 1", "Descripción del PDF 1", "pdf", "active");
        content2 = new Content("Video Tutorial", "Descripción del video", "video", "active");
        content3 = new Content("Presentación PPT", "Descripción de la presentación", "pptx", "inactive");

        contentRepository.save(content1);
        contentRepository.save(content2);
        contentRepository.save(content3);
    }

    @Test
    void findByTypeAndStatus_conTipoYEstadoExistentes_debeRetornarContenidos() {
        // When
        List<Content> activeContents = contentRepository.findByTypeAndStatus("pdf", "active");

        // Then
        assertEquals(1, activeContents.size());
        assertEquals("Documento PDF 1", activeContents.get(0).getTitle());
        assertEquals("pdf", activeContents.get(0).getType());
        assertEquals("active", activeContents.get(0).getStatus());
    }

    @Test
    void findByTypeAndStatus_conEstadoActive_debeRetornarMultiplesContenidos() {
        // When
        List<Content> results = contentRepository.findByTypeAndStatus("video", "active");

        // Then
        assertEquals(1, results.size());
        assertEquals("Video Tutorial", results.get(0).getTitle());
    }

    @Test
    void findByTypeAndStatus_conEstadoInactive_debeRetornarContenidos() {
        // When
        List<Content> inactiveContents = contentRepository.findByTypeAndStatus("pptx", "inactive");

        // Then
        assertEquals(1, inactiveContents.size());
        assertEquals("Presentación PPT", inactiveContents.get(0).getTitle());
        assertEquals("inactive", inactiveContents.get(0).getStatus());
    }

    @Test
    void findByTypeAndStatus_conTipoInexistente_debeRetornarListaVacia() {
        // When
        List<Content> results = contentRepository.findByTypeAndStatus("docx", "active");

        // Then
        assertTrue(results.isEmpty());
    }

    @Test
    void findByTypeAndStatus_conEstadoInexistente_debeRetornarListaVacia() {
        // When
        List<Content> results = contentRepository.findByTypeAndStatus("pdf", "archived");

        // Then
        assertTrue(results.isEmpty());
    }

    @Test
    void save_nuevoContenido_debeGuardarCorrectamente() {
        // Given
        Content newContent = new Content("Nuevo Documento", "Nueva descripción", "pdf", "active");

        // When
        Content savedContent = contentRepository.save(newContent);

        // Then
        assertNotNull(savedContent.getId());
        assertEquals("Nuevo Documento", savedContent.getTitle());
        assertEquals("Nueva descripción", savedContent.getDescription());
        assertEquals("pdf", savedContent.getType());
        assertEquals("active", savedContent.getStatus());
    }

    @Test
    void findById_conIdExistente_debeRetornarContenido() {
        // When
        Optional<Content> foundContent = contentRepository.findById(content1.getId());

        // Then
        assertTrue(foundContent.isPresent());
        assertEquals("Documento PDF 1", foundContent.get().getTitle());
    }

    @Test
    void findById_conIdInexistente_debeRetornarEmpty() {
        // When
        Optional<Content> foundContent = contentRepository.findById(999L);

        // Then
        assertFalse(foundContent.isPresent());
    }

    @Test
    void deleteById_debeEliminarContenido() {
        // Given
        Long contentId = content1.getId();
        assertTrue(contentRepository.findById(contentId).isPresent());

        // When
        contentRepository.deleteById(contentId);

        // Then
        assertFalse(contentRepository.findById(contentId).isPresent());
    }

    @Test
    void findAll_debeRetornarTodosLosContenidos() {
        // When
        List<Content> allContents = contentRepository.findAll();

        // Then
        assertEquals(3, allContents.size());
    }

    @Test
    void count_debeRetornarCantidadCorrecta() {
        // When
        long count = contentRepository.count();

        // Then
        assertEquals(3, count);
    }
}