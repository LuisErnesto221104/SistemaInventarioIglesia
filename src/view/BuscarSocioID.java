package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import tools.SocioDAO;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * BuscarSocioIDPanel
 * Panel para buscar el ID de un socio por su nombre y apellido
 */
public class BuscarSocioID extends JPanel {
    private JTextField txtNombre; // Campo para ingresar el nombre del socio
    private JTextField txtApellido; // Campo para ingresar el apellido del socio
    private JCheckBox chkSocioInfantil; // Checkbox para filtrar por tipo de socio infantil
    private JButton btnBuscar; // Botón para buscar el socio
    private JTable tablaSocios; // Tabla para mostrar los resultados de la búsqueda
    private DefaultTableModel modeloTabla; // Modelo de la tabla para manejar los datos
    private MenuPrincipal menuPrincipal; // Referencia al menú principal
    private SocioDAO socioDAO;  // DAO para acceder a los datos de los socios
    
    /**
     * Constructor
     * @param menuPrincipal Referencia al menú principal para poder volver
     */
    public BuscarSocioID(MenuPrincipal menuPrincipal) {
        this.menuPrincipal = menuPrincipal; // Guardar referencia al menú principal
        socioDAO = new SocioDAO(); // Inicializar el DAO para acceder a los datos de los socios
        inicializarComponentes(); // Llamar al método para inicializar los componentes del panel
    }
    
    /**
     * Inicializa los componentes del panel
     */
    private void inicializarComponentes() {
        // Configuración del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(800, 600));
        
        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPrincipal.mostrarPanelBienvenida();
            }
        });
        
        JLabel lblTitulo = new JLabel("BUSCAR ID DE SOCIO", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        panelTitulo.add(btnVolver, BorderLayout.WEST);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Criterios de Búsqueda", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        panelBusqueda.setBackground(new Color(240, 248, 255)); // Fondo azul claro
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelBusqueda.add(lblNombre, gbc);
        
        txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNombre.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        panelBusqueda.add(txtNombre, gbc);
        
        // Apellido
        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panelBusqueda.add(lblApellido, gbc);
        
        txtApellido = new JTextField(20);
        txtApellido.setFont(new Font("Arial", Font.PLAIN, 14));
        txtApellido.setPreferredSize(new Dimension(0, 30)); // Altura fija para el campo
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        panelBusqueda.add(txtApellido, gbc);
        
        // Checkbox para tipo de socio
        chkSocioInfantil = new JCheckBox("Socio Infantil");
        chkSocioInfantil.setFont(new Font("Arial", Font.BOLD, 12));
        chkSocioInfantil.setBackground(new Color(240, 248, 255));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panelBusqueda.add(chkSocioInfantil, gbc);
        
        // Botón buscar
        btnBuscar = new JButton("Buscar Socio");
        btnBuscar.setPreferredSize(new Dimension(150, 30));
        btnBuscar.setIcon(UIManager.getIcon("FileView.directoryIcon")); // Icono de búsqueda
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 12));
        btnBuscar.setBackground(new Color(70, 130, 180)); // Steel Blue
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSocios();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelBusqueda.add(btnBuscar, gbc);
        
        // Tabla de resultados
        String[] columnas = {"ID", "Nombre", "Apellido", "Fecha de Registro", "Dirección", "Teléfono", "Población", "Presentado por"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer todas las celdas no editables
            }
        };
        
        tablaSocios = new JTable(modeloTabla);
        tablaSocios.setRowHeight(25);
        tablaSocios.getTableHeader().setReorderingAllowed(false);
        tablaSocios.getTableHeader().setResizingAllowed(true);
        tablaSocios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaSocios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSocios.setFillsViewportHeight(true);
        
        // Mejorar aspecto de la tabla
        tablaSocios.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSocios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(tablaSocios);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Resultados de la búsqueda", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14)));
        
        // Panel de información
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBackground(new Color(255, 250, 240)); // Fondo amarillento muy suave
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(211, 211, 211)), // Línea superior gris
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Icono de información
        JLabel iconoInfo = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
        panelInfo.add(iconoInfo);
        
        JLabel lblInfo = new JLabel("Ingrese al menos el nombre o apellido del socio para realizar la búsqueda");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 12));
        lblInfo.setForeground(new Color(70, 130, 180)); // Steel Blue
        panelInfo.add(lblInfo);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 10));
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelInfo, BorderLayout.SOUTH);
        
        // Añadir todo al panel principal
        add(panelTitulo, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Añadir KeyListener para buscar al presionar Enter en los campos
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarSocios();
                }
            }
        };
        
        txtNombre.addKeyListener(enterKeyListener);
        txtApellido.addKeyListener(enterKeyListener);
    }
    
    /**
     * Busca socios según los criterios ingresados
     */
    private void buscarSocios() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        boolean esInfantil = chkSocioInfantil.isSelected();
        
        // Validar que al menos un campo tenga datos
        if (nombre.isEmpty() && apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, ingrese al menos el nombre o apellido del socio para realizar la búsqueda.",
                "Información requerida",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        try {
            // Realizar la búsqueda
            List<Map<String, Object>> socios;
            
            if (esInfantil) {
                socios = socioDAO.buscarSociosInfantilesPorNombre(nombre, apellido);
            } else {
                socios = socioDAO.buscarSociosAdultosPorNombre(nombre, apellido);
            }
            
            // Llenar la tabla con los resultados
            if (socios.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron socios que coincidan con los criterios de búsqueda.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            for (Map<String, Object> socio : socios) {
                Object[] fila = new Object[8];
                
                fila[0] = socio.get("NoSocio"); // ID
                fila[1] = socio.get("Nombres"); // Nombre
                fila[2] = socio.get("Apellidos"); // Apellido
                
                // Fecha de registro (diferente nombre de columna según tipo de socio)
                if (esInfantil) {
                    fila[3] = socio.get("Fecha") != null ? sdf.format(socio.get("Fecha")) : "";
                } else {
                    fila[3] = socio.get("FechaRegistro") != null ? sdf.format(socio.get("FechaRegistro")) : "";
                }
                
                fila[4] = socio.get("Direccion"); // Dirección
                fila[5] = socio.get("Telefono"); // Teléfono
                fila[6] = socio.get("Poblacion"); // Población
                fila[7] = socio.get("PresentadoPor"); // Presentado por
                
                modeloTabla.addRow(fila);
            }
            
            // Ajustar tamaños de columnas para mejor visualización
            tablaSocios.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
            tablaSocios.getColumnModel().getColumn(1).setPreferredWidth(120); // Nombre
            tablaSocios.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellido
            tablaSocios.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha
            tablaSocios.getColumnModel().getColumn(4).setPreferredWidth(200); // Dirección
            tablaSocios.getColumnModel().getColumn(5).setPreferredWidth(100); // Teléfono
            tablaSocios.getColumnModel().getColumn(6).setPreferredWidth(120); // Población
            tablaSocios.getColumnModel().getColumn(7).setPreferredWidth(120); // Presentado por
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al buscar socios: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
