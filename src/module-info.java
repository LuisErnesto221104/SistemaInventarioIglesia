/**
 * Módulo principal del Sistema de Ventas
 */
open module SistemsaVentas {    
    requires transitive java.sql;
    requires transitive java.desktop;
    requires java.logging;
    requires java.prefs;
    
    // Abrir acceso a bibliotecas externas
    requires static jdk.unsupported;
    
    // Configuración específica para JCalendar
    
    // Esta línea permite al módulo acceder a todas las clases en el classpath
    // Exportar vista a todos los módulos (incluido com.toedter.calendar)
    exports vista;

    // Exportar paquetes
    exports conexion;
    exports dao;
}

