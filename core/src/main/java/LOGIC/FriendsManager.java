
package LOGIC;

import java.io.File;
import java.io.IOException;


public class FriendsManager {
    
    private static final String BASE_FOLDER ="../assets/CTR._RAIZ";
    
    private String getAmigosPath(String username){
        return BASE_FOLDER+"/"+username+"/amigos.ctr";
    }
    
    private String getSolicitudesPath(String username) {
        File f = new File(BASE_FOLDER + "/" + username + "/solicitudes.ctr");
        if (!f.exists()) {
            try { f.createNewFile(); } catch (IOException e) {}
        }
        return f.getPath();
    }
}
