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
        return "images/lvl2_background.png"; // Cambia a fondo nivel 2
    }

    @Override
    protected void crearNivel() {
        // ─────────────────────────────────────────────────────────────
        // NIVEL 2: "CUERDAS CRUZADAS"
        // Puzzle: Cortar las cuerdas en el orden correcto para que la 
        // pelota atraviese los obstáculos y llegue a NomNom
        // ─────────────────────────────────────────────────────────────
        
        // Posición de la pelota
        float pelotaX = 7.5f;
        float pelotaY = 16f;
        
        // Crear la pelota
        pelota = new Pelota(mundo, pelotaX, pelotaY, 0.3f);
        
        // ─── CREAR ANCLAS (puntos fijos donde cuelgan las cuerdas) ───
        
        // Ancla principal (arriba izquierda)
        Body ancla1 = crearAncla(5.5f, 16.5f);
        
        // Ancla superior derecha
        Body ancla2 = crearAncla(9.5f, 16.5f);
        
        // Ancla intermedia (centro)
        Body ancla3 = crearAncla(7.5f, 13.5f);
        
        // Ancla baja izquierda
        Body ancla4 = crearAncla(4.5f, 10f);
        
        // ─── CREAR CUERDAS ───────────────────────────────────────────
        
        // Cuerda 1: Ancla1 -> Pelota (primera cuerda que sostiene la pelota)
        crearCuerda(ancla1, posicionesAnclas.get(0), 10, 0.32f);
        
        // Cuerda 2: Ancla2 -> Pelota (segunda cuerda, crea tensión cruzada)
        crearCuerda(ancla2, posicionesAnclas.get(1), 10, 0.32f);
        
        // Cuerda 3: Ancla3 -> Pelota (cuerda de seguridad intermedia)
        crearCuerda(ancla3, posicionesAnclas.get(2), 6, 0.32f);
        
        // ─── OBSTÁCULOS ESTRATÉGICOS ─────────────────────────────────
        
        // Plataforma inclinada izquierda - guía la pelota hacia la derecha
        obstaculos.add(new Obstaculo(mundo, 3.5f, 12f, 2.5f, 0.25f, TipoObstaculo.LARGO));
        
        // Plataforma inclinada derecha (rotada ligeramente)
        obstaculos.add(new Obstaculo(mundo, 10.5f, 9f, 2.5f, 0.25f, TipoObstaculo.LARGO));
        
        // Bloque central - divide el camino
        obstaculos.add(new Obstaculo(mundo, 7.5f, 7.5f, 1.2f, 1.2f, TipoObstaculo.CORTO));
        
        // Pequeño obstáculo en el fondo
        obstaculos.add(new Obstaculo(mundo, 6f, 4.5f, 0.8f, 0.8f, TipoObstaculo.CORTO));
        
        // ─── BURBUJAS (ayudan a subir) ───────────────────────────────
        
        // Burbuja izquierda - permite alcanzar estrella escondida
        burbujas.add(new Burbuja(mundo, 3f, 10.5f, 0.4f, 7f));
        
        // Burbuja derecha - ayuda a subir al segundo nivel
        burbujas.add(new Burbuja(mundo, 10f, 5f, 0.4f, 7f));
        
        // ─── ESTRELLAS (coleccionables) ──────────────────────────────
        
        // Estrella 1: Fácil, en el camino directo
        estrellas.add(new Estrella(mundo, 7.5f, 14.5f, 0.22f));
        
        // Estrella 2: Difícil, requiere cortar cuerdas en orden específico
        estrellas.add(new Estrella(mundo, 4f, 8.5f, 0.22f));
        
        // Estrella 3: Muy difícil, requiere usar burbuja
        estrellas.add(new Estrella(mundo, 10.5f, 3f, 0.22f));
        
        // ─── NOMNOM (objetivo) ───────────────────────────────────────
        nomnom = new NomNom(mundo, 7.5f, 1.5f, 0.35f);
        
        // ─── LIMITE INFERIOR ─────────────────────────────────────────
        limiteInferior = -2f;
        
        // ─── CONFIGURAR CÁMARA ───────────────────────────────────────
        camaraFisica.setToOrtho(false, 18f, 20f);
        camaraFisica.position.set(7.5f, 9f, 0);
        camaraFisica.update();
    }
}