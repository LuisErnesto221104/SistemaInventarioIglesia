@echo off
echo Ejecutando Sistema de Ventas - Login
echo ================================
java -cp "bin;Librerias/commons-lang-2.6.jar;Librerias/commons-logging-1.1.3.jar;Librerias/hsqldb.jar;Librerias/jackcess-2.1.9.jar;Librerias/ucanaccess-4.0.3.jar;Librerias/flatlaf-2.6.jar" vista.LoginForm
pause
