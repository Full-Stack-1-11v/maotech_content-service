package cl.maotech.content_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.maotech.content_service.model.Content;

import java.util.List;

/**
 * Repositorio para la gestión de datos de contenido.
 * 
 * <p>Esta interfaz extiende JpaRepository para proporcionar operaciones CRUD
 * básicas y define consultas personalizadas para el manejo de contenidos.</p>
 * 
 * <p>Spring Data JPA genera automáticamente la implementación de esta interfaz,
 * proporcionando métodos como findById, findAll, save, deleteById, etc.</p>
 * 
 * @author MaoTech Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    
    /**
     * Busca contenidos por tipo y estado específicos.
     * 
     * <p>Esta consulta personalizada utiliza JPQL para filtrar contenidos
     * que coincidan exactamente con el tipo y estado proporcionados.</p>
     * 
     * @param type El tipo de contenido a buscar (ej: "pdf", "video", "pptx").
     * @param status El estado del contenido a buscar (ej: "active", "inactive").
     * @return Lista de contenidos que coinciden con los criterios especificados.
     *         La lista puede estar vacía si no se encuentran coincidencias.
     */
    @Query("SELECT c FROM Content c WHERE c.type = :type AND c.status = :status")
    List<Content> findByTypeAndStatus(@Param("type") String type, @Param("status") String status);
    
    // Métodos heredados de JpaRepository<Content, Long>:
    // 
    // - Optional<Content> findById(Long id)
    //   Busca un contenido por su ID único.
    //
    // - List<Content> findAll()
    //   Obtiene todos los contenidos registrados.
    //
    // - Content save(Content content)
    //   Guarda un contenido nuevo o actualiza uno existente.
    //
    // - void deleteById(Long id)
    //   Elimina un contenido por su ID.
    //
    // - long count()
    //   Cuenta el número total de contenidos.
    //
    // - boolean existsById(Long id)
    //   Verifica si existe un contenido con el ID especificado.
    //
    // - void delete(Content content)
    //   Elimina el contenido especificado.
    //
    // - void deleteAll()
    //   Elimina todos los contenidos.
}