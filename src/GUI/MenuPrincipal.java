package GUI;

import LOGIC.LoginManager;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JPanel {

    public MenuPrincipal(String username, LoginManager manager) {
        setBackground(Login.FONDO);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Bienvenido, " + username + "!");
        label.setFont(Login.FUENTE_TITULO);
        label.setForeground(Login.ACENTO);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}