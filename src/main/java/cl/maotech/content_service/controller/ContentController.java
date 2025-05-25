package cl.maotech.content_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.service.ContentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public void createContent(@RequestBody Content content) {
        contentService.createContent(content);
    }

    @GetMapping("/{id}")
    public Content getContentById(@PathVariable Long id) {
        return contentService.getContentById(id);
    }

    @PutMapping("/{id}")
    public void updateContent(@PathVariable Long id, @RequestBody Content content) {
        contentService.updateContent(id, content);
    }

    @DeleteMapping("/{id}")
    public void deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
    }

    @GetMapping("/search")
    public List<Content> getContentByTypeAndStatus(
            @RequestParam String type, 
            @RequestParam String status) {
        return contentService.getContentByTypeAndStatus(type, status);
    }
}