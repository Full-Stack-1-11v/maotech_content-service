package cl.maotech.content_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.maotech.content_service.model.Example;
import cl.maotech.content_service.service.ExampleService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    private ExampleService exampleService;

    @GetMapping("/")
    public List<Example> getAllExamples() {
        return exampleService.getAllExamples();
    }

    @PostMapping("/")
    public void createExample(@RequestBody Example example) {
        exampleService.createExample(example);
    }

    @GetMapping("/{id}")
    public Example getExampleById(@PathVariable Long id) {
        return exampleService.getExampleById(id);
    }

    @PutMapping("/{id}")
    public void updateExample(@PathVariable Long id, @RequestBody Example example) {
        exampleService.updateExample(id, example);
    }

    @DeleteMapping("/{id}")
    public void deleteExample(@PathVariable Long id) {
        exampleService.deleteExample(id);
    }
    
}
