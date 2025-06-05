package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.*;

/**
 * SocioDAO
 * Clase para gestionar el acceso a datos de la tabla Socios
 */
public class SocioDAO {
    
    private Conexion conexion;
    
    public SocioDAO() {
        conexion = new Conexion();
    }
    
    /**
     * Obtiene la lista de todos los socios adultos
     * @return Lista de socios
     */
    public List<Map<String, Object>> listarSocios() {
        List<Map<String, Object>> listaSocios = new ArrayList<>();
        
        try {
            String consulta = "SELECT NoSocio, Nombres, Apellidos, Direccion, Telefono, FechaRegistro, PresentadoPor, Poblacion FROM Socios ORDER BY NoSocio";
            Statement statement = conexion.getConexion().createStatement();
            ResultSet resultado = statement.executeQuery(consulta);
            
            while (resultado.next()) {
                Map<String, Object> socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio"));
                socio.put("Nombres", resultado.getString("Nombres"));
                socio.put("Apellidos", resultado.getString("Apellidos"));
                socio.put("Direccion", resultado.getString("Direccion"));
                socio.put("Telefono", resultado.getString("Telefono"));
                socio.put("FechaRegistro", resultado.getDate("FechaRegistro"));
                socio.put("PrestadoPor", resultado.getString("PresentadoPor"));
                socio.put("Poblacion", resultado.getString("Poblacion"));
                
                listaSocios.add(socio);
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al listar socios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaSocios;
    }
    
    /**
     * Obtiene la lista de todos los socios infantiles
     * @return Lista de socios infantiles
     */
    public List<Map<String, Object>> listarSociosInfantiles() {
        List<Map<String, Object>> listaSociosInfa = new ArrayList<>();
        
        try {
            String consulta = "SELECT NoSocio, Fecha, Nombres, Apellidos, Direccion, Telefono, PresentadoPor, Poblacion FROM SociosInfa ORDER BY NoSocio";
            Statement statement = conexion.getConexion().createStatement();
            ResultSet resultado = statement.executeQuery(consulta);
            
            while (resultado.next()) {
                Map<String, Object> socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio"));
                socio.put("Fecha", resultado.getDate("Fecha"));
                socio.put("Nombres", resultado.getString("Nombres"));
                socio.put("Apellidos", resultado.getString("Apellidos"));
                socio.put("Direccion", resultado.getString("Direccion"));
                socio.put("Telefono", resultado.getString("Telefono"));
                socio.put("PresentadoPor", resultado.getString("PresentadoPor"));
                socio.put("Poblacion", resultado.getString("Poblacion"));
                
                listaSociosInfa.add(socio);
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al listar socios infantiles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaSociosInfa;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        conexion.Desconexion();
    }
}
