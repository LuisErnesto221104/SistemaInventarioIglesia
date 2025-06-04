package dao;

import conexion.Conexion;
import java.sql.*;

/**
 * UsuarioDAO
 * Clase para gestionar el acceso a datos de la tabla Usuarios
 */
public class UsuarioDAO {
    
    private Conexion conexion;
    
    public UsuarioDAO() {
        conexion = new Conexion();
    }
    
    /**
     * Verifica si las credenciales de un usuario son válidas
     * @param usuario ID del usuario
     * @param clave Clave del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean validarUsuario(String usuario, String clave) {
        boolean esValido = false;
        
        try {
            String consulta = "SELECT * FROM Usuarios WHERE id = ? AND clave = ?";
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setString(1, usuario);
            statement.setString(2, clave);
            
            ResultSet resultado = statement.executeQuery();
            
            if (resultado.next()) {
                esValido = true;
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return esValido;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        conexion.Desconexion();
    }
}
