package cl.maotech.content_service.exception;

public class ContentNotFoundException extends RuntimeException {

    public ContentNotFoundException(String message) {
        super(message);
    }

    public ContentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
