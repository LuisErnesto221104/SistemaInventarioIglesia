package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import tools.SocioDAO;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.text.SimpleDateFormat;

import javax.swing.border.TitledBorder;

/**
 * Panel para el movimiento de socios (depósitos, retiros, y pagos)
 * Soporta tanto socios adultos como infantiles
 */
public class MovimientoSocioPanelIntegradoMejorado extends JPanel {
    
    // Referencia al menú principal
    private MenuPrincipal menuPrincipal;
    
    // Data Access Object para Socios
    private SocioDAO socioDAO;
    
    // Componentes principales
    private JPanel panelTipoSocio;
    private JPanel panelBusquedaSocio;
    private JPanel panelInfoSocio;
    private JPanel panelMovimientos;
    private JPanel panelAuxiliarCobro;
    private JPanel panelBotones;
    
    // Componentes para selección de tipo de socio
    private JRadioButton radioAdulto;
    private JRadioButton radioInfantil;
    private ButtonGroup grupoTipoSocio;
    
    // Componentes para búsqueda de socio
    private JTextField txtIdSocio;
    private JButton btnBuscar;
      // Componentes para información del socio
    private JLabel lblNombre;
    private JLabel lblApellidos;
    private JLabel lblSaldoAportaciones;
    private JLabel lblSaldoPrestamos;
    private JLabel lblSaldoAhorros;
    
    // Componentes para movimientos
    private JTable tablaMovimientos;
    private DefaultTableModel modeloTabla;
    
    // Paneles para diferentes secciones de movimientos
    private JPanel panelAportaciones;
    private JPanel panelPrestaciones;
    private JPanel panelAhorros;
    private JPanel panelIntereses;
    
    // Campos para aportaciones
    private JFormattedTextField txtAportacionDeposito;
    private JFormattedTextField txtAportacionRetiro;
    
    // Campos para prestaciones
    private JFormattedTextField txtPrestamoDeposito;
    private JFormattedTextField txtPrestamoRetiro;
    
    // Campos para ahorros
    private JFormattedTextField txtAhorroDeposito;
    private JFormattedTextField txtAhorroRetiro;
      // Campos para intereses de deuda
    private JFormattedTextField txtInteresDeuda;
    private JLabel lblInteresCalculado;
    private JComboBox<Integer> cboSemanasInteres;
    
    // Campos para auxiliar de cobro
    private JFormattedTextField txtTotalMovimiento;
    private JFormattedTextField txtCantidadRecibida;
    private JLabel lblCambio;
    
    // Campo para mostrar saldo actual de ahorros en el panel de ahorros
    private JTextField txtSaldo;
    
    // Botones principales
    private JButton btnAplicar;
    private JButton btnLimpiar;
    private JButton btnCerrar;
    
    // Variables de control
    private boolean esSocioAdulto = true;
    private double saldoAportaciones = 0.0;
    private double saldoPrestamos = 0.0;
    private double saldoAhorros = 0.0;
    private final double TASA_INTERES_PRESTAMO = 0.004; // 0.4% del saldo de préstamo
    private int socioActualId = 0;
    // Variable para mantener el ID del movimiento seleccionado (para actualización)
    private int movimientoSeleccionadoId = 0;
    // Variable para controlar si estamos en modo de actualización o de nuevo movimiento
    private boolean modoActualizacion = false;
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal
     */
    public MovimientoSocioPanelIntegradoMejorado(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.socioDAO = new SocioDAO();
        inicializarComponentes();
        configurarEventos();
    }
    
    /**
     * Inicializa los componentes del panel
     */    private void inicializarComponentes() {
        // Configuración del panel principal
        setLayout(new BorderLayout(0, 0));
        
        // Panel con scroll para permitir ver todo el contenido en pantallas pequeñas
        JPanel panelScrollable = new JPanel(new GridBagLayout());
        panelScrollable.setBackground(new Color(245, 245, 250)); // Color de fondo suave
        
        JScrollPane scrollPanePrincipal = new JScrollPane(panelScrollable);
        scrollPanePrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPanePrincipal.getVerticalScrollBar().setUnitIncrement(16); // Mejor desplazamiento
        
        // Panel central con GridBagLayout para mejor organización
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCentral.setBackground(new Color(245, 245, 250)); // Color de fondo suave
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Mayor espaciado entre componentes
        
        // Panel para selección de tipo de socio
        crearPanelTipoSocio();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelCentral.add(panelTipoSocio, gbc);
        
        // Panel para búsqueda de socio
        crearPanelBusquedaSocio();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelCentral.add(panelBusquedaSocio, gbc);
        
        // Panel para información del socio
        crearPanelInfoSocio();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelCentral.add(panelInfoSocio, gbc);
          // Panel para tabla de movimientos
        crearPanelTablaMovimientos();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Permitir que la tabla se expanda en ambas direcciones
        gbc.weighty = 0.55; // Asignar peso vertical para que tome más espacio
        gbc.insets = new Insets(20, 10, 20, 10); // Más espacio alrededor de la tabla
        panelCentral.add(panelMovimientos, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Restaurar para otros componentes
        gbc.weighty = 0.0; // Restaurar peso vertical
        gbc.insets = new Insets(10, 10, 10, 10); // Restaurar insets        // Panel para secciones de movimiento - reorganizando en una disposición alineada con las columnas de la tabla
        JPanel panelSeccionesMovimiento = new JPanel(new GridBagLayout());
        panelSeccionesMovimiento.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
                BorderFactory.createEmptyBorder(15, 8, 15, 8)));
        panelSeccionesMovimiento.setBackground(new Color(245, 245, 250)); // Color de fondo suave mejorado
        
        // Crear paneles para cada tipo de movimiento
        crearPanelAportaciones();
        crearPanelPrestaciones();
        crearPanelIntereses();
        crearPanelAhorros();
          // Crear un panel para organizar los campos de entrada alineados con las columnas de la tabla
        JPanel panelAlineado = new JPanel(new GridBagLayout());
        panelAlineado.setOpaque(false);
        
        GridBagConstraints gbcAlineado = new GridBagConstraints();
        gbcAlineado.fill = GridBagConstraints.BOTH;
        gbcAlineado.insets = new Insets(5, 5, 5, 5);
        gbcAlineado.weighty = 1.0;
        
        // Calcular los anchos relativos basados exactamente en las columnas de la tabla
        // Obtenemos los anchos preferidos de cada columna
        int fechaWidth = tablaMovimientos.getColumnModel().getColumn(0).getPreferredWidth(); // Fecha
        
        // Aportaciones (columnas 1-3)
        int aportIngWidth = tablaMovimientos.getColumnModel().getColumn(1).getPreferredWidth(); 
        int aportEgrWidth = tablaMovimientos.getColumnModel().getColumn(2).getPreferredWidth();
        int aportSaldoWidth = tablaMovimientos.getColumnModel().getColumn(3).getPreferredWidth();
        int aportTotalWidth = aportIngWidth + aportEgrWidth + aportSaldoWidth;
        
        // Prestaciones (columnas 4-6)
        int prestEgrWidth = tablaMovimientos.getColumnModel().getColumn(4).getPreferredWidth();
        int prestIngWidth = tablaMovimientos.getColumnModel().getColumn(5).getPreferredWidth();
        int prestSaldoWidth = tablaMovimientos.getColumnModel().getColumn(6).getPreferredWidth();
        int prestTotalWidth = prestEgrWidth + prestIngWidth + prestSaldoWidth;
        
        // Intereses (columna 7)
        int interesWidth = tablaMovimientos.getColumnModel().getColumn(7).getPreferredWidth();
        
        // Ahorros (columnas 8-10)
        int ahorroIngWidth = tablaMovimientos.getColumnModel().getColumn(8).getPreferredWidth();
        int ahorroEgrWidth = tablaMovimientos.getColumnModel().getColumn(9).getPreferredWidth();
        int ahorroSaldoWidth = tablaMovimientos.getColumnModel().getColumn(10).getPreferredWidth();
        int ahorroTotalWidth = ahorroIngWidth + ahorroEgrWidth + ahorroSaldoWidth;
        
        // Total de unidades de ancho para calcular proporciones
        int totalWidth = fechaWidth + aportTotalWidth + prestTotalWidth + interesWidth + ahorroTotalWidth;
        
        // Espacio en blanco para la columna de fecha
        JPanel panelEspacioFecha = new JPanel();
        panelEspacioFecha.setOpaque(false);
        gbcAlineado.gridx = 0;
        gbcAlineado.weightx = (double)fechaWidth / totalWidth;
        panelAlineado.add(panelEspacioFecha, gbcAlineado);
        
        // Aportaciones
        gbcAlineado.gridx = 1;
        gbcAlineado.weightx = (double)aportTotalWidth / totalWidth;
        panelAlineado.add(panelAportaciones, gbcAlineado);
        
        // Prestaciones
        gbcAlineado.gridx = 2;
        gbcAlineado.weightx = (double)prestTotalWidth / totalWidth;
        panelAlineado.add(panelPrestaciones, gbcAlineado);
        
        // Intereses
        gbcAlineado.gridx = 3;
        gbcAlineado.weightx = (double)interesWidth / totalWidth;
        panelAlineado.add(panelIntereses, gbcAlineado);
        
        // Ahorros
        gbcAlineado.gridx = 4;
        gbcAlineado.weightx = (double)ahorroTotalWidth / totalWidth;
        panelAlineado.add(panelAhorros, gbcAlineado);
        
        // Añadir el panel alineado al panel de secciones de movimiento
        GridBagConstraints gbcSecciones = new GridBagConstraints();
        gbcSecciones.fill = GridBagConstraints.BOTH;
        gbcSecciones.weightx = 1.0;
        gbcSecciones.weighty = 1.0;
        panelSeccionesMovimiento.add(panelAlineado, gbcSecciones);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelCentral.add(panelSeccionesMovimiento, gbc);
          // Contenedor para auxiliar de cobro y botones
        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        GridBagConstraints gbcInferior = new GridBagConstraints();
        gbcInferior.fill = GridBagConstraints.BOTH;
        gbcInferior.insets = new Insets(5, 5, 5, 5);
        
        // Panel para auxiliar de cobro
        crearPanelAuxiliarCobro();
        gbcInferior.gridx = 0;
        gbcInferior.gridy = 0;
        gbcInferior.weightx = 0.6; // 60% del espacio para auxiliar de cobro
        panelInferior.add(panelAuxiliarCobro, gbcInferior);
        
        // Panel para botones
        crearPanelBotones();
        gbcInferior.gridx = 1;
        gbcInferior.weightx = 0.4; // 40% del espacio para los botones
        panelInferior.add(panelBotones, gbcInferior);
        
        // Añadir el panel inferior al panel central
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelCentral.add(panelInferior, gbc);        // Establecer un tamaño preferido más grande para el panel
        setPreferredSize(new Dimension(1050, 980));
        
        // Añadir panel central al panel scrollable
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weightx = 1.0;
        gbcMain.weighty = 1.0;
        gbcMain.insets = new Insets(10, 10, 10, 10);
        panelScrollable.add(panelCentral, gbcMain);
        
        // Añadir el scrollpane al panel principal
        add(scrollPanePrincipal, BorderLayout.CENTER);
        
        // Configuración inicial para mostrar/ocultar paneles según tipo de socio
        actualizarPanelesSegunTipoSocio();
    }
    
    /**
     * Crea el panel para selección de tipo de socio
     */
    private void crearPanelTipoSocio() {
        panelTipoSocio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipoSocio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Tipo de Socio", 
                TitledBorder.LEFT, TitledBorder.TOP));
        
        radioAdulto = new JRadioButton("ADULTO", true);
        radioInfantil = new JRadioButton("INFANTIL");
        
        grupoTipoSocio = new ButtonGroup();
        grupoTipoSocio.add(radioAdulto);
        grupoTipoSocio.add(radioInfantil);
        
        panelTipoSocio.add(radioAdulto);
        panelTipoSocio.add(radioInfantil);
    }
    
    /**
     * Crea el panel para búsqueda de socio por ID
     */
    private void crearPanelBusquedaSocio() {
        panelBusquedaSocio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusquedaSocio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Buscar Socio", 
                TitledBorder.LEFT, TitledBorder.TOP));
        
        JLabel lblIdSocio = new JLabel("ID Socio:");
        txtIdSocio = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        
        // Botón para prueba de IDs (temporal)
        JButton btnTestIds = new JButton("Test IDs");
        btnTestIds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testMovimientoIDs();
            }
        });
        
        panelBusquedaSocio.add(lblIdSocio);
        panelBusquedaSocio.add(txtIdSocio);
        panelBusquedaSocio.add(btnBuscar);
        panelBusquedaSocio.add(btnTestIds);
    }
    
    /**
     * Crea el panel para mostrar información del socio
     */    private void crearPanelInfoSocio() {        panelInfoSocio = new JPanel(new GridLayout(3, 2, 20, 12)); // Aún más espacio entre elementos
        
        // Crear un borde más atractivo con colores suaves
        Border lineaBorde = BorderFactory.createLineBorder(new Color(100, 150, 200), 1);
        Border bordeVacio = BorderFactory.createEmptyBorder(12, 18, 12, 18); // Padding interno mayor
        TitledBorder tituloBorde = BorderFactory.createTitledBorder(
                lineaBorde,
                "Información del Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP);
        tituloBorde.setTitleFont(new Font("Arial", Font.BOLD, 14));
        tituloBorde.setTitleColor(new Color(25, 25, 112)); // Navy blue
        
        panelInfoSocio.setBorder(BorderFactory.createCompoundBorder(tituloBorde, bordeVacio));
        panelInfoSocio.setBackground(new Color(248, 248, 255)); // Ghost white
          JPanel panelNombre = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaNombre = new JLabel("Nombre:");
        etiquetaNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre = new JLabel("-");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        panelNombre.add(etiquetaNombre);
        panelNombre.add(lblNombre);
        
        JPanel panelApellidos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaApellidos = new JLabel("Apellidos:");
        etiquetaApellidos.setFont(new Font("Arial", Font.BOLD, 14));
        lblApellidos = new JLabel("-");
        lblApellidos.setFont(new Font("Arial", Font.PLAIN, 14));
        panelApellidos.add(etiquetaApellidos);
        panelApellidos.add(lblApellidos);
          JPanel panelSaldoAportaciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaSaldoAportaciones = new JLabel("Saldo Aportaciones:");
        etiquetaSaldoAportaciones.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoAportaciones = new JLabel("$0.00");
        lblSaldoAportaciones.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoAportaciones.setForeground(new Color(0, 100, 0)); // Verde oscuro
        panelSaldoAportaciones.add(etiquetaSaldoAportaciones);
        panelSaldoAportaciones.add(lblSaldoAportaciones);
        
        JPanel panelSaldoPrestamos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaSaldoPrestamos = new JLabel("Saldo Préstamos:");
        etiquetaSaldoPrestamos.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoPrestamos = new JLabel("$0.00");
        lblSaldoPrestamos.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoPrestamos.setForeground(new Color(170, 0, 0)); // Rojo oscuro
        panelSaldoPrestamos.add(etiquetaSaldoPrestamos);
        panelSaldoPrestamos.add(lblSaldoPrestamos);
        
        JPanel panelSaldoAhorros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel etiquetaSaldoAhorros = new JLabel("Saldo Ahorros:");
        etiquetaSaldoAhorros.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoAhorros = new JLabel("$0.00");
        lblSaldoAhorros.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoAhorros.setForeground(new Color(0, 0, 180)); // Azul
        panelSaldoAhorros.add(etiquetaSaldoAhorros);
        panelSaldoAhorros.add(lblSaldoAhorros);
        
        panelInfoSocio.add(panelNombre);
        panelInfoSocio.add(panelApellidos);
        panelInfoSocio.add(panelSaldoAportaciones);
        panelInfoSocio.add(panelSaldoPrestamos);
        panelInfoSocio.add(panelSaldoAhorros);
    }
    
    /**
     * Crea el panel para la tabla de movimientos recientes
     */
    private void crearPanelTablaMovimientos() {
        panelMovimientos = new JPanel(new BorderLayout(10, 5)); // Agregamos espaciado
        panelMovimientos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), 
                    "Movimientos Recientes", 
                    TitledBorder.LEFT, 
                    TitledBorder.TOP),
                BorderFactory.createEmptyBorder(5, 5, 10, 5))); // Padding interno
          // Crear modelo de tabla según la estructura de la imagen de referencia
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que ninguna celda sea editable
            }
        };          // Columna de Fecha unificada
        modeloTabla.addColumn("Fecha");
        
        // Columnas para Aportaciones
        modeloTabla.addColumn("ING. APOR."); // Aportaciones - Ingresos
        modeloTabla.addColumn("EGR. APOR.");  // Aportaciones - Egresos
        modeloTabla.addColumn("SALDO APOR.");    // Aportaciones - Saldo
        
        // Columnas para Prestaciones
        modeloTabla.addColumn("EGR. PREST.");  // Prestaciones - Egresos
        modeloTabla.addColumn("ING. PREST."); // Prestaciones - Ingresos/Abonos
        modeloTabla.addColumn("SALDO PREST.");    // Prestaciones - Saldo
        
        // Columna para Intereses
        modeloTabla.addColumn("INTERÉS");
        
        // Columnas para Ahorros
        modeloTabla.addColumn("ING. AHORRO"); // Ahorros - Ingresos
        modeloTabla.addColumn("EGR. AHORRO");  // Ahorros - Egresos
        modeloTabla.addColumn("SALDO AHORRO");    // Ahorros - Saldo
        
        // Columna invisible para ID del movimiento
        modeloTabla.addColumn("ID Movimiento");
          // Crear tabla con el modelo y mejorar su apariencia
        tablaMovimientos = new JTable(modeloTabla) {
            // Personalizar los colores de las celdas según la categoría
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);                // Aplicar colores según categorías (similar a la imagen de referencia)
                if (comp instanceof JComponent) {
                    // Fecha (columna 0)
                    if (column == 0) {
                        comp.setBackground(new Color(240, 240, 240));
                    }
                    // Aportaciones (columnas 1-3)
                    else if (column >= 1 && column <= 3) {
                        comp.setBackground(new Color(230, 242, 255));
                    }
                    // Prestaciones (columnas 4-6)
                    else if (column >= 4 && column <= 6) {
                        comp.setBackground(new Color(255, 230, 230));
                    }
                    // Intereses (columna 7)
                    else if (column == 7) {
                        comp.setBackground(new Color(255, 255, 220));
                    }
                    // Ahorros (columnas 8-10)
                    else if (column >= 8 && column <= 10) {
                        comp.setBackground(new Color(230, 255, 230));
                    }
                    
                    // Resaltar columnas de saldo
                    if (column == 3 || column == 6 || column == 10) {
                        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
                    }
                    
                    // Si es la fila seleccionada, mantener un color que permita visualizar la selección
                    if (isRowSelected(row)) {
                        comp.setBackground(comp.getBackground().darker());
                    }
                }
                
                return comp;
            }        };        tablaMovimientos.setPreferredScrollableViewportSize(new Dimension(980, 300)); // Tamaño aún más grande
        tablaMovimientos.setFillsViewportHeight(true);
        tablaMovimientos.setRowHeight(38); // Filas aún más altas para mejor legibilidad
        tablaMovimientos.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente más grande
        tablaMovimientos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaMovimientos.getTableHeader().setPreferredSize(new Dimension(0, 42)); // Header aún más alto
        tablaMovimientos.setShowGrid(true);
        tablaMovimientos.setGridColor(new Color(180, 180, 180)); // Grid más visible, color más oscuro
        tablaMovimientos.setRowMargin(6); // Mayor espacio entre filas
        tablaMovimientos.setIntercellSpacing(new Dimension(14, 10)); // Mayor espacio entre celdas
        
        // Configurar anchos de columna para mejor visualización
        TableColumnModel columnModel = tablaMovimientos.getColumnModel();
          // Fecha unificada
        columnModel.getColumn(0).setPreferredWidth(90);  // Fecha
        
        // Aportaciones
        columnModel.getColumn(1).setPreferredWidth(90);  // Ingresos
        columnModel.getColumn(2).setPreferredWidth(90);  // Egresos
        columnModel.getColumn(3).setPreferredWidth(90);  // Saldo
          // Prestaciones
        columnModel.getColumn(4).setPreferredWidth(90);  // Egresos
        columnModel.getColumn(5).setPreferredWidth(90);  // Ingresos
        columnModel.getColumn(6).setPreferredWidth(90);  // Saldo
        
        // Intereses
        columnModel.getColumn(7).setPreferredWidth(90);  // Intereses
        
        // Ahorros
        columnModel.getColumn(8).setPreferredWidth(90); // Ingresos
        columnModel.getColumn(9).setPreferredWidth(90); // Egresos
        columnModel.getColumn(10).setPreferredWidth(90); // Saldo
        
        // Ocultar columna ID
        columnModel.getColumn(11).setMinWidth(0);
        columnModel.getColumn(11).setMaxWidth(0);
        columnModel.getColumn(11).setWidth(0);
        columnModel.getColumn(11).setPreferredWidth(0);
        
        // Mejorar la selección de filas para hacerla más visible
        tablaMovimientos.setSelectionBackground(new Color(135, 206, 250)); // Light sky blue
        tablaMovimientos.setSelectionForeground(Color.BLACK);
        
        // Alternar colores de filas para mejor legibilidad (se aplicará en conjunto con el renderizador personalizado)
        tablaMovimientos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? table.getBackground() : new Color(245, 245, 250));
                }
                
                // Mejora la alineación para valores numéricos
                if (value instanceof Number) {
                    ((JLabel) comp).setHorizontalAlignment(JLabel.RIGHT);
                    ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
                }
                
                return comp;
            }
        });
        
        // Ajustar anchos de columna        // Columna de fecha
        tablaMovimientos.getColumnModel().getColumn(0).setPreferredWidth(100); // Fecha
        
        // Columnas de aportaciones
        tablaMovimientos.getColumnModel().getColumn(1).setPreferredWidth(80); // Ingresos
        tablaMovimientos.getColumnModel().getColumn(2).setPreferredWidth(80); // Egresos
        tablaMovimientos.getColumnModel().getColumn(3).setPreferredWidth(80); // Saldo
        
        // Columnas de prestaciones
        tablaMovimientos.getColumnModel().getColumn(4).setPreferredWidth(80); // Egresos
        tablaMovimientos.getColumnModel().getColumn(5).setPreferredWidth(120); // Ingresos/Abonos
        tablaMovimientos.getColumnModel().getColumn(6).setPreferredWidth(80); // Saldo
        
        // Columna de intereses
        tablaMovimientos.getColumnModel().getColumn(7).setPreferredWidth(80); // Intereses
        
        // Columnas de ahorros
        tablaMovimientos.getColumnModel().getColumn(8).setPreferredWidth(80); // Ingresos
        tablaMovimientos.getColumnModel().getColumn(9).setPreferredWidth(80); // Egresos
        tablaMovimientos.getColumnModel().getColumn(10).setPreferredWidth(80); // Saldo
        
        // Ocultar la columna del ID del movimiento
        tablaMovimientos.getColumnModel().getColumn(11).setMinWidth(0);
        tablaMovimientos.getColumnModel().getColumn(11).setMaxWidth(0);
        tablaMovimientos.getColumnModel().getColumn(11).setWidth(0);
        
        // Configurar selección de fila
        tablaMovimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Agregar listener para detectar cuando el usuario selecciona un movimiento
        tablaMovimientos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaMovimientos.getSelectedRow() != -1) {
                seleccionarMovimiento(tablaMovimientos.getSelectedRow());
            }
        });
          // Crear una tabla con cabecera multicapa para mostrar las categorías principales
        // Esto requiere una implementación personalizada o usar librerías como JTableHeader
        // Por simplicidad, agregamos un panel informativo en la parte superior
        JPanel panelCategorias = new JPanel(new GridLayout(1, 5, 10, 0));
        panelCategorias.setBackground(new Color(240, 240, 240));
        
        // Etiquetas para cada categoría principal
        JLabel lblFecha = new JLabel("Fecha", JLabel.CENTER);
        lblFecha.setFont(new Font("Arial", Font.BOLD, 14));
        lblFecha.setBorder(BorderFactory.createEtchedBorder());
        lblFecha.setOpaque(true);
        lblFecha.setBackground(new Color(240, 240, 240));
        
        JLabel lblAportaciones = new JLabel("APORTACIONES", JLabel.CENTER);
        lblAportaciones.setFont(new Font("Arial", Font.BOLD, 14));
        lblAportaciones.setBorder(BorderFactory.createEtchedBorder());
        lblAportaciones.setOpaque(true);
        lblAportaciones.setBackground(new Color(230, 242, 255));
        
        JLabel lblPrestaciones = new JLabel("PRESTACIONES", JLabel.CENTER);
        lblPrestaciones.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrestaciones.setBorder(BorderFactory.createEtchedBorder());
        lblPrestaciones.setOpaque(true);
        lblPrestaciones.setBackground(new Color(255, 230, 230));
        
        JLabel lblIntereses = new JLabel("INTERESES", JLabel.CENTER);
        lblIntereses.setFont(new Font("Arial", Font.BOLD, 14));
        lblIntereses.setBorder(BorderFactory.createEtchedBorder());
        lblIntereses.setOpaque(true);
        lblIntereses.setBackground(new Color(255, 255, 220));
        
        JLabel lblAhorros = new JLabel("AHORROS", JLabel.CENTER);
        lblAhorros.setFont(new Font("Arial", Font.BOLD, 14));
        lblAhorros.setBorder(BorderFactory.createEtchedBorder());
        lblAhorros.setOpaque(true);
        lblAhorros.setBackground(new Color(230, 255, 230));
        
        // Agregar etiquetas al panel de categorías
        panelCategorias.add(lblFecha);
        panelCategorias.add(lblAportaciones);
        panelCategorias.add(lblPrestaciones);
        panelCategorias.add(lblIntereses);
        panelCategorias.add(lblAhorros);        // Agregar un panel de instrucciones en la parte superior con mejor diseño
        JPanel panelInstrucciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelInstrucciones.setBackground(new Color(255, 248, 220)); // Amarillo crema más agradable
        panelInstrucciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 180)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        // Icono informativo
        JLabel lblIcon = new JLabel("\u24D8"); // Unicode information symbol
        lblIcon.setFont(new Font("Arial", Font.BOLD, 16));
        lblIcon.setForeground(new Color(70, 130, 180)); // Steel blue
        
        JLabel lblInstrucciones = new JLabel("Seleccione un movimiento de la tabla para actualizar los datos");
        lblInstrucciones.setFont(new Font("Arial", Font.ITALIC, 13));
        lblInstrucciones.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        
        panelInstrucciones.add(lblIcon);
        panelInstrucciones.add(lblInstrucciones);// Crear un panel compuesto para la parte superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelInstrucciones, BorderLayout.NORTH);
        panelSuperior.add(panelCategorias, BorderLayout.CENTER);
        
        // Crear un scroll pane para la tabla con mejores propiedades visuales
        JScrollPane scrollPaneTabla = new JScrollPane(tablaMovimientos);
        scrollPaneTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPaneTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Agregar panel de categorías y scroll pane al panel principal
        panelMovimientos.add(panelSuperior, BorderLayout.NORTH);
        panelMovimientos.add(scrollPaneTabla, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel para las aportaciones
     */    private void crearPanelAportaciones() {
        panelAportaciones = new JPanel();
        panelAportaciones.setLayout(new BoxLayout(panelAportaciones, BoxLayout.Y_AXIS));
        panelAportaciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "APORTACIONES", 
                TitledBorder.CENTER, TitledBorder.TOP));
        panelAportaciones.setBackground(new Color(230, 242, 255)); // Color de fondo igual que la columna
        
        // Crear formato para campos numéricos
        NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
        
        // Formateador para edición (más permisivo)
        NumberFormatter formateadorEdicion = new NumberFormatter(NumberFormat.getInstance(Locale.forLanguageTag("es-MX")));
        formateadorEdicion.setValueClass(Double.class);
        formateadorEdicion.setAllowsInvalid(true); // Permitir entrada inválida durante la edición
        formateadorEdicion.setMinimum(0.0);
        
        // Formateador para visualización
        NumberFormatter formateador = new NumberFormatter(formatoNumero);
        formateador.setValueClass(Double.class);
        formateador.setMinimum(0.0);
        
        // Factory con ambos formateadores
        DefaultFormatterFactory factory = new DefaultFormatterFactory(
            formateador, // formateador por defecto
            formateador, // formateador de visualización
            formateadorEdicion // formateador de edición
        );
        
        // Panel para depósito (ING. APOR.)
        JPanel panelDeposito = new JPanel(new GridBagLayout());
        panelDeposito.setOpaque(false);
        GridBagConstraints gbcDeposito = new GridBagConstraints();
        gbcDeposito.fill = GridBagConstraints.HORIZONTAL;
        gbcDeposito.anchor = GridBagConstraints.WEST;
        gbcDeposito.insets = new Insets(3, 3, 3, 3);
        gbcDeposito.weightx = 0.3;
        gbcDeposito.gridx = 0;
        gbcDeposito.gridy = 0;
        
        JLabel lblDeposito = new JLabel("ING. APOR.:");
        lblDeposito.setFont(new Font("Arial", Font.BOLD, 12));
        panelDeposito.add(lblDeposito, gbcDeposito);
        
        gbcDeposito.gridx = 1;
        gbcDeposito.weightx = 0.7;
        txtAportacionDeposito = new JFormattedTextField(factory);
        txtAportacionDeposito.setColumns(8);
        txtAportacionDeposito.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelDeposito.add(txtAportacionDeposito, gbcDeposito);
        
        // Panel para retiro (EGR. APOR.)
        JPanel panelRetiro = new JPanel(new GridBagLayout());
        panelRetiro.setOpaque(false);
        GridBagConstraints gbcRetiro = new GridBagConstraints();
        gbcRetiro.fill = GridBagConstraints.HORIZONTAL;
        gbcRetiro.anchor = GridBagConstraints.WEST;
        gbcRetiro.insets = new Insets(3, 3, 3, 3);
        gbcRetiro.weightx = 0.3;
        gbcRetiro.gridx = 0;
        gbcRetiro.gridy = 0;
        
        JLabel lblRetiro = new JLabel("EGR. APOR.:");
        lblRetiro.setFont(new Font("Arial", Font.BOLD, 12));
        panelRetiro.add(lblRetiro, gbcRetiro);
        
        gbcRetiro.gridx = 1;
        gbcRetiro.weightx = 0.7;
        txtAportacionRetiro = new JFormattedTextField(factory);
        txtAportacionRetiro.setColumns(8);
        txtAportacionRetiro.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelRetiro.add(txtAportacionRetiro, gbcRetiro);
        
        // Añadir paneles al panel principal
        panelAportaciones.add(Box.createVerticalStrut(5)); // Espacio superior
        panelAportaciones.add(panelDeposito);
        panelAportaciones.add(Box.createVerticalStrut(8)); // Espacio entre campos
        panelAportaciones.add(panelRetiro);
        panelAportaciones.add(Box.createVerticalStrut(5)); // Espacio inferior
    }
    
    /**
     * Crea el panel para las prestaciones
     */    private void crearPanelPrestaciones() {
        panelPrestaciones = new JPanel();
        panelPrestaciones.setLayout(new BoxLayout(panelPrestaciones, BoxLayout.Y_AXIS));
        panelPrestaciones.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "PRESTACIONES", 
                TitledBorder.CENTER, TitledBorder.TOP));
        panelPrestaciones.setBackground(new Color(255, 230, 230)); // Color de fondo igual que la columna
        
        // Crear formato para campos numéricos
        NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
        
        // Formateador para edición (más permisivo)
        NumberFormatter formateadorEdicion = new NumberFormatter(NumberFormat.getInstance(Locale.forLanguageTag("es-MX")));
        formateadorEdicion.setValueClass(Double.class);
        formateadorEdicion.setAllowsInvalid(true); // Permitir entrada inválida durante la edición
        formateadorEdicion.setMinimum(0.0);
        
        // Formateador para visualización
        NumberFormatter formateador = new NumberFormatter(formatoNumero);
        formateador.setValueClass(Double.class);
        formateador.setMinimum(0.0);
        
        // Factory con ambos formateadores
        DefaultFormatterFactory factory = new DefaultFormatterFactory(
            formateador, // formateador por defecto
            formateador, // formateador de visualización
            formateadorEdicion // formateador de edición
        );
        
        // Panel para retiro (EGR. PREST.)
        JPanel panelRetiro = new JPanel(new GridBagLayout());
        panelRetiro.setOpaque(false);
        GridBagConstraints gbcRetiro = new GridBagConstraints();
        gbcRetiro.fill = GridBagConstraints.HORIZONTAL;
        gbcRetiro.anchor = GridBagConstraints.WEST;
        gbcRetiro.insets = new Insets(3, 3, 3, 3);
        gbcRetiro.weightx = 0.3;
        gbcRetiro.gridx = 0;
        gbcRetiro.gridy = 0;
        
        JLabel lblRetiro = new JLabel("EGR. PREST.:");
        lblRetiro.setFont(new Font("Arial", Font.BOLD, 12));
        panelRetiro.add(lblRetiro, gbcRetiro);
        
        gbcRetiro.gridx = 1;
        gbcRetiro.weightx = 0.7;
        txtPrestamoRetiro = new JFormattedTextField(factory);
        txtPrestamoRetiro.setColumns(8);
        txtPrestamoRetiro.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelRetiro.add(txtPrestamoRetiro, gbcRetiro);
        
        // Panel para depósito (ING. PREST.)
        JPanel panelDeposito = new JPanel(new GridBagLayout());
        panelDeposito.setOpaque(false);
        GridBagConstraints gbcDeposito = new GridBagConstraints();
        gbcDeposito.fill = GridBagConstraints.HORIZONTAL;
        gbcDeposito.anchor = GridBagConstraints.WEST;
        gbcDeposito.insets = new Insets(3, 3, 3, 3);
        gbcDeposito.weightx = 0.3;
        gbcDeposito.gridx = 0;
        gbcDeposito.gridy = 0;
        
        JLabel lblDeposito = new JLabel("ING. PREST.:");
        lblDeposito.setFont(new Font("Arial", Font.BOLD, 12));
        panelDeposito.add(lblDeposito, gbcDeposito);
        
        gbcDeposito.gridx = 1;
        gbcDeposito.weightx = 0.7;
        txtPrestamoDeposito = new JFormattedTextField(factory);
        txtPrestamoDeposito.setColumns(8);
        txtPrestamoDeposito.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelDeposito.add(txtPrestamoDeposito, gbcDeposito);
        
        // Añadir paneles al panel principal
        panelPrestaciones.add(Box.createVerticalStrut(5)); // Espacio superior
        panelPrestaciones.add(panelRetiro);
        panelPrestaciones.add(Box.createVerticalStrut(8)); // Espacio entre campos
        panelPrestaciones.add(panelDeposito);
        panelPrestaciones.add(Box.createVerticalStrut(5)); // Espacio inferior
    }
      /**
     * Crea el panel para los intereses
     */    private void crearPanelIntereses() {
        panelIntereses = new JPanel();
        panelIntereses.setLayout(new BoxLayout(panelIntereses, BoxLayout.Y_AXIS));
        panelIntereses.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "INTERESES", 
                TitledBorder.CENTER, TitledBorder.TOP));
        panelIntereses.setBackground(new Color(255, 255, 220)); // Color de fondo igual que la columna
        
        // Crear formato para campos numéricos
        NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
        
        // Formateador para edición (más permisivo)
        NumberFormatter formateadorEdicion = new NumberFormatter(NumberFormat.getInstance(Locale.forLanguageTag("es-MX")));
        formateadorEdicion.setValueClass(Double.class);
        formateadorEdicion.setAllowsInvalid(true); // Permitir entrada inválida durante la edición
        formateadorEdicion.setMinimum(0.0);
        
        // Formateador para visualización
        NumberFormatter formateador = new NumberFormatter(formatoNumero);
        formateador.setValueClass(Double.class);
        formateador.setMinimum(0.0);
        
        // Panel para intereses de deuda
        JPanel panelInteresesDeuda = new JPanel(new GridBagLayout());
        panelInteresesDeuda.setOpaque(false);
        GridBagConstraints gbcIntereses = new GridBagConstraints();
        gbcIntereses.fill = GridBagConstraints.HORIZONTAL;
        gbcIntereses.anchor = GridBagConstraints.WEST;
        gbcIntereses.insets = new Insets(3, 3, 3, 3);
        gbcIntereses.weightx = 0.3;
        gbcIntereses.gridx = 0;
        gbcIntereses.gridy = 0;
        
        JLabel lblIntereses = new JLabel("INTERÉS:");
        lblIntereses.setFont(new Font("Arial", Font.BOLD, 12));
        panelInteresesDeuda.add(lblIntereses, gbcIntereses);
        
        gbcIntereses.gridx = 1;
        gbcIntereses.weightx = 0.7;
        txtInteresDeuda = new JFormattedTextField(new DefaultFormatterFactory(formateador));
        txtInteresDeuda.setColumns(8);
        txtInteresDeuda.setEditable(false);
        panelInteresesDeuda.add(txtInteresDeuda, gbcIntereses);
        
        // Panel para selector de semanas de interés con diseño similar a la imagen
        JPanel panelSemanas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSemanas = new JLabel("Semanas de interés:");
        
        // Crear un panel especial para el selector de semanas
        JPanel selectorSemanas = new JPanel(new BorderLayout());
        selectorSemanas.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        selectorSemanas.setPreferredSize(new Dimension(80, 25));
        selectorSemanas.setBackground(Color.WHITE);
        
        // ComboBox para seleccionar el número de semanas
        cboSemanasInteres = new JComboBox<>();
        for (int i = 1; i <= 12; i++) { // Limitamos a 12 semanas que es más común
            cboSemanasInteres.addItem(i);
        }
        
        cboSemanasInteres.setSelectedItem(1); // Default: 1 semana
        cboSemanasInteres.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Agregar el ComboBox al panel selector
        selectorSemanas.add(cboSemanasInteres, BorderLayout.CENTER);
        
        panelSemanas.add(lblSemanas);
        panelSemanas.add(selectorSemanas);
        
        // Añadir etiqueta informativa
        JLabel lblInfo = new JLabel("Seleccione las semanas a calcular con Intereses");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel para mostrar cálculo de intereses
        JPanel panelCalculo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblCalculo = new JLabel("Cálculo (0.4%):");
        lblInteresCalculado = new JLabel("$0.00");
        lblInteresCalculado.setFont(new Font("Arial", Font.BOLD, 14));
        panelCalculo.add(lblCalculo);
        panelCalculo.add(lblInteresCalculado);        // Añadir los eventos para recalcular intereses al cambiar el número de semanas
        cboSemanasInteres.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularInteresesPorSemanas();
            }
        });
        
        // Añadir paneles al panel principal
        panelIntereses.add(panelInteresesDeuda);
        panelIntereses.add(panelSemanas);
        panelIntereses.add(Box.createVerticalStrut(5));
        panelIntereses.add(lblInfo);
        panelIntereses.add(Box.createVerticalStrut(5));
        panelIntereses.add(panelCalculo);
    }
    
    /**
     * Crea el panel para los ahorros
     */    private void crearPanelAhorros() {
        panelAhorros = new JPanel();
        panelAhorros.setLayout(new BoxLayout(panelAhorros, BoxLayout.Y_AXIS));
        panelAhorros.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "AHORROS", 
                TitledBorder.CENTER, TitledBorder.TOP));
        panelAhorros.setBackground(new Color(230, 255, 230)); // Color de fondo igual que la columna
        
        // Crear formato para campos numéricos
        NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
        
        // Formateador para edición (más permisivo)
        NumberFormatter formateadorEdicion = new NumberFormatter(NumberFormat.getInstance(Locale.forLanguageTag("es-MX")));
        formateadorEdicion.setValueClass(Double.class);
        formateadorEdicion.setAllowsInvalid(true); // Permitir entrada inválida durante la edición
        formateadorEdicion.setMinimum(0.0);
        
        // Formateador para visualización
        NumberFormatter formateador = new NumberFormatter(formatoNumero);
        formateador.setValueClass(Double.class);
        formateador.setMinimum(0.0);
        
        // Factory con ambos formateadores
        DefaultFormatterFactory factory = new DefaultFormatterFactory(
            formateador, // formateador por defecto
            formateador, // formateador de visualización
            formateadorEdicion // formateador de edición
        );
        
        // Panel para depósito (ING. AHORRO)
        JPanel panelDeposito = new JPanel(new GridBagLayout());
        panelDeposito.setOpaque(false);
        GridBagConstraints gbcDeposito = new GridBagConstraints();
        gbcDeposito.fill = GridBagConstraints.HORIZONTAL;
        gbcDeposito.anchor = GridBagConstraints.WEST;
        gbcDeposito.insets = new Insets(3, 3, 3, 3);
        gbcDeposito.weightx = 0.3;
        gbcDeposito.gridx = 0;
        gbcDeposito.gridy = 0;
        
        JLabel lblDeposito = new JLabel("ING. AHORRO:");
        lblDeposito.setFont(new Font("Arial", Font.BOLD, 12));
        panelDeposito.add(lblDeposito, gbcDeposito);
        
        gbcDeposito.gridx = 1;
        gbcDeposito.weightx = 0.7;
        txtAhorroDeposito = new JFormattedTextField(factory);
        txtAhorroDeposito.setColumns(8);
        txtAhorroDeposito.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelDeposito.add(txtAhorroDeposito, gbcDeposito);
        
        // Panel para retiro (EGR. AHORRO)
        JPanel panelRetiro = new JPanel(new GridBagLayout());
        panelRetiro.setOpaque(false);
        GridBagConstraints gbcRetiro = new GridBagConstraints();
        gbcRetiro.fill = GridBagConstraints.HORIZONTAL;
        gbcRetiro.anchor = GridBagConstraints.WEST;
        gbcRetiro.insets = new Insets(3, 3, 3, 3);
        gbcRetiro.weightx = 0.3;
        gbcRetiro.gridx = 0;
        gbcRetiro.gridy = 0;
        
        JLabel lblRetiro = new JLabel("EGR. AHORRO:");
        lblRetiro.setFont(new Font("Arial", Font.BOLD, 12));
        panelRetiro.add(lblRetiro, gbcRetiro);
        
        gbcRetiro.gridx = 1;
        gbcRetiro.weightx = 0.7;
        txtAhorroRetiro = new JFormattedTextField(factory);
        txtAhorroRetiro.setColumns(8);
        txtAhorroRetiro.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelRetiro.add(txtAhorroRetiro, gbcRetiro);
          // Panel para saldo actual (SALDO AHORRO)
        JPanel panelSaldo = new JPanel(new GridBagLayout());
        panelSaldo.setOpaque(false);
        GridBagConstraints gbcSaldo = new GridBagConstraints();
        gbcSaldo.fill = GridBagConstraints.HORIZONTAL;
        gbcSaldo.anchor = GridBagConstraints.WEST;
        gbcSaldo.insets = new Insets(3, 3, 3, 3);
        gbcSaldo.weightx = 0.3;
        gbcSaldo.gridx = 0;
        gbcSaldo.gridy = 0;
        
        JLabel lblSaldo = new JLabel("SALDO AHORRO:");
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 12));
        panelSaldo.add(lblSaldo, gbcSaldo);
        
        gbcSaldo.gridx = 1;
        gbcSaldo.weightx = 0.7;
        txtSaldo = new JTextField();
        txtSaldo.setColumns(8);
        txtSaldo.setFont(new Font("Arial", Font.BOLD, 12));
        txtSaldo.setEditable(false);
        txtSaldo.setText("$0.00"); // Valor inicial
        panelSaldo.add(txtSaldo, gbcSaldo);
        
        // Añadir paneles al panel principal
        panelAhorros.add(Box.createVerticalStrut(5)); // Espacio superior
        panelAhorros.add(panelDeposito);
        panelAhorros.add(Box.createVerticalStrut(8)); // Espacio entre campos
        panelAhorros.add(panelRetiro);
        panelAhorros.add(Box.createVerticalStrut(8)); // Espacio entre campos
        panelAhorros.add(panelSaldo);
        panelAhorros.add(Box.createVerticalStrut(5)); // Espacio inferior
    }
    
    /**
     * Crea el panel para el auxiliar de cobro
     */
    private void crearPanelAuxiliarCobro() {
        panelAuxiliarCobro = new JPanel(new BorderLayout(10, 10));
        panelAuxiliarCobro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Auxiliar de Cobro", 
                TitledBorder.LEFT, TitledBorder.TOP));
        
        // Panel interno para los campos con una disposición más específica
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCampos.setBackground(new Color(248, 248, 240)); // Color similar al de la imagen
          // Crear formato para campos numéricos (usando forLanguageTag para evitar la advertencia de deprecación)
        NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
        
        // Formateador para edición (más permisivo)
        NumberFormatter formateadorEdicion = new NumberFormatter(NumberFormat.getInstance(Locale.forLanguageTag("es-MX")));
        formateadorEdicion.setValueClass(Double.class);
        formateadorEdicion.setAllowsInvalid(true); // Permitir entrada inválida durante la edición
        formateadorEdicion.setMinimum(0.0);
        
        // Formateador para visualización
        NumberFormatter formateador = new NumberFormatter(formatoNumero);
        formateador.setValueClass(Double.class);
        formateador.setMinimum(0.0);
        
        // Factory con ambos formateadores
        DefaultFormatterFactory factory = new DefaultFormatterFactory(
            formateador, // formateador por defecto
            formateador, // formateador de visualización
            formateadorEdicion // formateador de edición
        );
        
        // Panel para total del movimiento - con estilo similar a la imagen
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTotal.setOpaque(false);
        JLabel lblTotal = new JLabel("Total:");        txtTotalMovimiento = new JFormattedTextField(formateador);
        txtTotalMovimiento.setColumns(8);
        txtTotalMovimiento.setValue(0.0);
        txtTotalMovimiento.setEditable(false);
        txtTotalMovimiento.setBackground(new Color(255, 200, 200)); // Fondo rojo claro como en la imagen
        txtTotalMovimiento.setFont(new Font("Arial", Font.BOLD, 14));
        panelTotal.add(lblTotal);
        panelTotal.add(txtTotalMovimiento);
        
        // Panel para cantidad recibida - con estilo similar a la imagen
        JPanel panelRecibido = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRecibido.setOpaque(false);
        JLabel lblRecibido = new JLabel("Su pago:");        txtCantidadRecibida = new JFormattedTextField(factory);
        txtCantidadRecibida.setColumns(8);
        txtCantidadRecibida.setFocusLostBehavior(JFormattedTextField.COMMIT);
        panelRecibido.add(lblRecibido);
        panelRecibido.add(txtCantidadRecibida);
        
        // Panel para cambio - con estilo similar a la imagen
        JPanel panelCambio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCambio.setOpaque(false);
        JLabel lblCambioTexto = new JLabel("Cambio:");
        lblCambio = new JLabel("...");
        lblCambio.setFont(new Font("Arial", Font.BOLD, 14));
        panelCambio.add(lblCambioTexto);
        panelCambio.add(lblCambio);
        
        // Añadir paneles al panel de campos
        panelCampos.add(panelTotal);
        panelCampos.add(panelRecibido);
        panelCampos.add(panelCambio);
        
        // Añadir panel de campos al panel principal
        panelAuxiliarCobro.add(panelCampos, BorderLayout.CENTER);
    }
      /**
     * Crea el panel para los botones principales
     */    // Botón para cancelar actualización
    private JButton btnCancelarActualizacion;
    
    private void crearPanelBotones() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        
        JPanel filaBotones1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel filaBotones2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        // Crear botones con apariencia similar a la imagen de referencia
        btnAplicar = new JButton("Guardar Datos");
        btnAplicar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAplicar.setBackground(new Color(220, 240, 220)); // Color verde claro
        btnAplicar.setForeground(Color.BLACK);
        btnAplicar.setPreferredSize(new Dimension(150, 40));
        
        btnCancelarActualizacion = new JButton("Cancelar Actualización");
        btnCancelarActualizacion.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelarActualizacion.setPreferredSize(new Dimension(250, 35));
        btnCancelarActualizacion.setVisible(false); // Inicialmente oculto
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
        btnLimpiar.setPreferredSize(new Dimension(120, 35));
        
        JButton btnImprimir = new JButton("Imprimir Movimientos");
        btnImprimir.setFont(new Font("Arial", Font.BOLD, 14));
        btnImprimir.setPreferredSize(new Dimension(250, 30));
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setPreferredSize(new Dimension(100, 35));
        
        // Agregar botones a los paneles
        filaBotones1.add(btnAplicar);
        filaBotones1.add(btnCancelarActualizacion);
        filaBotones1.add(btnLimpiar);
        
        filaBotones2.add(btnImprimir);
        filaBotones2.add(btnCerrar);
        
        // Agregar evento para botón de impresión
        btnImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    MovimientoSocioPanelIntegradoMejorado.this, 
                    "Funcionalidad de impresión aún no implementada", 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Agregar filas de botones al panel principal
        panelBotones.add(filaBotones1);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre filas
        panelBotones.add(filaBotones2);
    }
      /**
     * Configura los eventos para los componentes del formulario
     */
    private void configurarEventos() {
        // Configurar listeners para actualización dinámica del cambio
        configurarListenersCambioDinamico();
        
        // Eventos para selección de tipo de socio
        radioAdulto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                esSocioAdulto = true;
                actualizarPanelesSegunTipoSocio();
            }
        });
        
        radioInfantil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                esSocioAdulto = false;
                actualizarPanelesSegunTipoSocio();
            }
        });
        
        // Evento para buscar socio
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSocio();
            }
        });
        
        // Evento para tecla Enter en el campo de ID de socio
        txtIdSocio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarSocio();
                }
            }
        });
        
        // Evento para calcular total del movimiento
        KeyAdapter actualizarTotalKeyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotalMovimiento();
            }
        };
        
        // Añadir eventos para actualizar total en cada campo
        txtAportacionDeposito.addKeyListener(actualizarTotalKeyAdapter);
        txtAportacionRetiro.addKeyListener(actualizarTotalKeyAdapter);
        txtPrestamoDeposito.addKeyListener(actualizarTotalKeyAdapter);
        txtPrestamoRetiro.addKeyListener(actualizarTotalKeyAdapter);
        txtAhorroDeposito.addKeyListener(actualizarTotalKeyAdapter);
        txtAhorroRetiro.addKeyListener(actualizarTotalKeyAdapter);
        txtInteresDeuda.addKeyListener(actualizarTotalKeyAdapter);
        
        // Evento para calcular cambio
        txtCantidadRecibida.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularCambio();
            }
        });
        
        // Evento para botón aplicar
        btnAplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarMovimiento();
            }
        });
        
        // Evento para botón limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        
        // Evento para botón cerrar
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                socioDAO.cerrarConexion();
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
    }    /**
     * Configura los listeners para actualizar el cambio dinámicamente
     */
    private void configurarListenersCambioDinamico() {
        // Crear un PropertyChangeListener genérico para todos los campos numéricos
        PropertyChangeListener actualizadorTotal = new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcularTotalMovimiento();
            }
        };
        
        // Listener específico para depósitos de aportaciones (validar límite de $300)
        txtAportacionDeposito.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                validarLimiteAportacion();
                actualizarSaldosTemporal();
                calcularTotalMovimiento();
            }
        });
        
        // Listener específico para retiros de aportaciones
        txtAportacionRetiro.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                validarRetiros();
                actualizarSaldosTemporal();
                calcularTotalMovimiento();
            }
        });
        
        // Agregar listener a otros campos que afectan el total y saldos
        txtPrestamoDeposito.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                actualizarSaldosTemporal();
                calcularTotalMovimiento();
            }
        });
        
        txtPrestamoRetiro.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                validarRetiros();
                actualizarSaldosTemporal();
                calcularTotalMovimiento();
            }
        });
        
        txtAhorroDeposito.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                actualizarSaldosTemporal();
                calcularTotalMovimiento();
            }
        });
        
        txtAhorroRetiro.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                validarRetiros();
                actualizarSaldosTemporal();
                calcularTotalMovimiento();
            }
        });
        
        txtInteresDeuda.addPropertyChangeListener("value", actualizadorTotal);
        
        // Agregar listener específico para la cantidad recibida
        txtCantidadRecibida.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calcularCambio();
            }
        });
    }
    
    /**
     * Muestra u oculta paneles según el tipo de socio seleccionado
     */
    private void actualizarPanelesSegunTipoSocio() {
        if (esSocioAdulto) {
            // Para socios adultos mostrar todos los paneles
            panelAportaciones.setVisible(true);
            panelPrestaciones.setVisible(true);
            panelIntereses.setVisible(true);
            panelAhorros.setVisible(true);
            
            // Mostrar todas las columnas en la tabla
            if (tablaMovimientos.getColumnCount() > 0) {
                for (int i = 1; i <= 4; i++) {
                    tablaMovimientos.getColumnModel().getColumn(i).setMinWidth(75);
                    tablaMovimientos.getColumnModel().getColumn(i).setPreferredWidth(100);
                }
            }
            
        } else {
            // Para socios infantiles ocultar paneles no aplicables
            panelAportaciones.setVisible(false);
            panelPrestaciones.setVisible(false);
            panelIntereses.setVisible(false);
            panelAhorros.setVisible(true);
              // Ocultar columnas no aplicables en la tabla
            if (tablaMovimientos.getColumnCount() > 0) {
                // Ocultar columnas de aportaciones (1-3), prestaciones (4-6) e intereses (7)
                for (int i = 1; i <= 7; i++) {
                    tablaMovimientos.getColumnModel().getColumn(i).setMinWidth(0);
                    tablaMovimientos.getColumnModel().getColumn(i).setPreferredWidth(0);
                    tablaMovimientos.getColumnModel().getColumn(i).setWidth(0);
                    tablaMovimientos.getColumnModel().getColumn(i).setMaxWidth(0);
                }
            }
        }
        
        // Limpiar campos de entrada y recalcular total
        limpiarCamposEntrada();
        calcularTotalMovimiento();
        
        // Si ya había un socio cargado, buscar si existe el mismo número en el otro tipo
        if (socioActualId > 0) {
            buscarSocioPorId(socioActualId);
        }
    }
    
    /**
     * Cálcula los intereses según el número de semanas seleccionado
     */
    private void calcularInteresesPorSemanas() {
        try {
            // Obtener el número de semanas seleccionado
            int semanas = (Integer) cboSemanasInteres.getSelectedItem();
            
            // Calcular intereses por semanas (0.4% por semana)
            double interesCalculado = saldoPrestamos * TASA_INTERES_PRESTAMO * semanas;
            
            // Formatear y mostrar el interés calculado
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            lblInteresCalculado.setText(df.format(interesCalculado));
            txtInteresDeuda.setValue(interesCalculado);
            
            // Actualizar el total del movimiento
            calcularTotalMovimiento();
        } catch (Exception e) {
            System.err.println("Error al calcular intereses: " + e.getMessage());
        }
    }
    
    /**
     * Método para buscar un socio por su ID (desde la UI)
     */
    private void buscarSocio() {
        String idSocio = txtIdSocio.getText().trim();
        
        if (idSocio.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese un ID de socio válido", 
                "Campo vacío", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(idSocio);
            buscarSocioPorId(id);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El ID debe ser un número entero", 
                "ID inválido", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Busca un socio por su ID y tipo
     * @param id ID del socio a buscar
     */
    private void buscarSocioPorId(int id) {
        // Buscar socio en la base de datos
        Map<String, Object> datosFinancieros = socioDAO.obtenerDatosFinancierosSocio(id, !esSocioAdulto);
        Map<String, Object> datosSocio = obtenerDatosSocio(id);
        
        if (datosSocio == null) {
            JOptionPane.showMessageDialog(this, 
                "No se encontró un socio " + (esSocioAdulto ? "adulto" : "infantil") + 
                " con ID " + id, 
                "Socio no encontrado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Guardar el ID del socio actual
        socioActualId = id;
        
        // Actualizar datos del socio en la UI
        lblNombre.setText(datosSocio.get("Nombres").toString());
        lblApellidos.setText(datosSocio.get("Apellidos").toString());          // Actualizar saldos
        saldoAportaciones = getDoubleValue(datosFinancieros, "AporSaldo");
        saldoPrestamos = getDoubleValue(datosFinancieros, "PresSaldo");
        saldoAhorros = getDoubleValue(datosFinancieros, "AhoSaldo");
        
        // Actualizar etiquetas de saldo
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        lblSaldoAportaciones.setText(df.format(saldoAportaciones));
        lblSaldoPrestamos.setText(df.format(saldoPrestamos));
        lblSaldoAhorros.setText(df.format(saldoAhorros));
        
        // Actualizar también el campo de saldo en el panel de ahorros
        if (txtSaldo != null) {
            txtSaldo.setText(df.format(saldoAhorros));
            txtSaldo.setForeground(Color.BLACK);
        }
        
        // Restablecer colores
        lblSaldoAportaciones.setForeground(Color.BLACK);
        lblSaldoPrestamos.setForeground(Color.BLACK);
        lblSaldoAhorros.setForeground(Color.BLACK);
          // Calcular intereses de préstamo según semanas seleccionadas (si existe el combobox)
        if (cboSemanasInteres != null) {
            calcularInteresesPorSemanas();
        } else {
            // Cálculo anterior por si todavía no existe el combo
            double interesCalculado = saldoPrestamos * TASA_INTERES_PRESTAMO;
            lblInteresCalculado.setText(df.format(interesCalculado));
            txtInteresDeuda.setValue(interesCalculado);
        }
        
        // Cargar movimientos recientes
        cargarMovimientosRecientes(id, datosFinancieros);
        
        // Habilitar campos y botones
        habilitarControles(true);
    }
    
    /**
     * Obtiene los datos básicos de un socio
     * @param id ID del socio
     * @return Mapa con los datos del socio o null si no existe
     */
    private Map<String, Object> obtenerDatosSocio(int id) {
        if (esSocioAdulto) {
            return socioDAO.buscarSocioAdultoPorID(id);
        } else {
            return socioDAO.buscarSocioInfantilPorID(id);
        }
    }
    
    /**
     * Obtiene un valor double de un Map
     * @param map Mapa de datos
     * @param key Clave del valor a obtener
     * @return Valor como double (0.0 si no existe o es null)
     */
    private double getDoubleValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) {
            return 0.0;
        }
        
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * Carga los movimientos recientes para el socio desde la base de datos
     * @param idSocio ID del socio
     * @param datosFinancieros Datos financieros del socio
     */
    @SuppressWarnings("unchecked")
    private void cargarMovimientosRecientes(int idSocio, Map<String, Object> datosFinancieros) {
        // Limpiar tabla actual
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        
        // Verificar que existan movimientos
        if (datosFinancieros == null || !datosFinancieros.containsKey("Movimientos")) {
            return;
        }
        
        List<Map<String, Object>> movimientos = (List<Map<String, Object>>) datosFinancieros.get("Movimientos");
        if (movimientos == null || movimientos.isEmpty()) {
            return;
        }
        
        // Formato para fechas y montos
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("$#,##0.00");        // Añadir todas las filas a la tabla (sin limitar a 5 movimientos)
        for (Map<String, Object> movimiento : movimientos) {
            // Verificar y obtener la fecha del movimiento
            Date fecha = (Date) movimiento.get("Fecha");
            
            // Obtener el ID del movimiento (intentar IdMov primero, luego ID)
            Object idMovimiento = movimiento.get("IdMov");
            if (idMovimiento == null) {
                idMovimiento = movimiento.get("ID");
                System.out.println("Usando ID alternativo: " + idMovimiento);
            }
            
            // Si se encontró, agregar a la tabla
            if (idMovimiento != null) {
                // Formatear la fecha completa (o proporcionar un valor por defecto si es nula)
                String fechaFormateada;
                if (fecha != null) {
                    fechaFormateada = sdf.format(fecha);
                } else {
                    fechaFormateada = "Sin fecha";
                    System.out.println("Movimiento ID " + idMovimiento + " sin fecha, usando valor por defecto");
                }
                
                // Calcular los valores para cada categoría
                double aporIngresos = getDoubleValue(movimiento, "AporIngresos");
                double aporEgresos = getDoubleValue(movimiento, "AporEgresos");
                double aporSaldo = getDoubleValue(movimiento, "AporSaldo");
                
                double presEgresos = getDoubleValue(movimiento, "PresEgresos");
                double presIngresos = getDoubleValue(movimiento, "PresIngresos");
                double presSaldo = getDoubleValue(movimiento, "PresSaldo");
                  double ahoIngresos = getDoubleValue(movimiento, "AhoIngresos");
                double ahoEgresos = getDoubleValue(movimiento, "AhoEgresos");
                double ahoSaldo = getDoubleValue(movimiento, "AhoSaldo");
                  // Añadir la variable intereses que faltaba
                double intereses = getDoubleValue(movimiento, "Intereses");
                
                Object[] fila = new Object[] {
                    fechaFormateada,            // Fecha completa
                    df.format(aporIngresos),    // Aportaciones - Ingresos
                    df.format(aporEgresos),     // Aportaciones - Egresos
                    df.format(aporSaldo),       // Aportaciones - Saldo
                    df.format(presEgresos),     // Prestaciones - Egresos
                    df.format(presIngresos),    // Prestaciones - Ingresos (Abonos)
                    df.format(presSaldo),       // Prestaciones - Saldo
                    df.format(intereses),       // Intereses
                    df.format(ahoIngresos),     // Ahorros - Ingresos
                    df.format(ahoEgresos),      // Ahorros - Egresos
                    df.format(ahoSaldo),        // Ahorros - Saldo
                    idMovimiento                // ID Movimiento
                };
                
                modeloTabla.addRow(fila);                System.out.println("Añadido movimiento ID: " + idMovimiento + ", Fecha: " + sdf.format(fecha) + 
                    ", Aportaciones: " + df.format(aporIngresos) + "/" + df.format(aporEgresos) + "/" + df.format(aporSaldo) + 
                    ", Prestaciones: " + df.format(presEgresos) + "/" + df.format(presIngresos) + "/" + df.format(presSaldo) +
                    ", Ahorros: " + df.format(ahoIngresos) + "/" + df.format(ahoEgresos) + "/" + df.format(ahoSaldo) +
                    ", Intereses: " + df.format(intereses));
            } else {
                System.err.println("Movimiento sin ID encontrado para socio #" + idSocio + " con fecha " + sdf.format(fecha));
                // Mostrar todos los campos para depuración
                for (Map.Entry<String, Object> entry : movimiento.entrySet()) {
                    System.err.println("  " + entry.getKey() + " = " + entry.getValue());
                }
            }
        }
    }
    
    /**
     * Habilita o deshabilita los controles según corresponda
     * @param habilitar true para habilitar, false para deshabilitar
     */
    private void habilitarControles(boolean habilitar) {
        // Habilitar o deshabilitar paneles de entrada
        Component[] componentesAportaciones = panelAportaciones.getComponents();
        for (Component c : componentesAportaciones) {
            if (c instanceof JPanel) {
                Component[] subComponents = ((JPanel)c).getComponents();
                for (Component sc : subComponents) {
                    if (sc instanceof JFormattedTextField) {
                        sc.setEnabled(habilitar && esSocioAdulto);
                    }
                }
            }
        }
        
        Component[] componentesPrestaciones = panelPrestaciones.getComponents();
        for (Component c : componentesPrestaciones) {
            if (c instanceof JPanel) {
                Component[] subComponents = ((JPanel)c).getComponents();
                for (Component sc : subComponents) {
                    if (sc instanceof JFormattedTextField) {
                        sc.setEnabled(habilitar && esSocioAdulto);
                    }
                }
            }
        }
        
        Component[] componentesAhorros = panelAhorros.getComponents();
        for (Component c : componentesAhorros) {
            if (c instanceof JPanel) {
                Component[] subComponents = ((JPanel)c).getComponents();
                for (Component sc : subComponents) {
                    if (sc instanceof JFormattedTextField) {
                        sc.setEnabled(habilitar);
                    }
                }
            }
        }
        
        // El campo de interés de deuda siempre debe ser de solo lectura
        txtInteresDeuda.setEnabled(false);
        
        // Habilitar o deshabilitar controles del auxiliar de cobro
        txtCantidadRecibida.setEnabled(habilitar);
        
        // Habilitar o deshabilitar botón Aplicar
        btnAplicar.setEnabled(habilitar);
    }
      /**
     * Limpia los campos de entrada (pero no los datos del socio)
     */    private void limpiarCamposEntrada() {
        // Limpiar campos de aportaciones (establecer a null para no mostrar 0.00)
        txtAportacionDeposito.setValue(null);
        txtAportacionRetiro.setValue(null);
        
        // Limpiar campos de prestaciones
        txtPrestamoDeposito.setValue(null);
        txtPrestamoRetiro.setValue(null);
        
        // Limpiar campos de ahorros
        txtAhorroDeposito.setValue(null);
        txtAhorroRetiro.setValue(null);
        
        // Si estamos saliendo de modo de actualización, recalcular los intereses según el saldo actual
        if (modoActualizacion) {
            // Recalcular el interés según el saldo actual
            double interesCalculado = saldoPrestamos * TASA_INTERES_PRESTAMO;
            if (cboSemanasInteres != null) {
                interesCalculado *= ((Integer) cboSemanasInteres.getSelectedItem()).intValue();
            }
            
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            lblInteresCalculado.setText(df.format(interesCalculado));
            txtInteresDeuda.setValue(interesCalculado);        } else {
            // En modo normal, limpiar también intereses
            txtInteresDeuda.setValue(null);
        }
        
        // Limpiar campos del auxiliar de cobro
        txtTotalMovimiento.setValue(0.0); // Sí mantener 0.0 para el total del movimiento
        txtCantidadRecibida.setValue(null);
        lblCambio.setText("$0.00");
    }
    
    /**
     * Limpia todo el formulario incluyendo los datos del socio
     */
    private void limpiarFormulario() {
        // Limpiar campos de entrada
        limpiarCamposEntrada();
          // Limpiar datos del socio
        txtIdSocio.setText("");
        lblNombre.setText("-");
        lblApellidos.setText("-");
          // Restablecer etiquetas de saldo con color original
        lblSaldoAportaciones.setText("$0.00");
        lblSaldoPrestamos.setText("$0.00");
        lblSaldoAhorros.setText("$0.00");
        lblInteresCalculado.setText("$0.00");
        
        // Restablecer valor del campo de saldo en el panel de ahorros
        if (txtSaldo != null) {
            txtSaldo.setText("$0.00");
            txtSaldo.setForeground(Color.BLACK);
        }
        
        // Restablecer colores
        lblSaldoAportaciones.setForeground(Color.BLACK);
        lblSaldoPrestamos.setForeground(Color.BLACK);
        lblSaldoAhorros.setForeground(Color.BLACK);
        
        // Limpiar tabla de movimientos
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        
        // Deshabilitar controles
        habilitarControles(false);
        
        // Restablecer saldos y ID de socio actual
        saldoAportaciones = 0.0;
        saldoPrestamos = 0.0;
        saldoAhorros = 0.0;
        socioActualId = 0;
    }
    
    /**
     * Obtiene el valor numérico de un JFormattedTextField manejando valores nulos
     * @param campo El campo JFormattedTextField
     * @return El valor numérico o 0.0 si es nulo
     */
    private double obtenerValorNumerico(JFormattedTextField campo) {
        if (campo.getValue() == null) {
            return 0.0;
        }
        return ((Number)campo.getValue()).doubleValue();
    }
    
    /**
     * Calcula el total del movimiento basado en los valores ingresados
     */    private void calcularTotalMovimiento() {
        double total = 0.0;
        
        // Obtener valores de los campos con manejo de nulos
        double aportacionDeposito = obtenerValorNumerico(txtAportacionDeposito);
        double aportacionRetiro = obtenerValorNumerico(txtAportacionRetiro);
        double prestamoDeposito = obtenerValorNumerico(txtPrestamoDeposito);
        double prestamoRetiro = obtenerValorNumerico(txtPrestamoRetiro);
        double ahorroDeposito = obtenerValorNumerico(txtAhorroDeposito);
        double ahorroRetiro = obtenerValorNumerico(txtAhorroRetiro);
        double interesDeuda = obtenerValorNumerico(txtInteresDeuda);
        
        // Sumar depósitos
        total += aportacionDeposito;
        total += prestamoDeposito;
        total += ahorroDeposito;
        total += interesDeuda;
        
        // Restar retiros
        total -= aportacionRetiro;
        total -= prestamoRetiro;
        total -= ahorroRetiro;
        
        // Actualizar campo de total
        txtTotalMovimiento.setValue(total);
        
        // Recalcular cambio
        calcularCambio();
    }
    
    /**
     * Calcula el cambio a dar al socio
     */
    private void calcularCambio() {        try {
            double total = obtenerValorNumerico(txtTotalMovimiento);
            double recibido = obtenerValorNumerico(txtCantidadRecibida);
            double cambio = 0.0;
            
            if (total > 0) {
                // Si el total es positivo, el socio debe pagar
                cambio = recibido - total;
                if (cambio < 0) {
                    lblCambio.setForeground(Color.RED);
                } else {
                    lblCambio.setForeground(Color.BLACK);
                }
            } else if (total < 0) {
                // Si el total es negativo, hay que entregarle al socio
                cambio = Math.abs(total); // El cambio es el valor absoluto del total
                lblCambio.setForeground(Color.GREEN.darker());
            } else {
                // Si el total es cero, no hay cambio
                cambio = 0.0;
                lblCambio.setForeground(Color.BLACK);
            }
            
            // Formatear y mostrar el cambio
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            lblCambio.setText(df.format(cambio));
            
        } catch (Exception e) {
            lblCambio.setText("$0.00");
            lblCambio.setForeground(Color.BLACK);
        }
    }
      /**
     * Aplica el movimiento actual y actualiza saldos
     */    private void aplicarMovimiento() {
        // Verificar si estamos en modo actualización o creación de nuevo movimiento
        if (modoActualizacion && movimientoSeleccionadoId > 0) {
            actualizarMovimiento();
            return;
        }
        
        // A partir de aquí es el flujo para un nuevo movimiento
        
        // Validar que los campos tengan valores válidos
        if (!validarCampos()) {
            return;
        }
        
        // Verificar que haya un socio activo
        if (socioActualId <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un socio primero", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
          // Obtener valores de los campos con manejo de nulos
        double aportacionDeposito = obtenerValorNumerico(txtAportacionDeposito);
        double aportacionRetiro = obtenerValorNumerico(txtAportacionRetiro);
        double prestamoDeposito = obtenerValorNumerico(txtPrestamoDeposito);
        double prestamoRetiro = obtenerValorNumerico(txtPrestamoRetiro);
        double ahorroDeposito = obtenerValorNumerico(txtAhorroDeposito);
        double ahorroRetiro = obtenerValorNumerico(txtAhorroRetiro);
        double interesDeuda = obtenerValorNumerico(txtInteresDeuda);
        
        // Guardar movimiento en la base de datos según el tipo de socio
        boolean exito;
        int nuevoMovimientoId = 0;
        
        if (esSocioAdulto) {
            // Para socios adultos, registrar todos los campos            // Recalcular interés para registrarlo correctamente
            double interesCalculado = saldoPrestamos * TASA_INTERES_PRESTAMO;
            if (cboSemanasInteres.isVisible() && cboSemanasInteres.getSelectedItem() != null) {
                interesCalculado *= ((Integer) cboSemanasInteres.getSelectedItem()).intValue();
            }
            
            nuevoMovimientoId = socioDAO.registrarMovimientoSocio(
                socioActualId, !esSocioAdulto,
                aportacionDeposito, aportacionRetiro,
                prestamoDeposito, prestamoRetiro,
                ahorroDeposito, ahorroRetiro,
                interesDeuda, interesCalculado
            );
            exito = (nuevoMovimientoId > 0);
        } else {
            // Para socios infantiles, solo registrar operaciones de ahorro
            nuevoMovimientoId = socioDAO.registrarMovimientoSocioInfantil(
                socioActualId,
                ahorroDeposito, ahorroRetiro
            );
            exito = (nuevoMovimientoId > 0);
        }
        
        if (!exito) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar el movimiento en la base de datos", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }          // Actualizar saldos locales
        if (esSocioAdulto) {
            saldoAportaciones += aportacionDeposito - aportacionRetiro;
            saldoPrestamos -= prestamoDeposito - prestamoRetiro;
        }
        saldoAhorros += ahorroDeposito - ahorroRetiro;
        
        // Actualizar etiquetas de saldo
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        lblSaldoAportaciones.setText(df.format(saldoAportaciones));
        lblSaldoPrestamos.setText(df.format(saldoPrestamos));
        lblSaldoAhorros.setText(df.format(saldoAhorros));
        
        // Actualizar también el campo de saldo en el panel de ahorros
        if (txtSaldo != null) {
            txtSaldo.setText(df.format(saldoAhorros));
        }
        
        // Restablecer colores
        lblSaldoAportaciones.setForeground(Color.BLACK);
        lblSaldoPrestamos.setForeground(Color.BLACK);
        lblSaldoAhorros.setForeground(Color.BLACK);
          // Recalcular intereses de préstamo
        double interesCalculado = saldoPrestamos * TASA_INTERES_PRESTAMO;
        lblInteresCalculado.setText(df.format(interesCalculado));
        txtInteresDeuda.setValue(interesCalculado);            // Añadir el movimiento a la tabla
        // Usamos fecha sin hora para mostrar consistentemente con lo que se guarda en la BD
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Crear una fecha sin componente de hora
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date fechaActual = cal.getTime();
        
        String fechaFormateada = sdf.format(fechaActual);
        
        Object[] fila = new Object[] {
            fechaFormateada,                                                  // Fecha formateada
            esSocioAdulto ? df.format(aportacionDeposito) : df.format(0),     // Aportaciones - Ingresos
            esSocioAdulto ? df.format(aportacionRetiro) : df.format(0),       // Aportaciones - Egresos
            df.format(saldoAportaciones),                                     // Aportaciones - Saldo
            esSocioAdulto ? df.format(prestamoRetiro) : df.format(0),         // Prestaciones - Egresos
            esSocioAdulto ? df.format(prestamoDeposito) : df.format(0),       // Prestaciones - Ingresos
            df.format(saldoPrestamos),                                        // Prestaciones - Saldo
            esSocioAdulto ? df.format(interesDeuda) : df.format(0),           // Intereses
            df.format(ahorroDeposito),                                        // Ahorros - Ingresos
            df.format(ahorroRetiro),                                          // Ahorros - Egresos
            df.format(saldoAhorros),                                          // Ahorros - Saldo
            nuevoMovimientoId                                                 // ID Movimiento (oculto)
        };
          // Insertar al inicio de la tabla
        modeloTabla.insertRow(0, fila);
        
        // Limpiar campos de entrada
        limpiarCamposEntrada();
        
        // Mensaje de éxito
        JOptionPane.showMessageDialog(this, 
            "Movimiento aplicado correctamente", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Valida que los campos tengan valores válidos
     * @return true si los campos son válidos, false en caso contrario
     */    private boolean validarCampos() {
        try {
            // Obtener valores de todos los campos con manejo de nulos
            double aportacionDeposito = obtenerValorNumerico(txtAportacionDeposito);
            double aportacionRetiro = obtenerValorNumerico(txtAportacionRetiro);
            double prestamoDeposito = obtenerValorNumerico(txtPrestamoDeposito);
            double prestamoRetiro = obtenerValorNumerico(txtPrestamoRetiro);
            double ahorroDeposito = obtenerValorNumerico(txtAhorroDeposito);
            double ahorroRetiro = obtenerValorNumerico(txtAhorroRetiro);
            
            // Validar que haya al menos un movimiento
            if (aportacionDeposito == 0 && aportacionRetiro == 0 && 
                prestamoDeposito == 0 && prestamoRetiro == 0 && 
                ahorroDeposito == 0 && ahorroRetiro == 0) {
                JOptionPane.showMessageDialog(this,
                    "Debe ingresar al menos un movimiento para continuar",
                    "Sin movimientos", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Para socios infantiles, solo se permiten operaciones de ahorro
            if (!esSocioAdulto) {
                if (aportacionDeposito > 0 || aportacionRetiro > 0 || 
                    prestamoDeposito > 0 || prestamoRetiro > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Los socios infantiles solo pueden realizar operaciones de ahorro", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                // En modo actualización, debemos validar considerando el movimiento original
                if (!modoActualizacion || movimientoSeleccionadoId <= 0) {
                    // Validar que no se intente retirar más de lo que hay en cada cuenta
                    if (aportacionRetiro > saldoAportaciones) {
                        JOptionPane.showMessageDialog(this, 
                            "No puede retirar más de lo que tiene en aportaciones", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    
                    // Verificar que no se supere el límite de aportación de 300
                    if (saldoAportaciones >= 300 && aportacionDeposito > 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Ya ha completado la aportación máxima de $300. No puede realizar más aportaciones.", 
                            "Límite de aportación alcanzado", 
                            JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                    
                    // Verificar que la nueva aportación no exceda el límite de 300
                    if (saldoAportaciones + aportacionDeposito > 300) {
                        double aportacionPermitida = 300 - saldoAportaciones;
                        JOptionPane.showMessageDialog(this, 
                            "La aportación máxima permitida es de $300. Solo puede aportar $" + aportacionPermitida + " más.", 
                            "Excede límite de aportación", 
                            JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                } else {
                    // En modo de actualización, obtenemos el movimiento original para calcular diferencias
                    Map<String, Object> movimientoOriginal = socioDAO.obtenerMovimientoPorId(movimientoSeleccionadoId);
                    if (movimientoOriginal != null) {
                        double aportacionDepositoOriginal = getDoubleValue(movimientoOriginal, "AporIngresos");
                        double aportacionRetiroOriginal = getDoubleValue(movimientoOriginal, "AporEgresos");
                        
                        double diferenciaAportacion = (aportacionDeposito - aportacionDepositoOriginal) - 
                                                     (aportacionRetiro - aportacionRetiroOriginal);
                          // Verificar límite de aportación en base a la diferencia
                        if (diferenciaAportacion > 0 && saldoAportaciones + diferenciaAportacion > 300) {
                            JOptionPane.showMessageDialog(this, 
                                "La aportación máxima permitida es de $300. Su actualización excede este límite.", 
                                "Excede límite de aportación", 
                                JOptionPane.WARNING_MESSAGE);
                            return false;
                        }
                    }
                }
            }
            
            // Validaciones comunes para ambos tipos de socios
            if (!modoActualizacion || movimientoSeleccionadoId <= 0) {
                if (ahorroRetiro > saldoAhorros) {
                    JOptionPane.showMessageDialog(this, 
                        "No puede retirar más de lo que tiene en ahorros", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                // En modo de actualización, obtenemos el movimiento original para calcular diferencias
                Map<String, Object> movimientoOriginal = socioDAO.obtenerMovimientoPorId(movimientoSeleccionadoId);
                if (movimientoOriginal != null) {
                    double ahorroDepositoOriginal = getDoubleValue(movimientoOriginal, "AhoIngresos");
                    double ahorroRetiroOriginal = getDoubleValue(movimientoOriginal, "AhoEgresos");
                    
                    double diferenciaAhorro = (ahorroDeposito - ahorroDepositoOriginal) - 
                                             (ahorroRetiro - ahorroRetiroOriginal);
                    
                    // Si la diferencia es negativa (más retiros), verificar que no supere el saldo
                    if (diferenciaAhorro < 0 && Math.abs(diferenciaAhorro) > saldoAhorros) {
                        JOptionPane.showMessageDialog(this, 
                            "No puede retirar más de lo que tiene en ahorros", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
              // Verificar la cantidad recibida
            double total = obtenerValorNumerico(txtTotalMovimiento);
            double recibido = obtenerValorNumerico(txtCantidadRecibida);
            
            if (total > 0 && recibido < total) {
                JOptionPane.showMessageDialog(this, 
                    "La cantidad recibida debe ser igual o mayor al total del movimiento", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al validar campos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
      /**
     * Valida en tiempo real si la aportación supera el límite de $300
     */
    private void validarLimiteAportacion() {
        if (esSocioAdulto && socioActualId > 0) {
            try {
                double aportacionDeposito = obtenerValorNumerico(txtAportacionDeposito);
                
                // Si estamos en modo actualización, necesitamos considerar el movimiento original
                if (modoActualizacion && movimientoSeleccionadoId > 0) {
                    Map<String, Object> movimientoOriginal = socioDAO.obtenerMovimientoPorId(movimientoSeleccionadoId);
                    if (movimientoOriginal != null) {
                        double aportacionDepositoOriginal = getDoubleValue(movimientoOriginal, "AporIngresos");
                        
                        // Solo validar la diferencia entre el nuevo valor y el original
                        double diferencia = aportacionDeposito - aportacionDepositoOriginal;
                        
                        // Si no hay diferencia o es negativa (reducción), no hay problema
                        if (diferencia <= 0) {
                            return;
                        }
                        
                        // Verificar si ya se alcanzó el límite
                        if (saldoAportaciones >= 300) {
                            JOptionPane.showMessageDialog(this, 
                                "Ya ha completado la aportación máxima de $300. No puede realizar más aportaciones.", 
                                "Límite de aportación alcanzado", 
                                JOptionPane.WARNING_MESSAGE);
                            txtAportacionDeposito.setValue(aportacionDepositoOriginal);
                            return;
                        }
                        
                        // Verificar si la nueva aportación excede el límite
                        if (saldoAportaciones + diferencia > 300) {
                            double aportacionPermitida = 300 - saldoAportaciones + aportacionDepositoOriginal;
                            JOptionPane.showMessageDialog(this, 
                                "La aportación máxima permitida es de $300. Solo puede modificar hasta $" + aportacionPermitida + ".", 
                                "Excede límite de aportación", 
                                JOptionPane.WARNING_MESSAGE);
                            // Ajustar automáticamente al valor máximo permitido
                            txtAportacionDeposito.setValue(aportacionPermitida);
                        }
                        
                        return;
                    }
                }
                
                // Modo normal (nuevo movimiento)
                
                // Verificar si ya se alcanzó el límite
                if (saldoAportaciones >= 300) {
                    if (aportacionDeposito > 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Ya ha completado la aportación máxima de $300. No puede realizar más aportaciones.", 
                            "Límite de aportación alcanzado", 
                            JOptionPane.WARNING_MESSAGE);
                        txtAportacionDeposito.setValue(0.0);
                    }
                    return;
                }
                
                // Verificar si la aportación excede el límite
                if (saldoAportaciones + aportacionDeposito > 300) {
                    double aportacionPermitida = 300 - saldoAportaciones;
                    JOptionPane.showMessageDialog(this, 
                        "La aportación máxima permitida es de $300. Solo puede aportar $" + aportacionPermitida + " más.", 
                        "Excede límite de aportación", 
                        JOptionPane.WARNING_MESSAGE);
                    // Ajustar automáticamente al valor máximo permitido
                    txtAportacionDeposito.setValue(aportacionPermitida);
                }
                
            } catch (Exception e) {
                System.err.println("Error al validar límite de aportación: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
      /**
     * Actualiza los saldos de manera temporal mientras el usuario ingresa valores
     */
    private void actualizarSaldosTemporal() {
        if (socioActualId <= 0) {
            return; // No hay socio activo
        }
        
        try {            // Obtener valores actuales de los campos con manejo de nulos
            double aportacionDeposito = obtenerValorNumerico(txtAportacionDeposito);
            double aportacionRetiro = obtenerValorNumerico(txtAportacionRetiro);
            double prestamoDeposito = obtenerValorNumerico(txtPrestamoDeposito);
            double prestamoRetiro = obtenerValorNumerico(txtPrestamoRetiro);
            double ahorroDeposito = obtenerValorNumerico(txtAhorroDeposito);
            double ahorroRetiro = obtenerValorNumerico(txtAhorroRetiro);
            
            // Calcular saldos temporales
            double saldoAportacionesTemp = saldoAportaciones + aportacionDeposito - aportacionRetiro;
            double saldoPrestamosTemp = saldoPrestamos - prestamoDeposito + prestamoRetiro;
            double saldoAhorrosTemp = saldoAhorros + ahorroDeposito - ahorroRetiro;
            
            // Actualizar etiquetas con saldos temporales
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            
            // Actualizar etiquetas con color según los cambios
            if (saldoAportacionesTemp > saldoAportaciones) {
                lblSaldoAportaciones.setForeground(Color.GREEN.darker());
            } else if (saldoAportacionesTemp < saldoAportaciones) {
                lblSaldoAportaciones.setForeground(Color.RED);
            } else {
                lblSaldoAportaciones.setForeground(Color.BLACK);
            }
            
            if (saldoPrestamosTemp < saldoPrestamos) {
                lblSaldoPrestamos.setForeground(Color.GREEN.darker());
            } else if (saldoPrestamosTemp > saldoPrestamos) {
                lblSaldoPrestamos.setForeground(Color.RED);
                       } else {
                lblSaldoPrestamos.setForeground(Color.BLACK);
            }
            
            if (saldoAhorrosTemp > saldoAhorros) {
                lblSaldoAhorros.setForeground(Color.GREEN.darker());
            } else if (saldoAhorrosTemp < saldoAhorros) {
                lblSaldoAhorros.setForeground(Color.RED);
            } else {
                lblSaldoAhorros.setForeground(Color.BLACK);
            }
              // Mostrar los nuevos saldos
            lblSaldoAportaciones.setText(df.format(saldoAportacionesTemp) + " *");
            lblSaldoPrestamos.setText(df.format(saldoPrestamosTemp) + " *");
            lblSaldoAhorros.setText(df.format(saldoAhorrosTemp) + " *");
            
            // Actualizar también el campo de saldo en el panel de ahorros
            if (txtSaldo != null) {
                txtSaldo.setText(df.format(saldoAhorrosTemp) + " *");
                txtSaldo.setForeground(lblSaldoAhorros.getForeground());
            }
            
        } catch (Exception e) {
            System.err.println("Error al actualizar saldos temporales: " + e.getMessage());
        }
    }
      /**
     * Valida que no se intente retirar más de lo disponible
     */
    private void validarRetiros() {
        if (socioActualId <= 0) {
            return; // No hay socio activo
        }
        
        try {
            // Si estamos en modo actualización, necesitamos considerar el movimiento original
            if (modoActualizacion && movimientoSeleccionadoId > 0) {
                Map<String, Object> movimientoOriginal = socioDAO.obtenerMovimientoPorId(movimientoSeleccionadoId);
                if (movimientoOriginal != null) {                    // Verificar retiro de aportaciones considerando el movimiento original
                    double aportacionRetiro = obtenerValorNumerico(txtAportacionRetiro);
                    double aportacionRetiroOriginal = getDoubleValue(movimientoOriginal, "AporEgresos");
                    
                    // Solo validar si el nuevo retiro es mayor que el original
                    if (aportacionRetiro > aportacionRetiroOriginal) {

                        double diferenciaRetiro = aportacionRetiro - aportacionRetiroOriginal;
                        if (diferenciaRetiro > saldoAportaciones) {
                            JOptionPane.showMessageDialog(this, 
                                "No puede retirar más de lo que tiene en aportaciones ($" + saldoAportaciones + ")", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                            txtAportacionRetiro.setValue(aportacionRetiroOriginal);
                            return;
                        }
                    }
                      // Verificar retiro de ahorros considerando el movimiento original
                    double ahorroRetiro = obtenerValorNumerico(txtAhorroRetiro);
                    double ahorroRetiroOriginal = getDoubleValue(movimientoOriginal, "AhoEgresos");
                    
                    // Solo validar si el nuevo retiro es mayor que el original
                    if (ahorroRetiro > ahorroRetiroOriginal) {
                        double diferenciaRetiro = ahorroRetiro - ahorroRetiroOriginal;
                        if (diferenciaRetiro > saldoAhorros) {
                            JOptionPane.showMessageDialog(this, 
                                "No puede retirar más de lo que tiene en ahorros ($" + saldoAhorros + ")", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                            txtAhorroRetiro.setValue(ahorroRetiroOriginal);
                            return;
                        }
                    }
                }
            } else {
                // Modo normal (nuevo movimiento)
                  // Verificar retiro de aportaciones
                double aportacionRetiro = obtenerValorNumerico(txtAportacionRetiro);
                if (aportacionRetiro > saldoAportaciones) {
                    JOptionPane.showMessageDialog(this, 
                        "No puede retirar más de lo que tiene en aportaciones ($" + saldoAportaciones + ")", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    txtAportacionRetiro.setValue(0.0);
                    return;
                }
                  // Verificar retiro de ahorros
                double ahorroRetiro = obtenerValorNumerico(txtAhorroRetiro);
                if (ahorroRetiro > saldoAhorros) {
                    JOptionPane.showMessageDialog(this, 
                        "No puede retirar más de lo que tiene en ahorros ($" + saldoAhorros + ")", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    txtAhorroRetiro.setValue(0.0);
                    return;
                }
            }
            
            // No necesitamos validar el préstamo ya que es un monto negativo
            // (se puede retirar cualquier monto como préstamo)
            
        } catch (Exception e) {
            System.err.println("Error al validar retiros: " + e.getMessage());
            e.printStackTrace();
        }
    }    /**
     * Maneja la selección de un movimiento de la tabla
     * @param fila Índice de la fila seleccionada
     */
    private void seleccionarMovimiento(int fila) {
        try {
            // Verificar si ya estamos en modo actualización
            if (modoActualizacion) {
                // Si ya estamos editando un movimiento, preguntar si quiere cambiar
                int respuesta = JOptionPane.showConfirmDialog(this,
                    "Ya está editando un movimiento. ¿Desea cambiar al movimiento seleccionado?",
                    "Confirmar cambio", JOptionPane.YES_NO_OPTION);
                
                if (respuesta != JOptionPane.YES_OPTION) {
                    // Si no quiere cambiar, desseleccionar la nueva fila y mantener la edición actual
                    tablaMovimientos.clearSelection();
                    return;
                }
                
                // Si quiere cambiar, cancelar la edición actual
                cancelarActualizacion();
            }
            
            // Obtener el ID del movimiento seleccionado (columna 11 - última columna que está oculta)
            Object idObj = tablaMovimientos.getValueAt(fila, 11);
            if (idObj != null) {
                int idMovimiento;
                try {
                    idMovimiento = Integer.parseInt(idObj.toString());
                    System.out.println("Seleccionado movimiento con ID: " + idMovimiento);
                } catch (NumberFormatException e) {
                    System.err.println("Error al convertir ID de movimiento: " + idObj);
                    JOptionPane.showMessageDialog(this, 
                        "Error al obtener ID del movimiento seleccionado", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Guardar el ID para actualización posterior
                movimientoSeleccionadoId = idMovimiento;
                
                // Cargar los datos del movimiento
                cargarDatosMovimiento(idMovimiento);
                
                // Cambiar a modo actualización
                modoActualizacion = true;
                
                // Cambiar el texto del botón para indicar que es una actualización
                btnAplicar.setText("Actualizar Movimiento");
                btnAplicar.setFont(new Font("Arial", Font.BOLD, 14));
                btnAplicar.setPreferredSize(new Dimension(250, 35));

                
                // Mostrar el botón para cancelar actualización
                btnCancelarActualizacion.setVisible(true);
            }
        } catch (Exception e) {
            System.err.println("Error al seleccionar movimiento: " + e.getMessage());
            e.printStackTrace();
            
            // En caso de error, asegurarnos de que no quedamos en un estado inconsistente
            cancelarActualizacion();
        }
    }
      /**
     * Carga los datos de un movimiento en los campos del formulario
     * @param idMovimiento ID del movimiento a cargar
     */
    private void cargarDatosMovimiento(int idMovimiento) {
        try {
            // Obtener los datos del movimiento de la base de datos
            Map<String, Object> datosMovimiento = socioDAO.obtenerMovimientoPorId(idMovimiento);
            
            if (datosMovimiento != null) {
                // Cargar valores en los campos del formulario
                double aportacionDeposito = getDoubleValue(datosMovimiento, "AporIngresos");
                double aportacionRetiro = getDoubleValue(datosMovimiento, "AporEgresos");
                double prestamoDeposito = getDoubleValue(datosMovimiento, "PresIngresos");
                double prestamoRetiro = getDoubleValue(datosMovimiento, "PresEgresos");
                double ahorroDeposito = getDoubleValue(datosMovimiento, "AhoIngresos");
                double ahorroRetiro = getDoubleValue(datosMovimiento, "AhoEgresos");
                double interesDeuda = getDoubleValue(datosMovimiento, "RetInteres");
                
                // Establecer valores en los campos
                txtAportacionDeposito.setValue(aportacionDeposito);
                txtAportacionRetiro.setValue(aportacionRetiro);
                txtPrestamoDeposito.setValue(prestamoDeposito);
                txtPrestamoRetiro.setValue(prestamoRetiro);
                txtAhorroDeposito.setValue(ahorroDeposito);
                txtAhorroRetiro.setValue(ahorroRetiro);
                txtInteresDeuda.setValue(interesDeuda);
                
                // Recalcular total del movimiento
                calcularTotalMovimiento();
                
                // Actualizar las etiquetas de saldo para reflejar la edición
                actualizarSaldosTemporal();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los datos del movimiento: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza un movimiento existente en la base de datos
     */    private void actualizarMovimiento() {
        try {
            // Verificar que estamos en modo actualización y tenemos un ID válido
            if (!modoActualizacion || movimientoSeleccionadoId <= 0) {
                JOptionPane.showMessageDialog(this,
                    "No se ha seleccionado ningún movimiento para actualizar",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar campos antes de actualizar
            if (!validarCampos()) {
                return;
            }
              // Obtener valores actuales del formulario con manejo de nulos
            double aportacionDeposito = obtenerValorNumerico(txtAportacionDeposito);
            double aportacionRetiro = obtenerValorNumerico(txtAportacionRetiro);
            double prestamoDeposito = obtenerValorNumerico(txtPrestamoDeposito);
            double prestamoRetiro = obtenerValorNumerico(txtPrestamoRetiro);
            double ahorroDeposito = obtenerValorNumerico(txtAhorroDeposito);
            double ahorroRetiro = obtenerValorNumerico(txtAhorroRetiro);
            double interesDeuda = obtenerValorNumerico(txtInteresDeuda);
            
            // Actualizar el movimiento en la base de datos
            boolean exito = socioDAO.actualizarMovimiento(
                movimientoSeleccionadoId,
                socioActualId,
                aportacionDeposito, aportacionRetiro,
                prestamoDeposito, prestamoRetiro,
                ahorroDeposito, ahorroRetiro,
                interesDeuda
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this,
                    "Movimiento actualizado correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Recargar datos del socio para actualizar saldos y la tabla de movimientos
                buscarSocioPorId(socioActualId);
                
                // Actualizar la fila seleccionada en la tabla
                int filaSeleccionada = tablaMovimientos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    // La tabla se refrescará con los datos actualizados de la BD
                    tablaMovimientos.clearSelection();
                }
                
                // Limpiar campos y salir del modo actualización
                limpiarCamposEntrada();
                modoActualizacion = false;
                movimientoSeleccionadoId = 0;
                btnAplicar.setText("Aplicar Movimiento");
                btnCancelarActualizacion.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar el movimiento en la base de datos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar el movimiento: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
      /**
     * Cancela la actualización del movimiento seleccionado
     * y vuelve al modo de creación de nuevo movimiento
     */
    private void cancelarActualizacion() {
        // Limpiar campos del formulario
        limpiarCamposEntrada();
        
        // Desactivar modo actualización
        modoActualizacion = false;
        movimientoSeleccionadoId = 0;
        
        // Actualizar la interfaz
        btnAplicar.setText("Aplicar Movimiento");
        btnCancelarActualizacion.setVisible(false);
        
        // Eliminar selección de la tabla
        tablaMovimientos.clearSelection();
        
        // Recalcular el interés según el saldo actual
        double interesCalculado = saldoPrestamos * TASA_INTERES_PRESTAMO;
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        lblInteresCalculado.setText(df.format(interesCalculado));
       
        txtInteresDeuda.setValue(interesCalculado);
          // Restaurar saldos originales en las etiquetas
        lblSaldoAportaciones.setText(df.format(saldoAportaciones));
        lblSaldoPrestamos.setText(df.format(saldoPrestamos));
        lblSaldoAhorros.setText(df.format(saldoAhorros));
        
        // Actualizar también el campo de saldo en el panel de ahorros
        if (txtSaldo != null) {
            txtSaldo.setText(df.format(saldoAhorros));
            txtSaldo.setForeground(Color.BLACK);
        }
        
        // Restablecer colores
        lblSaldoAportaciones.setForeground(Color.BLACK);
        lblSaldoPrestamos.setForeground(Color.BLACK);
        lblSaldoAhorros.setForeground(Color.BLACK);
    }
    
    /**
     * Test method to validate movement IDs
     * Verifies that all movements in the table have valid IDs
     */    /**
     * Test method to validate movement IDs
     * Verifies that all movements in the table have valid IDs
     */
    private void testMovimientoIDs() {
        int totalRows = tablaMovimientos.getRowCount();
        int rowsWithIds = 0;
        int rowsWithoutIds = 0;
        StringBuilder message = new StringBuilder("Resultados de la prueba de IDs de movimiento:\n\n");
        
        message.append("Total de filas en la tabla: ").append(totalRows).append("\n\n");
        
        // El ID del movimiento está en la columna 11 (última columna)
        for (int i = 0; i < totalRows; i++) {
            Object idObj = tablaMovimientos.getValueAt(i, 11);
            Object fechaObj = tablaMovimientos.getValueAt(i, 0);
            
            if (idObj != null && !idObj.toString().isEmpty()) {
                rowsWithIds++;
                message.append("Fila ").append(i + 1).append(": ID = ").append(idObj)
                       .append(", Fecha = ").append(fechaObj).append("\n");
            } else {
                rowsWithoutIds++;
                message.append("Fila ").append(i + 1).append(": SIN ID!, Fecha = ").append(fechaObj).append("\n");
            }
        }
        
        message.append("\nResumen:\n")
               .append("Filas con ID: ").append(rowsWithIds).append("\n")
               .append("Filas sin ID: ").append(rowsWithoutIds);
        
        JOptionPane.showMessageDialog(this, message.toString(), "Resultado Test IDs", JOptionPane.INFORMATION_MESSAGE);
    }
}
