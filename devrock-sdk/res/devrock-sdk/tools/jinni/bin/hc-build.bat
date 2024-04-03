@echo off
setlocal EnableDelayedExpansion

set TOOLS_DIR=%~dp0\..\..

set DEVROCK_SDK_HOME=%TOOLS_DIR%\..
set REPOSITORY_CONFIGURATION_DEVROCK_ANT_TASKS=%DEVROCK_SDK_HOME%\conf\repository-configuration-devrock.yaml

CALL %TOOLS_DIR%\hc-build\bin\run %*

endlocal
