package cl.maotech.content_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.maotech.content_service.assemblers.ContentModelAssembler;
import cl.maotech.content_service.exception.ContentErrorResponse;
import cl.maotech.content_service.exception.ContentNotFoundException;
import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST versión 2 para gestionar las operaciones relacionadas con el contenido.
 * Incluye soporte para HATEOAS (Hypermedia as the Engine of Application State).
 * 
 * @author MaoTech Team
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping("/v2/content")
@Tag(name = "Contenido V2", description = "Operaciones relacionadas con la gestión de contenido con soporte HATEOAS")
public class ContentControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(ContentControllerV2.class);

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentModelAssembler assembler;

    /**
     * Obtiene una lista de todos los contenidos registrados en el sistema con enlaces HATEOAS.
     */
    @Operation(summary = "Listar todos los contenidos con HATEOAS", 
               description = "Obtiene una lista con todos los contenidos registrados en el sistema, incluyendo enlaces de navegación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Lista de contenidos obtenida correctamente", 
                        content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json", 
                            schema = @Schema(implementation = Content.class)))
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Content>>> getAllContents() {
        logger.info("Solicitud para obtener todos los contenidos con HATEOAS");
        
        List<EntityModel<Content>> contents = contentService.getAllContents().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Content>> collectionModel = CollectionModel.of(contents)
                .add(linkTo(methodOn(ContentControllerV2.class).getAllContents()).withSelfRel())
                .add(linkTo(methodOn(ContentControllerV2.class).createContent(new Content())).withRel("create").withType("POST"));
        
        logger.info("Se encontraron {} contenidos", contents.size());
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Crea un nuevo contenido en el sistema.
     */
    @Operation(summary = "Crear nuevo contenido", 
               description = "Crea un nuevo contenido en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contenido creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Content>> createContent(@RequestBody Content content) {
        logger.info("Solicitud para crear contenido: {}", content.getTitle());
        
        contentService.createContent(content);
        EntityModel<Content> contentModel = assembler.toModel(content);
        
        logger.info("Contenido creado exitosamente con título: {}", content.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(contentModel);
    }

    /**
     * Obtiene un contenido específico por su ID con enlaces HATEOAS.
     */
    @Operation(summary = "Obtener contenido por ID", 
               description = "Obtiene un contenido específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Contenido encontrado", 
                        content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json", 
                            schema = @Schema(implementation = Content.class))),
            @ApiResponse(responseCode = "404", 
                        description = "Contenido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Content>> getContentById(@PathVariable Long id) {
        logger.info("Solicitud para obtener contenido con ID: {}", id);
        
        Content content = contentService.getContentById(id);
        if (content == null) {
            logger.warn("No se encontró contenido con ID: {}", id);
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        
        EntityModel<Content> contentModel = assembler.toModel(content);
        logger.info("Contenido encontrado: {}", content.getTitle());
        return ResponseEntity.ok(contentModel);
    }

    /**
     * Actualiza completamente un contenido existente.
     */
    @Operation(summary = "Actualizar contenido", 
               description = "Actualiza un contenido existente según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Content>> updateContent(@PathVariable Long id, @RequestBody Content content) {
        logger.info("Solicitud para actualizar contenido con ID: {}", id);
        
        Content contentToEdit = contentService.getContentById(id);
        if (contentToEdit == null) {
            logger.warn("No se encontró contenido con ID: {} para actualizar", id);
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        
        contentService.updateContent(id, content);
        Content updatedContent = contentService.getContentById(id);
        EntityModel<Content> contentModel = assembler.toModel(updatedContent);
        
        logger.info("Contenido con ID: {} actualizado exitosamente", id);
        return ResponseEntity.ok(contentModel);
    }

    /**
     * Actualiza parcialmente un contenido existente.
     */
    @Operation(summary = "Actualizar contenido parcialmente", 
               description = "Actualiza parcialmente un contenido existente según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Content>> patchContent(@PathVariable Long id, @RequestBody Content content) {
        logger.info("Solicitud para actualizar parcialmente contenido con ID: {}", id);
        
        Content existingContent = contentService.getContentById(id);
        if (existingContent == null) {
            logger.warn("No se encontró contenido con ID: {} para actualizar", id);
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        
        // Actualización parcial - solo los campos no nulos
        if (content.getTitle() != null) {
            existingContent.setTitle(content.getTitle());
        }
        if (content.getDescription() != null) {
            existingContent.setDescription(content.getDescription());
        }
        if (content.getType() != null) {
            existingContent.setType(content.getType());
        }
        if (content.getStatus() != null) {
            existingContent.setStatus(content.getStatus());
        }
        
        contentService.updateContent(id, existingContent);
        EntityModel<Content> contentModel = assembler.toModel(existingContent);
        
        logger.info("Contenido con ID: {} actualizado parcialmente", id);
        return ResponseEntity.ok(contentModel);
    }

    /**
     * Elimina un contenido del sistema.
     */
    @Operation(summary = "Eliminar contenido", 
               description = "Elimina un contenido del sistema según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contenido eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        logger.info("Solicitud para eliminar contenido con ID: {}", id);
        
        Content content = contentService.getContentById(id);
        if (content == null) {
            logger.warn("No se encontró contenido con ID: {} para eliminar", id);
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        
        contentService.deleteContent(id);
        logger.info("Contenido con ID: {} eliminado exitosamente", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Busca contenidos por tipo y estado específicos con enlaces HATEOAS.
     */
    @Operation(summary = "Buscar contenido por tipo y estado", 
               description = "Obtiene una lista de contenidos filtrados por tipo y estado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Contenidos encontrados", 
                        content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json", 
                            schema = @Schema(implementation = Content.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<CollectionModel<EntityModel<Content>>> getContentByTypeAndStatus(
            @RequestParam String type,
            @RequestParam String status) {
        logger.info("Solicitud para buscar contenidos - Tipo: {}, Estado: {}", type, status);
        
        List<EntityModel<Content>> contents = contentService.getContentByTypeAndStatus(type, status).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Content>> collectionModel = CollectionModel.of(contents)
                .add(linkTo(methodOn(ContentControllerV2.class).getContentByTypeAndStatus(type, status)).withSelfRel())
                .add(linkTo(methodOn(ContentControllerV2.class).getAllContents()).withRel("all-contents"));
        
        logger.info("Se encontraron {} contenidos con tipo: {} y estado: {}", contents.size(), type, status);
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Busca contenidos por tipo específico con enlaces HATEOAS.
     */
    @Operation(summary = "Buscar contenido por tipo", 
               description = "Obtiene una lista de contenidos filtrados solo por tipo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenidos encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron contenidos del tipo especificado")
    })
    @GetMapping("/buscar/tipo/{type}")
    public ResponseEntity<CollectionModel<EntityModel<Content>>> getContentByType(@PathVariable String type) {
        logger.info("Solicitud para buscar contenidos por tipo: {}", type);
        
        List<Content> contents = contentService.getContentByTypeAndStatus(type, "active");
        if (contents.isEmpty()) {
            logger.warn("No se encontraron contenidos del tipo: {}", type);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        List<EntityModel<Content>> contentModels = contents.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Content>> collectionModel = CollectionModel.of(contentModels)
                .add(linkTo(methodOn(ContentControllerV2.class).getContentByType(type)).withSelfRel())
                .add(linkTo(methodOn(ContentControllerV2.class).getAllContents()).withRel("all-contents"));
        
        logger.info("Se encontraron {} contenidos del tipo: {}", contentModels.size(), type);
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Maneja las excepciones de tipo ContentNotFoundException.
     */
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ContentErrorResponse> handleContentNotFoundException(ContentNotFoundException ex) {
        logger.error("Excepción de contenido no encontrado: {}", ex.getMessage());
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja las excepciones generales del sistema.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ContentErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Excepción general: {}", ex.getMessage(), ex);
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}