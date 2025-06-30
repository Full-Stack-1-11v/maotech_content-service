# MaoTech Content Service

Microservicio de gestión de contenido desarrollado con Spring Boot que permite realizar operaciones CRUD sobre contenidos del sistema.

## Características

- **API REST** con versionado (V1 y V2)
- **HATEOAS** - API autodescubrible con navegación hipermedia (V2)
- **Documentación Swagger/OpenAPI** integrada
- **Actualización parcial** con PATCH (V2)
- **Validación** de datos y manejo de excepciones

## Tecnologías

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- Spring HATEOAS
- MySQL / H2 (Testing)
- Maven

## Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repositorio>
   cd maotech_content-service
   ```

2. **Configurar base de datos**
   ```properties
   # .env
   DB_URL=jdbc:mysql://localhost:3306/content_service
   DB_USER=content_user
   DB_PASS=your_password
   ```

3. **Ejecutar**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

Servicio disponible en: `http://localhost:8084`

## API Endpoints

### V1 - REST Tradicional
Base URL: `/content`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/content` | Obtener todos los contenidos |
| `POST` | `/content` | Crear nuevo contenido |
| `GET` | `/content/{id}` | Obtener contenido por ID |
| `PUT` | `/content/{id}` | Actualizar contenido |
| `DELETE` | `/content/{id}` | Eliminar contenido |
| `GET` | `/content/search?type={type}&status={status}` | Buscar contenidos |

### V2 - REST con HATEOAS
Base URL: `/v2/content`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/v2/content` | Obtener todos los contenidos |
| `POST` | `/v2/content` | Crear nuevo contenido |
| `GET` | `/v2/content/{id}` | Obtener contenido por ID |
| `PUT` | `/v2/content/{id}` | Actualizar contenido |
| `PATCH` | `/v2/content/{id}` | Actualizar parcialmente |
| `DELETE` | `/v2/content/{id}` | Eliminar contenido |
| `GET` | `/v2/content/search?type={type}&status={status}` | Buscar contenidos |

## Documentación

- **Swagger UI**: `http://localhost:8084/doc/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8084/api-docs`

### Generar Javadocs
```bash
# Generar documentación API
mvn javadoc:javadoc

# Ver documentación generada
target/site/apidocs/index.html
```

## HATEOAS (V2)

La API V2 incluye enlaces de navegación automáticos:

```json
{
  "id": 1,
  "title": "Manual de Usuario",
  "type": "pdf",
  "status": "active",
  "_links": {
    "self": { "href": "http://localhost:8084/v2/content/1" },
    "contents": { "href": "http://localhost:8084/v2/content" },
    "update": { "href": "http://localhost:8084/v2/content/1" },
    "delete": { "href": "http://localhost:8084/v2/content/1" }
  }
}
```

## Ejemplo de Uso

### Crear contenido
```bash
curl -X POST http://localhost:8084/v2/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Manual de Usuario",
    "description": "Guía del sistema",
    "type": "pdf",
    "status": "active"
  }'
```

### Actualización parcial (V2)
```bash
curl -X PATCH http://localhost:8084/v2/content/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "Nuevo Título"}'
```

## Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Pruebas con cobertura
mvn clean test jacoco:report

# Ver reporte de cobertura
target/site/jacoco/index.html
```