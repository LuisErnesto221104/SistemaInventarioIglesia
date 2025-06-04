package vista;

import dao.UsuarioDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginForm
 * Formulario de inicio de sesión
 */
public class LoginForm extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private JButton btnCancelar;
    private UsuarioDAO usuarioDAO;

    public LoginForm() {
        usuarioDAO = new UsuarioDAO();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configuración básica del formulario
        setTitle("Acceso al Sistema");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Crear panel principal con imagen de fondo
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel para el logo o imagen corporativa
        JPanel panelLogo = new JPanel();
        JLabel lblLogo = new JLabel("SISTEMA DE VENTAS");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        panelLogo.add(lblLogo);
        
        // Panel para el formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Etiqueta usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(lblUsuario, gbc);
        
        // Campo de texto usuario
        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelForm.add(txtUsuario, gbc);
        
        // Etiqueta clave
        JLabel lblClave = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(lblClave, gbc);
        
        // Campo de texto clave
        txtClave = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelForm.add(txtClave, gbc);
        
        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Botón ingresar
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setPreferredSize(new Dimension(100, 30));
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
        panelBotones.add(btnIngresar);
        
        // Botón cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 30));
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panelBotones.add(btnCancelar);
        
        // Añadir paneles al panel principal
        panelPrincipal.add(panelLogo, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        // Añadir panel principal al formulario
        add(panelPrincipal);
        
        // Configurar acceso rápido con tecla Enter
        getRootPane().setDefaultButton(btnIngresar);
        
        // Mostrar formulario
        setVisible(true);
    }
    
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String clave = new String(txtClave.getPassword());
        
        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese usuario y contraseña", 
                "Error de validación", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (usuarioDAO.validarUsuario(usuario, clave)) {
            JOptionPane.showMessageDialog(this, 
                "Bienvenido al Sistema", 
                "Acceso Correcto", 
                JOptionPane.INFORMATION_MESSAGE);
                
            // Cerrar formulario de login
            dispose();
            
            // Abrir formulario principal
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MenuPrincipal();
                }
            });
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos", 
                "Error de acceso", 
                JOptionPane.ERROR_MESSAGE);
            txtClave.setText("");
            txtUsuario.requestFocus();
        }
    }
    
    /**
     * Método principal para iniciar la aplicación
     */
    public static void main(String[] args) {
        // Intentar aplicar el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar el formulario de login
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm();
            }
        });
    }
}
