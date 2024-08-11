@echo off
setlocal

set LAUNCH_SCRIPT=%~nx0

set DEVROCK_SDK_HOME=%~dp0\..\..\..

if "%JAVA_HOME%"=="" (
    set JAVA_EXECUTABLE=java    
)else (
    set JAVA_EXECUTABLE="%JAVA_HOME%\bin\java"
)

%JAVA_EXECUTABLE% -Dreflex.launch.script=%LAUNCH_SCRIPT% -cp "%~dp0\hiconic-sdk-updator.jar" tribefire.extension.setup.hiconic.sdk.Updator %*

endlocal