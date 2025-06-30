package cl.maotech.content_service.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase ContentErrorResponse.
 */
public class ContentErrorResponseTests {

    @Test
    void constructor_sinParametros_debeCrearObjetoVacio() {
        // When
        ContentErrorResponse errorResponse = new ContentErrorResponse();

        // Then
        assertEquals(0, errorResponse.getStatusCode());
        assertNull(errorResponse.getMessage());
    }

    @Test
    void constructor_conParametros_debeAsignarValores() {
        // Given
        int expectedStatusCode = 404;
        String expectedMessage = "Contenido no encontrado";

        // When
        ContentErrorResponse errorResponse = new ContentErrorResponse(expectedStatusCode, expectedMessage);

        // Then
        assertEquals(expectedStatusCode, errorResponse.getStatusCode());
        assertEquals(expectedMessage, errorResponse.getMessage());
    }

    @Test
    void setStatusCode_debeAsignarValor() {
        // Given
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        int expectedStatusCode = 400;

        // When
        errorResponse.setStatusCode(expectedStatusCode);

        // Then
        assertEquals(expectedStatusCode, errorResponse.getStatusCode());
    }

    @Test
    void setMessage_debeAsignarValor() {
        // Given
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        String expectedMessage = "Error de validación";

        // When
        errorResponse.setMessage(expectedMessage);

        // Then
        assertEquals(expectedMessage, errorResponse.getMessage());
    }

    @Test
    void getters_debeFuncionarCorrectamente() {
        // Given
        int statusCode = 500;
        String message = "Error interno del servidor";
        ContentErrorResponse errorResponse = new ContentErrorResponse(statusCode, message);

        // When & Then
        assertEquals(statusCode, errorResponse.getStatusCode());
        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    void setters_debeCambiarValoresExistentes() {
        // Given
        ContentErrorResponse errorResponse = new ContentErrorResponse(404, "No encontrado");
        int nuevoStatusCode = 400;
        String nuevoMessage = "Solicitud incorrecta";

        // When
        errorResponse.setStatusCode(nuevoStatusCode);
        errorResponse.setMessage(nuevoMessage);

        // Then
        assertEquals(nuevoStatusCode, errorResponse.getStatusCode());
        assertEquals(nuevoMessage, errorResponse.getMessage());
    }

    @Test
    void setMessage_conNull_debeAceptarNull() {
        // Given
        ContentErrorResponse errorResponse = new ContentErrorResponse();

        // When
        errorResponse.setMessage(null);

        // Then
        assertNull(errorResponse.getMessage());
    }

    @Test
    void setMessage_conStringVacio_debeAceptarStringVacio() {
        // Given
        ContentErrorResponse errorResponse = new ContentErrorResponse();
        String emptyMessage = "";

        // When
        errorResponse.setMessage(emptyMessage);

        // Then
        assertEquals(emptyMessage, errorResponse.getMessage());
    }
}