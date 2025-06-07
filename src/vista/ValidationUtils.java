package vista;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Utilidades para validación de campos y efectos visuales mejorados
 */
public class ValidationUtils {

    // Colores para la validación y estilo visual
    public static final Color COLOR_CAMPO_VALIDO = Color.WHITE;
    public static final Color COLOR_ERROR = new Color(255, 221, 221); // Rosa claro para indicar error
    public static final Color COLOR_CAMPO_DESHABILITADO = new Color(240, 240, 240);
    public static final Color COLOR_BORDE_DESHABILITADO = new Color(200, 200, 200);
    public static final Color COLOR_BORDE_REQUERIDO = new Color(255, 150, 150);
    public static final Color COLOR_CAMPO_LECTURA = new Color(245, 245, 255); // Azul muy claro para campos de solo lectura
    public static final Color COLOR_TEXTO_LECTURA = new Color(0, 0, 128); // Azul oscuro para datos existentes
    
    /**
     * Valida que un campo obligatorio no esté vacío y aplica efectos visuales
     * @param campo Campo a validar
     * @return true si el campo es válido, false si está vacío
     */
    public static boolean validarCampoObligatorio(JTextField campo) {
        boolean esValido = !campo.getText().trim().isEmpty();
        
        if (esValido) {
            // Campo es válido - estilo mejorado con animación visual
            aplicarEstiloValidado(campo);
        } else {
            // Campo no es válido - estilo de error con animación
            aplicarEstiloError(campo);
        }
        
        return esValido;
    }
    
    /**
     * Aplica estilo visual de campo validado correctamente
     * @param campo Campo al que aplicar el estilo
     */
    public static void aplicarEstiloValidado(JTextField campo) {
        final Color colorOriginal = COLOR_CAMPO_VALIDO;
        
        // Borde con estilo de éxito
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 180, 100), 1), // Borde verde suave
            BorderFactory.createEmptyBorder(5, 8, 5, 8) // Padding mejorado
        ));
        
        // Tooltip informativo con formato HTML
        campo.setToolTipText("<html><b>Correcto:</b> El contenido del campo es válido</html>");
        
        // Efecto de transición suave con colores
        try {
            // Secuencia de colores (verde claro -> blanco)
            final Color[] secuenciaColores = {
                new Color(220, 255, 220), // Verde muy claro
                new Color(230, 255, 230),
                new Color(240, 255, 240),
                colorOriginal
            };
            
            // Timer para animar los colores
            final int[] contador = {0};
            javax.swing.Timer timer = new javax.swing.Timer(120, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (contador[0] < secuenciaColores.length) {
                        campo.setBackground(secuenciaColores[contador[0]]);
                        contador[0]++;
                    } else {
                        ((javax.swing.Timer)e.getSource()).stop();
                    }
                }
            });
            timer.start();
        } catch (Exception e) {
            // Si hay algún problema con el timer, usamos color estándar
            campo.setBackground(colorOriginal);
        }
    }
    
    /**
     * Aplica estilo visual de campo con error
     * @param campo Campo al que aplicar el estilo de error
     */
    public static void aplicarEstiloError(JTextField campo) {
        // Fondo de error
        campo.setBackground(COLOR_ERROR);
        
        // Borde rojo para destacar el error
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED, 2), // Borde rojo más grueso
            BorderFactory.createEmptyBorder(5, 8, 5, 8) // Padding consistente
        ));
        
        // Tooltip con formato HTML para mejor visualización
        campo.setToolTipText("<html><b>⚠️ Error:</b> Este campo es <u>obligatorio</u> y no puede estar vacío</html>");
        
        // Efecto de "shake" para llamar la atención
        try {
            final Point posicionOriginal = campo.getLocation();
            final int[] offsets = {2, -2, 3, -3, 2, -2, 1, -1, 0};
            final int[] index = {0};
            
            javax.swing.Timer timerShake = new javax.swing.Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (index[0] < offsets.length) {
                        campo.setLocation(posicionOriginal.x + offsets[index[0]], posicionOriginal.y);
                        index[0]++;
                    } else {
                        campo.setLocation(posicionOriginal);
                        ((javax.swing.Timer)e.getSource()).stop();
                        
                        // Iniciar animación de borde pulsante
                        iniciarAnimacionBordePulsante(campo);
                    }
                }
            });
            timerShake.start();
        } catch (Exception e) {
            // Si hay algún problema con la animación, al menos enfocamos el campo
            campo.requestFocus();
        }
    }
    
    /**
     * Inicia una animación de borde pulsante para un campo con error
     * @param campo El campo al que aplicar la animación
     */
    private static void iniciarAnimacionBordePulsante(JTextField campo) {
        // Colores para la animación pulsante
        final Color[] coloresBorde = {
            new Color(220, 50, 50),    // Rojo intenso
            new Color(240, 100, 100),  // Rosa rojizo
            new Color(250, 150, 150),  // Rosa claro
            new Color(240, 100, 100),  // Rosa rojizo
            new Color(220, 50, 50)     // Rojo intenso
        };
        
        try {
            javax.swing.Timer timerPulse = new javax.swing.Timer(300, new ActionListener() {
                private int indice = 0;
                private int repeticiones = 0;
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Guardar el padding interno original
                    Border paddingInterno = BorderFactory.createEmptyBorder(5, 8, 5, 8);
                    
                    // Cambiar el color del borde según la secuencia
                    campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(coloresBorde[indice], 2),
                        paddingInterno
                    ));
                    
                    indice = (indice + 1) % coloresBorde.length;
                    
                    // Limitar la animación a 2 ciclos completos
                    if (indice == 0) {
                        repeticiones++;
                        if (repeticiones >= 2) {
                            ((javax.swing.Timer)e.getSource()).stop();
                        }
                    }
                }
            });
            timerPulse.setInitialDelay(300); // Pequeña pausa antes de iniciar la pulsación
            timerPulse.start();
        } catch (Exception e) {
            // Ignorar errores de la animación
        }
    }
    
    /**
     * Aplica estilo visual para campos obligatorios sin marcarlos como error
     * @param campo Campo al que aplicar el estilo
     * @param mensaje Mensaje de tooltip a mostrar
     */
    public static void aplicarEstiloCampoObligatorio(JTextField campo, String mensaje) {
        // Color para campos obligatorios - rosa suave con borde más oscuro
        Color colorBorde = COLOR_BORDE_REQUERIDO;
        
        // Aplicar un borde visible que indique que es un campo obligatorio
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorBorde, 2), // Borde más grueso y visible
            BorderFactory.createEmptyBorder(5, 8, 5, 8) // Más padding para mejor visualización
        ));
        
        // Tooltip con formato HTML mejorado
        campo.setToolTipText("<html><b>Campo obligatorio:</b> " + mensaje + "</html>");
    }
    
    /**
     * Método auxiliar para mostrar datos en un campo de texto con mejor formato
     * y efectos visuales para mejor experiencia de usuario
     * @param campo El campo de texto donde mostrar el dato
     * @param valor El valor a mostrar (puede ser null)
     */
    public static void mostrarDatoEnCampo(JTextField campo, Object valor) {
        // Guardar color original para restaurarlo después de la animación
        final Color colorOriginal = campo.getBackground();
        
        if (valor != null) {
            String textoValor = String.valueOf(valor);
            campo.setText(textoValor);
            
            // Destacar visualmente que el campo tiene datos
            if (!textoValor.isEmpty()) {
                // Mejorar la presentación del texto con formato adecuado
                campo.setForeground(COLOR_TEXTO_LECTURA); // Azul oscuro para datos existentes
                campo.setFont(new Font("Arial", Font.PLAIN, 14));
                
                // Efecto de flash suave para indicar que el campo ha sido cargado con datos
                if (!campo.isEnabled()) {
                    // Para campos deshabilitados, aplicar un estilo especial de solo lectura
                    campo.setBackground(COLOR_CAMPO_LECTURA);
                } else {
                    // Animación sutil para indicar carga de datos (solo para campos habilitados)
                    try {
                        final Color[] colores = {
                            new Color(230, 255, 230), // Verde muy claro
                            new Color(240, 255, 240),
                            colorOriginal
                        };
                        
                        final int[] contador = {0};
                        javax.swing.Timer timer = new javax.swing.Timer(100, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (contador[0] < colores.length) {
                                    campo.setBackground(colores[contador[0]]);
                                    contador[0]++;
                                } else {
                                    ((javax.swing.Timer)e.getSource()).stop();
                                }
                            }
                        });
                        timer.start();
                    } catch (Exception e) {
                        campo.setBackground(colorOriginal);
                    }
                }
            }
            
            // Añadir tooltip que muestre el valor completo para textos largos
            if (textoValor.length() > 20) {
                campo.setToolTipText("<html><b>Valor completo:</b><br>" + textoValor + "</html>");
            }
        } else {
            campo.setText("");
            campo.setForeground(Color.BLACK);
            campo.setBackground(colorOriginal);
            campo.setToolTipText(null);
        }
    }
    
    /**
     * Aplica estilo visual para campos deshabilitados
     * @param campo Campo a estilizar
     */
    public static void aplicarEstiloCampoDeshabilitado(JTextField campo) {
        campo.setBackground(COLOR_CAMPO_DESHABILITADO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE_DESHABILITADO), 
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
}
