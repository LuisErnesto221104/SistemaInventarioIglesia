package view;

import javax.swing.*;
import javax.swing.table.*;

import connection.Conexion;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.sql.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.border.EmptyBorder;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

/**
 * Panel para mostrar el reporte mensual de socios infantiles
 */
public class ReporteMensualInfantesPanel extends JPanel {
    
    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cboAnio;
    private JComboBox<String> cboMes;
    private JButton btnGenerar;
    private JButton btnVolver;
    private JButton btnImprimir;
    private MenuPrincipal menuPrincipal;
    private Conexion conexion;
    private JPanel panelTotales;
    private JLabel lblTotalIngresos;
    private JLabel lblTotalEgresos;
    private JLabel lblTotal;
    private Calendar calendario;
    private JPanel panelSuperior;
    private JLabel lblTitulo;
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal
     */
    public ReporteMensualInfantesPanel(MenuPrincipal menuPrincipal) {
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
        
        // Panel superior con título y fecha
        panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.setBackground(new Color(240, 248, 255));
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        lblTitulo = new JLabel("Reporte Mensual de Socios Infantiles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFecha.setBackground(new Color(240, 248, 255));
        
        JLabel lblFecha = new JLabel("Seleccionar mes: ");
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Inicializar el calendario con la fecha actual
        calendario = Calendar.getInstance();
        int anioActual = calendario.get(Calendar.YEAR);
        int mesActual = calendario.get(Calendar.MONTH);
        
        // Combo para mes
        cboMes = new JComboBox<>();
        String[] meses = {"01 - Enero", "02 - Febrero", "03 - Marzo", "04 - Abril",
                          "05 - Mayo", "06 - Junio", "07 - Julio", "08 - Agosto",
                          "09 - Septiembre", "10 - Octubre", "11 - Noviembre", "12 - Diciembre"};
        for (String mes : meses) {
            cboMes.addItem(mes);
        }
        cboMes.setSelectedIndex(mesActual);
        cboMes.setPreferredSize(new Dimension(130, 25));
        cboMes.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Combo para año
        cboAnio = new JComboBox<>();
        // Rango de años fijo de 2010 a 2050
        for (int i = 2010; i <= 2050; i++) {
            cboAnio.addItem(String.valueOf(i));
        }
        cboAnio.setSelectedItem(String.valueOf(anioActual));
        cboAnio.setPreferredSize(new Dimension(70, 25));
        cboAnio.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });
        
        panelFecha.add(lblFecha);
        panelFecha.add(cboMes);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(cboAnio);
        panelFecha.add(btnGenerar);
        
        panelSuperior.add(panelFecha, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Crear el modelo y la tabla
        String[] columnas = {"Fecha", "Aportaciones", "Abonos", "Intereses", "Ahorros",
                           "DepBanco", "RetAportación", "Préstamos", "RetIntereses", "RetAhorro", "RetDeposito"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 1) return Double.class; // Columnas numéricas
                return String.class;
            }
        };
        
        tablaReporte = new JTable(modeloTabla);
        tablaReporte.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaReporte.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaReporte.setRowHeight(25);
        tablaReporte.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaReporte.setFillsViewportHeight(true);
        
        // Configurar renderer para los valores monetarios
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
        
        // Aplicar renderer a columnas monetarias
        for (int i = 1; i < tablaReporte.getColumnCount(); i++) {
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(moneyRenderer);
        }
          // Ajustar anchos de columna para mejor distribución en la impresión
        tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(70); // Fecha
        // Ajustar columnas numéricas a un ancho más uniforme
        for (int i = 1; i < tablaReporte.getColumnCount(); i++) {
            tablaReporte.getColumnModel().getColumn(i).setPreferredWidth(80);
        }
        
        // Panel de desplazamiento para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaReporte);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel para mostrar totales
        panelTotales = new JPanel(new GridLayout(2, 2, 10, 5));
        panelTotales.setBackground(new Color(240, 248, 255));
        panelTotales.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 0, 0),
            BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Totales")
        ));
        
        lblTotalIngresos = new JLabel("Total Ingresos: $ 0.00");
        lblTotalIngresos.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblTotalEgresos = new JLabel("Total Egresos: $ 0.00");
        lblTotalEgresos.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblTotal = new JLabel("TOTAL: $ 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(240, 248, 255));
        
        btnImprimir = new JButton("Imprimir Reporte");
        btnImprimir.setFont(new Font("Arial", Font.BOLD, 14));
        btnImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirReporte();
            }
        });
        
        btnVolver = new JButton("Volver al Menú");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        panelBotones.add(btnImprimir);
        panelBotones.add(btnVolver);
        
        panelTotales.add(lblTotalIngresos);
        panelTotales.add(lblTotalEgresos);
        panelTotales.add(lblTotal);
        panelTotales.add(panelBotones);
        
        add(panelTotales, BorderLayout.SOUTH);
        
        // Generar reporte inicial
        generarReporte();
    }
    
    /**
     * Genera el reporte mensual según la fecha seleccionada
     */
    private void generarReporte() {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        // Obtener el mes y año seleccionados
        int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
        int mes = cboMes.getSelectedIndex() + 1; // +1 porque en SQL los meses van de 1 a 12
        
        try {
            // Consulta SQL para obtener los movimientos del mes para socios infantiles
            String consulta = "SELECT ms.Fecha, " +
                         "SUM(ms.AporIngresos) as AporIngresos, " +
                         "SUM(ms.PresIngresos) as PresIngresos, " +
                         "SUM(ms.Intereses) as Intereses, " +
                         "SUM(ms.AhoIngresos) as AhoIngresos, " +
                         "SUM(ms.SaldoBanco) as SaldoBanco, " +
                         "SUM(ms.AporEgresos) as AporEgresos, " +
                         "SUM(ms.PresEgresos) as PresEgresos, " +
                         "SUM(ms.RetInteres) as RetInteres, " +
                         "SUM(ms.AhoEgresos) as AhoEgresos, " +
                         "SUM(ms.RetBanco) as RetBanco " +
                         "FROM MovimientosSocio ms " +
                         "WHERE YEAR(ms.Fecha) = ? AND MONTH(ms.Fecha) = ? " +
                         "AND (ms.TipoSocio = 'INFANTIL' OR ms.TipoSocio = 'INFANTE') " +
                         "GROUP BY ms.Fecha " +
                         "ORDER BY ms.Fecha";
            
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            pstmt.setInt(1, anio);
            pstmt.setInt(2, mes);
            
            ResultSet rs = pstmt.executeQuery();
            
            double totalAporIngresos = 0;
            double totalPresIngresos = 0;
            double totalIntereses = 0;
            double totalAhoIngresos = 0;
            double totalSaldoBanco = 0;
            double totalAporEgresos = 0;
            double totalPresEgresos = 0;
            double totalRetInteres = 0;
            double totalAhoEgresos = 0;
            double totalRetBanco = 0;
            
            // Agregar filas a la tabla
            while (rs.next()) {
                Date fecha = rs.getDate("Fecha");
                double aporIngresos = rs.getDouble("AporIngresos");
                double presIngresos = rs.getDouble("PresIngresos");
                double intereses = rs.getDouble("Intereses");
                double ahoIngresos = rs.getDouble("AhoIngresos");
                double saldoBanco = rs.getDouble("SaldoBanco");
                double aporEgresos = rs.getDouble("AporEgresos");
                double presEgresos = rs.getDouble("PresEgresos");
                double retInteres = rs.getDouble("RetInteres");
                double ahoEgresos = rs.getDouble("AhoEgresos");
                double retBanco = rs.getDouble("RetBanco");
                
                // Sumar a los totales
                totalAporIngresos += aporIngresos;
                totalPresIngresos += presIngresos;
                totalIntereses += intereses;
                totalAhoIngresos += ahoIngresos;
                totalSaldoBanco += saldoBanco;
                totalAporEgresos += aporEgresos;
                totalPresEgresos += presEgresos;
                totalRetInteres += retInteres;
                totalAhoEgresos += ahoEgresos;
                totalRetBanco += retBanco;
                
                // Formatear la fecha como dd/MM/yyyy
                String fechaFormateada = new java.text.SimpleDateFormat("dd/MM/yyyy").format(fecha);
                
                // Agregar fila a la tabla
                modeloTabla.addRow(new Object[]{
                    fechaFormateada,
                    aporIngresos,
                    presIngresos,
                    intereses,
                    ahoIngresos,
                    saldoBanco,
                    aporEgresos,
                    presEgresos,
                    retInteres,
                    ahoEgresos,
                    retBanco
                });
            }
            
            rs.close();
            pstmt.close();
            
            // Agregar fila de totales
            modeloTabla.addRow(new Object[]{
                "TOTALES",
                totalAporIngresos,
                totalPresIngresos,
                totalIntereses,
                totalAhoIngresos,
                totalSaldoBanco,
                totalAporEgresos,
                totalPresEgresos,
                totalRetInteres,
                totalAhoEgresos,
                totalRetBanco
            });
            
            // Calcular totales de ingresos y egresos
            double totalIngresos = totalAporIngresos + totalPresIngresos + totalIntereses + totalAhoIngresos;
            double totalEgresos = totalAporEgresos + totalPresEgresos + totalRetInteres + totalAhoEgresos;
            double total = totalIngresos - totalEgresos;
            
            // Actualizar las etiquetas de totales
            DecimalFormat df = new DecimalFormat("$ #,##0.00");
            lblTotalIngresos.setText("Total Ingresos: " + df.format(totalIngresos));
            lblTotalEgresos.setText("Total Egresos: " + df.format(totalEgresos));
            lblTotal.setText("TOTAL: " + df.format(total));
            
            // Mostrar el mes y año seleccionados en el título
            String mesTexto = cboMes.getSelectedItem().toString().substring(5); // Obtener la parte del nombre del mes
            lblTitulo.setText("Reporte Mensual de Socios Infantiles - " + mesTexto + " " + anio);
            
            // Cambiar color del total según sea positivo o negativo
            if (total >= 0) {
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
     * Imprime la tabla de reporte
     */
    private void imprimirReporte() {
        try {
            // Mensaje de impresión en proceso
            JOptionPane.showMessageDialog(this,
                "Preparando impresión...",
                "Impresión", JOptionPane.INFORMATION_MESSAGE);
            
            // Obtener el mes y año seleccionados para mostrar en el título
            int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
            String mesTexto = cboMes.getSelectedItem().toString().substring(5); // Obtener la parte del nombre del mes
              // Obtener texto de los totales para incluirlos en el encabezado
            String textoIngresos = lblTotalIngresos.getText();
            String textoEgresos = lblTotalEgresos.getText();
            String textoTotal = lblTotal.getText();
              // Crear un trabajo de impresión con encabezado que incluye totales y mejor espaciado
            String headerText = "Reporte Mensual de Socios Infantiles - " + mesTexto + " " + anio + 
                                "\n\n" + textoIngresos + "        " + textoEgresos + "        " + textoTotal;
            MessageFormat header = new MessageFormat(headerText);
            MessageFormat footer = new MessageFormat("Página {0}");
            
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
            Font originalFont = tablaReporte.getFont();
            tablaReporte.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 12));
            
            // Intentar imprimir la tabla con atributos personalizados
            boolean complete = tablaReporte.print(
                JTable.PrintMode.FIT_WIDTH,      // Modo de impresión para ajustar al ancho de página
                header,                          // Encabezado
                footer,                          // Pie de página
                true,                            // Mostrar diálogo de impresión
                attributes,                      // Atributos de impresión personalizados
                true,                            // Interactivo - muestra diálogos de error si los hay
                null);                           // Servicio de impresión (usar predeterminado)
                
            // Restaurar la fuente original después de imprimir
            tablaReporte.setFont(originalFont);
            
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
}
