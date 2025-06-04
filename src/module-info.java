/**
 * Módulo principal del Sistema de Ventas
 */
open module SistemsaVentas {
    requires transitive java.sql;
    requires java.desktop;
    requires java.logging;
    requires java.prefs;

    // Exportar paquetes
    exports conexion;
}

