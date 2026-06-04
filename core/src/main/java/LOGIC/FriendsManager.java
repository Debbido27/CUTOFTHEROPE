
package LOGIC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FriendsManager {
    
    private static final String BASE_FOLDER = "../assets/CTR_RAIZ";    
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
    
    
    public boolean enviarSolicitud(String de, String para, LoginManager loginManager) {
        if (loginManager.buscarUser(para) == null) return false;
        if (sonAmigos(de, para)) return false;
        if (tieneSolicitudPendiente(de, para)) return false;
        return agregarLinea(getSolicitudesPath(para), de);
    }

    public boolean aceptarSolicitud(String solicitante, String receptor) {
        if (!tieneSolicitudPendiente(solicitante, receptor)) return false;
        eliminarLinea(getSolicitudesPath(receptor), solicitante);
        agregarLinea(getAmigosPath(receptor), solicitante);
        agregarLinea(getAmigosPath(solicitante), receptor);
        return true;
    }
    
    public boolean rechazarSolicitud(String solicitante, String receptor) {
        return eliminarLinea(getSolicitudesPath(receptor), solicitante);
    }

    public boolean eliminarAmigo(String userA, String userB) {
        boolean a = eliminarLinea(getAmigosPath(userA), userB);
        boolean b = eliminarLinea(getAmigosPath(userB), userA);
        return a && b;
    }

    public boolean sonAmigos(String userA, String userB) {
        return contieneLinea(getAmigosPath(userA), userB);
    }

    public boolean tieneSolicitudPendiente(String solicitante, String receptor) {
        return contieneLinea(getSolicitudesPath(receptor), solicitante);
    }

    public String[] getAmigos(String username) {
        return leerLineas(getAmigosPath(username));
    }

    public String[] getSolicitudes(String username) {
        return leerLineas(getSolicitudesPath(username));
    }

    public int contarAmigos(String username) {
        return getAmigos(username).length;
    }
    
      private boolean agregarLinea(String path, String valor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(valor);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error escribiendo en " + path + ": " + e.getMessage());
            return false;
        }
    }

    private boolean eliminarLinea(String path, String valor) {
        File file = new File(path);
        if (!file.exists()) return false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String linea;
            boolean encontrado = false;
            while ((linea = br.readLine()) != null) {
                if (linea.equals(valor) && !encontrado) {
                    encontrado = true;
                } else {
                    sb.append(linea).append(System.lineSeparator());
                }
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(sb.toString());
            bw.close();
            return encontrado;
        } catch (IOException e) {
            System.out.println("Error eliminando linea: " + e.getMessage());
            return false;
        }
    }

    private boolean contieneLinea(String path, String valor) {
        File file = new File(path);
        if (!file.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.equals(valor)) return true;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo " + path);
        }
        return false;
    }

    private String[] leerLineas(String path) {
        File file = new File(path);
        if (!file.exists()) return new String[0];
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String[] temp = new String[500];
            int total = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) temp[total++] = linea.trim();
            }
            String[] resultado = new String[total];
            for (int i = 0; i < total; i++) resultado[i] = temp[i];
            return resultado;
        } catch (IOException e) {
            System.out.println("Error leyendo lineas: " + e.getMessage());
            return new String[0];
        }
    }
    
    
    

    
}
