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
public abstract class ElementoNivel {
    protected float x;
    protected float y;
    protected float ancho;
    protected float alto;
    protected TipoElemento tipo;
    protected Texture textura;
    
    public ElementoNivel(float x, float y, float ancho, float alto, TipoElemento tipo, Texture textura){
        this.x=x;
        this.y=y;
        this.ancho=ancho;
        this.alto=alto;
        this.tipo=tipo;
        this.textura=textura;
    }
    
}
