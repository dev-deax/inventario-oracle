-- Ejecutar como INVENTARIO_CURSO en XEPDB1, después de 02 a 05.
-- Agrega datos de curso sin sobrescribir los existentes. Se puede repetir.
-- Las cinco cuentas demo01 a demo05 usan la contraseña de demostración CursoDemo123!
WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK
SET DEFINE OFF

INSERT INTO categorias (id, nombre, descripcion)
SELECT categorias_seq.NEXTVAL, 'Tecnología', 'Accesorios de computadora' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Tecnología');

INSERT INTO categorias (id, nombre, descripcion)
SELECT categorias_seq.NEXTVAL, 'Oficina', 'Materiales de oficina' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Oficina');

INSERT INTO categorias (id, nombre, descripcion)
SELECT categorias_seq.NEXTVAL, 'Limpieza', 'Materiales para limpieza' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Limpieza');

INSERT INTO categorias (id, nombre, descripcion)
SELECT categorias_seq.NEXTVAL, 'Herramientas', 'Herramientas de trabajo' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Herramientas');

INSERT INTO categorias (id, nombre, descripcion)
SELECT categorias_seq.NEXTVAL, 'Mobiliario', 'Muebles para oficina' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Mobiliario');

INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email)
SELECT proveedores_seq.NEXTVAL, 'Carlos Méndez', 'Tecno GT', '548796-1', '5555-1001', 'ventas@tecnogt.test' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM proveedores WHERE nit = '548796-1');

INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email)
SELECT proveedores_seq.NEXTVAL, 'María López', 'OfiPlus', '778899-2', '5555-1002', 'ventas@ofiplus.test' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM proveedores WHERE nit = '778899-2');

INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email)
SELECT proveedores_seq.NEXTVAL, 'Ana García', 'Limpio GT', '100003-3', '5555-1003', 'ventas@limpio.test' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM proveedores WHERE nit = '100003-3');

INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email)
SELECT proveedores_seq.NEXTVAL, 'Luis Pérez', 'Herramientas GT', '100004-4', '5555-1004', 'ventas@herramientas.test' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM proveedores WHERE nit = '100004-4');

INSERT INTO proveedores (id, nombre, empresa, nit, telefono, email)
SELECT proveedores_seq.NEXTVAL, 'Sofía Ruiz', 'Muebles GT', '100005-5', '5555-1005', 'ventas@muebles.test' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM proveedores WHERE nit = '100005-5');

INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo)
SELECT productos_seq.NEXTVAL, 'TEC-001', 'Mouse inalámbrico', 125, 18, (SELECT id FROM categorias WHERE nombre = 'Tecnología'), (SELECT id FROM proveedores WHERE nit = '548796-1'), 1 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE codigo = 'TEC-001');

INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo)
SELECT productos_seq.NEXTVAL, 'OFI-001', 'Cuaderno', 25.5, 10, (SELECT id FROM categorias WHERE nombre = 'Oficina'), (SELECT id FROM proveedores WHERE nit = '778899-2'), 1 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE codigo = 'OFI-001');

INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo)
SELECT productos_seq.NEXTVAL, 'LIM-001', 'Escoba', 35, 12, (SELECT id FROM categorias WHERE nombre = 'Limpieza'), (SELECT id FROM proveedores WHERE nit = '100003-3'), 1 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE codigo = 'LIM-001');

INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo)
SELECT productos_seq.NEXTVAL, 'HER-001', 'Martillo', 60, 8, (SELECT id FROM categorias WHERE nombre = 'Herramientas'), (SELECT id FROM proveedores WHERE nit = '100004-4'), 1 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE codigo = 'HER-001');

INSERT INTO productos (id, codigo, nombre, precio, stock, categoria_id, proveedor_id, activo)
SELECT productos_seq.NEXTVAL, 'MOB-001', 'Silla de oficina', 450, 5, (SELECT id FROM categorias WHERE nombre = 'Mobiliario'), (SELECT id FROM proveedores WHERE nit = '100005-5'), 1 FROM dual
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE codigo = 'MOB-001');

INSERT INTO usuarios (id, usuario, nombre, apellido, password_hash)
SELECT usuarios_seq.NEXTVAL, 'demo01', 'Ana', 'García', '600000:adhmxR10+t+EWFPBDjrHDA==:atlDGITi+9hVaBf7PyyhsyeUw9JIY0OIt7oAZi4RvZQ=' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'demo01');

INSERT INTO usuarios (id, usuario, nombre, apellido, password_hash)
SELECT usuarios_seq.NEXTVAL, 'demo02', 'Luis', 'Pérez', '600000:E7oF9JxEPf2qCtasEeYO1A==:M9CIbLBDXuofF6l+A/UMRfiikajVF96CFchk6ANVb0Y=' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'demo02');

INSERT INTO usuarios (id, usuario, nombre, apellido, password_hash)
SELECT usuarios_seq.NEXTVAL, 'demo03', 'Sofía', 'Ruiz', '600000:DrJGwngIEn7VD6NdmR89tQ==:JVKeToqWjFLoxUdV+Q3Hn2BAHRdroJ44h6eYPO87sos=' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'demo03');

INSERT INTO usuarios (id, usuario, nombre, apellido, password_hash)
SELECT usuarios_seq.NEXTVAL, 'demo04', 'Pedro', 'López', '600000:GuTLcxBORtO/UDdztKsBiw==:nw/5bx7L6h8UJV3jNeQuEgGwViq/wjms5S7VSI4HW4g=' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'demo04');

INSERT INTO usuarios (id, usuario, nombre, apellido, password_hash)
SELECT usuarios_seq.NEXTVAL, 'demo05', 'Elena', 'Méndez', '600000:MNLDh8o2sShYpZZJ8o44pw==:5MprC2tmyzSLf9IgOdYlDGpJ0zHXFMKDCEPI7oIMPmY=' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'demo05');

COMMIT;
