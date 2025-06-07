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
    private JButton btnBuscar;
    
    private SocioDAO socioDAO;
    private MenuPrincipal menuPrincipal;
    
    // Colores para la validación
    private final Color COLOR_CAMPO_VALIDO = Color.WHITE;
    private final Color COLOR_ERROR = new Color(255, 221, 221); // Rosa claro para indicar error
    
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
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Buscar Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 12)));
        
        GridBagConstraints gbcBusqueda = new GridBagConstraints();
        gbcBusqueda.insets = new Insets(5, 5, 5, 5);
        gbcBusqueda.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo de socio (checkbox)
        chkSocioInfantil = new JCheckBox("Socio Infantil");
        gbcBusqueda.gridx = 0;
        gbcBusqueda.gridy = 0;
        panelBusqueda.add(chkSocioInfantil, gbcBusqueda);
        
        // No. Socio para búsqueda
        JLabel lblNoSocioBuscar = new JLabel("No. Socio:");
        gbcBusqueda.gridx = 1;
        gbcBusqueda.gridy = 0;
        panelBusqueda.add(lblNoSocioBuscar, gbcBusqueda);
        
        txtNoSocio = new JTextField(10);
        gbcBusqueda.gridx = 2;
        gbcBusqueda.gridy = 0;
        panelBusqueda.add(txtNoSocio, gbcBusqueda);
        
        // Botón de búsqueda
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSocio();
            }
        });
        gbcBusqueda.gridx = 3;
        gbcBusqueda.gridy = 0;
        panelBusqueda.add(btnBuscar, gbcBusqueda);
        
        // Panel de formulario para datos del socio
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Datos del Socio", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 12)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Fecha
        JLabel lblFecha = new JLabel("Fecha*:");
        lblFecha.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelForm.add(lblFecha, gbc);
        
        JPanel panelFecha = new JPanel(new BorderLayout(5, 0));
        try {
            MaskFormatter formatoFecha = new MaskFormatter("##/##/####");
            formatoFecha.setPlaceholderCharacter('_');
            txtFecha = new JFormattedTextField(formatoFecha);
        } catch (ParseException e) {
            txtFecha = new JFormattedTextField();
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
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelForm.add(panelFecha, gbc);
          
        // Nombres (campo obligatorio)
        JLabel lblNombres = new JLabel("Nombre(s)*:");
        lblNombres.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 1;
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
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelForm.add(txtNombres, gbc);
        
        // Apellidos (campo obligatorio)
        JLabel lblApellidos = new JLabel("Apellido(s)*:");
        lblApellidos.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 2;
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
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelForm.add(txtApellidos, gbc);
        
        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelForm.add(lblDireccion, gbc);
        
        txtDireccion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelForm.add(txtDireccion, gbc);
        
        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panelForm.add(lblTelefono, gbc);
        
        try {
            MaskFormatter formatoTelefono = new MaskFormatter("##########");
            txtTelefono = new JFormattedTextField(formatoTelefono);
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
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panelForm.add(lblPoblacion, gbc);
        
        txtPoblacion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelForm.add(txtPoblacion, gbc);
        
        // Presentado por
        JLabel lblPresentadoPor = new JLabel("Presentado por el socio N°:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panelForm.add(lblPresentadoPor, gbc);
        
        txtPresentadoPor = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panelForm.add(txtPresentadoPor, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setPreferredSize(new Dimension(150, 30));
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarSocio();
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
     * Deshabilita los campos del formulario hasta que se busque un socio
     */
    private void deshabilitarCamposFormulario() {
        txtFecha.setEnabled(false);
        btnCalendario.setEnabled(false);
        txtNombres.setEnabled(false);
        txtApellidos.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtPoblacion.setEnabled(false);
        txtPresentadoPor.setEnabled(false);
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
    }
    
    /**
     * Busca un socio por su número y tipo
     */    private void buscarSocio() {
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
                // Habilitar campos para edición
                habilitarCamposFormulario();
                
                // Mostrar datos del socio encontrado
                if (esInfantil) {                    // Formatear y mostrar la fecha
                    Date fecha = (Date) socio.get("Fecha");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (fecha != null) {
                        txtFecha.setText(sdf.format(fecha));
                    } else {
                        txtFecha.setText("");
                    }
                    
                    // Mostrar el resto de campos con protección contra valores nulos
                    txtNombres.setText(socio.get("Nombres") != null ? (String) socio.get("Nombres") : "");
                    txtApellidos.setText(socio.get("Apellidos") != null ? (String) socio.get("Apellidos") : "");
                    txtDireccion.setText(socio.get("Direccion") != null ? (String) socio.get("Direccion") : "");
                    txtTelefono.setText(socio.get("Telefono") != null ? (String) socio.get("Telefono") : "");
                    txtPoblacion.setText(socio.get("Poblacion") != null ? (String) socio.get("Poblacion") : "");
                    txtPresentadoPor.setText(socio.get("PresentadoPor") != null ? (String) socio.get("PresentadoPor") : "");
                    
                    JOptionPane.showMessageDialog(this, 
                        "Socio Infantil #" + noSocio + " encontrado.", 
                        "Socio encontrado", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {                    // Formatear y mostrar la fecha de registro
                    Date fechaRegistro = (Date) socio.get("FechaRegistro");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    if (fechaRegistro != null) {
                        txtFecha.setText(sdf.format(fechaRegistro));
                    } else {
                        txtFecha.setText("");
                    }
                    
                    // Mostrar el resto de campos con protección contra valores nulos
                    txtNombres.setText(socio.get("Nombres") != null ? (String) socio.get("Nombres") : "");
                    txtApellidos.setText(socio.get("Apellidos") != null ? (String) socio.get("Apellidos") : "");
                    txtDireccion.setText(socio.get("Direccion") != null ? (String) socio.get("Direccion") : "");
                    txtTelefono.setText(socio.get("Telefono") != null ? (String) socio.get("Telefono") : "");
                    txtPoblacion.setText(socio.get("Poblacion") != null ? (String) socio.get("Poblacion") : "");
                    txtPresentadoPor.setText(socio.get("PresentadoPor") != null ? (String) socio.get("PresentadoPor") : "");
                    
                    JOptionPane.showMessageDialog(this, 
                        "Socio Adulto #" + noSocio + " encontrado.", 
                        "Socio encontrado", 
                        JOptionPane.INFORMATION_MESSAGE);
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
     */
    private boolean validarCampoObligatorio(JTextField campo) {
        boolean esValido = !campo.getText().trim().isEmpty();
        if (esValido) {
            campo.setBackground(COLOR_CAMPO_VALIDO);
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
        txtNombres.setBackground(COLOR_CAMPO_VALIDO);
        txtApellidos.setBackground(COLOR_CAMPO_VALIDO);
        
        // Aplicar el estilo de campos obligatorios sin marcarlos como error
        aplicarEstiloCamposObligatorios();
    }
}
