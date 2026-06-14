
package NIVELES;

import Game.*;
import Game.Obstaculo.TipoObstaculo;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel3Screen extends NivelBaseScreen {

    public Nivel3Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 3);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl1.png"; // ← tu fondo específico
    }

    @Override
protected void crearNivel() {
    // Pelota en el centro izquierdo
    float pelotaX = 4.5f;
    float pelotaY = 13f;
    crearPelota(pelotaX, pelotaY, 0.3f);

    // Cuerda 1: arriba de la pelota, corta, casi perpendicular
    anclarCuerda(pelotaX, pelotaY + 3.5f, 8, 0.32f);

    // Cuerda 2: misma altura que cuerda 1 pero más a la derecha, más larga
    anclarCuerda(pelotaX + 4.5f, pelotaY + 3.5f, 13, 0.32f);

    // Cuerda 3: centro, alineada con cuerda 2 pero más abajo
    // su base está ligeramente por debajo de la pelota
    anclarCuerda(pelotaX + 2.5f, pelotaY - 1.5f, 10, 0.32f);

    // Cuerda 4: centro, debajo del obstáculo 1 y encima del obstáculo 2
    anclarCuerda(pelotaX + 2.5f, pelotaY - 5.5f, 8, 0.32f);

    // Obstáculo 1: entre base de cuerda 3 y base de cuerda 4
    obstaculos.add(new Obstaculo(mundo,
            pelotaX + 2.5f, pelotaY - 3.5f,
            4.5f, 0.3f, TipoObstaculo.LARGO));

    // Obstáculo 2: debajo de la base de cuerda 4
    obstaculos.add(new Obstaculo(mundo,
            pelotaX + 2.5f, pelotaY - 7.5f,
            4.5f, 0.3f, TipoObstaculo.LARGO));

    // Estrella 1: derecha de cuerda 3, junto al lado derecho del obstáculo 1
    estrellas.add(new Estrella(mundo, pelotaX + 5.5f, pelotaY - 2.0f, 0.22f));

    // Estrella 2: debajo de estrella 1, lado derecho de cuerda 4
    estrellas.add(new Estrella(mundo, pelotaX + 4.2f, pelotaY - 6.5f, 0.22f));

    // Estrella 3: misma altura que estrella 2, lado izquierdo de cuerda 4
    estrellas.add(new Estrella(mundo, pelotaX + 0.8f, pelotaY - 6.5f, 0.22f));

    // NomNom abajo al centro
    colocarNomNom(pelotaX + 2.5f, pelotaY - 11f, 0.6f);

    limiteInferior = pelotaY - 14f;

    camaraFisica.setToOrtho(false, 18f, 24f);
    camaraFisica.position.set(pelotaX + 2f, pelotaY - 5f, 0);
    camaraFisica.update();
}
}
