$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
& mvn compile dependency:copy-dependencies
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
$java = 'java'
if ($env:JAVA_HOME) { $java = Join-Path $env:JAVA_HOME 'bin/java.exe' }
& $java -cp 'target/classes;target/dependency/*' com.daniel.inventario.db.CrearUsuarioInicial
exit $LASTEXITCODE
