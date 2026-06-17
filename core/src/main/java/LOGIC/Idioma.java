package LOGIC;

public class Idioma {

    public enum Clave {
        JUGAR, CREAR_JUGADOR, INICIAR_SESION, AJUSTES, PERFIL, SALIR, VOLVER,
        PREFERENCIAS, IDIOMA, MUSICA, VOLUMEN,
        INGRESAR, CREAR, USUARIO, CONTRASENA, NOMBRE_COMPLETO,
        VER_CONTRASENA, ELEGIR_AVATAR, ELIGE_AVATAR,
        ANTERIOR, SIGUIENTE, CANCELAR,
        ESTADISTICAS, CERRAR_SESION, RANKING, AMIGOS,
        SELECCIONAR_NIVEL, NIVEL, BLOQUEADO,
        BIENVENIDO, ALIMENTA,
        MI_PERFIL, VER_INFORMACION, CAMBIAR_USUARIO, CAMBIAR_CONTRASENA, CAMBIAR_AVATAR,
        DATOS_PERSONALES, PROGRESO_JUEGO, HISTORIAL, VER_AMIGOS_RIVALES,
        VOLVER_PERFIL, NOMBRE_COMPLETO_LABEL, FECHA_REGISTRO, ULTIMA_SESION, AVATAR,
        NIVELES_COMPLETADOS, PUNTUACION_GENERAL, ESTRELLAS_TOTALES,
        PARTIDAS_JUGADAS, FALLOS_TOTALES, TIEMPO_JUGADO,
        HISTORIAL_PARTIDAS, SIN_HISTORIAL, RANKING_GENERAL, SIN_RANKING,
        AMIGOS_RIVALES, SIN_AMIGOS, VALIDAR, GUARDAR,
        CONTRASENA_ACTUAL, NUEVA_CONTRASENA, CONFIRMA_CONTRASENA,
        NUEVO_USUARIO, VOLVER_AL_PERFIL,
        VER_AMIGOS, AGREGAR_AMIGO, VER_SOLICITUDES, MIS_AMIGOS,
        NO_TIENES_AMIGOS, ELIMINAR, VER_DETALLES, AGREGAR_AMIGO_TITULO,
        BUSCAR, VER_JUGADORES, SOLICITUDES_AMISTAD, NO_SOLICITUDES,
        ACEPTAR, RECHAZAR, JUGADORES, AGREGAR, ELIMINAR_AMIGO,
        YA_AMIGOS, SOLICITUD_ENVIADA,
        CAMPOS_VACIOS, USUARIO_NO_EXISTE, CONTRASENA_INCORRECTA,
        USUARIO_YA_EXISTE, PASS_NO_CUMPLE, ERROR_CREAR,
        CHECK_MIN6, CHECK_MAYUS, CHECK_MINUS, CHECK_NUM,
        CONTRASENA_VALIDADA, CONTRASENA_VALIDADA_NUEVA,
        INGRESA_USUARIO, USUARIO_CAMBIADO, ERROR_CAMBIAR_USUARIO,
        CONTRASENA_CAMBIADA, ERROR_CAMBIAR_PASS,INGRESA_USUARIO_SOLICITUD,NO_AUTO_AGREGAR,ENVIAR_SOLICITUD,ERROR_SOLICITUD
        ,PERFIL_DE,ERROR_CARGAR_PERFIL, PRECISION, ESTRELLAS_POR_NIVEL, DE, RESULTADO, PUNTUACION, TIEMPO, SEGUNDOS, GANO, PERDIO, NIVELES,
        ERROR_CARGAR_DATOS,CLASIFICACION_GLOBAL,JUGADOR,PTS,
        NV, TU, TU_POSICION, ORO, PLATA, BRONCE,ESTRELLAS,


    }

    private static boolean ingles = false;

    public static void setIngles(boolean v) { ingles = v; }
    public static boolean isIngles()        { return ingles; }

    public static String get(Clave c) {
        switch (c) {
            case JUGAR:           return ingles ? "Play"             : "Jugar";
            case CREAR_JUGADOR:   return ingles ? "Create Player"    : "Crear Jugador";
            case INICIAR_SESION:  return ingles ? "Login"            : "Iniciar Sesion";
            case AJUSTES:         return ingles ? "Settings"         : "Ajustes";
            case PERFIL:          return ingles ? "Profile"          : "Perfil";
            case SALIR:           return ingles ? "Exit"             : "Salir";
            case VOLVER:          return ingles ? "Back"             : "Volver";
            case PREFERENCIAS:    return ingles ? "Preferences"      : "Preferencias";
            case IDIOMA:          return ingles ? "Language"         : "Idioma";
            case MUSICA:          return ingles ? "Music"            : "Musica";
            case VOLUMEN:         return ingles ? "Volume"           : "Volumen";
            case INGRESAR:        return ingles ? "Login"            : "Ingresar";
            case CREAR:           return ingles ? "Create"           : "Crear";
            case USUARIO:         return ingles ? "Username"         : "Usuario";
            case CONTRASENA:      return ingles ? "Password"         : "Contrasena";
            case NOMBRE_COMPLETO: return ingles ? "Full Name"        : "Nombre Completo";
            case VER_CONTRASENA:  return ingles ? " Show password"   : " Ver contrasena";
            case ELEGIR_AVATAR:   return ingles ? "Choose Avatar"    : "Elegir Avatar";
            case ELIGE_AVATAR:    return ingles ? "Choose your Avatar": "Elige tu Avatar";
            case ANTERIOR:        return ingles ? "< Previous"       : "< Anterior";
            case SIGUIENTE:       return ingles ? "Next >"           : "Siguiente >";
            case CANCELAR:        return ingles ? "Cancel"           : "Cancelar";
            case ESTADISTICAS:    return ingles ? "Statistics"       : "Estadisticas";
            case CERRAR_SESION:   return ingles ? "Log Out"          : "Cerrar Sesion";
            case RANKING:         return ingles ? "Ranking"          : "Ranking";
            case AMIGOS:          return ingles ? "Friends"          : "Amigos";
            case SELECCIONAR_NIVEL: return ingles ? "Select Level"   : "Seleccionar Nivel";
            case NIVEL:           return ingles ? "Level"            : "Nivel";
            case BLOQUEADO:       return ingles ? "Locked"           : "Bloqueado";
            case BIENVENIDO:      return ingles ? "Welcome"          : "Bienvenido";
            case ALIMENTA:        return ingles ? "Feed Om Nom!"     : "Alimenta a Om Nom!";
            default:              return c.name();
            case MI_PERFIL:           return ingles ? "My Profile"          : "Mi perfil";
            case VER_INFORMACION:     return ingles ? "View Information"    : "Ver Informacion";
            case CAMBIAR_USUARIO:     return ingles ? "Change Username"     : "Cambiar Usuario";
            case CAMBIAR_CONTRASENA:  return ingles ? "Change Password"     : "Cambiar Contrasena";
            case CAMBIAR_AVATAR:      return ingles ? "Change Avatar"       : "Cambiar Avatar";
            case DATOS_PERSONALES:    return ingles ? "Personal Data"       : "Datos Personales";
            case PROGRESO_JUEGO:      return ingles ? "Game Progress"       : "Progreso del Juego";
            case HISTORIAL:           return ingles ? "History"             : "Historial";
            case VER_AMIGOS_RIVALES:  return ingles ? "Friends / Rivals"    : "Amigos / Rivales";
            case VOLVER_PERFIL:       return ingles ? "Back to Profile"     : "Volver al Perfil";
            case NOMBRE_COMPLETO_LABEL: return ingles ? "Full name"         : "Nombre completo";
            case FECHA_REGISTRO:      return ingles ? "Registration date"   : "Fecha de registro";
            case ULTIMA_SESION:       return ingles ? "Last session"        : "Ultima sesion";
            case AVATAR:              return ingles ? "Avatar"              : "Avatar";
            case NIVELES_COMPLETADOS: return ingles ? "Levels completed"    : "Niveles completados";
            case PUNTUACION_GENERAL:  return ingles ? "Overall score"       : "Puntuacion general";
            case ESTRELLAS_TOTALES:   return ingles ? "Total stars"         : "Estrellas totales";
            case PARTIDAS_JUGADAS:    return ingles ? "Games played"        : "Partidas jugadas";
            case FALLOS_TOTALES:      return ingles ? "Total fails"         : "Fallos totales";
            case TIEMPO_JUGADO:       return ingles ? "Time played"         : "Tiempo jugado";
            case HISTORIAL_PARTIDAS:  return ingles ? "Match History"       : "Historial de Partidas";
            case SIN_HISTORIAL:       return ingles ? "No history yet."     : "Sin historial disponible aun.";
            case RANKING_GENERAL:     return ingles ? "Global Ranking"      : "Ranking General";
            case SIN_RANKING:         return ingles ? "No ranking yet."     : "Sin ranking disponible aun.";
            case AMIGOS_RIVALES:      return ingles ? "Friends / Rivals"    : "Amigos / Rivales";
            case SIN_AMIGOS:          return ingles ? "No friends yet."     : "Sin amigos agregados aun.";
            case VALIDAR:             return ingles ? "Validate"            : "Validar";
            case GUARDAR:             return ingles ? "Save"                : "Guardar";
            case CONTRASENA_ACTUAL:   return ingles ? "Current password"    : "Contrasena actual";
            case NUEVA_CONTRASENA:    return ingles ? "New password"        : "Nueva contrasena";
            case CONFIRMA_CONTRASENA: return ingles ? "Confirm your password": "Confirma tu contrasena";
            case NUEVO_USUARIO:       return ingles ? "New username"        : "Nuevo nombre de usuario";
            case VOLVER_AL_PERFIL:    return ingles ? "Back to Profile"     : "Volver al Perfil";
            case VER_AMIGOS:          return ingles ? "View Friends"       : "Ver Amigos";
            case AGREGAR_AMIGO:       return ingles ? "Add Friend"         : "Agregar Amigo";
            case VER_SOLICITUDES:     return ingles ? "View Requests"      : "Ver Solicitudes";
            case MIS_AMIGOS:          return ingles ? "My Friends"         : "Mis Amigos";
            case NO_TIENES_AMIGOS:    return ingles ? "No friends yet."    : "No tienes amigos aun.";
            case ELIMINAR:            return ingles ? "Remove"             : "Eliminar";
            case VER_DETALLES:        return ingles ? "View Details"       : "Ver Detalles";
            case AGREGAR_AMIGO_TITULO:return ingles ? "Add Friend"         : "Agregar Amigo";
            case BUSCAR:              return ingles ? "Search"             : "Buscar";
            case VER_JUGADORES:       return ingles ? "View Players"       : "Ver Jugadores";
            case SOLICITUDES_AMISTAD: return ingles ? "Friend Requests"    : "Solicitudes de Amistad";
            case NO_SOLICITUDES:      return ingles ? "No pending requests.": "No tienes solicitudes pendientes.";
            case ACEPTAR:             return ingles ? "Accept"             : "Aceptar";
            case RECHAZAR:            return ingles ? "Reject"             : "Rechazar";
            case JUGADORES:           return ingles ? "Players"            : "Jugadores";
            case AGREGAR:             return ingles ? "Add"                : "Agregar";
            case ELIMINAR_AMIGO:      return ingles ? "Remove Friend"      : "Eliminar Amigo";
            case YA_AMIGOS:           return ingles ? "Already friends."   : "Ya son amigos.";
            case SOLICITUD_ENVIADA:   return ingles ? "Request sent."      : "Solicitud enviada.";
            case CAMPOS_VACIOS:         return ingles ? "Fill in all fields."          : "Completa todos los campos.";
            case USUARIO_NO_EXISTE:     return ingles ? "User does not exist."         : "Usuario no existe.";
            case CONTRASENA_INCORRECTA: return ingles ? "Incorrect password."          : "Contrasena incorrecta.";
            case USUARIO_YA_EXISTE:     return ingles ? "Username already taken."      : "Usuario ya existe.";
            case PASS_NO_CUMPLE:        return ingles ? "Password does not meet requirements." : "La contrasena no cumple los requisitos.";
            case ERROR_CREAR:           return ingles ? "Error creating player."       : "Error al crear jugador.";
            case CHECK_MIN6:            return ingles ? "Min 6 characters"             : "Min 6 caracteres";
            case CHECK_MAYUS:           return ingles ? "One uppercase letter"         : "Una mayuscula";
            case CHECK_MINUS:           return ingles ? "One lowercase letter"         : "Una minuscula";
            case CHECK_NUM:             return ingles ? "One number"                   : "Un numero";
            case CONTRASENA_VALIDADA:      return ingles ? "Password validated."              : "Contrasena validada.";
            case CONTRASENA_VALIDADA_NUEVA:return ingles ? "Validated. Enter new password."   : "Validada. Ingresa la nueva contrasena.";
            case INGRESA_USUARIO:          return ingles ? "Enter a username."                : "Ingresa un nombre de usuario.";
            case USUARIO_CAMBIADO:         return ingles ? "Username changed successfully."   : "Usuario cambiado exitosamente.";
            case ERROR_CAMBIAR_USUARIO:    return ingles ? "Error changing username."         : "Error al cambiar usuario.";
            case CONTRASENA_CAMBIADA:      return ingles ? "Password changed successfully."   : "Contrasena cambiada exitosamente.";
            case ERROR_CAMBIAR_PASS:       return ingles ? "Error changing password."         : "Error al cambiar contrasena.";
            case INGRESA_USUARIO_SOLICITUD: return ingles ? "Enter the username to send a request:" : "Ingresa el usuario al que quieres enviar solicitud:";
            case NO_AUTO_AGREGAR: return ingles ? "You cannot add yourself." : "No puedes agregarte a ti mismo.";
            case ENVIAR_SOLICITUD: return ingles ? "Send Request" : "Enviar Solicitud";
            case ERROR_SOLICITUD: return ingles ? "Could not send request." : "No se pudo enviar la solicitud.";
            case PERFIL_DE: return ingles ? "Profile of @" : "Perfil de @";
            case ERROR_CARGAR_PERFIL: return ingles ? "Could not load profile." : "No se pudo cargar el perfil.";
            case PRECISION:             return ingles ? "Accuracy"            : "Precision";
            case ESTRELLAS_POR_NIVEL:   return ingles ? "Stars per level"     : "Estrellas por nivel";
            case DE:                    return ingles ? "of"                  : "de";
            case RESULTADO:             return ingles ? "Result"              : "Resultado";
            case PUNTUACION:            return ingles ? "Score"               : "Puntuacion";
            case TIEMPO:                return ingles ? "Time"                : "Tiempo";
            case SEGUNDOS:              return ingles ? "sec"                 : "seg";
            case GANO:                  return ingles ? "Won"                 : "Gano";
            case PERDIO:                return ingles ? "Lost"                : "Perdio";
            case NIVELES:               return ingles ? "levels"              : "niveles";
            case ERROR_CARGAR_DATOS: return ingles ? "Could not load data." : "No se pudieron cargar los datos.";
            case CLASIFICACION_GLOBAL:  return ingles ? "Global Ranking"          : "Clasificacion global";
            case JUGADOR:               return ingles ? "Player"                  : "Jugador";
            case PTS:                   return ingles ? "Pts"                     : "Pts";
            case NV:                    return ingles ? "Lv."                     : "Nv.";
            case TU:                    return ingles ? "You"                     : "Tu";
            case TU_POSICION:           return ingles ? "Your position"           : "Tu posicion";
            case ORO:                   return ingles ? "Gold"                    : "Oro";
            case PLATA:                 return ingles ? "Silver"                  : "Plata";
            case BRONCE:                return ingles ? "Bronze"                  : "Bronce";
            case ESTRELLAS: return ingles ? "Stars" : "Estrellas";
        }
    }
}
