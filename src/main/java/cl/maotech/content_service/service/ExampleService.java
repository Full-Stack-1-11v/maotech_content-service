package cl.maotech.content_service.service;

import java.util.List;

import cl.maotech.content_service.model.Example;

public interface ExampleService {

    void createExample(Example example);

    Example getExampleById(Long id);

    void updateExample(Long id, Example example);

    void deleteExample(Long id);

    List<Example> getAllExamples();

}
