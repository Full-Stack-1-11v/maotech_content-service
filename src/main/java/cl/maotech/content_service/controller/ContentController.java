package cl.maotech.content_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.maotech.content_service.exception.ContentErrorResponse;
import cl.maotech.content_service.exception.ContentNotFoundException;
import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para gestionar las operaciones relacionadas con el contenido.
 * 
 * <p>Este controlador proporciona endpoints para realizar operaciones CRUD
 * sobre el contenido del sistema, incluyendo la búsqueda por tipo y estado.</p>
 * 
 * <p>Utiliza el servicio {@link ContentService} para realizar las operaciones
 * de negocio y maneja las excepciones específicas del dominio de contenido.</p>
 * 
 * @author MaoTech Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/content")
@Tag(name = "Contenido", description = "Operaciones relacionadas con la gestión de contenido")
public class ContentController {

    /**
     * Logger de la clase para registrar eventos y errores.
     */
    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    /**
     * Servicio para gestionar las operaciones relacionadas con el contenido.
     */
    @Autowired
    private ContentService contentService;

    /**
     * Obtiene una lista de todos los contenidos registrados en el sistema.
     * 
     * @return Lista de objetos {@link Content} con todos los contenidos.
     */
    @Operation(summary = "Listar todos los contenidos", 
               description = "Obtiene una lista con todos los contenidos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Lista de contenidos obtenida correctamente", 
                        content = @Content(mediaType = "application/json", 
                                         schema = @Schema(implementation = cl.maotech.content_service.model.Content.class)))
    })
    @GetMapping
    public List<cl.maotech.content_service.model.Content> getAllContents() {
        logger.info("Solicitud para obtener todos los contenidos");
        List<cl.maotech.content_service.model.Content> contents = contentService.getAllContents();
        logger.info("Se encontraron {} contenidos", contents.size());
        return contents;
    }

    /**
     * Crea un nuevo contenido en el sistema.
     * 
     * @param content Objeto {@link Content} con los datos del contenido a crear.
     * @return ResponseEntity con estado HTTP 201 (Created) si la operación es exitosa.
     */
    @Operation(summary = "Crear nuevo contenido", 
               description = "Crea un nuevo contenido en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", 
                        description = "Contenido creado exitosamente"),
            @ApiResponse(responseCode = "400", 
                        description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Void> createContent(@RequestBody cl.maotech.content_service.model.Content content) {
        logger.info("Solicitud para crear contenido: {}", content.getTitle());
        contentService.createContent(content);
        logger.info("Contenido creado exitosamente con título: {}", content.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Obtiene un contenido específico por su ID.
     * 
     * @param id ID del contenido a buscar.
     * @return Objeto {@link Content} con los datos del contenido encontrado.
     * @throws ContentNotFoundException si no se encuentra un contenido con el ID especificado.
     */
    @Operation(summary = "Obtener contenido por ID", 
               description = "Obtiene un contenido específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Contenido encontrado", 
                        content = @Content(mediaType = "application/json", 
                                         schema = @Schema(implementation = cl.maotech.content_service.model.Content.class))),
            @ApiResponse(responseCode = "404", 
                        description = "Contenido no encontrado")
    })
    @GetMapping("/{id}")
    public cl.maotech.content_service.model.Content getContentById(@PathVariable Long id) {
        logger.info("Solicitud para obtener contenido con ID: {}", id);
        cl.maotech.content_service.model.Content content = contentService.getContentById(id);
        if (content == null) {
            logger.warn("No se encontró contenido con ID: {}", id);
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        logger.info("Contenido encontrado: {}", content.getTitle());
        return content;
    }

    /**
     * Actualiza un contenido existente.
     * 
     * @param id ID del contenido a actualizar.
     * @param content Objeto {@link Content} con los nuevos datos del contenido.
     * @throws ContentNotFoundException si no se encuentra un contenido con el ID especificado.
     */
    @Operation(summary = "Actualizar contenido", 
               description = "Actualiza un contenido existente según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Contenido actualizado exitosamente"),
            @ApiResponse(responseCode = "404", 
                        description = "Contenido no encontrado")
    })
    @PutMapping("/{id}")
    public void updateContent(@PathVariable Long id, @RequestBody cl.maotech.content_service.model.Content content) {
        logger.info("Solicitud para actualizar contenido con ID: {}", id);
        cl.maotech.content_service.model.Content contentToEdit = contentService.getContentById(id);
        if (contentToEdit == null) {
            logger.warn("No se encontró contenido con ID: {} para actualizar", id);
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        contentService.updateContent(id, content);
        logger.info("Contenido con ID: {} actualizado exitosamente", id);
    }

    /**
     * Elimina un contenido del sistema.
     * 
     * @param id ID del contenido a eliminar.
     * @throws ContentNotFoundException si no se encuentra un contenido con el ID especificado.
     */
    @Operation(summary = "Eliminar contenido", 
               description = "Elimina un contenido del sistema según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Contenido eliminado exitosamente"),
            @ApiResponse(responseCode = "404", 
                        description = "Contenido no encontrado")
    })
    @DeleteMapping("/{id}")
    public void deleteContent(@PathVariable Long id) {
        logger.info("Solicitud para eliminar contenido con ID: {}", id);
        cl.maotech.content_service.model.Content content = contentService.getContentById(id);
        if (content == null) {
            logger.warn("No se encontró contenido con ID: {} para eliminar", id);
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        contentService.deleteContent(id);
        logger.info("Contenido con ID: {} eliminado exitosamente", id);
    }

    /**
     * Busca contenidos por tipo y estado específicos.
     * 
     * @param type Tipo de contenido a buscar (ej: "pdf", "video", "pptx").
     * @param status Estado del contenido a buscar (ej: "active", "inactive").
     * @return Lista de objetos {@link Content} que coinciden con los criterios de búsqueda.
     */
    @Operation(summary = "Buscar contenido por tipo y estado", 
               description = "Obtiene una lista de contenidos filtrados por tipo y estado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", 
                        description = "Contenidos encontrados", 
                        content = @Content(mediaType = "application/json", 
                                         schema = @Schema(implementation = cl.maotech.content_service.model.Content.class)))
    })
    @GetMapping("/search")
    public List<cl.maotech.content_service.model.Content> getContentByTypeAndStatus(
            @RequestParam String type,
            @RequestParam String status) {
        logger.info("Solicitud para buscar contenidos - Tipo: {}, Estado: {}", type, status);
        List<cl.maotech.content_service.model.Content> contents = contentService.getContentByTypeAndStatus(type, status);
        logger.info("Se encontraron {} contenidos con tipo: {} y estado: {}", contents.size(), type, status);
        return contents;
    }

    /**
     * Maneja las excepciones de tipo ContentNotFoundException.
     * 
     * @param ex La excepción de contenido no encontrado.
     * @return ResponseEntity con el error y estado HTTP 404 (Not Found).
     */
    @ExceptionHandler
    public ResponseEntity<ContentErrorResponse> handleException(ContentNotFoundException ex) {
        logger.error("Excepción de contenido no encontrado: {}", ex.getMessage());
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja las excepciones generales del sistema.
     * 
     * @param ex La excepción general.
     * @return ResponseEntity con el error y estado HTTP 400 (Bad Request).
     */
    @ExceptionHandler
    public ResponseEntity<ContentErrorResponse> handleException(Exception ex) {
        logger.error("Excepción general: {}", ex.getMessage());
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}