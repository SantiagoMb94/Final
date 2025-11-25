# Sistema de Gestión de Encuestas Empresariales

Sistema web para la gestión de encuestas empresariales que permite a las empresas crear, distribuir y analizar encuestas para sus colaboradores, clientes o usuarios.

## Características Principales

- **Registro de empresas**: Gestión de información de empresas
- **Gestión de usuarios autorizados**: Administración de usuarios del sistema
- **Creación y administración de encuestas**: Crear y gestionar encuestas
- **Registro de preguntas y opciones**: Configurar preguntas con diferentes tipos y opciones
- **Captura y almacenamiento de respuestas**: Almacenar respuestas de los usuarios
- **Generación de reportes básicos**: Reportes estadísticos de las encuestas
- **Interfaz web moderna**: Frontend completo con diseño responsivo

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**

## Arquitectura

El sistema está estructurado en capas siguiendo el patrón de arquitectura en capas:

- **Capa de Presentación**: Controladores REST (`presentacion.controlador`)
- **Capa de Negocio**: Servicios de negocio (`negocio.servicio`)
- **Capa de Datos**: Repositorios JPA (`datos.repositorio`)
- **Modelo**: Entidades JPA (`modelo`)

## Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- PostgreSQL 12 o superior

## Configuración

### 1. Base de Datos

Crear la base de datos PostgreSQL:

```sql
CREATE DATABASE encuestas_db;
```

### 2. Configuración de la Aplicación

Editar el archivo `src/main/resources/application.properties` con las credenciales de tu base de datos:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/encuestas_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 3. Ejecutar la Aplicación

**Opción 1: Usando el Maven Wrapper (Recomendado - No requiere instalar Maven)**

```bash
.\mvnw.cmd spring-boot:run
```

**Opción 2: Si tienes Maven instalado**

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

> **Nota**: Si no tienes Java o Maven instalados, consulta el archivo `INSTALACION.md` para instrucciones detalladas de instalación.

### 4. Acceder a la Interfaz Web

Una vez iniciada la aplicación, puedes acceder a la interfaz web en:

- **Página Principal**: `http://localhost:8080/index.html`
- **Gestión de Empresas**: `http://localhost:8080/empresas.html`
- **Gestión de Usuarios**: `http://localhost:8080/usuarios.html`
- **Gestión de Encuestas**: `http://localhost:8080/encuestas.html`
- **Responder Encuestas**: `http://localhost:8080/responder.html`
- **Reportes**: `http://localhost:8080/reportes.html`

## API REST

### Empresas

- `GET /api/empresas` - Listar todas las empresas
- `GET /api/empresas/{id}` - Obtener empresa por ID
- `POST /api/empresas` - Crear nueva empresa
- `PUT /api/empresas/{id}` - Actualizar empresa
- `DELETE /api/empresas/{id}` - Eliminar empresa
- `PUT /api/empresas/{id}/desactivar` - Desactivar empresa

### Usuarios

- `GET /api/usuarios` - Listar todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario
- `PUT /api/usuarios/{id}/desactivar` - Desactivar usuario

### Encuestas

- `GET /api/encuestas` - Listar todas las encuestas
- `GET /api/encuestas/{id}` - Obtener encuesta por ID
- `POST /api/encuestas` - Crear nueva encuesta
- `PUT /api/encuestas/{id}` - Actualizar encuesta
- `DELETE /api/encuestas/{id}` - Eliminar encuesta
- `PUT /api/encuestas/{id}/activar` - Activar encuesta
- `PUT /api/encuestas/{id}/finalizar` - Finalizar encuesta

### Preguntas

- `GET /api/preguntas` - Listar todas las preguntas
- `GET /api/preguntas/{id}` - Obtener pregunta por ID
- `POST /api/preguntas` - Crear nueva pregunta
- `PUT /api/preguntas/{id}` - Actualizar pregunta
- `DELETE /api/preguntas/{id}` - Eliminar pregunta

### Opciones

- `GET /api/opciones` - Listar todas las opciones
- `GET /api/opciones/{id}` - Obtener opción por ID
- `POST /api/opciones` - Crear nueva opción
- `PUT /api/opciones/{id}` - Actualizar opción
- `DELETE /api/opciones/{id}` - Eliminar opción

### Respuestas

- `GET /api/respuestas` - Listar todas las respuestas
- `GET /api/respuestas/{id}` - Obtener respuesta por ID
- `POST /api/respuestas` - Crear nueva respuesta
- `PUT /api/respuestas/{id}` - Actualizar respuesta
- `DELETE /api/respuestas/{id}` - Eliminar respuesta

### Reportes

- `GET /api/reportes/encuestas/{encuestaId}` - Generar reporte básico de una encuesta
- `GET /api/reportes/empresas/{empresaId}` - Generar reporte por empresa

## Modelo de Datos

El sistema incluye las siguientes entidades principales:

- **Empresa**: Información de las empresas
- **Usuario**: Usuarios autorizados del sistema
- **Encuesta**: Encuestas creadas
- **Pregunta**: Preguntas de las encuestas
- **Opcion**: Opciones para preguntas de opción múltiple
- **Respuesta**: Respuestas de los usuarios

## Ejemplos de Uso

### Crear una Empresa

```bash
curl -X POST http://localhost:8080/api/empresas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Mi Empresa S.A.",
    "nit": "900123456-7",
    "direccion": "Calle 123 #45-67",
    "telefono": "6012345678",
    "email": "contacto@miempresa.com"
  }'
```

### Crear una Encuesta

```bash
curl -X POST http://localhost:8080/api/encuestas \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Encuesta de Satisfacción",
    "descripcion": "Encuesta para medir la satisfacción de los clientes",
    "empresaId": 1
  }'
```

## Interfaz Web

El sistema incluye una interfaz web completa y moderna que permite:

- **Gestión visual de empresas**: Crear, editar, eliminar y listar empresas
- **Gestión de usuarios**: Administrar usuarios con filtros por empresa
- **Gestión de encuestas**: Crear encuestas y gestionar preguntas y opciones
- **Responder encuestas**: Interfaz intuitiva para responder encuestas activas
- **Visualización de reportes**: Reportes gráficos y estadísticos

### Características de la Interfaz

- Diseño moderno y responsivo
- Navegación intuitiva
- Validación de formularios
- Mensajes de confirmación y error
- Tablas interactivas
- Modales para crear/editar

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── co/edu/poli/encuestas/
│   │       ├── EncuestasApplication.java
│   │       ├── modelo/          # Entidades JPA
│   │       ├── datos/           # Repositorios
│   │       ├── negocio/         # Servicios
│   │       └── presentacion/    # Controladores y DTOs
│   └── resources/
│       ├── application.properties
│       ├── db/                  # Scripts SQL
│       └── static/              # Frontend
│           ├── css/             # Estilos
│           ├── js/              # JavaScript
│           └── *.html           # Páginas HTML
└── test/
```

## Licencia

Este proyecto es parte de un trabajo académico.

