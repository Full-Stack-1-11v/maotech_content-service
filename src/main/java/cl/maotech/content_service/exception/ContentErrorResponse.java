package cl.maotech.content_service.exception;

/**
 * Clase que representa la respuesta de error estándar para el API de contenidos.
 * 
 * <p>Esta clase se utiliza para estructurar las respuestas de error de manera
 * consistente en toda la aplicación. Contiene el código de estado HTTP y un
 * mensaje descriptivo del error.</p>
 * 
 * <p>Es utilizada por los manejadores de excepciones del controlador para
 * devolver errores en formato JSON con una estructura consistente, permitiendo
 * a los clientes de la API procesar los errores de manera uniforme.</p>
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
     * Constructor por defecto.
     * Crea una respuesta de error vacía.
     */
    public ContentErrorResponse() {
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
}