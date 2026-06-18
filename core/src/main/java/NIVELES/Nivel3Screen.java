package NIVELES;

import Game.*;
import LOGIC.LoginManager;
import com.cutherope.CutTheRope;

public class Nivel3Screen extends NivelBaseScreen {

    public Nivel3Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 3);
        construirUI();
    }

    public Nivel3Screen(CutTheRope juego, String usuario, LoginManager gestor,
                        String retoRetador, String retoRetado) {
        super(juego, usuario, gestor, 3, retoRetador, retoRetado);
    }

    @Override
    protected String rutaFondo() {
        return "images/lvl23.png";
    }

    @Override
    protected void crearNivel() {
        // caramelo en la V entre ambas anclas; cuerda izq casi tensa, der con holgura
        // -> la izq jala mas fuerte hacia arriba-izquierda => oscila hacia omnom
        float pelotaX = 9f, pelotaY = 6f;

        crearPelota(pelotaX, pelotaY, 0.3f);

        // ancla izquierda: debajo de omnom (y=17), X alineada con burbuja izq y omnom
        // dist (3.5,12) -> (9,6) ~= 8.14  =>  10 * 0.83 = 8.3 (casi tensa)
        // fondo del pendulo izquierdo: (3.5, 12-8.3) = (3.5, 3.7) => cae en burbuja izq
        anclarCuerda(3.5f, 12f, 10, 0.83f);

        // ancla derecha: mas arriba y a la derecha => V amplia, arco mas largo
        // dist (13,15) -> (9,6) ~= 9.85  =>  11 * 0.95 = 10.45 (mas holgura, menos tension)
        anclarCuerda(13f, 15f, 11, 0.95f);

        estrellas.add(new Estrella(mundo,  6f,  9f,  0.22f));
        estrellas.add(new Estrella(mundo, 13f,  9f,  0.22f));
        estrellas.add(new Estrella(mundo,  9f,  4.5f, 0.22f));

        // burbuja izq alineada con omnom (X=3.5); al entrar con velocidad izq
        // la burbuja flota y el caramelo deriva ~1 unidad izq hasta omnom en X=2.5
        burbujas.add(new Burbuja(mundo,  3.5f, 3.5f, 1.0f));
        burbujas.add(new Burbuja(mundo,  9f,   3.5f, 1.0f));
        burbujas.add(new Burbuja(mundo, 13f,   3.5f, 1.0f));

        colocarNomNom(2.5f, 17f, 0.6f);

        limiteInferior = 0f;

        camaraFisica.setToOrtho(false, 18f, 24f);
        camaraFisica.position.set(9f, 12f, 0);
        camaraFisica.update();
    }
}
