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
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

import conexion.Conexion;

/**
 * Panel para mostrar el reporte            // Mejorar el manejo de página final y márgenes para la impresión
            attributes.add(new MediaPrintableArea(
                0.5f,   // Margen izquierdo en pulgadas
                0.5f,   // Margen superior en pulgadas 
                7.5f,   // Anchura imprimible en pulgadas (carta: 8.5 - márgenes)
                10.0f,  // Altura imprimible en pulgadas (carta: 11.0 - márgenes)
                MediaPrintableArea.INCH
            ));
            
            // Intentar imprimir la tabla con atributos personalizados
            boolean complete = tablaReporte.print(
                JTable.PrintMode.FIT_WIDTH,      // Modo de impresión para ajustar al ancho de página
                header,                          // Encabezado con totales
                footer,                          // Pie de página
                true,                            // Mostrar diálogo de impresión
                attributes,                      // Atributos de impresión personalizados
                true,                            // Interactivo - muestra diálogos de error si los hay
                null);                           // Servicio de impresión (usar predeterminado)                
            // Restaurar la fuente original después de imprimir
            tablaReporte.setFont(originalFont);socios adultos
 */
public class ReporteDiarioAdultosPanel extends JPanel {
      private JTable tablaReporte;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cboAnio;
    private JComboBox<String> cboMes;
    private JComboBox<String> cboDia;
    private JButton btnGenerar;
    private JButton btnVolver;
    private JButton btnImprimir;
    private MenuPrincipal menuPrincipal;
    private Conexion conexion;
    private JPanel panelTotales;    private JLabel lblTotalIngresos;
    private JLabel lblTotalEgresos;
    private JLabel lblTotal;
    private Calendar calendario;
    private JPanel panelSuperior;
    private JLabel lblTitulo;
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal
     */
    public ReporteDiarioAdultosPanel(MenuPrincipal menuPrincipal) {
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
        
        lblTitulo = new JLabel("Reporte Diario de Socios Adultos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
          JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFecha.setBackground(new Color(240, 248, 255));
        
        JLabel lblFecha = new JLabel("Seleccionar fecha: ");
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Inicializar el calendario con la fecha actual
        calendario = Calendar.getInstance();
        int anioActual = calendario.get(Calendar.YEAR);
        int mesActual = calendario.get(Calendar.MONTH);
        int diaActual = calendario.get(Calendar.DAY_OF_MONTH);
        
        // Crear los combos para seleccionar fecha
        // Combo para día
        cboDia = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            cboDia.addItem(String.format("%02d", i));
        }
        cboDia.setSelectedItem(String.format("%02d", diaActual));
        cboDia.setPreferredSize(new Dimension(60, 25));
        cboDia.setFont(new Font("Arial", Font.PLAIN, 14));
        
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
        
        // Evento de cambio de mes para ajustar días disponibles
        cboMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDiasDisponibles();
            }
        });
          // Combo para año
        cboAnio = new JComboBox<>();
        // Rango de años fijo de 2010 a 2050
        for (int i = 2010; i <= 2050; i++) {
            cboAnio.addItem(String.valueOf(i));
        }
        cboAnio.setSelectedItem(String.valueOf(anioActual));
        cboAnio.setPreferredSize(new Dimension(70, 25));
        cboAnio.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Evento de cambio de año para ajustar días disponibles
        cboAnio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDiasDisponibles();
            }
        });
        
        btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });
        
        panelFecha.add(lblFecha);
        panelFecha.add(cboDia);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(cboMes);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(cboAnio);
        panelFecha.add(btnGenerar);
        
        panelSuperior.add(panelFecha, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Crear el modelo y la tabla
        String[] columnas = {"No. Socio", "Nombre", "Apellido(s)", 
                            "Aportaciones", "Abonos", "Intereses", "Ahorros",
                            "R.Apor", "Préstamos", "R.Intereses", "R.Ahorro"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3) return Double.class; // Columnas numéricas
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
        for (int i = 3; i < tablaReporte.getColumnCount(); i++) {
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(moneyRenderer);
        }
          // Ajustar anchos de columna para mejor distribución en la impresión
        tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(50); // No. Socio
        tablaReporte.getColumnModel().getColumn(1).setPreferredWidth(130); // Nombre
        tablaReporte.getColumnModel().getColumn(2).setPreferredWidth(150); // Apellido(s)
        // Ajustar columnas numéricas a un ancho más uniforme
        for (int i = 3; i < tablaReporte.getColumnCount(); i++) {
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
     * Genera el reporte diario según la fecha seleccionada
     */    
    private void generarReporte() {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        // Obtener la fecha seleccionada de los combos
        int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
        int mes = cboMes.getSelectedIndex();
        int dia = Integer.parseInt(cboDia.getSelectedItem().toString());
        
        // Crear un objeto Calendar con la fecha seleccionada
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes, dia);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.util.Date fechaSeleccionada = cal.getTime();
        
        try {
            // Consulta SQL para obtener los movimientos de socios adultos en la fecha seleccionada
            String consulta = "SELECT ms.NoSocio, s.Nombres, s.Apellidos, " +
                         "ms.AporIngresos, ms.PresIngresos, ms.Intereses, ms.AhoIngresos, " +
                         "ms.AporEgresos, ms.PresEgresos, ms.RetInteres, ms.AhoEgresos " +
                         "FROM MovimientosSocio ms " +
                         "LEFT JOIN Socios s ON ms.NoSocio = s.NoSocio " +
                         "WHERE ms.Fecha = ? AND (ms.TipoSocio = 'ADULTO') " +
                         "ORDER BY ms.NoSocio";
            
            PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
            // Convertir a java.sql.Date para utilizar con la base de datos
            java.sql.Date sqlDate = new java.sql.Date(fechaSeleccionada.getTime());
            pstmt.setDate(1, sqlDate);
            
            ResultSet rs = pstmt.executeQuery();
            
            double totalAporIngresos = 0;
            double totalPresIngresos = 0;
            double totalIntereses = 0;
            double totalAhoIngresos = 0;
            double totalAporEgresos = 0;
            double totalPresEgresos = 0;
            double totalRetInteres = 0;
            double totalAhoEgresos = 0;
            
            // Agregar filas a la tabla
            while (rs.next()) {
                int noSocio = rs.getInt("NoSocio");
                String nombre = rs.getString("Nombres");
                String apellidos = rs.getString("Apellidos");
                double aporIngresos = rs.getDouble("AporIngresos");
                double presIngresos = rs.getDouble("PresIngresos");
                double intereses = rs.getDouble("Intereses");
                double ahoIngresos = rs.getDouble("AhoIngresos");
                double aporEgresos = rs.getDouble("AporEgresos");
                double presEgresos = rs.getDouble("PresEgresos");
                double retInteres = rs.getDouble("RetInteres");
                double ahoEgresos = rs.getDouble("AhoEgresos");
                
                // Sumar a los totales
                totalAporIngresos += aporIngresos;
                totalPresIngresos += presIngresos;
                totalIntereses += intereses;
                totalAhoIngresos += ahoIngresos;
                totalAporEgresos += aporEgresos;
                totalPresEgresos += presEgresos;
                totalRetInteres += retInteres;
                totalAhoEgresos += ahoEgresos;
                
                // Agregar fila a la tabla
                modeloTabla.addRow(new Object[]{
                    noSocio,
                    nombre != null ? nombre : "",
                    apellidos != null ? apellidos : "",
                    aporIngresos,
                    presIngresos,
                    intereses,
                    ahoIngresos,
                    aporEgresos,
                    presEgresos,
                    retInteres,
                    ahoEgresos
                });
            }
            
            rs.close();
            pstmt.close();
            
            // Agregar fila de totales
            modeloTabla.addRow(new Object[]{
                "TOTALES",
                "",
                "",
                totalAporIngresos,
                totalPresIngresos,
                totalIntereses,
                totalAhoIngresos,
                totalAporEgresos,
                totalPresEgresos,
                totalRetInteres,
                totalAhoEgresos
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
            
            // Mostrar la fecha seleccionada en el título
            String fechaFormateada = String.format("%02d/%02d/%04d", dia, mes + 1, anio);
            lblTitulo.setText("Reporte Diario de Socios Adultos - " + fechaFormateada);
            
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
     * Actualiza los días disponibles según el mes y año seleccionados
     */    private void actualizarDiasDisponibles() {
        int mes = cboMes.getSelectedIndex();
        int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
        
        // Guardar el índice seleccionado actual
        int indiceSeleccionado = cboDia.getSelectedIndex();
        
        // Limpiar los días
        cboDia.removeAllItems();
        
        // Determinar la cantidad de días para el mes y año seleccionados
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes, 1);
        int diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Añadir los días al combo
        for (int i = 1; i <= diasEnMes; i++) {
            cboDia.addItem(String.format("%02d", i));
        }
        
        // Restaurar la selección si es posible
        if (indiceSeleccionado < diasEnMes) {
            cboDia.setSelectedIndex(indiceSeleccionado);
        } else {
            // Si el día seleccionado antes no existe en el nuevo mes, seleccionar el último día
            cboDia.setSelectedIndex(diasEnMes - 1);
        }
    }
    
    /**
     * Imprime la tabla de reporte
     */    private void imprimirReporte() {
        try {
            // Mensaje de impresión en proceso
            JOptionPane.showMessageDialog(this,
                "Preparando impresión...",
                "Impresión", JOptionPane.INFORMATION_MESSAGE);
            
            // Obtener la fecha seleccionada para mostrar en el título
            int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
            int mes = cboMes.getSelectedIndex();
            int dia = Integer.parseInt(cboDia.getSelectedItem().toString());
            String fechaFormateada = String.format("%02d/%02d/%04d", dia, mes + 1, anio);            // Obtener texto de los totales para incluirlos en el encabezado
            String textoIngresos = lblTotalIngresos.getText();
            String textoEgresos = lblTotalEgresos.getText();
            String textoTotal = lblTotal.getText();
            
            // Crear un trabajo de impresión con encabezado que incluye totales y mejor espaciado
            String headerText = "Reporte Diario de Socios Adultos - " + fechaFormateada + 
                                "\n\n" + textoIngresos + "        " + textoEgresos + "        " + textoTotal;
            MessageFormat header = new MessageFormat(headerText);
            MessageFormat footer = new MessageFormat("Página {0}");
            
            // Configurar impresión para mejor ajuste y orientación horizontal
            HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(OrientationRequested.LANDSCAPE); // Orientación horizontal
            attributes.add(MediaSizeName.NA_LETTER); // Tamaño carta
              // Guardar la fuente original y escalar temporalmente para impresión
            Font originalFont = tablaReporte.getFont();
            tablaReporte.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 12));
            
            // Intentar imprimir la tabla con atributos personalizados
            boolean complete = tablaReporte.print(
                JTable.PrintMode.FIT_WIDTH,      // Modo de impresión para ajustar al ancho de página
                header,                          // Encabezado con totales
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
