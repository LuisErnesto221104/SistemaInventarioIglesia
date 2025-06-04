@echo off
echo Compilando TestConexionStandalone.java...
javac -cp ".;Librerias\commons-lang-2.6.jar;Librerias\commons-logging-1.1.3.jar;Librerias\hsqldb.jar;Librerias\jackcess-2.1.9.jar;Librerias\ucanaccess-4.0.3.jar" TestConexionStandalone.java

echo.
echo Ejecutando TestConexionStandalone...
java -cp ".;Librerias\commons-lang-2.6.jar;Librerias\commons-logging-1.1.3.jar;Librerias\hsqldb.jar;Librerias\jackcess-2.1.9.jar;Librerias\ucanaccess-4.0.3.jar" TestConexionStandalone

pause
