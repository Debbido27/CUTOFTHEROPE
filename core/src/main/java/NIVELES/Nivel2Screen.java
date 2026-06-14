package NIVELES;

import Game.*;
import Game.Obstaculo.TipoObstaculo;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel2Screen extends NivelBaseScreen {

    public Nivel2Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 2);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl1.png"; // ← tu fondo específico
    }

    @Override
protected void crearNivel() {
    // Pelota suspendida en el centro
    float pelotaX = 7.5f;
    float pelotaY = 8f;
    crearPelota(pelotaX, pelotaY, 0.3f);

    // Cuerda 1: debajo de NomNom, parte superior central
    anclarCuerda(pelotaX, 16f, 8, 1f);

    // Cuerda 2: parte superior derecha
    anclarCuerda(pelotaX + 4f, 14f, 8, 0.9f);

    // Cuerda 3: parte inferior izquierda
    anclarCuerda(pelotaX - 3.5f, 6f, 4, 1.0f);

    // NomNom arriba esperando, encima de la ancla de cuerda 1
    colocarNomNom(pelotaX, 17.5f, 0.6f);

    // Burbuja en la parte inferior, contiene Estrella 1
    burbujas.add(new Burbuja(mundo, pelotaX, pelotaY - 3f, 0.6f, 18f));

    // Estrella 1: dentro de la burbuja
    estrellas.add(new Estrella(mundo, pelotaX, pelotaY - 3f, 0.22f));

    // Estrella 2: cerca de la base de la cuerda 3 (izquierda, altura media)
    estrellas.add(new Estrella(mundo, pelotaX - 3.5f, 7f, 0.22f));

    // Estrella 3: más arriba, en la trayectoria ascendente hacia NomNom
    estrellas.add(new Estrella(mundo, pelotaX + 1f, 13f, 0.22f));

    limiteInferior = 1f;

    camaraFisica.setToOrtho(false, 18f, 22f);
    camaraFisica.position.set(7.5f, 9f, 0);
    camaraFisica.update();
}
}