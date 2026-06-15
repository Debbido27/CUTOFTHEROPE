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
    
    public  abstract void actualizar(float delta);
    
    public abstract void dibujar(SpriteBatch batch);
    
    public float getX() {return x;}
    public float getY() {return y;}
    public float getAncho(){return ancho;}
    public float getAlto(){return alto;}
    public TipoElemento getTipo(){return tipo;}
    
    public void dispose(){
        if(textura!=null) textura.dispose();
    }
}
