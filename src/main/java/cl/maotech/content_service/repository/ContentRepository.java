package cl.maotech.content_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.maotech.content_service.model.Content;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    
    // Query personalizada: Buscar contenido por tipo y estado
    @Query("SELECT c FROM Content c WHERE c.type = :type AND c.status = :status")
    List<Content> findByTypeAndStatus(@Param("type") String type, @Param("status") String status);
    
    // Métodos que vienen automáticamente con JPA Repository:
    // - findById(Long id) - busca por ID
    // - findAll() - trae todos los registros
    // - save(Content content) - guarda o actualiza
    // - deleteById(Long id) - elimina por ID
    // - count() - cuenta registros
}