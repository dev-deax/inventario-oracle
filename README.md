# Inventario Oracle

Proyecto de Programación II con Java 17, Maven, Swing, JDBC y Oracle.

Login y CRUD de usuarios, categorías, proveedores y productos. Sin buscadores ni roles.

## Preparar Oracle

1. Iniciar Oracle y abrir el PDB `XEPDB1` en `localhost:1521`.
2. Conectar como SYSTEM **a XEPDB1** y ejecutar `sql/01_crear_usuario_oracle.sql` con SQL*Plus o SQLcl. Solicita una contraseña para `INVENTARIO_CURSO`.
3. Conectar como `INVENTARIO_CURSO` y ejecutar, en orden, los scripts `02` a `05`.
4. Opcionalmente ejecutar `06_datos_demostracion_inventario.sql` una vez.
5. Copiar `db.properties.example` a `db.properties`, en la raíz del proyecto, y colocar la contraseña elegida.

`INVENTARIO_CURSO` es la cuenta y el esquema exclusivos del proyecto. La aplicación no utiliza SYSTEM ni el esquema del proyecto anterior. Los scripts crean objetos nuevos y no deben repetirse sobre un esquema ya instalado. Los cambios posteriores se agregan con nuevos scripts.

## Crear la primera cuenta

Con JDK 17 o posterior y Maven disponibles en la terminal:

```powershell
.\scripts\crear-usuario.ps1
```

Solicita usuario, nombre, apellido y contraseña (mínimo 8 caracteres). La contraseña no se muestra ni se guarda en texto plano. Esta cuenta es para entrar a la aplicación; es distinta de `INVENTARIO_CURSO`.

## Ejecutar

Abrir esta carpeta como proyecto Maven en NetBeans y usar **Run Project (F6)**. También se puede ejecutar desde la raíz:

```text
mvn compile exec:java
```

En Usuarios, una contraseña vacía al editar conserva la existente. No se permite eliminar la cuenta de la sesión actual. Productos requiere que existan categorías y proveedores. Las tablas muestran primero los registros más recientes.

## Verificar

```powershell
.\scripts\verificar.ps1
.\scripts\verificar.ps1 -Oracle
```

La primera orden comprueba contraseñas, validaciones y formularios Swing sin base de datos; genera imágenes en `target/capturas`. La segunda requiere el esquema instalado y prueba login, CRUD, orden y claves foráneas con registros temporales que elimina al terminar. Consume valores de las secuencias. Ejecutarla en el esquema de demostración sin otras sesiones modificando datos.

Las pruebas son programas Java sencillos con `main`; `mvn test` por sí solo no las ejecuta.

Para presentar: probar login incorrecto, entrar, crear una categoría y un proveedor, guardar/editar/eliminar un producto y cerrar sesión. Comprobar también un código duplicado y una categoría utilizada por un producto.

## Organización

- `model`: datos y asociaciones.
- `dao`: consultas JDBC.
- `ui`: ventanas, panel base y controles compartidos.
- `config`, `db`, `util`: configuración, conexión y validaciones.

El historial conserva ramas por funcionalidad y merges en `main`. La configuración local, las contraseñas y `target` están excluidos de Git.
