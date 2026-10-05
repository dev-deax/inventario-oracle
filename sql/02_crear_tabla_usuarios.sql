-- Ejecutar como INVENTARIO_CURSO en XEPDB1.
WHENEVER SQLERROR EXIT SQL.SQLCODE
CREATE SEQUENCE usuarios_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE usuarios (
    id NUMBER PRIMARY KEY,
    usuario VARCHAR2(40) NOT NULL,
    nombre VARCHAR2(80) NOT NULL,
    apellido VARCHAR2(80) NOT NULL,
    password_hash VARCHAR2(300) NOT NULL,
    creado_en TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT uq_usuarios_usuario UNIQUE (usuario),
    CONSTRAINT ck_usuarios_minusculas CHECK (usuario = LOWER(usuario))
);
