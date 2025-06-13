package view;

import javax.swing.*;
import javax.swing.table.*;

import tools.SocioDAO;

import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

/**
 * ListadoSociosInfantilesPanelIntegrado
 * Panel integrado para mostrar el listado de socios infantiles
 */
public class ListadoSociosInfantilesPanelIntegrado extends JPanel {
    
    private SocioDAO socioDAO;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private JLabel lblContador;
    private MenuPrincipal menuPrincipal;
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal
     */
    public ListadoSociosInfantilesPanelIntegrado(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        socioDAO = new SocioDAO();
        inicializarComponentes();
        cargarDatos();
    }
    
    /**
     * Inicializa los componentes del panel
     */
    private void inicializarComponentes() {
        // Configuración del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
          JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14)); // Letra más grande
        btnVolver.setPreferredSize(new Dimension(100, 30)); // Botón más grande
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        JLabel lblTitulo = new JLabel("LISTADO DE SOCIOS INFANTILES", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        panelTitulo.add(btnVolver, BorderLayout.WEST);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 10));
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JPanel panelBuscar = new JPanel(new BorderLayout(5, 0));        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Arial", Font.BOLD, 14)); // Letra más grande
        
        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 14)); // Letra más grande
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                aplicarFiltro(txtBuscar.getText());
            }
        });
        
        panelBuscar.add(lblBuscar, BorderLayout.WEST);
        panelBuscar.add(txtBuscar, BorderLayout.CENTER);
        
        JPanel panelContador = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblContador = new JLabel("Total: 0 registros");
        lblContador.setFont(new Font("Arial", Font.BOLD, 14)); // Letra más grande
        panelContador.add(lblContador);
        
        panelBusqueda.add(panelBuscar, BorderLayout.CENTER);
        panelBusqueda.add(panelContador, BorderLayout.EAST);
        
        // Panel tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        
        // Crear modelo de tabla
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        // Agregar columnas
        String[] columnas = {"N° Socio", "Fecha", "Nombres", "Apellidos", "Dirección", "Teléfono", "Presentado Por", "Población"};
        modelo.setColumnIdentifiers(columnas);        // Crear tabla
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); // Cambio a AUTO_RESIZE_SUBSEQUENT_COLUMNS para poder controlar los anchos
        tabla.getTableHeader().setReorderingAllowed(false);
        
        // Aumentar tamaño de letra de la tabla
        Font tableFont = new Font("Arial", Font.PLAIN, 14); // Tamaño de letra aumentado para los datos
        tabla.setFont(tableFont);
        tabla.setRowHeight(24); // Aumentar altura de filas para adaptarse a la letra más grande
        
        // Aumentar tamaño de letra del encabezado
        Font headerFont = new Font("Arial", Font.BOLD, 14);
        tabla.getTableHeader().setFont(headerFont);
        
        // Configurar anchos específicos para cada columna
        TableColumnModel columnModel = tabla.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);   // N° Socio 
        columnModel.getColumn(1).setPreferredWidth(100);  // Fecha
        columnModel.getColumn(2).setPreferredWidth(140);  // Nombres
        columnModel.getColumn(3).setPreferredWidth(140);  // Apellidos
        columnModel.getColumn(4).setPreferredWidth(250);  // Dirección 
        columnModel.getColumn(5).setPreferredWidth(110);  // Teléfono
        columnModel.getColumn(6).setPreferredWidth(140);  // Presentado Por
        columnModel.getColumn(7).setPreferredWidth(140);  // Población
        
        // Configurar el renderizador para fechas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                if (column == 1 && value instanceof Date) {
                    value = sdf.format((Date)value);
                }
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(tableFont); // Asegurar que todas las celdas usen la fuente más grande
                
                return c;
            }
        });
          // Agregar tabla a un scroll pane
        JScrollPane scrollPane = new JScrollPane(tabla);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        
        // Panel de acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
          JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.setFont(new Font("Arial", Font.BOLD, 14)); // Letra más grande
        btnImprimir.setPreferredSize(new Dimension(120, 30)); // Botón más grande
        btnImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirDatos();
            }
        });
        
        panelAcciones.add(btnImprimir);
        
        // Añadir componentes al panel principal
        add(panelTitulo, BorderLayout.NORTH);
        add(panelBusqueda, BorderLayout.PAGE_START);
        add(panelTabla, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }
    
    /**
     * Carga los datos de los socios en la tabla
     */
    private void cargarDatos() {
        // Limpiar tabla
        modelo.setRowCount(0);
        
        // Obtener datos
        List<Map<String, Object>> listaSocios = socioDAO.listarSociosInfantiles();
        
        // Llenar tabla
        if (listaSocios != null) {
            for (Map<String, Object> socio : listaSocios) {
                modelo.addRow(new Object[] {
                    socio.get("NoSocio"),
                    socio.get("Fecha"),
                    socio.get("Nombres"),
                    socio.get("Apellidos"),
                    socio.get("Direccion"),
                    socio.get("Telefono"),
                    socio.get("PresentadoPor"),
                    socio.get("Poblacion")
                });
            }
        }
        
        // Actualizar contador
        actualizarContador();
    }
    
    /**
     * Actualiza el contador de registros
     */
    private void actualizarContador() {
        int totalRegistros = tabla.getRowCount();
        int totalSinFiltro = modelo.getRowCount();
        
        if (totalRegistros != totalSinFiltro) {
            lblContador.setText("Total: " + totalRegistros + " de " + totalSinFiltro + " registros");
        } else {
            lblContador.setText("Total: " + totalRegistros + " registros");
        }
    }
    
    /**
     * Aplica un filtro a la tabla
     * @param texto Texto a buscar
     */
    private void aplicarFiltro(String texto) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        
        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
        
        actualizarContador();
    }
      /**
     * Imprime los datos de la tabla
     */
    private void imprimirDatos() {        try {
            // Obtener texto del contador para incluirlo en el encabezado
            String textoContador = lblContador.getText();
              // Crear un trabajo de impresión con encabezado que incluye el contador y mejor espaciado
            String headerText = "Listado de Socios Infantiles\n\n" + textoContador;
            MessageFormat encabezado = new MessageFormat(headerText);
            MessageFormat pie = new MessageFormat("Página {0}");
            JTable.PrintMode modo = JTable.PrintMode.FIT_WIDTH;
            
            // Configurar impresión para mejor ajuste y orientación horizontal
            HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(OrientationRequested.LANDSCAPE); // Orientación horizontal
            attributes.add(MediaSizeName.NA_LETTER); // Tamaño carta
            
            // Mejorar el manejo de página final y márgenes para la impresión
            attributes.add(new MediaPrintableArea(
                0.5f,   // Margen izquierdo en pulgadas
                0.5f,   // Margen superior en pulgadas 
                7.5f,   // Anchura imprimible en pulgadas (carta: 8.5 - márgenes)
                10.0f,  // Altura imprimible en pulgadas (carta: 11.0 - márgenes)
                MediaPrintableArea.INCH
            ));
              
            // Guardar la fuente original y escalar temporalmente para impresión
            Font originalFont = tabla.getFont();
            tabla.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 12));
            
            boolean status = tabla.print(modo, encabezado, pie, true, attributes, true);
            
            if (status) {
                JOptionPane.showMessageDialog(this, 
                        "Impresión completada con éxito", 
                        "Información", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Impresión cancelada", 
                        "Información", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, 
                    "Error de impresión: " + pe.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
