param([switch]$Oracle)
$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
& mvn test-compile
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
$java = 'java'
if ($env:JAVA_HOME) { $java = Join-Path $env:JAVA_HOME 'bin/java.exe' }
foreach ($prueba in @('com.daniel.inventario.PasswordsTest', 'com.daniel.inventario.ValidatorsTest', 'com.daniel.inventario.ui.UiSmokeTest')) {
    & $java '-Djava.awt.headless=true' -cp 'target/classes;target/test-classes' $prueba
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}
if ($Oracle) {
    & mvn dependency:copy-dependencies
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    & $java -cp 'target/classes;target/test-classes;target/dependency/*' com.daniel.inventario.OracleIntegrationTest
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
}
