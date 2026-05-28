
package LOGIC;

import java.io.File;
import java.io.IOException;


public class LoginManager {
    private static final String BASE_FOLDER = "CTR_RAIZ";
    private static final String USERS_FILE  = BASE_FOLDER + "/users.ctr";
    private static final String USERS_TEMP  = BASE_FOLDER + "/users_temp.ctr";

    private USER currentUser;
    
    public LoginManager() {
        File base = new File(BASE_FOLDER);
        if (!base.exists()) base.mkdir();
        try { new File(USERS_FILE).createNewFile(); } catch (IOException e) {}
        limpiarUsuariosHuerfanos();
        currentUser = cargarUltimoUsuarioActivo();
    }
}
