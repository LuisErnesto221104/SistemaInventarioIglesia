package vista;

import dao.SocioDAO;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.*;

/**
 * ListadoSociosInfantiles
 * Ventana que muestra el listado de socios infantiles
 */
public class ListadoSociosInfantiles extends JFrame {
    
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private SocioDAO socioDAO;
    private JLabel lblContador;
    
    public ListadoSociosInfantiles() {
        socioDAO = new SocioDAO();
        inicializarComponentes();
        cargarDatos();
    }
    
    private void inicializarComponentes() {
        setTitle("Listado de Socios Infantiles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior con controles de búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
          // Campo de búsqueda
        JLabel lblBuscar = new JLabel("Buscar: ");
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRefrescar = new JButton("Actualizar");
        JButton btnExportar = new JButton("Exportar a CSV");
        JButton btnImprimir = new JButton("Imprimir");
        lblContador = new JLabel("Total: 0 registros");
        
        panelSuperior.add(lblBuscar);
        panelSuperior.add(txtBuscar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnRefrescar);
        panelSuperior.add(btnExportar);
        panelSuperior.add(btnImprimir);
        
        // Crear modelo de tabla
        String[] columnas = {"No. Socio", "Nombres", "Apellidos", "Dirección", 
            "Teléfono", "Fecha Registro", "Presentado Por", "Población"};
            
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición
            }
        };
        
        // Añadir columnas
        for (String columna : columnas) {
            modelo.addColumn(columna);
        }
        
        // Crear tabla
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getTableHeader().setReorderingAllowed(false);
        
        // Configurar renderizador para fechas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                if (value instanceof Date) {
                    value = sdf.format((Date) value);
                }
                
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
          // Configurar scroll pane
        JScrollPane scrollPane = new JScrollPane(tabla);
        
        // Panel para el contador (a la derecha)
        JPanel panelContador = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelContador.add(lblContador);
        
        // Panel que combina superior y contador
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(panelSuperior, BorderLayout.WEST);
        panelTop.add(panelContador, BorderLayout.EAST);
        
        // Añadir componentes al panel principal
        panelPrincipal.add(panelTop, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        // Añadir panel principal al formulario
        add(panelPrincipal);
        
        // Configurar acciones
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltro(txtBuscar.getText());
            }
        });
        
        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatos();
            }
        });
          btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarDatos();
            }
        });
        
        btnImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirDatos();
            }
        });
        
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    aplicarFiltro(txtBuscar.getText());
                }
            }
        });
        
        // Mostrar formulario
        setVisible(true);
    }
      private void cargarDatos() {        // Limpiar modelo
        modelo.setRowCount(0);
        
        // Obtener listado de socios infantiles
        java.util.List<Map<String, Object>> listaSocios = socioDAO.listarSociosInfantiles();
        
        // Declarar variable para referencia al Label contador
        
        // Buscar el JLabel contador en los componentes
        Component[] components = ((JPanel)((JPanel)getContentPane().getComponent(0)).getComponent(0)).getComponents();
        for(Component component : components) {
            if(component instanceof JPanel && ((JPanel)component).getComponentCount() > 0) {
                Component innerComponent = ((JPanel)component).getComponent(0);
                if(innerComponent instanceof JLabel && ((JLabel)innerComponent).getText().startsWith("Total:")) {
                    lblContador = (JLabel)innerComponent;
                    break;
                }
            }
        }
        
        // Llenar tabla
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
          // Restablecer búsqueda
        txtBuscar.setText("");
        aplicarFiltro("");
        
        // Actualizar el contador
        actualizarContador();
    }
    
    private void aplicarFiltro(String texto) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabla.getModel());
        tabla.setRowSorter(sorter);
        
        if (texto.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            } catch (java.util.regex.PatternSyntaxException ex) {
                // Si el patrón de regex es inválido, no aplicar filtro
                sorter.setRowFilter(null);
            }
        }
        
        // Actualizar el contador de registros
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
    
    private void exportarDatos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                
                // Si no tiene extensión, añadir .csv
                String fileName = fileToSave.getAbsolutePath();
                if (!fileName.toLowerCase().endsWith(".csv")) {
                    fileName += ".csv";
                    fileToSave = new java.io.File(fileName);
                }
                
                java.io.FileWriter fw = new java.io.FileWriter(fileToSave);
                
                // Escribir encabezados
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    fw.append(modelo.getColumnName(i));
                    if (i < modelo.getColumnCount() - 1) {
                        fw.append(",");
                    }
                }
                fw.append("\n");
                
                // Escribir datos
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        Object valor = modelo.getValueAt(i, j);
                        if (valor != null) {
                            fw.append(valor.toString().replace(",", ";"));
                        }
                        
                        if (j < modelo.getColumnCount() - 1) {
                            fw.append(",");
                        }
                    }
                    fw.append("\n");
                }
                
                fw.flush();
                fw.close();
                
                JOptionPane.showMessageDialog(this, 
                    "Datos exportados exitosamente a: " + fileName, 
                    "Exportación Exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al exportar datos: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
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
                new ListadoSociosInfantiles();
            }
        });
    }
    
    /**
     * Imprime los datos de la tabla
     */
    private void imprimirDatos() {
        try {
            MessageFormat encabezado = new MessageFormat("Listado de Socios Infantiles");
            MessageFormat pie = new MessageFormat("Página {0}");
            
            boolean mostrarDialogo = true;
            boolean interactivo = true;
            
            JTable.PrintMode modo = JTable.PrintMode.FIT_WIDTH;
            
            boolean status = tabla.print(modo, encabezado, pie, mostrarDialogo, null, interactivo);
            
            if (status) {
                JOptionPane.showMessageDialog(this, 
                    "Impresión enviada con éxito", 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "La impresión fue cancelada", 
                    "Impresión Cancelada", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, 
                "Error de impresión: " + pe.getMessage(), 
                "Error de Impresión", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
