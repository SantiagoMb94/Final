# Guía de Instalación - Sistema de Gestión de Encuestas

Esta guía te ayudará a instalar todas las dependencias necesarias para ejecutar el sistema.

## Requisitos Previos

1. **Java JDK 17 o superior**
2. **PostgreSQL 12 o superior**
3. **Maven** (opcional, se incluye wrapper)

## Paso 1: Instalar Java JDK 17

### Opción A: Descarga Manual

1. Ve a [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) o [OpenJDK 17](https://adoptium.net/temurin/releases/?version=17)
2. Descarga el instalador para Windows (x64)
3. Ejecuta el instalador y sigue las instrucciones
4. **Importante**: Durante la instalación, marca la opción "Add to PATH" o configúralo manualmente

### Opción B: Usando Chocolatey (Recomendado)

Si tienes Chocolatey instalado:

```powershell
choco install openjdk17
```

### Verificar Instalación

Abre PowerShell y ejecuta:

```powershell
java -version
```

Deberías ver algo como:
```
openjdk version "17.0.x" 2023-xx-xx
```

### Configurar JAVA_HOME (si es necesario)

1. Busca "Variables de entorno" en el menú de Windows
2. Clic en "Variables de entorno"
3. En "Variables del sistema", haz clic en "Nueva"
4. Nombre: `JAVA_HOME`
5. Valor: Ruta donde instalaste Java (ej: `C:\Program Files\Java\jdk-17`)
6. Acepta y cierra todas las ventanas
7. Reinicia PowerShell

## Paso 2: Instalar PostgreSQL

### Opción A: Descarga Manual

1. Ve a [PostgreSQL Downloads](https://www.postgresql.org/download/windows/)
2. Descarga el instalador de PostgreSQL
3. Ejecuta el instalador
4. Durante la instalación:
   - Anota la contraseña del usuario `postgres` (la necesitarás)
   - El puerto por defecto es `5432` (déjalo así)
   - Acepta todas las opciones por defecto

### Opción B: Usando Chocolatey

```powershell
choco install postgresql
```

### Verificar Instalación

Abre PowerShell y ejecuta:

```powershell
psql --version
```

## Paso 3: Crear la Base de Datos

1. Abre **pgAdmin** (viene con PostgreSQL) o usa la línea de comandos
2. Conéctate al servidor PostgreSQL (usuario: `postgres`, contraseña: la que configuraste)
3. Crea la base de datos:

```sql
CREATE DATABASE encuestas_db;
```

O desde la línea de comandos:

```powershell
psql -U postgres -c "CREATE DATABASE encuestas_db;"
```

## Paso 4: Configurar la Aplicación

1. Abre el archivo `src/main/resources/application.properties`
2. Ajusta las credenciales de PostgreSQL si es necesario:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/encuestas_db
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASEÑA_AQUI
```

## Paso 5: Ejecutar la Aplicación

### Usando el Maven Wrapper (Recomendado)

El proyecto incluye un wrapper de Maven, así que no necesitas instalar Maven:

```powershell
.\mvnw.cmd spring-boot:run
```

Si es la primera vez, el wrapper descargará Maven automáticamente.

### Usando Maven (si lo tienes instalado)

```powershell
mvn spring-boot:run
```

## Paso 6: Acceder a la Aplicación

Una vez que la aplicación esté ejecutándose, abre tu navegador en:

**http://localhost:8080**

Deberías ver la página principal del sistema.

## Solución de Problemas

### Error: "JAVA_HOME not found"

1. Verifica que Java esté instalado: `java -version`
2. Configura la variable de entorno `JAVA_HOME` (ver Paso 1)

### Error: "No se puede conectar a PostgreSQL"

1. Verifica que PostgreSQL esté ejecutándose
2. Verifica las credenciales en `application.properties`
3. Verifica que la base de datos `encuestas_db` exista

### Error: "Puerto 8080 ya está en uso"

1. Cierra otras aplicaciones que usen el puerto 8080
2. O cambia el puerto en `application.properties`:
   ```properties
   server.port=8081
   ```

### El wrapper no funciona

Si `mvnw.cmd` no funciona, puedes instalar Maven manualmente:

1. Descarga Maven de [Apache Maven](https://maven.apache.org/download.cgi)
2. Extrae el archivo ZIP
3. Agrega Maven al PATH o configura `MAVEN_HOME`

## Verificación Final

Si todo está correcto, deberías ver en la consola algo como:

```
Started EncuestasApplication in X.XXX seconds
```

Y al abrir `http://localhost:8080` en tu navegador, deberías ver la interfaz del sistema.

## Próximos Pasos

1. Crea tu primera empresa desde la interfaz web
2. Crea usuarios asociados a la empresa
3. Crea encuestas y agrega preguntas
4. Activa las encuestas
5. Responde las encuestas y visualiza los reportes

¡Listo! Tu sistema está funcionando. 🎉

