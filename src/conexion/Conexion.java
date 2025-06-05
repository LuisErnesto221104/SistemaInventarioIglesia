package conexion;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.io.File;
/**
 * Clase Conexion
 * Esta clase maneja la conexión a la base de datos.
 */


public class Conexion {    Connection conexion = null; // Objeto de conexión a la base de datos
    Statement sentencia = null; // Objeto para ejecutar sentencias SQL
    
    public Conexion(){
        try {
            // Cargar el driver explícitamente para asegurarnos que está disponible
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");// Cargar el driver UCanAccess
                System.out.println("Driver UCanAccess cargado correctamente"); // Mensaje de éxito al cargar el driver
            } catch (ClassNotFoundException e) {
                System.out.println(" ERROR: No se pudo cargar el driver UCanAccess"); // Mensaje de error si no se encuentra el driver
                System.out.println("Detalles: " + e.getMessage()); // Mostrar detalles del error
                e.printStackTrace(); // Imprimir el stack trace para depuración
            }            // Obtener la ruta absoluta al directorio actual de trabajo
            String directorioActual = System.getProperty("user.dir");
            String ruta = directorioActual + File.separator + "bd" + File.separator + "dbcaja.mdb"; // Ruta absoluta de la base de datos Access            // Verificar si el archivo de la BD existe
            File archivoBD = new File(ruta);
            if (!archivoBD.exists()) {
                System.out.println("ADVERTENCIA: El archivo de la base de datos no existe en: " + ruta);
                JOptionPane.showMessageDialog(null, "El archivo de la base de datos no existe en: " + ruta, 
                                            "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                throw new Exception("Archivo de base de datos no encontrado: " + ruta);
            } else {
                System.out.println("Archivo de base de datos encontrado en: " + ruta);
            }
            
            String url = "jdbc:ucanaccess://" + ruta; // URL de conexión a la base de datos
            System.out.println("Intentando conectar a la base de datos: " + url); // Mensaje informativo antes de intentar la conexión
            
            // Intentar la conexión
            conexion = DriverManager.getConnection(url); // Establecer la conexión a la base de datos
            // Si la conexión es exitosa, imprimir un mensaje
            System.out.println("Conexión establecida correctamente");
            sentencia = conexion.createStatement(); // Crear un objeto Statement para ejecutar sentencias SQL
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage()); // Mostrar un mensaje de error si la conexión falla
        }
    }

    // Método para obtener la conexión
    public Connection getConexion() {
        return conexion; // Retorna el objeto de conexión a la base de datos
    }

    // Método para la desconexión de la base de datos
    public void Desconexion(){
        try{
            conexion.close(); // Cierra la conexión a la base de datos
            System.out.println("Desconectado de la base de datos"); // Mensaje informativo al cerrar la conexión

        }catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex); // Registra el error en el log si ocurre un problema al cerrar la conexión
            JOptionPane.showMessageDialog(null, "Error al desconectar de la base de datos: " + ex.getMessage()); // Muestra un mensaje de error si ocurre un problema al cerrar la conexión
        }
    }

}
