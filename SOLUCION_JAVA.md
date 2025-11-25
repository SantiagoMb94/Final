# Solución al Problema de Compatibilidad Java 25

## Problema
Java 25 es muy reciente y no es compatible con Lombok (herramienta que genera getters/setters automáticamente).

## Solución Recomendada: Instalar Java 17 o 21

### Opción A: Instalar Java 17 (LTS - Recomendado)

1. **Descargar Java 17:**
   - Ve a: https://adoptium.net/temurin/releases/?version=17
   - Descarga "JDK 17" para Windows x64
   - Instálalo (por ejemplo, en `C:\Program Files\Java\jdk-17`)

2. **Configurar JAVA_HOME:**
   ```powershell
   [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
   ```

3. **Reiniciar PowerShell** y verificar:
   ```powershell
   java -version
   ```

4. **Ejecutar la aplicación:**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

### Opción B: Usar Java 21 (LTS - También recomendado)

1. **Descargar Java 21:**
   - Ve a: https://adoptium.net/temurin/releases/?version=21
   - Descarga "JDK 21" para Windows x64
   - Instálalo

2. **Configurar JAVA_HOME** apuntando a Java 21

3. **Actualizar pom.xml** para usar Java 21:
   ```xml
   <java.version>21</java.version>
   <maven.compiler.source>21</maven.compiler.source>
   <maven.compiler.target>21</maven.compiler.target>
   ```

## Solución Temporal: Usar Java 17 en esta sesión

Si tienes Java 17 instalado en otra ubicación, puedes configurarlo temporalmente:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd spring-boot:run
```

## Verificar Versión de Java

```powershell
java -version
```

Deberías ver algo como:
```
openjdk version "17.0.x" 2023-xx-xx
```

## Nota

Java 25 es una versión muy nueva (release candidate) y muchas herramientas aún no la soportan completamente. Para proyectos de producción, se recomienda usar versiones LTS (Long Term Support):
- **Java 17** (LTS) - Recomendado
- **Java 21** (LTS) - También recomendado

