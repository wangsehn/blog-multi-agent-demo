@REM Maven Wrapper startup script for Windows
@REM Required ENV vars: JAVA_HOME
@echo off
setlocal

set "WRAPPER_JAR=%~dp0\.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

if exist "%WRAPPER_JAR%" (
    java %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" %WRAPPER_LAUNCHER% %*
) else (
    echo Maven wrapper jar not found. Please run: mvn wrapper:wrapper
    exit /b 1
)
