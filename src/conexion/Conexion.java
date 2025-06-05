package conexion;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 * Clase Conexion
 * Esta clase maneja la conexión a la base de datos.
 */


public class Conexion {

    Connection conexion = null; // Objeto de conexión a la base de datos
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
            }
            
            String ruta = "SistemsaVentas\\bd\\dbcaja.mdb"; // Ruta de la base de datos Access
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
