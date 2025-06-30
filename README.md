# MaoTech Content Service

Servicio de gestión de contenido desarrollado con Spring Boot que permite realizar operaciones CRUD sobre contenidos del sistema, incluyendo búsquedas por tipo y estado.

## Características

- **API REST** completa para gestión de contenidos
- **Documentación Swagger/OpenAPI** integrada
- **Pruebas unitarias** con JUnit 5 y Mockito
- **Pruebas de integración** con H2 Database
- **Cobertura de código** con JaCoCo
- **Documentación Javadoc** completa
- **Logging** estructurado con SLF4J
- **Validación** de datos y manejo de excepciones

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **MySQL** (Producción)
- **H2 Database** (Testing)
- **Maven** (Gestión de dependencias)
- **JUnit 5** (Testing)
- **Mockito** (Mocking)
- **JaCoCo** (Cobertura de código)
- **Swagger/OpenAPI** (Documentación API)

## Inicialización

### Prerrequisitos

- Java 17 o superior
- Maven 3.6 o superior
- MySQL 8.0 o superior

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repositorio>
   cd maotech_content-service
   ```

2. **Configurar la base de datos MySQL**

3. **Crear archivo de configuración**
   
   Crear un archivo `.env` en la raíz del proyecto basado en `.env.example`:
   ```properties
   DB_URL=jdbc:mysql://localhost:3306/content_service
   DB_USER=content_user
   DB_PASS=your_password
   ```

4. **Compilar y ejecutar el proyecto**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Verificar que el servicio esté funcionando**
   
   El servicio estará disponible en: `http://localhost:8084`

## Ejecución de Pruebas

### Ejecutar todas las pruebas
```bash
mvn test
```

### Ejecutar pruebas con reporte de cobertura
```bash
mvn clean test jacoco:report
```

### Ver reporte de cobertura
Después de ejecutar las pruebas con JaCoCo, el reporte estará disponible en:
```
target/site/jacoco/index.html
```

### Ejecutar solo pruebas unitarias
```bash
mvn test -Dtest="*Test"
```

### Ejecutar solo pruebas de integración
```bash
mvn test -Dtest="*Tests"
```

### Ejecutar pruebas en modo verbose
```bash
mvn test -Dmaven.test.failure.ignore=true -Dsurefire.printSummary=true
```

## Generación de Documentación

### Generar Javadocs
```bash
mvn javadoc:javadoc
```

Los Javadocs se generarán en: `target/site/apidocs/index.html`

### Generar Javadocs como JAR
```bash
mvn javadoc:jar
```

### Generar documentación completa del sitio
```bash
mvn site
```

## Listado de Endpoints

La documentación completa de la API está disponible a través de Swagger UI:

**Swagger UI**: `http://localhost:8084/doc/swagger-ui.html`

**OpenAPI JSON**: `http://localhost:8084/api-docs`

### Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/content` | Obtener todos los contenidos |
| `POST` | `/content` | Crear un nuevo contenido |
| `GET` | `/content/{id}` | Obtener contenido por ID |
| `PUT` | `/content/{id}` | Actualizar contenido existente |
| `DELETE` | `/content/{id}` | Eliminar contenido por ID |
| `GET` | `/content/search?type={type}&status={status}` | Buscar contenidos por tipo y estado |

### Ejemplos de Uso

#### Crear un nuevo contenido
```json
POST /content HTTP/1.1
Host: localhost:8084
Content-Type: application/json

{
    "title": "Manual de Usuario",
    "description": "Guía completa para el uso del sistema",
    "type": "pdf",
    "status": "active"
}
```

#### Buscar contenidos por tipo y estado
```json
GET /content/search?type=pdf&status=active HTTP/1.1
Host: localhost:8084
```

#### Actualizar un contenido
```json
PUT /content/1 HTTP/1.1
Host: localhost:8084
Content-Type: application/json

{
    "title": "Manual de Usuario Actualizado",
    "description": "Versión actualizada de la guía",
    "type": "pdf",
    "status": "active"
}
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/cl/maotech/content_service/
│   │   ├── controller/         # Controladores REST
│   │   ├── service/           # Lógica de negocio
│   │   ├── repository/        # Acceso a datos
│   │   ├── model/            # Entidades JPA
│   │   ├── exception/        # Excepciones personalizadas
│   │   └── ContentServiceApplication.java
│   └── resources/
│       ├── application.yml    # Configuración principal
│       └── application-test.yml # Configuración para tests
└── test/
    └── java/cl/maotech/content_service/
        ├── controller/        # Tests de controladores
        ├── service/          # Tests de servicios
        ├── repository/       # Tests de repositorios
        └── exception/        # Tests de excepciones
```

## Configuración de Perfiles

### Perfil de Desarrollo (default)
Utiliza MySQL como base de datos principal.

### Perfil de Testing
```bash
mvn test -Dspring.profiles.active=test
```
Utiliza H2 en memoria para las pruebas.

## Comandos Maven Útiles

```bash
# Limpiar y compilar
mvn clean compile

# Ejecutar solo la aplicación
mvn spring-boot:run

# Crear JAR ejecutable
mvn clean package

# Verificar estilo de código y dependencias
mvn verify

# Generar reporte completo del proyecto
mvn clean install site
```

## Métricas de Cobertura

El proyecto incluye configuración de JaCoCo para medir la cobertura de código:

- **Objetivo mínimo**: 80% de cobertura de líneas
- **Reportes**: HTML, XML y CSV
- **Exclusiones**: Clases de configuración y DTOs simples

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request


## Contacto

**MaoTech Team**
- Documentación: http://localhost:8084/doc/swagger-ui.html