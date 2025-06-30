package cl.maotech.content_service.assembler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import cl.maotech.content_service.assemblers.ContentModelAssembler;
import cl.maotech.content_service.model.Content;

/**
 * Pruebas unitarias para ContentModelAssembler.
 */
public class ContentModelAssemblerTests {

    private ContentModelAssembler assembler;
    private Content sampleContent;

    @BeforeEach
    void setUp() {
        assembler = new ContentModelAssembler();
        sampleContent = new Content();
        sampleContent.setId(1L);
        sampleContent.setTitle("Título de Prueba");
        sampleContent.setDescription("Descripción de prueba");
        sampleContent.setType("pdf");
        sampleContent.setStatus("active");
    }

    @Test
    void toModel_debeCrearEntityModelConEnlaces() {
        // When
        EntityModel<Content> result = assembler.toModel(sampleContent);

        // Then
        assertNotNull(result);
        assertEquals(sampleContent, result.getContent());
        
        // Verificar que tiene enlaces
        assertFalse(result.getLinks().isEmpty());
        assertTrue(result.getLinks().hasSize(5)); // self, contents, update, delete, search
    }

    @Test
    void toModel_debeIncluirEnlaceSelf() {
        // When
        EntityModel<Content> result = assembler.toModel(sampleContent);

        // Then
        assertTrue(result.hasLink("self"));
        Link selfLink = result.getRequiredLink("self");
        assertTrue(selfLink.getHref().contains("/v2/content/1"));
    }

    @Test
    void toModel_debeIncluirEnlaceContents() {
        // When
        EntityModel<Content> result = assembler.toModel(sampleContent);

        // Then
        assertTrue(result.hasLink("contents"));
        Link contentsLink = result.getRequiredLink("contents");
        assertTrue(contentsLink.getHref().contains("/v2/content"));
    }

    @Test
    void toModel_debeIncluirEnlaceUpdate() {
        // When
        EntityModel<Content> result = assembler.toModel(sampleContent);

        // Then
        assertTrue(result.hasLink("update"));
        Link updateLink = result.getRequiredLink("update");
        assertEquals("PUT", updateLink.getType());
        assertTrue(updateLink.getHref().contains("/v2/content/1"));
    }

    @Test
    void toModel_debeIncluirEnlaceDelete() {
        // When
        EntityModel<Content> result = assembler.toModel(sampleContent);

        // Then
        assertTrue(result.hasLink("delete"));
        Link deleteLink = result.getRequiredLink("delete");
        assertEquals("DELETE", deleteLink.getType());
        assertTrue(deleteLink.getHref().contains("/v2/content/1"));
    }

    @Test
    void toModel_debeIncluirEnlaceSearch() {
        // When
        EntityModel<Content> result = assembler.toModel(sampleContent);

        // Then
        assertTrue(result.hasLink("search"));
        Link searchLink = result.getRequiredLink("search");
        assertTrue(searchLink.getHref().contains("/v2/content/search"));
        assertTrue(searchLink.getHref().contains("type=pdf"));
        assertTrue(searchLink.getHref().contains("status=active"));
    }

    @Test
    void toModel_conDiferentesTiposYEstados_debeGenerarEnlacesCorrectos() {
        // Given
        Content videoContent = new Content();
        videoContent.setId(2L);
        videoContent.setTitle("Video de Prueba");
        videoContent.setDescription("Descripción del video");
        videoContent.setType("video");
        videoContent.setStatus("inactive");

        // When
        EntityModel<Content> result = assembler.toModel(videoContent);

        // Then
        Link searchLink = result.getRequiredLink("search");
        assertTrue(searchLink.getHref().contains("type=video"));
        assertTrue(searchLink.getHref().contains("status=inactive"));
        
        Link selfLink = result.getRequiredLink("self");
        assertTrue(selfLink.getHref().contains("/v2/content/2"));
    }

    @Test
    void toModel_conContenidoSinId_debeCrearEnlaces() {
        // Given
        Content contentSinId = new Content();
        contentSinId.setTitle("Sin ID");
        contentSinId.setDescription("Descripción");
        contentSinId.setType("pptx");
        contentSinId.setStatus("active");

        // When
        EntityModel<Content> result = assembler.toModel(contentSinId);

        // Then
        assertNotNull(result);
        assertTrue(result.hasLink("contents"));
        assertTrue(result.hasLink("search"));
    }

    @Test
    void toModel_debePreservarContenidoOriginal() {
        // When
        EntityModel<Content> result = assembler.toModel(sampleContent);

        // Then
        Content content = result.getContent();
        assertNotNull(content);
        assertEquals(sampleContent.getId(), content.getId());
        assertEquals(sampleContent.getTitle(), content.getTitle());
        assertEquals(sampleContent.getDescription(), content.getDescription());
        assertEquals(sampleContent.getType(), content.getType());
        assertEquals(sampleContent.getStatus(), content.getStatus());
    }

    @Test
    void toModel_conDiferentesTiposDeArchivo_debeGenerarEnlacesApropriados() {
        // Test para diferentes tipos de archivo
        String[] tipos = {"pdf", "video", "pptx", "docx"};
        String[] estados = {"active", "inactive"};

        for (String tipo : tipos) {
            for (String estado : estados) {
                // Given
                Content content = new Content();
                content.setId(1L);
                content.setTitle("Test " + tipo);
                content.setType(tipo);
                content.setStatus(estado);

                // When
                EntityModel<Content> result = assembler.toModel(content);

                // Then
                assertTrue(result.hasLink("search"));
                Link searchLink = result.getRequiredLink("search");
                assertTrue(searchLink.getHref().contains("type=" + tipo));
                assertTrue(searchLink.getHref().contains("status=" + estado));
            }
        }
    }
}