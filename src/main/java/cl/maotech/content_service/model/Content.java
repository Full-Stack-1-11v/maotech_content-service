package cl.maotech.content_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidad que representa un contenido en el sistema.
 * 
 * <p>Esta clase define la estructura de un contenido que puede ser de diferentes tipos
 * (PDF, video, presentación, etc.) y tener diferentes estados (activo, inactivo).</p>
 * 
 * <p>La entidad se mapea a la tabla 'content' en la base de datos y utiliza
 * generación automática de IDs mediante estrategia IDENTITY.</p>
 * 
 * @author MaoTech Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
public class Content {

    /**
     * Identificador único del contenido.
     * Se genera automáticamente usando la estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título del contenido.
     * Campo obligatorio que describe brevemente el contenido.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Descripción detallada del contenido.
     * Campo obligatorio que proporciona información adicional sobre el contenido.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Tipo de contenido.
     * Campo obligatorio que especifica el formato del contenido (ej: "pdf", "video", "pptx").
     */
    @Column(nullable = false)
    private String type;

    /**
     * Estado del contenido.
     * Campo obligatorio que indica si el contenido está "active" o "inactive".
     */
    @Column(nullable = false)
    private String status;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Content() {
    }

    /**
     * Constructor con parámetros para crear un nuevo contenido.
     * 
     * @param title Título del contenido. No debe ser null ni vacío.
     * @param description Descripción del contenido. No debe ser null ni vacía.
     * @param type Tipo de contenido. No debe ser null ni vacío.
     * @param status Estado del contenido. No debe ser null ni vacío.
     */
    public Content(String title, String description, String type, String status) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
    }

    /**
     * Obtiene el ID del contenido.
     * 
     * @return El ID único del contenido.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del contenido.
     * 
     * @param id El ID único del contenido.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título del contenido.
     * 
     * @return El título del contenido.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del contenido.
     * 
     * @param title El título del contenido. No debe ser null ni vacío.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción del contenido.
     * 
     * @return La descripción del contenido.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del contenido.
     * 
     * @param description La descripción del contenido. No debe ser null ni vacía.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el tipo del contenido.
     * 
     * @return El tipo del contenido (ej: "pdf", "video", "pptx").
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo del contenido.
     * 
     * @param type El tipo del contenido. No debe ser null ni vacío.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene el estado del contenido.
     * 
     * @return El estado del contenido ("active" o "inactive").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Establece el estado del contenido.
     * 
     * @param status El estado del contenido. No debe ser null ni vacío.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}