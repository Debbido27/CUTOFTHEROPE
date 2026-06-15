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
    float bajar = 2.5f;
    float pelotaX = 7.5f;
    float pelotaY = 15f - bajar; // 12.5f

    crearPelota(pelotaX, pelotaY, 0.3f);

    // Cuerda 1: corta, vertical, sostiene la pelota justo bajo NomNom
    anclarCuerda(pelotaX, 18f - bajar, 3, 1.0f);

    // Cuerda 2: arriba-derecha, holgura para no frenar la caída
    anclarCuerda(pelotaX + 5f, 15f - bajar, 10, 0.95f);

    // Cuerda 3: ancla subida para que la cuerda sea más corta
    // Ancla en (4.0, 10.5) → distancia a pelota ≈ 3.2u, cuerda justa
anclarCuerda(pelotaX - 3.5f, 5f, 5, 1.5f);

    // NomNom arriba (NO SE TOCA)
    colocarNomNom(pelotaX, 19f, 0.6f);

    // Burbuja justo debajo de la pelota
    burbujas.add(new Burbuja(mundo, pelotaX, 7f - bajar, 0.6f, 9f));

    // Estrella 1: dentro de la burbuja
    estrellas.add(new Estrella(mundo, pelotaX, 7f - bajar, 0.22f));

    // Estrella 2: subida para coincidir con la trayectoria de cuerda 3
    // Antes: (4.5f, 7.5f) → Ahora: (5.5f, 8.5f) — más alta y más central
    estrellas.add(new Estrella(mundo, pelotaX - 2.5f, 9f, 0.22f));

    // Estrella 3: en la trayectoria hacia NomNom
    estrellas.add(new Estrella(mundo, pelotaX + 1.5f, 13f - bajar, 0.22f));

    limiteInferior = 1f;
    camaraFisica.setToOrtho(false, 18f, 22f);
    camaraFisica.position.set(7.5f, 11f, 0);
    camaraFisica.update();
}
}