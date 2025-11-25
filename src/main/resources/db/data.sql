-- Datos de ejemplo para pruebas (opcional)
-- Este archivo se ejecuta automáticamente si spring.jpa.hibernate.ddl-auto está configurado

-- Insertar empresa de ejemplo
INSERT INTO empresa (nombre, nit, direccion, telefono, email, activa) 
VALUES ('Empresa Ejemplo S.A.', '900123456-7', 'Calle 123 #45-67', '6012345678', 'contacto@empresaejemplo.com', TRUE)
ON CONFLICT (nombre) DO NOTHING;

-- Insertar usuario administrador de ejemplo (contraseña: admin123)
-- NOTA: En producción, las contraseñas deben estar hasheadas
INSERT INTO usuario (nombre, apellido, email, contrasena, rol, activo, empresa_id)
SELECT 'Admin', 'Sistema', 'admin@empresaejemplo.com', 'admin123', 'ADMINISTRADOR', TRUE, id
FROM empresa WHERE nombre = 'Empresa Ejemplo S.A.'
ON CONFLICT (email) DO NOTHING;

