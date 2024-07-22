@echo off
setlocal
set JINNI_OPTS=-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=y
CALL %~dp0/jinni %*
endlocal