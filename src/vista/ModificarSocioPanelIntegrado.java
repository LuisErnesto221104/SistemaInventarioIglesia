package vista;

import dao.SocioDAO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ModificarSocioPanelIntegrado
 * Panel integrado para modificar datos de un socio existente
 */
public class ModificarSocioPanelIntegrado extends JPanel {
    private JTextField txtNoSocio;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDireccion;
    private JFormattedTextField txtTelefono;
    private JTextField txtPoblacion;
    private JTextField txtPresentadoPor;
    private JFormattedTextField txtFecha;
    private JButton btnCalendario;
    private JCheckBox chkSocioInfantil;
    private JCheckBox chkHabilitarEdicion; // Checkbox para habilitar la edición
    private JButton btnBuscar;
    
    private SocioDAO socioDAO;
    private MenuPrincipal menuPrincipal;    // Nota: Los colores para validación ahora están en ValidationUtils
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal para poder volver
     */
    public ModificarSocioPanelIntegrado(MenuPrincipal menuPrincipal) {
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
        setPreferredSize(new Dimension(750, 620)); // Aumentar el tamaño del panel para mejor visualización
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        JLabel lblTitulo = new JLabel("MODIFICAR DATOS DE SOCIO", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        panelTitulo.add(btnVolver, BorderLayout.WEST);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);        // Panel de búsqueda con mejor diseño
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Búsqueda de Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        panelBusqueda.setBackground(new Color(240, 248, 255)); // Fondo azul claro para destacar
        
        GridBagConstraints gbcBusqueda = new GridBagConstraints();
        gbcBusqueda.insets = new Insets(10, 10, 10, 10);
        gbcBusqueda.fill = GridBagConstraints.HORIZONTAL;
        
        // Panel para agrupar la selección del tipo de socio
        JPanel panelTipoSocio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipoSocio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Tipo de Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP));
        panelTipoSocio.setBackground(new Color(240, 248, 255)); // Mismo fondo
          // Radio buttons para la selección del tipo de socio
        JRadioButton rbAdulto = new JRadioButton("Socio Adulto");
        rbAdulto.setSelected(true); // Por defecto, seleccionado
        rbAdulto.setBackground(new Color(240, 248, 255)); // Mismo fondo
        rbAdulto.setFont(new Font("Arial", Font.BOLD, 12));
        
        JRadioButton rbInfantil = new JRadioButton("Socio Infantil");
        rbInfantil.setBackground(new Color(240, 248, 255)); // Mismo fondo
        rbInfantil.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Grupo para que solo se pueda seleccionar uno
        ButtonGroup groupTipoSocio = new ButtonGroup();
        groupTipoSocio.add(rbAdulto);
        groupTipoSocio.add(rbInfantil);
        
        // Añadir los radio buttons al panel
        panelTipoSocio.add(rbAdulto);
        panelTipoSocio.add(rbInfantil);
        
        // Añadir el panel de tipo de socio al panel de búsqueda
        gbcBusqueda.gridx = 0;
        gbcBusqueda.gridy = 0;
        gbcBusqueda.gridwidth = 2;
        panelBusqueda.add(panelTipoSocio, gbcBusqueda);
          // Checkbox para habilitar edición (inicialmente invisible)
        chkHabilitarEdicion = new JCheckBox("Habilitar edición de campos");
        chkHabilitarEdicion.setEnabled(false);
        chkHabilitarEdicion.setBackground(new Color(240, 248, 255)); // Mismo fondo
        chkHabilitarEdicion.setFont(new Font("Arial", Font.BOLD, 12));
        chkHabilitarEdicion.setForeground(new Color(0, 100, 0)); // Verde oscuro para destacar
        chkHabilitarEdicion.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        chkHabilitarEdicion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chkHabilitarEdicion.isSelected()) {
                    habilitarCamposFormulario();
                    JOptionPane.showMessageDialog(ModificarSocioPanelIntegrado.this,
                        "Los campos están habilitados para edición.\nRecuerde que los campos marcados con * son obligatorios.",
                        "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    deshabilitarCamposFormulario();
                }
            }
        });
        gbcBusqueda.gridx = 2;
        gbcBusqueda.gridy = 0;
        gbcBusqueda.gridwidth = 1;
        panelBusqueda.add(chkHabilitarEdicion, gbcBusqueda);
          // Panel para la búsqueda por número de socio
        JPanel panelNumSocio = new JPanel(new BorderLayout(10, 0));
        panelNumSocio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Número de Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP));
        panelNumSocio.setBackground(new Color(240, 248, 255)); // Mismo fondo azul claro
          // No. Socio para búsqueda
        txtNoSocio = new JTextField(15);
        txtNoSocio.setFont(new Font("Arial", Font.BOLD, 16)); // Letra más grande y en negrita
        txtNoSocio.setPreferredSize(new Dimension(0, 35)); // Aumentar altura
        txtNoSocio.setHorizontalAlignment(JTextField.CENTER); // Texto centrado
        txtNoSocio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 2), // Cornflower blue, borde más grueso
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Más padding interno
        ));
        // Agregar KeyListener para responder a Enter
        txtNoSocio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnBuscar.doClick(); // Simular clic en el botón buscar
                }
            }
        });
        panelNumSocio.add(txtNoSocio, BorderLayout.CENTER);
        
        // Botón de búsqueda
        btnBuscar = new JButton("Buscar Socio");
        btnBuscar.setIcon(UIManager.getIcon("FileView.directoryIcon")); // Icono de búsqueda
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
        btnBuscar.setBackground(new Color(70, 130, 180)); // Steel Blue
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setPreferredSize(new Dimension(130, 35));
        btnBuscar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Actualizar el estado del checkbox tipo socio basado en los radio buttons
                chkSocioInfantil = new JCheckBox(); // Checkbox oculto para mantener la lógica existente
                chkSocioInfantil.setSelected(rbInfantil.isSelected());
                buscarSocio();
            }
        });
        panelNumSocio.add(btnBuscar, BorderLayout.EAST);
        
        // Añadir el panel de número de socio al panel de búsqueda
        gbcBusqueda.gridx = 0;
        gbcBusqueda.gridy = 1;
        gbcBusqueda.gridwidth = 3;
        panelBusqueda.add(panelNumSocio, gbcBusqueda);
          // Panel de formulario para datos del socio
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Datos del Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        panelForm.setBackground(new Color(245, 245, 250)); // Fondo gris muy claro con toque de azul
          GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Aumentar espacio entre elementos
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0; // Distribución proporcional del espacio horizontal
        
        // Fecha
        JLabel lblFecha = new JLabel("Fecha*:");
        lblFecha.setForeground(Color.RED);
        lblFecha.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelForm.add(lblFecha, gbc);
        
        JPanel panelFecha = new JPanel(new BorderLayout(5, 0));
        try {
            MaskFormatter formatoFecha = new MaskFormatter("##/##/####");
            formatoFecha.setPlaceholderCharacter('_');
            txtFecha = new JFormattedTextField(formatoFecha);
            txtFecha.setFont(new Font("Arial", Font.PLAIN, 14));
            txtFecha.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        } catch (ParseException e) {
            txtFecha = new JFormattedTextField();
            e.printStackTrace();
        }
        
        btnCalendario = new JButton("...");
        btnCalendario.setPreferredSize(new Dimension(30, 30));
        btnCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCalendario();
            }
        });
        
        panelFecha.add(txtFecha, BorderLayout.CENTER);
        panelFecha.add(btnCalendario, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelForm.add(panelFecha, gbc);
          
        // Nombres (campo obligatorio)
        JLabel lblNombres = new JLabel("Nombre(s)*:");
        lblNombres.setForeground(Color.RED);
        lblNombres.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelForm.add(lblNombres, gbc);
        
        txtNombres = new JTextField(30); // Aumentar número de columnas
        txtNombres.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNombres.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        // Agregar borde para indicar estado de validación
        txtNombres.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding interno para mejor visualización
        ));
        // Agregar validación mientras se escribe
        txtNombres.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validarCampoObligatorio(txtNombres);
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validarCampoObligatorio(txtNombres);
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validarCampoObligatorio(txtNombres);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelForm.add(txtNombres, gbc);
        
        // Apellidos (campo obligatorio)
        JLabel lblApellidos = new JLabel("Apellido(s)*:");
        lblApellidos.setForeground(Color.RED);
        lblApellidos.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelForm.add(lblApellidos, gbc);
        
        txtApellidos = new JTextField(30); // Aumentar número de columnas
        txtApellidos.setFont(new Font("Arial", Font.PLAIN, 14));
        txtApellidos.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        txtApellidos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding interno para mejor visualización
        ));
        // Agregar validación mientras se escribe
        txtApellidos.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validarCampoObligatorio(txtApellidos);
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validarCampoObligatorio(txtApellidos);
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validarCampoObligatorio(txtApellidos);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelForm.add(txtApellidos, gbc);
        
        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelForm.add(lblDireccion, gbc);
        
        txtDireccion = new JTextField(30); // Aumentar número de columnas
        txtDireccion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDireccion.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        txtDireccion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding interno para mejor visualización
        ));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelForm.add(txtDireccion, gbc);
        
        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panelForm.add(lblTelefono, gbc);
        
        try {
            MaskFormatter formatoTelefono = new MaskFormatter("##########");
            formatoTelefono.setPlaceholderCharacter('_');
            txtTelefono = new JFormattedTextField(formatoTelefono);
            txtTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
            txtTelefono.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
            txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding interno para mejor visualización
            ));
        } catch (ParseException e) {
            txtTelefono = new JFormattedTextField();
            e.printStackTrace();
        }
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelForm.add(txtTelefono, gbc);
        
        // Población
        JLabel lblPoblacion = new JLabel("Población:");
        lblPoblacion.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panelForm.add(lblPoblacion, gbc);
        
        txtPoblacion = new JTextField(30); // Aumentar número de columnas
        txtPoblacion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPoblacion.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        txtPoblacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding interno para mejor visualización
        ));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelForm.add(txtPoblacion, gbc);
        
        // Presentado por
        JLabel lblPresentadoPor = new JLabel("Presentado por el socio N°:");
        lblPresentadoPor.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panelForm.add(lblPresentadoPor, gbc);
        
        txtPresentadoPor = new JTextField(30); // Aumentar número de columnas
        txtPresentadoPor.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPresentadoPor.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        txtPresentadoPor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Padding interno para mejor visualización
        ));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panelForm.add(txtPresentadoPor, gbc);
          // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotones.setBackground(new Color(240, 248, 255)); // Fondo azul claro
        panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(0, 128, 0)); // Verde
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setPreferredSize(new Dimension(160, 35));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnGuardar.setIcon(UIManager.getIcon("FileView.floppyDriveIcon")); // Icono de guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarSocio();
            }
        });
        panelBotones.add(btnGuardar);
          JButton btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 12));
        btnLimpiar.setBackground(new Color(70, 130, 180)); // Steel Blue
        btnLimpiar.setForeground(Color.BLACK);
        btnLimpiar.setPreferredSize(new Dimension(140, 35));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnLimpiar.setIcon(UIManager.getIcon("FileChooser.upFolderIcon")); // Icono para limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        panelBotones.add(btnLimpiar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(178, 34, 34)); // Firebrick red
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnCancelar.setIcon(UIManager.getIcon("OptionPane.errorIcon")); // Icono para cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        panelBotones.add(btnCancelar);
          // Crear panel de información para campos obligatorios
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(new Color(255, 250, 240)); // Fondo amarillento muy suave
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)), // Línea superior gris
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Icono de información
        JLabel iconoInfo = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        panelInfo.add(iconoInfo);
        
        JLabel lblCamposObligatorios = new JLabel("* Los campos marcados con asterisco son obligatorios para guardar");
        lblCamposObligatorios.setFont(new Font("Arial", Font.BOLD, 12));
        lblCamposObligatorios.setForeground(new Color(178, 34, 34)); // Rojo oscuro
        panelInfo.add(lblCamposObligatorios);
        
        // Panel que combina los formularios
        JPanel panelFormularios = new JPanel(new BorderLayout(0, 10));
        panelFormularios.add(panelBusqueda, BorderLayout.NORTH);
        panelFormularios.add(panelForm, BorderLayout.CENTER);
        panelFormularios.add(panelInfo, BorderLayout.SOUTH);
        
        // Deshabilitar los campos del formulario hasta que se busque un socio
        deshabilitarCamposFormulario();
        
        // Añadir paneles al panel principal
        add(panelTitulo, BorderLayout.NORTH);
        add(panelFormularios, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Aplicar estilos de campos requeridos
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                aplicarEstiloCamposObligatorios();
            }
        });
    }
    
    /**
     * Deshabilita los campos del formulario para evitar edición accidental
     */
    private void deshabilitarCamposFormulario() {
        // Deshabilitar campos
        txtFecha.setEnabled(false);
        btnCalendario.setEnabled(false);
        txtNombres.setEnabled(false);
        txtApellidos.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtPoblacion.setEnabled(false);
        txtPresentadoPor.setEnabled(false);
        
        // Aplicar estilo visual consistente a todos los campos deshabilitados usando ValidationUtils
        ValidationUtils.aplicarEstiloCampoDeshabilitado(txtFecha);
        ValidationUtils.aplicarEstiloCampoDeshabilitado(txtNombres);
        ValidationUtils.aplicarEstiloCampoDeshabilitado(txtApellidos);
        ValidationUtils.aplicarEstiloCampoDeshabilitado(txtDireccion);
        ValidationUtils.aplicarEstiloCampoDeshabilitado(txtTelefono);
        ValidationUtils.aplicarEstiloCampoDeshabilitado(txtPoblacion);
        ValidationUtils.aplicarEstiloCampoDeshabilitado(txtPresentadoPor);
    }
    
    /**
     * Habilita los campos del formulario para edición
     */
    private void habilitarCamposFormulario() {
        txtFecha.setEnabled(true);
        btnCalendario.setEnabled(true);
        txtNombres.setEnabled(true);
        txtApellidos.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtPoblacion.setEnabled(true);
        txtPresentadoPor.setEnabled(true);
          // Restaurar colores
        txtFecha.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        txtNombres.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        txtApellidos.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        txtDireccion.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        txtTelefono.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        txtPoblacion.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        txtPresentadoPor.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        
        // Destacar campos obligatorios
        aplicarEstiloCamposObligatorios();
    }
      /**
     * Busca un socio por su número y tipo
     */
    private void buscarSocio() {
        try {
            int noSocio = Integer.parseInt(txtNoSocio.getText().trim());
            boolean esInfantil = chkSocioInfantil.isSelected();
            
            // Buscar el socio en la base de datos usando SocioDAO
            Map<String, Object> socio;
            if (esInfantil) {
                socio = socioDAO.buscarSocioInfantilPorID(noSocio);
            } else {
                socio = socioDAO.buscarSocioAdultoPorID(noSocio);
            }
            
            // Verificar si se encontró el socio
            if (socio != null) {
                // Habilitar el checkbox de edición (pero los campos aún están deshabilitados)
                chkHabilitarEdicion.setEnabled(true);
                
                // Muestra mensaje indicando que se puede marcar el checkbox para editar
                JOptionPane.showMessageDialog(this,
                    "Se encontró el socio correctamente.\n\n" +
                    "Para modificar sus datos, marque la casilla\n" +
                    "'Habilitar edición de campos' que se encuentra arriba.",
                    "Socio encontrado - Instrucciones", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Mostrar datos del socio encontrado
                if (esInfantil) {
                    // Formatear y mostrar la fecha
                    Date fecha = (Date) socio.get("Fecha");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (fecha != null) {
                        txtFecha.setText(sdf.format(fecha));
                    } else {
                        txtFecha.setText("");
                    }
                    
                    // Mostrar el resto de campos con formato mejorado usando ValidationUtils
                    mostrarDatoEnCampo(txtNombres, socio.get("Nombres"));
                    mostrarDatoEnCampo(txtApellidos, socio.get("Apellidos"));
                    mostrarDatoEnCampo(txtDireccion, socio.get("Direccion"));
                    mostrarDatoEnCampo(txtTelefono, socio.get("Telefono"));
                    mostrarDatoEnCampo(txtPoblacion, socio.get("Poblacion"));
                    mostrarDatoEnCampo(txtPresentadoPor, socio.get("PresentadoPor"));
                } else {
                    // Formatear y mostrar la fecha de registro
                    Date fechaRegistro = (Date) socio.get("FechaRegistro");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (fechaRegistro != null) {
                        txtFecha.setText(sdf.format(fechaRegistro));
                    } else {
                        txtFecha.setText("");
                    }
                    
                    // Mostrar el resto de campos usando ValidationUtils
                    mostrarDatoEnCampo(txtNombres, socio.get("Nombres"));
                    mostrarDatoEnCampo(txtApellidos, socio.get("Apellidos"));
                    mostrarDatoEnCampo(txtDireccion, socio.get("Direccion"));
                    mostrarDatoEnCampo(txtTelefono, socio.get("Telefono"));
                    mostrarDatoEnCampo(txtPoblacion, socio.get("Poblacion"));
                    mostrarDatoEnCampo(txtPresentadoPor, socio.get("PresentadoPor"));
                }
                
                // Aplicar estilos de validación a los campos
                aplicarEstiloCamposObligatorios();
            } else {
                // Si no se encuentra el socio, mostrar mensaje y deshabilitar campos
                JOptionPane.showMessageDialog(this, 
                    "No se encontró ningún socio " + (esInfantil ? "infantil" : "adulto") + " con el número " + noSocio, 
                    "Socio no encontrado", 
                    JOptionPane.WARNING_MESSAGE);
                deshabilitarCamposFormulario();
                chkHabilitarEdicion.setEnabled(false);
                chkHabilitarEdicion.setSelected(false);
                limpiarFormulario();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese un número de socio válido.", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al buscar el socio: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza los datos del socio en la base de datos
     */
    private void actualizarSocio() {
        // Validar campos obligatorios
        boolean fechaValida = !txtFecha.getText().replace("_", "").trim().equals("//");
        boolean nombreValido = validarCampoObligatorio(txtNombres);
        boolean apellidoValido = validarCampoObligatorio(txtApellidos);
        
        if (!fechaValida || !nombreValido || !apellidoValido) {
            // Construir mensaje de error específico
            StringBuilder mensajeError = new StringBuilder("Los siguientes campos son obligatorios:\n");
            if (!fechaValida) mensajeError.append("- Fecha\n");
            if (!nombreValido) mensajeError.append("- Nombre(s)\n");
            if (!apellidoValido) mensajeError.append("- Apellido(s)");
            
            JOptionPane.showMessageDialog(this, 
                mensajeError.toString(), 
                "Error de validación", 
                JOptionPane.WARNING_MESSAGE);
                
            // Enfocar el primer campo con error
            if (!fechaValida) {
                txtFecha.requestFocus();
            } else if (!nombreValido) {
                txtNombres.requestFocus();
            } else if (!apellidoValido) {
                txtApellidos.requestFocus();
            }
            return;
        }
        
        try {
            // Obtener valores de los campos
            int noSocio = Integer.parseInt(txtNoSocio.getText());
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();
            Date fecha;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                fecha = sdf.parse(txtFecha.getText());
            } catch (ParseException ex) {
                fecha = new Date();
                ex.printStackTrace();
            }
            String presentadoPor = txtPresentadoPor.getText().trim();
            String poblacion = txtPoblacion.getText().trim();
            boolean esInfantil = chkSocioInfantil.isSelected();
              // Llamar al método correspondiente del DAO para actualizar el socio
            boolean exito = false;
            
            if (esInfantil) {
                exito = socioDAO.actualizarSocioInfantil(
                    noSocio, fecha, nombres, apellidos, 
                    direccion, telefono, presentadoPor, poblacion
                );
            } else {
                exito = socioDAO.actualizarSocioAdulto(
                    noSocio, nombres, apellidos, direccion, 
                    telefono, fecha, presentadoPor, poblacion
                );
            }
            
            // Mostrar mensaje según el resultado
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Socio actualizado con éxito.", 
                    "Operación exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Deshabilitar campos después de actualizar
                deshabilitarCamposFormulario();
                txtNoSocio.setText("");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar el socio", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, asegúrese de que el número de socio sea válido.", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra un calendario para seleccionar la fecha
     */
    private void mostrarCalendario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JPanel panelCalendario = new JPanel();
        panelCalendario.setLayout(new GridLayout(7, 7));
        
        // Obtener fecha actual o la que esté en el campo
        Calendar calendario = Calendar.getInstance();
        try {
            if (!txtFecha.getText().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = sdf.parse(txtFecha.getText());
                calendario.setTime(fecha);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        final int mesActual = calendario.get(Calendar.MONTH);
        final int anioActual = calendario.get(Calendar.YEAR);
        
        // Panel superior con mes y año
        JPanel panelMesAnio = new JPanel(new BorderLayout());
        
        JButton btnAnterior = new JButton("<");
        JButton btnSiguiente = new JButton(">");
        JLabel lblMesAnio = new JLabel(new SimpleDateFormat("MMMM yyyy").format(calendario.getTime()), JLabel.CENTER);
        
        panelMesAnio.add(btnAnterior, BorderLayout.WEST);
        panelMesAnio.add(lblMesAnio, BorderLayout.CENTER);
        panelMesAnio.add(btnSiguiente, BorderLayout.EAST);
        
        panel.add(panelMesAnio, BorderLayout.NORTH);
        
        // Nombres de los días
        String[] nombresDias = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String nombreDia : nombresDias) {
            JLabel lblDia = new JLabel(nombreDia, JLabel.CENTER);
            lblDia.setFont(new Font("Arial", Font.BOLD, 12));
            panelCalendario.add(lblDia);
        }
        
        // Días del mes
        calendario.set(Calendar.DAY_OF_MONTH, 1); // Primer día del mes
        int primerDiaSemana = calendario.get(Calendar.DAY_OF_WEEK) - 1; // 0 = Domingo
        
        // Celdas vacías antes del primer día
        for (int i = 0; i < primerDiaSemana; i++) {
            panelCalendario.add(new JLabel(""));
        }
        
        // Días del mes
        int diasEnMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int dia = 1; dia <= diasEnMes; dia++) {
            final int diaFinal = dia;
            JButton btnDia = new JButton(Integer.toString(dia));
            btnDia.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Formatear fecha seleccionada
                    Calendar cal = Calendar.getInstance();
                    cal.set(anioActual, mesActual, diaFinal);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    txtFecha.setText(sdf.format(cal.getTime()));
                    
                    // Cerrar el diálogo
                    Window window = SwingUtilities.getWindowAncestor(panel);
                    if (window instanceof JDialog) {
                        ((JDialog) window).dispose();
                    }
                }
            });
              // Resaltar día actual
            Calendar hoy = Calendar.getInstance();
            if (dia == hoy.get(Calendar.DAY_OF_MONTH) && 
                calendario.get(Calendar.MONTH) == hoy.get(Calendar.MONTH) && 
                calendario.get(Calendar.YEAR) == hoy.get(Calendar.YEAR)) {
                btnDia.setBackground(new Color(135, 206, 250));
                btnDia.setForeground(Color.WHITE);
            }
            
            panelCalendario.add(btnDia);
        }
        
        panel.add(panelCalendario, BorderLayout.CENTER);
        
        // Mostrar en un diálogo
        JDialog dialogo = new JDialog();
        dialogo.setTitle("Seleccionar Fecha");
        dialogo.setModal(true);
        dialogo.setContentPane(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(btnCalendario);
        dialogo.setVisible(true);
    }
      /**
     * Valida un campo obligatorio y cambia su estilo visual en consecuencia
     * @param campo El campo de texto a validar
     * @return true si es válido, false si está vacío
     */    private boolean validarCampoObligatorio(JTextField campo) {
        // Utilizar la clase de utilidades para validar el campo
        return ValidationUtils.validarCampoObligatorio(campo);
    }
      /**
     * Aplica el estilo visual a los campos obligatorios sin marcarlos como error
     */    private void aplicarEstiloCamposObligatorios() {
        // Usar la clase ValidationUtils para aplicar estilos a los campos obligatorios
        ValidationUtils.aplicarEstiloCampoObligatorio(txtNombres, "Debe ingresar el/los nombre(s) del socio");
        ValidationUtils.aplicarEstiloCampoObligatorio(txtApellidos, "Debe ingresar el/los apellido(s) del socio");
        ValidationUtils.aplicarEstiloCampoObligatorio(txtFecha, "Debe seleccionar una fecha válida");
    }
    
    /**
     * Limpia el formulario
     */
    private void limpiarFormulario() {
        txtFecha.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtPoblacion.setText("");
        txtPresentadoPor.setText("");
          // Resetear el estilo visual de los campos obligatorios
        txtNombres.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        txtApellidos.setBackground(ValidationUtils.COLOR_CAMPO_VALIDO);
        
        // Aplicar el estilo de campos obligatorios sin marcarlos como error
        aplicarEstiloCamposObligatorios();
    }
      /**
     * Método auxiliar para mostrar datos en un campo de texto con mejor formato
     * y efectos visuales para mejor experiencia de usuario
     * @param campo El campo de texto donde mostrar el dato
     * @param valor El valor a mostrar (puede ser null)
     */    private void mostrarDatoEnCampo(JTextField campo, Object valor) {
        // Utilizar el método de la clase de utilidades
        ValidationUtils.mostrarDatoEnCampo(campo, valor);
    }
}
