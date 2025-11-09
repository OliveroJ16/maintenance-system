CREATE DATABASE IF NOT EXISTS sistema_mantenimiento;
USE sistema_mantenimiento;


CREATE TABLE chofer (
    id_chofer INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    cedula VARCHAR(20) UNIQUE,
    telefono VARCHAR(15),
    email VARCHAR(100),
    categoria_licencia ENUM('A','B','C','D','E','F','G','H','I'),
    fecha_expiracion_licencia DATE,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo'
);


CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('administrador', 'supervisor') NOT NULL,
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_chofer INT NULL,
    FOREIGN KEY (id_chofer) REFERENCES chofer(id_chofer)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


CREATE TABLE taller (
    id_taller INT AUTO_INCREMENT PRIMARY KEY,
    nombre_taller VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    especialidad VARCHAR(100),
    estado ENUM('activo', 'inactivo') DEFAULT 'activo'
);


CREATE TABLE vehiculo (
    id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(20) NOT NULL UNIQUE,
    numero_serie VARCHAR(50) NOT NULL,
    kilometraje INT DEFAULT 0,
    fecha_adquisicion DATE,
    estado_vehiculo ENUM('activo', 'inactivo', 'mantenimiento') DEFAULT 'activo',
    tipo_combustible ENUM('gasolina', 'diesel', 'electrico', 'hibrido', 'otro'),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    tipo_vehiculo ENUM('sedán', 'SUV', 'camioneta', 'van', 'camión', 'otro') NOT NULL
);


CREATE TABLE tipo_mantenimiento (
    id_tipo_mant INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    categoria ENUM('preventivo', 'correctivo') NOT NULL,
    prioridad ENUM('baja', 'media', 'alta')
);


CREATE TABLE configuracion_mantenimiento (
    id_conf_mant INT AUTO_INCREMENT PRIMARY KEY,
    frecuencia_km INT,
    frecuencia_meses INT,
    descripcion TEXT,
    id_tipo_mant INT NOT NULL,
    id_vehiculo INT NOT NULL,
    FOREIGN KEY (id_tipo_mant) REFERENCES tipo_mantenimiento(id_tipo_mant)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE alerta_mantenimiento (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,
    tipo_alerta ENUM('preventiva', 'correctiva') DEFAULT 'preventiva',
    fecha_alerta DATE NOT NULL,
    km_alerta INT,
    estado_alerta ENUM('notificada', 'atendida', 'vencida') DEFAULT 'notificada',
    mensaje TEXT,
    visto BOOLEAN DEFAULT FALSE,
    id_conf_mant INT NOT NULL,
    FOREIGN KEY (id_conf_mant) REFERENCES configuracion_mantenimiento(id_conf_mant)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT PRIMARY KEY,
    fecha_programada DATE NOT NULL,
    km_programado INT NOT NULL,
    fecha_ejecucion DATE,
    km_ejecucion INT,
    estado ENUM('pendiente', 'en_proceso', 'completado', 'cancelado') DEFAULT 'pendiente',
    descripcion TEXT,
    id_vehiculo INT NOT NULL,
    id_tipo_mant INT NOT NULL,
    id_taller INT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (id_tipo_mant) REFERENCES tipo_mantenimiento(id_tipo_mant)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (id_taller) REFERENCES taller(id_taller)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);



CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre_servicio VARCHAR(100) NOT NULL,
    descripcion TEXT,
    costo DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    tiempo_minutos INT,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    id_taller INT NOT NULL,
    FOREIGN KEY (id_taller) REFERENCES taller(id_taller)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE asignacion_vehiculo (
    id_chofer INT,
    id_vehiculo INT,
    fecha_asignacion DATE NOT NULL,
    PRIMARY KEY (id_chofer, id_vehiculo),
    FOREIGN KEY (id_chofer) REFERENCES chofer(id_chofer)
        ON DELETE CASCADE,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo)
        ON DELETE CASCADE
);