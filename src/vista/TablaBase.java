package vista;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TablaBase
 * Panel base para mostrar tablas de datos
 */
public abstract class TablaBase extends JPanel {
    
    protected JTable tabla;
    protected DefaultTableModel modelo;
    protected JScrollPane scrollPane;
    protected JTextField txtBuscar;
    protected JButton btnBuscar;
    protected JButton btnRefrescar;
    protected JButton btnExportar;
    protected JButton btnImprimir;
    protected JLabel lblContador;
    
    public TablaBase(String[] columnas) {
        setLayout(new BorderLayout());
        
        // Panel superior con controles de búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Campo de búsqueda
        JLabel lblBuscar = new JLabel("Buscar: ");
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnRefrescar = new JButton("Actualizar");
        btnExportar = new JButton("Exportar");
        btnImprimir = new JButton("Imprimir");
        lblContador = new JLabel("Total: 0 registros");
        
        panelSuperior.add(lblBuscar);
        panelSuperior.add(txtBuscar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnRefrescar);
        panelSuperior.add(btnExportar);
        panelSuperior.add(btnImprimir);
        
        // Panel para el contador (a la derecha)
        JPanel panelContador = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelContador.add(lblContador);
        
        // Panel que combina superior y contador
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(panelSuperior, BorderLayout.WEST);
        panelTop.add(panelContador, BorderLayout.EAST);
        
        // Crear modelo de tabla
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
        scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Añadir componentes al panel
        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Configurar listeners
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar();
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
                    buscar();
                }
            }
        });
    }
    
    /**
     * Carga los datos en la tabla
     */
    public abstract void cargarDatos();
    
    /**
     * Realiza la búsqueda de datos según el texto ingresado
     */
    protected abstract void buscar();
    
    /**
     * Actualiza el contador de registros
     */
    protected void actualizarContador() {
        int totalRegistros = tabla.getRowCount();
        int totalSinFiltro = modelo.getRowCount();
        if (totalRegistros != totalSinFiltro) {
            lblContador.setText("Total: " + totalRegistros + " de " + totalSinFiltro + " registros");
        } else {
            lblContador.setText("Total: " + totalRegistros + " registros");
        }
    }
      /**
     * Imprime los datos de la tabla
     */
    protected void imprimirDatos() {
        try {
            MessageFormat encabezado = new MessageFormat("Listado de Socios");
            MessageFormat pie = new MessageFormat("Página {0}");
            
            boolean mostrarDialogo = true;
            boolean interactivo = true;
            
            JTable.PrintMode modo = JTable.PrintMode.FIT_WIDTH;
            
            // Configurar atributos de impresión para mejor ajuste y orientación horizontal
            HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(OrientationRequested.LANDSCAPE); // Orientación horizontal
            attributes.add(MediaSizeName.NA_LETTER); // Tamaño carta
              // Guardar la fuente original y escalar temporalmente para impresión
            Font originalFont = tabla.getFont();
            tabla.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 12));
            
            // Intentar imprimir la tabla con atributos personalizados
            boolean status = tabla.print(modo, encabezado, pie, mostrarDialogo, attributes, interactivo);
            
            // Restaurar la fuente original después de imprimir
            tabla.setFont(originalFont);
            
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
    
    /**
     * Exporta los datos de la tabla
     */
    protected void exportarDatos() {
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
     * Aplica un filtro a la tabla basado en el texto proporcionado
     */
    protected void aplicarFiltro(String texto) {
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
        
        // Actualizar el contador con los resultados filtrados
        actualizarContador();
    }
}
