
package GUI;

import LOGIC.LoginManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
     
     
     //CREAR PANEL LOGIN
         private JPanel crearPanelLogin() {
           JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACENTO, 2),
            BorderFactory.createEmptyBorder(50, 80, 50, 80)
        ));

        JLabel titulo = new JLabel("Iniciar Sesión");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(ACENTO);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        JSeparator sep = crearSeparador();

        JLabel lblUser = crearLabel("Usuario");
        campoUsuarioLogin = crearCampoTexto();

        JLabel lblPass = crearLabel("Contraseña");
        campoPasswordLogin = crearCampoPassword();

        // fila contraseña + ojo
        JPanel filaPass = crearFilaPassword(campoPasswordLogin);

        JLabel mensajeLogin = crearMensaje();

        JButton btnAceptar = crearBoton("Ingresar", BTN_PRIMARIO,   Color.WHITE);
        JButton btnVolver  = crearBoton("Volver",   BTN_SECUNDARIO, Color.WHITE);

        btnAceptar.addActionListener(e -> {
            String user = campoUsuarioLogin.getText().trim();
            String pass = new String(campoPasswordLogin.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                mostrarError(mensajeLogin, "Completa todos los campos.");
            } else if (!manager.userExiste(user)) {
                mostrarError(mensajeLogin, "Usuario no existe.");
            } else if (manager.login(user, pass)) {
                dispose();
                // new MenuPrincipal(user, manager);  <- descomentar cuando exista
            } else {
                mostrarError(mensajeLogin, "Contraseña incorrecta.");
            }
        });

        btnVolver.addActionListener(e -> {
            campoUsuarioLogin.setText("");
            campoPasswordLogin.setText("");
            mensajeLogin.setText(" ");
            cardLayout.show(panelPrincipal, "menu");
        });

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(30));
        panel.add(lblUser);
        panel.add(Box.createVerticalStrut(6));
        panel.add(campoUsuarioLogin);
        panel.add(Box.createVerticalStrut(16));
        panel.add(lblPass);
        panel.add(Box.createVerticalStrut(6));
        panel.add(filaPass);
        panel.add(Box.createVerticalStrut(20));
        panel.add(mensajeLogin);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnAceptar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnVolver);

        return panel;
    }
         
     
           private JPanel crearPanelCrear() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACENTO, 2),
            BorderFactory.createEmptyBorder(30, 80, 30, 80)
        ));

        JLabel titulo = new JLabel("Crear Jugador");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(ACENTO);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        JSeparator sep = crearSeparador();

        JLabel lblNombre = crearLabel("Nombre Completo");
        campoNombreCompleto = crearCampoTexto();

        JLabel lblUser = crearLabel("Nombre de Usuario");
        campoUsuarioCrear = crearCampoTexto();

        JLabel lblPass = crearLabel("Contraseña");
        campoPasswordCrear = crearCampoPassword();
        JPanel filaPass = crearFilaPassword(campoPasswordCrear);

        JPanel panelChecks = crearPanelChecks();

        campoPasswordCrear.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                actualizarChecks(new String(campoPasswordCrear.getPassword()));
            }
        });

        JLabel lblAvatar = crearLabel("Elige tu avatar");
        JPanel panelAvatares = crearPanelAvatares();

        JLabel mensajeCrear = crearMensaje();

        JButton btnCrear  = crearBoton("Crear",  BTN_PRIMARIO,   Color.WHITE);
        JButton btnVolver = crearBoton("Volver", BTN_SECUNDARIO, Color.WHITE);

        btnCrear.addActionListener(e -> {
            String nombre = campoNombreCompleto.getText().trim();
            String user   = campoUsuarioCrear.getText().trim();
            String pass   = new String(campoPasswordCrear.getPassword());

            if (nombre.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                mostrarError(mensajeCrear, "Completa todos los campos.");
            } else if (manager.userExiste(user)) {
                mostrarError(mensajeCrear, "Ese usuario ya existe.");
            } else if (!validarPassword(pass)) {
                mostrarError(mensajeCrear, "La contraseña no cumple los requisitos.");
            } else if (manager.crearUser(user, pass, nombre)) {
                if (!avatarSeleccionado.isEmpty())
                    manager.cambiarAvatar(user, avatarSeleccionado);
                mostrarExito(mensajeCrear, "¡Jugador creado!");
                campoNombreCompleto.setText("");
                campoUsuarioCrear.setText("");
                campoPasswordCrear.setText("");
                actualizarChecks("");
            } else {
                mostrarError(mensajeCrear, "Error al crear jugador.");
            }
        });

        btnVolver.addActionListener(e -> {
            campoNombreCompleto.setText("");
            campoUsuarioCrear.setText("");
            campoPasswordCrear.setText("");
            mensajeCrear.setText(" ");
            actualizarChecks("");
            cardLayout.show(panelPrincipal, "menu");
        });

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(16));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campoNombreCompleto);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblUser);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campoUsuarioCrear);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblPass);
        panel.add(Box.createVerticalStrut(5));
        panel.add(filaPass);
        panel.add(Box.createVerticalStrut(8));
        panel.add(panelChecks);
        panel.add(Box.createVerticalStrut(14));
        panel.add(lblAvatar);
        panel.add(Box.createVerticalStrut(8));
        panel.add(panelAvatares);
        panel.add(Box.createVerticalStrut(14));
        panel.add(mensajeCrear);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnCrear);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnVolver);

        return panel;
    }
           
           
           //HELPERS
        private JPanel crearFilaPassword(JPasswordField campo) {
        JPanel fila = new JPanel(new BorderLayout(6, 0));
        fila.setBackground(PANEL);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JButton ojo = new JButton("👁");
        ojo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        ojo.setBackground(CAMPO_BORDE);
        ojo.setForeground(TEXTO);
        ojo.setFocusPainted(false);
        ojo.setBorderPainted(false);
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ojo.setPreferredSize(new Dimension(40, 40));

        final boolean[] visible = {false};
        ojo.addActionListener(e -> {
            visible[0] = !visible[0];
            campo.setEchoChar(visible[0] ? (char) 0 : '●');
        });

        fila.add(campo, BorderLayout.CENTER);
        fila.add(ojo,   BorderLayout.EAST);
        return fila;
    }
        
        
         private JPanel crearPanelChecks() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        checkLongitud  = crearCheckLabel("Mínimo 6 caracteres");
        checkMayuscula = crearCheckLabel("Una mayúscula");
        checkMinuscula = crearCheckLabel("Una minúscula");
        checkNumero    = crearCheckLabel("Un número");

        panel.add(checkLongitud);
        panel.add(Box.createVerticalStrut(3));
        panel.add(checkMayuscula);
        panel.add(Box.createVerticalStrut(3));
        panel.add(checkMinuscula);
        panel.add(Box.createVerticalStrut(3));
        panel.add(checkNumero);
        return panel;
    }
         
         
         private JLabel crearCheckLabel(String texto) {
        JLabel lbl = new JLabel("✗  " + texto);
        lbl.setFont(FUENTE_CHECK);
        lbl.setForeground(CHECK_MAL);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }
         
         
         private void actualizarChecks(String pass) {
        setCheck(checkLongitud,  pass.length() >= 6,          "Mínimo 6 caracteres");
        setCheck(checkMayuscula, pass.matches(".*[A-Z].*"),   "Una mayúscula");
        setCheck(checkMinuscula, pass.matches(".*[a-z].*"),   "Una minúscula");
        setCheck(checkNumero,    pass.matches(".*[0-9].*"),   "Un número");
    }
         
        private void setCheck(JLabel lbl, boolean ok, String texto) {
        lbl.setText((ok ? "✓  " : "✗  ") + texto);
        lbl.setForeground(ok ? CHECK_OK : CHECK_MAL);
    }

    private boolean validarPassword(String pass) {
        return pass.length() >= 6
            && pass.matches(".*[A-Z].*")
            && pass.matches(".*[a-z].*")
            && pass.matches(".*[0-9].*");
    }
    
    
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_LABEL);
        lbl.setForeground(TEXTO_TENUE);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(FUENTE_CAMPO);
        campo.setForeground(TEXTO);
        campo.setBackground(CAMPO_FONDO);
        campo.setCaretColor(ACENTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CAMPO_BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setAlignmentX(LEFT_ALIGNMENT);
        return campo;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(FUENTE_CAMPO);
        campo.setForeground(TEXTO);
        campo.setBackground(CAMPO_FONDO);
        campo.setCaretColor(ACENTO);
        campo.setEchoChar('●');
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CAMPO_BORDE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setAlignmentX(LEFT_ALIGNMENT);
        return campo;
    }

    private JButton crearBoton(String texto, Color fondo, Color colorTexto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setForeground(colorTexto);
        btn.setBackground(fondo);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        Color hover = fondo.brighter();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(fondo); }
        });
        return btn;
    }

    private JSeparator crearSeparador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(CAMPO_BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private JLabel crearMensaje() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(FUENTE_LABEL);
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        return lbl;
    }

    private void mostrarError(JLabel lbl, String msg) {
        lbl.setForeground(CHECK_MAL);
        lbl.setText(msg);
    }

    private void mostrarExito(JLabel lbl, String msg) {
        lbl.setForeground(CHECK_OK);
        lbl.setText(msg);
    }
}
