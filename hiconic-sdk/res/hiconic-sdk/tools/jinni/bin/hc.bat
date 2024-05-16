@echo off
setlocal EnableDelayedExpansion

set HC_BUILD=%~dp0\hc-build

REM differentiate between gradle and ant build scripts to call according build system
if exist parent\pom.xml (
    REM group build
    CALL %HC_BUILD% build %*
) else (
    REM artifact build
    CALL %HC_BUILD% run-ant %*
)

endlocal