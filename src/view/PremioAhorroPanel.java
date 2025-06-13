package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import tools.SocioDAO;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Panel para calcular y mostrar el Premio al Ahorro de los socios
 */
public class PremioAhorroPanel extends JPanel {
    
    // Referencia al menú principal
    private MenuPrincipal menuPrincipal;
    
    // Data Access Object para Socios
    private SocioDAO socioDAO;
    
    // Componentes de la GUI
    private JPanel panelTipoSocio;
    private JPanel panelParametros;
    private JPanel panelTabla;
    private JPanel panelBotones;
    
    // Componentes para selección de tipo de socio
    private JCheckBox chkSocioAdulto;
    private JCheckBox chkSocioInfantil;
    
    // Componentes para parámetros
    private JComboBox<Integer> cboAño;
    private JFormattedTextField txtPorcentajeInteres;
    private JFormattedTextField txtPorcentajeAhorro;
    
    // Componentes para tabla
    private JTable tablaPremios;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    
    // Botones
    private JButton btnCalcular;
    private JButton btnImprimir;
    private JButton btnCerrar;
    
    // Variables de control
    private boolean mostrarAdultos = true;
    private boolean mostrarInfantiles = true;
    private int añoSeleccionado = Calendar.getInstance().get(Calendar.YEAR);
    private double porcentajeInteres = 40.0; // Default 40%
    private double porcentajeAhorro = 5.0;   // Default 5%
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal
     */
    public PremioAhorroPanel(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.socioDAO = new SocioDAO();
        inicializarComponentes();
        configurarEventos();
    }
    
    /**
     * Inicializa los componentes del panel
     */
    private void inicializarComponentes() {
        // Configuración del panel principal
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel para selección de tipo de socio
        crearPanelTipoSocio();
        add(panelTipoSocio, BorderLayout.NORTH);
        
        // Panel principal con GridBagLayout
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Panel de parámetros
        crearPanelParametros();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 5);
        gbc.weightx = 1.0;
        panelPrincipal.add(panelParametros, gbc);
        
        // Panel de tabla
        crearPanelTabla();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panelPrincipal.add(panelTabla, gbc);
        
        // Panel de botones
        crearPanelBotones();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        panelPrincipal.add(panelBotones, gbc);
        
        // Añadir panel principal al centro
        add(panelPrincipal, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel para selección de tipo de socio
     */
    private void crearPanelTipoSocio() {
        panelTipoSocio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipoSocio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Tipo de Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP));
        
        chkSocioAdulto = new JCheckBox("Socios Adultos", true);
        chkSocioInfantil = new JCheckBox("Socios Infantiles", false);
        
        chkSocioAdulto.setFont(new Font("Arial", Font.BOLD, 14));
        chkSocioInfantil.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelTipoSocio.add(chkSocioAdulto);
        panelTipoSocio.add(Box.createHorizontalStrut(20)); // Espacio entre checkboxes
        panelTipoSocio.add(chkSocioInfantil);
    }
    
    /**
     * Crea el panel para los parámetros de cálculo
     */
    private void crearPanelParametros() {
        panelParametros = new JPanel(new GridBagLayout());
        panelParametros.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Parámetros de Cálculo", 
                TitledBorder.LEFT, 
                TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Etiqueta Año
        JLabel lblAño = new JLabel("Año:");
        lblAño.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelParametros.add(lblAño, gbc);
        
        // Combo Año
        cboAño = new JComboBox<>();
        int añoActual = Calendar.getInstance().get(Calendar.YEAR);
        for (int año = añoActual; año >= añoActual - 16; año--) {
            cboAño.addItem(año);
        }
        cboAño.setPreferredSize(new Dimension(100, 25));
        cboAño.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelParametros.add(cboAño, gbc);
        
        // Etiqueta Porcentaje Interés
        JLabel lblPorcentajeInteres = new JLabel("% Interés a Premiar:");
        lblPorcentajeInteres.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 30, 5, 10); // Más espacio a la izquierda
        panelParametros.add(lblPorcentajeInteres, gbc);
        
        // Campo Porcentaje Interés
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(1);
        percentFormat.setMaximumFractionDigits(2);
        
        NumberFormatter percentFormatter = new NumberFormatter(percentFormat);
        percentFormatter.setValueClass(Double.class);
        percentFormatter.setMinimum(0.0);
        percentFormatter.setMaximum(1.0);
        
        txtPorcentajeInteres = new JFormattedTextField(new DefaultFormatterFactory(percentFormatter));
        txtPorcentajeInteres.setValue(0.40); // 40%
        txtPorcentajeInteres.setColumns(5);
        txtPorcentajeInteres.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        panelParametros.add(txtPorcentajeInteres, gbc);
        
        // Etiqueta Porcentaje Ahorro
        JLabel lblPorcentajeAhorro = new JLabel("% Ahorro a Premiar:");
        lblPorcentajeAhorro.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 30, 5, 10); // Más espacio a la izquierda
        panelParametros.add(lblPorcentajeAhorro, gbc);
        
        // Campo Porcentaje Ahorro
        txtPorcentajeAhorro = new JFormattedTextField(new DefaultFormatterFactory(percentFormatter));
        txtPorcentajeAhorro.setValue(0.05); // 5%
        txtPorcentajeAhorro.setColumns(5);
        txtPorcentajeAhorro.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        panelParametros.add(txtPorcentajeAhorro, gbc);
          // Panel de información
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(new Color(240, 248, 255)); // Alice Blue
        panelInfo.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1)); // Steel Blue border
        
        JLabel lblInfo = new JLabel("<html><b>Nota:</b> El cálculo del Premio al Ahorro considera el saldo de ahorro " +
                "y los intereses acumulados <b>únicamente del año seleccionado</b>. Solo muestra socios " +
                "que tuvieron actividad en ese año.</html>");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        panelInfo.add(lblInfo);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 5, 10);
        panelParametros.add(panelInfo, gbc);
    }
    
    /**
     * Crea el panel para la tabla de premios
     */
    private void crearPanelTabla() {        
        panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Cálculo de Premios al Ahorro", 
                TitledBorder.LEFT, 
                TitledBorder.TOP));
        
        // Modelo de tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas las celdas no editables
            }
        };
        
        // Agregar columnas
        modeloTabla.addColumn("No. Socio");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellidos");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Interés");
        modeloTabla.addColumn("Premio Interés");
        modeloTabla.addColumn("Saldo Ahorro");
        modeloTabla.addColumn("Premio Ahorro");
        modeloTabla.addColumn("Total Premio");
        modeloTabla.addColumn("Tipo"); // Columna oculta para indicar si es adulto o infantil
        
        // Crear tabla
        tablaPremios = new JTable(modeloTabla);
        tablaPremios.setRowHeight(25);
        tablaPremios.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaPremios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaPremios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaPremios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar renderizador para mostrar los valores monetarios con formato
        DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00");
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                // Aplicar formato de moneda a columnas de valores monetarios
                if (column >= 4 && column <= 8 && value != null) { // Columnas con valores monetarios
                    if (value instanceof Number) {
                        setText(moneyFormat.format(((Number)value).doubleValue()));
                    }
                }
                
                // Colorear celdas según tipo o valores
                if (!isSelected) {
                    String tipo = (String) table.getValueAt(row, 9); // Columna oculta con el tipo
                    
                    if ("INFANTIL".equals(tipo)) {
                        c.setBackground(new Color(255, 240, 245)); // Lavender Blush (rosado muy suave)
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 248, 255)); // Alice Blue
                    }
                    
                    // Colorear columnas de Premio y Total diferente
                    if (column == 5 || column == 7) { // Premio Interés y Premio Ahorro
                        c.setForeground(new Color(0, 100, 0)); // Dark Green
                    } else if (column == 8) { // Total Premio
                        c.setForeground(new Color(128, 0, 0)); // Maroon
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                
                return c;
            }
        };
        
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Aplicar renderizador a todas las columnas de valores monetarios
        for (int i = 4; i <= 8; i++) {
            tablaPremios.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
        
        // Renderizador para la columna de fecha
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaPremios.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        // Ajustar anchos de columnas
        TableColumnModel columnModel = tablaPremios.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);   // No. Socio
        columnModel.getColumn(1).setPreferredWidth(120);  // Nombre
        columnModel.getColumn(2).setPreferredWidth(120);  // Apellidos
        columnModel.getColumn(3).setPreferredWidth(100);  // Fecha
        columnModel.getColumn(4).setPreferredWidth(100);  // Interés
        columnModel.getColumn(5).setPreferredWidth(100);  // Premio Interés
        columnModel.getColumn(6).setPreferredWidth(100);  // Saldo Ahorro
        columnModel.getColumn(7).setPreferredWidth(100);  // Premio Ahorro
        columnModel.getColumn(8).setPreferredWidth(100);  // Total Premio
        
        // Ocultar columna de tipo
        columnModel.getColumn(9).setMinWidth(0);
        columnModel.getColumn(9).setMaxWidth(0);
        columnModel.getColumn(9).setWidth(0);
        
        // Scroll Pane para la tabla
        scrollPane = new JScrollPane(tablaPremios);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel para los botones
     */
    private void crearPanelBotones() {
        panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        btnCalcular = new JButton("Calcular Premios");
        btnCalcular.setFont(new Font("Arial", Font.BOLD, 14));
        btnCalcular.setPreferredSize(new Dimension(200, 35));
        btnCalcular.setBackground(new Color(220, 230, 242));
        
        btnImprimir = new JButton("Imprimir Reporte");
        btnImprimir.setFont(new Font("Arial", Font.BOLD, 14));
        btnImprimir.setPreferredSize(new Dimension(200, 35));
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setPreferredSize(new Dimension(100, 35));
        
        panelBotones.add(btnCalcular);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnImprimir);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnCerrar);
    }
    
    /**
     * Configura los eventos para los componentes
     */
    private void configurarEventos() {        // Eventos de los checkboxes de tipo de socio
        chkSocioAdulto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarAdultos = chkSocioAdulto.isSelected();
                // Asegurar que al menos un checkbox esté seleccionado
                if (!mostrarAdultos && !mostrarInfantiles) {
                    chkSocioAdulto.setSelected(true);
                    mostrarAdultos = true;
                    JOptionPane.showMessageDialog(PremioAhorroPanel.this, 
                        "Debe seleccionar al menos un tipo de socio", 
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        chkSocioInfantil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInfantiles = chkSocioInfantil.isSelected();
                // Asegurar que al menos un checkbox esté seleccionado
                if (!mostrarAdultos && !mostrarInfantiles) {
                    chkSocioInfantil.setSelected(true);
                    mostrarInfantiles = true;
                    JOptionPane.showMessageDialog(PremioAhorroPanel.this, 
                        "Debe seleccionar al menos un tipo de socio", 
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Evento de cambio de año
        cboAño.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cboAño.getSelectedItem() != null) {
                    añoSeleccionado = (Integer) cboAño.getSelectedItem();
                }
            }
        });
        
        // Eventos de los campos de porcentaje
        txtPorcentajeInteres.addPropertyChangeListener("value", new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (txtPorcentajeInteres.getValue() != null) {
                    porcentajeInteres = ((Number) txtPorcentajeInteres.getValue()).doubleValue() * 100;
                }
            }
        });
        
        txtPorcentajeAhorro.addPropertyChangeListener("value", new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if (txtPorcentajeAhorro.getValue() != null) {
                    porcentajeAhorro = ((Number) txtPorcentajeAhorro.getValue()).doubleValue() * 100;
                }
            }
        });
        
        // Evento botón Calcular
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularPremios();
            }
        });
        
        // Evento botón Imprimir
        btnImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirReporte();
            }
        });
        
        // Evento botón Cerrar
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                socioDAO.cerrarConexion();
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
    }
    
    /**
     * Calcula los premios para los socios seleccionados y actualiza la tabla
     */
    private void calcularPremios() {
        try {
            // Limpiar tabla
            while (modeloTabla.getRowCount() > 0) {
                modeloTabla.removeRow(0);
            }
            
            // Obtener los valores de los porcentajes
            if (txtPorcentajeInteres.getValue() != null) {
                porcentajeInteres = ((Number) txtPorcentajeInteres.getValue()).doubleValue() * 100;
            }
            
            if (txtPorcentajeAhorro.getValue() != null) {
                porcentajeAhorro = ((Number) txtPorcentajeAhorro.getValue()).doubleValue() * 100;
            }
              // Actualizar año seleccionado
            if (cboAño.getSelectedItem() != null) {
                añoSeleccionado = (Integer) cboAño.getSelectedItem();
                
                // Actualizar el título del panel de tabla con el año seleccionado
                panelTabla.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), 
                    "Cálculo de Premios al Ahorro - Año " + añoSeleccionado, 
                    TitledBorder.LEFT, 
                    TitledBorder.TOP));
            }
            
            // Obtener la lista de socios adultos si están seleccionados
            List<Map<String, Object>> listaSocios = new ArrayList<>();
            
            if (mostrarAdultos) {
                listaSocios.addAll(socioDAO.listarSocios());
            }
            
            if (mostrarInfantiles) {
                listaSocios.addAll(socioDAO.listarSociosInfantiles());
            }
            
            if (listaSocios.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron socios para mostrar", 
                    "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Procesar cada socio
            for (Map<String, Object> socio : listaSocios) {
                try {
                    // Determinar si es socio adulto o infantil
                    boolean esInfantil = socio.containsKey("Fecha"); // Los socios infantiles tienen "Fecha" en vez de "FechaRegistro"
                    String tipo = esInfantil ? "INFANTIL" : "ADULTO";
                    
                    int noSocio = (Integer) socio.get("NoSocio");
                    String nombre = (String) socio.get("Nombres");
                    String apellidos = (String) socio.get("Apellidos");                    // Obtener datos financieros del socio
                    Map<String, Object> datosFinancieros = socioDAO.obtenerDatosFinancierosSocio(noSocio, esInfantil);
                    
                    if (datosFinancieros == null || !datosFinancieros.containsKey("AhoSaldo")) {
                        continue; // Saltar si no hay datos financieros
                    }
                    
                    // Obtener el saldo de ahorro del año seleccionado
                    double saldoAhorro = obtenerSaldoAhorroAnual(datosFinancieros, añoSeleccionado);
                    
                    // Si no hay saldo en el año seleccionado, usar el saldo actual solo si el año seleccionado es el actual
                    if (saldoAhorro == 0.0) {
                        int añoActual = Calendar.getInstance().get(Calendar.YEAR);
                        if (añoSeleccionado == añoActual) {
                            saldoAhorro = (Double) datosFinancieros.getOrDefault("AhoSaldo", 0.0);
                        } else {
                            // Si no hay saldo en un año anterior, pasamos al siguiente socio
                            continue;
                        }
                    }
                    
                    // Calcular el interés del año seleccionado
                    double totalInteres = calcularInteresAnual(noSocio, esInfantil, añoSeleccionado);
                    
                    // Fecha del último movimiento del año seleccionado
                    Date fechaUltimoMovimiento = obtenerFechaUltimoMovimiento(datosFinancieros);
                    
                    // Si no hay movimientos en el año seleccionado, continuamos con el siguiente socio
                    if (fechaUltimoMovimiento == null) {
                        continue; // Saltar si no hay movimientos en el año seleccionado
                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaFormateada = sdf.format(fechaUltimoMovimiento);
                    
                    // Calcular los premios
                    double premioInteres = totalInteres * (porcentajeInteres / 100.0);
                    double premioAhorro = saldoAhorro * (porcentajeAhorro / 100.0);
                    double totalPremio = premioInteres + premioAhorro;
                    
                    // Agregar fila a la tabla
                    Object[] fila = {
                        noSocio,
                        nombre,
                        apellidos,
                        fechaFormateada,
                        totalInteres,
                        premioInteres,
                        saldoAhorro,
                        premioAhorro,
                        totalPremio,
                        tipo // Columna oculta para el tipo de socio
                    };
                    
                    modeloTabla.addRow(fila);
                    
                } catch (Exception ex) {
                    System.err.println("Error procesando socio: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            
            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron datos para los criterios seleccionados", 
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al calcular premios: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    /**
     * Calcula el interés anual para un socio específico
     * @param noSocio Número de socio
     * @param esInfantil Indica si es socio infantil
     * @param año Año para el cual calcular el interés
     * @return Total de interés del año
     */
    private double calcularInteresAnual(int noSocio, boolean esInfantil, int año) {
        try {
            // Obtener todos los movimientos del socio
            Map<String, Object> datosFinancieros = socioDAO.obtenerDatosFinancierosSocio(noSocio, esInfantil);
            
            if (datosFinancieros == null || !datosFinancieros.containsKey("Movimientos")) {
                return 0.0;
            }
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> movimientos = (List<Map<String, Object>>) datosFinancieros.get("Movimientos");
            
            if (movimientos == null || movimientos.isEmpty()) {
                return 0.0;
            }
            
            double totalInteres = 0.0;
            double totalRetiroInteres = 0.0;
            
            // Procesar cada movimiento
            for (Map<String, Object> movimiento : movimientos) {
                // Verificar la fecha del movimiento
                Date fecha = (Date) movimiento.get("Fecha");
                if (fecha == null) {
                    continue;
                }
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(fecha);
                int añoMovimiento = cal.get(Calendar.YEAR);
                
                // Si el movimiento es del año seleccionado, sumar los intereses y restar los retiros
                if (añoMovimiento == año) {
                    double interes = (Double) movimiento.getOrDefault("Intereses", 0.0);
                    double retiroInteres = (Double) movimiento.getOrDefault("RetInteres", 0.0);
                    
                    totalInteres += interes;
                    totalRetiroInteres += retiroInteres;
                }
            }
            
            // Calcular el interés neto (intereses ganados menos retiros de intereses)
            return totalInteres - totalRetiroInteres;
            
        } catch (Exception ex) {
            System.err.println("Error al calcular interés anual para socio #" + noSocio + ": " + ex.getMessage());
            ex.printStackTrace();
            return 0.0;
        }
    }
      /**
     * Obtiene la fecha del último movimiento de un socio en el año seleccionado
     * @param datosFinancieros Datos financieros del socio
     * @return Fecha del último movimiento en el año seleccionado o null si no hay movimientos
     */
    private Date obtenerFechaUltimoMovimiento(Map<String, Object> datosFinancieros) {
        if (datosFinancieros == null || !datosFinancieros.containsKey("Movimientos")) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> movimientos = (List<Map<String, Object>>) datosFinancieros.get("Movimientos");
        
        if (movimientos == null || movimientos.isEmpty()) {
            return null;
        }
        
        // Buscar el movimiento más reciente del año seleccionado
        Date fechaMovimientoDelAño = null;
        Calendar cal = Calendar.getInstance();
        
        for (Map<String, Object> movimiento : movimientos) {
            Date fecha = (Date) movimiento.get("Fecha");
            if (fecha != null) {
                cal.setTime(fecha);
                int añoMovimiento = cal.get(Calendar.YEAR);
                
                if (añoMovimiento == añoSeleccionado) {
                    fechaMovimientoDelAño = fecha;
                    break;  // Encontramos el primer movimiento (más reciente) del año seleccionado
                }
            }
        }
        
        return fechaMovimientoDelAño;
    }
    
    /**
     * Obtiene el saldo de ahorro de un socio para el año seleccionado
     * @param datosFinancieros Datos financieros del socio
     * @param año Año para el cual obtener el saldo
     * @return Saldo de ahorro del socio en el año seleccionado, o 0 si no hay datos
     */
    private double obtenerSaldoAhorroAnual(Map<String, Object> datosFinancieros, int año) {
        if (datosFinancieros == null || !datosFinancieros.containsKey("Movimientos")) {
            return 0.0;
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> movimientos = (List<Map<String, Object>>) datosFinancieros.get("Movimientos");
        
        if (movimientos == null || movimientos.isEmpty()) {
            return 0.0;
        }
        
        // Buscar el último movimiento del año seleccionado
        Calendar cal = Calendar.getInstance();
        for (Map<String, Object> movimiento : movimientos) {
            Date fecha = (Date) movimiento.get("Fecha");
            if (fecha != null) {
                cal.setTime(fecha);
                int añoMovimiento = cal.get(Calendar.YEAR);
                
                if (añoMovimiento == año) {
                    // Encontramos el primer movimiento (más reciente) del año seleccionado
                    return (Double) movimiento.getOrDefault("AhoSaldo", 0.0);
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Imprime el reporte de premios al ahorro
     */
    private void imprimirReporte() {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "No hay datos para imprimir. Primero calcule los premios.", 
                "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "La funcionalidad de impresión aún está en desarrollo.", 
            "Información", JOptionPane.INFORMATION_MESSAGE);
        
        // Aquí iría el código para imprimir el reporte cuando se implemente
    }
}
