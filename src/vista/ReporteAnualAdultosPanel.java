package vista;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.border.EmptyBorder;

import conexion.Conexion;

/**
 * Panel para mostrar el reporte anual de socios adultos
 */
public class ReporteAnualAdultosPanel extends JPanel {
    
    private JTable tablaIngresos;
    private JTable tablaEgresos;
    private DefaultTableModel modeloTablaIngresos;
    private DefaultTableModel modeloTablaEgresos;
    private JComboBox<String> cboAnio;
    private JButton btnGenerar;
    private JButton btnImprimirIngresos;
    private JButton btnImprimirEgresos;
    private JButton btnVolver;
    private MenuPrincipal menuPrincipal;
    private Conexion conexion;
    private JLabel lblTotal;
    private JPanel panelTotales;
    private JPanel panelSuperior;
    private JLabel lblTitulo;
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal
     */
    public ReporteAnualAdultosPanel(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.conexion = new Conexion();
        inicializarComponentes();
    }
    
    /**
     * Inicializa los componentes del panel
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 248, 255));
        
        // Panel superior con título y selector de año
        panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.setBackground(new Color(240, 248, 255));
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        lblTitulo = new JLabel("Reporte Anual de Socios Adultos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        JPanel panelSeleccionAnio = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSeleccionAnio.setBackground(new Color(240, 248, 255));
        
        JLabel lblAnio = new JLabel("Seleccionar año: ");
        lblAnio.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Inicializar el combo de años
        cboAnio = new JComboBox<>();
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2010; i <= anioActual; i++) {
            cboAnio.addItem(String.valueOf(i));
        }
        cboAnio.setSelectedItem(String.valueOf(anioActual));
        cboAnio.setPreferredSize(new Dimension(100, 25));
        cboAnio.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });
        
        panelSeleccionAnio.add(lblAnio);
        panelSeleccionAnio.add(cboAnio);
        panelSeleccionAnio.add(btnGenerar);
        
        panelSuperior.add(panelSeleccionAnio, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con las dos tablas
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 0, 10));
        panelCentral.setBackground(new Color(240, 248, 255));
        
        // Tabla de ingresos
        JPanel panelIngresos = new JPanel(new BorderLayout(0, 5));
        panelIngresos.setBackground(new Color(240, 248, 255));
        panelIngresos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ingresos"));
        
        String[] columnasIngresos = {"Mes", "Aportaciones", "Abonos", "Intereses", "Ahorros", "DepBanco", "Total"};
        modeloTablaIngresos = new DefaultTableModel(columnasIngresos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 1) return Double.class;
                return String.class;
            }
        };
        
        tablaIngresos = new JTable(modeloTablaIngresos);
        tablaIngresos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaIngresos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaIngresos.setRowHeight(25);
        tablaIngresos.setFillsViewportHeight(true);
        
        // Renderer para formato de moneda
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
            private final DecimalFormat formatter = new DecimalFormat("$ #,##0.00");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = formatter.format(((Number) value).doubleValue());
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        moneyRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Aplicar renderer monetario a columnas numéricas
        for (int i = 1; i < tablaIngresos.getColumnCount(); i++) {
            tablaIngresos.getColumnModel().getColumn(i).setCellRenderer(moneyRenderer);
        }
        
        JScrollPane scrollIngresos = new JScrollPane(tablaIngresos);
        scrollIngresos.setBorder(BorderFactory.createEtchedBorder());
        
        // Botón para imprimir tabla de ingresos
        btnImprimirIngresos = new JButton("Imprimir Ingresos");
        btnImprimirIngresos.setFont(new Font("Arial", Font.BOLD, 14));
        btnImprimirIngresos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirTabla(tablaIngresos, "Ingresos Anuales - Socios Adultos");
            }
        });
        
        JPanel panelBtnIngresos = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnIngresos.setBackground(new Color(240, 248, 255));
        panelBtnIngresos.add(btnImprimirIngresos);
        
        panelIngresos.add(scrollIngresos, BorderLayout.CENTER);
        panelIngresos.add(panelBtnIngresos, BorderLayout.SOUTH);
        
        // Tabla de egresos
        JPanel panelEgresos = new JPanel(new BorderLayout(0, 5));
        panelEgresos.setBackground(new Color(240, 248, 255));
        panelEgresos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Egresos"));
        
        String[] columnasEgresos = {"Mes", "RetAportación", "Préstamos", "RetIntereses", "RetAhorro", "RetDeposito", "Total"};
        modeloTablaEgresos = new DefaultTableModel(columnasEgresos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 1) return Double.class;
                return String.class;
            }
        };
        
        tablaEgresos = new JTable(modeloTablaEgresos);
        tablaEgresos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaEgresos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaEgresos.setRowHeight(25);
        tablaEgresos.setFillsViewportHeight(true);
        
        // Aplicar renderer monetario a columnas numéricas
        for (int i = 1; i < tablaEgresos.getColumnCount(); i++) {
            tablaEgresos.getColumnModel().getColumn(i).setCellRenderer(moneyRenderer);
        }
        
        JScrollPane scrollEgresos = new JScrollPane(tablaEgresos);
        scrollEgresos.setBorder(BorderFactory.createEtchedBorder());
        
        // Botón para imprimir tabla de egresos
        btnImprimirEgresos = new JButton("Imprimir Egresos");
        btnImprimirEgresos.setFont(new Font("Arial", Font.BOLD, 14));
        btnImprimirEgresos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirTabla(tablaEgresos, "Egresos Anuales - Socios Adultos");
            }
        });
        
        JPanel panelBtnEgresos = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnEgresos.setBackground(new Color(240, 248, 255));
        panelBtnEgresos.add(btnImprimirEgresos);
        
        panelEgresos.add(scrollEgresos, BorderLayout.CENTER);
        panelEgresos.add(panelBtnEgresos, BorderLayout.SOUTH);
        
        panelCentral.add(panelIngresos);
        panelCentral.add(panelEgresos);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con totales y botón volver
        panelTotales = new JPanel(new BorderLayout(10, 0));
        panelTotales.setBackground(new Color(240, 248, 255));
        panelTotales.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        lblTotal = new JLabel("BALANCE ANUAL: $ 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setHorizontalAlignment(JLabel.LEFT);
        
        btnVolver = new JButton("Volver al Menú");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        JPanel panelBtnVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnVolver.setBackground(new Color(240, 248, 255));
        panelBtnVolver.add(btnVolver);
        
        panelTotales.add(lblTotal, BorderLayout.WEST);
        panelTotales.add(panelBtnVolver, BorderLayout.EAST);
        
        add(panelTotales, BorderLayout.SOUTH);
        
        // Generar reporte inicial
        generarReporte();
    }
    
    /**
     * Genera el reporte anual según el año seleccionado
     */
    private void generarReporte() {
        // Limpiar tablas
        modeloTablaIngresos.setRowCount(0);
        modeloTablaEgresos.setRowCount(0);
        
        int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
        lblTitulo.setText("Reporte Anual de Socios Adultos - " + anio);
        
        try {
            // Consulta para obtener los ingresos mensuales
            String consultaIngresos = "SELECT MONTH(Fecha) as Mes, " +
                         "SUM(AporIngresos) as AporIngresos, " +
                         "SUM(PresIngresos) as PresIngresos, " +
                         "SUM(Intereses) as Intereses, " +
                         "SUM(AhoIngresos) as AhoIngresos, " +
                         "SUM(SaldoBanco) as SaldoBanco " +
                         "FROM MovimientosSocio " +
                         "WHERE YEAR(Fecha) = ? AND TipoSocio = 'ADULTO' " +
                         "GROUP BY MONTH(Fecha) " +
                         "ORDER BY MONTH(Fecha)";
            
            PreparedStatement pstmtIngresos = conexion.getConexion().prepareStatement(consultaIngresos);
            pstmtIngresos.setInt(1, anio);
            
            ResultSet rsIngresos = pstmtIngresos.executeQuery();
            
            double totalAporIngresos = 0;
            double totalPresIngresos = 0;
            double totalIntereses = 0;
            double totalAhoIngresos = 0;
            double totalSaldoBanco = 0;
            double totalIngresos = 0;
            
            // Rellenar tabla de ingresos
            while (rsIngresos.next()) {
                int mes = rsIngresos.getInt("Mes");
                double aporIngresos = rsIngresos.getDouble("AporIngresos");
                double presIngresos = rsIngresos.getDouble("PresIngresos");
                double intereses = rsIngresos.getDouble("Intereses");
                double ahoIngresos = rsIngresos.getDouble("AhoIngresos");
                double saldoBanco = rsIngresos.getDouble("SaldoBanco");
                
                // Calcular total mensual
                double totalMensual = aporIngresos + presIngresos + intereses + ahoIngresos + saldoBanco;
                
                // Sumar a los totales anuales
                totalAporIngresos += aporIngresos;
                totalPresIngresos += presIngresos;
                totalIntereses += intereses;
                totalAhoIngresos += ahoIngresos;
                totalSaldoBanco += saldoBanco;
                totalIngresos += totalMensual;
                
                // Nombre del mes
                String nombreMes = obtenerNombreMes(mes);
                
                // Añadir fila a la tabla
                modeloTablaIngresos.addRow(new Object[]{
                    nombreMes,
                    aporIngresos,
                    presIngresos,
                    intereses,
                    ahoIngresos,
                    saldoBanco,
                    totalMensual
                });
            }
            
            rsIngresos.close();
            pstmtIngresos.close();
            
            // Añadir fila de totales a la tabla de ingresos
            modeloTablaIngresos.addRow(new Object[]{
                "TOTALES",
                totalAporIngresos,
                totalPresIngresos,
                totalIntereses,
                totalAhoIngresos,
                totalSaldoBanco,
                totalIngresos
            });
            
            // Consulta para obtener los egresos mensuales
            String consultaEgresos = "SELECT MONTH(Fecha) as Mes, " +
                         "SUM(AporEgresos) as AporEgresos, " +
                         "SUM(PresEgresos) as PresEgresos, " +
                         "SUM(RetInteres) as RetInteres, " +
                         "SUM(AhoEgresos) as AhoEgresos, " +
                         "SUM(RetBanco) as RetBanco " +
                         "FROM MovimientosSocio " +
                         "WHERE YEAR(Fecha) = ? AND TipoSocio = 'ADULTO' " +
                         "GROUP BY MONTH(Fecha) " +
                         "ORDER BY MONTH(Fecha)";
            
            PreparedStatement pstmtEgresos = conexion.getConexion().prepareStatement(consultaEgresos);
            pstmtEgresos.setInt(1, anio);
            
            ResultSet rsEgresos = pstmtEgresos.executeQuery();
            
            double totalAporEgresos = 0;
            double totalPresEgresos = 0;
            double totalRetInteres = 0;
            double totalAhoEgresos = 0;
            double totalRetBanco = 0;
            double totalEgresos = 0;
            
            // Rellenar tabla de egresos
            while (rsEgresos.next()) {
                int mes = rsEgresos.getInt("Mes");
                double aporEgresos = rsEgresos.getDouble("AporEgresos");
                double presEgresos = rsEgresos.getDouble("PresEgresos");
                double retInteres = rsEgresos.getDouble("RetInteres");
                double ahoEgresos = rsEgresos.getDouble("AhoEgresos");
                double retBanco = rsEgresos.getDouble("RetBanco");
                
                // Calcular total mensual
                double totalMensual = aporEgresos + presEgresos + retInteres + ahoEgresos + retBanco;
                
                // Sumar a los totales anuales
                totalAporEgresos += aporEgresos;
                totalPresEgresos += presEgresos;
                totalRetInteres += retInteres;
                totalAhoEgresos += ahoEgresos;
                totalRetBanco += retBanco;
                totalEgresos += totalMensual;
                
                // Nombre del mes
                String nombreMes = obtenerNombreMes(mes);
                
                // Añadir fila a la tabla
                modeloTablaEgresos.addRow(new Object[]{
                    nombreMes,
                    aporEgresos,
                    presEgresos,
                    retInteres,
                    ahoEgresos,
                    retBanco,
                    totalMensual
                });
            }
            
            rsEgresos.close();
            pstmtEgresos.close();
            
            // Añadir fila de totales a la tabla de egresos
            modeloTablaEgresos.addRow(new Object[]{
                "TOTALES",
                totalAporEgresos,
                totalPresEgresos,
                totalRetInteres,
                totalAhoEgresos,
                totalRetBanco,
                totalEgresos
            });
            
            // Calcular balance total y actualizar etiqueta
            double balanceTotal = totalIngresos - totalEgresos;
            DecimalFormat df = new DecimalFormat("$ #,##0.00");
            lblTotal.setText("BALANCE ANUAL: " + df.format(balanceTotal));
            
            // Colorear según balance
            if (balanceTotal >= 0) {
                lblTotal.setForeground(new Color(0, 128, 0)); // Verde
            } else {
                lblTotal.setForeground(new Color(255, 0, 0)); // Rojo
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Error al generar el reporte: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Imprime una tabla específica
     * @param tabla Tabla a imprimir
     * @param titulo Título para el reporte impreso
     */
    private void imprimirTabla(JTable tabla, String titulo) {
        try {
            // Mensaje de impresión en proceso
            JOptionPane.showMessageDialog(this,
                "Preparando impresión...",
                "Impresión", JOptionPane.INFORMATION_MESSAGE);
            
            int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
            
            // Crear un trabajo de impresión
            MessageFormat header = new MessageFormat(titulo + " - " + anio);
            MessageFormat footer = new MessageFormat("Página {0}");
            
            // Intentar imprimir la tabla
            boolean complete = tabla.print(
                JTable.PrintMode.FIT_WIDTH,
                header,
                footer,
                true,
                null,
                true,
                null);
                
            // Mensaje según resultado
            if (complete) {
                JOptionPane.showMessageDialog(this,
                    "Impresión completada correctamente",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Impresión cancelada o incompleta",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this,
                "Error de impresión: " + pe.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            pe.printStackTrace();
        }
    }
    
    /**
     * Obtiene el nombre del mes a partir de su número
     * @param numeroMes Número del mes (1-12)
     * @return Nombre del mes
     */
    private String obtenerNombreMes(int numeroMes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[numeroMes - 1];
    }
}
