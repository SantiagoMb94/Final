# Script para descargar el Maven Wrapper
Write-Host "Descargando Maven Wrapper..." -ForegroundColor Green

$wrapperDir = ".mvn\wrapper"
$wrapperJar = "$wrapperDir\maven-wrapper.jar"
$wrapperUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

# Crear directorio si no existe
if (-not (Test-Path $wrapperDir)) {
    New-Item -ItemType Directory -Path $wrapperDir -Force | Out-Null
}

# Descargar el wrapper
try {
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $wrapperUrl -OutFile $wrapperJar
    Write-Host "Maven Wrapper descargado exitosamente!" -ForegroundColor Green
    Write-Host "Ahora puedes ejecutar: .\mvnw.cmd spring-boot:run" -ForegroundColor Yellow
} catch {
    Write-Host "Error al descargar el wrapper: $_" -ForegroundColor Red
    Write-Host "Por favor, descarga manualmente desde: $wrapperUrl" -ForegroundColor Yellow
    Write-Host "Y colócalo en: $wrapperJar" -ForegroundColor Yellow
}

