/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game;

import com.badlogic.gdx.graphics.Texture;

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
 
}