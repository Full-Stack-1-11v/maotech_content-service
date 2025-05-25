package cl.maotech.content_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.maotech.content_service.model.Content;
import cl.maotech.content_service.repository.ContentRepository;

@Service
public class ContentService {

    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public void createContent(Content content) {
        contentRepository.save(content);
    }

    public Content getContentById(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    public void updateContent(Long id, Content content) {
        Content existingContent = contentRepository.findById(id).orElse(null);
        if (existingContent != null) {
            existingContent.setTitle(content.getTitle());
            existingContent.setDescription(content.getDescription());
            existingContent.setType(content.getType());
            existingContent.setStatus(content.getStatus());
            contentRepository.save(existingContent);
        }
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    public List<Content> getContentByTypeAndStatus(String type, String status) {
        // Implementation using custom query
        return contentRepository.findByTypeAndStatus(type, status);
    }
}