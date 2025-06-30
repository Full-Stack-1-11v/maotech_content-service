package cl.maotech.content_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.repository.ContentRepository;

/**
 * Servicio de negocio para la gestión de contenido.
 * 
 * <p>Esta clase implementa la lógica de negocio para las operaciones CRUD
 * sobre el contenido del sistema. Actúa como capa intermedia entre el
 * controlador y el repositorio.</p>
 * 
 * <p>Utiliza el repositorio {@link ContentRepository} para acceder a los
 * datos y proporciona métodos para crear, leer, actualizar y eliminar contenidos.</p>
 * 
 * @author MaoTech Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class ContentService {

    /**
     * Logger de la clase para registrar eventos y errores.
     */
    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);

    /**
     * Repositorio para acceder a los datos de contenido.
     */
    private final ContentRepository contentRepository;

    /**
     * Constructor del servicio de contenido.
     * 
     * @param contentRepository Repositorio para acceder a los datos de contenido.
     */
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    /**
     * Crea un nuevo contenido en el sistema.
     * 
     * <p>Este método guarda un nuevo contenido en la base de datos.
     * No valida duplicados, por lo que es responsabilidad del llamador
     * verificar que el contenido no exista previamente si es necesario.</p>
     * 
     * @param content El objeto {@link Content} a crear. No debe ser null.
     * @throws IllegalArgumentException si el contenido es null o tiene datos inválidos.
     */
    public void createContent(Content content) {
        logger.debug("Creando nuevo contenido: {}", content.getTitle());
        contentRepository.save(content);
        logger.info("Contenido creado exitosamente: {}", content.getTitle());
    }

    /**
     * Obtiene un contenido por su ID.
     * 
     * @param id El ID del contenido a buscar. Debe ser un valor positivo.
     * @return El objeto {@link Content} encontrado, o null si no existe.
     */
    public Content getContentById(Long id) {
        logger.debug("Buscando contenido con ID: {}", id);
        Content content = contentRepository.findById(id).orElse(null);
        if (content != null) {
            logger.debug("Contenido encontrado: {}", content.getTitle());
        } else {
            logger.debug("No se encontró contenido con ID: {}", id);
        }
        return content;
    }

    /**
     * Actualiza un contenido existente.
     * 
     * <p>Este método busca el contenido por ID y actualiza todos sus campos
     * con los valores proporcionados. Si el contenido no existe, no se
     * realiza ninguna operación.</p>
     * 
     * @param id El ID del contenido a actualizar.
     * @param content El objeto {@link Content} con los nuevos datos.
     * @return true si se actualizó el contenido, false si no se encontró.
     */
    public boolean updateContent(Long id, Content content) {
        logger.debug("Actualizando contenido con ID: {}", id);
        Content existingContent = contentRepository.findById(id).orElse(null);
        if (existingContent != null) {
            existingContent.setTitle(content.getTitle());
            existingContent.setDescription(content.getDescription());
            existingContent.setType(content.getType());
            existingContent.setStatus(content.getStatus());
            contentRepository.save(existingContent);
            logger.info("Contenido actualizado exitosamente: {}", existingContent.getTitle());
            return true;
        } else {
            logger.warn("No se pudo actualizar el contenido con ID: {} - no encontrado", id);
            return false;
        }
    }

    /**
     * Elimina un contenido del sistema por su ID.
     * 
     * <p>Este método elimina permanentemente el contenido de la base de datos.
     * La operación no se puede deshacer.</p>
     * 
     * @param id El ID del contenido a eliminar.
     */
    public void deleteContent(Long id) {
        logger.debug("Eliminando contenido con ID: {}", id);
        contentRepository.deleteById(id);
        logger.info("Contenido eliminado exitosamente con ID: {}", id);
    }

    /**
     * Obtiene todos los contenidos registrados en el sistema.
     * 
     * @return Lista de todos los objetos {@link Content}. Puede estar vacía si no hay contenidos.
     */
    public List<Content> getAllContents() {
        logger.debug("Obteniendo todos los contenidos");
        List<Content> contents = contentRepository.findAll();
        logger.info("Se obtuvieron {} contenidos", contents.size());
        return contents;
    }

    /**
     * Busca contenidos por tipo y estado específicos.
     * 
     * <p>Este método utiliza una consulta personalizada para filtrar
     * contenidos que coincidan exactamente con el tipo y estado proporcionados.</p>
     * 
     * @param type El tipo de contenido a buscar (ej: "pdf", "video", "pptx").
     * @param status El estado del contenido a buscar (ej: "active", "inactive").
     * @return Lista de objetos {@link Content} que coinciden con los criterios.
     *         La lista puede estar vacía si no se encuentran coincidencias.
     */
    public List<Content> getContentByTypeAndStatus(String type, String status) {
        logger.debug("Buscando contenidos por tipo: {} y estado: {}", type, status);
        List<Content> contents = contentRepository.findByTypeAndStatus(type, status);
        logger.info("Se encontraron {} contenidos con tipo: {} y estado: {}", contents.size(), type, status);
        return contents;
    }
}