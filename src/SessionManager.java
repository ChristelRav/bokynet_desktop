public class SessionManager {
    public static String accessToken = null;
    public static String refreshToken = null;

    // Mettre à jour les tokens après login
    public static void setTokens(String access, String refresh) {
        accessToken = access;
        refreshToken = refresh;
    }

    // Vérifier si on est connecté
    public static boolean isLoggedIn() {
        return accessToken != null && refreshToken != null;
    }
}
