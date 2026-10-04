-- Ejecutar una vez como INVENTARIO_APP después de los scripts 02 a 05.
WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK
INSERT INTO categorias (id, nombre, descripcion)
VALUES (categorias_seq.NEXTVAL, 'Tecnología', 'Accesorios de computadora');
INSERT INTO categorias (id, nombre, descripcion)
VALUES (categorias_seq.NEXTVAL, 'Oficina', 'Materiales de oficina');
INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email)
VALUES (proveedores_seq.NEXTVAL, 'Carlos Méndez', 'Tecno GT', '548796-1', '5555-1001', 'ventas@tecnogt.test');
INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email)
VALUES (proveedores_seq.NEXTVAL, 'María López', 'OfiPlus', '778899-2', '5555-1002', 'ventas@ofiplus.test');
INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo)
VALUES (productos_seq.NEXTVAL, 'TEC-001', 'Mouse inalámbrico', 125, 18,
    (SELECT id FROM categorias WHERE nombre = 'Tecnología'),
    (SELECT id FROM proveedores WHERE nit = '548796-1'), 1);
INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo)
VALUES (productos_seq.NEXTVAL, 'OFI-001', 'Cuaderno', 25.50, 10,
    (SELECT id FROM categorias WHERE nombre = 'Oficina'),
    (SELECT id FROM proveedores WHERE nit = '778899-2'), 1);
COMMIT;
