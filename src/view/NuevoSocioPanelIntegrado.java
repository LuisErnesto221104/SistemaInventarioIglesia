package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import tools.SocioDAO;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * NuevoSocioPanelIntegrado
 * Panel integrado para agregar un nuevo socio dentro del panel principal
 */
public class NuevoSocioPanelIntegrado extends JPanel {
    
    private JTextField txtNoSocio;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDireccion;
    private JFormattedTextField txtTelefono;
    private JTextField txtPoblacion;
    private JTextField txtPresentadoPor;
    private JFormattedTextField txtFecha;
    private JButton btnCalendario;
    private JComboBox<String> cboTipoSocio;
    private SocioDAO socioDAO;
    private MenuPrincipal menuPrincipal;
      /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal para poder volver
     */    public NuevoSocioPanelIntegrado(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        socioDAO = new SocioDAO();
        inicializarComponentes();
        
        // Configurar y mostrar indicadores de campos obligatorios
        // Usando invokeLater para asegurar que el UI esté completamente construido
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Aplicar el estilo de campos obligatorios sin marcarlos como error al inicio
                aplicarEstiloCamposObligatorios();
            }
        });
    }
    
    /**
     * Aplica el estilo visual a los campos obligatorios sin marcarlos como error
     */
    private void aplicarEstiloCamposObligatorios() {
        // Aplicar un borde sutil que indique que es un campo obligatorio
        txtNombres.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 200, 200)), 
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        txtNombres.setToolTipText("Campo obligatorio");
        
        txtApellidos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 200, 200)),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        txtApellidos.setToolTipText("Campo obligatorio");
    }
    
    /**
     * Inicializa los componentes del panel
     */    // Colores para la validación
    private final Color COLOR_CAMPO_VALIDO = Color.WHITE;
    private final Color COLOR_ERROR = new Color(255, 221, 221); // Rosa claro para indicar error
    
    /**
     * Valida un campo obligatorio y cambia su estilo visual en consecuencia
     * @param campo El campo de texto a validar
     * @return true si es válido, false si está vacío
     */    private boolean validarCampoObligatorio(JTextField campo) {
        boolean esValido = !campo.getText().trim().isEmpty();
        if (esValido) {
            campo.setBackground(COLOR_CAMPO_VALIDO);
            // Mantener un borde sutil que indica que el campo es obligatorio incluso cuando es válido
            campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 200)), 
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
            ));
            campo.setToolTipText("Campo obligatorio");
        } else {
            campo.setBackground(COLOR_ERROR);
            campo.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            campo.setToolTipText("Este campo es obligatorio y no puede estar vacío");
        }
        return esValido;
    }
    
    private void inicializarComponentes() {
        // Configuración del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        JLabel lblTitulo = new JLabel("REGISTRO DE NUEVO SOCIO", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        panelTitulo.add(btnVolver, BorderLayout.WEST);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo de socio
        JLabel lblTipoSocio = new JLabel("Tipo de socio:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(lblTipoSocio, gbc);
        
        cboTipoSocio = new JComboBox<>(new String[]{"Adulto", "Infantil"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelForm.add(cboTipoSocio, gbc);
        
        // Agregar un listener al combobox para actualizar el número de socio
        cboTipoSocio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarNumeroSocio();
            }
        });
        
        // No. Socio
        JLabel lblNoSocio = new JLabel("No. Socio:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelForm.add(lblNoSocio, gbc);
        
        txtNoSocio = new JTextField(10);
        txtNoSocio.setEditable(false);
        txtNoSocio.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelForm.add(txtNoSocio, gbc);
        
        // Fecha
        JLabel lblFecha = new JLabel("Fecha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelForm.add(lblFecha, gbc);
        
        JPanel panelFecha = new JPanel(new BorderLayout(5, 0));
        try {
            MaskFormatter formatoFecha = new MaskFormatter("##/##/####");
            formatoFecha.setPlaceholderCharacter('_');
            txtFecha = new JFormattedTextField(formatoFecha);
            txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        } catch (ParseException e) {
            txtFecha = new JFormattedTextField();
            txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            e.printStackTrace();
        }
        
        btnCalendario = new JButton("...");
        btnCalendario.setPreferredSize(new Dimension(30, 25));
        btnCalendario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarCalendario();
            }
        });
        
        panelFecha.add(txtFecha, BorderLayout.CENTER);
        panelFecha.add(btnCalendario, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelForm.add(panelFecha, gbc);
          // Nombres (campo obligatorio)
        JLabel lblNombres = new JLabel("Nombre(s)*:");
        lblNombres.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelForm.add(lblNombres, gbc);
        
        txtNombres = new JTextField(20);
        // Agregar borde para indicar estado de validación
        txtNombres.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelForm.add(txtNombres, gbc);
          // Apellidos (campo obligatorio)
        JLabel lblApellidos = new JLabel("Apellido(s)*:");
        lblApellidos.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panelForm.add(lblApellidos, gbc);
        
        txtApellidos = new JTextField(20);
        txtApellidos.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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
        
        txtApellidos = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelForm.add(txtApellidos, gbc);
        
        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panelForm.add(lblDireccion, gbc);
        
        txtDireccion = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelForm.add(txtDireccion, gbc);
        
        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panelForm.add(lblTelefono, gbc);
        
        MaskFormatter formatoTelefono = null;
        try {
            formatoTelefono = new MaskFormatter("##########");
            formatoTelefono.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        txtTelefono = new JFormattedTextField(formatoTelefono);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panelForm.add(txtTelefono, gbc);
        
        // Población
        JLabel lblPoblacion = new JLabel("Población:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panelForm.add(lblPoblacion, gbc);
        
        txtPoblacion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panelForm.add(txtPoblacion, gbc);
        
        // Presentado por
        JLabel lblPresentadoPor = new JLabel("Presentado por socio N°:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        panelForm.add(lblPresentadoPor, gbc);
        
        txtPresentadoPor = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panelForm.add(txtPresentadoPor, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(120, 30));
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarSocio();
            }
        });
        panelBotones.add(btnGuardar);
        
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(new Dimension(120, 30));
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
        panelBotones.add(btnLimpiar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 30));
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        panelBotones.add(btnCancelar);
          // Crear panel de información para campos obligatorios
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblCamposObligatorios = new JLabel("* Los campos marcados con asterisco son obligatorios");
        lblCamposObligatorios.setFont(new Font("Arial", Font.ITALIC, 11));
        lblCamposObligatorios.setForeground(Color.RED);
        panelInfo.add(lblCamposObligatorios);
        
        // Panel que combina el formulario y la información
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(panelForm, BorderLayout.CENTER);
        panelCentral.add(panelInfo, BorderLayout.SOUTH);
        
        // Añadir paneles al panel principal
        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Actualizar número de socio al iniciar
        actualizarNumeroSocio();
    }
    
    /**
     * Muestra un diálogo de calendario para seleccionar una fecha
     */
    private void mostrarCalendario() {
        // Crear un frame temporal para el diálogo
        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Seleccionar Fecha");
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Crear el panel de calendario
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Crear los componentes del calendario
        final JComboBox<Integer> yearCombo = new JComboBox<>();
        final JComboBox<String> monthCombo = new JComboBox<>(new String[] {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        
        // Obtener el año actual y agregar +/- 10 años
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 10; i <= currentYear + 10; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedItem(currentYear);
        
        // Obtener el mes actual (0-11)
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        monthCombo.setSelectedIndex(currentMonth);
        
        // Panel para año y mes
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        topPanel.add(monthCombo);
        topPanel.add(yearCombo);
        
        // Panel para los días
        final JPanel daysPanel = new JPanel(new GridLayout(0, 7));
        
        // Etiquetas para los días de la semana
        String[] dayNames = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String day : dayNames) {
            JLabel label = new JLabel(day, JLabel.CENTER);
            label.setForeground(Color.BLUE);
            daysPanel.add(label);
        }
        
        // Listener para actualizar los días al cambiar el mes o año
        ActionListener dateChangeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDaysPanel(daysPanel, monthCombo.getSelectedIndex(), 
                        (Integer) yearCombo.getSelectedItem(), dialog);
            }
        };
        
        monthCombo.addActionListener(dateChangeListener);
        yearCombo.addActionListener(dateChangeListener);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(cancelButton);
        
        // Agregar componentes al panel del calendario
        calendarPanel.add(topPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        
        // Agregar componentes al diálogo
        dialog.add(calendarPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Actualizar el panel de días por primera vez
        updateDaysPanel(daysPanel, monthCombo.getSelectedIndex(), 
                (Integer) yearCombo.getSelectedItem(), dialog);
        
        // Mostrar diálogo
        dialog.setVisible(true);
    }
    
    /**
     * Actualiza el panel de días para el mes y año seleccionados
     */
    private void updateDaysPanel(JPanel daysPanel, int month, int year, final JDialog dialog) {
        // Limpiar el panel excepto las etiquetas de los días de la semana (las primeras 7 etiquetas)
        Component[] components = daysPanel.getComponents();
        for (int i = 7; i < components.length; i++) {
            daysPanel.remove(components[i]);
        }
        
        // Obtener el primer día del mes y el número de días en el mes
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 = Sunday
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Agregar espacios en blanco para los días antes del primer día del mes
        for (int i = 0; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }
        
        // Agregar botones para cada día del mes
        for (int day = 1; day <= daysInMonth; day++) {
            final JButton button = new JButton(String.valueOf(day));
            button.setMargin(new Insets(1, 1, 1, 1));
            
            final int selectedDay = day;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Formatear la fecha seleccionada (DD/MM/YYYY)
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, selectedDay);
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    txtFecha.setText(sdf.format(selectedDate.getTime()));
                    
                    dialog.dispose();
                }
            });
            
            daysPanel.add(button);
        }
        
        daysPanel.revalidate();
        daysPanel.repaint();
    }
    
    /**
     * Actualiza el número de socio según el tipo seleccionado
     */    private void actualizarNumeroSocio() {
        int siguienteNumero;
        
        if (cboTipoSocio.getSelectedIndex() == 0) { // Adulto
            siguienteNumero = socioDAO.obtenerSiguienteNumeroSocioAdulto();
        } else { // Infantil
            siguienteNumero = socioDAO.obtenerSiguienteNumeroSocioInfantil();
        }
        
        txtNoSocio.setText(String.valueOf(siguienteNumero));
    }
    
    /**
     * Guarda el socio en la base de datos
     */    private void guardarSocio() {
        // Validar campos obligatorios usando nuestra función de validación
        boolean nombreValido = !txtNombres.getText().trim().isEmpty();
        boolean apellidoValido = !txtApellidos.getText().trim().isEmpty();
        
        // Aplicar estilos visuales basados en la validación
        if (!nombreValido) {
            txtNombres.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            txtNombres.setBackground(COLOR_ERROR);
            txtNombres.setToolTipText("Este campo es obligatorio");
        }
        
        if (!apellidoValido) {
            txtApellidos.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            txtApellidos.setBackground(COLOR_ERROR);
            txtApellidos.setToolTipText("Este campo es obligatorio");
        }
        
        if (!nombreValido || !apellidoValido) {
            // Construir mensaje de error específico
            StringBuilder mensajeError = new StringBuilder("Los siguientes campos son obligatorios:\n");
            if (!nombreValido) mensajeError.append("- Nombre(s)\n");
            if (!apellidoValido) mensajeError.append("- Apellido(s)");
            
            JOptionPane.showMessageDialog(this, 
                mensajeError.toString(), 
                "Error de validación", 
                JOptionPane.WARNING_MESSAGE);
                
            // Enfocar el primer campo con error
            if (!nombreValido) {
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
            
            boolean exito;
            
            // Insertar según el tipo de socio
            if (cboTipoSocio.getSelectedIndex() == 0) { // Adulto
                exito = socioDAO.insertarSocioAdulto(
                    noSocio, nombres, apellidos, direccion, 
                    telefono, fecha, presentadoPor, poblacion
                );
            } else { // Infantil
                exito = socioDAO.insertarSocioInfantil(
                    noSocio, fecha, nombres, apellidos, 
                    direccion, telefono, presentadoPor, poblacion
                );
            }
            
            // Mostrar mensaje de éxito o error
            if (exito) {
                JOptionPane.showMessageDialog(this, 
                    "Socio guardado con éxito", 
                    "Operación exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar formulario y actualizar número de socio
                limpiarFormulario();
                actualizarNumeroSocio();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar el socio", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Número de socio inválido", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpia los campos del formulario
     */
      private void limpiarFormulario() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtPoblacion.setText("");
        txtPresentadoPor.setText("");
        txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        
        // Resetear el estilo visual de los campos obligatorios
        txtNombres.setBackground(COLOR_CAMPO_VALIDO);
        txtApellidos.setBackground(COLOR_CAMPO_VALIDO);
        
        // Aplicar el estilo de campos obligatorios sin marcarlos como error
        aplicarEstiloCamposObligatorios();
        
        txtNombres.requestFocus();
    }
}
