package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MenuPrincipal
 * Interfaz principal del sistema
 */
public class MenuPrincipal extends JFrame {
    
    public MenuPrincipal() {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Configuración básica del formulario
        setTitle("Sistema de Ventas - Menú Principal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic(KeyEvent.VK_A);
        
        JMenuItem itemSalir = new JMenuItem("Salir", KeyEvent.VK_S);
        itemSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        itemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        menuArchivo.add(itemSalir);
        
        // Menú Socios
        JMenu menuSocios = new JMenu("Socios");
        menuSocios.setMnemonic(KeyEvent.VK_S);
        
        JMenuItem itemNuevoSocio = new JMenuItem("Nuevo Socio", KeyEvent.VK_N);
        JMenuItem itemBuscarSocio = new JMenuItem("Buscar Socio", KeyEvent.VK_B);
        JMenuItem itemListadoSocios = new JMenuItem("Listado de Socios", KeyEvent.VK_L);
        
        menuSocios.add(itemNuevoSocio);
        menuSocios.add(itemBuscarSocio);
        menuSocios.addSeparator();
        menuSocios.add(itemListadoSocios);
        
        // Menú Préstamos
        JMenu menuPrestamos = new JMenu("Préstamos");
        menuPrestamos.setMnemonic(KeyEvent.VK_P);
        
        JMenuItem itemNuevoPrestamo = new JMenuItem("Nuevo Préstamo", KeyEvent.VK_N);
        JMenuItem itemConsultarPrestamo = new JMenuItem("Consultar Préstamo", KeyEvent.VK_C);
        JMenuItem itemListadoPrestamos = new JMenuItem("Listado de Préstamos", KeyEvent.VK_L);
        
        menuPrestamos.add(itemNuevoPrestamo);
        menuPrestamos.add(itemConsultarPrestamo);
        menuPrestamos.addSeparator();
        menuPrestamos.add(itemListadoPrestamos);
        
        // Menú Ahorros
        JMenu menuAhorros = new JMenu("Ahorros");
        menuAhorros.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem itemDeposito = new JMenuItem("Depósito", KeyEvent.VK_D);
        JMenuItem itemRetiro = new JMenuItem("Retiro", KeyEvent.VK_R);
        JMenuItem itemConsultaSaldo = new JMenuItem("Consulta de Saldo", KeyEvent.VK_C);
        
        menuAhorros.add(itemDeposito);
        menuAhorros.add(itemRetiro);
        menuAhorros.addSeparator();
        menuAhorros.add(itemConsultaSaldo);
        
        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.setMnemonic(KeyEvent.VK_R);
        
        JMenuItem itemReporteIngresos = new JMenuItem("Reporte de Ingresos", KeyEvent.VK_I);
        JMenuItem itemReporteEgresos = new JMenuItem("Reporte de Egresos", KeyEvent.VK_E);
        JMenuItem itemReportePrestamos = new JMenuItem("Reporte de Préstamos", KeyEvent.VK_P);
        
        menuReportes.add(itemReporteIngresos);
        menuReportes.add(itemReporteEgresos);
        menuReportes.add(itemReportePrestamos);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setMnemonic(KeyEvent.VK_Y);
        
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de", KeyEvent.VK_A);
        itemAcercaDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuPrincipal.this, 
                    "Sistema de Ventas v1.0\nDesarrollado para Sistemsa", 
                    "Acerca de", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        menuAyuda.add(itemAcercaDe);
        
        // Añadir menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuSocios);
        menuBar.add(menuPrestamos);
        menuBar.add(menuAhorros);
        menuBar.add(menuReportes);
        menuBar.add(menuAyuda);
        
        // Establecer la barra de menú
        setJMenuBar(menuBar);
        
        // Panel principal con una imagen o logo de fondo
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        
        // Panel central con título
        JPanel panelCentral = new JPanel(new GridBagLayout());
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Ventas");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        
        panelCentral.add(lblBienvenida);
        
        // Barra de estado en la parte inferior
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel lblUsuario = new JLabel("  Usuario: Admin");
        JLabel lblFecha = new JLabel("Fecha: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) + "  ");
        
        panelEstado.add(lblUsuario, BorderLayout.WEST);
        panelEstado.add(lblFecha, BorderLayout.EAST);
        
        // Añadir paneles al panel principal
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);
        
        // Añadir panel principal al formulario
        add(panelPrincipal);
        
        // Mostrar formulario
        setVisible(true);
    }
    
    /**
     * Método principal para pruebas (normalmente se accede desde LoginForm)
     */
    public static void main(String[] args) {
        // Intentar aplicar el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear y mostrar el formulario principal
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuPrincipal();
            }
        });
    }
}
