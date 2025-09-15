import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
                     JPanel topPanel = new JPanel(new BorderLayout());
                    JLabel titreLabel = new JLabel("Liste des livres");
                    titreLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    titreLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    mainPanel.add(titreLabel, BorderLayout.NORTH);

                    // Bouton Ajouter
                    JButton addButton = new JButton("Ajouter");
                    addButton.addActionListener(ev -> {
                        addLivre(mainPanel);
                    });

                    topPanel.add(titreLabel, BorderLayout.CENTER);
                    topPanel.add(addButton, BorderLayout.EAST);
                    mainPanel.add(topPanel, BorderLayout.NORTH);

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

    public void addLivre(JPanel mainPanel) {
        JDialog dialog = new JDialog((Frame) null, "Insertion Livre", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(mainPanel);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Champs du formulaire ---
        // 1. Titre
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Titre :"), gbc);
        gbc.gridx = 1;
        JTextField titreField = new JTextField(20);
        dialog.add(titreField, gbc);

        // 2. Date de sortie
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Date de sortie (YYYY-MM-DD) :"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(10);
        dialog.add(dateField, gbc);

        // 3. Résumé
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Résumé :"), gbc);
        gbc.gridx = 1;
        JTextArea resumeArea = new JTextArea(4, 20);
        JScrollPane scrollResume = new JScrollPane(resumeArea);
        dialog.add(scrollResume, gbc);

        // 4. Auteur (ComboBox)
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Auteur :"), gbc);
        gbc.gridx = 1;
        JComboBox<String> auteurCombo = new JComboBox<>();
        try {
            String auteursJson = ApiService.getAuteurs();
            org.json.JSONArray auteursArray = new org.json.JSONArray(auteursJson);
            for (int i = 0; i < auteursArray.length(); i++) {
                org.json.JSONObject obj = auteursArray.getJSONObject(i);
                auteurCombo.addItem(obj.getInt("idauteur") + " - " + obj.getString("auteur"));
            }
        } catch (Exception ex) {
            auteurCombo.addItem("Erreur chargement auteurs");
        }
        dialog.add(auteurCombo, gbc);

        // 5. Catégorie (ComboBox)
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Catégorie :"), gbc);
        gbc.gridx = 1;
        JComboBox<String> categorieCombo = new JComboBox<>();
        try {
            String catJson = ApiService.getCategorie();
            org.json.JSONArray catArray = new org.json.JSONArray(catJson);
            for (int i = 0; i < catArray.length(); i++) {
                org.json.JSONObject obj = catArray.getJSONObject(i);
                categorieCombo.addItem(obj.getInt("idcategorie") + " - " + obj.getString("categorie"));
            }
        } catch (Exception ex) {
            categorieCombo.addItem("Erreur chargement catégories");
        }
        dialog.add(categorieCombo, gbc);

        // 6. Couverture (Upload)
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Couverture :"), gbc);
        gbc.gridx = 1;
        JTextField couvertureField = new JTextField(15);
        JButton btnUploadImg = new JButton("Choisir image");
        JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        imgPanel.add(couvertureField);
        imgPanel.add(btnUploadImg);
        dialog.add(imgPanel, gbc);

        btnUploadImg.addActionListener(ev -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                try {
                    File dest = new File("img_livre/" + selected.getName());
                    java.nio.file.Files.copy(selected.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    couvertureField.setText(selected.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Erreur upload image: " + ex.getMessage());
                }
            }
        });

        // 7. Fichier PDF (Upload)
        gbc.gridx = 0; gbc.gridy++;
        dialog.add(new JLabel("Fichier PDF :"), gbc);
        gbc.gridx = 1;
        JTextField fichierField = new JTextField(15);
        JButton btnUploadPdf = new JButton("Choisir PDF");
        JPanel pdfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pdfPanel.add(fichierField);
        pdfPanel.add(btnUploadPdf);
        dialog.add(pdfPanel, gbc);

        btnUploadPdf.addActionListener(ev -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                try {
                    File dest = new File("livre/" + selected.getName());
                    java.nio.file.Files.copy(selected.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    fichierField.setText(selected.getName());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Erreur upload PDF: " + ex.getMessage());
                }
            }
        });

        // --- Bouton Enregistrer ---
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSave = new JButton("Ajouter Livre");
        dialog.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            try {
                String titre = titreField.getText().trim();
                String dateSortie = dateField.getText().trim();
                String resume = resumeArea.getText().trim();
                String couverture = couvertureField.getText().trim();
                String fichier = fichierField.getText().trim();

                if (titre.isEmpty() || dateSortie.isEmpty() || resume.isEmpty() ||
                    couverture.isEmpty() || fichier.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tous les champs sont obligatoires !");
                    return;
                }

                // Récupérer ID auteur
                int auteurId = Integer.parseInt(auteurCombo.getSelectedItem().toString().split(" - ")[0]);
                int categorieId = Integer.parseInt(categorieCombo.getSelectedItem().toString().split(" - ")[0]);

                String resumeJson = resume
                .replace("\n", "\\n")   // remplace les retours à la ligne
                .replace("\"", "\\\""); // échappe les guillemets

                 System.out.println(">>> Envoi livre : " +
                "titre=" + titre + ", " +
                "fichier=" + fichier + ", " +
                "couverture=" + couverture + ", " +
                "categorieId=" + categorieId + ", " +
                "auteurId=" + auteurId + ", " +
                "dateSortie=" + dateSortie + ", " +
                "resume=" + resume);

                ApiService.addLivre(titre, fichier, couverture, categorieId, auteurId, dateSortie, resumeJson);
                JOptionPane.showMessageDialog(dialog, "Livre ajouté avec succès !");
                dialog.dispose();
                loadLivre(mainPanel);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur ajout livre: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        dialog.setVisible(true);
    }


    


}
