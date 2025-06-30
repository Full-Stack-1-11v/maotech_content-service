package cl.maotech.content_service.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase ContentNotFoundException.
 */
public class ContentNotFoundExceptionTests {

    @Test
    void constructor_conMensaje_debeAsignarMensaje() {
        // Given
        String expectedMessage = "Contenido no encontrado con ID: 123";

        // When
        ContentNotFoundException exception = new ContentNotFoundException(expectedMessage);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_conMensajeYCause_debeAsignarAmbos() {
        // Given
        String expectedMessage = "Error al buscar contenido";
        Throwable expectedCause = new RuntimeException("Causa raíz");

        // When
        ContentNotFoundException exception = new ContentNotFoundException(expectedMessage, expectedCause);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    @Test
    void constructor_soloConCause_debeAsignarCause() {
        // Given
        Throwable expectedCause = new IllegalArgumentException("Argumento inválido");

        // When
        ContentNotFoundException exception = new ContentNotFoundException(expectedCause);

        // Then
        assertEquals(expectedCause, exception.getCause());
        // El mensaje será el toString() de la causa
        assertTrue(exception.getMessage().contains("IllegalArgumentException"));
    }

    @Test
    void esInstanciaDeRuntimeException() {
        // Given
        ContentNotFoundException exception = new ContentNotFoundException("Test");

        // When & Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_conMensajeNull_debeAceptarNull() {
        // When
        ContentNotFoundException exception = new ContentNotFoundException((String) null);

        // Then
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_conCauseNull_debeAceptarNull() {
        // When
        ContentNotFoundException exception = new ContentNotFoundException((Throwable) null);

        // Then
        assertNull(exception.getCause());
    }

    @Test
    void constructor_conMensajeVacio_debeAceptarStringVacio() {
        // Given
        String emptyMessage = "";

        // When
        ContentNotFoundException exception = new ContentNotFoundException(emptyMessage);

        // Then
        assertEquals(emptyMessage, exception.getMessage());
    }

    @Test
    void herencia_debeSerRuntimeException() {
        // Given
        ContentNotFoundException exception = new ContentNotFoundException("Test message");

        // When & Then
        assertInstanceOf(RuntimeException.class, exception);
        assertInstanceOf(Exception.class, exception);
        assertInstanceOf(Throwable.class, exception);
    }

    @Test
    void stackTrace_debeEstarDisponible() {
        // Given
        ContentNotFoundException exception = new ContentNotFoundException("Test message");

        // When
        StackTraceElement[] stackTrace = exception.getStackTrace();

        // Then
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    void lanzarExcepcion_debeFuncionarCorrectamente() {
        // Given
        String message = "Contenido con ID 999 no encontrado";

        // When & Then
        assertThrows(ContentNotFoundException.class, () -> {
            throw new ContentNotFoundException(message);
        });
    }
}