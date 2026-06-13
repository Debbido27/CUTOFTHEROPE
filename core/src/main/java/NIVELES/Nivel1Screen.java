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
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl1.png"; // ← tu fondo específico
    }

    @Override
    protected void crearNivel() {
        // Nivel 1: fácil, cuerda corta, estrellas accesibles
        float anclaX = 4f, anclaY = 5.5f;
        int segmentos = 10;
        float largoSeg = 0.3f;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(anclaX, anclaY);
        anclaBody = mundo.createBody(def);
        anclaPos  = new Vector2(anclaX, anclaY);

        float dist = segmentos * largoSeg;
        pelota = new Pelota(mundo, anclaX, anclaY - dist, 0.3f);
        cuerda = new Cuerda(mundo, anclaBody, anclaPos, segmentos, largoSeg, pelota.getBody(), true);

        float ex = anclaX, ey = anclaY - dist - 0.8f;
        estrellas.add(new Estrella(mundo, ex, ey,        0.2f));
        estrellas.add(new Estrella(mundo, ex, ey - 0.8f, 0.2f));
        estrellas.add(new Estrella(mundo, ex, ey - 1.6f, 0.2f));

        camaraFisica.setToOrtho(false, 16f, 12f);
        camaraFisica.position.set(anclaX, anclaY - 4f, 0);
        camaraFisica.update();
    }
}