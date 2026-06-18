/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NIVELES;

/**
 *
 * @author Dell
 */

import Game.Cuerda;
import Game.Estrella;
import Game.Pelota;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel1Screen extends NivelBaseScreen {

    public Nivel1Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 1);
        construirUI();
    }
    public Nivel1Screen(CutTheRope juego, String usuario, LoginManager gestor,
                        String retoRetador, String retoRetado) {
        super(juego, usuario, gestor, 1, retoRetador, retoRetado);
    }
    @Override
    protected String rutaFondo() {
        return "images/lvl1.png"; // ← tu fondo específico
    }

    @Override
protected void crearNivel() {
    float anclaX = 4f, anclaY = 5.5f;
    int   segmentos = 10;
    float largoSeg  = 0.3f;
    float dist = segmentos * largoSeg;

    crearPelota(anclaX, anclaY - dist, 0.3f);
    anclarCuerda(anclaX, anclaY, segmentos, largoSeg);

    float ey = anclaY - dist - 0.8f;
    estrellas.add(new Estrella(mundo, anclaX, ey,        0.2f));
    estrellas.add(new Estrella(mundo, anclaX, ey - 0.8f, 0.2f));
    estrellas.add(new Estrella(mundo, anclaX, ey - 1.6f, 0.2f));

    colocarNomNom(anclaX, ey - 2.6f, 0.5f);
    limiteInferior = anclaY - dist - 6f;

    camaraFisica.setToOrtho(false, 16f, 12f);
    camaraFisica.position.set(anclaX, anclaY - 4f, 0);
    camaraFisica.update();
}
}
