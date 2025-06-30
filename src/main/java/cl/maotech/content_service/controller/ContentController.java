package cl.maotech.content_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.maotech.content_service.exception.ContentErrorResponse;
import cl.maotech.content_service.exception.ContentNotFoundException;
import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.service.ContentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping
    public List<Content> getAllContents() {
        return contentService.getAllContents();
    }

    @PostMapping
    public ResponseEntity<Void> createContent(@RequestBody Content content) {
        contentService.createContent(content);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public Content getContentById(@PathVariable Long id) {
        Content content = contentService.getContentById(id);
        if (content == null) {
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        return content;
    }

    @PutMapping("/{id}")
    public void updateContent(@PathVariable Long id, @RequestBody Content content) {
        Content contentToEdit = contentService.getContentById(id);
        if (contentToEdit == null) {
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        contentService.updateContent(id, content);
    }

    @DeleteMapping("/{id}")
    public void deleteContent(@PathVariable Long id) {
        Content content = contentService.getContentById(id);
        if (content == null) {
            throw new ContentNotFoundException("Contenido no encontrado con la ID: " + id);
        }
        contentService.deleteContent(id);
    }

    @GetMapping("/search")
    public List<Content> getContentByTypeAndStatus(
            @RequestParam String type,
            @RequestParam String status) {
        return contentService.getContentByTypeAndStatus(type, status);
    }

    @ExceptionHandler
    public ResponseEntity<ContentErrorResponse> handleException(ContentNotFoundException ex) {
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ContentErrorResponse> handleException(Exception ex) {
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}