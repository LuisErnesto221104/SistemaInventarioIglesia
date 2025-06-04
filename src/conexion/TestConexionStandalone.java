package conexion;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * TestConexionStandalone
 * 
 * Esta clase independiente verifica la conexión a la base de datos
 * sin depender del resto del proyecto.
 */
public class TestConexionStandalone {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            
        }
        
        
        JFrame frame = new JFrame("Test de Conexión a Base de Datos");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Panel con área de texto para mostrar resultados
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel();
        JButton testButton = new JButton("Probar Conexión");
        JButton closeButton = new JButton("Cerrar");
        
        buttonPanel.add(testButton);
        buttonPanel.add(closeButton);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        // Hacer visible el frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Acción para el botón de prueba
        testButton.addActionListener(_ -> {
            testButton.setEnabled(false);
            textArea.setText("");
            new Thread(() -> {
                try {
                    testConexion(textArea);
                } finally {
                    SwingUtilities.invokeLater(() -> testButton.setEnabled(true));
                }
            }).start();
        });
        
        // Acción para el botón de cerrar
        closeButton.addActionListener(_ -> System.exit(0));
        
        // Ejecutar la prueba automáticamente al inicio
        testButton.doClick();
    }
    
    private static void testConexion(JTextArea textArea) {
        appendToTextArea(textArea, "==========================================================\n");
        appendToTextArea(textArea, "        VERIFICACIÓN DE CONEXIÓN A BASE DE DATOS\n");
        appendToTextArea(textArea, "==========================================================\n\n");
        
        Connection conexion = null;
        
        try {
            // Verificar existencia del archivo de base de datos
            String rutaDB = "C:\\Users\\ernes\\eclipse-workspace\\SistemsaVentas\\bd\\dbcaja.mdb";
            File dbFile = new File(rutaDB);
            
            if (dbFile.exists()) {
                appendToTextArea(textArea, "Archivo de base de datos encontrado:\n   " + rutaDB + "\n\n");
            } else {
                appendToTextArea(textArea, "ADVERTENCIA: No se encontró el archivo de base de datos:\n   " + rutaDB + "\n\n");
            }
            
            // Cargar el driver
            appendToTextArea(textArea, "Cargando el driver UCanAccess...\n");
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            appendToTextArea(textArea, "Driver UCanAccess cargado correctamente.\n\n");
            
            // Establecer la conexión
            appendToTextArea(textArea, "Intentando conectar a la base de datos...\n");
            String url = "jdbc:ucanaccess://" + rutaDB;
            
            conexion = DriverManager.getConnection(url);
            appendToTextArea(textArea, "¡CONEXIÓN ESTABLECIDA CON ÉXITO!\n\n");
            
            // Mostrar información de la conexión
            DatabaseMetaData metaData = conexion.getMetaData();
            appendToTextArea(textArea, "Información de la conexión:\n");
            appendToTextArea(textArea, "- Driver: " + metaData.getDriverName() + "\n");
            appendToTextArea(textArea, "- Versión: " + metaData.getDriverVersion() + "\n");
            appendToTextArea(textArea, "- URL: " + metaData.getURL() + "\n");
            appendToTextArea(textArea, "- Usuario: " + metaData.getUserName() + "\n\n");
            
            // Mostrar las tablas
            appendToTextArea(textArea, "Tablas disponibles en la base de datos:\n");
            ResultSet tablas = metaData.getTables(null, null, "%", new String[] {"TABLE"});
            
            int tableCount = 0;
            while (tablas.next()) {
                appendToTextArea(textArea, "- " + tablas.getString("TABLE_NAME") + "\n");
                tableCount++;
            }
            
            if (tableCount == 0) {
                appendToTextArea(textArea, "No se encontraron tablas en la base de datos.\n");
            }
            
            appendToTextArea(textArea, "\n¡La verificación de conexión ha sido exitosa!\n");
        } catch (ClassNotFoundException e) {
            appendToTextArea(textArea, "ERROR: No se pudo cargar el driver UCanAccess.\n");
            appendToTextArea(textArea, "Detalles: " + e.getMessage() + "\n\n");
            appendToTextArea(textArea, "Asegúrate de que las siguientes librerías estén en el CLASSPATH:\n");
            appendToTextArea(textArea, "- ucanaccess-4.0.3.jar\n");
            appendToTextArea(textArea, "- commons-lang-2.6.jar\n");
            appendToTextArea(textArea, "- commons-logging-1.1.3.jar\n");
            appendToTextArea(textArea, "- hsqldb.jar\n");
            appendToTextArea(textArea, "- jackcess-2.1.9.jar\n");
            e.printStackTrace();
        } catch (SQLException e) {
            appendToTextArea(textArea, "ERROR: No se pudo conectar a la base de datos.\n");
            appendToTextArea(textArea, "Detalles: " + e.getMessage() + "\n");
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                    appendToTextArea(textArea, "\nLa conexión se ha cerrado correctamente.\n");
                } catch (SQLException e) {
                    appendToTextArea(textArea, "\nError al cerrar la conexión: " + e.getMessage() + "\n");
                }
            }
        }
    }
    
    private static void appendToTextArea(JTextArea textArea, String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}
