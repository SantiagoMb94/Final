-- Script de creación de la base de datos para el Sistema de Gestión de Encuestas Empresariales
-- PostgreSQL

-- Crear la base de datos (ejecutar manualmente si no existe)
-- CREATE DATABASE encuestas_db;

-- Tabla: empresa
CREATE TABLE IF NOT EXISTS empresa (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    nit VARCHAR(100) UNIQUE,
    direccion VARCHAR(255),
    telefono VARCHAR(50),
    email VARCHAR(100),
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: usuario
CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'USUARIO',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    empresa_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE
);

-- Tabla: encuesta
CREATE TABLE IF NOT EXISTS encuesta (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion VARCHAR(1000),
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP,
    estado VARCHAR(20) NOT NULL DEFAULT 'BORRADOR',
    empresa_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_encuesta_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE
);

-- Tabla: pregunta
CREATE TABLE IF NOT EXISTS pregunta (
    id BIGSERIAL PRIMARY KEY,
    texto VARCHAR(500) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    obligatoria BOOLEAN NOT NULL DEFAULT FALSE,
    orden INTEGER,
    encuesta_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pregunta_encuesta FOREIGN KEY (encuesta_id) REFERENCES encuesta(id) ON DELETE CASCADE
);

-- Tabla: opcion
CREATE TABLE IF NOT EXISTS opcion (
    id BIGSERIAL PRIMARY KEY,
    texto VARCHAR(255) NOT NULL,
    orden INTEGER,
    pregunta_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_opcion_pregunta FOREIGN KEY (pregunta_id) REFERENCES pregunta(id) ON DELETE CASCADE
);

-- Tabla: respuesta
CREATE TABLE IF NOT EXISTS respuesta (
    id BIGSERIAL PRIMARY KEY,
    valor VARCHAR(1000),
    fecha_respuesta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    identificador_respondente VARCHAR(100),
    encuesta_id BIGINT NOT NULL,
    pregunta_id BIGINT NOT NULL,
    opcion_id BIGINT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_respuesta_encuesta FOREIGN KEY (encuesta_id) REFERENCES encuesta(id) ON DELETE CASCADE,
    CONSTRAINT fk_respuesta_pregunta FOREIGN KEY (pregunta_id) REFERENCES pregunta(id) ON DELETE CASCADE,
    CONSTRAINT fk_respuesta_opcion FOREIGN KEY (opcion_id) REFERENCES opcion(id) ON DELETE SET NULL
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_usuario_empresa ON usuario(empresa_id);
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_encuesta_empresa ON encuesta(empresa_id);
CREATE INDEX IF NOT EXISTS idx_encuesta_estado ON encuesta(estado);
CREATE INDEX IF NOT EXISTS idx_pregunta_encuesta ON pregunta(encuesta_id);
CREATE INDEX IF NOT EXISTS idx_opcion_pregunta ON opcion(pregunta_id);
CREATE INDEX IF NOT EXISTS idx_respuesta_encuesta ON respuesta(encuesta_id);
CREATE INDEX IF NOT EXISTS idx_respuesta_pregunta ON respuesta(pregunta_id);
CREATE INDEX IF NOT EXISTS idx_respuesta_respondente ON respuesta(identificador_respondente);

