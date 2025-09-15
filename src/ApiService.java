
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ApiService {
    public static String login(String username, String password) throws Exception {
        URL url = new URL("http://127.0.0.1:8000/login/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInputString.getBytes("utf-8"));
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        }
    }
    public static String refresh(String refreshToken) throws Exception {
    URL url = new URL("http://127.0.0.1:8000/token/refresh/");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);

    String jsonInputString = "{\"refresh\": \"" + refreshToken + "\"}";

    try (OutputStream os = con.getOutputStream()) {
        os.write(jsonInputString.getBytes("utf-8"));
    }

    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line.trim());
        }
        return response.toString();
        }
    }
    public static String getLivres() throws Exception {
        if (SessionManager.accessToken == null) {
            throw new IllegalStateException("Pas de token d'accès ! Connectez-vous d'abord.");
        }
        System.err.println("HUHUHHUHUHUHU = "+SessionManager.accessToken);

        URL url = new URL("http://127.0.0.1:8000/livre");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode == 401) {
            // Access token expiré, on rafraîchit
            if (SessionManager.refreshToken != null) {
                String refreshResponse = refresh(SessionManager.refreshToken);
                org.json.JSONObject json = new org.json.JSONObject(refreshResponse);
                if (json.has("access")) {
                    SessionManager.accessToken = json.getString("access");
                }
            }

            // Retenter la requête après rafraîchissement
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
            con.setRequestProperty("Accept", "application/json");
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        }
    }    
    public static String getCategorie() throws Exception {
        if (SessionManager.accessToken == null) {
            throw new IllegalStateException("Pas de token d'accès ! Connectez-vous d'abord.");
        }

        URL url = new URL("http://127.0.0.1:8000/categorie/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode == 401) { // token expiré
            if (SessionManager.refreshToken != null) {
                String refreshResponse = refresh(SessionManager.refreshToken);
                org.json.JSONObject json = new org.json.JSONObject(refreshResponse);
                if (json.has("access")) {
                    SessionManager.accessToken = json.getString("access");
                }
            }

            // Retenter la requête après rafraîchissement
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
            con.setRequestProperty("Accept", "application/json");
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        }
    }
    public static String getAuteurs() throws Exception {
        if (SessionManager.accessToken == null) {
            throw new IllegalStateException("Pas de token d'accès ! Connectez-vous d'abord.");
        }
        System.err.println("Token actuel = " + SessionManager.accessToken);

        URL url = new URL("http://127.0.0.1:8000/auteurs/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode == 401) { // token expiré
            if (SessionManager.refreshToken != null) {
                String refreshResponse = refresh(SessionManager.refreshToken);
                org.json.JSONObject json = new org.json.JSONObject(refreshResponse);
                if (json.has("access")) {
                    SessionManager.accessToken = json.getString("access");
                }
            }

            // Retenter la requête après rafraîchissement
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
            con.setRequestProperty("Accept", "application/json");
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        }
    }
    public static void addAuteur(String nomAuteur) throws Exception {
        if (SessionManager.accessToken == null) {
            throw new IllegalStateException("Pas de token d'accès ! Connectez-vous d'abord.");
        }

        URL url = new URL("http://127.0.0.1:8000/auteurs/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "{\"auteur\": \"" + nomAuteur + "\"}";

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInputString.getBytes("utf-8"));
        }

        int responseCode = con.getResponseCode();
        if (responseCode == 401) {
            // Token expiré, on tente de rafraîchir
            if (SessionManager.refreshToken != null) {
                String refreshResponse = refresh(SessionManager.refreshToken);
                org.json.JSONObject json = new org.json.JSONObject(refreshResponse);
                if (json.has("access")) {
                    SessionManager.accessToken = json.getString("access");
                }
                // Retenter la requête après rafraîchissement
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                try (OutputStream os = con.getOutputStream()) {
                    os.write(jsonInputString.getBytes("utf-8"));
                }
            }
        }

        // Lire la réponse pour vérifier si ça a fonctionné
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("Réponse API ajout auteur: " + response.toString());
        }
    }
    public static void addCategorie(String nomCategorie) throws Exception {
        if (SessionManager.accessToken == null) {
            throw new IllegalStateException("Pas de token d'accès ! Connectez-vous d'abord.");
        }

        URL url = new URL("http://127.0.0.1:8000/categorie/"); // URL exacte avec slash final
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "{\"categorie\": \"" + nomCategorie + "\"}";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        }

        InputStream stream;
        if (con.getResponseCode() >= 400) {
            stream = con.getErrorStream();
        } else {
            stream = con.getInputStream();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("Réponse API ajout catégorie: " + response.toString());
        }
    }
    public static void addLivre(String titre, String fichier, String couverture,
        int categorieId, int auteurId,
        String dateSortie, String resume) throws Exception {
        if (SessionManager.accessToken == null) {
            throw new IllegalStateException("Pas de token d'accès ! Connectez-vous d'abord.");
        }

        URL url = new URL("http://127.0.0.1:8000/livre/"); // endpoint livre
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + SessionManager.accessToken);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        // Construction JSON
        String jsonInputString = "{"
                + "\"titre\": \"" + titre + "\","
                + "\"fichier\": \"" + fichier + "\","
                + "\"couverture\": \"" + couverture + "\","
                + "\"categorie_id\": " + categorieId + ","
                + "\"auteur_id\": " + auteurId + ","
                + "\"dateSortie\": \"" + dateSortie + "\","
                + "\"resume\": \"" + resume + "\""
                + "}";

        // Envoi
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.flush();
        }

        InputStream stream;
        if (con.getResponseCode() >= 400) {
            stream = con.getErrorStream();
        } else {
            stream = con.getInputStream();
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("Réponse API ajout livre: " + response.toString());
        }
    }



}
