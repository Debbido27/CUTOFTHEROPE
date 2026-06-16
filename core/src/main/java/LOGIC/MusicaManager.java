package LOGIC;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicaManager {

    private static MusicaManager instancia;
    private Music musica;

    private MusicaManager() {
        musica = Gdx.audio.newMusic(Gdx.files.internal("audio/Cut the Rope Theme.mp3"));
        musica.setLooping(true);
        musica.setVolume(1.0f);
    }

    public static MusicaManager getInstance() {
        if (instancia == null) instancia = new MusicaManager();
        return instancia;
    }

    public void reproducir() {
        if (!musica.isPlaying()) musica.play();
    }

    public void pausar() {
        if (musica.isPlaying()) musica.pause();
    }

    public void setVolumen(float vol) {
        musica.setVolume(vol);
    }

    public float getVolumen() {
        return musica.getVolume();
    }

    public boolean isReproduciendo() {
        return musica.isPlaying();
    }

    public void aplicarPreferencias(boolean activa, float volumen) {
        setVolumen(volumen);
        if (activa) reproducir();
        else        pausar();
    }

    public void dispose() {
        if (musica != null) musica.dispose();
        instancia = null;
    }
}
