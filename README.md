# Sistema Integral de Gestión para Caja de Ahorro

Este sistema integral permite administrar una cooperativa o caja de ahorro, gestionando socios adultos e infantiles, aportaciones, ahorros, préstamos, intereses y generando diversos reportes financieros.


## Descripción General

El Sistema de Gestión para Caja de Ahorro es una aplicación robusta desarrollada en Java con una interfaz gráfica de usuario moderna que facilita la operación diaria de cajas de ahorro y cooperativas. Permite un control preciso de movimientos financieros, manejo de socios y generación de reportes, todo organizado en una estructura modular.

## Características Principales

- **Gestión de Socios**: Alta, modificación y baja de socios adultos e infantiles
- **Control de Movimientos Financieros**: 
  - Aportaciones (depósitos y retiros)
  - Préstamos (otorgamiento y pagos)
  - Ahorros (depósitos y retiros)
  - Intereses (cálculo y retiros)
- **Reportes Financieros**:
  - Listados de socios con filtros
  - Estados de cuenta
  - Reportes diarios y mensuales
  - Premios al ahorro
- **Interfaz Intuitiva**:
  - Diseño moderno y responsivo
  - Validación en tiempo real
  - Conversión automática a mayúsculas
  - Impresión de reportes y movimientos

## Estructura del Sistema

### Paquete `connection`
Administra la conexión a la base de datos Access.
- `Conexion.java`: Establece y mantiene la conexión a la base de datos.
- `TestConexionStandalone.java`: Herramienta para verificar la conectividad.

### Paquete `tools` (Acceso a Datos)
Contiene las clases que manipulan directamente la información en la base de datos.
- `UsuarioDAO.java`: Gestiona la autenticación de usuarios.
- `SocioDAO.java`: Administra todas las operaciones de socios y sus movimientos financieros.
- `TablaBase.java`: Clase base para mostrar datos tabulares con funcionalidad de búsqueda, impresión y exportación.
- `ValidationUtils.java`: Proporciona validación de datos de entrada.

### Paquete `view`
Implementa todas las interfaces de usuario organizadas por funcionalidad:

#### Autenticación y Navegación
- `LoginForm.java`: Pantalla de acceso al sistema.
- `MenuPrincipal.java`: Navegación central a todos los módulos.

#### Gestión de Socios
- `NuevoSocioPanelIntegrado.java`: Registro de nuevos socios.
- `ModificarSocioPanelIntegrado.java`: Actualización de datos de socios.
- `EliminarSocioPanel.java`: Proceso de baja de socios.
- `BuscarSocioIDPanel.java`: Búsqueda de socios por identificador.

#### Listados de Socios
- `ListadoSociosAdultosPanelIntegrado.java`: Muestra el catálogo de socios adultos.
- `ListadoSociosInfantilesPanelIntegrado.java`: Muestra el catálogo de socios infantiles.

#### Operaciones Financieras
- `MovimientoSocioPanelIntegradoMejorado.java`: Gestiona los movimientos financieros de socios.
- `RetiroInteresesPanel.java`: Administra los retiros de intereses generados.
- `PremioAhorroPanel.java`: Calcula y muestra los premios al ahorro de los socios.

#### Reportes
- `ReporteDiarioGeneralPanel.java`: Reporte de movimientos diarios generales.
- `ReporteDiarioAdultosPanel.java`: Reporte de movimientos diarios de socios adultos.
- `ReporteDiarioInfantesPanel.java`: Reporte de movimientos diarios de socios infantiles.
- `ReporteMensualGeneralPanel.java`: Reporte de movimientos mensuales generales.
- `ReporteMensualAdultosPanel.java`: Reporte de movimientos mensuales de socios adultos.
- `ReporteMensualInfantesPanel.java`: Reporte de movimientos mensuales de socios infantiles.

## Base de Datos

El sistema utiliza una base de datos Access (dbcaja.mdb) con las siguientes tablas principales:

- **Socios**: Registro de socios adultos.
- **SociosInfa**: Registro de socios infantiles.
- **MovimientosSocio**: Transacciones financieras de socios.
- **InteresesRetirados**: Registro específico de retiros de intereses.
- **Usuarios**: Credenciales de acceso al sistema.

# Sus tablas con campos son:
-**Ahorro**: IdAho	NoSocio	Cantidad	Fecha
-**AhorroInfa**:Idsocain	NoSocio	Fecha	Cantidad
-**Cancelaciones**:Idcanc	NoSocio	Descripcion	Cantidad	Fecha
-**Deposito**:Iddeps	NoSocio	Cantidad
-**DepPrestamo**:IdDepPre	NoSocio	Cantidad	Fecha	Intereses
-**EgreDeposito**:Idegrede	NoSocio	Cantidad	Fecha
-**IngreDeposito**:Idredep	NoSocio	Cantidad	Fecha
-**MovimientosSocios**: IdMov	NoSocio	Fecha	AporIngresos	AporEgresos	AporSaldo	PresEgresos	PresIngresos    PresSaldo	Intereses	AhoIngresos	AhoEgresos	AhoSaldo	TipoSocio	Nada	RetInteres	SaldoBanco	RetBanco	IngOtros	EgrOtros	GastosAdmon
-**MovimientosSociosTemporal**:Id	Defaul	NoSocio	Fecha	AporIngresos	AporEgresos	AporSaldo	PresEgresos	PresIngresos	PresSaldo	Intereses	RetInteres	AhoIngresos	AhoEgresos	AhoSaldo	TipoSocio	Nada	SaldoBanco	RetBanco	IngOtros	EgrOtros	GastosAdmon
-**Prac**:Idadas	No
-**IdPresSoc**:IdPresSoc	NoSocio	Cantidad	Fecha
-**Prestamo**:IdPre	NoSocio	Aval	Cantidad	Fecha	InteresActual
-**PrestamoFechaT**:Id34d	NoSocio	Fecha
-**RegPrestamo**:IdRegPre	NoSocio	Aval	Cantidad	Avalado	Fecha
-**ReporteEgresos**:Idrepegre	Fecha	NoSocio	Retiro	Prestamo	Deposito	Utilerias	Descripcion	TIPO
-**SociosInfa**:Iddasdf	NoSocio	Fecha	Nombres	Apellidos	Direccion	Telefono	PresentadoPor


## Requisitos Técnicos

- **Java Runtime**: JDK 8 o superior
- **Librerías**:
  - UCanAccess 4.0.3+ (conexión a bases de datos Access)
  - JCalendar 1.4 (selector de fechas)
  - FlatLaf 2.6+ (tema visual moderno)
  - Commons-lang 2.6 (utilidades)
  - Commons-logging 1.1.3 (registro de eventos)

Todas las dependencias necesarias están incluidas en la carpeta `Librerias` del proyecto.

## Instalación y Configuración

1. Clone o descargue el repositorio
2. Abra el proyecto en su IDE de preferencia (Eclipse recomendado)
3. Asegúrese que todas las librerías en la carpeta `Librerias` estén correctamente referenciadas
4. Verifique que el archivo de base de datos `dbcaja.mdb` esté ubicado en la carpeta `database`
5. Ejecute la clase principal `LoginForm.java`

## Acceso al Sistema

Para ingresar al sistema, utilice las credenciales almacenadas en la tabla `Usuarios` de la base de datos. El sistema diferencia entre tipos de usuarios con diferentes niveles de acceso a funcionalidades.

## Funcionalidades Implementadas

- ✅ Autenticación segura de usuarios
- ✅ Gestión completa de socios adultos e infantiles (alta, baja, modificación)
- ✅ Control de aportaciones, préstamos, ahorros e intereses
- ✅ Generación de reportes diarios y mensuales
- ✅ Cálculo de premios al ahorro
- ✅ Exportación de datos a CSV
- ✅ Impresión de reportes y estados de cuenta

## Guía de Uso Rápido

1. **Inicio de Sesión**: Acceda con usuario y contraseña válidos
2. **Menú Principal**: Navegue entre las diferentes secciones del sistema
3. **Gestión de Socios**: Registre nuevos socios o modifique datos existentes
4. **Operaciones Financieras**: Registre movimientos como aportaciones, préstamos o ahorros
5. **Reportes**: Genere e imprima informes financieros según necesite

## Mantenimiento y Soporte

El sistema incluye mecanismos para diagnóstico y resolución de problemas:

- Mensajes de registro detallados
- Validación de datos en tiempo real
- Manejo de excepciones con información útil

## Colaboradores

- Departamento de Desarrollo de Soluciones Financieras
- Equipo de Diseño de Interfaces de Usuario
- Especialistas en Sistemas de Gestión Cooperativa

## Licencia

Este software es propietario y su uso está restringido a los términos establecidos en el contrato de licencia.



---

© 2025 Sistema de Gestión para Caja de Ahorro - Todos los derechos reservados
