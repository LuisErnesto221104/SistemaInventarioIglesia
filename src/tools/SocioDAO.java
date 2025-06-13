package tools;

import java.sql.*;
import java.util.*;

import connection.Conexion;

/**
 * SocioDAO
 * Clase para gestionar el acceso a datos de la tabla Socios
 */
public class SocioDAO {
    
    private static final double TASA_INTERES_PRESTAMO = 0.04; // 1% - tasa mensual de interés para préstamos
    
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
            String consulta = "SELECT MAX(NoSocio) AS UltimoNumero FROM Socios"; // Consulta SQL para obtener el último número de socio adulto
            Statement statement = conexion.getConexion().createStatement(); // Crear un objeto Statement para ejecutar la consulta
            ResultSet resultado = statement.executeQuery(consulta); // Ejecutar la consulta y obtener los resultados

            // Si hay resultados, obtener el último número de socio
            if (resultado.next()) {
                ultimoNumero = resultado.getInt("UltimoNumero"); 
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el Statement
            

        } catch (SQLException e) {
            System.err.println("Error al obtener el último número de socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ultimoNumero; // Retornar el último número de socio adulto obtenido
    }
    
    /**
     * Obtiene el último número de socio infantil
     * @return Último número de socio infantil
     */
    public int obtenerUltimoNumeroSocioInfantil() {
        int ultimoNumero = 0; // Inicializar el último número de socio infantil
        
        try {
            String consulta = "SELECT MAX(NoSocio) AS UltimoNumero FROM SociosInfa"; // Consulta SQL para obtener el último número de socio infantil
            Statement statement = conexion.getConexion().createStatement(); // Crear un objeto Statement para ejecutar la consulta
            ResultSet resultado = statement.executeQuery(consulta); // Ejecutar la consulta y obtener los resultados
            
            // Si hay resultados, obtener el último número de socio infantil
            if (resultado.next()) {
                ultimoNumero = resultado.getInt("UltimoNumero");
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el Statement
            
        } catch (SQLException e) {
            System.err.println("Error al obtener el último número de socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Si no se encontró ningún socio infantil, retornar 0
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
     */    
    public boolean insertarSocioAdulto(int noSocio, String nombres, String apellidos, 
            String direccion, String telefono, java.util.Date fechaRegistro, 
            String presentadoPor, String poblacion) {
        
        boolean exito = false; // Variable para indicar si la inserción fue exitosa
        
        try {
            String consulta = "INSERT INTO Socios (NoSocio, Nombres, Apellidos, Direccion, " +
                    "Telefono, FechaRegistro, PresentadoPor, Poblacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; // Consulta SQL para insertar un nuevo socio adulto
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Crear un objeto PreparedStatement para ejecutar la consulta
            statement.setInt(1, noSocio); // Establecer el número de socio
            statement.setString(2, nombres); // Establecer los nombres del socio
            statement.setString(3, apellidos); // Establecer los apellidos del socio
            statement.setString(4, direccion); // Establecer la dirección del socio
            statement.setString(5, telefono); // Establecer el teléfono del socio
            statement.setDate(6, new java.sql.Date(fechaRegistro.getTime())); // Establecer la fecha de registro del socio
            statement.setString(7, presentadoPor); // Establecer la persona que presentó al socio
            statement.setString(8, poblacion); // Establecer la población del socio
            
            int filasInsertadas = statement.executeUpdate(); // Ejecutar la consulta de inserción y obtener el número de filas afectadas
            
            exito = (filasInsertadas > 0); // Si se insertó al menos una fila, la inserción fue exitosa
            
            statement.close(); // Cerrar el PreparedStatement
            
        } catch (SQLException e) {
            System.err.println("Error al insertar socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito; // Retornar el resultado de la inserción
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
     */    
    public boolean insertarSocioInfantil(int noSocio, java.util.Date fecha, String nombres, String apellidos, 
            String direccion, String telefono, String presentadoPor, String poblacion) {
        
        boolean exito = false; // Variable para indicar si la inserción fue exitosa
        
        try {
            // Consulta SQL para insertar un nuevo socio infantil
            String consulta = "INSERT INTO SociosInfa (NoSocio, Fecha, Nombres, Apellidos, " +
                    "Direccion, Telefono, PresentadoPor, Poblacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; 
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Crear un objeto PreparedStatement para ejecutar la consulta
            statement.setInt(1, noSocio); // Establecer el número de socio infantil
            statement.setDate(2, new java.sql.Date(fecha.getTime())); // Establecer la fecha de registro del socio infantil
            statement.setString(3, nombres); // Establecer los nombres del socio infantil
            statement.setString(4, apellidos); // Establecer los apellidos del socio infantil
            statement.setString(5, direccion); // Establecer la dirección del socio infantil
            statement.setString(6, telefono); // Establecer el teléfono del socio infantil
            statement.setString(7, presentadoPor); // Establecer la persona que presentó al socio infantil
            statement.setString(8, poblacion); // Establecer la población del socio infantil
            
            int filasInsertadas = statement.executeUpdate(); // Ejecutar la consulta de inserción y obtener el número de filas afectadas
            
            exito = (filasInsertadas > 0); // Si se insertó al menos una fila, la inserción fue exitosa
            
            statement.close(); // Cerrar el PreparedStatement
            
        } catch (SQLException e) {
            System.err.println("Error al insertar socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito; // Retornar el resultado de la inserción
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
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Crear un objeto PreparedStatement para ejecutar la consulta
            statement.setInt(1, noSocio); // Establecer el número de socio en la consulta
            
            ResultSet resultado = statement.executeQuery(); // Ejecutar la consulta y obtener los resultados
            
            // Si hay resultados, crear el mapa de datos del socio
            if (resultado.next()) {
                socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio")); // Número de socio adulto
                socio.put("Nombres", resultado.getString("Nombres")); // Nombres del socio adulto
                socio.put("Apellidos", resultado.getString("Apellidos")); // Apellidos del socio adulto
                socio.put("Direccion", resultado.getString("Direccion")); // Dirección del socio adulto
                socio.put("Telefono", resultado.getString("Telefono")); // Teléfono del socio adulto
                socio.put("FechaRegistro", resultado.getDate("FechaRegistro")); // Fecha de registro del socio adulto
                socio.put("PresentadoPor", resultado.getString("PresentadoPor")); // Persona que presentó al socio adulto
                socio.put("Poblacion", resultado.getString("Poblacion")); // Lugar de Nacimiento del socio adulto
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el PreparedStatement
            
        } catch (SQLException e) {
            System.err.println("Error al buscar socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socio; // Retornar el mapa con los datos del socio adulto o null si no se encontró
    }
    
    /**
     * Busca un socio infantil por su número de socio
     * @param noSocio Número de socio
     * @return Mapa con los datos del socio o null si no se encuentra
     */
    public Map<String, Object> buscarSocioInfantilPorID(int noSocio) {
        Map<String, Object> socio = null; // Mapa para almacenar los datos del socio infantil
        
        try {
            // Consulta SQL para obtener los datos del socio infantil
            String consulta = "SELECT NoSocio, Fecha, Nombres, Apellidos, Direccion, Telefono, PresentadoPor, Poblacion " +
                              "FROM SociosInfa WHERE NoSocio = ?";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Crear un objeto PreparedStatement para ejecutar la consulta
            statement.setInt(1, noSocio); // Establecer el número de socio en la consulta
            
            ResultSet resultado = statement.executeQuery(); // Ejecutar la consulta y obtener los resultados
            
            // Si hay resultados, crear el mapa de datos del socio infantil
            if (resultado.next()) {
                socio = new HashMap<>();
                socio.put("NoSocio", resultado.getInt("NoSocio")); // Número de socio infantil
                socio.put("Fecha", resultado.getDate("Fecha")); // Fecha de registro del socio infantil
                socio.put("Nombres", resultado.getString("Nombres")); // Nombres del socio infantil
                socio.put("Apellidos", resultado.getString("Apellidos")); // Apellidos del socio infantil
                socio.put("Direccion", resultado.getString("Direccion")); // Dirección del socio infantil
                socio.put("Telefono", resultado.getString("Telefono")); // Teléfono del socio infantil
                socio.put("PresentadoPor", resultado.getString("PresentadoPor")); // Persona que presentó al socio infantil
                socio.put("Poblacion", resultado.getString("Poblacion")); // Lugar de Nacimiento del socio infantil
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el PreparedStatement
            
        } catch (SQLException e) {
            System.err.println("Error al buscar socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socio; // Retornar el mapa con los datos del socio infantil o null si no se encontró
    }
    
    /**
     * Busca socios adultos por nombre y/o apellido
     * @param nombre Nombre del socio (puede ser vacío)
     * @param apellido Apellido del socio (puede ser vacío)
     * @return Lista de socios encontrados
     */
    public List<Map<String, Object>> buscarSociosAdultosPorNombre(String nombre, String apellido) {
        List<Map<String, Object>> socios = new ArrayList<>(); // Lista para almacenar los socios encontrados
        StringBuilder consulta = new StringBuilder("SELECT NoSocio, Nombres, Apellidos, Direccion, Telefono, FechaRegistro, PresentadoPor, Poblacion FROM Socios WHERE 1=1"); // Consulta SQL base para buscar socios adultos
        
        // Añadir condiciones a la consulta según los parámetros de búsqueda
        if (nombre != null && !nombre.isEmpty()) { 
            consulta.append(" AND Nombres LIKE ?"); // Si se proporciona un nombre, añadir condición de búsqueda
        }

        // Si se proporciona un apellido, añadir condición de búsqueda
        if (apellido != null && !apellido.isEmpty()) {
            consulta.append(" AND Apellidos LIKE ?"); // Añadir condición de búsqueda para el apellido
        }
        
        consulta.append(" ORDER BY NoSocio"); // Ordenar los resultados por número de socio
        
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta.toString()); // Crear un objeto PreparedStatement para ejecutar la consulta
            int index = 1; // Índice para los parámetros de la consulta
            
            // Establecer los parámetros de búsqueda en la consulta
            if (nombre != null && !nombre.isEmpty()) {
                statement.setString(index++, "%" + nombre + "%"); // Añadir el nombre con comodines para búsqueda parcial
            }
            
            // Si se proporciona un apellido, establecer el parámetro en la consulta
            if (apellido != null && !apellido.isEmpty()) {
                statement.setString(index++, "%" + apellido + "%"); // Añadir el apellido con comodines para búsqueda parcial
            }
            
            ResultSet resultado = statement.executeQuery(); // Ejecutar la consulta y obtener los resultados
            
            // Iterar sobre los resultados y llenar la lista de socios encontrados
            while (resultado.next()) {
                Map<String, Object> socio = new HashMap<>(); // Crear un mapa para almacenar los datos de cada socio encontrado
                socio.put("NoSocio", resultado.getInt("NoSocio")); // Número de socio adulto
                socio.put("Nombres", resultado.getString("Nombres")); // Nombres del socio adulto
                socio.put("Apellidos", resultado.getString("Apellidos")); // Apellidos del socio adulto
                socio.put("Direccion", resultado.getString("Direccion")); // Dirección del socio adulto
                socio.put("Telefono", resultado.getString("Telefono")); // Teléfono del socio adulto
                socio.put("FechaRegistro", resultado.getDate("FechaRegistro")); // Fecha de registro del socio adulto
                socio.put("PresentadoPor", resultado.getString("PresentadoPor")); // Persona que presentó al socio adulto
                socio.put("Poblacion", resultado.getString("Poblacion")); // Lugar de Nacimiento del socio adulto
                
                socios.add(socio); // Añadir el mapa del socio a la lista de socios encontrados
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el PreparedStatement
        } catch (SQLException e) {
            System.err.println("Error al buscar socios adultos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socios; // Retornar la lista de socios encontrados
    }
    
    /**
     * Busca socios infantiles por nombre y/o apellido
     * @param nombre Nombre del socio infantil (puede ser vacío)
     * @param apellido Apellido del socio infantil (puede ser vacío)
     * @return Lista de socios infantiles encontrados
     */
    public List<Map<String, Object>> buscarSociosInfantilesPorNombre(String nombre, String apellido) {
        List<Map<String, Object>> socios = new ArrayList<>(); // Lista para almacenar los socios infantiles encontrados
        StringBuilder consulta = new StringBuilder("SELECT NoSocio, Fecha, Nombres, Apellidos, Direccion, Telefono, PresentadoPor, Poblacion FROM SociosInfa WHERE 1=1"); // Consulta SQL base para buscar socios infantiles
        
        // Añadir condiciones a la consulta según los parámetros de búsqueda
        if (nombre != null && !nombre.isEmpty()) {
            consulta.append(" AND Nombres LIKE ?"); // Si se proporciona un nombre, añadir condición de búsqueda
        }
        
        // Si se proporciona un apellido, añadir condición de búsqueda
        if (apellido != null && !apellido.isEmpty()) {
            consulta.append(" AND Apellidos LIKE ?"); // Añadir condición de búsqueda para el apellido
        }
        
        consulta.append(" ORDER BY NoSocio"); // Ordenar los resultados por número de socio infantil
        
        try {
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta.toString()); // Crear un objeto PreparedStatement para ejecutar la consulta
            int index = 1; // Índice para los parámetros de la consulta
            
            // Establecer los parámetros de búsqueda en la consulta
            if (nombre != null && !nombre.isEmpty()) {
                statement.setString(index++, "%" + nombre + "%"); // Añadir el nombre con comodines para búsqueda parcial
            }
            
            // Si se proporciona un apellido, establecer el parámetro en la consulta
            if (apellido != null && !apellido.isEmpty()) {
                statement.setString(index++, "%" + apellido + "%"); // Añadir el apellido con comodines para búsqueda parcial
            }
            
            ResultSet resultado = statement.executeQuery(); // Ejecutar la consulta y obtener los resultados
            
            while (resultado.next()) {
                Map<String, Object> socio = new HashMap<>(); // Crear un mapa para almacenar los datos de cada socio infantil encontrado
                socio.put("NoSocio", resultado.getInt("NoSocio")); // Número de socio infantil
                socio.put("Fecha", resultado.getDate("Fecha")); // Fecha de registro del socio infantil
                socio.put("Nombres", resultado.getString("Nombres")); // Nombres del socio infantil
                socio.put("Apellidos", resultado.getString("Apellidos")); // Apellidos del socio infantil
                socio.put("Direccion", resultado.getString("Direccion")); // Dirección del socio infantil
                socio.put("Telefono", resultado.getString("Telefono")); // Teléfono del socio infantil
                socio.put("PresentadoPor", resultado.getString("PresentadoPor")); // Persona que presentó al socio infantil
                socio.put("Poblacion", resultado.getString("Poblacion")); // Lugar de Nacimiento del socio infantil
                
                socios.add(socio); // Añadir el mapa del socio infantil a la lista de socios encontrados
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el PreparedStatement
        } catch (SQLException e) {
            System.err.println("Error al buscar socios infantiles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return socios; // Retornar la lista de socios infantiles encontrados
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
        
        boolean exito = false; // Variable para indicar si la actualización fue exitosa
        
        try {
            String consulta = "UPDATE Socios SET Nombres = ?, Apellidos = ?, Direccion = ?, " +
                    "Telefono = ?, FechaRegistro = ?, PresentadoPor = ?, Poblacion = ? " +
                    "WHERE NoSocio = ?"; // Consulta SQL para actualizar los datos del socio adulto
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Crear un objeto PreparedStatement para ejecutar la consulta
            statement.setString(1, nombres); // Establecer los nombres del socio adulto
            statement.setString(2, apellidos); // Establecer los apellidos del socio adulto
            statement.setString(3, direccion); // Establecer la dirección del socio adulto
            statement.setString(4, telefono); // Establecer el teléfono del socio adulto
            statement.setDate(5, new java.sql.Date(fechaRegistro.getTime())); // Establecer la fecha de registro del socio adulto
            statement.setString(6, presentadoPor); // Establecer la persona que presentó al socio adulto
            statement.setString(7, poblacion); // Establecer la población del socio adulto
            statement.setInt(8, noSocio); // Establecer el número de socio en la consulta
            
            int filasActualizadas = statement.executeUpdate(); // Ejecutar la consulta de actualización y obtener el número de filas afectadas
            
            exito = (filasActualizadas > 0); // Si se actualizó al menos una fila, la actualización fue exitosa
            
            statement.close(); // Cerrar el PreparedStatement
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar socio adulto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito; // Retornar el resultado de la actualización
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
        
        boolean exito = false; // Variable para indicar si la actualización fue exitosa
        
        try {
            String consulta = "UPDATE SociosInfa SET Fecha = ?, Nombres = ?, Apellidos = ?, " +
                    "Direccion = ?, Telefono = ?, PresentadoPor = ?, Poblacion = ? " +
                    "WHERE NoSocio = ?"; // Consulta SQL para actualizar los datos del socio infantil
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Crear un objeto PreparedStatement para ejecutar la consulta
            statement.setDate(1, new java.sql.Date(fecha.getTime())); // Establecer la fecha de registro del socio infantil
            statement.setString(2, nombres); // Establecer los nombres del socio infantil
            statement.setString(3, apellidos); // Establecer los apellidos del socio infantil
            statement.setString(4, direccion);// Establecer la dirección del socio infantil
            statement.setString(5, telefono);// Establecer el teléfono del socio infantil
            statement.setString(6, presentadoPor); // Establecer la persona que presentó al socio infantil
            statement.setString(7, poblacion); // Establecer la población del socio infantil
            statement.setInt(8, noSocio); // Establecer el número de socio en la consulta
            
            int filasActualizadas = statement.executeUpdate(); // Ejecutar la consulta de actualización y obtener el número de filas afectadas
            
            exito = (filasActualizadas > 0); // Si se actualizó al menos una fila, la actualización fue exitosa
            
            statement.close(); // Cerrar el PreparedStatement
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar socio infantil: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito; // Retornar el resultado de la actualización
    }
    
    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        conexion.Desconexion(); // Cerrar la conexión a la base de datos
    }

      /**
     * Verifica si un socio tiene préstamos pendientes
     * @param noSocio Número de socio
     * @return true si tiene préstamos pendientes, false en caso contrario
     */
    public boolean tienePrestamoPendiente(int noSocio) {
        boolean tienePrestamo = false;
        
        try {
            // Usando la tabla Prestamo en lugar de Prestamos
            String consulta = "SELECT COUNT(*) AS Cantidad FROM Prestamo WHERE NoSocio = ?";
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Crear un objeto PreparedStatement para ejecutar la consulta
            statement.setInt(1, noSocio);// Establecer el número de socio en la consulta
            
            ResultSet resultado = statement.executeQuery(); // Ejecutar la consulta y obtener los resultados
            
            // Si hay resultados, verificar si el socio tiene préstamos pendientes
            if (resultado.next()) {
                tienePrestamo = resultado.getInt("Cantidad") > 0; // Si la cantidad de préstamos es mayor que 0, tiene préstamos pendientes
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el PreparedStatement
            
        } catch (SQLException e) {
            System.err.println("Error al verificar préstamos pendientes: " + e.getMessage());
            e.printStackTrace();
            // En caso de error, asumimos que no tiene préstamos pendientes para poder continuar
            tienePrestamo = false;
        }
        
        return tienePrestamo; // Retornar el resultado de la verificación de préstamos pendientes
    }
    
    /**
     * Obtiene los datos financieros de un socio
     * @param noSocio Número de socio
     * @param esInfantil Indica si es socio infantil
     * @return Mapa con los datos financieros del socio
     */    /**
     * Obtiene los datos financieros de un socio
     * @param noSocio Número de socio
     * @param esInfantil Indica si es socio infantil
     * @return Mapa con los datos financieros del socio, incluyendo una lista de todos los movimientos
     */
    public Map<String, Object> obtenerDatosFinancierosSocio(int noSocio, boolean esInfantil) {
        Map<String, Object> datosFinancieros = new HashMap<>(); // Mapa para almacenar los datos financieros del socio
        List<Map<String, Object>> listaMovimientos = new ArrayList<>(); // Lista para almacenar los movimientos del socio
        
        // Inicializar los datos financieros con valores por defecto (para el registro más reciente)
        datosFinancieros.put("AporIngresos", 0.0);
        datosFinancieros.put("AporEgresos", 0.0);
        datosFinancieros.put("AporSaldo", 0.0);
        datosFinancieros.put("AportacionSocial", 0.0);
        datosFinancieros.put("PresIngresos", 0.0);
        datosFinancieros.put("PresEgresos", 0.0);
        datosFinancieros.put("PresSaldo", 0.0);
        datosFinancieros.put("AhoIngresos", 0.0);
        datosFinancieros.put("AhoEgresos", 0.0);
        datosFinancieros.put("AhoSaldo", 0.0);
        datosFinancieros.put("Intereses", 0.0);
        String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO"; // Normalizar el tipo de socio a mayúsculas
        System.out.println("Obteniendo datos financieros para socio #" + noSocio + ", tipo: " + tipoSocio); // Debugging output
        
        try {
            // Primero verificamos si el socio realmente existe en la tabla correspondiente
            Map<String, Object> socioInfo = esInfantil ? 
                buscarSocioInfantilPorID(noSocio) : 
                buscarSocioAdultoPorID(noSocio);
                
            // Si no se encontró el socio, mostramos un mensaje de error    
            if (socioInfo == null) {
                System.err.println("No se encontró el socio " + tipoSocio + " con número " + noSocio + " en la tabla de socios"); // Error si no se encuentra el socio
                // Verificar si existe en la otra categoría antes de dar error
                Map<String, Object> socioAlternativo = esInfantil ? 
                    buscarSocioAdultoPorID(noSocio) : 
                    buscarSocioInfantilPorID(noSocio);
                    
                // Si se encontró en la otra categoría, informamos al usuario    
                if (socioAlternativo != null) {
                    System.out.println("Socio #" + noSocio + " encontrado en la categoría contraria: " + 
                                     (esInfantil ? "adulto" : "infantil") + " pero se solicitó como " + tipoSocio);
                }
            }            // Consultar la tabla MovimientosSocio para obtener todos los registros de movimientos
            String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio); // Normalizar el tipo de socio a mayúsculas
            String consultaMovimientos; // Consulta SQL para obtener los movimientos del socio
            PreparedStatement stmtMovimientos; // Declaración preparada para la consulta de movimientos

            // Construir la consulta dependiendo del tipo de socio
              if (tipoSocioUpperCase.equals("INFANTIL")) {
                consultaMovimientos = 
                    "SELECT IdMov, AporIngresos, AporEgresos, AporSaldo, PresIngresos, PresEgresos, " +
                    "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, Fecha, TipoSocio, " +
                    "RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon " +
                    "FROM MovimientosSocio " +
                    "WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE') " +
                    "ORDER BY Fecha DESC"; // Consulta para socios infantiles
            } else {
                consultaMovimientos = 
                    "SELECT IdMov, AporIngresos, AporEgresos, AporSaldo, PresIngresos, PresEgresos, " +
                    "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, Fecha, TipoSocio, " +
                    "RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon " +
                    "FROM MovimientosSocio " +
                    "WHERE NoSocio = ? AND TipoSocio = ? " +
                    "ORDER BY Fecha DESC"; // Consulta para socios adultos
            }
              
            stmtMovimientos = conexion.getConexion().prepareStatement(consultaMovimientos); // Preparar la consulta de movimientos
            stmtMovimientos.setInt(1, noSocio); // Establecer el número de socio en la consulta
            stmtMovimientos.setString(2, tipoSocioUpperCase); // Establecer el tipo de socio en la consulta
            
            System.out.println("Ejecutando consulta para MovimientosSocio con socio #" + noSocio + " y tipo " + tipoSocio); // Debugging output
            ResultSet rsMovimientos = stmtMovimientos.executeQuery(); // Ejecutar la consulta y obtener los resultados
            
            boolean primerRegistro = true; // Variable para identificar el primer registro (el más reciente)
            // Iterar sobre los resultados de los movimientos
            while (rsMovimientos.next()) {
                // Crear un mapa para cada movimiento
                Map<String, Object> movimiento = new HashMap<>();
                
                // Añadir todos los campos al movimiento                
                try {
                    int idMov = rsMovimientos.getInt("IdMov"); // Obtener el ID del movimiento
                    movimiento.put("IdMov", idMov); // Guardar el ID del movimiento
                    movimiento.put("ID", idMov);  // Por compatibilidad también lo guardamos como ID
                } catch (SQLException e) {
                    System.err.println("Error al obtener IdMov del movimiento: " + e.getMessage());
                }
                
                // Añadir los demás campos del movimiento
                movimiento.put("AporIngresos", rsMovimientos.getDouble("AporIngresos"));
                movimiento.put("AporEgresos", rsMovimientos.getDouble("AporEgresos"));
                movimiento.put("AporSaldo", rsMovimientos.getDouble("AporSaldo"));
                movimiento.put("PresIngresos", rsMovimientos.getDouble("PresIngresos"));
                movimiento.put("PresEgresos", rsMovimientos.getDouble("PresEgresos"));
                movimiento.put("PresSaldo", rsMovimientos.getDouble("PresSaldo"));
                movimiento.put("Intereses", rsMovimientos.getDouble("Intereses"));
                movimiento.put("AhoIngresos", rsMovimientos.getDouble("AhoIngresos"));
                movimiento.put("AhoEgresos", rsMovimientos.getDouble("AhoEgresos"));
                movimiento.put("AhoSaldo", rsMovimientos.getDouble("AhoSaldo"));
                movimiento.put("Fecha", rsMovimientos.getDate("Fecha"));
                movimiento.put("TipoSocio", rsMovimientos.getString("TipoSocio"));
                movimiento.put("RetInteres", rsMovimientos.getDouble("RetInteres"));
                movimiento.put("SaldoBanco", rsMovimientos.getDouble("SaldoBanco"));                
                movimiento.put("RetBanco", rsMovimientos.getDouble("RetBanco"));
                movimiento.put("IngOtros", rsMovimientos.getDouble("IngOtros"));
                movimiento.put("EgrOtros", rsMovimientos.getDouble("EgrOtros"));
                movimiento.put("GastosAdmon", rsMovimientos.getDouble("GastosAdmon"));
                
                // Calcular el saldo total (AhorroSaldo + AporteSocial - PrestamoSaldo)
                double saldoTotal = rsMovimientos.getDouble("AhoSaldo") + rsMovimientos.getDouble("AporSaldo") - rsMovimientos.getDouble("PresSaldo");
                movimiento.put("SaldoTotal", saldoTotal); // Guardar el saldo total calculado
                
                // Añadir el movimiento a la lista
                listaMovimientos.add(movimiento);
                
                // Si es el primer registro (más reciente), usarlo también para los datos financieros principales
                if (primerRegistro) {
                    datosFinancieros.put("AporIngresos", rsMovimientos.getDouble("AporIngresos"));
                    datosFinancieros.put("AporEgresos", rsMovimientos.getDouble("AporEgresos"));
                    datosFinancieros.put("AporSaldo", rsMovimientos.getDouble("AporSaldo"));
                    datosFinancieros.put("AportacionSocial", rsMovimientos.getDouble("AporSaldo")); // Se usa el mismo campo para ambos
                    datosFinancieros.put("PresIngresos", rsMovimientos.getDouble("PresIngresos"));
                    datosFinancieros.put("PresEgresos", rsMovimientos.getDouble("PresEgresos"));
                    datosFinancieros.put("PresSaldo", rsMovimientos.getDouble("PresSaldo"));
                    datosFinancieros.put("Intereses", rsMovimientos.getDouble("Intereses"));
                    datosFinancieros.put("AhoIngresos", rsMovimientos.getDouble("AhoIngresos"));
                    datosFinancieros.put("AhoEgresos", rsMovimientos.getDouble("AhoEgresos"));
                    datosFinancieros.put("AhoSaldo", rsMovimientos.getDouble("AhoSaldo"));
                    datosFinancieros.put("RetInteres", rsMovimientos.getDouble("RetInteres"));
                    datosFinancieros.put("SaldoBanco", rsMovimientos.getDouble("SaldoBanco"));
                    datosFinancieros.put("RetBanco", rsMovimientos.getDouble("RetBanco"));
                    datosFinancieros.put("IngOtros", rsMovimientos.getDouble("IngOtros"));
                    datosFinancieros.put("EgrOtros", rsMovimientos.getDouble("EgrOtros"));
                    datosFinancieros.put("GastosAdmon", rsMovimientos.getDouble("GastosAdmon"));
                    datosFinancieros.put("SaldoTotal", saldoTotal);
                    
                    primerRegistro = false; // Solo el primer registro se usa para los datos financieros principales
                }
            }
            
            // Agregar la lista de movimientos al mapa de datos financieros
            datosFinancieros.put("Movimientos", listaMovimientos);
            
            // Si no se encontró ningún registro en MovimientosSocio, usar el método de respaldo
            if (listaMovimientos.isEmpty()) {
                System.out.println("No se encontraron registros en MovimientosSocio para el socio " + noSocio + 
                                 " de tipo " + tipoSocio + ". Usando método alternativo.");
                obtenerDatosFinancierosDesdeTablas(noSocio, esInfantil, datosFinancieros); // Llamar al método de respaldo para obtener datos financieros desde las tablas individuales
            } else {
                System.out.println("Se encontraron " + listaMovimientos.size() + " movimientos para el socio #" + 
                                  noSocio + " de tipo " + tipoSocio); // Informar cuántos movimientos se encontraron
            }
            
            rsMovimientos.close(); // Cerrar el ResultSet de movimientos
            stmtMovimientos.close(); // Cerrar el PreparedStatement de movimientos
        } catch (SQLException e) {
            System.err.println("Error al obtener datos financieros: " + e.getMessage());
            e.printStackTrace();
            
            try {
                // En caso de error, intentamos usar el método de respaldo
                obtenerDatosFinancierosDesdeTablas(noSocio, esInfantil, datosFinancieros); 
            } catch (SQLException ex) {
                System.err.println("Error al obtener datos financieros alternativos: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        
        return datosFinancieros; // Retornar el mapa con los datos financieros del socio
    }
    
    /**
     * Método auxiliar para obtener datos financieros de las tablas individuales
     * Se utiliza como respaldo cuando no hay registros en MovimientosSocio
     * 
     * @param noSocio Número de socio
     * @param esInfantil Indica si el socio es infantil
     * @param datosFinancieros Mapa donde se almacenarán los datos financieros
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */    
    
     private void obtenerDatosFinancierosDesdeTablas(int noSocio, boolean esInfantil, Map<String, Object> datosFinancieros) throws SQLException {
        String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO"; // Normalizar el tipo de socio a mayúsculas
        System.out.println("Buscando datos financieros desde tablas individuales para socio " + noSocio + 
                         " de tipo " + tipoSocio); // Debugging output
                           
        // Verificar primero si existe el socio en la tabla correspondiente
        Map<String, Object> socio = esInfantil ? 
            buscarSocioInfantilPorID(noSocio) : 
            buscarSocioAdultoPorID(noSocio);
            
        if (socio == null) {
            System.err.println("El socio " + tipoSocio + " con número " + noSocio + " no existe en tabla de socios");
            return; // Terminamos sin alterar los valores por defecto
        }
        
        // Intentar obtener datos de aporte desde tabla Deposito
        String consultaDeposito = "SELECT SUM(Cantidad) AS Total FROM Deposito WHERE NoSocio = ?";
        
        PreparedStatement stmtDeposito = conexion.getConexion().prepareStatement(consultaDeposito); // Preparar la consulta de depósito
        stmtDeposito.setInt(1, noSocio); // Establecer el número de socio en la consulta
        
        ResultSet rsDeposito = stmtDeposito.executeQuery(); // Ejecutar la consulta y obtener los resultados
        
        // Si hay resultados, sumar las cantidades de depósitos
        if (rsDeposito.next()) {
            double total = rsDeposito.getDouble("Total"); // Obtener el total de depósitos
            datosFinancieros.put("AporIngresos", total); // Guardar el total de ingresos de aportes
            datosFinancieros.put("AporSaldo", total); // Guardar el total de saldo de aportes
            datosFinancieros.put("AportacionSocial", total); // Guardar el total de aportación social
        }
        
        rsDeposito.close(); // Cerrar el ResultSet de depósitos
        stmtDeposito.close(); // Cerrar el PreparedStatement de depósitos
        
        // Obtener datos de préstamos desde la tabla Prestamo
        String consultaPrestamo = "SELECT SUM(Cantidad) AS Total FROM Prestamo WHERE NoSocio = ?";
        
        PreparedStatement stmtPrestamo = conexion.getConexion().prepareStatement(consultaPrestamo); // Preparar la consulta de préstamo
        stmtPrestamo.setInt(1, noSocio); // Establecer el número de socio en la consulta
        
        ResultSet rsPrestamo = stmtPrestamo.executeQuery(); // Ejecutar la consulta y obtener los resultados
        
        // Si hay resultados, sumar las cantidades de préstamos
        if (rsPrestamo.next()) {
            double total = rsPrestamo.getDouble("Total"); // Obtener el total de préstamos
            datosFinancieros.put("PresIngresos", total); // Guardar el total de ingresos de préstamos
            datosFinancieros.put("PresSaldo", total); // Guardar el total de saldo de préstamos
        }
        
        rsPrestamo.close(); // Cerrar el ResultSet de préstamos
        stmtPrestamo.close(); // Obtener datos de ahorros desde la tabla MovimientosSocio para ambos tipos de socios
        String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio); // Normalizar el tipo de socio a mayúsculas
        String consultaAhorro; // Consulta SQL para obtener los datos de ahorro del socio
        PreparedStatement stmtAhorro; // Declaración preparada para la consulta de ahorro
        
        // Si el tipo es INFANTIL, también buscar con INFANTE
        if (tipoSocioUpperCase.equals("INFANTIL")) {
            consultaAhorro = "SELECT SUM(AhoIngresos) AS TotalIngresos, SUM(AhoEgresos) AS TotalEgresos, " +
                           "MAX(AhoSaldo) AS UltimoSaldo FROM MovimientosSocio WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE')"; // Consulta para socios infantiles, incluyendo INFANTE
        } else {
            consultaAhorro = "SELECT SUM(AhoIngresos) AS TotalIngresos, SUM(AhoEgresos) AS TotalEgresos, " +
                           "MAX(AhoSaldo) AS UltimoSaldo FROM MovimientosSocio WHERE NoSocio = ? AND TipoSocio = ?"; // Consulta para socios adultos
        }
        
        stmtAhorro = conexion.getConexion().prepareStatement(consultaAhorro); // Preparar la consulta de ahorro
        stmtAhorro.setInt(1, noSocio); // Establecer el número de socio en la consulta
        stmtAhorro.setString(2, tipoSocioUpperCase); // Establecer el tipo de socio en la consulta
        
        ResultSet rsAhorro = stmtAhorro.executeQuery(); // Ejecutar la consulta y obtener los resultados
        
        // Verificar si hay resultados en MovimientosSocio para ahorro
        if (rsAhorro.next() && rsAhorro.getDouble("UltimoSaldo") > 0) {
            // Si encontramos datos en MovimientosSocio, usarlos
            datosFinancieros.put("AhoIngresos", rsAhorro.getDouble("TotalIngresos"));
            datosFinancieros.put("AhoEgresos", rsAhorro.getDouble("TotalEgresos"));
            datosFinancieros.put("AhoSaldo", rsAhorro.getDouble("UltimoSaldo"));
            System.out.println("Datos de ahorro obtenidos de MovimientosSocio para socio " + noSocio + 
                             " tipo " + tipoSocio); // Informar que se encontraron datos de ahorro
        } else {
            // No se encontraron datos de ahorro en MovimientosSocio
            System.out.println("No se encontraron datos de ahorro en MovimientosSocio para socio " + noSocio + 
                             " tipo " + tipoSocio + ". Asignando valores por defecto.");
            
            // Asignar valores por defecto para ahorro cuando no hay datos
            datosFinancieros.put("AhoIngresos", 0.0);
            datosFinancieros.put("AhoSaldo", 0.0);
            System.out.println("No se encontró información de ahorro para socio " + noSocio + 
                             " en MovimientosSocio. Se asignan valores por defecto.");
        }
        
        rsAhorro.close(); // Cerrar el ResultSet de ahorro
        stmtAhorro.close(); // Cerrar el PreparedStatement de ahorro
        
        // Intentar obtener intereses de la tabla DepPrestamo
        String consultaIntereses = "SELECT SUM(Intereses) AS Total FROM DepPrestamo WHERE NoSocio = ?";
        
        PreparedStatement stmtIntereses = conexion.getConexion().prepareStatement(consultaIntereses); // Preparar la consulta de intereses
        stmtIntereses.setInt(1, noSocio); // Establecer el número de socio en la consulta
        
        ResultSet rsIntereses = stmtIntereses.executeQuery(); // Ejecutar la consulta y obtener los resultados
        
        // Si hay resultados, sumar las cantidades de intereses
        if (rsIntereses.next()) {
            double total = rsIntereses.getDouble("Total"); // Obtener el total de intereses
            datosFinancieros.put("Intereses", total); // Guardar el total de intereses
        }
        
        rsIntereses.close(); // Cerrar el ResultSet de intereses
        stmtIntereses.close(); // Cerrar el PreparedStatement de intereses
        
        // Calcular el saldo total
        double ahoSaldo = ((Number)datosFinancieros.getOrDefault("AhoSaldo", 0.0)).doubleValue();
        double aporSaldo = ((Number)datosFinancieros.getOrDefault("AporSaldo", 0.0)).doubleValue();
        double presSaldo = ((Number)datosFinancieros.getOrDefault("PresSaldo", 0.0)).doubleValue();
        double saldoTotal = ahoSaldo + aporSaldo - presSaldo; // Calcular el saldo total
        datosFinancieros.put("SaldoTotal", saldoTotal); // Guardar el saldo total calculado
        
        // Crear un movimiento artificial para mantener la consistencia de la interfaz
        Map<String, Object> movimiento = new HashMap<>();
        movimiento.putAll(datosFinancieros); // Copiar los datos financieros al movimiento
        movimiento.put("Fecha", new java.util.Date()); // Usar la fecha actual como fecha del movimiento
        movimiento.put("TipoSocio", tipoSocio); // Guardar el tipo de socio en el movimiento
        
        // Crear la lista de movimientos con este único movimiento
        List<Map<String, Object>> movimientos = new ArrayList<>(); 
        movimientos.add(movimiento); // Añadir el movimiento a la lista
        datosFinancieros.put("Movimientos", movimientos); // Guardar la lista de movimientos en los datos financieros
        System.out.println("Datos financieros completados para socio " + noSocio + " de tipo " + tipoSocio + 
                         " con saldo total: " + saldoTotal); // Informar que se completaron los datos financieros
    }    
    
    /**
     * Registra un nuevo movimiento para un socio
     * @param noSocio Número de socio
     * @param esInfantil Indica si es socio infantil
     * @param aportacionDeposito Monto depositado a aportaciones
     * @param aportacionRetiro Monto retirado de aportaciones
     * @param prestamoDeposito Monto pagado de préstamo
     * @param prestamoRetiro Monto retirado como préstamo
     * @param ahorroDeposito Monto depositado a ahorros
     * @param ahorroRetiro Monto retirado de ahorros
     * @param interesDeuda Interés pagado por deuda
     * @param interesCalculado Interés calculado total (puede ser mayor que interesDeuda)
     * @return ID del movimiento registrado, o -1 si ocurrió un error
     */    
    
     public int registrarMovimientoSocio(
            int noSocio, boolean esInfantil,
            double aportacionDeposito, double aportacionRetiro,
            double prestamoDeposito, double prestamoRetiro,
            double ahorroDeposito, double ahorroRetiro,
            double interesDeuda, double interesCalculado) {
        
        // Validar que los montos sean positivos o cero
        String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO";
        System.out.println("Registrando movimiento para socio #" + noSocio + ", tipo: " + tipoSocio);
        System.out.println("Valores: AportDeposito=" + aportacionDeposito + 
            ", AportRetiro=" + aportacionRetiro +
            ", PrestDeposito=" + prestamoDeposito + 
            ", PrestRetiro=" + prestamoRetiro +
            ", AhorroDeposito=" + ahorroDeposito + 
            ", AhorroRetiro=" + ahorroRetiro +
            ", InteresDeuda=" + interesDeuda + 
            ", InteresCalculado=" + interesCalculado);
        System.out.println("Configurando 19 parámetros para la instrucción SQL...");
        
        try {
            // Primero obtenemos los saldos actuales
            Map<String, Object> datosActuales = obtenerDatosFinancierosSocio(noSocio, esInfantil);
            if (datosActuales == null) { // Verificar si se obtuvieron los datos correctamente
                System.err.println("No se pudieron obtener los datos financieros actuales del socio #" + noSocio); // Error si no se obtuvieron los datos
                return -1; // Terminar si no se pudieron obtener los datos
            }
            
            // Calcular los nuevos saldos
            double aportacionSaldoActual = (Double) datosActuales.getOrDefault("AporSaldo", 0.0);
            double prestamoSaldoActual = (Double) datosActuales.getOrDefault("PresSaldo", 0.0);
            double ahorroSaldoActual = (Double) datosActuales.getOrDefault("AhoSaldo", 0.0);
            
            // Calcular nuevos saldos
            double nuevoSaldoAportaciones = aportacionSaldoActual + aportacionDeposito - aportacionRetiro;
            double nuevoSaldoPrestamos = prestamoSaldoActual - prestamoDeposito + prestamoRetiro;
            double nuevoSaldoAhorros = ahorroSaldoActual + ahorroDeposito - ahorroRetiro;
            
            // Registrar el movimiento en MovimientosSocio
            String consultaMovimiento = "INSERT INTO MovimientosSocio " +
                "(NoSocio, Fecha, AporIngresos, AporEgresos, AporSaldo, PresEgresos, PresIngresos, " +
                "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, TipoSocio, " +
                "RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            System.out.println("Configurando 19 parámetros para la instrucción SQL...");
            System.out.println("Valores a insertar: NoSocio=" + noSocio + 
                ", AporIngresos=" + aportacionDeposito + 
                ", AporEgresos=" + aportacionRetiro + 
                ", AporSaldo=" + nuevoSaldoAportaciones + 
                ", PresEgresos=" + prestamoRetiro +
                ", PresIngresos=" + prestamoDeposito +
                ", PresSaldo=" + nuevoSaldoPrestamos +
                ", Intereses=" + interesDeuda +
                ", AhoIngresos=" + ahorroDeposito +
                ", AhoEgresos=" + ahorroRetiro +
                ", AhoSaldo=" + nuevoSaldoAhorros);
                
            PreparedStatement stmtMovimiento = conexion.getConexion().prepareStatement(
                consultaMovimiento, 
                java.sql.Statement.RETURN_GENERATED_KEYS // Para obtener el ID generado
            );
            
            // 1. NoSocio
            stmtMovimiento.setInt(1, noSocio);
            
            // 2. Fecha - Crear fecha sin tiempo (solo con año, mes y día)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            stmtMovimiento.setDate(2, new java.sql.Date(cal.getTimeInMillis()));
            
            // 3. AporIngresos
            stmtMovimiento.setDouble(3, aportacionDeposito);
            
            // 4. AporEgresos
            stmtMovimiento.setDouble(4, aportacionRetiro);
            
            // 5. AporSaldo
            stmtMovimiento.setDouble(5, nuevoSaldoAportaciones);
            
            // 6. PresEgresos (retiro = egreso)
            stmtMovimiento.setDouble(6, prestamoRetiro);
            
            // 7. PresIngresos (depósito = ingreso)
            stmtMovimiento.setDouble(7, prestamoDeposito);
            
            // 8. PresSaldo
            stmtMovimiento.setDouble(8, nuevoSaldoPrestamos);
            
            // 9. Intereses (el interés que se está pagando)
            stmtMovimiento.setDouble(9, interesDeuda);
            
            // 10. AhoIngresos
            stmtMovimiento.setDouble(10, ahorroDeposito);
            
            // 11. AhoEgresos
            stmtMovimiento.setDouble(11, ahorroRetiro);
            
            // 12. AhoSaldo
            stmtMovimiento.setDouble(12, nuevoSaldoAhorros);
            
            // 13. TipoSocio
            stmtMovimiento.setString(13, tipoSocio);
            
            // 14. RetInteres (solo para retiros de intereses)
            stmtMovimiento.setDouble(14, 0);
            
            // 15. SaldoBanco
            stmtMovimiento.setDouble(15, 0);
            
            // 16. RetBanco
            stmtMovimiento.setDouble(16, 0);
            
            // 17. IngOtros
            stmtMovimiento.setDouble(17, 0);
            
            // 18. EgrOtros
            stmtMovimiento.setDouble(18, 0);
            
            // 19. GastosAdmon
            stmtMovimiento.setDouble(19, 0);
            
            System.out.println("Los 19 parámetros han sido configurados correctamente. Ejecutando la instrucción...");
            int filasAfectadas = stmtMovimiento.executeUpdate(); // Ejecutar la instrucción y obtener el número de filas afectadas
            int idMovimientoNuevo = -1; // Variable para almacenar el ID del nuevo movimiento
            
            // Si se afectaron filas, obtener el ID generado
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet rs = stmtMovimiento.getGeneratedKeys();
                // Verificar si se obtuvo un ID
                if (rs.next()) {
                    idMovimientoNuevo = rs.getInt(1); // Obtener el ID del movimiento recién insertado
                }
                rs.close(); // Cerrar el ResultSet de claves generadas
            }
            
            stmtMovimiento.close(); // Cerrar el PreparedStatement del movimiento
            
            System.out.println("Movimiento registrado correctamente. ID: " + idMovimientoNuevo);
            return idMovimientoNuevo; // Retornar el ID del movimiento registrado
        } catch (SQLException e) {
            System.err.println("Error al registrar movimiento: " + e.getMessage());
            System.err.println("Código de error SQL: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Contexto: NoSocio=" + noSocio + ", TipoSocio=" + (esInfantil ? "INFANTIL" : "ADULTO"));
            System.err.println("Detalles completos de la excepción:");
            e.printStackTrace();
            return -1; // Retornar -1 en caso de error al registrar el movimiento
        }
    }    
    
    /**
     * Registra un nuevo movimiento para un socio infantil
     * Solo considera operaciones de ahorro, ignorando los demás campos
     * @param noSocio Número de socio
     * @param ahorroDeposito Monto depositado a ahorros
     * @param ahorroRetiro Monto retirado de ahorros
     * @return ID del movimiento registrado, o -1 si ocurrió un error
     */

    public int registrarMovimientoSocioInfantil(
            int noSocio, 
            double ahorroDeposito, double ahorroRetiro) {
        
        System.out.println("Registrando movimiento para socio infantil #" + noSocio);
        
        try {
            // Primero obtenemos los saldos actuales
            Map<String, Object> datosActuales = obtenerDatosFinancierosSocio(noSocio, true);
              if (datosActuales == null) { // Verificar si se obtuvieron los datos correctamente
                System.err.println("No se pudieron obtener los datos financieros actuales del socio infantil #" + noSocio);
                return -1; // Terminar si no se pudieron obtener los datos
            }
            
            // Calcular el nuevo saldo de ahorros
            double ahorroSaldoActual = (Double) datosActuales.getOrDefault("AhoSaldo", 0.0);
            double nuevoSaldoAhorros = ahorroSaldoActual + ahorroDeposito - ahorroRetiro;
            
            // Registrar el movimiento en MovimientosSocio
            String consultaMovimiento = "INSERT INTO MovimientosSocio " +
                "(NoSocio, Fecha, AporIngresos, AporEgresos, AporSaldo, PresEgresos, PresIngresos, " +
                "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, TipoSocio, " +
                "RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon) " +                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
            System.out.println("Configurando 19 parámetros para la instrucción SQL en registrarMovimientoSocioInfantil...");
            System.out.println("Valores a insertar para socio infantil: NoSocio=" + noSocio + 
                ", AhoIngresos=" + ahorroDeposito + 
                ", AhoEgresos=" + ahorroRetiro + 
                ", AhoSaldo=" + nuevoSaldoAhorros);
                
            PreparedStatement stmtMovimiento = conexion.getConexion().prepareStatement(
                consultaMovimiento,
                java.sql.Statement.RETURN_GENERATED_KEYS // Para obtener el ID generado
            );
            
            stmtMovimiento.setInt(1, noSocio); // 1. NoSocio
            
            // Crear fecha sin tiempo (solo con año, mes y día)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            stmtMovimiento.setDate(2, new java.sql.Date(cal.getTimeInMillis()));
            
            stmtMovimiento.setDouble(3, 0.0);  // AporIngresos
            stmtMovimiento.setDouble(4, 0.0);    // AporEgresos
            stmtMovimiento.setDouble(5, 0.0); // AporSaldo
            stmtMovimiento.setDouble(6, 0.0);      // PresEgresos
            stmtMovimiento.setDouble(7, 0.0);    // PresIngresos
            stmtMovimiento.setDouble(8, 0.0); // PresSaldo
            stmtMovimiento.setDouble(9, 0.0);                   // Intereses
            stmtMovimiento.setDouble(10, ahorroDeposito);     // AhoIngresos
            stmtMovimiento.setDouble(11, ahorroRetiro);       // AhoEgresos
            stmtMovimiento.setDouble(12, nuevoSaldoAhorros);  // AhoSaldo
            stmtMovimiento.setString(13, "INFANTIL");          // TipoSocio
            stmtMovimiento.setDouble(14, 0.0);       // RetInteres
            stmtMovimiento.setDouble(15, 0.0);                  // SaldoBanco
            stmtMovimiento.setDouble(16, 0.0);                  // RetBanco
            stmtMovimiento.setDouble(17, 0.0);                  // IngOtros
            stmtMovimiento.setDouble(18, 0.0);                  // EgrOtros
            stmtMovimiento.setDouble(19, 0.0);                  // GastosAdmon
            
            System.out.println("Los 19 parámetros han sido configurados correctamente para socio infantil. Ejecutando la instrucción...");
            int filasAfectadas = stmtMovimiento.executeUpdate(); // Ejecutar la instrucción y obtener el número de filas afectadas
            int idMovimientoNuevo = -1; // Variable para almacenar el ID del nuevo movimiento
            
            // Si se afectaron filas, obtener el ID generado
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet rs = stmtMovimiento.getGeneratedKeys(); // Ejecutar la consulta y obtener las claves generadas
                if (rs.next()) {
                    idMovimientoNuevo = rs.getInt(1); // Obtener el ID del movimiento recién insertado
                }
                rs.close(); // Cerrar el ResultSet de claves generadas
            }
            
            stmtMovimiento.close(); // Cerrar el PreparedStatement del movimiento
            
            System.out.println("Movimiento para socio infantil registrado correctamente. ID: " + idMovimientoNuevo);
            return idMovimientoNuevo;
              } catch (SQLException e) {
            System.err.println("Error al registrar movimiento para socio infantil: " + e.getMessage());
            System.err.println("Código de error SQL: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Detalles completos de la excepción:");
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Elimina un socio y traslada sus datos a la tabla de socios cancelados
     * @param noSocio Número de socio
     * @param esInfantil Indica si es socio infantil
     * @param totalRetiro Total a retirar
     * @return true si se eliminó correctamente, false en caso contrario
     */    
    
     public boolean eliminarSocio(int noSocio, boolean esInfantil, double totalRetiro) {
        boolean exito = false; // Indica si la eliminación fue exitosa
        Connection conexionActual = null; // Conexión actual a la base de datos
        String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO"; // Normalizar el tipo de socio a mayúsculas
        
        try {
            System.out.println("Iniciando eliminación de socio " + noSocio + " tipo: " + tipoSocio);
            conexionActual = conexion.getConexion(); // Obtener la conexión actual
            conexionActual.setAutoCommit(false); // Iniciar transacción
            
            // 1. Verificar si el socio existe en MovimientosSocio
            boolean existeEnMovimientos = existeEnMovimientos(noSocio, tipoSocio);
            if (!existeEnMovimientos) {
                System.out.println("ADVERTENCIA: El socio #" + noSocio + " no tiene registros en MovimientosSocio de tipo " + tipoSocio);
            }
            
            // 2. Obtener los datos del socio antes de eliminarlo
            Map<String, Object> socio = esInfantil ? 
                buscarSocioInfantilPorID(noSocio) : 
                buscarSocioAdultoPorID(noSocio);
            
            // Verificar si se encontró el socio    
            if (socio == null) {
                throw new Exception("No se encontró el socio " + tipoSocio + " con número " + noSocio); // Lanzar excepción si no se encuentra el socio
            }
            
            // 3. Insertar en la tabla de cancelaciones
            String consultaInsertCancelado = "INSERT INTO Cancelaciones " +
                "(NoSocio, Descripcion, Cantidad, Fecha) " +
                "VALUES (?, ?, ?, ?)";
            
            PreparedStatement stmtInsertCancelado = conexionActual.prepareStatement(consultaInsertCancelado); // Preparar la consulta de cancelación
            stmtInsertCancelado.setInt(1, noSocio); // 1. NoSocio
            stmtInsertCancelado.setString(2, "Cancelación de socio " + tipoSocio + ": " + 
                socio.get("Nombres") + " " + socio.get("Apellidos")); // Descripción de la cancelación
            stmtInsertCancelado.setDouble(3, totalRetiro); // 3. Cantidad a retirar
            stmtInsertCancelado.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Fecha actual
            
            stmtInsertCancelado.executeUpdate(); // Ejecutar la inserción en la tabla de cancelaciones
            stmtInsertCancelado.close(); // Cerrar el PreparedStatement de cancelación
            
            // 4. Limpiar todos los registros relacionados            
            try {
                String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio); // Normalizar el tipo de socio a mayúsculas
                String consultaMovimientos; // Consulta SQL para eliminar registros de MovimientosSocio
                PreparedStatement stmtMovimientos; // Declaración preparada para la consulta de movimientos
                
                // Si el tipo es INFANTIL, también eliminar registros con INFANTE
                if (tipoSocioUpperCase.equals("INFANTIL")) {
                    // Si es infantil, eliminar registros con ambos tipos: INFANTIL y INFANTE
                    consultaMovimientos = "DELETE FROM MovimientosSocio WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE')";
                } else {
                    consultaMovimientos = "DELETE FROM MovimientosSocio WHERE NoSocio = ? AND TipoSocio = ?"; // Consulta para socios adultos
                }
                
                stmtMovimientos = conexionActual.prepareStatement(consultaMovimientos); // Preparar la consulta de movimientos
                stmtMovimientos.setInt(1, noSocio); // Establecer el número de socio en la consulta
                stmtMovimientos.setString(2, tipoSocioUpperCase); // Establecer el tipo de socio en la consulta
                int filasMovimientos = stmtMovimientos.executeUpdate(); // Ejecutar la consulta y obtener el número de filas afectadas
                stmtMovimientos.close(); // Cerrar el PreparedStatement de movimientos
                System.out.println("Registros eliminados de MovimientosSocio: " + filasMovimientos);
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de MovimientosSocio: " + e.getMessage());
            }
            // Todo se maneja a través de MovimientosSocio, no hay necesidad de usar tablas legacy
            
            // Eliminar registros de depósitos
            try {
                String consultaDeposito = "DELETE FROM Deposito WHERE NoSocio = ?"; // Consulta SQL para eliminar depósitos
                PreparedStatement stmtDeposito = conexionActual.prepareStatement(consultaDeposito); // Preparar la consulta de depósito
                stmtDeposito.setInt(1, noSocio); // Establecer el número de socio en la consulta
                int filasDeposito = stmtDeposito.executeUpdate(); // Ejecutar la consulta y obtener el número de filas afectadas
                stmtDeposito.close(); // Cerrar el PreparedStatement de depósito
                System.out.println("Registros de Deposito eliminados: " + filasDeposito); // Informar cuántos registros se eliminaron
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de Deposito: " + e.getMessage());
            }
            
            // Eliminar registros de préstamos
            try {
                String consultaPrestamo = "DELETE FROM Prestamo WHERE NoSocio = ?"; // Consulta SQL para eliminar préstamos
                PreparedStatement stmtPrestamo = conexionActual.prepareStatement(consultaPrestamo); // Preparar la consulta de préstamo
                stmtPrestamo.setInt(1, noSocio); // Establecer el número de socio en la consulta
                int filasPrestamo = stmtPrestamo.executeUpdate(); // Ejecutar la consulta y obtener el número de filas afectadas
                stmtPrestamo.close(); // Cerrar el PreparedStatement de préstamo
                System.out.println("Registros de Prestamo eliminados: " + filasPrestamo); // Informar cuántos registros se eliminaron
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de Prestamo: " + e.getMessage());
            }
            
            // Eliminar registros de depósitos de préstamo
            try {
                String consultaDepPrestamo = "DELETE FROM DepPrestamo WHERE NoSocio = ?"; // Consulta SQL para eliminar depósitos de préstamo
                PreparedStatement stmtDepPrestamo = conexionActual.prepareStatement(consultaDepPrestamo); // Preparar la consulta de DepPrestamo
                stmtDepPrestamo.setInt(1, noSocio); // Establecer el número de socio en la consulta
                int filasDepPrestamo = stmtDepPrestamo.executeUpdate(); // Ejecutar la consulta y obtener el número de filas afectadas
                stmtDepPrestamo.close(); // Cerrar el PreparedStatement de DepPrestamo
                System.out.println("Registros de DepPrestamo eliminados: " + filasDepPrestamo); // Informar cuántos registros se eliminaron
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de DepPrestamo: " + e.getMessage());
            }
            
            // 5. Eliminar el socio de la tabla principal
            String tablaSocio = esInfantil ? "SociosInfa" : "Socios"; // Determinar la tabla según el tipo de socio
            try {
                String consultaEliminarSocio = "DELETE FROM " + tablaSocio + " WHERE NoSocio = ?"; // Consulta SQL para eliminar el socio de la tabla correspondiente
                
                PreparedStatement stmtEliminarSocio = conexionActual.prepareStatement(consultaEliminarSocio); // Preparar la consulta de eliminación del socio
                stmtEliminarSocio.setInt(1, noSocio); // Establecer el número de socio en la consulta
                int filasSocio = stmtEliminarSocio.executeUpdate(); // Ejecutar la consulta y obtener el número de filas afectadas
                stmtEliminarSocio.close(); // Cerrar el PreparedStatement de eliminación del socio
                System.out.println("Socio eliminado de la tabla " + tablaSocio + ": " + filasSocio); // Informar cuántos registros se eliminaron
            } catch (SQLException e) {
                System.err.println("Error al eliminar socio de " + tablaSocio + ": " + e.getMessage());
                throw e; // Re-lanzar la excepción para que se haga rollback
            }
            
            // 6. Registrar el movimiento final en la tabla de movimientos (para tener un registro histórico de la cancelación)
            try {
                String consultaMovimiento = "INSERT INTO MovimientosSocio " +
                    "(NoSocio, Fecha, AporIngresos, AporEgresos, AporSaldo, PresEgresos, PresIngresos, " +
                    "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, TipoSocio, " +
                    "Nada, RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Consulta SQL para registrar el movimiento final de cancelación
                
                PreparedStatement stmtMovimiento = conexionActual.prepareStatement(consultaMovimiento); // Preparar la consulta de movimiento final
                stmtMovimiento.setInt(1, noSocio); // NoSocio
                stmtMovimiento.setDate(2, new java.sql.Date(System.currentTimeMillis())); // Fecha actual sin tiempo
                stmtMovimiento.setDouble(3, 0); // AporIngresos
                stmtMovimiento.setDouble(4, totalRetiro); // AporEgresos
                stmtMovimiento.setDouble(5, 0); // AporSaldo
                stmtMovimiento.setDouble(6, 0); // PresEgresos
                stmtMovimiento.setDouble(7, 0); // PresIngresos
                stmtMovimiento.setDouble(8, 0); // PresSaldo
                stmtMovimiento.setDouble(9, 0); // Intereses
                stmtMovimiento.setDouble(10, 0); // AhoIngresos
                stmtMovimiento.setDouble(11, 0); // AhoEgresos
                stmtMovimiento.setDouble(12, 0); // AhoSaldo
                stmtMovimiento.setString(13, tipoSocio); // TipoSocio
                stmtMovimiento.setString(14, "CANCELACIÓN"); // Nada - usando como observación
                stmtMovimiento.setDouble(15, 0); // RetInteres
                stmtMovimiento.setDouble(16, 0); // SaldoBanco
                stmtMovimiento.setDouble(17, 0); // RetBanco
                stmtMovimiento.setDouble(18, 0); // IngOtros
                stmtMovimiento.setDouble(19, 0); // EgrOtros
                stmtMovimiento.setDouble(20, 0); // GastosAdmon
                
                stmtMovimiento.executeUpdate(); // Ejecutar la inserción del movimiento final
                stmtMovimiento.close(); // Cerrar el PreparedStatement del movimiento final
                System.out.println("Registrado movimiento final de cancelación para socio " + noSocio + 
                                  " tipo " + tipoSocio +
                                  " con retiro de " + totalRetiro); // Informar que se registró el movimiento final de cancelación
                
                // Confirmar la transacción
                conexionActual.commit();
                exito = true; // Indicar que la eliminación fue exitosa
                System.out.println("Eliminación de socio " + noSocio + " tipo " + tipoSocio + " completada con éxito");
            } catch (SQLException e) {
                System.err.println("Error al registrar movimiento final: " + e.getMessage());
                throw e;
            }
            
        } catch (Exception e) {
            System.err.println("Error al eliminar socio: " + e.getMessage());
            e.printStackTrace();
            
            // Si ocurre un error, hacemos rollback de la transacción
            try {
                if (conexionActual != null) { // Verificar si la conexión es válida
                    conexionActual.rollback(); // Hacer rollback de la transacción
                    System.out.println("Se realizó rollback por error: " + e.getMessage()); // Informar que se hizo rollback
                }
            } catch (SQLException ex) {
                System.err.println("Error al realizar rollback: " + ex.getMessage());
            }
            
        } finally { // Bloque finally para asegurar que se restaura el modo auto-commit
            try {
                if (conexionActual != null) { // Verificar si la conexión es válida
                    conexionActual.setAutoCommit(true); // Restaurar modo auto-commit
                }
            } catch (SQLException e) {
                System.err.println("Error al restaurar auto-commit: " + e.getMessage());
            }
        }
        
        return exito;
    }
    
    /**
     * Verifica si un socio existe en la tabla MovimientosSocio con el tipo especificado
     * @param noSocio Número de socio
     * @param tipoSocio Tipo de socio ("Infantil" o "Adulto")
     * @return true si existe, false en caso contrario
     */    
    
     public boolean existeEnMovimientos(int noSocio, String tipoSocio) {
        boolean existe = false; // Indica si el socio existe en MovimientosSocio
        
        try {
            // Normalizar el tipo de socio
            String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio);
            
            // Si el tipo es INFANTIL, también debemos buscar registros con INFANTE
            String consulta;
            PreparedStatement statement;
            
            // Verificar si el tipo de socio es INFANTIL
            if (tipoSocioUpperCase.equals("INFANTIL")) {
                consulta = "SELECT COUNT(*) AS Total FROM MovimientosSocio WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE')"; // Consulta SQL para buscar en MovimientosSocio
                // Preparar la consulta para buscar tanto INFANTIL como INFANTE
                statement = conexion.getConexion().prepareStatement(consulta);
                statement.setInt(1, noSocio);
                statement.setString(2, tipoSocioUpperCase);
            } else {
                consulta = "SELECT COUNT(*) AS Total FROM MovimientosSocio WHERE NoSocio = ? AND TipoSocio = ?"; // Consulta SQL para buscar en MovimientosSocio
                statement = conexion.getConexion().prepareStatement(consulta);
                statement.setInt(1, noSocio);
                statement.setString(2, tipoSocioUpperCase);
            }
            
            ResultSet resultado = statement.executeQuery(); // Ejecutar la consulta y obtener el resultado
            
            // Verificar si existe al menos un registro
            if (resultado.next()) {
                existe = resultado.getInt("Total") > 0; // Verificar si el total es mayor que 0
            }
            
            resultado.close(); // Cerrar el ResultSet
            statement.close(); // Cerrar el PreparedStatement
            
            System.out.println("Socio " + noSocio + " de tipo " + tipoSocio + 
                              (existe ? " existe en MovimientosSocio" : " no existe en MovimientosSocio"));
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia en MovimientosSocio: " + e.getMessage());
            e.printStackTrace();
        }
        
        return existe;
    }
    
    /**
     * Método utilitario para normalizar el tipo de socio
     * Convierte INFANTE a INFANTIL y asegura que todo esté en mayúscula
     * 
     * @param tipoSocio El tipo de socio a normalizar
     * @return El tipo de socio normalizado (INFANTIL o ADULTO)
     */

    private String normalizarTipoSocio(String tipoSocio) {
        if (tipoSocio == null) return null; // Verificar si el tipoSocio es nulo
        String tipo = tipoSocio.toUpperCase(); // Convertir a mayúsculas para normalizar
        // Normalizar INFANTE a INFANTIL
        if (tipo.equals("INFANTE")) { 
            return "INFANTIL"; // Normalizar INFANTE a INFANTIL
        }
        return tipo; // Retornar el tipo normalizado
    }
    
    /**
     * Obtiene el siguiente número de socio adulto disponible
     * Este método busca números disponibles en huecos (cuando se han eliminado socios)
     * Si no encuentra huecos, devuelve el siguiente número consecutivo
     * @return Siguiente número de socio adulto disponible
     */

    public int obtenerSiguienteNumeroSocioAdulto() {
        int siguienteNumero = 0; // Variable para almacenar el siguiente número de socio adulto
        
        // Intentamos encontrar un hueco en la numeración de socios adultos
        try {
            // Primero intentamos encontrar un hueco en la numeración
            String consultaHuecos = 
                "SELECT t1.NoSocio + 1 AS HuecoDisponible " +
                "FROM Socios t1 " +
                "LEFT JOIN Socios t2 ON t1.NoSocio + 1 = t2.NoSocio " +
                "WHERE t2.NoSocio IS NULL AND t1.NoSocio + 1 < (" +
                "    SELECT MAX(NoSocio) FROM Socios" +
                ") " +
                "ORDER BY t1.NoSocio " +
                "LIMIT 1"; // Consulta SQL para encontrar huecos disponibles
            
            Statement statementHuecos = conexion.getConexion().createStatement(); // Crear un Statement para ejecutar la consulta
            ResultSet resultadoHuecos = statementHuecos.executeQuery(consultaHuecos); // Ejecutar la consulta y obtener el resultado
            
            // Si encontramos un hueco, usamos ese número
            if (resultadoHuecos.next()) {
                siguienteNumero = resultadoHuecos.getInt("HuecoDisponible"); // Obtener el hueco disponible
                System.out.println("Se encontró un hueco disponible para socio adulto: " + siguienteNumero); 
            } else {
                // Si no hay huecos, usamos el siguiente al último número
                siguienteNumero = obtenerUltimoNumeroSocioAdulto() + 1;
                System.out.println("No se encontraron huecos, siguiente número para socio adulto: " + siguienteNumero);
            }
            
            resultadoHuecos.close(); // Cerrar el ResultSet de huecos
            statementHuecos.close(); // Cerrar el Statement de huecos
            
        } catch (SQLException e) {
            // Si ocurre algún error, usamos el método tradicional
            System.err.println("Error al buscar huecos para socio adulto: " + e.getMessage());
            siguienteNumero = obtenerUltimoNumeroSocioAdulto() + 1;
        }
        
        return siguienteNumero; // Retornar el siguiente número de socio adulto disponible
    }
    
    /**
     * Obtiene el siguiente número de socio infantil disponible
     * Este método busca números disponibles en huecos (cuando se han eliminado socios)
     * Si no encuentra huecos, devuelve el siguiente número consecutivo
     * @return Siguiente número de socio infantil disponible
     */

    public int obtenerSiguienteNumeroSocioInfantil() {
        int siguienteNumero = 0; // Variable para almacenar el siguiente número de socio infantil
        
        // Intentamos encontrar un hueco en la numeración de socios infantiles
        try {
            // Primero intentamos encontrar un hueco en la numeración
            String consultaHuecos = 
                "SELECT t1.NoSocio + 1 AS HuecoDisponible " +
                "FROM SociosInfa t1 " +
                "LEFT JOIN SociosInfa t2 ON t1.NoSocio + 1 = t2.NoSocio " +
                "WHERE t2.NoSocio IS NULL AND t1.NoSocio + 1 < (" +
                "    SELECT MAX(NoSocio) FROM SociosInfa" +
                ") " +
                "ORDER BY t1.NoSocio " +
                "LIMIT 1"; // Consulta SQL para encontrar huecos disponibles
            
            Statement statementHuecos = conexion.getConexion().createStatement(); // Crear un Statement para ejecutar la consulta
            ResultSet resultadoHuecos = statementHuecos.executeQuery(consultaHuecos); // Ejecutar la consulta y obtener el resultado
            
            // Si encontramos un hueco, usamos ese número
            if (resultadoHuecos.next()) {
                siguienteNumero = resultadoHuecos.getInt("HuecoDisponible"); // Obtener el hueco disponible
                System.out.println("Se encontró un hueco disponible para socio infantil: " + siguienteNumero);
            } else {
                // Si no hay huecos, usamos el siguiente al último número
                siguienteNumero = obtenerUltimoNumeroSocioInfantil() + 1; // Obtener el último número de socio infantil y sumar 1
                System.out.println("No se encontraron huecos, siguiente número para socio infantil: " + siguienteNumero);
            }
            
            resultadoHuecos.close(); // Cerrar el ResultSet de huecos
            statementHuecos.close(); // Cerrar el Statement de huecos
            
        } catch (SQLException e) {
            // Si ocurre algún error, usamos el método tradicional
            System.err.println("Error al buscar huecos para socio infantil: " + e.getMessage());
            siguienteNumero = obtenerUltimoNumeroSocioInfantil() + 1;
        }
        
        return siguienteNumero; // Retornar el siguiente número de socio infantil disponible
    }
    
    /**
     * Obtiene los datos de un movimiento específico por su ID
     * @param idMovimiento ID del movimiento a consultar
     * @return Map con los datos del movimiento o null si no existe
     */    
    
     public Map<String, Object> obtenerMovimientoPorId(int idMovimiento) {
        Map<String, Object> datosMovimiento = null; // Inicializar el mapa para almacenar los datos del movimiento
        
        // Verificar que la conexión esté activa
        try {
            System.out.println("Buscando movimiento con IdMov = " + idMovimiento); // Informar que se está buscando el movimiento
            String consulta = "SELECT * FROM MovimientosSocio WHERE IdMov = ?"; // Consulta SQL para obtener el movimiento por ID
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Preparar la consulta con el ID del movimiento
            statement.setInt(1, idMovimiento); // Establecer el ID del movimiento en la consulta
            ResultSet resultado = statement.executeQuery(); // Ejecutar la consulta y obtener el resultado
            
            // Verificar si se encontró el movimiento
            if (resultado.next()) {
                datosMovimiento = new HashMap<>(); // Inicializar el mapa para almacenar los datos del movimiento
                try {
                    int id = resultado.getInt("IdMov"); // Obtener el ID del movimiento
                    datosMovimiento.put("ID", id);       // Mantener compatibilidad
                    datosMovimiento.put("IdMov", id);    // Agregar con nombre real
                    System.out.println("Movimiento encontrado con IdMov = " + id);
                } catch (SQLException e) {
                    System.err.println("Error al obtener IdMov: " + e.getMessage());
                }
                
                // Agregar los demás campos del movimiento al mapa
                datosMovimiento.put("NoSocio", resultado.getInt("NoSocio"));
                datosMovimiento.put("Fecha", resultado.getDate("Fecha"));
                datosMovimiento.put("AporIngresos", resultado.getDouble("AporIngresos"));
                datosMovimiento.put("AporEgresos", resultado.getDouble("AporEgresos"));
                datosMovimiento.put("PresIngresos", resultado.getDouble("PresIngresos"));
                datosMovimiento.put("PresEgresos", resultado.getDouble("PresEgresos"));
                datosMovimiento.put("AhoIngresos", resultado.getDouble("AhoIngresos"));
                datosMovimiento.put("AhoEgresos", resultado.getDouble("AhoEgresos"));
                datosMovimiento.put("RetInteres", resultado.getDouble("RetInteres"));
                datosMovimiento.put("Intereses", resultado.getDouble("Intereses"));
                datosMovimiento.put("AporSaldo", resultado.getDouble("AporSaldo"));
                datosMovimiento.put("PresSaldo", resultado.getDouble("PresSaldo"));
                datosMovimiento.put("AhoSaldo", resultado.getDouble("AhoSaldo"));
                datosMovimiento.put("TipoSocio", resultado.getString("TipoSocio"));
            }
            
            resultado.close(); // Cerrar el ResultSet después de obtener los datos
            statement.close(); // Cerrar el PreparedStatement después de ejecutar la consulta
            
        } catch (SQLException e) {
            System.err.println("Error al obtener movimiento por ID " + idMovimiento + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // Verificar si se encontró el movimiento
        if (datosMovimiento == null) {
            System.err.println("No se encontró el movimiento con ID: " + idMovimiento);
        } else {
            System.out.println("Movimiento recuperado correctamente con ID: " + idMovimiento);
        }
        
        return datosMovimiento; // Retornar el mapa con los datos del movimiento o null si no se encontró
    }
      /**
     * Actualiza un movimiento existente en la base de datos
     * @param idMovimiento ID del movimiento a actualizar
     * @param aportacionDeposito Nuevo monto depositado a aportaciones
     * @param aportacionRetiro Nuevo monto retirado de aportaciones
     * @param prestamoDeposito Nuevo monto pagado de préstamo
     * @param prestamoRetiro Nuevo monto retirado como préstamo
     * @param ahorroDeposito Nuevo monto depositado a ahorros
     * @param ahorroRetiro Nuevo monto retirado de ahorros
     * @param interesDeuda Nuevo interés pagado por deuda
     * @return true si la actualización fue exitosa, false en caso contrario
     */    /**
     * Actualiza un movimiento existente en la base de datos
     * @param idMovimiento ID del movimiento a actualizar
     * @param noSocio ID del socio al que pertenece el movimiento
     * @param aportacionDeposito Monto de depósito en aportaciones
     * @param aportacionRetiro Monto de retiro en aportaciones
     * @param prestamoDeposito Monto de depósito en préstamos
     * @param prestamoRetiro Monto de retiro en préstamos
     * @param ahorroDeposito Monto de depósito en ahorros
     * @param ahorroRetiro Monto de retiro en ahorros
     * @param interesDeuda Monto de interés de deuda
     * @return true si la actualización fue exitosa, false en caso contrario
     */

    public boolean actualizarMovimiento(
            int idMovimiento,
            int noSocio,
            double aportacionDeposito, double aportacionRetiro,
            double prestamoDeposito, double prestamoRetiro,
            double ahorroDeposito, double ahorroRetiro,
            double interesDeuda) {
        
        // Verificar que la conexión esté activa        
        try {
            // Primero obtenemos el movimiento actual
            Map<String, Object> movimientoActual = obtenerMovimientoPorId(idMovimiento);
            
            // Verificar que se haya encontrado el movimiento
            if (movimientoActual == null) {
                System.err.println("No se encontró el movimiento con ID: " + idMovimiento);
                return false; // No se puede actualizar si no existe el movimiento
            }
            
            // Verificar que el socio coincida con el del movimiento
            int socioDelMovimiento = (int) movimientoActual.get("NoSocio");
            // Asegurarnos de que el socio del movimiento coincide con el proporcionado
            if (socioDelMovimiento != noSocio) {
                System.err.println("El socio del movimiento (" + socioDelMovimiento + 
                    ") no coincide con el socio proporcionado (" + noSocio + ")"); // No se puede actualizar si el socio no coincide
                return false; // No se puede actualizar si el socio no coincide
            }


            String tipoSocioStr = (String) movimientoActual.get("TipoSocio"); // Obtener el tipo de socio del movimiento
            boolean esInfantil = "INFANTIL".equals(tipoSocioStr); // Verificar si el tipo de socio es infantil
            
            System.out.println("Actualizando movimiento ID: " + idMovimiento + 
                " para socio #" + noSocio + ", tipo: " + tipoSocioStr);
            System.out.println("Nuevos valores: AportDeposito=" + aportacionDeposito + 
                ", AportRetiro=" + aportacionRetiro +
                ", PrestDeposito=" + prestamoDeposito + 
                ", PrestRetiro=" + prestamoRetiro +
                ", AhorroDeposito=" + ahorroDeposito + 
                ", AhorroRetiro=" + ahorroRetiro +
                ", InteresDeuda=" + interesDeuda); 
            
            // Obtenemos los valores anteriores para poder calcular las diferencias
            double aportacionDepositoAnterior = (double) movimientoActual.get("AporIngresos");
            double aportacionRetiroAnterior = (double) movimientoActual.get("AporEgresos");
            double prestamoDepositoAnterior = (double) movimientoActual.get("PresIngresos");
            double prestamoRetiroAnterior = (double) movimientoActual.get("PresEgresos");
            double ahorroDepositoAnterior = (double) movimientoActual.get("AhoIngresos");
            double ahorroRetiroAnterior = (double) movimientoActual.get("AhoEgresos"); // Obtenemos el interés anterior pero no lo usamos para cálculos de saldo
            // Ya que los intereses son un pago directo y no afectan los saldos
            @SuppressWarnings("unused")
            double interesDeudaAnterior = (double) movimientoActual.get("RetInteres");
            
            // Obtenemos los saldos actuales del socio
            Map<String, Object> datosFinancieros = obtenerDatosFinancierosSocio(noSocio, esInfantil);
            
            if (datosFinancieros == null) {
                System.err.println("No se pudieron obtener los datos financieros del socio #" + noSocio);
                return false;
            }
            
            // Calcular los nuevos saldos
            double aportacionSaldoActual = (Double) datosFinancieros.getOrDefault("AporSaldo", 0.0);
            double prestamoSaldoActual = (Double) datosFinancieros.getOrDefault("PresSaldo", 0.0);
            double ahorroSaldoActual = (Double) datosFinancieros.getOrDefault("AhoSaldo", 0.0);
            
            // Revertir el efecto del movimiento anterior
            aportacionSaldoActual -= aportacionDepositoAnterior;
            aportacionSaldoActual += aportacionRetiroAnterior;
            prestamoSaldoActual += prestamoDepositoAnterior;
            prestamoSaldoActual -= prestamoRetiroAnterior;
            ahorroSaldoActual -= ahorroDepositoAnterior;
            ahorroSaldoActual += ahorroRetiroAnterior;
            
            // Aplicar el efecto del nuevo movimiento
            aportacionSaldoActual += aportacionDeposito;
            aportacionSaldoActual -= aportacionRetiro;
            prestamoSaldoActual -= prestamoDeposito;
            prestamoSaldoActual += prestamoRetiro;
            ahorroSaldoActual += ahorroDeposito;
            ahorroSaldoActual -= ahorroRetiro;            // Actualizar el movimiento en la base de datos
            System.out.println("Actualizando saldos: AporSaldo=" + aportacionSaldoActual + 
                             ", PresSaldo=" + prestamoSaldoActual + 
                             ", AhoSaldo=" + ahorroSaldoActual);
                             
            // Preparar la consulta de actualización   
            String consulta = "UPDATE MovimientosSocio SET " +
                    "AporIngresos = ?, AporEgresos = ?, " +
                    "PresIngresos = ?, PresEgresos = ?, " +
                    "AhoIngresos = ?, AhoEgresos = ?, " +
                    "RetInteres = ?, Intereses = ?, " +
                    "AporSaldo = ?, PresSaldo = ?, AhoSaldo = ? " +
                    "WHERE IdMov = ?";
            
            // Preparar el PreparedStatement para la actualización  
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            // Establecer los parámetros de la consulta
            statement.setDouble(1, aportacionDeposito);
            statement.setDouble(2, aportacionRetiro);
            statement.setDouble(3, prestamoDeposito);
            statement.setDouble(4, prestamoRetiro);
            statement.setDouble(5, ahorroDeposito);
            statement.setDouble(6, ahorroRetiro);// Calculamos el interés actual basado en el saldo de préstamos
            double interesCalculado = prestamoSaldoActual * TASA_INTERES_PRESTAMO; // Asumimos una tasa de interés fija para el cálculo
            
            statement.setDouble(7, interesDeuda);
            statement.setDouble(8, interesCalculado);
            statement.setDouble(9, aportacionSaldoActual);
            statement.setDouble(10, prestamoSaldoActual);
            statement.setDouble(11, ahorroSaldoActual);
            statement.setInt(12, idMovimiento);
            
            // Informar sobre la actualización que se va a realizar
            System.out.println("Ejecutando actualización del movimiento con " + 12 + " parámetros:");
            System.out.println("1: AporIngresos = " + aportacionDeposito);
            System.out.println("2: AporEgresos = " + aportacionRetiro);
            System.out.println("3: PresIngresos = " + prestamoDeposito);
            System.out.println("4: PresEgresos = " + prestamoRetiro);
            System.out.println("5: AhoIngresos = " + ahorroDeposito);
            System.out.println("6: AhoEgresos = " + ahorroRetiro);
            System.out.println("7: RetInteres = " + interesDeuda);
            System.out.println("8: Intereses = " + interesCalculado);
            System.out.println("9: AporSaldo = " + aportacionSaldoActual);
            System.out.println("10: PresSaldo = " + prestamoSaldoActual);
            System.out.println("11: AhoSaldo = " + ahorroSaldoActual);
            System.out.println("12: IdMov = " + idMovimiento);
            
            int filasAfectadas = statement.executeUpdate(); // Ejecutar la actualización y obtener el número de filas afectadas
            statement.close(); // Cerrar el PreparedStatement después de la actualización
            
            System.out.println("Actualización completada con " + filasAfectadas + " filas afectadas");
            
            // Verificar si se actualizó al menos un registro
            return (filasAfectadas > 0);
            } catch (SQLException e) {
            System.err.println("Error al actualizar movimiento: " + e.getMessage());
            System.err.println("Código de error SQL: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("ID del movimiento: " + idMovimiento);
            System.err.println("Detalles completos de la excepción:");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene la lista de socios con intereses pendientes altos
     * @param montoMinimo El monto mínimo para considerar el interés pendiente como alto
     * @return Lista de socios con intereses pendientes altos
     */
    public List<Map<String, Object>> obtenerSociosConInteresesPendientesAltos(double montoMinimo) {
        List<Map<String, Object>> sociosConInteresesAltos = new ArrayList<>(); // Lista para almacenar los socios con intereses pendientes altos
        
        // Verificar que la conexión esté activa
        try {
            // Consultar todos los socios
            String consulta = "SELECT s.NoSocio, s.Nombres, s.Apellidos FROM Socios s " +
                              "INNER JOIN MovimientoSocios m ON s.NoSocio = m.NoSocio " +
                              "WHERE m.InteresesPendientes > ? " +
                              "GROUP BY s.NoSocio, s.Nombres, s.Apellidos " +
                              "ORDER BY m.InteresesPendientes DESC";
            
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            pstmt.setDouble(1, montoMinimo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int idSocio = rs.getInt("NoSocio");
                
                // Para cada socio, obtener sus datos financieros actualizados
                Map<String, Object> datosFinancieros = obtenerDatosFinancierosSocio(idSocio, false);
                
                // Si tiene intereses pendientes altos, añadirlo a la lista
                double interesPendiente = 0.0;
                if (datosFinancieros != null) {                    if (datosFinancieros.containsKey("InteresesPendientes")) {
                        Object valor = datosFinancieros.get("InteresesPendientes");
                        if (valor != null) {
                            try {
                                if (valor instanceof Double) {
                                    interesPendiente = (Double) valor;
                                } else if (valor instanceof Number) {
                                    interesPendiente = ((Number) valor).doubleValue();
                                } else if (valor instanceof String) {
                                    // Limpiar posibles caracteres no numéricos
                                    String cleanValue = ((String) valor).replaceAll("[^\\d.-]", "");
                                    if (!cleanValue.isEmpty()) {
                                        interesPendiente = Double.parseDouble(cleanValue);
                                    }
                                } else {
                                    // Para cualquier otro tipo, intentar convertir su representación en texto
                                    interesPendiente = Double.parseDouble(valor.toString().replaceAll("[^\\d.-]", ""));
                                }
                            } catch (NumberFormatException | NullPointerException e) {
                                System.err.println("Error al convertir InteresesPendientes '" + valor + "' para socio " + idSocio + ": " + e.getMessage());
                            }
                        }
                    }
                    
                    // Si no se encontró en el registro, calcular desde el historial
                    if (interesPendiente == 0.0) {
                        interesPendiente = calcularInteresesPendientesAcumulados(idSocio, datosFinancieros);
                    }
                    
                    // Si el monto supera el límite, añadir a la lista
                    if (interesPendiente >= montoMinimo) {
                        Map<String, Object> socio = new HashMap<>();
                        socio.put("ID", idSocio);
                        socio.put("Nombres", rs.getString("Nombres"));
                        socio.put("Apellidos", rs.getString("Apellidos"));
                        socio.put("InteresesPendientes", interesPendiente);
                        
                        sociosConInteresesAltos.add(socio);
                    }
                }
            }
            
            rs.close();
            pstmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener socios con intereses pendientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sociosConInteresesAltos;
    }
    
    /**
     * Calcula los intereses pendientes acumulados a partir del histórico de movimientos
     * @param idSocio ID del socio
     * @param datosFinancieros Mapa con los datos financieros del socio (si ya están disponibles)
     * @return Monto total de intereses pendientes
     */
    public double calcularInteresesPendientesAcumulados(int idSocio, Map<String, Object> datosFinancieros) {
        double totalInteresesPendientes = 0.0; // Variable para acumular los intereses pendientes
        
        // Verificar que la conexión esté activa
        try {
            // Si no se pasaron datos financieros, obtenerlos
            if (datosFinancieros == null) {
                datosFinancieros = obtenerDatosFinancierosSocio(idSocio, false);
            }
            
            // Verificar que existan movimientos
            if (datosFinancieros == null || !datosFinancieros.containsKey("Movimientos")) {
                return totalInteresesPendientes;
            }
              // Se usa SuppressWarnings ya que sabemos que la estructura es correcta
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> movimientos = (List<Map<String, Object>>) datosFinancieros.get("Movimientos");
            if (movimientos == null || movimientos.isEmpty()) {
                return totalInteresesPendientes;
            }
            
            // Recorrer los movimientos y acumular los intereses que no fueron pagados
            for (Map<String, Object> movimiento : movimientos) {                
                double intereses = getDoubleValue(movimiento, "Intereses");
                double interesesPagados = getDoubleValue(movimiento, "InteresesPagados");
                
                try {
                    // Acumular la diferencia entre intereses generados y pagados
                    double interesesPendientes = intereses - interesesPagados;
                    if (interesesPendientes > 0) {
                        totalInteresesPendientes += interesesPendientes;
                    }
                } catch (Exception e) {
                    System.err.println("Error al calcular intereses pendientes para un movimiento: " + e.getMessage());
                    System.err.println("Intereses: " + intereses + ", Pagados: " + interesesPagados);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al calcular intereses pendientes acumulados: " + e.getMessage());
            e.printStackTrace();
        }
        
        return totalInteresesPendientes;
    }
    
    /**
     * Obtiene un valor double de un mapa, manejando valores nulos
     * @param map Mapa de datos
     * @param key Clave a buscar
     * @return Valor como double (0.0 si no existe o es null)
     */    
    private double getDoubleValue(Map<String, Object> map, String key) {
        // Verificar si el mapa es nulo o no contiene la clave
        if (map == null || !map.containsKey(key) || map.get(key) == null) {
            return 0.0;
        }
        
        Object value = map.get(key); // Obtener el valor asociado a la clave
        try {
            // Intentar convertir el valor a double
            if (value instanceof Double) {
                return (Double) value;
            } else if (value instanceof Number) { // Si es un número, convertir a double
                return ((Number) value).doubleValue();
            } else if (value instanceof String) { // Si es una cadena, limpiar caracteres no numéricos
                // Limpiar posibles caracteres no numéricos (como "$" o ",")
                String cleanValue = ((String) value).replaceAll("[^\\d.-]", "");
                if (!cleanValue.isEmpty()) { // Verificar que la cadena no esté vacía después de limpiar
                    return Double.parseDouble(cleanValue); // Convertir a double
                }
            } else if (value != null) { // Si es otro tipo de objeto, intentar convertir su representación en texto
                // Para cualquier otro tipo, intentar convertir su representación en texto
                String stringValue = value.toString().replaceAll("[^\\d.-]", "");
                if (!stringValue.isEmpty()) { // Verificar que la cadena no esté vacía después de limpiar
                    return Double.parseDouble(stringValue); // Convertir a double
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
            System.err.println("Error al convertir valor '" + value + "' para la clave '" + key + "': " + e.getMessage());
        }
        return 0.0; // Retornar 0.0 si no se pudo convertir el valor
    }
    
    /**
     * Obtiene los intereses generados en un año específico para un socio
     * @param idSocio ID del socio
     * @param año El año para el cual se desean obtener los intereses
     * @return La suma de intereses generados en ese año
     */    
    
     public double obtenerInteresesPorAño(int idSocio, int año) {
        double totalIntereses = 0.0; // Variable para almacenar el total de intereses generados
        double totalRetirosIntereses = 0.0; // Variable para almacenar el total de retiros de intereses
        
        try {
            // Consulta SQL para obtener la suma de intereses del año especificado
            String consultaIntereses = "SELECT SUM(Intereses) as TotalIntereses FROM MovimientosSocio " +
                             "WHERE NoSocio = ? AND YEAR(Fecha) = ?";
            
            PreparedStatement statementIntereses = conexion.getConexion().prepareStatement(consultaIntereses); // Preparar la consulta con los parámetros necesarios
            statementIntereses.setInt(1, idSocio); // Establecer el ID del socio en la consulta
            statementIntereses.setInt(2, año); // Establecer el año en la consulta
            
            ResultSet resultadoIntereses = statementIntereses.executeQuery(); // Ejecutar la consulta y obtener el resultado
            
            // Verificar si se obtuvo algún resultado y extraer el total de intereses
            if (resultadoIntereses.next()) {
                totalIntereses = resultadoIntereses.getDouble("TotalIntereses"); // Obtener el total de intereses
                if (resultadoIntereses.wasNull()) { // Si el resultado es nulo, asignar 0.0
                    totalIntereses = 0.0; // Asignar 0.0 si no hay intereses
                }
            }
            
            resultadoIntereses.close(); // Cerrar el ResultSet después de obtener los datos
            statementIntereses.close(); // Cerrar el PreparedStatement después de ejecutar la consulta
            
            // Obtener retiros de intereses realizados en el mismo año para el mismo socio
            String consultaRetiros = "SELECT SUM(RetInteres) as TotalRetiros FROM MovimientosSocio " +
                                    "WHERE NoSocio = ? AND YEAR(Fecha) = ? AND RetInteres > 0";
            
            PreparedStatement statementRetiros = conexion.getConexion().prepareStatement(consultaRetiros); // Preparar la consulta para obtener los retiros de intereses
            statementRetiros.setInt(1, idSocio); // Establecer el ID del socio en la consulta de retiros
            statementRetiros.setInt(2, año); // Establecer el año en la consulta de retiros
            
            ResultSet resultadoRetiros = statementRetiros.executeQuery(); // Ejecutar la consulta de retiros y obtener el resultado
            
            // Verificar si se obtuvo algún resultado y extraer el total de retiros de intereses
            if (resultadoRetiros.next()) { 
                totalRetirosIntereses = resultadoRetiros.getDouble("TotalRetiros"); // Obtener el total de retiros de intereses
                if (resultadoRetiros.wasNull()) { // Si el resultado es nulo, asignar 0.0
                    totalRetirosIntereses = 0.0; // Asignar 0.0 si no hay retiros
                }
            }
            
            resultadoRetiros.close();
            statementRetiros.close();
            
            // El disponible es la diferencia entre lo generado y lo retirado
            double interesesDisponibles = totalIntereses - totalRetirosIntereses;
            if (interesesDisponibles < 0) interesesDisponibles = 0; // Asegurarse de que no haya un valor negativo
            
            return interesesDisponibles; // Retornar el total de intereses disponibles para el año especificado
            
        } catch (SQLException e) {
            System.err.println("Error al obtener intereses por año: " + e.getMessage());
            e.printStackTrace();
        }
        
        return totalIntereses; // Retornar 0.0 si ocurre un error o no se encuentran intereses
    }
    
    /**
     * Registra un retiro de intereses
     * @param movimiento Mapa con los datos del movimiento
     * @return true si el registro fue exitoso, false en caso contrario
     */    
    
     public boolean registrarRetiroIntereses(Map<String, Object> movimiento) {
        
        // Verificar que la conexión esté activa
        try {
            // Iniciar la transacción
            conexion.getConexion().setAutoCommit(false);
            
            // Obtener los valores del mapa
            int idSocio = (int) movimiento.get("IdSocio"); // ID del socio al que pertenece el movimiento
            double montoRetiro = (double) movimiento.get("RetInteres"); // Monto del retiro de intereses
            java.util.Date fecha = (java.util.Date) movimiento.get("Fecha"); // Fecha del movimiento
            boolean esInfantil = (boolean) movimiento.get("EsInfantil"); // Indica si el socio es infantil o adulto
            // El año de interés está disponible en el mapa como "AñoInteres" 
            
            // Obtener los datos financieros actuales del socio
            Map<String, Object> datosFinancieros = obtenerDatosFinancierosSocio(idSocio, esInfantil);
            
            // Verificar que se hayan obtenido los datos financieros
            if (datosFinancieros == null) {
                System.err.println("No se pudieron obtener los datos financieros del socio #" + idSocio);
                return false; // No se puede registrar el retiro si no hay datos financieros
            }
            
            double saldoAhorros = (Double) datosFinancieros.getOrDefault("AhoSaldo", 0.0); // Saldo actual de ahorros del socio
            
            // El tipo de socio lo determina si es infantil o no
            String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO";
            
            // Crear el movimiento en la tabla MovimientosSocio
            String consultaMovimiento = "INSERT INTO MovimientosSocio " +
                "(NoSocio, Fecha, AporIngresos, AporEgresos, AporSaldo, PresEgresos, PresIngresos, " +
                "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, TipoSocio, " +
                "RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = conexion.getConexion().prepareStatement(consultaMovimiento); // Preparar la consulta de inserción
            
            statement.setInt(1, idSocio);                 // NoSocio
            statement.setDate(2, new java.sql.Date(fecha.getTime()));  // Fecha
            statement.setDouble(3, 0.0);                  // AporIngresos
            statement.setDouble(4, 0.0);                  // AporEgresos
            statement.setDouble(5, 0.0);                  // AporSaldo
            statement.setDouble(6, 0.0);                  // PresEgresos
            statement.setDouble(7, 0.0);                  // PresIngresos
            statement.setDouble(8, 0.0);                  // PresSaldo
            statement.setDouble(9, 0.0);                  // Intereses
            statement.setDouble(10, 0.0);                 // AhoIngresos
            statement.setDouble(11, 0.0);                 // AhoEgresos
            statement.setDouble(12, saldoAhorros); // AhoSaldo
            statement.setString(13, tipoSocio); // TipoSocio
            statement.setDouble(14, montoRetiro);         // RetInteres - monto de retiro de intereses
            statement.setDouble(15, 0.0);                 // SaldoBanco
            statement.setDouble(16, 0.0);                 // RetBanco
            statement.setDouble(17, 0.0);                 // IngOtros
            statement.setDouble(18, 0.0);                 // EgrOtros
            statement.setDouble(19, 0.0);                 // GastosAdmon
            
            int filasAfectadas = statement.executeUpdate(); // Ejecutar la inserción y obtener el número de filas afectadas
            
            // Si se registró el movimiento correctamente, registrar también en la tabla InteresesRetirados
            if (filasAfectadas > 0) {
                // Obtener el ID del movimiento generado
                ResultSet rs = statement.getGeneratedKeys();
                int idMovimientoNuevo = -1; // Inicializar con un valor inválido
                
                // Verificar si se obtuvo el ID del movimiento
                if (rs.next()) {
                    idMovimientoNuevo = rs.getInt(1); // Obtener el ID del movimiento recién insertado
                }
                rs.close();
                
                // Si estamos usando una tabla específica para intereses retirados, registrar ahí también
                String consultaIntereses = "INSERT INTO InteresesRetirados " +
                    "(NoSocio, IdMovimiento, Año, Monto, Fecha, TipoSocio) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
                
                // Preparar la consulta para registrar el retiro de intereses
                try {
                    PreparedStatement stmtIntereses = conexion.getConexion().prepareStatement(consultaIntereses); // Preparar la consulta de inserción en InteresesRetirados
                    stmtIntereses.setInt(1, idSocio); 
                    stmtIntereses.setInt(2, idMovimientoNuevo);
                    stmtIntereses.setInt(3, (int) movimiento.getOrDefault("AñoInteres", java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
                    stmtIntereses.setDouble(4, montoRetiro);
                    stmtIntereses.setDate(5, new java.sql.Date(fecha.getTime()));
                    stmtIntereses.setString(6, tipoSocio);
                    
                    stmtIntereses.executeUpdate(); // Ejecutar la inserción en InteresesRetirados
                    stmtIntereses.close(); // Cerrar el PreparedStatement después de la inserción
                } catch (SQLException ex) {
                    // Si la tabla no existe, solo lo registraremos en MovimientosSocio
                    System.out.println("Aviso: No se pudo registrar en InteresesRetirados: " + ex.getMessage());
                }
                
                System.out.println("Retiro de intereses registrado correctamente para socio #" + idSocio + 
                                 " por monto: " + montoRetiro + " del año: " + movimiento.getOrDefault("AñoInteres", java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
                return true; // Indicar que el registro fue exitoso
            }
            
            return false; // Si no se afectaron filas, retornar false indicando que no se pudo registrar el retiro
            
        } catch (Exception e) {
            System.err.println("Error al registrar retiro de intereses: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
      /**
     * Actualiza la fecha de un movimiento específico
     * @param idMovimiento ID del movimiento a actualizar
     * @param fecha Nueva fecha para el movimiento
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarFechaMovimiento(int idMovimiento, java.util.Date fecha) {
        try {
            String consulta = "UPDATE MovimientosSocio SET Fecha = ? WHERE IdMov = ?"; // Consulta para actualizar la fecha del movimiento
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta); // Preparar el PreparedStatement para la actualización
            statement.setDate(1, new java.sql.Date(fecha.getTime())); // Establecer la nueva fecha del movimiento
            statement.setInt(2, idMovimiento); // Establecer el ID del movimiento a actualizar
            
            int filasAfectadas = statement.executeUpdate(); // Ejecutar la actualización y obtener el número de filas afectadas
            statement.close(); // Cerrar el PreparedStatement después de la actualización
            
            return filasAfectadas > 0; // Retornar true si se actualizó al menos un registro, false en caso contrario
            
        } catch (Exception e) {
            System.err.println("Error al actualizar fecha de movimiento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
