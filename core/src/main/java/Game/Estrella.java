/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Dell
 */
public class Estrella extends ElementoNivel implements Interactuable {
 private boolean activa;
 
 public Estrella(float x, float y, float ancho, float alto, Texture textura) {
        super(x, y, ancho, alto, TipoElemento.ESTRELLA, textura);
        this.activa = true;
    }
 
    @Override
    public void actualizar(float delta) {
    }
 
    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activa) return; 
        batch.draw(textura, x, y, ancho, alto);
    }
 
    @Override
    public void interactuar() {
        activa = false;
    }
 
    @Override
    public boolean estaActivo() {
        return activa;
    }
 
}