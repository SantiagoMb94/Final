# Guía Completa de Uso - Sistema de Gestión de Encuestas

## 🚀 Inicio Rápido

### 1. Iniciar la Aplicación

Abre PowerShell en la carpeta del proyecto y ejecuta:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
.\mvnw.cmd spring-boot:run
```

**Nota:** La primera vez puede tardar 1-2 minutos mientras descarga las dependencias.

Espera a ver el mensaje:
```
Started EncuestasApplication in X.XXX seconds
```

### 2. Acceder a la Aplicación

Abre tu navegador en: **http://localhost:8081**

> ⚠️ **Importante:** La aplicación ahora corre en el puerto **8081** (no 8080) para evitar conflictos con EDB Postgres.

---

## 📋 Flujo de Trabajo Completo

### Paso 1: Crear una Empresa

1. En el menú, haz clic en **"Empresas"**
2. Haz clic en **"+ Nueva Empresa"**
3. Completa el formulario:
   - **Nombre***: Nombre de la empresa (obligatorio)
   - **NIT**: Número de identificación tributaria
   - **Dirección**: Dirección de la empresa
   - **Teléfono**: Número de contacto
   - **Email**: Correo electrónico
   - **Estado**: Activa/Inactiva
4. Haz clic en **"Guardar"**

**Ejemplo:**
- Nombre: "Mi Empresa S.A."
- NIT: "900123456-7"
- Email: "contacto@miempresa.com"

---

### Paso 2: Crear Usuarios

1. En el menú, haz clic en **"Usuarios"**
2. Haz clic en **"+ Nuevo Usuario"**
3. Completa el formulario:
   - **Nombre***: Nombre del usuario
   - **Apellido***: Apellido del usuario
   - **Email***: Correo electrónico (debe ser único)
   - **Contraseña***: Mínimo 6 caracteres
   - **Rol**: Usuario o Administrador
   - **Empresa***: Selecciona la empresa creada
   - **Estado**: Activo/Inactivo
4. Haz clic en **"Guardar"**

**Ejemplo:**
- Nombre: "Juan"
- Apellido: "Pérez"
- Email: "juan.perez@miempresa.com"
- Contraseña: "password123"
- Rol: Administrador
- Empresa: Mi Empresa S.A.

---

### Paso 3: Crear una Encuesta

1. En el menú, haz clic en **"Encuestas"**
2. Haz clic en **"+ Nueva Encuesta"**
3. Completa el formulario:
   - **Título***: Título de la encuesta
   - **Descripción**: Descripción detallada
   - **Fecha de Inicio**: Fecha y hora de inicio (opcional)
   - **Fecha de Fin**: Fecha y hora de finalización (opcional)
   - **Estado**: Borrador/Activa/Finalizada
   - **Empresa***: Selecciona la empresa
4. Haz clic en **"Guardar"**

**Ejemplo:**
- Título: "Encuesta de Satisfacción del Cliente"
- Descripción: "Encuesta para medir el nivel de satisfacción de nuestros clientes"
- Estado: Borrador
- Empresa: Mi Empresa S.A.

---

### Paso 4: Agregar Preguntas a la Encuesta

1. En la lista de encuestas, encuentra tu encuesta
2. Haz clic en el botón **"Preguntas"** de la encuesta
3. Haz clic en **"+ Nueva Pregunta"**
4. Completa el formulario:
   - **Texto de la Pregunta***: La pregunta completa
   - **Tipo***: 
     - **Opción Múltiple**: Para preguntas con opciones predefinidas
     - **Texto Libre**: Para respuestas abiertas
     - **Sí/No**: Para preguntas de respuesta binaria
     - **Escala**: Para preguntas con escala numérica
   - **Obligatoria**: Marca si la pregunta es obligatoria
   - **Orden**: Número de orden (opcional)
5. Si elegiste **"Opción Múltiple"** o **"Escala"**:
   - Haz clic en **"+ Agregar Opción"**
   - Escribe el texto de cada opción
   - Define el orden de cada opción
6. Haz clic en **"Guardar"**

**Ejemplo de Pregunta con Opciones:**
- Texto: "¿Cómo calificaría nuestro servicio?"
- Tipo: Opción Múltiple
- Opciones:
  1. Excelente
  2. Muy Bueno
  3. Bueno
  4. Regular
  5. Malo

**Ejemplo de Pregunta de Texto Libre:**
- Texto: "¿Qué sugerencias tiene para mejorar nuestro servicio?"
- Tipo: Texto Libre
- Obligatoria: No

---

### Paso 5: Activar la Encuesta

1. En la lista de encuestas, encuentra tu encuesta
2. Verifica que tenga preguntas agregadas
3. Haz clic en el botón **"Activar"**
4. El estado cambiará a **"Activa"**

> ⚠️ **Importante:** Solo las encuestas con estado "Activa" pueden ser respondidas.

---

### Paso 6: Responder la Encuesta

1. En el menú, haz clic en **"Responder"**
2. Selecciona la encuesta activa del menú desplegable
3. Completa todas las preguntas obligatorias
4. Ingresa tu identificador (email o ID)
5. Haz clic en **"Enviar Respuestas"**

**Tipos de Respuestas:**
- **Opción Múltiple**: Selecciona una opción con el botón de radio
- **Sí/No**: Selecciona Sí o No
- **Texto Libre**: Escribe tu respuesta en el cuadro de texto
- **Escala**: Selecciona una opción de la escala

---

### Paso 7: Ver Reportes

1. En el menú, haz clic en **"Reportes"**
2. Selecciona el tipo de reporte:
   - **Reporte por Encuesta**: Estadísticas detalladas de una encuesta
   - **Reporte por Empresa**: Resumen de todas las encuestas de una empresa
3. Selecciona la encuesta o empresa
4. Haz clic en **"Generar Reporte"**

**El reporte mostrará:**
- Total de respondentes
- Estadísticas por pregunta
- Distribución de respuestas (para opción múltiple)
- Porcentajes y cantidades

---

## 🎯 Caso de Uso Completo: Ejemplo Práctico

### Escenario: Encuesta de Satisfacción del Cliente

1. **Crear Empresa:**
   - Nombre: "Tienda Online S.A."
   - NIT: "900111222-3"

2. **Crear Usuario Administrador:**
   - Nombre: "María"
   - Email: "maria@tiendaonline.com"
   - Rol: Administrador

3. **Crear Encuesta:**
   - Título: "Satisfacción del Cliente 2024"
   - Descripción: "Ayúdanos a mejorar nuestros servicios"

4. **Agregar Preguntas:**
   - Pregunta 1: "¿Cómo calificaría nuestro servicio?" (Opción Múltiple)
     - Opciones: Excelente, Muy Bueno, Bueno, Regular, Malo
   - Pregunta 2: "¿Recomendaría nuestros servicios?" (Sí/No)
   - Pregunta 3: "Comentarios adicionales" (Texto Libre)

5. **Activar Encuesta:**
   - Cambiar estado a "Activa"

6. **Responder Encuesta:**
   - Acceder desde "Responder"
   - Completar todas las preguntas
   - Enviar respuestas

7. **Ver Reportes:**
   - Generar reporte de la encuesta
   - Ver estadísticas y porcentajes

---

## 🔧 Funcionalidades Adicionales

### Editar y Eliminar

- **Editar**: Haz clic en el botón "Editar" de cualquier elemento
- **Eliminar**: Haz clic en "Eliminar" (se pedirá confirmación)

### Filtros

- **Usuarios**: Filtra por empresa
- **Encuestas**: Filtra por empresa y estado

### Estados de Encuesta

- **Borrador**: Encuesta en creación, no puede ser respondida
- **Activa**: Encuesta disponible para responder
- **Finalizada**: Encuesta cerrada, no acepta más respuestas

---

## ⚠️ Solución de Problemas

### La aplicación no inicia

1. Verifica que PostgreSQL esté ejecutándose
2. Verifica que la base de datos `encuestas_db` exista
3. Verifica las credenciales en `application.properties`

### Error de conexión a la base de datos

1. Verifica que PostgreSQL esté corriendo
2. Verifica usuario y contraseña en `application.properties`
3. Verifica que la base de datos exista:
   ```sql
   CREATE DATABASE encuestas_db;
   ```

### No puedo ver las páginas

1. Verifica que la aplicación esté corriendo (puerto 8081)
2. Verifica la URL: `http://localhost:8081`
3. Limpia la caché del navegador

### Las respuestas no se guardan

1. Verifica que la encuesta esté en estado "Activa"
2. Verifica que todas las preguntas obligatorias estén respondidas
3. Verifica la conexión a la base de datos

---

## 📝 Notas Importantes

- **Primera ejecución**: La aplicación creará automáticamente las tablas en la base de datos
- **Puerto**: La aplicación corre en el puerto **8081** (no 8080)
- **Base de datos**: Asegúrate de que PostgreSQL esté ejecutándose
- **JAVA_HOME**: Configúralo permanentemente para evitar problemas

---

## 🎉 ¡Listo!

Ahora tienes todo lo necesario para usar el sistema completo. Si tienes dudas, consulta esta guía o revisa los mensajes de error en la consola.

