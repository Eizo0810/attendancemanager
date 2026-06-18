$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$javaHome = "C:\pleiades\2025-09_Java\java\21"

if (-not (Test-Path (Join-Path $javaHome "bin\java.exe"))) {
    throw "Java 21 was not found at $javaHome"
}

$env:JAVA_HOME = $javaHome
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Set-Location $projectRoot

& .\mvnw.cmd test

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}
