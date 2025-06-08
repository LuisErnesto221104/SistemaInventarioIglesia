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
            
            PreparedStatement statement = conexion.getConexion().prepareStatement(consulta);
            statement.setInt(1, noSocio);
            
            ResultSet resultado = statement.executeQuery();
            
            if (resultado.next()) {
                tienePrestamo = resultado.getInt("Cantidad") > 0;
            }
            
            resultado.close();
            statement.close();
            
        } catch (SQLException e) {
            System.err.println("Error al verificar préstamos pendientes: " + e.getMessage());
            e.printStackTrace();
            // En caso de error, asumimos que no tiene préstamos pendientes para poder continuar
            tienePrestamo = false;
        }
        
        return tienePrestamo;
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
        Map<String, Object> datosFinancieros = new HashMap<>();
        List<Map<String, Object>> listaMovimientos = new ArrayList<>();
        
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
          String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO";
        System.out.println("Obteniendo datos financieros para socio #" + noSocio + ", tipo: " + tipoSocio);
        
        try {
            // Primero verificamos si el socio realmente existe en la tabla correspondiente
            Map<String, Object> socioInfo = esInfantil ? 
                buscarSocioInfantilPorID(noSocio) : 
                buscarSocioAdultoPorID(noSocio);
                
            if (socioInfo == null) {
                System.err.println("No se encontró el socio " + tipoSocio + " con número " + noSocio + " en la tabla de socios");
                // Verificar si existe en la otra categoría antes de dar error
                Map<String, Object> socioAlternativo = esInfantil ? 
                    buscarSocioAdultoPorID(noSocio) : 
                    buscarSocioInfantilPorID(noSocio);
                    
                if (socioAlternativo != null) {
                    System.out.println("Socio #" + noSocio + " encontrado en la categoría contraria: " + 
                                     (esInfantil ? "adulto" : "infantil") + " pero se solicitó como " + tipoSocio);
                }
            }            // Consultar la tabla MovimientosSocio para obtener todos los registros de movimientos
            String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio);
            String consultaMovimientos;
            PreparedStatement stmtMovimientos;
            
            if (tipoSocioUpperCase.equals("INFANTIL")) {
                consultaMovimientos = 
                    "SELECT AporIngresos, AporEgresos, AporSaldo, PresIngresos, PresEgresos, " +
                    "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, Fecha, TipoSocio, " +
                    "RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon " +
                    "FROM MovimientosSocio " +
                    "WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE') " +
                    "ORDER BY Fecha DESC";
            } else {
                consultaMovimientos = 
                    "SELECT AporIngresos, AporEgresos, AporSaldo, PresIngresos, PresEgresos, " +
                    "PresSaldo, Intereses, AhoIngresos, AhoEgresos, AhoSaldo, Fecha, TipoSocio, " +
                    "RetInteres, SaldoBanco, RetBanco, IngOtros, EgrOtros, GastosAdmon " +
                    "FROM MovimientosSocio " +
                    "WHERE NoSocio = ? AND TipoSocio = ? " +
                    "ORDER BY Fecha DESC";
            }
              
            stmtMovimientos = conexion.getConexion().prepareStatement(consultaMovimientos);
            stmtMovimientos.setInt(1, noSocio);
            stmtMovimientos.setString(2, tipoSocioUpperCase);
            
            System.out.println("Ejecutando consulta para MovimientosSocio con socio #" + noSocio + " y tipo " + tipoSocio);
            ResultSet rsMovimientos = stmtMovimientos.executeQuery();
            
            boolean primerRegistro = true;
            while (rsMovimientos.next()) {
                // Crear un mapa para cada movimiento
                Map<String, Object> movimiento = new HashMap<>();
                
                // Añadir todos los campos al movimiento
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
                movimiento.put("SaldoTotal", saldoTotal);
                
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
                    
                    primerRegistro = false;
                }
            }
            
            // Agregar la lista de movimientos al mapa de datos financieros
            datosFinancieros.put("Movimientos", listaMovimientos);
            
            // Si no se encontró ningún registro en MovimientosSocio, usar el método de respaldo
            if (listaMovimientos.isEmpty()) {
                System.out.println("No se encontraron registros en MovimientosSocio para el socio " + noSocio + 
                                 " de tipo " + tipoSocio + ". Usando método alternativo.");
                obtenerDatosFinancierosDesdeTablas(noSocio, esInfantil, datosFinancieros);
            } else {
                System.out.println("Se encontraron " + listaMovimientos.size() + " movimientos para el socio #" + 
                                  noSocio + " de tipo " + tipoSocio);
            }
            
            rsMovimientos.close();
            stmtMovimientos.close();
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
        
        return datosFinancieros;
    }
    
    /**
     * Método auxiliar para obtener datos financieros de las tablas individuales
     * Se utiliza como respaldo cuando no hay registros en MovimientosSocio
     * 
     * @param noSocio Número de socio
     * @param esInfantil Indica si el socio es infantil
     * @param datosFinancieros Mapa donde se almacenarán los datos financieros
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */    private void obtenerDatosFinancierosDesdeTablas(int noSocio, boolean esInfantil, Map<String, Object> datosFinancieros) throws SQLException {
        String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO";
        System.out.println("Buscando datos financieros desde tablas individuales para socio " + noSocio + 
                         " de tipo " + tipoSocio);
                           
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
        
        PreparedStatement stmtDeposito = conexion.getConexion().prepareStatement(consultaDeposito);
        stmtDeposito.setInt(1, noSocio);
        
        ResultSet rsDeposito = stmtDeposito.executeQuery();
        
        if (rsDeposito.next()) {
            double total = rsDeposito.getDouble("Total");
            datosFinancieros.put("AporIngresos", total);
            datosFinancieros.put("AporSaldo", total);
            datosFinancieros.put("AportacionSocial", total);
        }
        
        rsDeposito.close();
        stmtDeposito.close();
        
        // Obtener datos de préstamos desde la tabla Prestamo
        String consultaPrestamo = "SELECT SUM(Cantidad) AS Total FROM Prestamo WHERE NoSocio = ?";
        
        PreparedStatement stmtPrestamo = conexion.getConexion().prepareStatement(consultaPrestamo);
        stmtPrestamo.setInt(1, noSocio);
        
        ResultSet rsPrestamo = stmtPrestamo.executeQuery();
        
        if (rsPrestamo.next()) {
            double total = rsPrestamo.getDouble("Total");
            datosFinancieros.put("PresIngresos", total);
            datosFinancieros.put("PresSaldo", total);
        }
        
        rsPrestamo.close();
        stmtPrestamo.close();        // Obtener datos de ahorros desde la tabla MovimientosSocio para ambos tipos de socios
        String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio);
        String consultaAhorro;
        PreparedStatement stmtAhorro;
        
        // Si el tipo es INFANTIL, también buscar con INFANTE
        if (tipoSocioUpperCase.equals("INFANTIL")) {
            consultaAhorro = "SELECT SUM(AhoIngresos) AS TotalIngresos, SUM(AhoEgresos) AS TotalEgresos, " +
                           "MAX(AhoSaldo) AS UltimoSaldo FROM MovimientosSocio WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE')";
        } else {
            consultaAhorro = "SELECT SUM(AhoIngresos) AS TotalIngresos, SUM(AhoEgresos) AS TotalEgresos, " +
                           "MAX(AhoSaldo) AS UltimoSaldo FROM MovimientosSocio WHERE NoSocio = ? AND TipoSocio = ?";
        }
        
        stmtAhorro = conexion.getConexion().prepareStatement(consultaAhorro);
        stmtAhorro.setInt(1, noSocio);
        stmtAhorro.setString(2, tipoSocioUpperCase);
        
        ResultSet rsAhorro = stmtAhorro.executeQuery();
        
        if (rsAhorro.next() && rsAhorro.getDouble("UltimoSaldo") > 0) {
            // Si encontramos datos en MovimientosSocio, usarlos
            datosFinancieros.put("AhoIngresos", rsAhorro.getDouble("TotalIngresos"));
            datosFinancieros.put("AhoEgresos", rsAhorro.getDouble("TotalEgresos"));
            datosFinancieros.put("AhoSaldo", rsAhorro.getDouble("UltimoSaldo"));
            System.out.println("Datos de ahorro obtenidos de MovimientosSocio para socio " + noSocio + 
                             " tipo " + tipoSocio);
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
        
        rsAhorro.close();
        stmtAhorro.close();
        
        // Intentar obtener intereses de la tabla DepPrestamo
        String consultaIntereses = "SELECT SUM(Intereses) AS Total FROM DepPrestamo WHERE NoSocio = ?";
        
        PreparedStatement stmtIntereses = conexion.getConexion().prepareStatement(consultaIntereses);
        stmtIntereses.setInt(1, noSocio);
        
        ResultSet rsIntereses = stmtIntereses.executeQuery();
        
        if (rsIntereses.next()) {
            double total = rsIntereses.getDouble("Total");
            datosFinancieros.put("Intereses", total);
        }
        
        rsIntereses.close();
        stmtIntereses.close();
        
        // Calcular el saldo total
        double ahoSaldo = ((Number)datosFinancieros.getOrDefault("AhoSaldo", 0.0)).doubleValue();
        double aporSaldo = ((Number)datosFinancieros.getOrDefault("AporSaldo", 0.0)).doubleValue();
        double presSaldo = ((Number)datosFinancieros.getOrDefault("PresSaldo", 0.0)).doubleValue();
        double saldoTotal = ahoSaldo + aporSaldo - presSaldo;
        datosFinancieros.put("SaldoTotal", saldoTotal);
        
        // Crear un movimiento artificial para mantener la consistencia de la interfaz
        Map<String, Object> movimiento = new HashMap<>();
        movimiento.putAll(datosFinancieros);
        movimiento.put("Fecha", new java.util.Date());
        movimiento.put("TipoSocio", tipoSocio);
        
        // Crear la lista de movimientos con este único movimiento
        List<Map<String, Object>> movimientos = new ArrayList<>();
        movimientos.add(movimiento);
        datosFinancieros.put("Movimientos", movimientos);
        System.out.println("Datos financieros completados para socio " + noSocio + " de tipo " + tipoSocio + 
                         " con saldo total: " + saldoTotal);
    }
    
    /**
     * Elimina un socio y traslada sus datos a la tabla de socios cancelados
     * @param noSocio Número de socio
     * @param esInfantil Indica si es socio infantil
     * @param totalRetiro Total a retirar
     * @return true si se eliminó correctamente, false en caso contrario
     */    public boolean eliminarSocio(int noSocio, boolean esInfantil, double totalRetiro) {
        boolean exito = false;
        Connection conexionActual = null;
        String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO";
        
        try {
            System.out.println("Iniciando eliminación de socio " + noSocio + " tipo: " + tipoSocio);
            conexionActual = conexion.getConexion();
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
            
            if (socio == null) {
                throw new Exception("No se encontró el socio " + tipoSocio + " con número " + noSocio);
            }
            
            // 3. Insertar en la tabla de cancelaciones
            String consultaInsertCancelado = "INSERT INTO Cancelaciones " +
                "(NoSocio, Descripcion, Cantidad, Fecha) " +
                "VALUES (?, ?, ?, ?)";
            
            PreparedStatement stmtInsertCancelado = conexionActual.prepareStatement(consultaInsertCancelado);
            stmtInsertCancelado.setInt(1, noSocio);
            stmtInsertCancelado.setString(2, "Cancelación de socio " + tipoSocio + ": " + 
                socio.get("Nombres") + " " + socio.get("Apellidos"));
            stmtInsertCancelado.setDouble(3, totalRetiro);
            stmtInsertCancelado.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Fecha actual
            
            stmtInsertCancelado.executeUpdate();
            stmtInsertCancelado.close();
            
            // 4. Limpiar todos los registros relacionados            // Eliminar de MovimientosSocio para el tipo específico
            try {
                String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio);
                String consultaMovimientos;
                PreparedStatement stmtMovimientos;
                
                if (tipoSocioUpperCase.equals("INFANTIL")) {
                    // Si es infantil, eliminar registros con ambos tipos: INFANTIL y INFANTE
                    consultaMovimientos = "DELETE FROM MovimientosSocio WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE')";
                } else {
                    consultaMovimientos = "DELETE FROM MovimientosSocio WHERE NoSocio = ? AND TipoSocio = ?";
                }
                
                stmtMovimientos = conexionActual.prepareStatement(consultaMovimientos);
                stmtMovimientos.setInt(1, noSocio);
                stmtMovimientos.setString(2, tipoSocioUpperCase);
                int filasMovimientos = stmtMovimientos.executeUpdate();
                stmtMovimientos.close();
                System.out.println("Registros eliminados de MovimientosSocio: " + filasMovimientos);
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de MovimientosSocio: " + e.getMessage());
            }
              // Todo se maneja a través de MovimientosSocio, no hay necesidad de usar tablas legacy
            
            // Eliminar registros de depósitos
            try {
                String consultaDeposito = "DELETE FROM Deposito WHERE NoSocio = ?";
                PreparedStatement stmtDeposito = conexionActual.prepareStatement(consultaDeposito);
                stmtDeposito.setInt(1, noSocio);
                int filasDeposito = stmtDeposito.executeUpdate();
                stmtDeposito.close();
                System.out.println("Registros de Deposito eliminados: " + filasDeposito);
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de Deposito: " + e.getMessage());
            }
            
            // Eliminar registros de préstamos
            try {
                String consultaPrestamo = "DELETE FROM Prestamo WHERE NoSocio = ?";
                PreparedStatement stmtPrestamo = conexionActual.prepareStatement(consultaPrestamo);
                stmtPrestamo.setInt(1, noSocio);
                int filasPrestamo = stmtPrestamo.executeUpdate();
                stmtPrestamo.close();
                System.out.println("Registros de Prestamo eliminados: " + filasPrestamo);
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de Prestamo: " + e.getMessage());
            }
            
            // Eliminar registros de depósitos de préstamo
            try {
                String consultaDepPrestamo = "DELETE FROM DepPrestamo WHERE NoSocio = ?";
                PreparedStatement stmtDepPrestamo = conexionActual.prepareStatement(consultaDepPrestamo);
                stmtDepPrestamo.setInt(1, noSocio);
                int filasDepPrestamo = stmtDepPrestamo.executeUpdate();
                stmtDepPrestamo.close();
                System.out.println("Registros de DepPrestamo eliminados: " + filasDepPrestamo);
            } catch (SQLException e) {
                System.err.println("Error al eliminar registros de DepPrestamo: " + e.getMessage());
            }
            
            // 5. Eliminar el socio de la tabla principal
            String tablaSocio = esInfantil ? "SociosInfa" : "Socios";
            try {
                String consultaEliminarSocio = "DELETE FROM " + tablaSocio + " WHERE NoSocio = ?";
                
                PreparedStatement stmtEliminarSocio = conexionActual.prepareStatement(consultaEliminarSocio);
                stmtEliminarSocio.setInt(1, noSocio);
                int filasSocio = stmtEliminarSocio.executeUpdate();
                stmtEliminarSocio.close();
                System.out.println("Socio eliminado de la tabla " + tablaSocio + ": " + filasSocio);
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
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                PreparedStatement stmtMovimiento = conexionActual.prepareStatement(consultaMovimiento);
                stmtMovimiento.setInt(1, noSocio);
                stmtMovimiento.setDate(2, new java.sql.Date(System.currentTimeMillis()));
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
                
                stmtMovimiento.executeUpdate();
                stmtMovimiento.close();
                System.out.println("Registrado movimiento final de cancelación para socio " + noSocio + 
                                  " tipo " + tipoSocio +
                                  " con retiro de " + totalRetiro);
                
                // Confirmar la transacción
                conexionActual.commit();
                exito = true;
                System.out.println("Eliminación de socio " + noSocio + " tipo " + tipoSocio + " completada con éxito");
            } catch (SQLException e) {
                System.err.println("Error al registrar movimiento final: " + e.getMessage());
                throw e;
            }
            
        } catch (Exception e) {
            System.err.println("Error al eliminar socio: " + e.getMessage());
            e.printStackTrace();
            
            try {
                if (conexionActual != null) {
                    conexionActual.rollback();
                    System.out.println("Se realizó rollback por error: " + e.getMessage());
                }
            } catch (SQLException ex) {
                System.err.println("Error al realizar rollback: " + ex.getMessage());
            }
            
        } finally {
            try {
                if (conexionActual != null) {
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
     */    public boolean existeEnMovimientos(int noSocio, String tipoSocio) {
        boolean existe = false;
        
        try {
            // Normalizar el tipo de socio
            String tipoSocioUpperCase = normalizarTipoSocio(tipoSocio);
            
            // Si el tipo es INFANTIL, también debemos buscar registros con INFANTE
            String consulta;
            PreparedStatement statement;
            
            if (tipoSocioUpperCase.equals("INFANTIL")) {
                consulta = "SELECT COUNT(*) AS Total FROM MovimientosSocio WHERE NoSocio = ? AND (TipoSocio = ? OR TipoSocio = 'INFANTE')";
                statement = conexion.getConexion().prepareStatement(consulta);
                statement.setInt(1, noSocio);
                statement.setString(2, tipoSocioUpperCase);
            } else {
                consulta = "SELECT COUNT(*) AS Total FROM MovimientosSocio WHERE NoSocio = ? AND TipoSocio = ?";
                statement = conexion.getConexion().prepareStatement(consulta);
                statement.setInt(1, noSocio);
                statement.setString(2, tipoSocioUpperCase);
            }
            
            ResultSet resultado = statement.executeQuery();
            
            if (resultado.next()) {
                existe = resultado.getInt("Total") > 0;
            }
            
            resultado.close();
            statement.close();
            
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
        if (tipoSocio == null) return null;
        
        String tipo = tipoSocio.toUpperCase();
        if (tipo.equals("INFANTE")) {
            return "INFANTIL";
        }
        return tipo;
    }
}
