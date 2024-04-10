@echo off
setlocal EnableDelayedExpansion

set HC_BUILD=%~dp0\hc-build

REM differentiate between gradle and ant build scripts to call according build system
if exist build.xml (
    REM artifact build
    CALL %HC_BUILD% run-ant %*
) else (
    REM group build
    CALL %HC_BUILD% build %*
)

endlocal