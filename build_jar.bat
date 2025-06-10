@echo off
echo Creando archivo JAR para SistemsaVentas...
cd c:\Users\ernes\eclipse-workspace\SistemsaVentas

:: Crear el directorio dist si no existe
mkdir dist 2>nul

:: Compilar el proyecto (asumiendo que ya está compilado en bin)
echo Usando clases compiladas en el directorio bin...

:: Crear el JAR con el manifiesto y las clases compiladas
jar cfm dist\SistemsaVentas.jar src\META-INF\MANIFEST.MF -C bin .

:: Verificar si se creó correctamente
if exist dist\SistemsaVentas.jar (
    echo JAR creado exitosamente: dist\SistemsaVentas.jar
    
    :: Copiar las librerías necesarias
    echo Copiando librerías necesarias...
    mkdir dist\lib 2>nul
    
    :: Copiar todos los JARs necesarios a la carpeta dist\lib
    copy Librerias\*.jar dist\lib\
    
    :: Copiar la base de datos
    echo Copiando base de datos...
    mkdir dist\bd 2>nul
    copy bd\dbcaja.mdb dist\bd\
    
    echo Proceso completado. El archivo JAR y sus dependencias están en la carpeta 'dist'.
    echo Para ejecutar la aplicación, abra una terminal y ejecute:
    echo java -jar dist\SistemsaVentas.jar
) else (
    echo Error al crear el archivo JAR.
)

pause
