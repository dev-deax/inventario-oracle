-- Ejecutar como INVENTARIO_APP en XEPDB1.
WHENEVER SQLERROR EXIT SQL.SQLCODE
CREATE SEQUENCE productos_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE productos (
    id NUMBER PRIMARY KEY,
    codigo VARCHAR2(40) NOT NULL,
    nombre VARCHAR2(150) NOT NULL,
    precio NUMBER(12,2) NOT NULL,
    stock NUMBER(10) NOT NULL,
    categoria_id NUMBER NOT NULL,
    proveedor_id NUMBER NOT NULL,
    activo NUMBER(1) DEFAULT 1 NOT NULL,
    creado_en TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT uq_productos_codigo UNIQUE (codigo),
    CONSTRAINT ck_productos_precio CHECK (precio >= 0),
    CONSTRAINT ck_productos_stock CHECK (stock BETWEEN 0 AND 2147483647),
    CONSTRAINT ck_productos_activo CHECK (activo IN (0, 1)),
    CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_productos_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
);
