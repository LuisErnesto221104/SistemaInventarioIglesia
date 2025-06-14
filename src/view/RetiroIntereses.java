package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import tools.SocioDAO;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Panel para el retiro de intereses de socios
 */
public class RetiroIntereses extends JPanel {
    
    // Referencia al menú principal
    private MenuPrincipal menuPrincipal;
    
    // Data Access Object para Socios
    private SocioDAO socioDAO;
    
    // Componentes de la interfaz
    private JPanel panelBusqueda;
    private JPanel panelInfo;
    private JPanel panelRetiro;
    private JPanel panelBotones;
    
    // Campos para la búsqueda
    private JTextField txtIdSocio;
    private JButton btnBuscar;
    
    // Campos para mostrar información del socio
    private JLabel lblNombreValor;
    private JLabel lblApellidosValor;
    private JLabel lblSaldoInteresesValor;
    
    // Campos para el retiro
    private JComboBox<Integer> cboAño;
    private JLabel lblInteresesAñoValor;
    private JFormattedTextField txtMontoRetiro;
    
    // Botones principales
    private JButton btnAplicar;
    private JButton btnLimpiar;
    private JButton btnCerrar;
    
    // Datos del socio actual
    private Map<String, Object> socioActual;
    private double saldoTotalIntereses;
    private double interesesAño;
    
    /**
     * Constructor del panel
     * @param menuPrincipal Referencia al menú principal
     */
    public RetiroIntereses(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.socioDAO = new SocioDAO();
        inicializarComponentes();
    }
    
    /**
     * Inicializa todos los componentes del panel
     */    private void inicializarComponentes() {
        // Configuración del panel principal
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Panel central con GridBagLayout para mejor organización
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Inicializar paneles
        inicializarPanelBusqueda();
        inicializarPanelInfo();
        inicializarPanelRetiro();
        inicializarPanelBotones();
        
        // Añadir paneles al panel central con GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        panelCentral.add(panelBusqueda, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 10, 10, 10);
        panelCentral.add(panelInfo, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        panelCentral.add(panelRetiro, gbc);
        
        // Añadir panel central al centro del BorderLayout
        JScrollPane scrollPane = new JScrollPane(panelCentral);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Añadir panel de botones al sur del BorderLayout
        add(panelBotones, BorderLayout.SOUTH);
        
        // Establecer el estado inicial
        establecerEstadoInicial();
    }
    
    /**
     * Inicializa el panel de búsqueda
     */    private void inicializarPanelBusqueda() {
        panelBusqueda = new JPanel(new BorderLayout(15, 0));
        panelBusqueda.setBackground(new Color(240, 240, 245));
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Búsqueda de Socio", 
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        
        JPanel panelCampoBusqueda = new JPanel();
        panelCampoBusqueda.setBackground(new Color(240, 240, 245));
        panelCampoBusqueda.setLayout(new BoxLayout(panelCampoBusqueda, BoxLayout.X_AXIS));
        
        JLabel lblIdSocio = new JLabel("ID del Socio:   ");
        lblIdSocio.setFont(new Font("Arial", Font.BOLD, 14));
          txtIdSocio = new JTextField(10);
        txtIdSocio.setFont(new Font("Arial", Font.PLAIN, 14));
        txtIdSocio.setMaximumSize(new Dimension(150, 30));
        txtIdSocio.setPreferredSize(new Dimension(150, 30));
        
        // Añadir Key Listener para procesar cuando se presiona Enter
        txtIdSocio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarSocio();
                }
            }
        });
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.black);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSocio();
            }
        });
        
        panelCampoBusqueda.add(lblIdSocio);
        panelCampoBusqueda.add(Box.createRigidArea(new Dimension(10, 0)));
        panelCampoBusqueda.add(txtIdSocio);
        panelCampoBusqueda.add(Box.createRigidArea(new Dimension(20, 0)));
        panelCampoBusqueda.add(btnBuscar);
        panelCampoBusqueda.add(Box.createHorizontalGlue());
        
        panelBusqueda.add(panelCampoBusqueda, BorderLayout.CENTER);
    }
    
    /**
     * Inicializa el panel de información del socio
     */    private void inicializarPanelInfo() {
        panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBackground(new Color(240, 240, 245));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Información del Socio", 
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
          // Etiquetas de información
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Ya no mostramos este label para ocultar el saldo total de intereses
        JLabel lblSaldoIntereses = new JLabel("Saldo Total de Intereses:");
        lblSaldoIntereses.setFont(new Font("Arial", Font.BOLD, 14));
          // Valores (inicialmente vacíos)
        lblNombreValor = new JLabel("-");
        lblNombreValor.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNombreValor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        lblNombreValor.setOpaque(true);
        lblNombreValor.setBackground(Color.WHITE);
        
        lblApellidosValor = new JLabel("-");
        lblApellidosValor.setFont(new Font("Arial", Font.PLAIN, 14));
        lblApellidosValor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        lblApellidosValor.setOpaque(true);
        lblApellidosValor.setBackground(Color.WHITE);
        
        // Mantenemos la variable pero no la mostramos en la interfaz
        lblSaldoInteresesValor = new JLabel("$ 0.00");
        
        // Añadir componentes al panel con mejor espaciado
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panelInfo.add(lblNombre, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelInfo.add(lblNombreValor, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panelInfo.add(lblApellidos, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelInfo.add(lblApellidosValor, gbc);
          // Ya no mostramos el saldo total de intereses en la interfaz
        // aunque seguimos calculándolo internamente
    }
    
    /**
     * Inicializa el panel de retiro de intereses
     */    private void inicializarPanelRetiro() {
        panelRetiro = new JPanel(new GridBagLayout());
        panelRetiro.setBackground(new Color(240, 240, 245));
        panelRetiro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(), "Retiro de Intereses", 
                    TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Componentes para selección de año
        JLabel lblAño = new JLabel("Año:");
        lblAño.setFont(new Font("Arial", Font.BOLD, 14));
          cboAño = new JComboBox<>();
        cboAño.setFont(new Font("Arial", Font.PLAIN, 14));
        cboAño.setPreferredSize(new Dimension(150, 30));
        cboAño.setBackground(Color.WHITE);
        // Llenar combobox con años desde el actual hasta 2009
        int añoActual = Calendar.getInstance().get(Calendar.YEAR);
        int añoInicial = 2009;
        for (int año = añoActual; año >= añoInicial; año--) {
            cboAño.addItem(año);
        }
        cboAño.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && socioActual != null) {
                    calcularInteresesAño();
                }
            }
        });
          JLabel lblInteresesAño = new JLabel("Intereses Disponibles del Año:");
        lblInteresesAño.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblInteresesAñoValor = new JLabel("$ 0.00");
        lblInteresesAñoValor.setFont(new Font("Arial", Font.BOLD, 14));
        lblInteresesAñoValor.setForeground(new Color(0, 0, 150));
        lblInteresesAñoValor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        lblInteresesAñoValor.setOpaque(true);
        lblInteresesAñoValor.setBackground(Color.WHITE);
        
        // Campo para monto de retiro
        JLabel lblMontoRetiro = new JLabel("Monto a Retirar:");
        lblMontoRetiro.setFont(new Font("Arial", Font.BOLD, 14));            // Configurar formateador para valores monetarios
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
        DecimalFormat decimalFormat = (DecimalFormat) formatoMoneda;
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        
        NumberFormatter formateadorMoneda = new NumberFormatter(decimalFormat);
        formateadorMoneda.setMinimum(0.0);
        formateadorMoneda.setValueClass(Double.class);
        formateadorMoneda.setAllowsInvalid(true);
        formateadorMoneda.setCommitsOnValidEdit(true);
        
        txtMontoRetiro = new JFormattedTextField(formateadorMoneda);
        
        txtMontoRetiro.setValue(null);
        txtMontoRetiro.setColumns(12);
        txtMontoRetiro.setFont(new Font("Arial", Font.BOLD, 14));
        txtMontoRetiro.setHorizontalAlignment(JTextField.LEFT);
        txtMontoRetiro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                
        // Añadir Key Listener para procesar cuando se presiona Enter
        txtMontoRetiro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && btnAplicar.isEnabled()) {
                    aplicarRetiro();
                }
            }
        });
        
        // Crear un panel para el monto de retiro con un borde especial
        JPanel panelMontoRetiro = new JPanel(new BorderLayout(5, 0));
        panelMontoRetiro.setBackground(new Color(240, 240, 245));
        panelMontoRetiro.add(txtMontoRetiro, BorderLayout.CENTER);
        
        // Añadir componentes al panel con mejor espaciado
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panelRetiro.add(lblAño, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelRetiro.add(cboAño, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panelRetiro.add(lblInteresesAño, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelRetiro.add(lblInteresesAñoValor, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(20, 10, 10, 10);
        panelRetiro.add(lblMontoRetiro, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelRetiro.add(panelMontoRetiro, gbc);
    }
    
    /**
     * Inicializa el panel de botones
     */    private void inicializarPanelBotones() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        panelBotones.setBackground(new Color(240, 240, 245));
        
        // Agregar espacio elástico a la izquierda para empujar botones a la derecha
        panelBotones.add(Box.createHorizontalGlue());
        
        btnAplicar = new JButton("Aplicar Retiro");
        btnAplicar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAplicar.setBackground(new Color(0, 128, 0));
        btnAplicar.setForeground(Color.black);
        btnAplicar.setFocusPainted(false);
        btnAplicar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarRetiro();
            }
        });
        
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 14));
        btnLimpiar.setBackground(new Color(255, 165, 0));
        btnLimpiar.setForeground(Color.black);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                establecerEstadoInicial();
            }
        });
        
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(Color.RED);
        btnCerrar.setForeground(Color.black);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        // Agregar los botones con espacio entre ellos
        panelBotones.add(btnAplicar);
        panelBotones.add(Box.createRigidArea(new Dimension(15, 0)));
        panelBotones.add(btnLimpiar);
        panelBotones.add(Box.createRigidArea(new Dimension(15, 0)));
        panelBotones.add(btnCerrar);
    }
    
    /**
     * Establece el estado inicial del panel
     */
    private void establecerEstadoInicial() {        txtIdSocio.setText("");
        lblNombreValor.setText("-");
        lblApellidosValor.setText("-");
        lblSaldoInteresesValor.setText("$ 0.00");
        lblInteresesAñoValor.setText("$ 0.00");
        txtMontoRetiro.setValue(null);
        
        socioActual = null;
        saldoTotalIntereses = 0.0;
        interesesAño = 0.0;
        
        // Deshabilitar componentes hasta que se busque un socio
        cboAño.setEnabled(false);
        txtMontoRetiro.setEnabled(false);
        btnAplicar.setEnabled(false);
        
        // Dar el foco al campo de ID
        txtIdSocio.requestFocus();
    }
      /**
     * Busca un socio por su ID
     */
    private void buscarSocio() {
        try {
            int idSocio = Integer.parseInt(txtIdSocio.getText().trim());
            // Obtener el socio desde la base de datos
            // Primero intentamos buscar como socio adulto
            socioActual = socioDAO.buscarSocioAdultoPorID(idSocio);
            
            // Si no se encuentra, intentamos buscar como socio infantil
            if (socioActual == null) {
                socioActual = socioDAO.buscarSocioInfantilPorID(idSocio);
            }
            
            if (socioActual != null) {
                // Mostrar datos del socio
                lblNombreValor.setText((String) socioActual.get("Nombres"));
                lblApellidosValor.setText((String) socioActual.get("Apellidos"));
                
                // Calcular el saldo total de intereses
                calcularSaldoTotalIntereses(idSocio);
                
                // Habilitar componentes
                cboAño.setEnabled(true);
                txtMontoRetiro.setEnabled(true);
                btnAplicar.setEnabled(true);
                
                // Calcular intereses del año seleccionado
                calcularInteresesAño();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "No se encontró un socio con el ID especificado", 
                        "Socio no encontrado", 
                        JOptionPane.ERROR_MESSAGE);
                establecerEstadoInicial();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                    "El ID del socio debe ser un número válido", 
                    "Error de formato", 
                    JOptionPane.ERROR_MESSAGE);
            txtIdSocio.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al buscar el socio: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
      /**
     * Calcula el saldo total de intereses del socio
     */
    private void calcularSaldoTotalIntereses(int idSocio) {
        try {
            // Determinar si es socio adulto o infantil
            boolean esInfantil = socioActual.containsKey("Fecha"); // Los socios infantiles tienen "Fecha" en vez de "FechaRegistro"
            
            // Obtener datos financieros del socio
            Map<String, Object> datosFinancieros = socioDAO.obtenerDatosFinancierosSocio(idSocio, esInfantil);
            
            if (datosFinancieros != null) {
                // Obtener los intereses generados y los retiros
                double interesesGenerados = datosFinancieros.containsKey("Intereses") ? 
                        Double.parseDouble(datosFinancieros.get("Intereses").toString()) : 0.0;
                double retirosIntereses = datosFinancieros.containsKey("RetInteres") ? 
                        Double.parseDouble(datosFinancieros.get("RetInteres").toString()) : 0.0;
                
                saldoTotalIntereses = interesesGenerados - retirosIntereses;
            } else {
                saldoTotalIntereses = 0.0;
            }              // Formatear y mostrar el saldo
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es"));
            lblSaldoInteresesValor.setText(formatoMoneda.format(saldoTotalIntereses));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al calcular el saldo total de intereses: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }      /**     * Calcula los intereses disponibles para retirar en el año seleccionado
     * (intereses generados menos intereses ya retirados en ese año)
     * Los intereses se pueden retirar en su totalidad por año, independientemente
     * del saldo total de intereses disponible.
     */private void calcularInteresesAño() {
        try {
            if (socioActual == null) return;
            
            // Los socios adultos tienen NoSocio, y los infantiles también
            int idSocio = Integer.parseInt(socioActual.get("NoSocio").toString());
            int año = (int) cboAño.getSelectedItem();
            
            // Obtener los intereses generados en el año seleccionado
            interesesAño = socioDAO.obtenerInteresesPorAño(idSocio, año);
            
            // Formatear y mostrar los intereses del año
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-MX"));
            lblInteresesAñoValor.setText(formatoMoneda.format(interesesAño));
            
            // Establecer el valor máximo para el campo de retiro
            // Solo se puede retirar hasta el monto de intereses del año seleccionado
            // (no del saldo total de intereses)
            NumberFormatter formatter = (NumberFormatter) ((DefaultFormatterFactory) txtMontoRetiro.getFormatterFactory()).getDefaultFormatter();
            formatter.setMaximum(interesesAño);
            
            // Limpiar el campo de retiro
            txtMontoRetiro.setValue(null);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al calcular los intereses del año: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }    /**
     * Aplica el retiro de intereses del año seleccionado
     */    private void aplicarRetiro() {
        try {
            if (socioActual == null) return;
            
            // Obtener el año seleccionado
            int añoSeleccionado = (int) cboAño.getSelectedItem();
            
            // Manejar el caso cuando el valor es nulo o inválido
            double montoRetiro = 0.0;
            
            try {
                // Intentar obtener el valor del campo
                Object valorObj = txtMontoRetiro.getValue();
                if (valorObj == null) {
                    // Si está vacío el campo, intentar leer el texto directamente
                    String textoMonto = txtMontoRetiro.getText().trim();
                    if (textoMonto.isEmpty() || textoMonto.equals("$") || textoMonto.contains("_")) {
                        JOptionPane.showMessageDialog(this, 
                                "Por favor ingrese un monto a retirar", 
                                "Monto requerido", 
                                JOptionPane.WARNING_MESSAGE);
                        txtMontoRetiro.requestFocus();
                        return;
                    } else {
                        // Intentar parsear el valor manualmente
                        textoMonto = textoMonto.replace("$", "").replace(",", "").trim();
                        montoRetiro = Double.parseDouble(textoMonto);
                    }
                } else if (valorObj instanceof Number) {
                    montoRetiro = ((Number) valorObj).doubleValue();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                        "El monto ingresado no es válido. Por favor ingrese un valor numérico.", 
                        "Formato inválido", 
                        JOptionPane.WARNING_MESSAGE);
                txtMontoRetiro.requestFocus();
                return;
            }
            
            // Actualizar el cálculo de intereses del año antes de continuar para asegurar
            // que tenemos el valor más reciente
            int idSocio = Integer.parseInt(socioActual.get("NoSocio").toString());
            interesesAño = socioDAO.obtenerInteresesPorAño(idSocio, añoSeleccionado);
            
            // Validar que el monto sea mayor que cero
            if (montoRetiro <= 0) {
                JOptionPane.showMessageDialog(this, 
                        "El monto a retirar debe ser mayor que cero", 
                        "Monto inválido", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }            // Validar que el monto no exceda los intereses del año seleccionado
            // Esto es lo único que se puede retirar según el requerimiento
            if (montoRetiro > interesesAño) {
                JOptionPane.showMessageDialog(this, 
                        "El monto a retirar no puede ser mayor que los intereses generados en el año seleccionado", 
                        "Monto inválido", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Ya no validamos contra el saldo total porque el requerimiento es
            // permitir retirar intereses del año aunque sea mayor al saldo total            // Confirmar la operación
            String mensajeConfirmacion = "¿Está seguro que desea retirar $" + String.format("%.2f", montoRetiro) + 
                                       " de intereses del año " + añoSeleccionado + "?";
            
            int opcion = JOptionPane.showConfirmDialog(this, 
                    mensajeConfirmacion, 
                    "Confirmar Retiro", 
                    JOptionPane.YES_NO_OPTION);
                    
            if (opcion == JOptionPane.YES_OPTION) {
                  // Crear el mapa con los datos del movimiento
                Map<String, Object> movimiento = new HashMap<>();
                movimiento.put("IdSocio", Integer.parseInt(socioActual.get("NoSocio").toString()));
                movimiento.put("RetInteres", montoRetiro);
                
                // Crear una fecha sin componente de hora (solo fecha)
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                movimiento.put("Fecha", cal.getTime());  // Solo fecha sin hora
                
                movimiento.put("AñoInteres", añoSeleccionado); // Añadir el año del interés que se está retirando
                // Determinar si es socio infantil o adulto
                boolean esInfantil = socioActual.containsKey("Fecha"); // Los socios infantiles tienen "Fecha" en vez de "FechaRegistro"
                movimiento.put("EsInfantil", esInfantil);
                
                // Registrar el retiro en la base de datos
                boolean registrado = socioDAO.registrarRetiroIntereses(movimiento);                  if (registrado) {
                    JOptionPane.showMessageDialog(this, 
                            "Retiro de intereses del año " + añoSeleccionado + " registrado exitosamente", 
                            "Operación exitosa", 
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    // Actualizar el saldo total de intereses
                    calcularSaldoTotalIntereses(Integer.parseInt(socioActual.get("NoSocio").toString()));
                    
                    // Recalcular los intereses del año para reflejar el cambio
                    calcularInteresesAño();
                    
                    // Restablecer el campo de monto de retiro
                    txtMontoRetiro.setValue(null);
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "No se pudo registrar el retiro de intereses", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error al aplicar el retiro: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
