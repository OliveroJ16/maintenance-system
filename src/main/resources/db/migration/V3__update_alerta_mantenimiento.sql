-- ========================================
-- MIGRACIÓN V3: Actualizar tabla de alertas
-- ========================================

-- 1. Hacer nullable la columna de configuración
ALTER TABLE alerta_mantenimiento
MODIFY COLUMN id_conf_mant INT NULL;

-- 2. Agregar columnas directas para vehículo y tipo de mantenimiento
ALTER TABLE alerta_mantenimiento
ADD COLUMN id_vehiculo INT NULL AFTER id_conf_mant,
ADD COLUMN id_tipo_mant INT NULL AFTER id_vehiculo;

-- 3. Crear foreign keys
ALTER TABLE alerta_mantenimiento
ADD CONSTRAINT fk_alerta_vehiculo
    FOREIGN KEY (id_vehiculo)
    REFERENCES vehiculo(id_vehiculo)
    ON DELETE CASCADE,
ADD CONSTRAINT fk_alerta_tipo_mant
    FOREIGN KEY (id_tipo_mant)
    REFERENCES tipo_mantenimiento(id_tipo_mant)
    ON DELETE CASCADE;

ALTER TABLE alerta_mantenimiento
MODIFY COLUMN id_conf_mant INT NULL;
