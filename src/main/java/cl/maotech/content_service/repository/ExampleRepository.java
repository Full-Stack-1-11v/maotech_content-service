package cl.maotech.content_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.maotech.content_service.model.Example;

public interface ExampleRepository extends JpaRepository<Example, Long> {
}
