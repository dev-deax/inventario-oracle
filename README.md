# Inventario Oracle

Proyecto de Programación II con Java 17, Maven, Swing, JDBC y Oracle.

Login y CRUD de usuarios, categorías, proveedores y productos. Con búsqueda por ID o texto, sin roles.

## Preparar Oracle

1. Iniciar Oracle y abrir el PDB `XEPDB1` en `localhost:1521`.
2. Conectar como SYSTEM **a XEPDB1** y ejecutar `sql/01_crear_usuario_oracle.sql` con SQL*Plus o SQLcl. Solicita una contraseña para `INVENTARIO_CURSO`.
3. Conectar como `INVENTARIO_CURSO` y ejecutar, en orden, los scripts `02` a `05`.
4. Ejecutar `07_completar_datos_prueba.sql` para disponer de al menos cinco registros por tabla. Puede ejecutarse también si ya se aplicó `06_datos_demostracion_inventario.sql`; conserva los registros existentes.
5. Copiar `config/database.properties.example` a `config/database.properties`, dentro de la carpeta config del proyecto, y colocar la contraseña elegida.

`INVENTARIO_CURSO` es la cuenta y el esquema exclusivos del proyecto. La aplicación no utiliza SYSTEM ni el esquema del proyecto anterior. Los scripts crean objetos nuevos y no deben repetirse sobre un esquema ya instalado. Los cambios posteriores se agregan con nuevos scripts.

El script 07 incluye las cuentas de demostración `demo01` a `demo05`, con contraseña `CursoDemo123!` guardada mediante PBKDF2 con HMAC-SHA256. Son datos exclusivos de la presentación. No modifica la contraseña de una cuenta que ya exista.

## Crear la primera cuenta

Con JDK 17 o posterior y Maven disponibles en la terminal:

```powershell
mvn compile dependency:copy-dependencies
java -cp "target/classes;target/dependency/*" com.daniel.inventario.db.CrearUsuarioInicial
```

Solicita usuario, nombre, apellido y contraseña (mínimo 8 caracteres). La contraseña no se muestra ni se guarda en texto plano. Esta cuenta es para entrar a la aplicación; es distinta de `INVENTARIO_CURSO`.

## Ejecutar

Abrir esta carpeta como proyecto Maven en NetBeans y usar **Run Project (F6)**. También se puede ejecutar desde la raíz:

```text
mvn compile exec:java
```

En Usuarios, una contraseña vacía al editar conserva la existente. No se permite eliminar la cuenta de la sesión actual. Productos requiere que existan categorías y proveedores. Las tablas muestran primero los registros más recientes. Buscar admite ID o texto sin distinguir mayúsculas; Enter ejecuta la búsqueda y Actualizar limpia el filtro.

## Verificar

```powershell
mvn test-compile dependency:copy-dependencies
java -Djava.awt.headless=true -cp "target/classes;target/test-classes" com.daniel.inventario.PasswordsTest
java -Djava.awt.headless=true -cp "target/classes;target/test-classes" com.daniel.inventario.ValidatorsTest
java -Djava.awt.headless=true -cp "target/classes;target/test-classes" com.daniel.inventario.ErrorsTest
java -Djava.awt.headless=true -cp "target/classes;target/test-classes" com.daniel.inventario.ui.UiSmokeTest
java -cp "target/classes;target/test-classes;target/dependency/*" com.daniel.inventario.OracleIntegrationTest
```

Las pruebas de contraseñas, validaciones, mensajes y formularios funcionan sin base de datos. La prueba de formularios genera imágenes en `target/capturas`. `OracleIntegrationTest` requiere el esquema instalado y prueba login, CRUD, búsquedas, orden y claves foráneas con registros temporales que elimina al terminar. Consume valores de las secuencias. Ejecutarla en el esquema de demostración sin otras sesiones modificando datos.

Las pruebas son programas Java sencillos con `main`; `mvn test` por sí solo no las ejecuta.

Para presentar: probar login incorrecto, entrar, crear una categoría y un proveedor, guardar/editar/eliminar un producto y cerrar sesión. Comprobar también un código duplicado y una categoría utilizada por un producto.

## Organización

- `model`: datos y asociaciones.
- `dao`: consultas JDBC.
- `ui`: ventanas, panel base y controles compartidos.
- `config`, `db`, `util`: configuración, conexión y validaciones.

El historial conserva ramas por funcionalidad y merges en `main`. La configuración local, las contraseñas y `target` están excluidos de Git.
