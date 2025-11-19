@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

SET "DEFAULT_GRADLE_VERSION=8.7"
IF NOT "%GRADLE_VERSION%"=="" (
    SET "TARGET_VERSION=%GRADLE_VERSION%"
) ELSE (
    SET "TARGET_VERSION=%DEFAULT_GRADLE_VERSION%"
)

SET "DIST_NAME=gradle-%TARGET_VERSION%"
IF NOT "%GRADLE_USER_HOME%"=="" (
    SET "DIST_DIR=%GRADLE_USER_HOME%"
) ELSE (
    SET "DIST_DIR=%CD%\.gradle-dist"
)
SET "DIST_ARCHIVE=%DIST_DIR%\%DIST_NAME%-bin.zip"
SET "GRADLE_HOME_DIR=%DIST_DIR%\%DIST_NAME%"

IF NOT EXIST "%DIST_DIR%" (
    MKDIR "%DIST_DIR%"
)

IF NOT EXIST "%DIST_ARCHIVE%" (
    ECHO Downloading Gradle %TARGET_VERSION% ...
    powershell -Command "if (!(Get-Command Invoke-WebRequest -ErrorAction SilentlyContinue)) { exit 1 } else { Invoke-WebRequest -Uri https://services.gradle.org/distributions/%DIST_NAME%-bin.zip -OutFile '%DIST_ARCHIVE%' }"
    IF ERRORLEVEL 1 (
        ECHO Failed to download Gradle. Ensure PowerShell is available.
        EXIT /B 1
    )
)

IF NOT EXIST "%GRADLE_HOME_DIR%" (
    ECHO Extracting Gradle %TARGET_VERSION% ...
    powershell -Command "Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::ExtractToDirectory('%DIST_ARCHIVE%', '%DIST_DIR%')" 2>NUL
    IF ERRORLEVEL 1 (
        ECHO Failed to extract Gradle archive. Please ensure .NET Framework 4.5+ is available.
        EXIT /B 1
    )
)

IF NOT "%GRADLE_JAVA_HOME%"=="" (
    SET "JAVA_HOME=%GRADLE_JAVA_HOME%"
)

"%GRADLE_HOME_DIR%\bin\gradle.bat" %*
