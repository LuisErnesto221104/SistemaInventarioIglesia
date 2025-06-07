package dao;

import conexion.Conexion;
import java.sql.*;
import java.util.*;

/**
 * SocioDAO
 * Clase para gestionar el acceso a datos de la tabla Socios
 */
public class SocioDAO {
    
    private Conexion conexion; // Objeto de conexión a la base de datos
    
    // Constructor de la clase SocioDAO
    public SocioDAO() {
        conexion = new Conexion(); //Inicializacion de la conexion 
    }
    
    /**
     * Obtiene la lista de todos los socios adultos
     * @return Lista de socios
     */
    public List<Map<String, Object>> listarSocios() {
        List<Map<String, Object>> listaSocios = new ArrayList<>(); // Lista para almacenar los socios
        
        try {
            // Consulta SQL para obtener los datos de los socios
            String consulta = "SELECT NoSocio, Nombres, Apellidos, Direccion, Telefono, FechaRegistro, PresentadoPor, Poblacion FROM Socios ORDER BY NoSocio";
            Statement statement = conexion.getConexion().createStatement(); // Crear un objeto Statement para ejecutar la consulta
            ResultSet resultado = statement.executeQuery(consulta); // Ejecutar la consulta y obtener los resultados
            
            // Iterar sobre los resultados y llenar la lista de socios 
            while (resultado.next()) {
                // Crear un mapa para almacenar los datos de cada socio
                Map<String, Object> socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio")); // Número de socio
                socio.put("Nombres", resultado.getString("Nombres")); // Nombres del socio
                socio.put("Apellidos", resultado.getString("Apellidos")); // Apellidos del socio
                socio.put("Direccion", resultado.getString("Direccion")); // Dirección del socio
                socio.put("Telefono", resultado.getString("Telefono")); // Teléfono del socio
                socio.put("FechaRegistro", resultado.getDate("FechaRegistro")); // Fecha de registro del socio
                socio.put("PrestadoPor", resultado.getString("PresentadoPor")); // Persona que presentó al socio
                socio.put("Poblacion", resultado.getString("Poblacion")); //Lugar de Nacimiento del socio 
                
                listaSocios.add(socio); // Añadir el mapa del socio a la lista
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el Statement
            
        } catch (SQLException e) {
            System.err.println("Error al listar socios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaSocios; // Retornar la lista de socios obtenida
    }
    
    /**
     * Obtiene la lista de todos los socios infantiles
     * @return Lista de socios infantiles
     */
    public List<Map<String, Object>> listarSociosInfantiles() {
        List<Map<String, Object>> listaSociosInfa = new ArrayList<>(); // Lista para almacenar los socios infantiles
        
        try {
            // Consulta SQL para obtener los datos de los socios infantiles
            String consulta = "SELECT NoSocio, Fecha, Nombres, Apellidos, Direccion, Telefono, PresentadoPor, Poblacion FROM SociosInfa ORDER BY NoSocio";
            Statement statement = conexion.getConexion().createStatement(); // Crear un objeto Statement para ejecutar la consulta
            ResultSet resultado = statement.executeQuery(consulta); // Ejecutar la consulta y obtener los resultados
            
            // Iterar sobre los resultados y llenar la lista de socios infantiles
            while (resultado.next()) {
                Map<String, Object> socio = new HashMap<>(); // Crear un mapa para almacenar los datos de cada socio infantil
                socio.put("NoSocio", resultado.getInt("NoSocio")); // Número de socio infantil
                socio.put("Fecha", resultado.getDate("Fecha")); // Fecha de registro del socio infantil
                socio.put("Nombres", resultado.getString("Nombres")); // Nombres del socio infantil
                socio.put("Apellidos", resultado.getString("Apellidos")); // Apellidos del socio infantil
                socio.put("Direccion", resultado.getString("Direccion")); // Dirección del socio infantil
                socio.put("Telefono", resultado.getString("Telefono")); // Teléfono del socio infantil
                socio.put("PresentadoPor", resultado.getString("PresentadoPor")); // Persona que presentó al socio infantil
                socio.put("Poblacion", resultado.getString("Poblacion")); // Lugar de Nacimiento del socio infantil
                
                listaSociosInfa.add(socio); // Añadir el mapa del socio infantil a la lista
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el Statement
            
        } catch (SQLException e) {
            System.err.println("Error al listar socios infantiles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return listaSociosInfa; // Retornar la lista de socios infantiles obtenida
    }
    
    /**
     * Obtiene el último número de socio adulto
     * @return Último número de socio adulto
     */
    public int obtenerUltimoNumeroSocioAdulto() {
        int ultimoNumero = 0;
        
        try {
            String consulta = "SELECT MAX(NoSocio) AS UltimoNumero FROM Socios";
            Statement statement = conexion.getConexion().createStatement();
            ResultSet resultado = statement.executeQuery(consulta);
            
            if (resultado.next()) {
                ultimoNumero = resultado.getInt("UltimoNumero");
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener el último número de socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ultimoNumero;
    }
    
    /**
     * Obtiene el último número de socio infantil
     * @return Último número de socio infantil
     */
    public int obtenerUltimoNumeroSocioInfantil() {
        int ultimoNumero = 0;
        
        try {
            String consulta = "SELECT MAX(NoSocio) AS UltimoNumero FROM SociosInfa";
            Statement statement = conexion.getConexion().createStatement();
            ResultSet resultado = statement.executeQuery(consulta);
            
            if (resultado.next()) {
                ultimoNumero = resultado.getInt("UltimoNumero");
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener el último número de socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ultimoNumero;
    }
    
    /**
     * Inserta un nuevo socio adulto
     * @param noSocio Número de socio
     * @param nombres Nombres del socio
     * @param apellidos Apellidos del socio
     * @param direccion Dirección del socio
     * @param telefono Teléfono del socio
     * @param fechaRegistro Fecha de registro del socio
     * @param presentadoPor Persona que presentó al socio
     * @param poblacion Población del socio
     * @return true si se insertó correctamente, false en caso contrario
     */    public boolean insertarSocioAdulto(int noSocio, String nombres, String apellidos, 
            String direccion, String telefono, java.util.Date fechaRegistro, 
            String presentadoPor, String poblacion) {
        
        boolean exito = false;
        
        try {
            String consulta = "INSERT INTO Socios (NoSocio, Nombres, Apellidos, Direccion, " +
                    "Telefono, FechaRegistro, PresentadoPor, Poblacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setInt(1, noSocio);
            statement.setString(2, nombres);
            statement.setString(3, apellidos);
            statement.setString(4, direccion);
            statement.setString(5, telefono);
            statement.setDate(6, new java.sql.Date(fechaRegistro.getTime()));
            statement.setString(7, presentadoPor);
            statement.setString(8, poblacion);
            
            int filasInsertadas = statement.executeUpdate();
            
            exito = (filasInsertadas > 0);
            
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al insertar socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito;
    }
    
    /**
     * Inserta un nuevo socio infantil
     * @param noSocio Número de socio
     * @param fecha Fecha de registro
     * @param nombres Nombres del socio
     * @param apellidos Apellidos del socio
     * @param direccion Dirección del socio
     * @param telefono Teléfono del socio
     * @param presentadoPor Persona que presentó al socio
     * @param poblacion Población del socio
     * @return true si se insertó correctamente, false en caso contrario
     */    public boolean insertarSocioInfantil(int noSocio, java.util.Date fecha, String nombres, String apellidos, 
            String direccion, String telefono, String presentadoPor, String poblacion) {
        
        boolean exito = false;
        
        try {
            String consulta = "INSERT INTO SociosInfa (NoSocio, Fecha, Nombres, Apellidos, " +
                    "Direccion, Telefono, PresentadoPor, Poblacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setInt(1, noSocio);
            statement.setDate(2, new java.sql.Date(fecha.getTime()));
            statement.setString(3, nombres);
            statement.setString(4, apellidos);
            statement.setString(5, direccion);
            statement.setString(6, telefono);
            statement.setString(7, presentadoPor);
            statement.setString(8, poblacion);
            
            int filasInsertadas = statement.executeUpdate();
            
            exito = (filasInsertadas > 0);
            
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al insertar socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito;
    }
    
    /**
     * Busca un socio adulto por su número de socio
     * @param noSocio Número de socio
     * @return Mapa con los datos del socio o null si no se encuentra
     */
    public Map<String, Object> buscarSocioAdultoPorID(int noSocio) {
        Map<String, Object> socio = null;
        
        try {
            // Consulta SQL para obtener los datos del socio
            String consulta = "SELECT NoSocio, Nombres, Apellidos, Direccion, Telefono, FechaRegistro, PresentadoPor, Poblacion " +
                              "FROM Socios WHERE NoSocio = ?";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setInt(1, noSocio);
            
            ResultSet resultado = statement.executeQuery();
            
            // Si hay resultados, crear el mapa de datos del socio
            if (resultado.next()) {
                socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio"));
                socio.put("Nombres", resultado.getString("Nombres"));
                socio.put("Apellidos", resultado.getString("Apellidos"));
                socio.put("Direccion", resultado.getString("Direccion"));
                socio.put("Telefono", resultado.getString("Telefono"));
                socio.put("FechaRegistro", resultado.getDate("FechaRegistro"));
                socio.put("PresentadoPor", resultado.getString("PresentadoPor"));
                socio.put("Poblacion", resultado.getString("Poblacion"));
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socio;
    }
    
    /**
     * Busca un socio infantil por su número de socio
     * @param noSocio Número de socio
     * @return Mapa con los datos del socio o null si no se encuentra
     */
    public Map<String, Object> buscarSocioInfantilPorID(int noSocio) {
        Map<String, Object> socio = null;
        
        try {
            // Consulta SQL para obtener los datos del socio infantil
            String consulta = "SELECT NoSocio, Fecha, Nombres, Apellidos, Direccion, Telefono, PresentadoPor, Poblacion " +
                              "FROM SociosInfa WHERE NoSocio = ?";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setInt(1, noSocio);
            
            ResultSet resultado = statement.executeQuery();
            
            // Si hay resultados, crear el mapa de datos del socio infantil
            if (resultado.next()) {
                socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio"));
                socio.put("Fecha", resultado.getDate("Fecha"));
                socio.put("Nombres", resultado.getString("Nombres"));
                socio.put("Apellidos", resultado.getString("Apellidos"));
                socio.put("Direccion", resultado.getString("Direccion"));
                socio.put("Telefono", resultado.getString("Telefono"));
                socio.put("PresentadoPor", resultado.getString("PresentadoPor"));
                socio.put("Poblacion", resultado.getString("Poblacion"));
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al buscar socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socio;
    }
    
    /**
     * Busca socios adultos por nombre y/o apellido
     * @param nombre Nombre del socio (puede ser vacío)
     * @param apellido Apellido del socio (puede ser vacío)
     * @return Lista de socios encontrados
     */
    public List<Map<String, Object>> buscarSociosAdultosPorNombre(String nombre, String apellido) {
        List<Map<String, Object>> socios = new ArrayList<>();
        StringBuilder consulta = new StringBuilder("SELECT NoSocio, Nombres, Apellidos, Direccion, Telefono, FechaRegistro, PresentadoPor, Poblacion FROM Socios WHERE 1=1");
        
        if (nombre != null && !nombre.isEmpty()) {
            consulta.append(" AND Nombres LIKE ?");
        }
        
        if (apellido != null && !apellido.isEmpty()) {
            consulta.append(" AND Apellidos LIKE ?");
        }
        
        consulta.append(" ORDER BY NoSocio");
        
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta.toString());
            int index = 1;
            
            if (nombre != null && !nombre.isEmpty()) {
                statement.setString(index++, "%" + nombre + "%");
            }
            
            if (apellido != null && !apellido.isEmpty()) {
                statement.setString(index++, "%" + apellido + "%");
            }
            
            ResultSet resultado = statement.executeQuery();
            
            while (resultado.next()) {
                Map<String, Object> socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio"));
                socio.put("Nombres", resultado.getString("Nombres"));
                socio.put("Apellidos", resultado.getString("Apellidos"));
                socio.put("Direccion", resultado.getString("Direccion"));
                socio.put("Telefono", resultado.getString("Telefono"));
                socio.put("FechaRegistro", resultado.getDate("FechaRegistro"));
                socio.put("PresentadoPor", resultado.getString("PresentadoPor"));
                socio.put("Poblacion", resultado.getString("Poblacion"));
                
                socios.add(socio);
            }
            
            resultado.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error al buscar socios adultos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socios;
    }
    
    /**
     * Busca socios infantiles por nombre y/o apellido
     * @param nombre Nombre del socio infantil (puede ser vacío)
     * @param apellido Apellido del socio infantil (puede ser vacío)
     * @return Lista de socios infantiles encontrados
     */
    public List<Map<String, Object>> buscarSociosInfantilesPorNombre(String nombre, String apellido) {
        List<Map<String, Object>> socios = new ArrayList<>();
        StringBuilder consulta = new StringBuilder("SELECT NoSocio, Fecha, Nombres, Apellidos, Direccion, Telefono, PresentadoPor, Poblacion FROM SociosInfa WHERE 1=1");
        
        if (nombre != null && !nombre.isEmpty()) {
            consulta.append(" AND Nombres LIKE ?");
        }
        
        if (apellido != null && !apellido.isEmpty()) {
            consulta.append(" AND Apellidos LIKE ?");
        }
        
        consulta.append(" ORDER BY NoSocio");
        
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta.toString());
            int index = 1;
            
            if (nombre != null && !nombre.isEmpty()) {
                statement.setString(index++, "%" + nombre + "%");
            }
            
            if (apellido != null && !apellido.isEmpty()) {
                statement.setString(index++, "%" + apellido + "%");
            }
            
            ResultSet resultado = statement.executeQuery();
            
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
                
                socios.add(socio);
            }
            
            resultado.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error al buscar socios infantiles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socios;
    }
    
    /**
     * Actualiza los datos de un socio adulto
     * @param noSocio Número de socio
     * @param nombres Nombres del socio
     * @param apellidos Apellidos del socio
     * @param direccion Dirección del socio
     * @param telefono Teléfono del socio
     * @param fechaRegistro Fecha de registro
     * @param presentadoPor Persona que presentó al socio
     * @param poblacion Población del socio
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarSocioAdulto(int noSocio, String nombres, String apellidos, String direccion, 
            String telefono, java.util.Date fechaRegistro, String presentadoPor, String poblacion) {
        
        boolean exito = false;
        
        try {
            String consulta = "UPDATE Socios SET Nombres = ?, Apellidos = ?, Direccion = ?, " +
                    "Telefono = ?, FechaRegistro = ?, PresentadoPor = ?, Poblacion = ? " +
                    "WHERE NoSocio = ?";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setString(1, nombres);
            statement.setString(2, apellidos);
            statement.setString(3, direccion);
            statement.setString(4, telefono);
            statement.setDate(5, new java.sql.Date(fechaRegistro.getTime()));
            statement.setString(6, presentadoPor);
            statement.setString(7, poblacion);
            statement.setInt(8, noSocio);
            
            int filasActualizadas = statement.executeUpdate();
            
            exito = (filasActualizadas > 0);
            
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito;
    }
    
    /**
     * Actualiza los datos de un socio infantil
     * @param noSocio Número de socio
     * @param fecha Fecha de registro
     * @param nombres Nombres del socio
     * @param apellidos Apellidos del socio
     * @param direccion Dirección del socio
     * @param telefono Teléfono del socio
     * @param presentadoPor Persona que presentó al socio
     * @param poblacion Población del socio
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarSocioInfantil(int noSocio, java.util.Date fecha, String nombres, String apellidos, 
            String direccion, String telefono, String presentadoPor, String poblacion) {
        
        boolean exito = false;
        
        try {
            String consulta = "UPDATE SociosInfa SET Fecha = ?, Nombres = ?, Apellidos = ?, " +
                    "Direccion = ?, Telefono = ?, PresentadoPor = ?, Poblacion = ? " +
                    "WHERE NoSocio = ?";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setDate(1, new java.sql.Date(fecha.getTime()));
            statement.setString(2, nombres);
            statement.setString(3, apellidos);
            statement.setString(4, direccion);
            statement.setString(5, telefono);
            statement.setString(6, presentadoPor);
            statement.setString(7, poblacion);
            statement.setInt(8, noSocio);
            
            int filasActualizadas = statement.executeUpdate();
            
            exito = (filasActualizadas > 0);
            
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito;
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        conexion.Desconexion(); // Cerrar la conexión a la base de datos
    }
}
