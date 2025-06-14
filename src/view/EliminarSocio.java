package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import tools.SocioDAO;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * EliminarSocioPanel
 * Panel para eliminar socios y trasladar sus datos a la tabla de socios cancelados
 */
public class EliminarSocio extends JPanel {
    private JTextField txtNoSocio;
    private JCheckBox chkSocioInfantil;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JButton btnVolver;
    
    // Campos para mostrar información del socio
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtPoblacion;
    private JTextField txtFechaRegistro;
    private JTextField txtPresentadoPor;
    
    // Campos para mostrar información financiera
    private JTextField txtAporteSocial;
    private JTextField txtSaldoPrestamo;
    private JTextField txtSaldoAhorro;
    private JTextField txtTotalRetiro;
    
    private JTable tablaSaldos;
    private DefaultTableModel modeloTablaSaldos;
    
    private SocioDAO socioDAO;
    private MenuPrincipal menuPrincipal;
    private Map<String, Object> socioActual;
    private boolean esInfantil;
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal
     */
    public EliminarSocio(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        socioDAO = new SocioDAO();
        inicializarComponentes();
    }
    
    /**
     * Inicializa los componentes del panel
     */
    private void inicializarComponentes() {
        // Configuración del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(800, 650));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout(10, 0));
        
        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setBackground(new Color(70, 130, 180)); // Azul acero
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        JLabel lblTitulo = new JLabel("ELIMINAR SOCIO", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        
        panelTitulo.add(btnVolver, BorderLayout.WEST);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Buscar Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        panelBusqueda.setBackground(new Color(240, 248, 255)); // Azul muy claro
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID del socio
        JLabel lblNoSocio = new JLabel("No. Socio:");
        lblNoSocio.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panelBusqueda.add(lblNoSocio, gbc);
        
        txtNoSocio = new JTextField(10);
        txtNoSocio.setFont(new Font("Arial", Font.BOLD, 16));
        txtNoSocio.setPreferredSize(new Dimension(0, 35));
        txtNoSocio.setHorizontalAlignment(JTextField.CENTER);
        txtNoSocio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 2), // Azul cornflower
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        // Agregar KeyListener para responder a Enter
        txtNoSocio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnBuscar.doClick();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        panelBusqueda.add(txtNoSocio, gbc);
        
        // Checkbox para tipo de socio
        chkSocioInfantil = new JCheckBox("Socio Infantil");
        chkSocioInfantil.setFont(new Font("Arial", Font.BOLD, 14));
        chkSocioInfantil.setBackground(new Color(240, 248, 255));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panelBusqueda.add(chkSocioInfantil, gbc);
        
        // Botón buscar
        btnBuscar = new JButton("Buscar");
        btnBuscar.setIcon(UIManager.getIcon("FileView.directoryIcon")); // Icono de búsqueda
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(70, 130, 180)); // Azul acero
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setPreferredSize(new Dimension(120, 35));
        btnBuscar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSocio();
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panelBusqueda.add(btnBuscar, gbc);
        
        // Panel de datos del socio
        JPanel panelDatosSocio = new JPanel(new GridBagLayout());
        panelDatosSocio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Datos del Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        panelDatosSocio.setBackground(new Color(245, 245, 250));
        
        GridBagConstraints gbcDatos = new GridBagConstraints();
        gbcDatos.insets = new Insets(5, 10, 5, 10);
        gbcDatos.fill = GridBagConstraints.HORIZONTAL;
        gbcDatos.anchor = GridBagConstraints.WEST;
        
        // Nombre
        JLabel lblNombres = new JLabel("Nombre(s):");
        lblNombres.setFont(new Font("Arial", Font.BOLD, 12));
        gbcDatos.gridx = 0;
        gbcDatos.gridy = 0;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 0.0;
        panelDatosSocio.add(lblNombres, gbcDatos);
        
        txtNombres = new JTextField(20);
        txtNombres.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNombres.setPreferredSize(new Dimension(0, 30));
        txtNombres.setEditable(false);
        txtNombres.setBackground(new Color(245, 245, 255)); // Azul muy claro para campos de solo lectura
        txtNombres.setForeground(new Color(0, 0, 128)); // Azul oscuro para datos
        gbcDatos.gridx = 1;
        gbcDatos.gridy = 0;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 1.0;
        panelDatosSocio.add(txtNombres, gbcDatos);
        
        // Apellidos
        JLabel lblApellidos = new JLabel("Apellido(s):");
        lblApellidos.setFont(new Font("Arial", Font.BOLD, 12));
        gbcDatos.gridx = 2;
        gbcDatos.gridy = 0;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 0.0;
        panelDatosSocio.add(lblApellidos, gbcDatos);
        
        txtApellidos = new JTextField(20);
        txtApellidos.setFont(new Font("Arial", Font.PLAIN, 14));
        txtApellidos.setPreferredSize(new Dimension(0, 30));
        txtApellidos.setEditable(false);
        txtApellidos.setBackground(new Color(245, 245, 255));
        txtApellidos.setForeground(new Color(0, 0, 128));
        gbcDatos.gridx = 3;
        gbcDatos.gridy = 0;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 1.0;
        panelDatosSocio.add(txtApellidos, gbcDatos);
        
        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(new Font("Arial", Font.BOLD, 12));
        gbcDatos.gridx = 0;
        gbcDatos.gridy = 1;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 0.0;
        panelDatosSocio.add(lblDireccion, gbcDatos);
        
        txtDireccion = new JTextField(40);
        txtDireccion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDireccion.setPreferredSize(new Dimension(0, 30));
        txtDireccion.setEditable(false);
        txtDireccion.setBackground(new Color(245, 245, 255));
        txtDireccion.setForeground(new Color(0, 0, 128));
        gbcDatos.gridx = 1;
        gbcDatos.gridy = 1;
        gbcDatos.gridwidth = 3;
        gbcDatos.weightx = 1.0;
        panelDatosSocio.add(txtDireccion, gbcDatos);
        
        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Arial", Font.BOLD, 12));
        gbcDatos.gridx = 0;
        gbcDatos.gridy = 2;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 0.0;
        panelDatosSocio.add(lblTelefono, gbcDatos);
        
        txtTelefono = new JTextField(15);
        txtTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTelefono.setPreferredSize(new Dimension(0, 30));
        txtTelefono.setEditable(false);
        txtTelefono.setBackground(new Color(245, 245, 255));
        txtTelefono.setForeground(new Color(0, 0, 128));
        gbcDatos.gridx = 1;
        gbcDatos.gridy = 2;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 1.0;
        panelDatosSocio.add(txtTelefono, gbcDatos);
        
        // Población
        JLabel lblPoblacion = new JLabel("Población:");
        lblPoblacion.setFont(new Font("Arial", Font.BOLD, 12));
        gbcDatos.gridx = 2;
        gbcDatos.gridy = 2;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 0.0;
        panelDatosSocio.add(lblPoblacion, gbcDatos);
        
        txtPoblacion = new JTextField(15);
        txtPoblacion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPoblacion.setPreferredSize(new Dimension(0, 30));
        txtPoblacion.setEditable(false);
        txtPoblacion.setBackground(new Color(245, 245, 255));
        txtPoblacion.setForeground(new Color(0, 0, 128));
        gbcDatos.gridx = 3;
        gbcDatos.gridy = 2;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 1.0;
        panelDatosSocio.add(txtPoblacion, gbcDatos);
        
        // Fecha de registro
        JLabel lblFechaRegistro = new JLabel("Fecha de Registro:");
        lblFechaRegistro.setFont(new Font("Arial", Font.BOLD, 12));
        gbcDatos.gridx = 0;
        gbcDatos.gridy = 3;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 0.0;
        panelDatosSocio.add(lblFechaRegistro, gbcDatos);
        
        txtFechaRegistro = new JTextField(15);
        txtFechaRegistro.setFont(new Font("Arial", Font.PLAIN, 14));
        txtFechaRegistro.setPreferredSize(new Dimension(0, 30));
        txtFechaRegistro.setEditable(false);
        txtFechaRegistro.setBackground(new Color(245, 245, 255));
        txtFechaRegistro.setForeground(new Color(0, 0, 128));
        gbcDatos.gridx = 1;
        gbcDatos.gridy = 3;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 1.0;
        panelDatosSocio.add(txtFechaRegistro, gbcDatos);
        
        // Presentado Por
        JLabel lblPresentadoPor = new JLabel("Presentado Por:");
        lblPresentadoPor.setFont(new Font("Arial", Font.BOLD, 12));
        gbcDatos.gridx = 2;
        gbcDatos.gridy = 3;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 0.0;
        panelDatosSocio.add(lblPresentadoPor, gbcDatos);
        
        txtPresentadoPor = new JTextField(15);
        txtPresentadoPor.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPresentadoPor.setPreferredSize(new Dimension(0, 30));
        txtPresentadoPor.setEditable(false);
        txtPresentadoPor.setBackground(new Color(245, 245, 255));
        txtPresentadoPor.setForeground(new Color(0, 0, 128));
        gbcDatos.gridx = 3;
        gbcDatos.gridy = 3;
        gbcDatos.gridwidth = 1;
        gbcDatos.weightx = 1.0;
        panelDatosSocio.add(txtPresentadoPor, gbcDatos);
        
        // Panel de información financiera
        JPanel panelFinanciero = new JPanel(new GridLayout(1, 2, 15, 0));
        
        // Tabla de saldos
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Detalle de Saldos", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        String[] columnas = {"Concepto", "Ingresos", "Egresos", "Saldo"};
        modeloTablaSaldos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaSaldos = new JTable(modeloTablaSaldos);
        tablaSaldos.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSaldos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaSaldos.setRowHeight(30);
        tablaSaldos.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column > 0 && row < table.getRowCount()) { // Columnas numéricas
                    setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }
                return c;
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaSaldos);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        
        // Panel de totales y acciones
        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Información de Retiro", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        GridBagConstraints gbcTotales = new GridBagConstraints();
        gbcTotales.insets = new Insets(5, 10, 5, 10);
        gbcTotales.fill = GridBagConstraints.HORIZONTAL;
        gbcTotales.anchor = GridBagConstraints.WEST;
        
        // Aporte Social
        JLabel lblAporteSocial = new JLabel("Aporte Social:");
        lblAporteSocial.setFont(new Font("Arial", Font.BOLD, 12));
        gbcTotales.gridx = 0;
        gbcTotales.gridy = 0;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 0.0;
        panelTotales.add(lblAporteSocial, gbcTotales);
        
        txtAporteSocial = new JTextField(15);
        txtAporteSocial.setFont(new Font("Arial", Font.BOLD, 14));
        txtAporteSocial.setEditable(false);
        txtAporteSocial.setHorizontalAlignment(JTextField.RIGHT);
        txtAporteSocial.setBackground(new Color(245, 255, 245)); // Verdoso claro
        txtAporteSocial.setForeground(new Color(0, 100, 0)); // Verde oscuro
        gbcTotales.gridx = 1;
        gbcTotales.gridy = 0;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 1.0;
        panelTotales.add(txtAporteSocial, gbcTotales);
        
        // Saldo de Préstamo
        JLabel lblSaldoPrestamo = new JLabel("Saldo de Préstamo:");
        lblSaldoPrestamo.setFont(new Font("Arial", Font.BOLD, 12));
        gbcTotales.gridx = 0;
        gbcTotales.gridy = 1;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 0.0;
        panelTotales.add(lblSaldoPrestamo, gbcTotales);
        
        txtSaldoPrestamo = new JTextField(15);
        txtSaldoPrestamo.setFont(new Font("Arial", Font.BOLD, 14));
        txtSaldoPrestamo.setEditable(false);
        txtSaldoPrestamo.setHorizontalAlignment(JTextField.RIGHT);
        txtSaldoPrestamo.setBackground(new Color(255, 245, 245)); // Rojizo claro
        txtSaldoPrestamo.setForeground(new Color(139, 0, 0)); // Rojo oscuro
        gbcTotales.gridx = 1;
        gbcTotales.gridy = 1;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 1.0;
        panelTotales.add(txtSaldoPrestamo, gbcTotales);
        
        // Saldo de Ahorro
        JLabel lblSaldoAhorro = new JLabel("Saldo de Ahorro:");
        lblSaldoAhorro.setFont(new Font("Arial", Font.BOLD, 12));
        gbcTotales.gridx = 0;
        gbcTotales.gridy = 2;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 0.0;
        panelTotales.add(lblSaldoAhorro, gbcTotales);
        
        txtSaldoAhorro = new JTextField(15);
        txtSaldoAhorro.setFont(new Font("Arial", Font.BOLD, 14));
        txtSaldoAhorro.setEditable(false);
        txtSaldoAhorro.setHorizontalAlignment(JTextField.RIGHT);
        txtSaldoAhorro.setBackground(new Color(245, 245, 255)); // Azulado claro
        txtSaldoAhorro.setForeground(new Color(0, 0, 139)); // Azul oscuro
        gbcTotales.gridx = 1;
        gbcTotales.gridy = 2;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 1.0;
        panelTotales.add(txtSaldoAhorro, gbcTotales);
        
        // Separador
        JSeparator separador = new JSeparator();
        gbcTotales.gridx = 0;
        gbcTotales.gridy = 3;
        gbcTotales.gridwidth = 2;
        gbcTotales.fill = GridBagConstraints.HORIZONTAL;
        panelTotales.add(separador, gbcTotales);
        
        // Total a Retirar
        JLabel lblTotalRetiro = new JLabel("TOTAL A RETIRAR:");
        lblTotalRetiro.setFont(new Font("Arial", Font.BOLD, 16));
        gbcTotales.gridx = 0;
        gbcTotales.gridy = 4;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 0.0;
        panelTotales.add(lblTotalRetiro, gbcTotales);
        
        txtTotalRetiro = new JTextField(15);
        txtTotalRetiro.setFont(new Font("Arial", Font.BOLD, 16));
        txtTotalRetiro.setEditable(false);
        txtTotalRetiro.setHorizontalAlignment(JTextField.RIGHT);
        txtTotalRetiro.setBackground(new Color(255, 250, 205)); // Amarillo claro
        txtTotalRetiro.setForeground(new Color(139, 69, 19)); // Marrón oscuro
        gbcTotales.gridx = 1;
        gbcTotales.gridy = 4;
        gbcTotales.gridwidth = 1;
        gbcTotales.weightx = 1.0;
        panelTotales.add(txtTotalRetiro, gbcTotales);
        
        // Botón eliminar
        btnEliminar = new JButton("Eliminar Socio");
        btnEliminar.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEliminar.setBackground(new Color(178, 34, 34)); // Rojo firebrick
        btnEliminar.setForeground(Color.BLACK);
        btnEliminar.setPreferredSize(new Dimension(180, 40));
        btnEliminar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnEliminar.setFocusPainted(false);
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarEliminarSocio();
            }
        });
        gbcTotales.gridx = 0;
        gbcTotales.gridy = 5;
        gbcTotales.gridwidth = 2;
        gbcTotales.anchor = GridBagConstraints.CENTER;
        gbcTotales.insets = new Insets(20, 10, 5, 10);
        panelTotales.add(btnEliminar, gbcTotales);
        
        // Agregar paneles a panel financiero
        panelFinanciero.add(panelTabla);
        panelFinanciero.add(panelTotales);
        
        // Panel de advertencia
        JPanel panelAdvertencia = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAdvertencia.setBackground(new Color(255, 240, 240)); // Rojo muy suave
        panelAdvertencia.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)), // Línea superior
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel iconoAdvertencia = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        panelAdvertencia.add(iconoAdvertencia);
        
        JLabel lblAdvertencia = new JLabel("<html><b>ADVERTENCIA:</b> El proceso de eliminación de socios es irreversible. " + 
                "Todos los datos del socio serán trasladados a la tabla de socios cancelados.</html>");
        lblAdvertencia.setFont(new Font("Arial", Font.BOLD, 12));
        lblAdvertencia.setForeground(new Color(178, 34, 34)); // Rojo firebrick
        panelAdvertencia.add(lblAdvertencia);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.add(panelBusqueda);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(panelDatosSocio);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(panelFinanciero);
        
        // Layout principal
        add(panelTitulo, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelAdvertencia, BorderLayout.SOUTH);
    }
      /**
     * Busca un socio por número, verificando tanto tablas de socios como la tabla MovimientosSocio
     */
    private void buscarSocio() {
        if (txtNoSocio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, ingrese el número de socio.",
                "Campo requerido",
                JOptionPane.INFORMATION_MESSAGE);
            txtNoSocio.requestFocus();
            return;
        }
        
        try {
            int noSocio = Integer.parseInt(txtNoSocio.getText().trim());
            esInfantil = chkSocioInfantil.isSelected();
              // Definir exactamente los valores de tipo de socio según la base de datos
            String tipoActual = esInfantil ? "INFANTIL" : "ADULTO";
            String tipoAlternativo = esInfantil ? "ADULTO" : "INFANTIL";
            
            System.out.println("Buscando socio #" + noSocio + " con tipo: " + tipoActual);
            
            // Verificar primero si existe en la tabla de movimientos con el tipo seleccionado
            boolean existeEnMovimientos = socioDAO.existeEnMovimientos(noSocio, tipoActual);
            if (!existeEnMovimientos) {
                // Verificar si existe en la otra categoría
                boolean existeEnOtraCategoria = socioDAO.existeEnMovimientos(noSocio, tipoAlternativo);
                if (existeEnOtraCategoria) {
                    System.out.println("Socio #" + noSocio + " encontrado como " + tipoAlternativo + " en MovimientosSocio");
                    
                    int respuesta = JOptionPane.showConfirmDialog(this,
                        "No se encontró el socio como " + tipoActual + " en los movimientos, pero existe como " + 
                        tipoAlternativo + ".\n\n¿Desea cambiar el tipo de socio?",
                        "Socio encontrado en otra categoría",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (respuesta == JOptionPane.YES_OPTION) {
                        // Cambiar el tipo de socio
                        esInfantil = !esInfantil;
                        chkSocioInfantil.setSelected(esInfantil);
                        System.out.println("Tipo de socio cambiado a: " + (esInfantil ? "INFANTIL" : "ADULTO"));
                    }
                }
            }
            
            // Limpiar campos y deshabilitar botón de eliminar
            limpiarCampos();
            
            // Verificar primero si tiene préstamos pendientes
            if (socioDAO.tienePrestamoPendiente(noSocio)) {
                JOptionPane.showMessageDialog(this,
                    "No se puede eliminar al socio porque tiene préstamos pendientes.",
                    "Préstamos pendientes",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Buscar el socio según el tipo seleccionado
            socioActual = esInfantil ? 
                socioDAO.buscarSocioInfantilPorID(noSocio) : 
                socioDAO.buscarSocioAdultoPorID(noSocio);
            
            // Si no se encuentra en la tabla correspondiente al tipo seleccionado, verificar en la otra tabla
            if (socioActual == null) {
                // Intentar en la otra tabla
                Map<String, Object> socioAlternativo = esInfantil ? 
                    socioDAO.buscarSocioAdultoPorID(noSocio) : 
                    socioDAO.buscarSocioInfantilPorID(noSocio);
                
                if (socioAlternativo != null) {
                    // Se encontró en la otra tabla, sugerir el cambio
                    int respuesta = JOptionPane.showConfirmDialog(this,
                        "No se encontró el socio como " + (esInfantil ? "infantil" : "adulto") + ", pero existe como " + 
                        (esInfantil ? "adulto" : "infantil") + ".\n\n¿Desea cambiar el tipo de socio?",
                        "Socio encontrado en otra categoría",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (respuesta == JOptionPane.YES_OPTION) {
                        // Cambiar el tipo de socio y volver a buscar
                        esInfantil = !esInfantil;
                        chkSocioInfantil.setSelected(esInfantil);
                        socioActual = socioAlternativo;
                    } else {
                        return; // Usuario decidió no cambiar el tipo
                    }
                } else {
                    // No se encontró en ninguna tabla
                    JOptionPane.showMessageDialog(this,
                        "No se encontró ningún socio con el número " + noSocio,
                        "Socio no encontrado",
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            
            // Mostrar los datos del socio
            mostrarDatosSocio();
            
            // Buscar y mostrar datos financieros
            Map<String, Object> datosFinancieros = socioDAO.obtenerDatosFinancierosSocio(noSocio, esInfantil);
            mostrarDatosFinancieros(datosFinancieros);
            
            // Habilitar botón de eliminar
            btnEliminar.setEnabled(true);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Por favor, ingrese un número de socio válido.",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
            txtNoSocio.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar socio: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Muestra los datos del socio en los campos correspondientes
     */
    private void mostrarDatosSocio() {
        if (socioActual == null) return;
        
        // Mostrar datos básicos
        txtNombres.setText(socioActual.get("Nombres") != null ? (String)socioActual.get("Nombres") : "");
        txtApellidos.setText(socioActual.get("Apellidos") != null ? (String)socioActual.get("Apellidos") : "");
        txtDireccion.setText(socioActual.get("Direccion") != null ? (String)socioActual.get("Direccion") : "");
        txtTelefono.setText(socioActual.get("Telefono") != null ? (String)socioActual.get("Telefono") : "");
        txtPoblacion.setText(socioActual.get("Poblacion") != null ? (String)socioActual.get("Poblacion") : "");
        txtPresentadoPor.setText(socioActual.get("PresentadoPor") != null ? (String)socioActual.get("PresentadoPor") : "");
        
        // Formatear y mostrar fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        
        if (esInfantil) {
            fecha = (Date)socioActual.get("Fecha");
        } else {
            fecha = (Date)socioActual.get("FechaRegistro");
        }
        
        txtFechaRegistro.setText(fecha != null ? sdf.format(fecha) : "");
    }
    
    /**
     * Muestra datos financieros del socio
     */    private void mostrarDatosFinancieros(Map<String, Object> datos) {
        if (datos == null) return;
        
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        // Limpiar tabla
        modeloTablaSaldos.setRowCount(0);
        
        // Variable para almacenar el total a retirar (declarada aquí para ser accesible en todo el método)
        double totalRetiro = 0.0;
        
        // Verificar si hay movimientos en el mapa de datos
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> movimientos = (List<Map<String, Object>>) datos.getOrDefault("Movimientos", new ArrayList<>());
        
        if (movimientos.isEmpty()) {
            // Si no hay movimientos, usar solo los datos del más reciente o por defecto
            // Aporte
            double aporIngresos = ((Number)datos.getOrDefault("AporIngresos", 0.0)).doubleValue();
            double aporEgresos = ((Number)datos.getOrDefault("AporEgresos", 0.0)).doubleValue();
            double aporSaldo = ((Number)datos.getOrDefault("AporSaldo", 0.0)).doubleValue();
            
            // Préstamos
            double presIngresos = ((Number)datos.getOrDefault("PresIngresos", 0.0)).doubleValue();
            double presEgresos = ((Number)datos.getOrDefault("PresEgresos", 0.0)).doubleValue();
            double presSaldo = ((Number)datos.getOrDefault("PresSaldo", 0.0)).doubleValue();
            
            // Ahorro
            double ahoIngresos = ((Number)datos.getOrDefault("AhoIngresos", 0.0)).doubleValue();
            double ahoEgresos = ((Number)datos.getOrDefault("AhoEgresos", 0.0)).doubleValue();
            double ahoSaldo = ((Number)datos.getOrDefault("AhoSaldo", 0.0)).doubleValue();
            
            // Intereses
            double intereses = ((Number)datos.getOrDefault("Intereses", 0.0)).doubleValue();
            
            // Aportación social
            double aportacionSocial = ((Number)datos.getOrDefault("AportacionSocial", 0.0)).doubleValue();
            
            // Llenar la tabla con los datos por defecto
            modeloTablaSaldos.addRow(new Object[]{"Aporte", formato.format(aporIngresos), formato.format(aporEgresos), formato.format(aporSaldo)});
            modeloTablaSaldos.addRow(new Object[]{"Préstamo", formato.format(presIngresos), formato.format(presEgresos), formato.format(presSaldo)});
            modeloTablaSaldos.addRow(new Object[]{"Ahorro", formato.format(ahoIngresos), formato.format(ahoEgresos), formato.format(ahoSaldo)});
            modeloTablaSaldos.addRow(new Object[]{"Intereses", "", "", formato.format(intereses)});
              // Mostrar los campos con los valores por defecto
            txtAporteSocial.setText(formato.format(aportacionSocial));
            txtSaldoAhorro.setText(formato.format(ahoSaldo));
            txtSaldoPrestamo.setText(formato.format(presSaldo));
            
            // Calcular total a retirar (AhorroSaldo + AporteSocial - PrestamoSaldo)
            totalRetiro = ahoSaldo + aportacionSocial - presSaldo;
            txtTotalRetiro.setText(formato.format(totalRetiro));
            
            // Cambiar el color del total según sea positivo o negativo
            if (totalRetiro < 0) {
                txtTotalRetiro.setForeground(new Color(255, 0, 0)); // Rojo
            } else {
                txtTotalRetiro.setForeground(new Color(0, 128, 0)); // Verde
            }
            
        } else {
            // Si hay movimientos, mostrarlos todos con fecha y saldos
            
            // Para los campos de texto usamos el registro más reciente (el primero en la lista)
            Map<String, Object> registroReciente = movimientos.get(0);
            double aportacionSocial = ((Number)registroReciente.getOrDefault("AporSaldo", 0.0)).doubleValue();
            double ahoSaldo = ((Number)registroReciente.getOrDefault("AhoSaldo", 0.0)).doubleValue();
            double presSaldo = ((Number)registroReciente.getOrDefault("PresSaldo", 0.0)).doubleValue();
            
            // Para la tabla, mostramos todos los registros con su fecha
            for (Map<String, Object> movimiento : movimientos) {
                String fechaStr = "Sin fecha";
                if (movimiento.get("Fecha") != null) {
                    fechaStr = formatoFecha.format(movimiento.get("Fecha"));
                }
                  double aporSaldoMov = ((Number)movimiento.getOrDefault("AporSaldo", 0.0)).doubleValue();
                double presSaldoMov = ((Number)movimiento.getOrDefault("PresSaldo", 0.0)).doubleValue();
                double ahoSaldoMov = ((Number)movimiento.getOrDefault("AhoSaldo", 0.0)).doubleValue();
                double interesesMov = ((Number)movimiento.getOrDefault("Intereses", 0.0)).doubleValue();
                
                // Calcular el saldo total (AhorroSaldo + AporteSocial - PrestamoSaldo)
                double saldoTotal = ahoSaldoMov + aporSaldoMov - presSaldoMov;
                  // Agregar filas a la tabla con la fecha y los saldos
                modeloTablaSaldos.addRow(new Object[]{
                    "Fecha: " + fechaStr,
                    "Total: " + formato.format(saldoTotal),
                    "(Ahorro + Aporte - Préstamo)",
                    saldoTotal < 0 ? "❌" : "✅"
                });
                
                modeloTablaSaldos.addRow(new Object[]{"Aporte", "", "", formato.format(aporSaldoMov)});
                modeloTablaSaldos.addRow(new Object[]{"Préstamo", "", "", formato.format(presSaldoMov)});
                modeloTablaSaldos.addRow(new Object[]{"Ahorro", "", "", formato.format(ahoSaldoMov)});
                modeloTablaSaldos.addRow(new Object[]{"Intereses", "", "", formato.format(interesesMov)});
                
                // Agregar una fila en blanco para separar los movimientos
                modeloTablaSaldos.addRow(new Object[]{"", "", "", ""});
            }
              // Mostrar los campos con los valores del registro más reciente
            txtAporteSocial.setText(formato.format(aportacionSocial));
            txtSaldoAhorro.setText(formato.format(ahoSaldo));
            txtSaldoPrestamo.setText(formato.format(presSaldo));
            
            // Calcular total a retirar (AhorroSaldo + AporteSocial - PrestamoSaldo)
            totalRetiro = ahoSaldo + aportacionSocial - presSaldo;
            txtTotalRetiro.setText(formato.format(totalRetiro));
            
            // Cambiar el color del total según sea positivo o negativo
            if (totalRetiro < 0) {
                txtTotalRetiro.setForeground(new Color(255, 0, 0)); // Rojo
            } else {
                txtTotalRetiro.setForeground(new Color(0, 128, 0)); // Verde
            }
        }
        
        // Cambiar color según si el total es positivo o negativo
        if (totalRetiro < 0) {
            txtTotalRetiro.setForeground(Color.RED);
        } else {
            txtTotalRetiro.setForeground(new Color(0, 100, 0)); // Verde oscuro
        }
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtPoblacion.setText("");
        txtFechaRegistro.setText("");
        txtPresentadoPor.setText("");
        txtAporteSocial.setText("");
        txtSaldoPrestamo.setText("");
        txtSaldoAhorro.setText("");
        txtTotalRetiro.setText("");
        
        modeloTablaSaldos.setRowCount(0);
        btnEliminar.setEnabled(false);
        socioActual = null;
    }
    
    /**
     * Confirma la eliminación del socio
     */
    private void confirmarEliminarSocio() {
        if (socioActual == null) return;
        
        int noSocio = (int) socioActual.get("NoSocio");
        String nombre = (String) socioActual.get("Nombres");
        String apellido = (String) socioActual.get("Apellidos");
        String tipoSocio = esInfantil ? "INFANTIL" : "ADULTO";
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar al socio " + tipoSocio + " #" + noSocio + ": " + nombre + " " + apellido + "?\n\n" +
            "Esta acción no se puede deshacer y toda la información del socio será trasladada a la tabla de socios cancelados.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            // Obtener el total a retirar
            double totalRetiro = 0;
            try {
                String valorTexto = txtTotalRetiro.getText().replace(",", "");
                totalRetiro = Double.parseDouble(valorTexto);
            } catch (Exception e) {
                // Ignorar errores de parseo, usar 0
            }
            
            System.out.println("Ejecutando eliminación de socio " + tipoSocio + " #" + noSocio + 
                ": " + nombre + " " + apellido + " con monto de retiro: " + totalRetiro);
            
            // Verificar que existe en MovimientosSocio del tipo correcto
            boolean existeEnMovimientos = socioDAO.existeEnMovimientos(noSocio, tipoSocio);
            if (!existeEnMovimientos) {
                int respuesta = JOptionPane.showConfirmDialog(this,
                    "El socio #" + noSocio + " no tiene registros en MovimientosSocio como " + tipoSocio + ".\n" +
                    "¿Desea continuar con la eliminación de todos modos?",
                    "Advertencia de datos",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (respuesta != JOptionPane.YES_OPTION) {
                    return;
                }
            }
                
            // Eliminar socio
            boolean exito = socioDAO.eliminarSocio(noSocio, esInfantil, totalRetiro);
            
            if (exito) {
                JOptionPane.showMessageDialog(this,
                    "El socio ha sido eliminado exitosamente y sus datos han sido trasladados a la tabla de socios cancelados.",
                    "Socio eliminado",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos e inactivar el botón de eliminar
                limpiarCampos();
                txtNoSocio.setText("");
                btnEliminar.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar al socio. Por favor, intente nuevamente.",
                    "Error al eliminar",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar socio: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
