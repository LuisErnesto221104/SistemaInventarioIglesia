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

    Connection conexion = null;
    Statement sentencia = null;    public Conexion(){
        try {
            // Cargar el driver explícitamente para asegurarnos que está disponible
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                System.out.println("Driver UCanAccess cargado correctamente");
            } catch (ClassNotFoundException e) {
                System.out.println(" ERROR: No se pudo cargar el driver UCanAccess");
                System.out.println("Detalles: " + e.getMessage());
                e.printStackTrace();
            }
            
            String ruta = "SistemsaVentas\\bd\\dbcaja.mdb";
            String url = "jdbc:ucanaccess://" + ruta;
            System.out.println("Intentando conectar a la base de datos: " + url);
            
            // Intentar la conexión
            conexion = DriverManager.getConnection(url);
            System.out.println("Conexión establecida correctamente");
            sentencia = conexion.createStatement();
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void Desconexion(){
        try{
            conexion.close();
            System.out.println("Desconectado de la base de datos");

        }catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al desconectar de la base de datos: " + ex.getMessage());
        }
    }

}
