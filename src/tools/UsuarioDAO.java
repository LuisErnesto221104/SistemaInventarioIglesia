package tools;

import java.sql.*;

import connection.Conexion;

/**
 * UsuarioDAO
 * Clase para gestionar el acceso a datos de la tabla Usuarios
 */
public class UsuarioDAO {
    
    private Conexion conexion; // Objeto de conexión a la base de datos
    
    // Constructor de la clase UsuarioDAO
    public UsuarioDAO() {
        conexion = new Conexion(); // Inicializa la conexión a la base de datos
    }
    
    /**
     * Verifica si las credenciales de un usuario son válidas
     * @param usuario ID del usuario
     * @param clave Clave del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean validarUsuario(String usuario, String clave) {
        boolean esValido = false; // Variable para almacenar el resultado de la validación
        
        try {
            String consulta = "SELECT * FROM Usuarios WHERE id = ? AND clave = ?"; // Consulta SQL para verificar las credenciales del usuario
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Prepara la sentencia SQL para evitar inyecciones SQL
            statement.setString(1, usuario); // Establece el primer parámetro de la consulta (ID del usuario)
            statement.setString(2, clave);// Establece el segundo parámetro de la consulta (clave del usuario)
            
            ResultSet resultado = statement.executeQuery(); // Ejecuta la consulta y obtiene el resultado
            
            // Verifica si se obtuvo algún resultado
            if (resultado.next()) {
                esValido = true; // Si hay un resultado, las credenciales son válidas
            }
            
            resultado.close(); // Cierra el ResultSet para liberar recursos
            statement.close(); // Cierra el PreparedStatement para liberar recursos
            
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
