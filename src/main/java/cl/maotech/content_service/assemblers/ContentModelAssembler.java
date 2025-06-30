package cl.maotech.content_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import cl.maotech.content_service.controller.ContentControllerV2;
import cl.maotech.content_service.model.Content;

/**
 * Assembler para convertir entidades Content en EntityModel con enlaces HATEOAS.
 * 
 * <p>Esta clase implementa RepresentationModelAssembler para proporcionar
 * enlaces hipermedia que permiten la navegación entre recursos relacionados.</p>
 */
@Component
public class ContentModelAssembler implements RepresentationModelAssembler<Content, EntityModel<Content>> {

    /**
     * Convierte una entidad Content en un EntityModel con enlaces HATEOAS.
     * 
     * @param content La entidad Content a convertir
     * @return EntityModel con la entidad y enlaces de navegación
     */
    @Override
    public EntityModel<Content> toModel(Content content) {
        // Link a sí mismo (self)
        Link selfLink = linkTo(methodOn(ContentControllerV2.class).getContentById(content.getId()))
                .withSelfRel();
        
        // Link a la colección de todos los contenidos
        Link allContentsLink = linkTo(methodOn(ContentControllerV2.class).getAllContents())
                .withRel("contents");
        
        // Link para actualizar el contenido
        Link updateLink = linkTo(methodOn(ContentControllerV2.class).updateContent(content.getId(), content))
                .withRel("update")
                .withType("PUT");
        
        // Link para eliminar el contenido
        Link deleteLink = linkTo(methodOn(ContentControllerV2.class).deleteContent(content.getId()))
                .withRel("delete")
                .withType("DELETE");
        
        // Link para búsqueda por tipo y estado
        Link searchLink = linkTo(methodOn(ContentControllerV2.class).getContentByTypeAndStatus(content.getType(), content.getStatus()))
                .withRel("search");
        
        return EntityModel.of(content, selfLink, allContentsLink, updateLink, deleteLink, searchLink);
    }
}