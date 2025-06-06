# Sistema de Caja - Aplicación de Gestión

Este sistema permite la gestión de socios y operaciones financieras dentro de una cooperativa o caja de ahorro.

## Estructura del Sistema

El sistema está organizado en varios paquetes:

### Paquete `conexion`
Contiene las clases para la conexión y administración de la base de datos.
- `Conexion.java`: Administra la conexión a la base de datos Access.
- `TestConexionStandalone.java`: Prueba la conexión a la base de datos.

### Paquete `dao` (Data Access Object)
Contiene las clases que interactúan directamente con la base de datos.
- `UsuarioDAO.java`: Gestiona las operaciones relacionadas con usuarios y autenticación.
- `SocioDAO.java`: Gestiona las operaciones relacionadas con socios adultos e infantiles.

### Paquete `vista`
Contiene todas las interfaces de usuario.
- `LoginForm.java`: Pantalla de inicio de sesión.
- `MenuPrincipal.java`: Menú principal con todas las opciones del sistema.
- `ListadoSociosAdultos.java`: Muestra el listado de socios adultos.
- `ListadoSociosInfantiles.java`: Muestra el listado de socios infantiles.

## Requisitos del Sistema

- Java 8 o superior
- Driver UCanAccess para conexión a bases de datos Access
- Las librerías necesarias se encuentran en la carpeta `Librerias`


## Información de Acceso

Para acceder al sistema, utilice las credenciales almacenadas en la tabla `Usuarios` de la base de datos.

## Funcionalidades Implementadas

- Autenticación de usuarios
- Listado de socios adultos
- Listado de socios infantiles

## Próximas Funcionalidades

- Gestión completa de socios (alta, baja, modificación)
- Gestión de préstamos
- Gestión de ahorros
- Reportes completos
