package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

/**
 * MenuPrincipal
 * Interfaz principal del sistema
 */
public class MenuPrincipal extends JFrame {
    
    // Panel principal que contendrá todos los demás paneles
    private JPanel panelContenido;
    
    // Panel que se muestra por defecto al iniciar
    private JPanel panelBienvenida;
    
    // Paneles para las diferentes funcionalidades
    private JPanel panelActual;
    
    public MenuPrincipal() {
        inicializarComponentes();
    }
      private void inicializarComponentes() {
        // Configuración básica del formulario
        setTitle("Sistema de Caja - Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Iniciar en modo pantalla completa
        setSize(900, 600); // Tamaño por defecto cuando no esté maximizada
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
        JMenuItem itemBuscarSocioID = new JMenuItem("Buscar ID Socio", KeyEvent.VK_I);   
        JMenuItem itemModificarSocio = new JMenuItem("Modificar/Buscar Socio", KeyEvent.VK_M);
        JMenuItem itemEliminarSocio = new JMenuItem("Eliminar Socio", KeyEvent.VK_E);
        
        // Añadir acción al menú de buscar ID socio
        itemBuscarSocioID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelBuscarID = new BuscarSocioIDPanel(MenuPrincipal.this);
                cambiarPanel(panelBuscarID, "buscarIDSocio");
            }
        });
        
        // Añadir acción al menú de eliminar socio
        itemEliminarSocio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelEliminarSocio = new EliminarSocioPanel(MenuPrincipal.this);
                cambiarPanel(panelEliminarSocio, "eliminarSocio");
            }
        });
        
        // Añadir acción al menú de modificar socio
        itemModificarSocio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelModificarSocio = new ModificarSocioPanelIntegrado(MenuPrincipal.this);
                cambiarPanel(panelModificarSocio, "modificar/BuscarSocio");
            }
        });
          // Añadir acción al menú de nuevo socio
        itemNuevoSocio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelNuevoSocio = new NuevoSocioPanelIntegrado(MenuPrincipal.this);
                cambiarPanel(panelNuevoSocio, "nuevoSocio");
            }
        });
        
        menuSocios.add(itemNuevoSocio);
        menuSocios.addSeparator();
        menuSocios.add(itemModificarSocio);
        menuSocios.addSeparator();
        menuSocios.add(itemBuscarSocioID);
        menuSocios.addSeparator();
        menuSocios.add(itemEliminarSocio);

          // Menú Ver 
        JMenu menuVer = new JMenu("Ver");
        menuVer.setMnemonic(KeyEvent.VK_P);
          JMenuItem itemNuevoPrestamo = new JMenuItem("Lista Socios Registrados", KeyEvent.VK_N);
        JMenuItem itemConsultarPrestamo = new JMenuItem("Lista Socios Infantiles", KeyEvent.VK_C);
        JMenuItem itemTogglePantallaCompleta = new JMenuItem("Alternar Pantalla Completa", KeyEvent.VK_F);
        itemTogglePantallaCompleta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        
        // Añadir acción para alternar pantalla completa
        itemTogglePantallaCompleta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                    setExtendedState(JFrame.NORMAL);
                } else {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }
        });
        
        // Añadir acción al menú de socios registrados
        itemNuevoPrestamo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelSociosAdultos = new ListadoSociosAdultosPanelIntegrado(MenuPrincipal.this);
                cambiarPanel(panelSociosAdultos, "sociosAdultos");
            }
        });
        
        // Añadir acción al menú de socios infantiles
        itemConsultarPrestamo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelSociosInfantiles = new ListadoSociosInfantilesPanelIntegrado(MenuPrincipal.this);
                cambiarPanel(panelSociosInfantiles, "sociosInfantiles");
            }
        });
        
        menuVer.add(itemNuevoPrestamo);
        menuVer.add(itemConsultarPrestamo);
        menuVer.addSeparator();
        menuVer.add(itemTogglePantallaCompleta);
        
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
        JMenuItem itemReporteMensualAdultos = new JMenuItem("Reporte Mensual Adultos", KeyEvent.VK_P);
        JMenuItem itemReporteMensualInfantes = new JMenuItem("Reporte Mensual de Infantes", KeyEvent.VK_M);
        JMenuItem itemReporteMensual = new JMenuItem("Reporte Mensual General", KeyEvent.VK_G);

        JMenuItem itemReporteAnualAdultos = new JMenuItem("Reporte Anual Adultos", KeyEvent.VK_A);
        JMenuItem itemReporteAnualInfantes = new JMenuItem("Reporte Anual de Infantes", KeyEvent.VK_N);
        JMenuItem itemReporteAnual = new JMenuItem("Reporte Anual General", KeyEvent.VK_L);
        
        // Añadir acción al menú de reporte diario de adultos
        itemReporteDiarioAdultos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelReporteDiarioAdultos = new ReporteDiarioAdultosPanel(MenuPrincipal.this);
                cambiarPanel(panelReporteDiarioAdultos, "reporteDiarioAdultos");
            }
        });
        
        // Añadir acción al menú de reporte diario de infantes
        itemReporteDiarioInfantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panelReporteDiarioInfantes = new ReporteDiarioInfantesPanel(MenuPrincipal.this);
                cambiarPanel(panelReporteDiarioInfantes, "reporteDiarioInfantes");
            }
        });
        
        menuReportes.add(itemReporteDiarioAdultos);
        menuReportes.add(itemReporteDiarioInfantes);
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
        menuBar.add(menuVer);
        menuBar.add(menuSocios);
        menuBar.add(menuMovimientos);
        menuBar.add(menuReportes);
        menuBar.add(menuPremioAhorro);
        menuBar.add(menuAyuda);
        
        // Establecer la barra de menú
        setJMenuBar(menuBar);
          // Crear panel de contenido principal que usará CardLayout para cambiar entre paneles
        panelContenido = new JPanel();
        panelContenido.setLayout(new CardLayout());
        panelContenido.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Crear el panel de bienvenida (panel por defecto)
        panelBienvenida = crearPanelBienvenida();
        
        // Añadir el panel de bienvenida al panel de contenido
        panelContenido.add(panelBienvenida, "bienvenida");
        
        // Establecer el panel actual al panel de bienvenida
        panelActual = panelBienvenida;
        
        // Crear el panel principal con layout BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        
        // Barra de estado en la parte inferior
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel lblUsuario = new JLabel("  Usuario: Admin");
        JLabel lblFecha = new JLabel("Fecha: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()) + "  ");
        
        panelEstado.add(lblUsuario, BorderLayout.WEST);
        panelEstado.add(lblFecha, BorderLayout.EAST);
        
        // Añadir paneles al panel principal
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
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
    
    /**
     * Crea el panel de bienvenida que se muestra al inicio
     * @return Panel de bienvenida
     */
    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBackground(new Color(240, 240, 240));
        
        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Caja");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        
        panelCentro.add(lblBienvenida);
        
        panel.add(panelCentro, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cambia el panel actual al panel especificado
     * @param panel Panel a mostrar
     * @param name Nombre identificador del panel
     */
    public void cambiarPanel(JPanel panel, String name) {
        // Eliminar el panel actual si no es el de bienvenida
        if (panelActual != panelBienvenida) {
            panelContenido.remove(panelActual);
        }
        
        // Añadir el nuevo panel
        panelContenido.add(panel, name);
        panelActual = panel;
        
        // Mostrar el nuevo panel
        CardLayout cl = (CardLayout) panelContenido.getLayout();
        cl.show(panelContenido, name);
        
        // Actualizar el panel
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    /**
     * Restaura el panel de bienvenida
     */
    public void mostrarPanelBienvenida() {
        // Si el panel actual no es el de bienvenida, lo eliminamos
        if (panelActual != panelBienvenida) {
            panelContenido.remove(panelActual);
        }
        
        panelActual = panelBienvenida;
        
        // Mostrar el panel de bienvenida
        CardLayout cl = (CardLayout) panelContenido.getLayout();
        cl.show(panelContenido, "bienvenida");
        
        // Actualizar el panel
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
