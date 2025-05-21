package cl.maotech.content_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.maotech.content_service.model.Example;
import cl.maotech.content_service.repository.ExampleRepository;

@Service
public class ExampleImpl implements ExampleService {

    private final ExampleRepository exampleRepository;

    public ExampleImpl(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @Override
    public void createExample(Example example) {
        // Implementation for creating an example
        exampleRepository.save(example);
    }

    @Override
    public Example getExampleById(Long id) {
        // Implementation for getting an example by ID
        return exampleRepository.findById(id).orElse(null);}

    @Override
    public void updateExample(Long id, Example example) {
        // Implementation for updating an example
        Example existingExample = exampleRepository.findById(id).orElse(null);
        if (existingExample != null) {
            existingExample.setName(example.getName());
            existingExample.setDescription(example.getDescription());
            exampleRepository.save(existingExample);
        }
    }

    @Override
    public void deleteExample(Long id) {
        // Implementation for deleting an example
        exampleRepository.deleteById(id);
    }

    @Override
    public List<Example> getAllExamples() {
        // Implementation for getting all examples
        return exampleRepository.findAll();
    }
    
}
