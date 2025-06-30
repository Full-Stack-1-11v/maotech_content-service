package cl.maotech.content_service.exception;

/**
 * Excepción personalizada que se lanza cuando no se encuentra un contenido solicitado.
 * 
 * <p>Esta excepción es una subclase de RuntimeException, lo que significa que es
 * una excepción no verificada (unchecked exception). Se utiliza específicamente
 * en el contexto del servicio de contenidos cuando se busca un contenido por ID
 * y no se encuentra en la base de datos.</p>
 * 
 * <p>La excepción puede ser capturada por los manejadores de excepciones del
 * controlador para devolver respuestas HTTP apropiadas (como 404 Not Found).</p>
 * 
 * @author MaoTech Team
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentErrorResponse {

    /**
     * Código de estado HTTP del error.
     * Representa el código de respuesta HTTP apropiado (ej: 404, 400, 500).
     */
    private int statusCode;
    /**
     * Mensaje descriptivo del error.
     * Proporciona información detallada sobre la causa del error.
     */
    private String message;
    
    /**
     * Obtiene el código de estado HTTP del error.
     * 
     * @return El código de estado HTTP.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Establece el código de estado HTTP del error.
     * 
     * @param statusCode El código de estado HTTP a establecer.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Obtiene el mensaje descriptivo del error.
     * 
     * @return El mensaje del error.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje descriptivo del error.
     * 
     * @param message El mensaje del error a establecer.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Constructor con parámetros para crear una respuesta de error completa.
     * 
     * @param statusCode El código de estado HTTP del error.
     * @param message El mensaje descriptivo del error.
     */
    public ContentErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

     /**
     * Constructor por defecto.
     * Crea una respuesta de error vacía.
     */
    public ContentErrorResponse() {
    }
    
}
