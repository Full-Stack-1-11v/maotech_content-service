# maotech_content-service


## Inicialización

1. Clonar el repositorio.
2. Levantar una base de datos MySQL.
3. Crear un archivo `.env` a partir del `.envexample` con las credenciales de la DB.
4. Ejecutar el proyecto de Spring Boot.
5. Probar con Postman, ejemplo:

```JSON
POST /example HTTP/1.1
Host: localhost:8090
Content-Type: application/json
Content-Length: 64

{
    "name": "Ejemplo",
    "description": "Texto de ejemplo"
}
```
## Listado de Endpoints

* Swagger: `http://localhost:8090/swagger-ui/index.html`
