
package GUI;

import LOGIC.LoginManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;


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
    
    // campos login
    private JTextField     campoUsuarioLogin;
    private JPasswordField campoPasswordLogin;

    // campos crear
    private JTextField     campoNombreCompleto;
    private JTextField     campoUsuarioCrear;
    private JPasswordField campoPasswordCrear;
    private String         avatarSeleccionado = "";

    // checks contraseña
    private JLabel checkLongitud;
    private JLabel checkMayuscula;
    private JLabel checkMinuscula;
    private JLabel checkNumero;
    
    
    public Login() {
        setTitle("Cut the Rope");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());
        manager = new LoginManager();
        cardLayout     = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(PANEL);
        panelPrincipal.add(crearPanelMenu(),   "menu");
        panelPrincipal.add(crearPanelLogin(),  "login");
        panelPrincipal.add(crearPanelCrear(),  "crear");
        add(panelPrincipal, BorderLayout.CENTER);
        cardLayout.show(panelPrincipal, "menu");
        setVisible(true);
    }
    
    
    //CREAR PANEL PRINCIPAl
     private JPanel crearPanelMenu() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACENTO, 2),
            BorderFactory.createEmptyBorder(60, 80, 60, 80)
        ));

        JLabel titulo = new JLabel("🍬 Cut the Rope");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(ACENTO);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("¡Alimenta a Om Nom!");
        sub.setFont(FUENTE_SUB);
        sub.setForeground(TEXTO_TENUE);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        JSeparator sep = crearSeparador();

        JButton btnLogin = crearBoton("Iniciar Sesión", BTN_PRIMARIO,   Color.WHITE);
        JButton btnCrear = crearBoton("Crear Jugador",  BTN_SECUNDARIO, Color.WHITE);
        JButton btnSalir = crearBoton("Salir",          BTN_SALIR,      Color.WHITE);

        btnLogin.addActionListener(e -> cardLayout.show(panelPrincipal, "login"));
        btnCrear.addActionListener(e -> cardLayout.show(panelPrincipal, "crear"));
        btnSalir.addActionListener(e -> System.exit(0));

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(sub);
        panel.add(Box.createVerticalStrut(30));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(40));
        panel.add(btnLogin);
        panel.add(Box.createVerticalStrut(14));
        panel.add(btnCrear);
        panel.add(Box.createVerticalStrut(14));
        panel.add(btnSalir);

        return panel;
    }
}
