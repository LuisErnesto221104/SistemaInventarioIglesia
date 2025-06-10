package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import dao.SocioDAO;

/**
 * ListadoSociosAdultosPanelIntegrado
 * Panel integrado para mostrar el listado de socios adultos
 */
public class ListadoSociosAdultosPanelIntegrado extends JPanel {
    
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
    public ListadoSociosAdultosPanelIntegrado(MenuPrincipal menuPrincipal) {
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
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        JLabel lblTitulo = new JLabel("LISTADO DE SOCIOS ADULTOS", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        panelTitulo.add(btnVolver, BorderLayout.WEST);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 10));
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JPanel panelBuscar = new JPanel(new BorderLayout(5, 0));
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField();
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
        String[] columnas = {"N° Socio", "Nombres", "Apellidos", "Dirección", "Teléfono", "Fecha Registro", "Presentado Por", "Población"};
        modelo.setColumnIdentifiers(columnas);
        
        // Crear tabla
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getTableHeader().setReorderingAllowed(false);
        
        // Configurar el renderizador para fechas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                if (column == 5 && value instanceof Date) {
                    value = sdf.format((Date)value);
                }
                
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        
        // Agregar tabla a un scroll pane
        JScrollPane scrollPane = new JScrollPane(tabla);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnImprimir = new JButton("Imprimir");
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
        List<Map<String, Object>> listaSocios = socioDAO.listarSocios();
        
        // Llenar tabla
        if (listaSocios != null) {
            for (Map<String, Object> socio : listaSocios) {
                modelo.addRow(new Object[] {
                    socio.get("NoSocio"),
                    socio.get("Nombres"),
                    socio.get("Apellidos"),
                    socio.get("Direccion"),
                    socio.get("Telefono"),
                    socio.get("FechaRegistro"),
                    socio.get("PrestadoPor"),
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
            String headerText = "Listado de Socios Adultos\n\n" + textoContador;
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
              // Restaurar la fuente original después de imprimir
            tabla.setFont(originalFont);
            
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
