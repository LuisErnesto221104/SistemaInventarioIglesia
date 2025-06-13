package view;

import java.util.*;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.Font;

import tools.SocioDAO;
import tools.TablaBase;

/**
 * ListadoSocios
 * Ventana que muestra el listado de socios
 */
public class ListadoSocios extends JFrame {
    
    private PanelListadoSocios panelSocios;
    
    public ListadoSocios() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Listado de Socios");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Crear panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña de socios adultos
        panelSocios = new PanelListadoSocios(false);
        tabbedPane.addTab("Socios Adultos", panelSocios);
        
        // Pestaña de socios infantiles
        PanelListadoSocios panelSociosInfantiles = new PanelListadoSocios(true);
        tabbedPane.addTab("Socios Infantiles", panelSociosInfantiles);
        
        // Añadir panel de pestañas al formulario
        add(tabbedPane);
        
        // Cargar datos
        panelSocios.cargarDatos();
        panelSociosInfantiles.cargarDatos();
        
        // Mostrar formulario
        setVisible(true);
    }
    
    /**
     * PanelListadoSocios
     * Panel que muestra una tabla con el listado de socios
     */
    private class PanelListadoSocios extends TablaBase {
        
        private SocioDAO socioDAO;
        private boolean esInfantil;
        
        public PanelListadoSocios(boolean esInfantil) {
            super(new String[] {"No. Socio", "Nombres", "Apellidos", "Dirección", 
                "Teléfono", "Fecha Registro", "Presentado Por", "Población"});
            
            this.esInfantil = esInfantil;            socioDAO = new SocioDAO();
            
            // Configurar apariencia de la tabla después de la inicialización
            configurarAparienciaTabla();
        }
          /**
         * Configura la apariencia visual de la tabla con letra más grande
         */
        private void configurarAparienciaTabla() {
            // Aumentar tamaño de letra de la tabla
            Font tableFont = new Font("Arial", Font.PLAIN, 14);
            tabla.setFont(tableFont);
            tabla.setRowHeight(24); // Aumentar altura de filas para adaptarse a la letra más grande
            
            // Aumentar tamaño de letra del encabezado
            Font headerFont = new Font("Arial", Font.BOLD, 14);
            tabla.getTableHeader().setFont(headerFont);
            
            // Cambiar el modo de redimensionamiento y configurar anchos específicos para cada columna
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(50);   // No. Socio
            columnModel.getColumn(1).setPreferredWidth(140);  // Nombres
            columnModel.getColumn(2).setPreferredWidth(140);  // Apellidos
            columnModel.getColumn(3).setPreferredWidth(250);  // Dirección 
            columnModel.getColumn(4).setPreferredWidth(110);  // Teléfono
            columnModel.getColumn(5).setPreferredWidth(120);  // Fecha Registro
            columnModel.getColumn(6).setPreferredWidth(140);  // Presentado Por
            columnModel.getColumn(7).setPreferredWidth(140);  // Población
        }
          @Override
        public void cargarDatos() {
            // Limpiar modelo
            modelo.setRowCount(0);
            
            // Obtener datos según el tipo
            List<Map<String, Object>> listaSocios;
            if (esInfantil) {
                listaSocios = socioDAO.listarSociosInfantiles();
            } else {
                listaSocios = socioDAO.listarSocios();
            }
            
            // Llenar tabla
            for (Map<String, Object> socio : listaSocios) {
                modelo.addRow(new Object[] {
                    socio.get("NoSocio"),
                    socio.get("Nombres"),
                    socio.get("Apellidos"),
                    socio.get("Direccion"),
                    socio.get("Telefono"),
                    socio.get("FechaRegistro"),
                    socio.get("PresentadoPor"),
                    socio.get("Poblacion")
                });
            }
            
            // Actualizar el contador de registros
            actualizarContador();
        }
          @Override
        protected void buscar() {
            aplicarFiltro(txtBuscar.getText());
            // No necesitamos llamar a actualizarContador() aquí porque aplicarFiltro() ya lo hace
        }
    }
    
    /**
     * Método principal para pruebas
     */
    public static void main(String[] args) {
        // Intentar aplicar el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar el formulario
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ListadoSocios();
            }
        });
    }
}
