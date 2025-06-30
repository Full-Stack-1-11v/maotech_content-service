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
public class ContentNotFoundException extends RuntimeException {

    /**
     * Crea una nueva excepción con el mensaje especificado.
     * 
     * @param message El mensaje de error que describe la causa de la excepción.
     */
    public ContentNotFoundException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción con el mensaje y la causa especificados.
     * 
     * @param message El mensaje de error que describe la causa de la excepción.
     * @param cause La causa raíz de la excepción (otra excepción que causó esta).
     */
    public ContentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Crea una nueva excepción con la causa especificada.
     * 
     * <p>El mensaje de esta excepción será el resultado de llamar
     * cause.toString() si cause no es null.</p>
     * 
     * @param cause La causa raíz de la excepción.
     */
    public ContentNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
