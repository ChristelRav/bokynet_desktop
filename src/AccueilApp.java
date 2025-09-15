import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.*;

public class AccueilApp {

    public AccueilApp() {
        Window wd = new Window();

        // Panel pour les boutons en haut
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 10));
        JButton btnLivre = new JButton("LIVRE");
        JButton btnCategorie = new JButton("CATEGORIE");
        JButton btnAuteur = new JButton("AUTEUR");

        topPanel.add(btnLivre);
        topPanel.add(btnCategorie);
        topPanel.add(btnAuteur);

        // Panel central pour le contenu
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        // Ajouter les panels à la fenêtre
        wd.setLayout(new BorderLayout());
        wd.add(topPanel, BorderLayout.NORTH);
        wd.add(mainPanel, BorderLayout.CENTER);

        // Actions des boutons
        btnLivre.addActionListener(e -> {
            mainPanel.removeAll();
            loadLivre(mainPanel);
        });

        btnCategorie.addActionListener(e -> {
            mainPanel.removeAll();
            loadCategorie(mainPanel);
        });

        btnAuteur.addActionListener(e -> {
            mainPanel.removeAll();
            loadAuteur(mainPanel);
        });

        // Charger la liste des livres par défaut
        loadLivre(mainPanel);
    }

    public void loadLivre(JPanel mainPanel) {
        new Thread(() -> {
            try {
                String response = ApiService.getLivres();
                org.json.JSONArray livresArray = new org.json.JSONArray(response);

                // Colonnes sans couverture, mais avec résumé
                String[] colonnes = {"Titre", "Auteur", "Catégorie", "Date Sortie", "Résumé"};
                Object[][] data = new Object[livresArray.length()][5];

                for (int i = 0; i < livresArray.length(); i++) {
                    org.json.JSONObject livre = livresArray.getJSONObject(i);

                    data[i][0] = livre.getString("titre");
                    data[i][1] = livre.getJSONObject("auteur").getString("auteur");
                    data[i][2] = livre.getJSONObject("categorie").getString("categorie");
                    data[i][3] = livre.getString("dateSortie");
                    data[i][4] = livre.getString("resume");
                }

                SwingUtilities.invokeLater(() -> {
                    mainPanel.removeAll();
                    mainPanel.setLayout(new BorderLayout());

                    // Label titre "Liste des livres"
                    JLabel titreLabel = new JLabel("Liste des livres");
                    titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    titreLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    mainPanel.add(titreLabel, BorderLayout.NORTH);

                    // JTable avec scroll
                    JTable table = new JTable(data, colonnes);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    table.setRowHeight(30);

                    JScrollPane scrollPane = new JScrollPane(table);
                    mainPanel.add(scrollPane, BorderLayout.CENTER);

                    // Label bienvenue en bas
                    JLabel welcomeLabel = new JLabel("Bienvenue dans la partie administrateur Desktop de BokyNet");
                    welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    mainPanel.add(welcomeLabel, BorderLayout.SOUTH);

                    mainPanel.revalidate();
                    mainPanel.repaint();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    mainPanel.removeAll();
                    mainPanel.setLayout(new FlowLayout());
                    mainPanel.add(new JLabel("Erreur lors du chargement des livres !"));
                    mainPanel.revalidate();
                    mainPanel.repaint();
                });
            }
        }).start();
    }
    public void loadCategorie(JPanel mainPanel) {
        new Thread(() -> {
            try {
                String response = ApiService.getCategorie();
                org.json.JSONArray categoriesArray = new org.json.JSONArray(response);

                // Colonnes du tableau
                String[] colonnes = {"ID", "Catégorie"};
                Object[][] data = new Object[categoriesArray.length()][2];

                for (int i = 0; i < categoriesArray.length(); i++) {
                    org.json.JSONObject cat = categoriesArray.getJSONObject(i);
                    data[i][0] = cat.getInt("idcategorie");
                    data[i][1] = cat.getString("categorie");
                }

                SwingUtilities.invokeLater(() -> {
                    mainPanel.removeAll();
                    mainPanel.setLayout(new BorderLayout());

                    // Label titre
                    JPanel topPanel = new JPanel(new BorderLayout());
                    JLabel titreLabel = new JLabel("Liste des catégories");
                    titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    titreLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    mainPanel.add(titreLabel, BorderLayout.NORTH);

                    // Bouton Ajout
                    JButton addButton = new JButton("Ajouter");
                    addButton.addActionListener(ev -> {
                        addCategorie(mainPanel);
                    });

                    topPanel.add(titreLabel, BorderLayout.CENTER);
                    topPanel.add(addButton, BorderLayout.EAST);
                    mainPanel.add(topPanel, BorderLayout.NORTH);

                    // JTable
                    JTable table = new JTable(data, colonnes);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    table.setRowHeight(30);
                    JScrollPane scrollPane = new JScrollPane(table);
                    mainPanel.add(scrollPane, BorderLayout.CENTER);

                    // Label bienvenue en bas
                    JLabel welcomeLabel = new JLabel("Bienvenue dans la partie administrateur Desktop de BokyNet");
                    welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    mainPanel.add(welcomeLabel, BorderLayout.SOUTH);

                    mainPanel.revalidate();
                    mainPanel.repaint();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    mainPanel.removeAll();
                    mainPanel.setLayout(new FlowLayout());
                    mainPanel.add(new JLabel("Erreur lors du chargement des catégories !"));
                    mainPanel.revalidate();
                    mainPanel.repaint();
                });
            }
        }).start();
    }
    public void loadAuteur(JPanel mainPanel) {
        new Thread(() -> {
            try {
                String response = ApiService.getAuteurs(); // Méthode à créer dans ApiService
                org.json.JSONArray auteursArray = new org.json.JSONArray(response);

                // Colonnes du tableau
                String[] colonnes = {"ID", "Auteur"};
                Object[][] data = new Object[auteursArray.length()][2];

                for (int i = 0; i < auteursArray.length(); i++) {
                    org.json.JSONObject auteur = auteursArray.getJSONObject(i);
                    data[i][0] = auteur.getInt("idauteur");
                    data[i][1] = auteur.getString("auteur");
                }

                SwingUtilities.invokeLater(() -> {
                    mainPanel.removeAll();
                    mainPanel.setLayout(new BorderLayout());

                    // Bouton Ajout
                    JPanel topPanel = new JPanel(new BorderLayout());
                    JLabel titreLabel = new JLabel("Liste des auteurs");
                    titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    titreLabel.setFont(new Font("Arial", Font.BOLD, 16));

                    JButton addButton = new JButton("Ajouter");
                    addButton.addActionListener(ev -> {
                        addAuteur(mainPanel);
                    });

                    topPanel.add(titreLabel, BorderLayout.CENTER);
                    topPanel.add(addButton, BorderLayout.EAST);
                    mainPanel.add(topPanel, BorderLayout.NORTH);

                    // JTable
                    JTable table = new JTable(data, colonnes);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    table.setRowHeight(30);

                    // Centrer la colonne ID
                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                    table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

                    JScrollPane scrollPane = new JScrollPane(table);
                    mainPanel.add(scrollPane, BorderLayout.CENTER);

                    // Label bienvenue en bas
                    JLabel welcomeLabel = new JLabel("Bienvenue dans la partie administrateur Desktop de BokyNet");
                    welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    mainPanel.add(welcomeLabel, BorderLayout.SOUTH);

                    mainPanel.revalidate();
                    mainPanel.repaint();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    mainPanel.removeAll();
                    mainPanel.setLayout(new FlowLayout());
                    mainPanel.add(new JLabel("Erreur lors du chargement des auteurs !"));
                    mainPanel.revalidate();
                    mainPanel.repaint();
                });
            }
        }).start();
    }

    public void addAuteur(JPanel mainPanel) {
        JDialog dialog = new JDialog((Frame) null, "Insertion Auteur", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setLayout(new GridBagLayout()); // plus flexible que GridLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // marges entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        JTextField nomField = new JTextField(15); // 15 colonnes => champ plus petit
        dialog.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton okButton = new JButton("Ajouter");
        okButton.setPreferredSize(new Dimension(80, 30)); // largeur 80px, hauteur 30px
        okButton.setMinimumSize(new Dimension(80, 30));
        okButton.setMaximumSize(new Dimension(80, 30));
        gbc.fill = GridBagConstraints.NONE; // très important pour que la taille soit respectée
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(okButton, gbc);

        okButton.addActionListener(eok -> {
            String nomAuteur = nomField.getText().trim();
            if (!nomAuteur.isEmpty()) {
                try {
                    ApiService.addAuteur(nomAuteur);
                    dialog.dispose();
                    loadAuteur(mainPanel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erreur lors de l'ajout !\nDétail : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Le nom ne peut pas être vide !", "Attention", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
    public void addCategorie(JPanel mainPanel) {
        JDialog dialog = new JDialog((Frame) null, "Insertion Catégorie", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Nom catégorie :"), gbc);

        // Champ texte
        gbc.gridx = 1;
        JTextField nomField = new JTextField(15);
        dialog.add(nomField, gbc);

        // Bouton
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton okButton = new JButton("Ajouter");
        okButton.setPreferredSize(new Dimension(80, 30));
        gbc.fill = GridBagConstraints.NONE;
        dialog.add(okButton, gbc);

        okButton.addActionListener(eok -> {
            String nomCategorie = nomField.getText().trim();
            if (!nomCategorie.isEmpty()) {
                try {
                    ApiService.addCategorie(nomCategorie); // 👉 bien /categorie/
                    dialog.dispose();
                    loadCategorie(mainPanel); // 👉 charge les catégories, pas les auteurs
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erreur lors de l'ajout !\nDétail : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Le nom ne peut pas être vide !",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    


}
