# Script para crear la base de datos encuestas_db en PostgreSQL
Write-Host "Creando base de datos encuestas_db..." -ForegroundColor Green

# Intentar crear la base de datos usando psql
$env:PGPASSWORD = "arthas123"
& "C:\Program Files\PostgreSQL\16\bin\psql.exe" -U postgres -h localhost -c "CREATE DATABASE encuestas_db;" 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "Base de datos creada exitosamente!" -ForegroundColor Green
} else {
    Write-Host "Intentando con ruta alternativa..." -ForegroundColor Yellow
    # Intentar con diferentes rutas comunes de PostgreSQL
    $psqlPaths = @(
        "C:\Program Files\PostgreSQL\15\bin\psql.exe",
        "C:\Program Files\PostgreSQL\14\bin\psql.exe",
        "C:\Program Files\PostgreSQL\13\bin\psql.exe",
        "C:\Program Files\PostgreSQL\12\bin\psql.exe"
    )
    
    $found = $false
    foreach ($path in $psqlPaths) {
        if (Test-Path $path) {
            Write-Host "Usando: $path" -ForegroundColor Cyan
            & $path -U postgres -h localhost -c "CREATE DATABASE encuestas_db;" 2>&1
            if ($LASTEXITCODE -eq 0) {
                Write-Host "Base de datos creada exitosamente!" -ForegroundColor Green
                $found = $true
                break
            }
        }
    }
    
    if (-not $found) {
        Write-Host "No se pudo encontrar psql.exe automáticamente." -ForegroundColor Red
        Write-Host "Por favor, crea la base de datos manualmente:" -ForegroundColor Yellow
        Write-Host "1. Abre pgAdmin o la línea de comandos de PostgreSQL" -ForegroundColor Yellow
        Write-Host "2. Ejecuta: CREATE DATABASE encuestas_db;" -ForegroundColor Yellow
    }
}

