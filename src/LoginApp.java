import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.json.JSONObject;

public class LoginApp {

    public LoginApp() {
        Window window = new Window();
        window.loadSign();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        //  Titre 
        JLabel titleLabel = new JLabel("Page de connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1; 

        //  Username 
        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField(15);
        gbc.gridx = 1;
        panel.add(userText, gbc);

        //  Password 
        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        JPasswordField passText = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passText, gbc);

        //  Bouton login 
        JButton loginButton = new JButton("Se connecter");
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        //  Message 
        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        panel.add(messageLabel, gbc);

        //  Action bouton 
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());

                new Thread(() -> {
                    try {
                        String response = ApiService.login(username, password); // appel API
                        System.out.println("Response body: " + response);

                        SwingUtilities.invokeLater(() -> {
                            if (response.trim().startsWith("{")) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    if (jsonResponse.has("access")) {
                                        String access = jsonResponse.getString("access");
                                        String refresh = jsonResponse.getString("refresh");

                                        SessionManager.setTokens(access, refresh);

                                        messageLabel.setForeground(Color.GREEN);
                                        messageLabel.setText("Connexion réussie !");
                                        // System.out.println("Access Token: " + access);
                                        // System.out.println("Refresh Token: " + refresh);

                                        window.dispose(); 
                                        new AccueilApp();
                                    } else {
                                        messageLabel.setForeground(Color.RED);
                                        messageLabel.setText("Identifiants incorrects !");
                                    }
                                } catch (Exception ex) {
                                    messageLabel.setForeground(Color.RED);
                                    messageLabel.setText("Erreur JSON !");
                                    ex.printStackTrace();
                                }
                            } else {
                                messageLabel.setForeground(Color.RED);
                                messageLabel.setText("Réponse invalide du serveur !");
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            messageLabel.setForeground(Color.RED);
                            messageLabel.setText("Erreur serveur !");
                        });
                        ex.printStackTrace();
                    }
                }).start();
            }
        });

        window.add(panel); 
        window.setVisible(true);
    }
}
