package LOGIC;

public class Idioma {

    private static boolean ingles = false;

    public static void setIngles(boolean val) { ingles = val; }
    public static boolean isIngles()          { return ingles; }

    public static String get(String clave) {
        switch (clave) {
            // Menú principal
            case "jugar":           return ingles ? "Play"           : "Jugar";
            case "crear_jugador":   return ingles ? "Create Player"  : "Crear Jugador";
            case "iniciar_sesion":  return ingles ? "Login"          : "Iniciar Sesion";
            case "ajustes":         return ingles ? "Settings"       : "Ajustes";
            case "perfil":          return ingles ? "Profile"        : "Perfil";
            case "salir":           return ingles ? "Exit"           : "Salir";
            case "volver":          return ingles ? "Back"           : "Volver";
            // Preferencias
            case "preferencias":    return ingles ? "Preferences"    : "Preferencias";
            case "idioma":          return ingles ? "Language"       : "Idioma";
            case "musica":          return ingles ? "Music"          : "Musica";
            case "volumen":         return ingles ? "Volume"         : "Volumen";
            case "guardado":        return ingles ? "Saved!"         : "Guardado!";
            // Login / Crear
            case "ingresar":        return ingles ? "Login"          : "Ingresar";
            case "crear":           return ingles ? "Create"         : "Crear";
            case "usuario":         return ingles ? "Username"       : "Usuario";
            case "contrasena":      return ingles ? "Password"       : "Contrasena";
            case "nombre_completo": return ingles ? "Full Name"      : "Nombre Completo";
            case "ver_contrasena":  return ingles ? " Show password" : " Ver contrasena";
            case "elegir_avatar":   return ingles ? "Choose Avatar"  : "Elegir Avatar";
            case "elige_avatar":    return ingles ? "Choose your Avatar" : "Elige tu Avatar";
            case "anterior":        return ingles ? "< Previous"     : "< Anterior";
            case "siguiente":       return ingles ? "Next >"         : "Siguiente >";
            case "cancelar":        return ingles ? "Cancel"         : "Cancelar";
            // Ajustes
            case "eliminar_cuenta": return ingles ? "Delete Account" : "Eliminar Cuenta";
            case "cambiar_pass":    return ingles ? "Change Password": "Cambiar Contrasena";
            // Niveles
            case "seleccionar_nivel": return ingles ? "Select Level"  : "Seleccionar Nivel";
            case "nivel":           return ingles ? "Level"          : "Nivel";
            case "bloqueado":       return ingles ? "Locked"         : "Bloqueado";
            case "amigos":          return ingles ? "Friends"        : "Amigos";
            case "ranking":         return ingles ? "Ranking"        : "Ranking";
            default:                return clave;
        }
    }
}
