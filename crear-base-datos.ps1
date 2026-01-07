# Script para recrear la base de datos encuestas_db en PostgreSQL
Write-Host "Recreando base de datos encuestas_db..." -ForegroundColor Green

$env:PGPASSWORD = "arthas123"
$dbName = "encuestas_db"

$commands = @(
    # Terminar conexiones existentes
    "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$dbName' AND pid <> pg_backend_pid();",
    # Dropear base de datos si existe
    "DROP DATABASE IF EXISTS $dbName;",
    # Crear base de datos
    "CREATE DATABASE $dbName;"
)

# Setup command to run with psql.exe
$psqlPaths = @(
    "C:\Program Files\PostgreSQL\18\bin\psql.exe",
    "C:\Program Files\PostgreSQL\16\bin\psql.exe",
    "C:\Program Files\PostgreSQL\15\bin\psql.exe",
    "C:\Program Files\PostgreSQL\14\bin\psql.exe",
    "C:\Program Files\PostgreSQL\13\bin\psql.exe"
)

$psqlExe = $null
foreach ($path in $psqlPaths) {
    if (Test-Path $path) {
        $psqlExe = $path
        break
    }
}

if ($null -eq $psqlExe) {
    Write-Host "No se encontró psql.exe en las rutas estándar." -ForegroundColor Red
    exit 1
}

Write-Host "Usando psql: $psqlExe" -ForegroundColor Cyan

foreach ($cmd in $commands) {
    Write-Host "Ejecutando: $cmd" -ForegroundColor Gray
    & $psqlExe -U postgres -h localhost -c $cmd 2>&1
}

# Habilitar pgcrypto en la nueva base de datos
Write-Host "Habilitando pgcrypto..." -ForegroundColor Gray
& $psqlExe -U postgres -h localhost -d $dbName -c "CREATE EXTENSION IF NOT EXISTS pgcrypto;" 2>&1

Write-Host "Base de datos recreada exitosamente!" -ForegroundColor Green

