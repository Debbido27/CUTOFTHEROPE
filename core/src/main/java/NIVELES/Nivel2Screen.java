package NIVELES;

import Game.*;
import com.badlogic.gdx.math.Vector2;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel2Screen extends NivelBaseScreen {

    public Nivel2Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 2);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl1.png";
    }

    /**
     * Diseño del nivel 2:
     *
     *   - Pelota colgada de 3 cuerdas en el centro.
     *   - NomNom arriba, esperando.
     *   - Una burbuja debajo de la pelota: al entrar, la pelota flota hacia NomNom.
     *   - 3 estrellas en la trayectoria.
     *
     * Constructora de Burbuja: new Burbuja(mundo, x, y, radio)
     *   → ya NO recibe fuerzaElevacion (la flotación la controla gravityScale).
     */
    @Override
    protected void crearNivel() {
        final float pelotaX = 7.5f;
        final float pelotaY = 8f;

        // ── pelota ────────────────────────────────────────────────────────────
        crearPelota(pelotaX, pelotaY, 0.3f);

        // ── cuerdas ───────────────────────────────────────────────────────────
        anclarCuerda(pelotaX,        16f,  8, 1.0f);   // central → hacia NomNom
        anclarCuerda(pelotaX + 4f,   14f,  8, 0.9f);   // derecha
        anclarCuerda(pelotaX - 3.5f,  6f,  4, 1.0f);   // izquierda baja

        // ── NomNom ────────────────────────────────────────────────────────────
        colocarNomNom(pelotaX, 17.5f, 0.6f);

        // ── burbuja ───────────────────────────────────────────────────────────
        // Está debajo de la pelota. El jugador corta la cuerda inferior,
        // la pelota cae hacia la burbuja, queda atrapada y flota hacia NomNom.
        // El usuario puede tocar la burbuja para que explote antes de llegar.
        burbujas.add(new Burbuja(mundo, pelotaX, pelotaY - 3f, 0.65f));

        // ── estrellas ─────────────────────────────────────────────────────────
        estrellas.add(new Estrella(mundo, pelotaX,        pelotaY - 3f, 0.22f)); // dentro de la burbuja
        estrellas.add(new Estrella(mundo, pelotaX - 3.5f, 7f,           0.22f)); // izquierda
        estrellas.add(new Estrella(mundo, pelotaX + 1f,   13f,          0.22f)); // en la subida

        // ── cámara ────────────────────────────────────────────────────────────
        limiteInferior = 1f;
        camaraFisica.setToOrtho(false, 18f, 22f);
        camaraFisica.position.set(7.5f, 9f, 0);
        camaraFisica.update();
    }
}