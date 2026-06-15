
        package NIVELES;

        import Game.*;
        import Game.Obstaculo.TipoObstaculo;
        import com.badlogic.gdx.physics.box2d.BodyDef;
        import com.badlogic.gdx.math.Vector2;
        import LOGIC.LoginManager;
        import com.cutherope.CutTheRope;

        
        public class Nivel5Screen extends NivelBaseScreen {

        public Nivel5Screen(CutTheRope juego, String usuario, LoginManager gestor) {
        super(juego, usuario, gestor, 5);
        }

        @Override
        protected String rutaFondo() {
        return "images/lvl1.png"; 
        }

        @Override
        protected void crearNivel() {
        float anclaX = 7.5f;
        float anclaY = 22f;
        int   segmentos = 24;
        float largoSeg  = 0.28f;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(anclaX, anclaY);
       

        float dist = segmentos * largoSeg;
        pelota = new Pelota(mundo, anclaX, anclaY - dist, 0.3f);


        float base = anclaY - dist; 

        
        obstaculos.add(new Obstaculo(mundo, anclaX - 4.0f, base - 0.6f, 6.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 4.5f, base - 0.6f, 4.5f, 0.3f, TipoObstaculo.LARGO));

        obstaculos.add(new Obstaculo(mundo, anclaX - 0.5f, base - 1.5f, 0.7f, 0.7f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 3.0f, base - 1.5f, 0.7f, 0.7f, TipoObstaculo.CORTO));

      
        obstaculos.add(new Obstaculo(mundo, anclaX - 5.0f, base - 2.4f, 4.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 1.5f, base - 3.0f, 4.5f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX - 3.5f, base - 3.8f, 4.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 3.5f, base - 4.5f, 4.0f, 0.3f, TipoObstaculo.LARGO));

        obstaculos.add(new Obstaculo(mundo, anclaX + 2.5f, base - 2.0f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX - 2.0f, base - 3.3f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 0.5f, base - 4.2f, 0.65f, 0.65f, TipoObstaculo.CORTO));

    
        obstaculos.add(new Obstaculo(mundo, anclaX - 4.5f, base - 5.2f, 3.0f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 4.5f, base - 5.2f, 3.0f, 0.3f, TipoObstaculo.LARGO));

        obstaculos.add(new Obstaculo(mundo, anclaX - 1.5f, base - 5.8f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 1.5f, base - 5.8f, 0.65f, 0.65f, TipoObstaculo.CORTO));
        obstaculos.add(new Obstaculo(mundo, anclaX,        base - 6.5f, 0.65f, 0.65f, TipoObstaculo.CORTO));

        obstaculos.add(new Obstaculo(mundo, anclaX - 3.0f, base - 6.8f, 3.5f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 3.0f, base - 7.5f, 3.5f, 0.3f, TipoObstaculo.LARGO));

       
        obstaculos.add(new Obstaculo(mundo, anclaX - 4.2f, base - 8.5f, 5.8f, 0.3f, TipoObstaculo.LARGO));
        obstaculos.add(new Obstaculo(mundo, anclaX + 4.2f, base - 8.5f, 5.8f, 0.3f, TipoObstaculo.LARGO));

        burbujas.add(new Burbuja(mundo, anclaX,        base - 2.8f, 0.4f, 6f)); 
        burbujas.add(new Burbuja(mundo, anclaX - 4.8f, base - 4.0f, 0.4f, 7f));  
        burbujas.add(new Burbuja(mundo, anclaX + 4.8f, base - 6.0f, 0.4f, 7f));  
        burbujas.add(new Burbuja(mundo, anclaX - 1.0f, base - 7.8f, 0.4f, 8f));  
        burbujas.add(new Burbuja(mundo, anclaX + 1.0f, base - 9.2f, 0.4f, 8f));  

        estrellas.add(new Estrella(mundo, anclaX - 5.5f, base - 1.2f, 0.22f));

        estrellas.add(new Estrella(mundo, anclaX + 5.5f, base - 6.2f, 0.22f));

        estrellas.add(new Estrella(mundo, anclaX, base - 10.0f, 0.22f));

        camaraFisica.setToOrtho(false, 24f, 32f);
        camaraFisica.position.set(anclaX, anclaY - 12f, 0);
        camaraFisica.update();
        }
        }