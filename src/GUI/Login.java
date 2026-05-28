
package GUI;

import LOGIC.LoginManager;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Login extends JFrame {
    
    //PALETA DE COLORES
    static final Color FONDO          = new Color(245, 235, 210);
    static final Color PANEL          = new Color(255, 248, 225);
    static final Color ACENTO         = new Color(85, 150, 80);
    static final Color TEXTO          = new Color(60, 40, 20);
    static final Color TEXTO_TENUE    = new Color(140, 110, 70);
    static final Color CAMPO_FONDO    = new Color(255, 253, 240);
    static final Color CAMPO_BORDE    = new Color(180, 150, 100);
    static final Color BTN_PRIMARIO   = new Color(85, 150, 80);
    static final Color BTN_SECUNDARIO = new Color(200, 160, 80);
    static final Color BTN_SALIR      = new Color(180, 70, 50);
    static final Color CHECK_OK       = new Color(60, 140, 60);
    static final Color CHECK_MAL      = new Color(180, 70, 50);
    
    //FUENTES
    static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD,  28);
    static final Font FUENTE_SUB    = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FUENTE_LABEL  = new Font("SansSerif", Font.PLAIN, 12);
    static final Font FUENTE_CAMPO  = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FUENTE_BOTON  = new Font("SansSerif", Font.BOLD,  13);
    static final Font FUENTE_CHECK  = new Font("SansSerif", Font.PLAIN, 11);
    
    //ESTADO
    private LoginManager manager;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
}
