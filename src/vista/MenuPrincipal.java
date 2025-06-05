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
        setTitle("Sistema de Caja - Menú Principal");
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

        JMenuItem itemVentanaPrincipal = new JMenuItem("Ventana Principal", KeyEvent.VK_V);
        JMenuItem itemConfiguracion = new JMenuItem("Configuración", KeyEvent.VK_C);
        JMenuItem itemConfImpresora = new JMenuItem("Configuración de Impresora", KeyEvent.VK_I);
        JMenuItem itemSaldoBanco = new JMenuItem("Saldo Banco", KeyEvent.VK_O);

        menuArchivo.add(itemVentanaPrincipal);
        menuArchivo.addSeparator();
        menuArchivo.add(itemConfiguracion);
        menuArchivo.add(itemConfImpresora);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSaldoBanco);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        // Menú Socios
        JMenu menuSocios = new JMenu("Socios");
        menuSocios.setMnemonic(KeyEvent.VK_S);
        
        JMenuItem itemNuevoSocio = new JMenuItem("Nuevo Socio", KeyEvent.VK_N);
        JMenuItem itemBuscarSocio = new JMenuItem("Buscar Socio", KeyEvent.VK_B);
        JMenuItem itemListadoSocios = new JMenuItem("Listado de Socios", KeyEvent.VK_L);
        
        menuSocios.add(itemNuevoSocio);
        menuSocios.addSeparator();
        menuSocios.add(itemBuscarSocio);
        menuSocios.addSeparator();
        menuSocios.add(itemListadoSocios);
        
        // Menú Ver 
        JMenu menuPrestamos = new JMenu("Ver");
        menuPrestamos.setMnemonic(KeyEvent.VK_P);
        
        JMenuItem itemNuevoPrestamo = new JMenuItem("Lista Socios Registrados", KeyEvent.VK_N);
        JMenuItem itemConsultarPrestamo = new JMenuItem("Lista Socios Infantiles", KeyEvent.VK_C);
        
        menuPrestamos.add(itemNuevoPrestamo);
        menuPrestamos.add(itemConsultarPrestamo);
        
        // Menú Movimientos
        JMenu menuMovimientos = new JMenu("Movimientos");
        menuMovimientos.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem itemDeposito = new JMenuItem("Movimiento de Socio", KeyEvent.VK_D);
        JMenuItem itemRetiro = new JMenuItem("Retiro de Intereses", KeyEvent.VK_R);
        JMenuItem itemDepositoBancario = new JMenuItem("Depositos Bancarios", KeyEvent.VK_M);
        JMenuItem itemRetiroDepositoBancario = new JMenuItem("Retiro de Deposito Bancario", KeyEvent.VK_C);
        JMenuItem itemOtros = new JMenuItem("Otros Moviminetos(Ingresos, Egresos, Gastos)");

        
        menuMovimientos.add(itemDeposito);
        menuMovimientos.addSeparator();
        menuMovimientos.add(itemRetiro);
        menuMovimientos.addSeparator();
        menuMovimientos.add(itemRetiroDepositoBancario);
        menuMovimientos.add(itemDepositoBancario);
        menuMovimientos.addSeparator();
        menuMovimientos.add(itemOtros);

        
        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.setMnemonic(KeyEvent.VK_R);
        
        JMenuItem itemReporteDiarioAdultos = new JMenuItem("Reporte Diarios de Adultos", KeyEvent.VK_I);
        JMenuItem itemReporteDiarioInfantes = new JMenuItem("Reporte Diario de Infantes", KeyEvent.VK_E);
        JMenuItem itemReporteDiario = new JMenuItem("Reporte Mensual en General ", KeyEvent.VK_T);
        JMenuItem itemReporteMensualAdultos = new JMenuItem("Reporte Mensual Adultos", KeyEvent.VK_P);
        JMenuItem itemReporteMensualInfantes = new JMenuItem("Reporte Mensual de Infantes", KeyEvent.VK_M);
        JMenuItem itemReporteMensual = new JMenuItem("Reporte Mensual General", KeyEvent.VK_G);

        JMenuItem itemReporteAnualAdultos = new JMenuItem("Reporte Anual Adultos", KeyEvent.VK_A);
        JMenuItem itemReporteAnualInfantes = new JMenuItem("Reporte Anual de Infantes", KeyEvent.VK_N);
        JMenuItem itemReporteAnual = new JMenuItem("Reporte Anual General", KeyEvent.VK_L);
        
        menuReportes.add(itemReporteDiarioAdultos);
        menuReportes.add(itemReporteDiarioInfantes);
        menuReportes.add(itemReporteDiario);
        menuReportes.addSeparator();
        menuReportes.add(itemReporteMensualAdultos);
        menuReportes.add(itemReporteMensualInfantes);
        menuReportes.add(itemReporteMensual);
        menuReportes.addSeparator();
        menuReportes.add(itemReporteAnualAdultos);
        menuReportes.add(itemReporteAnualInfantes);
        menuReportes.add(itemReporteAnual);

        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setMnemonic(KeyEvent.VK_Y);
        
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de", KeyEvent.VK_A);
        itemAcercaDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuPrincipal.this, 
                    "Sistema de Caja v1.0\nDesarrollado por Los Papus", 
                    "Acerca de", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        menuAyuda.add(itemAcercaDe);
        
        JMenu menuPremioAhorro = new JMenu("Premio Ahorro");
        menuPremioAhorro.setMnemonic(KeyEvent.VK_W);
        JMenuItem itemPremioAhorro = new JMenuItem("Premio Ahorro", KeyEvent.VK_W);

        menuPremioAhorro.add(itemPremioAhorro);


        

        // Añadir menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuSocios);
        menuBar.add(menuPrestamos);
        menuBar.add(menuMovimientos);
        menuBar.add(menuReportes);
        menuBar.add(menuPremioAhorro);
        menuBar.add(menuAyuda);
        
        // Establecer la barra de menú
        setJMenuBar(menuBar);
        
        // Panel principal con una imagen o logo de fondo
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        
        // Panel central con título
        JPanel panelCentral = new JPanel(new GridBagLayout());
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Caja");
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
