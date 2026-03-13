package gui;

import services.*;
import modeles.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class FenetrePrincipale extends JFrame {
    
    // ==================== COULEURS PROFESSIONNELLES ====================
    private static final Color PRIMARY_DARK = new Color(15, 23, 42);      // Slate 900 - Fond sombre
    private static final Color PRIMARY = new Color(30, 41, 59);           // Slate 800 - Fond secondaire
    private static final Color SECONDARY = new Color(51, 65, 85);         // Slate 700 - Éléments secondaires
    private static final Color ACCENT = new Color(59, 130, 246);          // Blue 500 - Actions principales
    private static final Color ACCENT_HOVER = new Color(37, 99, 235);     // Blue 600 - Survol
    private static final Color SUCCESS = new Color(34, 197, 94);          // Green 500 - Succès
    private static final Color WARNING = new Color(245, 158, 11);         // Amber 500 - Attention
    private static final Color DANGER = new Color(239, 68, 68);           // Red 500 - Erreur
    private static final Color TEXT_PRIMARY = new Color(248, 250, 252);   // Slate 50 - Texte clair
    private static final Color TEXT_SECONDARY = new Color(148, 163, 184); // Slate 400 - Texte secondaire
    private static final Color BACKGROUND = new Color(241, 245, 249);     // Slate 100 - Fond général
    private static final Color CARD_BG = new Color(255, 255, 255, 250);   // Blanc carte
    private static final Color BORDER_COLOR = new Color(226, 232, 240);   // Bordure grise
    
    // ==================== SERVICES ====================
    private ServiceVols serviceVols;
    private ServiceReservations serviceReservations;
    private ServiceSieges serviceSieges;
    private ServiceAvis serviceAvis;
    private ServicePaiement servicePaiement;
    private ServiceNotifications serviceNotifications;
    
    // ==================== UTILISATEUR ====================
    private Client clientConnecte;
    private Admin adminConnecte;
    private boolean estAdmin = false;
    
    // ==================== COMPOSANTS PRINCIPAUX ====================
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private JPanel sidebar;
    
    // ==================== PANELS ====================
    private PanelConnexion panelConnexion;
    private PanelInscription panelInscription;
    private PanelRecherche panelRecherche;
    private PanelReservation panelReservation;
    private PanelPaiement panelPaiement;
    private PanelConfirmation panelConfirmation;
    private PanelProfil panelProfil;
    private PanelAdmin panelAdmin;
    private PanelSieges panelSieges;
    private PanelAvis panelAvis;
    
    /**
     * Constructeur principal - Initialise l'application
     */
    public FenetrePrincipale() {
        
        // Initialisation des services (singletons)
        serviceVols = ServiceVols.getInstance();
        serviceReservations = ServiceReservations.getInstance();
        serviceSieges = ServiceSieges.getInstance();
        serviceAvis = ServiceAvis.getInstance();
        servicePaiement = ServicePaiement.getInstance();
        serviceNotifications = ServiceNotifications.getInstance();
        
        // Configuration de la fenêtre principale
        setTitle("SkyBooking - Système de Réservation de Vols");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));
        
        // Configuration du look and feel système
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background", BACKGROUND);
            UIManager.put("OptionPane.background", BACKGROUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Layout principal
        setLayout(new BorderLayout());
        
        // Création de la sidebar (masquée initialement)
        sidebar = creerSidebar();
        add(sidebar, BorderLayout.WEST);
        sidebar.setVisible(false);
        
        // Panel central avec CardLayout pour la navigation
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(BACKGROUND);
        
        // Initialisation de tous les panels
        panelConnexion = new PanelConnexion();
        panelInscription = new PanelInscription();
        panelRecherche = new PanelRecherche();
        panelReservation = new PanelReservation();
        panelPaiement = new PanelPaiement();
        panelConfirmation = new PanelConfirmation();
        panelProfil = new PanelProfil();
        panelAdmin = new PanelAdmin();
        panelSieges = new PanelSieges();
        panelAvis = new PanelAvis();
        
        // Ajout des panels au CardLayout
        panelPrincipal.add(panelConnexion, "CONNEXION");
        panelPrincipal.add(panelInscription, "INSCRIPTION");
        panelPrincipal.add(panelRecherche, "RECHERCHE");
        panelPrincipal.add(panelReservation, "RESERVATION");
        panelPrincipal.add(panelPaiement, "PAIEMENT");
        panelPrincipal.add(panelConfirmation, "CONFIRMATION");
        panelPrincipal.add(panelProfil, "PROFIL");
        panelPrincipal.add(panelAdmin, "ADMIN");
        panelPrincipal.add(panelSieges, "SIEGES");
        panelPrincipal.add(panelAvis, "AVIS");
        
        // ScrollPane pour le contenu principal (permet le défilement si nécessaire)
        JScrollPane mainScrollPane = new JScrollPane(panelPrincipal);
        mainScrollPane.setBorder(null);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScrollPane, BorderLayout.CENTER);
        
        // Affichage du panel de connexion au démarrage
        cardLayout.show(panelPrincipal, "CONNEXION");
        
        setVisible(true);
    }
   
    // ==================== SIDEBAR ====================
    
    /**
     * Crée la sidebar de navigation
     */
    private JPanel creerSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(280, 0));
        
        // Logo
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelLogo.setBackground(PRIMARY_DARK);
        JLabel lblLogo = new JLabel("SkyBooking");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(TEXT_PRIMARY);
        panelLogo.add(lblLogo);
        sidebar.add(panelLogo, BorderLayout.NORTH);
        
        // Menu
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(PRIMARY_DARK);
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // Structure du menu : [texte, identifiant panel]
        String[][] menuItems = {
            {"Rechercher un vol", "RECHERCHE"},
            {"Mes réservations", "PROFIL"},
            {"Avis et Reviews", "AVIS"},
            {"Mon profil", "PROFIL"},
            {"Administration", "ADMIN"},
            {"Déconnexion", "DECONNEXION"}
        };
        
        for (String[] item : menuItems) {
            // Masquer l'item Admin si l'utilisateur n'est pas admin
            if (item[1].equals("ADMIN") && !estAdmin) continue;
            
            JButton btn = creerBoutonMenu(item[0]);
            btn.addActionListener(e -> {
                if (item[1].equals("DECONNEXION")) {
                    deconnecter();
                } else {
                    cardLayout.show(panelPrincipal, item[1]);
                }
            });
            panelMenu.add(btn);
            panelMenu.add(Box.createVerticalStrut(5));
        }
        
        sidebar.add(panelMenu, BorderLayout.CENTER);
        
        // Information utilisateur en bas de la sidebar
        JPanel panelUser = new JPanel(new BorderLayout());
        panelUser.setBackground(SECONDARY);
        panelUser.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblUser = new JLabel();
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(TEXT_PRIMARY);
        panelUser.add(lblUser, BorderLayout.CENTER);
        
        sidebar.add(panelUser, BorderLayout.SOUTH);
        
        return sidebar;
    }
    
    /**
     * Crée un bouton de menu stylisé
     */
    private JButton creerBoutonMenu(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(TEXT_SECONDARY);
        btn.setBackground(PRIMARY_DARK);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Effet de survol
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(SECONDARY);
                btn.setForeground(TEXT_PRIMARY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(PRIMARY_DARK);
                btn.setForeground(TEXT_SECONDARY);
            }
        });
        
        return btn;
    }
    
    /**
     * Met à jour le label utilisateur dans la sidebar
     */
    private void updateSidebarUserLabel() {
        Component[] components = sidebar.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getComponentCount() > 0 && panel.getComponent(0) instanceof JLabel) {
                    JLabel lblUser = (JLabel) panel.getComponent(0);
                    if (clientConnecte != null) {
                        lblUser.setText(clientConnecte.getPrenom() + " " + clientConnecte.getNom());
                    } else if (adminConnecte != null) {
                        lblUser.setText(adminConnecte.getNom());
                    }
                    break;
                }
            }
        }
    }
    
    // ==================== PANEL DE CONNEXION ====================
    
    /**
     * Panel de connexion avec design moderne
     */
    private class PanelConnexion extends JPanel {
        private JTextField txtEmail;
        private JPasswordField txtPassword;
        private JButton btnConnexion;
        private JLabel lblMessageErreur;
        private JPanel card;
        
        public PanelConnexion() {
            initPanel();
            initComponents();
            setupLayout();
            setupEvents();
        }
        
        /**
         * Configuration du panel
         */
        private void initPanel() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND);
        }
        
        /**
         * Initialisation des composants
         */
        private void initComponents() {
            // Carte principale avec dégradé
            card = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), new Color(250, 250, 252)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2d.dispose();
                }
            };
            
            card.setOpaque(false);
            card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(20, new Color(0, 0, 0, 0.15f)),
                BorderFactory.createEmptyBorder(45, 55, 45, 55)
            ));
            
            txtEmail = createModernTextField("exemple@email.com");
            txtPassword = createModernPasswordField();
            btnConnexion = createGradientButton("Se connecter", ACCENT, ACCENT_HOVER);
            
            lblMessageErreur = new JLabel(" ");
            lblMessageErreur.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblMessageErreur.setForeground(DANGER);
            lblMessageErreur.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        /**
         * Organisation du layout
         */
        private void setupLayout() {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 10, 8, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Logo (avion)
            JLabel lblIcon = new JLabel("✈️");
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 64));
            lblIcon.setForeground(ACCENT);
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Titre
            JLabel lblTitre = new JLabel("Bienvenue sur SkyBooking");
            lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 32));
            lblTitre.setForeground(PRIMARY_DARK);
            lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Sous-titre
            JLabel lblSousTitre = new JLabel("Connectez-vous pour accéder à votre espace");
            lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSousTitre.setForeground(TEXT_SECONDARY);
            lblSousTitre.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Séparateur
            JSeparator separator = new JSeparator();
            separator.setForeground(BORDER_COLOR);
            separator.setBackground(BORDER_COLOR);
            
            // Labels des champs
            JLabel lblEmail = createFieldLabel("Adresse email");
            JLabel lblPassword = createFieldLabel("Mot de passe");
            
            // Liens
            JButton btnForgotPassword = createLinkButton("Mot de passe oublié ?");
            btnForgotPassword.addActionListener(e -> showForgotPasswordDialog());
            
            JButton btnInscription = createLinkButton("Pas encore de compte ? S'inscrire");
            btnInscription.addActionListener(e -> cardLayout.show(panelPrincipal, "INSCRIPTION"));
            
            JButton btnInvite = createGhostButton("Continuer sans compte →");
            btnInvite.addActionListener(e -> loginAsGuest());
            
            JButton btnAdmin = createDiscreetLink("Accès administrateur");
            btnAdmin.addActionListener(e -> loginAsAdmin());
            
            gbc.gridwidth = 2;
            
            gbc.gridy = 0;
            card.add(lblIcon, gbc);
            
            gbc.gridy = 1;
            gbc.insets = new Insets(20, 10, 5, 10);
            card.add(lblTitre, gbc);
            
            gbc.gridy = 2;
            gbc.insets = new Insets(0, 10, 25, 10);
            card.add(lblSousTitre, gbc);
            
            gbc.gridy = 3;
            gbc.insets = new Insets(5, 10, 15, 10);
            card.add(separator, gbc);
            
            gbc.gridy = 4;
            gbc.insets = new Insets(5, 10, 2, 10);
            card.add(lblEmail, gbc);
            
            gbc.gridy = 5;
            gbc.insets = new Insets(0, 10, 10, 10);
            card.add(txtEmail, gbc);
            
            gbc.gridy = 6;
            gbc.insets = new Insets(5, 10, 2, 10);
            card.add(lblPassword, gbc);
            
            gbc.gridy = 7;
            gbc.insets = new Insets(0, 10, 5, 10);
            card.add(txtPassword, gbc);
            
            gbc.gridy = 8;
            gbc.insets = new Insets(0, 10, 20, 10);
            card.add(btnForgotPassword, gbc);
            
            gbc.gridy = 9;
            gbc.insets = new Insets(5, 10, 5, 10);
            card.add(lblMessageErreur, gbc);
            
            gbc.gridy = 10;
            gbc.insets = new Insets(10, 10, 10, 10);
            card.add(btnConnexion, gbc);
            
            gbc.gridy = 11;
            gbc.insets = new Insets(15, 10, 5, 10);
            card.add(btnInscription, gbc);
            
            gbc.gridy = 12;
            gbc.insets = new Insets(5, 10, 5, 10);
            card.add(btnInvite, gbc);
            
            gbc.gridy = 13;
            gbc.insets = new Insets(25, 10, 5, 10);
            card.add(btnAdmin, gbc);
            
            add(card);
        }
        
        /**
         * Configuration des événements
         */
        private void setupEvents() {
            // Animation au focus
            txtEmail.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    animateFieldFocus(txtEmail, true);
                }
                @Override
                public void focusLost(FocusEvent e) {
                    animateFieldFocus(txtEmail, false);
                }
            });
            
            txtPassword.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    animateFieldFocus(txtPassword, true);
                }
                @Override
                public void focusLost(FocusEvent e) {
                    animateFieldFocus(txtPassword, false);
                }
            });
            
            // Validation par touche Entrée
            KeyAdapter enterKeyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        login();
                    }
                }
            };
            
            txtEmail.addKeyListener(enterKeyListener);
            txtPassword.addKeyListener(enterKeyListener);
        }
        
        /**
         * Animation du focus sur les champs
         */
        private void animateFieldFocus(JComponent field, boolean focused) {
            if (focused) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT, 2),
                    BorderFactory.createEmptyBorder(11, 14, 11, 14)
                ));
            } else {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
            }
            field.repaint();
        }
        
        /**
         * Crée un champ de texte moderne avec placeholder
         */
        private JTextField createModernTextField(String placeholder) {
            JTextField field = new JTextField() {
                private String placeholderText = placeholder;
                
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getText().isEmpty() && !isFocusOwner()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(TEXT_SECONDARY);
                        g2.setFont(getFont().deriveFont(Font.PLAIN));
                        g2.drawString(placeholderText, 15, getHeight() - 12);
                        g2.dispose();
                    }
                }
            };
            
            field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
            field.setBackground(Color.WHITE);
            field.setPreferredSize(new Dimension(300, 45));
            
            return field;
        }
        
        /**
         * Crée un champ de mot de passe moderne
         */
        private JPasswordField createModernPasswordField() {
            JPasswordField field = new JPasswordField() {
                private String placeholderText = "••••••••";
                
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getPassword().length == 0 && !isFocusOwner()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(TEXT_SECONDARY);
                        g2.setFont(getFont().deriveFont(Font.PLAIN));
                        g2.drawString(placeholderText, 15, getHeight() - 12);
                        g2.dispose();
                    }
                }
            };
            
            field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
            field.setBackground(Color.WHITE);
            field.setPreferredSize(new Dimension(300, 45));
            
            return field;
        }
        
        /**
         * Crée un bouton avec dégradé
         */
        private JButton createGradientButton(String text, Color color1, Color color2) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gp = new GradientPaint(
                        0, 0, color1,
                        getWidth(), 0, color2
                    );
                    g2d.setPaint(gp);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2d.drawString(getText(), x, y);
                    
                    g2d.dispose();
                }
            };
            
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setPreferredSize(new Dimension(300, 50));
            button.setContentAreaFilled(false);
            
            // Effets de survol
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 2),
                        BorderFactory.createEmptyBorder(10, 38, 10, 38)
                    ));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
                }
            });
            
            button.addActionListener(e -> login());
            
            return button;
        }
        
        /**
         * Crée un bouton de type lien
         */
        private JButton createLinkButton(String text) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            button.setForeground(ACCENT);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Effet de soulignement au survol
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setFont(button.getFont().deriveFont(Font.BOLD));
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setFont(button.getFont().deriveFont(Font.PLAIN));
                }
            });
            
            return button;
        }
        
        /**
         * Crée un bouton fantôme
         */
        private JButton createGhostButton(String text) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            button.setForeground(TEXT_SECONDARY);
            button.setBackground(new Color(248, 250, 252));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Effet de survol
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(241, 245, 249));
                    button.setForeground(PRIMARY_DARK);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(248, 250, 252));
                    button.setForeground(TEXT_SECONDARY);
                }
            });
            
            return button;
        }
        
        /**
         * Crée un lien discret
         */
        private JButton createDiscreetLink(String text) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            button.setForeground(new Color(148, 163, 184, 150));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Effet de survol
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setForeground(ACCENT);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setForeground(new Color(148, 163, 184, 150));
                }
            });
            
            button.addActionListener(e -> loginAsAdmin());
            
            return button;
        }
        
        /**
         * Crée un label pour les champs
         */
        private JLabel createFieldLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(TEXT_SECONDARY);
            return label;
        }
        
        /**
         * Validation du format email
         */
        private boolean isValidEmail(String email) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            return email.matches(emailRegex);
        }
        
        /**
         * Validation du mot de passe (minimum 6 caractères)
         */
        private boolean isValidPassword(String password) {
            return password.length() >= 6;
        }
        
        /**
         * Affiche une erreur avec animation
         */
        private void showError(String message) {
            lblMessageErreur.setText("⚠ " + message);
            
            // Animation de secousse
            Timer timer = new Timer(50, null);
            final int[] step = {0};
            final int originalX = btnConnexion.getX();
            
            timer.addActionListener(e -> {
                if (step[0] < 6) {
                    int offset = (step[0] % 2 == 0) ? 5 : -5;
                    btnConnexion.setLocation(originalX + offset, btnConnexion.getY());
                    step[0]++;
                } else {
                    btnConnexion.setLocation(originalX, btnConnexion.getY());
                    timer.stop();
                }
            });
            
            timer.start();
        }
        
        /**
         * Efface le message d'erreur
         */
        private void clearError() {
            lblMessageErreur.setText(" ");
        }
        
        /**
         * Processus de connexion
         */
        private void login() {
            clearError();
            
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            
            // Validations
            if (email.isEmpty()) {
                showError("Veuillez saisir votre adresse email");
                txtEmail.requestFocus();
                return;
            }
            
            if (!isValidEmail(email)) {
                showError("Format d'email invalide");
                txtEmail.requestFocus();
                txtEmail.selectAll();
                return;
            }
            
            if (password.isEmpty()) {
                showError("Veuillez saisir votre mot de passe");
                txtPassword.requestFocus();
                return;
            }
            
            if (!isValidPassword(password)) {
                showError("Le mot de passe doit contenir au moins 6 caractères");
                txtPassword.requestFocus();
                txtPassword.selectAll();
                return;
            }
            
            // Désactivation du bouton pendant la connexion
            btnConnexion.setEnabled(false);
            btnConnexion.setText("Connexion en cours...");
            
            // Simulation de connexion avec SwingWorker (évite de bloquer l'UI)
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    Thread.sleep(1000); // Simule un délai réseau
                    return true;
                }
                
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            // Création du client connecté
                            clientConnecte = new Client(1, "Amghar", "Zineb", email, "0123456789", password);
                            clientConnecte.seConnecter(email, password);
                            estAdmin = false;
                            
                            // Affichage de la sidebar et navigation
                            sidebar.setVisible(true);
                            FenetrePrincipale.this.updateSidebarUserLabel();
                            cardLayout.show(panelPrincipal, "RECHERCHE");
                            
                            // Message de bienvenue
                            JOptionPane.showMessageDialog(
                                FenetrePrincipale.this,
                                "Connexion réussie !\nBienvenue " + clientConnecte.getPrenom(),
                                "Succès",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showError("Erreur de connexion");
                    } finally {
                        // Réactivation du bouton
                        btnConnexion.setEnabled(true);
                        btnConnexion.setText("Se connecter");
                    }
                }
            };
            
            worker.execute();
        }
        
        /**
         * Connexion en mode invité
         */
        private void loginAsGuest() {
            clientConnecte = new Client(0, "Invité", "", "guest@skybooking.com", "", "");
            estAdmin = false;
            
            JOptionPane.showMessageDialog(
                FenetrePrincipale.this,
                "Bienvenue en mode invité !\n" +
                "Vous pouvez rechercher des vols sans créer de compte.\n" +
                "Pour réserver, vous devrez vous connecter ou créer un compte.",
                "Mode invité",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            sidebar.setVisible(true);
            cardLayout.show(panelPrincipal, "RECHERCHE");
        }
        
        /**
         * Connexion administrateur
         */
        private void loginAsAdmin() {
            // Panel personnalisé pour la connexion admin
            JPanel adminPanel = new JPanel(new GridBagLayout());
            adminPanel.setBackground(Color.WHITE);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JTextField adminEmail = new JTextField("admin@skybooking.com", 20);
            JPasswordField adminPass = new JPasswordField("admin123", 20);
            
            styleTextField(adminEmail);
            styleTextField(adminPass);
            
            gbc.gridx = 0; gbc.gridy = 0;
            adminPanel.add(new JLabel("Email admin:"), gbc);
            gbc.gridx = 1;
            adminPanel.add(adminEmail, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            adminPanel.add(new JLabel("Mot de passe:"), gbc);
            gbc.gridx = 1;
            adminPanel.add(adminPass, gbc);
            
            int result = JOptionPane.showConfirmDialog(
                this,
                adminPanel,
                "Connexion Administrateur",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String email = adminEmail.getText();
                String pass = new String(adminPass.getPassword());
                
                // Vérification des identifiants (simulée)
                if (email.equals("admin@skybooking.com") && pass.equals("admin123")) {
                    adminConnecte = new Admin(1, "Administrateur", email, pass);
                    adminConnecte.seConnecter(email, pass);
                    estAdmin = true;
                    clientConnecte = null;
                    
                    sidebar.setVisible(true);
                    cardLayout.show(panelPrincipal, "ADMIN");
                    
                    JOptionPane.showMessageDialog(
                        this,
                        "Bienvenue, Administrateur !",
                        "Accès autorisé",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Identifiants administrateur incorrects",
                        "Accès refusé",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        
        /**
         * Dialogue de mot de passe oublié
         */
        private void showForgotPasswordDialog() {
            String email = JOptionPane.showInputDialog(
                this,
                "Saisissez votre adresse email :",
                "Réinitialisation du mot de passe",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (email != null && !email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Un lien de réinitialisation a été envoyé à :\n" + email,
                    "Email envoyé",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
    
    // ==================== BORDURE AVEC OMBRE ====================
    
    /**
     * Bordure personnalisée avec ombre portée
     */
    private class ShadowBorder extends AbstractBorder {
        private int shadowSize;
        private Color shadowColor;
        
        public ShadowBorder(int size, Color color) {
            this.shadowSize = size;
            this.shadowColor = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dessine plusieurs couches d'ombre
            for (int i = 0; i < shadowSize; i++) {
                int alpha = (shadowSize - i) * 5;
                g2d.setColor(new Color(
                    shadowColor.getRed(),
                    shadowColor.getGreen(),
                    shadowColor.getBlue(),
                    alpha
                ));
                g2d.fillRoundRect(
                    x + i, y + i,
                    width - (2 * i), height - (2 * i),
                    15 + (shadowSize - i), 15 + (shadowSize - i)
                );
            }
            
            // Bordure principale blanche
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRoundRect(x, y, width - 1, height - 1, 20, 20);
            
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize + 1, shadowSize + 1, shadowSize + 2, shadowSize + 2);
        }
    }
    
    // ==================== PANEL INSCRIPTION ====================
    
    /**
     * Panel d'inscription
     */
    private class PanelInscription extends JPanel {
        private JTextField txtNom, txtPrenom, txtEmail, txtTelephone;
        private JPasswordField txtPassword, txtConfirmPassword;
        
        public PanelInscription() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND);
            
            // Carte d'inscription
            JPanel card = new JPanel(new GridBagLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
            ));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 10, 8, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Titre
            JLabel lblTitre = new JLabel("Créer un compte");
            lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitre.setForeground(PRIMARY_DARK);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            card.add(lblTitre, gbc);
            
            // Champs du formulaire
            txtNom = new JTextField(20);
            txtPrenom = new JTextField(20);
            txtEmail = new JTextField(20);
            txtTelephone = new JTextField(20);
            txtPassword = new JPasswordField(20);
            txtConfirmPassword = new JPasswordField(20);
            
            ajouterChamp(card, gbc, "Nom", txtNom, 1);
            ajouterChamp(card, gbc, "Prénom", txtPrenom, 3);
            ajouterChamp(card, gbc, "Email", txtEmail, 5);
            ajouterChamp(card, gbc, "Téléphone", txtTelephone, 7);
            ajouterChamp(card, gbc, "Mot de passe", txtPassword, 9);
            ajouterChamp(card, gbc, "Confirmer", txtConfirmPassword, 11);
            
            // Bouton d'inscription
            JButton btnInscrire = creerBoutonPrincipal("S'inscrire");
            btnInscrire.addActionListener(e -> {
                String pass = new String(txtPassword.getPassword());
                String confirmPass = new String(txtConfirmPassword.getPassword());
                
                // Vérification de la correspondance des mots de passe
                if (!pass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(this, 
                        "Les mots de passe ne correspondent pas!", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Compte créé avec succès!\nBienvenue " + txtPrenom.getText() + " " + txtNom.getText(), 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(panelPrincipal, "CONNEXION");
            });
            gbc.gridy = 13;
            gbc.insets = new Insets(20, 10, 10, 10);
            card.add(btnInscrire, gbc);
            
            // Lien de retour
            JButton btnRetour = new JButton("← Retour à la connexion");
            btnRetour.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btnRetour.setForeground(TEXT_SECONDARY);
            btnRetour.setContentAreaFilled(false);
            btnRetour.setBorderPainted(false);
            btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRetour.addActionListener(e -> cardLayout.show(panelPrincipal, "CONNEXION"));
            gbc.gridy = 14;
            card.add(btnRetour, gbc);
            
            add(card);
        }
        
        /**
         * Ajoute un champ avec son label
         */
        private void ajouterChamp(JPanel panel, GridBagConstraints gbc, 
                                  String label, JComponent champ, int gridY) {
            gbc.gridy = gridY;
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setForeground(TEXT_SECONDARY);
            panel.add(lbl, gbc);
            
            gbc.gridy = gridY + 1;
            if (champ instanceof JTextField) styleTextField((JTextField)champ);
            else if (champ instanceof JPasswordField) styleTextField((JPasswordField)champ);
            panel.add(champ, gbc);
        }
    }
    
    // ==================== PANEL RECHERCHE ====================
    
    /**
     * Panel de recherche de vols
     */
    private class PanelRecherche extends JPanel {
        private JComboBox<String> comboDepart, comboArrivee, comboClasse, comboCompagnie;
        private JTextField txtDate, txtPrixMax;
        private JTable tableVols;
        private DefaultTableModel modelTable;
        private Vol volSelectionne;
        private JLabel lblResultCount;
        private JPanel panelFiltres;
        private boolean filtresVisible = true;
        
        public PanelRecherche() {
            setLayout(new BorderLayout(0, 0));
            setBackground(BACKGROUND);
            
            // En-tête
            JPanel header = createEnhancedHeader();
            add(header, BorderLayout.NORTH);
            
            // Filtres de recherche
            panelFiltres = createModernFiltersPanel();
            add(panelFiltres, BorderLayout.CENTER);
            
            // Tableau des résultats
            JPanel panelTable = createEnhancedTablePanel();
            add(panelTable, BorderLayout.SOUTH);
            
            chargerTousLesVols();
        }
        
        /**
         * Crée l'en-tête amélioré
         */
        private JPanel createEnhancedHeader() {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(PRIMARY_DARK);
            header.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
            
            // Partie gauche avec titre et sous-titre
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setOpaque(false);
            
            JLabel lblTitre = new JLabel("Rechercher un vol");
            lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 32));
            lblTitre.setForeground(TEXT_PRIMARY);
            lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel lblSousTitre = new JLabel("Trouvez le vol parfait pour votre prochaine destination");
            lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSousTitre.setForeground(TEXT_SECONDARY);
            lblSousTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            leftPanel.add(lblTitre);
            leftPanel.add(Box.createVerticalStrut(5));
            leftPanel.add(lblSousTitre);
            
            header.add(leftPanel, BorderLayout.WEST);
            
            // Partie droite avec informations utilisateur
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
            rightPanel.setOpaque(false);
            
            if (clientConnecte != null && !clientConnecte.getNom().isEmpty()) {
                JPanel userInfoPanel = new JPanel();
                userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
                userInfoPanel.setOpaque(false);
                
                JLabel lblWelcome = new JLabel("Bienvenue,");
                lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblWelcome.setForeground(TEXT_SECONDARY);
                lblWelcome.setAlignmentX(Component.RIGHT_ALIGNMENT);
                
                JLabel lblUserName = new JLabel(clientConnecte.getPrenom() + " " + clientConnecte.getNom());
                lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblUserName.setForeground(TEXT_PRIMARY);
                lblUserName.setAlignmentX(Component.RIGHT_ALIGNMENT);
                
                userInfoPanel.add(lblWelcome);
                userInfoPanel.add(lblUserName);
                
                rightPanel.add(userInfoPanel);
            }
            
            // Badge de date
            JPanel dateBadge = new JPanel();
            dateBadge.setLayout(new BoxLayout(dateBadge, BoxLayout.Y_AXIS));
            dateBadge.setBackground(new Color(255, 255, 255, 30));
            dateBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
            
            JLabel lblDate = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            lblDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblDate.setForeground(TEXT_PRIMARY);
            lblDate.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            dateBadge.add(lblDate);
            
            rightPanel.add(dateBadge);
            
            header.add(rightPanel, BorderLayout.EAST);
            
            return header;
        }
        
        /**
         * Crée le panel de filtres moderne
         */
        private JPanel createModernFiltersPanel() {
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(Color.WHITE);
            mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 30, 25, 30)
            ));
            
            // Titre des filtres
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            
            JLabel lblFiltres = new JLabel("Filtres de recherche");
            lblFiltres.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblFiltres.setForeground(PRIMARY_DARK);
            
            JButton btnToggle = new JButton("▼ Masquer");
            btnToggle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnToggle.setForeground(ACCENT);
            btnToggle.setBackground(Color.WHITE);
            btnToggle.setBorderPainted(false);
            btnToggle.setFocusPainted(false);
            btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnToggle.addActionListener(e -> {
                filtresVisible = !filtresVisible;
                btnToggle.setText(filtresVisible ? "▼ Masquer" : "▶ Afficher");
                // Logique pour masquer/afficher les filtres
            });
            
            titlePanel.add(lblFiltres, BorderLayout.WEST);
            titlePanel.add(btnToggle, BorderLayout.EAST);
            
            mainPanel.add(titlePanel);
            mainPanel.add(Box.createVerticalStrut(20));
            
            // Initialisation des composants
            comboDepart = createModernComboBox();
            comboArrivee = createModernComboBox();
            comboCompagnie = createModernComboBox();
            comboClasse = createModernComboBox(new String[]{"Toutes classes", "Économique", "Business", "Première"});
            txtDate = createModernTextField("JJ/MM/AAAA");
            txtPrixMax = createModernTextField("Prix maximum");
            
            remplirComboBoxesModern();
            
            // Grille de filtres
            JPanel gridPanel = new JPanel(new GridBagLayout());
            gridPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 10, 15, 10);
            gbc.weightx = 1.0;
            
            // Ligne 1 : Départ, Arrivée, Date
            gbc.gridy = 0;
            gbc.gridx = 0;
            gridPanel.add(createModernFilterCard("Départ", "Ville de départ", comboDepart), gbc);
            gbc.gridx = 1;
            gridPanel.add(createModernFilterCard("Arrivée", "Ville d'arrivée", comboArrivee), gbc);
            gbc.gridx = 2;
            gridPanel.add(createModernFilterCard("Date", "Date de départ", txtDate), gbc);
            
            // Ligne 2 : Prix, Compagnie, Classe
            gbc.gridy = 1;
            gbc.gridx = 0;
            txtPrixMax.setText("1000");
            gridPanel.add(createModernFilterCard("Budget", "Prix maximum (€)", txtPrixMax), gbc);
            gbc.gridx = 1;
            gridPanel.add(createModernFilterCard("Compagnie", "Compagnie aérienne", comboCompagnie), gbc);
            gbc.gridx = 2;
            gridPanel.add(createModernFilterCard("Classe", "Classe de voyage", comboClasse), gbc);
            
            mainPanel.add(gridPanel);
            mainPanel.add(Box.createVerticalStrut(15));
            
            // Boutons d'action
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            buttonPanel.setBackground(Color.WHITE);
            
            JButton btnRechercher = createModernButton("Rechercher", ACCENT, Color.blue);
            btnRechercher.addActionListener(e -> effectuerRecherche());
            
            JButton btnReset = createModernButton("Réinitialiser", new Color(248, 250, 252), PRIMARY_DARK);
            btnReset.addActionListener(e -> reinitialiserFiltres());
            
            JButton btnAdvanced = createModernButton("Avancé", new Color(248, 250, 252), PRIMARY_DARK);
            btnAdvanced.addActionListener(e -> showAdvancedSearch());
            
            buttonPanel.add(btnRechercher);
            buttonPanel.add(btnReset);
            buttonPanel.add(btnAdvanced);
            
            mainPanel.add(buttonPanel);
            
            return mainPanel;
        }
        
        /**
         * Crée une carte de filtre
         */
        private JPanel createModernFilterCard(String title, String placeholder, JComponent component) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
            
            // Effet de survol
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 1),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)
                    ));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)
                    ));
                }
            });
            
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblTitle.setForeground(PRIMARY_DARK);
            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel lblPlaceholder = new JLabel(placeholder);
            lblPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblPlaceholder.setForeground(TEXT_SECONDARY);
            lblPlaceholder.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
            component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            
            card.add(lblTitle);
            card.add(Box.createVerticalStrut(2));
            card.add(lblPlaceholder);
            card.add(Box.createVerticalStrut(5));
            card.add(component);
            
            return card;
        }
        
        /**
         * Crée une combo box moderne
         */
        private JComboBox<String> createModernComboBox() {
            JComboBox<String> combo = new JComboBox<>();
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            combo.setBackground(Color.WHITE);
            combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
            return combo;
        }
        
        private JComboBox<String> createModernComboBox(String[] items) {
            JComboBox<String> combo = new JComboBox<>(items);
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            combo.setBackground(Color.WHITE);
            combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
            return combo;
        }
        
        /**
         * Crée un champ de texte moderne
         */
        private JTextField createModernTextField(String placeholder) {
            JTextField field = new JTextField() {
                private String placeholderText = placeholder;
                
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getText().isEmpty() && !isFocusOwner()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(TEXT_SECONDARY);
                        g2.setFont(getFont().deriveFont(Font.ITALIC, 12));
                        g2.drawString(placeholderText, 8, getHeight() - 10);
                        g2.dispose();
                    }
                }
            };
            field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            field.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
            field.setBackground(Color.WHITE);
            return field;
        }
        
        /**
         * Crée un bouton moderne
         */
        private JButton createModernButton(String text, Color bgColor, Color fgColor) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(fgColor);
            btn.setBackground(bgColor);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor == ACCENT ? ACCENT_HOVER : BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Effet de survol
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (bgColor == ACCENT) {
                        btn.setBackground(ACCENT_HOVER);
                    } else {
                        btn.setBackground(new Color(241, 245, 249));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(bgColor);
                }
            });
            
            return btn;
        }
        
        /**
         * Remplit les combo boxes avec les données
         */
        private void remplirComboBoxesModern() {
            comboDepart.removeAllItems();
            comboDepart.addItem("Tous les départs");
            for (String dest : serviceVols.getToutesDestinations()) {
                comboDepart.addItem(dest);
            }
            
            comboArrivee.removeAllItems();
            comboArrivee.addItem("Toutes les destinations");
            for (String dest : serviceVols.getToutesDestinations()) {
                comboArrivee.addItem(dest);
            }
            
            comboCompagnie.removeAllItems();
            comboCompagnie.addItem("Toutes compagnies");
            for (String comp : serviceVols.getToutesCompagnies()) {
                comboCompagnie.addItem(comp);
            }
            
            txtDate.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtPrixMax.setText("1000");
        }
        
        /**
         * Crée le panel du tableau amélioré
         */
      private JPanel createEnhancedTablePanel() {
    // Définition des colonnes
    String[] colonnes = {"Vol", "Compagnie", "Départ", "Arrivée", "Date", "Durée", "Prix", "Places", "Action"};
    
    modelTable = new DefaultTableModel(colonnes, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 8; // Seule la colonne Action est éditable
        }
        
        @Override
        public Class<?> getColumnClass(int column) {
            if (column == 7) return Integer.class; // Places est un Integer
            if (column == 6) return String.class;  // Prix est un String
            return String.class;
        }
    };
    
    tableVols = new JTable(modelTable);
    tableVols.setRowHeight(70);
    tableVols.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tableVols.setSelectionBackground(new Color(224, 242, 254));
    tableVols.setShowGrid(false);
    tableVols.setIntercellSpacing(new Dimension(0, 0));
    tableVols.setRowMargin(0);
    
    // Style de l'en-tête - CORRECTION ICI
    JTableHeader headerTable = tableVols.getTableHeader();
    headerTable.setFont(new Font("Segoe UI", Font.BOLD, 13));
    headerTable.setBackground(PRIMARY); // Fond bleu foncé
    headerTable.setForeground(Color.blue); // Texte BLANC (bien visible)
    headerTable.setPreferredSize(new Dimension(0, 45));
    headerTable.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
    
    // Centrer le texte de l'en-tête
    ((DefaultTableCellRenderer) headerTable.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
    
    // Renderer par défaut pour le corps du tableau
    tableVols.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Alternance de couleurs pour les lignes
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            }
            
            setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
            
            // Texte en noir pour le corps
            setForeground(PRIMARY_DARK);
            
            return c;
        }
    });
    
    // Renderer spécial pour le prix (colonne 6)
    tableVols.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value != null ? value.toString() : "");
            setForeground(SUCCESS); // Vert
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setHorizontalAlignment(RIGHT);
            setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
            
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            }
            return c;
        }
    });
    
    // Renderer spécial pour les places (colonne 7)
    tableVols.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                int places = (Integer) value;
                setText(String.valueOf(places));
                
                // Code couleur selon la disponibilité
                if (places > 20) {
                    setForeground(SUCCESS); // Vert
                } else if (places > 5) {
                    setForeground(WARNING); // Orange
                } else if (places > 0) {
                    setForeground(DANGER); // Rouge
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    setText("Complet");
                    setForeground(TEXT_SECONDARY); // Gris
                }
                
                setHorizontalAlignment(CENTER);
            }
            
            setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
            
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            }
            
            return c;
        }
    });
    
    // Configuration de la colonne Action (colonne 8)
    TableColumn actionColumn = tableVols.getColumnModel().getColumn(8);
    actionColumn.setCellRenderer(new ButtonRenderer());
    actionColumn.setCellEditor(new ButtonEditor(new JCheckBox(), tableVols, modelTable));
    actionColumn.setPreferredWidth(100);
    actionColumn.setMinWidth(100);
    actionColumn.setMaxWidth(120);
    
    // Ajustement des largeurs des colonnes
    tableVols.getColumnModel().getColumn(0).setPreferredWidth(80);  // Vol
    tableVols.getColumnModel().getColumn(1).setPreferredWidth(120); // Compagnie
    tableVols.getColumnModel().getColumn(2).setPreferredWidth(100); // Départ
    tableVols.getColumnModel().getColumn(3).setPreferredWidth(100); // Arrivée
    tableVols.getColumnModel().getColumn(4).setPreferredWidth(140); // Date
    tableVols.getColumnModel().getColumn(5).setPreferredWidth(60);  // Durée
    tableVols.getColumnModel().getColumn(6).setPreferredWidth(80);  // Prix
    tableVols.getColumnModel().getColumn(7).setPreferredWidth(60);  // Places
    
    JScrollPane scrollPane = new JScrollPane(tableVols);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.getViewport().setBackground(BACKGROUND);
    scrollPane.getViewport().setPreferredSize(new Dimension(800, 350));
    
    JPanel panelTable = new JPanel(new BorderLayout());
    panelTable.setBackground(BACKGROUND);
    panelTable.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
    
    // Barre d'information
    JPanel infoBar = createInfoBar();
    
    panelTable.add(infoBar, BorderLayout.NORTH);
    panelTable.add(scrollPane, BorderLayout.CENTER);
    
    return panelTable;
}
        
        /**
         * Crée la barre d'information
         */
        private JPanel createInfoBar() {
            JPanel infoBar = new JPanel(new BorderLayout());
            infoBar.setBackground(Color.WHITE);
            infoBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 0, 1, BORDER_COLOR),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
            ));
            
            lblResultCount = new JLabel("0 vols trouvés");
            lblResultCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblResultCount.setForeground(PRIMARY_DARK);
            
            JPanel triPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            triPanel.setBackground(Color.WHITE);
            
            // Options de tri
            String[] tris = {"Prix ↑", "Prix ↓", "Durée", "Départ"};
            for (String tri : tris) {
                JLabel lblTri = new JLabel(tri);
                lblTri.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblTri.setForeground(ACCENT);
                lblTri.setCursor(new Cursor(Cursor.HAND_CURSOR));
                lblTri.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        lblTri.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        lblTri.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    }
                });
                triPanel.add(lblTri);
            }
            
            infoBar.add(lblResultCount, BorderLayout.WEST);
            infoBar.add(triPanel, BorderLayout.EAST);
            
            return infoBar;
        }
        
        /**
         * Affiche la recherche avancée
         */
        private void showAdvancedSearch() {
            JOptionPane.showMessageDialog(this,
                "Recherche avancée\n\n" +
                "• Vols avec escales\n" +
                "• Compagnies partenaires\n" +
                "• Offres spéciales\n" +
                "• Vols + Hôtel",
                "Recherche avancée",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        /**
         * Groupe de filtres (méthode utilitaire)
         */
        private JPanel creerGroupeFiltre(String label, JComponent composant) {
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBackground(Color.WHITE);
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(TEXT_SECONDARY);
            panel.add(lbl, BorderLayout.NORTH);
            panel.add(composant, BorderLayout.CENTER);
            return panel;
        }
        
        /**
         * Effectue la recherche de vols
         */
        private void effectuerRecherche() {
            modelTable.setRowCount(0);
            
            // Récupération des critères de recherche
            String depart = comboDepart.getSelectedIndex() == 0 ? null : (String) comboDepart.getSelectedItem();
            String arrivee = comboArrivee.getSelectedIndex() == 0 ? null : (String) comboArrivee.getSelectedItem();
            String compagnie = comboCompagnie.getSelectedIndex() == 0 ? null : (String) comboCompagnie.getSelectedItem();
            String classe = comboClasse.getSelectedIndex() == 0 ? null : (String) comboClasse.getSelectedItem();
            Double prixMax = txtPrixMax.getText().isEmpty() ? null : Double.parseDouble(txtPrixMax.getText());
            
            // Appel au service de recherche
            List<Vol> resultats = serviceVols.rechercheAvancee(depart, arrivee, null, prixMax, compagnie, classe);
            
            // Affichage des résultats
            for (Vol vol : resultats) {
                long duree = java.time.Duration.between(vol.getDateDepart(), vol.getDateArrivee()).toHours();
                modelTable.addRow(new Object[]{
                    vol.getNumeroVol(),
                    vol.getCompagnie(),
                    vol.getAeroportDepart(),
                    vol.getAeroportArrivee(),
                    vol.getDateDepart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    duree + "h",
                    String.format("%.2f €", vol.getPrix()),
                    vol.getPlacesDisponibles(),
                    "Réserver"
                });
            }
            updateResultCount();
        }
        
        /**
         * Réinitialise les filtres
         */
        private void reinitialiserFiltres() {
            comboDepart.setSelectedIndex(0);
            comboArrivee.setSelectedIndex(0);
            comboCompagnie.setSelectedIndex(0);
            comboClasse.setSelectedIndex(0);
            txtPrixMax.setText("1000");
            chargerTousLesVols();
        }
        
        /**
         * Charge tous les vols
         */
        private void chargerTousLesVols() {
            modelTable.setRowCount(0);
            for (Vol vol : serviceVols.getTousLesVols()) {
                long duree = java.time.Duration.between(vol.getDateDepart(), vol.getDateArrivee()).toHours();
                modelTable.addRow(new Object[]{
                    vol.getNumeroVol(),
                    vol.getCompagnie(),
                    vol.getAeroportDepart(),
                    vol.getAeroportArrivee(),
                    vol.getDateDepart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    duree + "h",
                    String.format("%.2f €", vol.getPrix()),
                    vol.getPlacesDisponibles(),
                    "Réserver"
                });
            }
            updateResultCount();
        }
        
        /**
         * Met à jour le compteur de résultats
         */
        private void updateResultCount() {
            int count = modelTable.getRowCount();
            lblResultCount.setText(count + " vol" + (count > 1 ? "s" : "") + " trouvé" + (count > 1 ? "s" : ""));
        }
    }
    
    // ==================== RENDERERS PERSONNALISÉS ====================
    
    /**
     * Renderer moderne pour le tableau
     */
    private class ModernTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Alternance de couleurs pour les lignes
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            }
            
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            return c;
        }
    }
    
    /**
     * Renderer pour le bouton d'action
     */
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(ACCENT);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    /**
     * Éditeur pour le bouton d'action
     */
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private DefaultTableModel model;
        
        public ButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel model) {
            super(checkBox);
            this.table = table;
            this.model = model;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setBackground(ACCENT);
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }
        
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String numeroVol = (String) model.getValueAt(row, 0);
                    Vol vol = serviceVols.trouverVolParNumero(numeroVol);
                    if (vol != null) {
                        panelReservation.setVol(vol);
                        cardLayout.show(panelPrincipal, "RESERVATION");
                    }
                }
            }
            isPushed = false;
            return label;
        }
    }
    
    // ==================== PANEL RÉSERVATION ====================
    
// ==================== PANEL RÉSERVATION CORRIGÉ ====================
private class PanelReservation extends JPanel {
    private Vol vol;
    private JTextField txtNom, txtPrenom;
    private JComboBox<String> comboClasse;
    private JLabel lblInfoVol, lblPrixTotal;
    private Passager passager;
    private String siegeSelectionne;
    private JPanel grilleSieges;
    private ButtonGroup groupeSieges;
    
    public PanelReservation() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        
        // En-tête
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
        
        // Contenu principal avec les deux panels
        JPanel contenu = new JPanel(new GridLayout(1, 2, 30, 0));
        contenu.setBackground(BACKGROUND);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Panel 1 : Formulaire passager
        JPanel panelFormulaire = createFormulairePanel();
        
        // Panel 2 : Sélection des sièges (AVEC VRAIS SIÈGES)
        JPanel panelSiege = createSiegePanelComplet();
        
        contenu.add(panelFormulaire);
        contenu.add(panelSiege);
        
        add(contenu, BorderLayout.CENTER);
    }
    
    /**
     * Crée l'en-tête
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JLabel lblTitre = new JLabel("Réservation");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(TEXT_PRIMARY);
        header.add(lblTitre, BorderLayout.WEST);
        return header;
    }
    
    /**
     * Crée le formulaire passager
     */
    private JPanel createFormulairePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Informations du vol
        lblInfoVol = new JLabel("Sélectionnez un vol");
        lblInfoVol.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblInfoVol.setForeground(PRIMARY_DARK);
        lblInfoVol.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblInfoVol);
        panel.add(Box.createVerticalStrut(20));
        
        // Champ Nom
        JLabel lblNom = new JLabel("Nom complet");
        lblNom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNom.setForeground(TEXT_SECONDARY);
        lblNom.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblNom);
        panel.add(Box.createVerticalStrut(5));
        
        txtNom = new JTextField();
        txtNom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtNom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtNom.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtNom);
        panel.add(Box.createVerticalStrut(15));
        
        // Champ Prénom
        JLabel lblPrenom = new JLabel("Prénom");
        lblPrenom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrenom.setForeground(TEXT_SECONDARY);
        lblPrenom.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPrenom);
        panel.add(Box.createVerticalStrut(5));
        
        txtPrenom = new JTextField();
        txtPrenom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPrenom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        txtPrenom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtPrenom.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtPrenom);
        panel.add(Box.createVerticalStrut(15));
        
        // Champ Classe
        JLabel lblClasse = new JLabel("Classe de voyage");
        lblClasse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblClasse.setForeground(TEXT_SECONDARY);
        lblClasse.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblClasse);
        panel.add(Box.createVerticalStrut(5));
        
        comboClasse = new JComboBox<>(new String[]{"Economique", "Business", "Première"});
        comboClasse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboClasse.setBackground(Color.WHITE);
        comboClasse.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        comboClasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        comboClasse.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(comboClasse);
        panel.add(Box.createVerticalStrut(20));
        
        // Prix total
        lblPrixTotal = new JLabel("Total: 0,00 €");
        lblPrixTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblPrixTotal.setForeground(SUCCESS);
        lblPrixTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPrixTotal);
        panel.add(Box.createVerticalStrut(20));
        
        // Bouton Continuer
        JButton btnContinuer = new JButton("Continuer vers le paiement →");
        btnContinuer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnContinuer.setForeground(Color.WHITE);
        btnContinuer.setBackground(ACCENT);
        btnContinuer.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnContinuer.setFocusPainted(false);
        btnContinuer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinuer.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnContinuer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        btnContinuer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnContinuer.setBackground(ACCENT_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnContinuer.setBackground(ACCENT);
            }
        });
        
        btnContinuer.addActionListener(e -> {
            if (validerFormulaire()) {
                continuerVersPaiement();
            }
        });
        
        panel.add(btnContinuer);
        panel.add(Box.createVerticalStrut(10));
        
        // Bouton Retour
        JButton btnRetour = new JButton("← Retour aux vols");
        btnRetour.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRetour.setForeground(ACCENT);
        btnRetour.setContentAreaFilled(false);
        btnRetour.setBorderPainted(false);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRetour.addActionListener(e -> cardLayout.show(panelPrincipal, "RECHERCHE"));
        panel.add(btnRetour);
        
        return panel;
    }
    
    /**
     * Crée le panel de sélection des sièges avec de vrais sièges
     */
    private JPanel createSiegePanelComplet() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Titre
        JLabel lblSiegeTitre = new JLabel("Sélectionnez votre siège");
        lblSiegeTitre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSiegeTitre.setForeground(PRIMARY_DARK);
        lblSiegeTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblSiegeTitre);
        panel.add(Box.createVerticalStrut(5));
        
        JLabel lblSiegeSub = new JLabel("Cliquez sur un siège disponible");
        lblSiegeSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSiegeSub.setForeground(TEXT_SECONDARY);
        lblSiegeSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblSiegeSub);
        panel.add(Box.createVerticalStrut(20));
        
        // Plan de l'avion
        JPanel planAvion = new JPanel();
        planAvion.setLayout(new BoxLayout(planAvion, BoxLayout.Y_AXIS));
        planAvion.setBackground(new Color(248, 250, 252));
        planAvion.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        planAvion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Cockpit
        JPanel cockpit = new JPanel(new FlowLayout());
        cockpit.setBackground(new Color(226, 232, 240));
        cockpit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel lblCockpit = new JLabel("✈️ Cockpit");
        lblCockpit.setFont(new Font("Segoe UI", Font.BOLD, 10));
        cockpit.add(lblCockpit);
        planAvion.add(cockpit);
        
        // Grille de sièges
        JPanel grille = new JPanel(new GridLayout(0, 4, 10, 10));
        grille.setBackground(new Color(248, 250, 252));
        grille.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Créer les boutons de sièges
        String[] classes = {"E", "B", "P"};
        for (int c = 0; c < classes.length; c++) {
            for (int i = 1; i <= 4; i++) {
                String numeroSiege = classes[c] + i;
                JButton btnSiege = new JButton(numeroSiege);
                btnSiege.setFont(new Font("Segoe UI", Font.BOLD, 11));
                btnSiege.setPreferredSize(new Dimension(45, 45));
                
                // Simuler des sièges disponibles (80%) et occupés (20%)
                if (Math.random() > 0.2) {
                    btnSiege.setBackground(new Color(219, 234, 254)); // Bleu clair
                    btnSiege.setForeground(PRIMARY_DARK);
                    btnSiege.setEnabled(true);
                } else {
                    btnSiege.setBackground(new Color(229, 231, 235)); // Gris
                    btnSiege.setForeground(TEXT_SECONDARY);
                    btnSiege.setEnabled(false);
                }
                
                btnSiege.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                btnSiege.setFocusPainted(false);
                btnSiege.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnSiege.addActionListener(e -> {
                    // Désélectionner tous les autres boutons
                    for (Component comp : grille.getComponents()) {
                        if (comp instanceof JButton) {
                            comp.setBackground(new Color(219, 234, 254));
                        }
                    }
                    // Sélectionner celui-ci
                    btnSiege.setBackground(ACCENT);
                    siegeSelectionne = btnSiege.getText();
                });
                
                grille.add(btnSiege);
            }
        }
        
        planAvion.add(grille);
        
        // Allée
        JPanel allee = new JPanel(new FlowLayout());
        allee.setBackground(new Color(226, 232, 240));
        allee.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel lblAllee = new JLabel("⬇ Allée centrale ⬇");
        lblAllee.setFont(new Font("Segoe UI", Font.BOLD, 10));
        allee.add(lblAllee);
        planAvion.add(allee);
        
        panel.add(planAvion);
        panel.add(Box.createVerticalStrut(20));
        
        // Légende
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Disponible
        JPanel dispPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dispPanel.setBackground(Color.WHITE);
        JPanel dispColor = new JPanel();
        dispColor.setBackground(new Color(219, 234, 254));
        dispColor.setPreferredSize(new Dimension(15, 15));
        dispColor.setBorder(BorderFactory.createLineBorder(new Color(147, 197, 253)));
        dispPanel.add(dispColor);
        dispPanel.add(new JLabel("Disponible"));
        
        // Occupé
        JPanel occPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        occPanel.setBackground(Color.WHITE);
        JPanel occColor = new JPanel();
        occColor.setBackground(new Color(229, 231, 235));
        occColor.setPreferredSize(new Dimension(15, 15));
        occColor.setBorder(BorderFactory.createLineBorder(new Color(156, 163, 175)));
        occPanel.add(occColor);
        occPanel.add(new JLabel("Occupé"));
        
        // Sélectionné
        JPanel selPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        selPanel.setBackground(Color.WHITE);
        JPanel selColor = new JPanel();
        selColor.setBackground(ACCENT);
        selColor.setPreferredSize(new Dimension(15, 15));
        selColor.setBorder(BorderFactory.createLineBorder(ACCENT_HOVER));
        selPanel.add(selColor);
        selPanel.add(new JLabel("Sélectionné"));
        
        legendPanel.add(dispPanel);
        legendPanel.add(occPanel);
        legendPanel.add(selPanel);
        
        panel.add(legendPanel);
        
        return panel;
    }
    
    /**
     * Définit le vol à réserver
     */
    public void setVol(Vol vol) {
        this.vol = vol;
        lblInfoVol.setText(String.format("<html><b>%s</b><br>%s → %s<br>%s</html>",
            vol.getNumeroVol(), 
            vol.getAeroportDepart(), 
            vol.getAeroportArrivee(),
            vol.getDateDepart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        lblPrixTotal.setText(String.format("Total: %.2f €", vol.getPrix()));
    }
    
    /**
     * Crée un champ de formulaire
     */
    private JPanel creerChampFormulaire(String label, JComponent champ) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_SECONDARY);
        panel.add(lbl, BorderLayout.NORTH);
        
        if (champ instanceof JTextField) styleTextField((JTextField)champ);
        panel.add(champ, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Valide le formulaire
     */
    private boolean validerFormulaire() {
        if (txtNom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir votre nom");
            txtNom.requestFocus();
            return false;
        }
        if (txtPrenom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir votre prénom");
            txtPrenom.requestFocus();
            return false;
        }
        return true;
    }
    
    /**
     * Continue vers le paiement
     */
    private void continuerVersPaiement() {
        passager = new Passager(
            txtNom.getText().trim(),
            txtPrenom.getText().trim(),
            LocalDate.of(1990, 1, 1),
            "P" + System.currentTimeMillis(),
            (String) comboClasse.getSelectedItem()
        );
        
        Reservation reservation = serviceReservations.creerReservation(clientConnecte, vol);
        reservation.ajouterPassager(passager);
        
        panelPaiement.setReservation(reservation);
        cardLayout.show(panelPrincipal, "PAIEMENT");
    }
}
    
    // ==================== PANEL PAIEMENT ====================
    
    /**
     * Panel de paiement
     */
    private class PanelPaiement extends JPanel {
        private JComboBox<String> comboMethode;
        private Reservation reservation;
        
        public PanelPaiement() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND);
            
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
            ));
            
            JLabel lblTitre = new JLabel("Paiement sécurisé");
            lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitre.setForeground(PRIMARY_DARK);
            lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(lblTitre);
            card.add(Box.createVerticalStrut(30));
            
            // Méthodes de paiement
            String[] methodes = {
                "Carte Bancaire",
                "PayPal", 
                "Virement Bancaire",
                "Cryptomonnaie"
            };
            comboMethode = new JComboBox<>(methodes);
            comboMethode.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            comboMethode.setMaximumSize(new Dimension(400, 50));
            card.add(new JLabel("Méthode de paiement"));
            card.add(Box.createVerticalStrut(10));
            card.add(comboMethode);
            card.add(Box.createVerticalStrut(30));
            
            // Simulation de carte bancaire
            JPanel panelCarte = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(PRIMARY);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                    g2d.drawString("**** **** **** 4242", 30, 80);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    g2d.drawString("TITULAIRE", 30, 120);
                    g2d.drawString("12/25", 280, 120);
                }
            };
            panelCarte.setPreferredSize(new Dimension(400, 150));
            panelCarte.setMaximumSize(new Dimension(400, 150));
            card.add(panelCarte);
            card.add(Box.createVerticalStrut(30));
            
            JButton btnPayer = creerBoutonPrincipal("Payer maintenant");
            btnPayer.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnPayer.addActionListener(e -> effectuerPaiement());
            card.add(btnPayer);
            
            add(card);
        }
        
        /**
         * Définit la réservation
         */
        public void setReservation(Reservation r) {
            this.reservation = r;
        }
        
        /**
         * Effectue le paiement
         */
        private void effectuerPaiement() {
            ServicePaiement.MethodePaiement methode = ServicePaiement.MethodePaiement.values()
                [comboMethode.getSelectedIndex()];
            
            boolean succes = servicePaiement.effectuerPaiement(reservation, methode);
            
            if (succes) {
                serviceNotifications.envoyerConfirmationReservation(clientConnecte, reservation);
                panelConfirmation.setReservation(reservation);
                cardLayout.show(panelPrincipal, "CONFIRMATION");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Paiement refusé. Veuillez réessayer.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ==================== PANEL CONFIRMATION ====================
    
    /**
     * Panel de confirmation de réservation
     */
    private class PanelConfirmation extends JPanel {
        private Reservation reservation;
        private JTextArea txtBillet;
        
        public PanelConfirmation() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND);
            
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
            ));
            
            // Icône de succès
            JLabel lblIcone = new JLabel("✓");
            lblIcone.setFont(new Font("Segoe UI", Font.BOLD, 72));
            lblIcone.setForeground(SUCCESS);
            lblIcone.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(lblIcone);
            card.add(Box.createVerticalStrut(20));
            
            JLabel lblTitre = new JLabel("Réservation confirmée!");
            lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitre.setForeground(PRIMARY_DARK);
            lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(lblTitre);
            card.add(Box.createVerticalStrut(30));
            
            // Zone du billet
            txtBillet = new JTextArea(15, 50);
            txtBillet.setFont(new Font("Consolas", Font.PLAIN, 12));
            txtBillet.setEditable(false);
            txtBillet.setBackground(new Color(248, 250, 252));
            txtBillet.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JScrollPane scroll = new JScrollPane(txtBillet);
            card.add(scroll);
            card.add(Box.createVerticalStrut(20));
            
            // Boutons d'action
            JPanel panelBoutons = new JPanel(new FlowLayout());
            panelBoutons.setBackground(Color.WHITE);
            
            JButton btnTelecharger = creerBoutonPrincipal("Télécharger le billet");
            btnTelecharger.addActionListener(e -> telechargerBillet());
            
            JButton btnNouveau = new JButton("Nouvelle recherche");
            btnNouveau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnNouveau.addActionListener(e -> cardLayout.show(panelPrincipal, "RECHERCHE"));
            
            panelBoutons.add(btnTelecharger);
            panelBoutons.add(btnNouveau);
            card.add(panelBoutons);
            
            add(card);
        }
        
        /**
         * Définit la réservation et affiche le billet
         */
        public void setReservation(Reservation r) {
            this.reservation = r;
            String billet = GenerateurPDF.genererBilletPDF(r, r.getPassagers().get(0), "E12");
            txtBillet.setText(billet);
        }
        
        /**
         * Simule le téléchargement du billet
         */
        private void telechargerBillet() {
            JOptionPane.showMessageDialog(this, 
                "Billet téléchargé: billet_" + reservation.getIdReservation() + ".pdf",
                "Téléchargement", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ==================== PANEL ADMIN ====================
    
    /**
     * Panel d'administration
     */
    private class PanelAdmin extends JPanel {
        private JLabel lblStatsVols, lblStatsPassagers, lblStatsRevenus;
        private DefaultTableModel modelReservations;
        
        public PanelAdmin() {
            setLayout(new BorderLayout());
            setBackground(BACKGROUND);
            
            // En-tête
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(PRIMARY_DARK);
            header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            JLabel lblTitre = new JLabel("Tableau de bord Admin");
            lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblTitre.setForeground(TEXT_PRIMARY);
            header.add(lblTitre, BorderLayout.WEST);
            add(header, BorderLayout.NORTH);
            
            // Statistiques
            JPanel panelStats = new JPanel(new GridLayout(1, 3, 20, 0));
            panelStats.setBackground(BACKGROUND);
            panelStats.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            
            lblStatsVols = new JLabel("0");
            lblStatsPassagers = new JLabel("0");
            lblStatsRevenus = new JLabel("0 €");
            
            panelStats.add(creerCarteStat("Vols", lblStatsVols, ACCENT));
            panelStats.add(creerCarteStat("Passagers", lblStatsPassagers, SUCCESS));
            panelStats.add(creerCarteStat("Revenus", lblStatsRevenus, WARNING));
            
            add(panelStats, BorderLayout.CENTER);
            
            // Tableau des réservations
            String[] cols = {"ID", "Client", "Vol", "Date", "Montant", "Statut", "Actions"};
            modelReservations = new DefaultTableModel(cols, 0);
            JTable table = new JTable(modelReservations);
            styleTable(table);
            
            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
            add(scroll, BorderLayout.SOUTH);
            
            refreshStats();
        }
        
        /**
         * Crée une carte de statistique
         */
        private JPanel creerCarteStat(String titre, JLabel valeur, Color couleur) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
            ));
            
            JLabel lblTitre = new JLabel(titre);
            lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblTitre.setForeground(TEXT_SECONDARY);
            
            valeur.setFont(new Font("Segoe UI", Font.BOLD, 36));
            valeur.setForeground(couleur);
            
            card.add(lblTitre);
            card.add(Box.createVerticalStrut(10));
            card.add(valeur);
            
            return card;
        }
        
        /**
         * Rafraîchit les statistiques
         */
        private void refreshStats() {
            if (adminConnecte != null) {
                lblStatsVols.setText(String.valueOf(adminConnecte.getNombreVols()));
                lblStatsPassagers.setText(String.valueOf(adminConnecte.getNombrePassagers()));
                lblStatsRevenus.setText(String.format("%.2f €", adminConnecte.getRevenusTotaux()));
            }
        }
    }
    
    // ==================== UTILITAIRES UI ====================
    
    /**
     * Style pour les champs de texte
     */
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(Color.WHITE);
    }
    
    /**
     * Style pour les combo boxes
     */
    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
    }
    
    /**
     * Crée un bouton principal
     */
    private JButton creerBoutonPrincipal(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(ACCENT);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet de survol
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ACCENT_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ACCENT);
            }
        });
        
        return btn;
    }
    
    /**
     * Style pour les tableaux
     */
    private void styleTable(JTable table) {
        table.setRowHeight(50);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(224, 242, 254));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY);
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(0, 45));
    }
    
    /**
     * Déconnexion de l'utilisateur
     */
    private void deconnecter() {
        clientConnecte = null;
        adminConnecte = null;
        estAdmin = false;
        sidebar.setVisible(false);
        cardLayout.show(panelPrincipal, "CONNEXION");
    }
    
    // ==================== PANEL MES RÉSERVATIONS ====================
private class PanelProfil extends JPanel {
    private JTable tableReservations;
    private DefaultTableModel modelReservations;
    private JLabel lblMessage;
    
    public PanelProfil() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        
        // En-tête
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
        
        // Contenu principal
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        
        // Charger les réservations
        chargerReservations();
    }
    
    /**
     * Crée l'en-tête du panel
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        JLabel lblTitre = new JLabel("Mes réservations");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitre.setForeground(TEXT_PRIMARY);
        
        JLabel lblSousTitre = new JLabel("Consultez et gérez vos voyages");
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSousTitre.setForeground(TEXT_SECONDARY);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(lblTitre);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblSousTitre);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        return header;
    }
    
    /**
     * Crée le contenu principal
     */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Carte blanche pour le contenu
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Message si aucune réservation
        lblMessage = new JLabel("Vous n'avez aucune réservation pour le moment.");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMessage.setForeground(TEXT_SECONDARY);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Tableau des réservations
        String[] colonnes = {"N° Réservation", "Vol", "Date", "Passagers", "Montant", "Statut", "Action"};
        modelReservations = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seule la colonne Action est éditable
            }
        };
        
        tableReservations = new JTable(modelReservations);
        tableReservations.setRowHeight(60);
        tableReservations.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableReservations.setSelectionBackground(new Color(224, 242, 254));
        tableReservations.setShowGrid(false);
        tableReservations.setIntercellSpacing(new Dimension(0, 0));
        
        // Style de l'en-tête
        JTableHeader headerTable = tableReservations.getTableHeader();
        headerTable.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerTable.setBackground(PRIMARY);
        headerTable.setForeground(Color.WHITE);
        headerTable.setPreferredSize(new Dimension(0, 45));
        headerTable.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));
        
        // Renderer pour le statut
        tableReservations.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String statut = value != null ? value.toString() : "";
                setText(statut);
                setHorizontalAlignment(CENTER);
                
                if ("Confirmée".equals(statut)) {
                    setForeground(SUCCESS);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else if ("En attente".equals(statut)) {
                    setForeground(WARNING);
                } else if ("Annulée".equals(statut)) {
                    setForeground(DANGER);
                }
                
                setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                
                return c;
            }
        });
        
        // Bouton d'action dans la dernière colonne
        TableColumn actionColumn = tableReservations.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ProfilButtonEditor(new JCheckBox()));
        actionColumn.setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableReservations);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        card.add(scrollPane, BorderLayout.CENTER);
        panel.add(card, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Charge les réservations de l'utilisateur
     */
    private void chargerReservations() {
        modelReservations.setRowCount(0);
        
        if (clientConnecte == null) {
            return;
        }
        
        // Récupérer les réservations du client
        List<Reservation> reservations = serviceReservations.getReservationsClient(clientConnecte);
        
        if (reservations.isEmpty()) {
            // Afficher un message si pas de réservations
            JPanel messagePanel = new JPanel(new GridBagLayout());
            messagePanel.setBackground(Color.WHITE);
            
            JLabel lblIcon = new JLabel("📅");
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 64));
            lblIcon.setForeground(TEXT_SECONDARY);
            
            JLabel lblMessage = new JLabel("Vous n'avez aucune réservation pour le moment.");
            lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblMessage.setForeground(TEXT_SECONDARY);
            
            JButton btnRechercher = new JButton("Rechercher un vol");
            btnRechercher.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnRechercher.setForeground(Color.WHITE);
            btnRechercher.setBackground(ACCENT);
            btnRechercher.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
            btnRechercher.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRechercher.addActionListener(e -> cardLayout.show(panelPrincipal, "RECHERCHE"));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 20, 0);
            messagePanel.add(lblIcon, gbc);
            
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 20, 0);
            messagePanel.add(lblMessage, gbc);
            
            gbc.gridy = 2;
            messagePanel.add(btnRechercher, gbc);
            
            JPanel card = (JPanel) ((JPanel) getComponent(1)).getComponent(0);
            card.removeAll();
            card.add(messagePanel, BorderLayout.CENTER);
            card.revalidate();
            card.repaint();
        } else {
            // Afficher les réservations dans le tableau
            for (Reservation r : reservations) {
                modelReservations.addRow(new Object[]{
                    "RES" + String.format("%04d", r.getIdReservation()),
                    r.getVol().getNumeroVol(),
                    r.getVol().getDateDepart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    r.getPassagers().size() + " passager(s)",
                    String.format("%.2f €", r.calculerMontantTotal()),
                    r.getStatut(),
                    "Détails"
                });
            }
        }
    }
    
    /**
     * Éditeur pour le bouton Détails
     */
    private class ProfilButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        
        public ProfilButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Détails" : value.toString();
            button.setText(label);
            button.setBackground(ACCENT);
            button.setForeground(Color.WHITE);
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                JOptionPane.showMessageDialog(panelPrincipal,
                    "Fonctionnalité à venir : Détails de la réservation",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            isPushed = false;
            return label;
        }
    }
}
    private class PanelSieges extends JPanel {
        public PanelSieges() { setBackground(BACKGROUND); }
    }
    // ==================== PANEL MON PROFIL ====================
private class PanelMesReservations extends JPanel {
    private JTextField txtNom, txtPrenom, txtEmail, txtTelephone;
    private JPasswordField txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    private JLabel lblMembreDepuis;
    private JTable tableReservations;
    private DefaultTableModel modelReservations;
    
    public PanelMesReservations() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        
        // En-tête
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
        
        // Contenu principal
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        // Onglet Informations personnelles
        JPanel infoPanel = createInfoPanel();
        tabbedPane.addTab("👤 Informations personnelles", infoPanel);
        
        // Onglet Sécurité
        JPanel securityPanel = createSecurityPanel();
        tabbedPane.addTab("🔒 Sécurité", securityPanel);
        
        // Onglet Historique des réservations
        JPanel historiquePanel = createHistoriquePanel();
        tabbedPane.addTab("📅 Historique des réservations", historiquePanel);
        
        // Onglet Préférences
        JPanel preferencesPanel = createPreferencesPanel();
        tabbedPane.addTab("⚙️ Préférences", preferencesPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Charger les données du profil
        chargerProfil();
    }
    
    /**
     * Crée l'en-tête
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        JLabel lblTitre = new JLabel("Mon profil");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitre.setForeground(TEXT_PRIMARY);
        
        JLabel lblSousTitre = new JLabel("Gérez vos informations personnelles et vos préférences");
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSousTitre.setForeground(TEXT_SECONDARY);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(lblTitre);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblSousTitre);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        // Badge membre
        JPanel badgePanel = new JPanel();
        badgePanel.setBackground(new Color(255, 255, 255, 30));
        badgePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        lblMembreDepuis = new JLabel("Membre depuis " + LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblMembreDepuis.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMembreDepuis.setForeground(TEXT_PRIMARY);
        badgePanel.add(lblMembreDepuis);
        
        header.add(badgePanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Crée le panel d'informations personnelles
     */
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Avatar
        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        lblAvatar.setForeground(ACCENT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblAvatar, gbc);
        
        // Nom
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createLabel("Nom complet"), gbc);
        
        gbc.gridx = 1;
        txtNom = new JTextField(20);
        styleTextField(txtNom);
        panel.add(txtNom, gbc);
        
        // Prénom
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createLabel("Prénom"), gbc);
        
        gbc.gridx = 1;
        txtPrenom = new JTextField(20);
        styleTextField(txtPrenom);
        panel.add(txtPrenom, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(createLabel("Adresse email"), gbc);
        
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        styleTextField(txtEmail);
        panel.add(txtEmail, gbc);
        
        // Téléphone
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(createLabel("Téléphone"), gbc);
        
        gbc.gridx = 1;
        txtTelephone = new JTextField(20);
        styleTextField(txtTelephone);
        panel.add(txtTelephone, gbc);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAnnuler.setForeground(TEXT_SECONDARY);
        btnAnnuler.setBackground(new Color(248, 250, 252));
        btnAnnuler.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnSauvegarder = new JButton("Sauvegarder les modifications");
        btnSauvegarder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSauvegarder.setForeground(Color.WHITE);
        btnSauvegarder.setBackground(ACCENT);
        btnSauvegarder.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnSauvegarder.setFocusPainted(false);
        btnSauvegarder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSauvegarder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSauvegarder.setBackground(ACCENT_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnSauvegarder.setBackground(ACCENT);
            }
        });
        
        btnSauvegarder.addActionListener(e -> sauvegarderProfil());
        
        buttonPanel.add(btnAnnuler);
        buttonPanel.add(btnSauvegarder);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    /**
     * Crée le panel de sécurité
     */
    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Mot de passe actuel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createLabel("Mot de passe actuel"), gbc);
        
        gbc.gridx = 1;
        txtCurrentPassword = new JPasswordField(20);
        styleTextField(txtCurrentPassword);
        panel.add(txtCurrentPassword, gbc);
        
        // Nouveau mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createLabel("Nouveau mot de passe"), gbc);
        
        gbc.gridx = 1;
        txtNewPassword = new JPasswordField(20);
        styleTextField(txtNewPassword);
        panel.add(txtNewPassword, gbc);
        
        // Confirmation
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createLabel("Confirmer le mot de passe"), gbc);
        
        gbc.gridx = 1;
        txtConfirmPassword = new JPasswordField(20);
        styleTextField(txtConfirmPassword);
        panel.add(txtConfirmPassword, gbc);
        
        // Force du mot de passe
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JProgressBar passwordStrength = new JProgressBar(0, 100);
        passwordStrength.setValue(0);
        passwordStrength.setStringPainted(true);
        passwordStrength.setString("Force du mot de passe");
        passwordStrength.setForeground(new Color(156, 163, 175));
        panel.add(passwordStrength, gbc);
        
        // Recommandations
        JPanel recommandationsPanel = new JPanel();
        recommandationsPanel.setLayout(new BoxLayout(recommandationsPanel, BoxLayout.Y_AXIS));
        recommandationsPanel.setBackground(new Color(248, 250, 252));
        recommandationsPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        JLabel lblRecommandations = new JLabel("Recommandations :");
        lblRecommandations.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRecommandations.setForeground(PRIMARY_DARK);
        lblRecommandations.setAlignmentX(Component.LEFT_ALIGNMENT);
        recommandationsPanel.add(lblRecommandations);
        recommandationsPanel.add(Box.createVerticalStrut(5));
        
        String[] recos = {
            "• Au moins 8 caractères",
            "• Au moins une lettre majuscule",
            "• Au moins un chiffre",
            "• Au moins un caractère spécial"
        };
        
        for (String reco : recos) {
            JLabel lblReco = new JLabel(reco);
            lblReco.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblReco.setForeground(TEXT_SECONDARY);
            recommandationsPanel.add(lblReco);
        }
        
        gbc.gridy = 4;
        panel.add(recommandationsPanel, gbc);
        
        // Bouton de changement
        JButton btnChanger = new JButton("Changer le mot de passe");
        btnChanger.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChanger.setForeground(Color.WHITE);
        btnChanger.setBackground(ACCENT);
        btnChanger.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnChanger.setFocusPainted(false);
        btnChanger.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnChanger.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnChanger.setBackground(ACCENT_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnChanger.setBackground(ACCENT);
            }
        });
        
        btnChanger.addActionListener(e -> changerMotDePasse(passwordStrength));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnChanger);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(30, 10, 10, 10);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    /**
     * Crée le panel d'historique
     */
    private JPanel createHistoriquePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        String[] colonnes = {"Réservation", "Vol", "Date", "Passagers", "Montant", "Statut"};
        modelReservations = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableReservations = new JTable(modelReservations);
        tableReservations.setRowHeight(50);
        tableReservations.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableReservations.setShowGrid(false);
        tableReservations.setIntercellSpacing(new Dimension(0, 0));
        
        // Style de l'en-tête
        JTableHeader header = tableReservations.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));
        
        // Renderer pour le statut
        tableReservations.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String statut = value != null ? value.toString() : "";
                
                if ("Confirmée".equals(statut)) {
                    setForeground(SUCCESS);
                } else if ("En attente".equals(statut)) {
                    setForeground(WARNING);
                } else if ("Annulée".equals(statut)) {
                    setForeground(DANGER);
                }
                
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableReservations);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crée le panel des préférences
     */
    private JPanel createPreferencesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridwidth = 2;
        
        // Notifications par email
        JCheckBox chkEmailNotifications = new JCheckBox("Recevoir les notifications par email");
        chkEmailNotifications.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkEmailNotifications.setBackground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(chkEmailNotifications, gbc);
        
        // Rappels de vol
        JCheckBox chkRappels = new JCheckBox("Recevoir des rappels avant les vols");
        chkRappels.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkRappels.setBackground(Color.WHITE);
        gbc.gridy = 1;
        panel.add(chkRappels, gbc);
        
        // Offres promotionnelles
        JCheckBox chkOffres = new JCheckBox("Recevoir des offres promotionnelles");
        chkOffres.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkOffres.setBackground(Color.WHITE);
        gbc.gridy = 2;
        panel.add(chkOffres, gbc);
        
        // Langue préférée
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(createLabel("Langue préférée"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> comboLangue = new JComboBox<>(new String[]{"Français", "English", "العربية", "Español"});
        comboLangue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboLangue.setBackground(Color.WHITE);
        comboLangue.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(comboLangue, gbc);
        
        // Devise préférée
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(createLabel("Devise préférée"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> comboDevise = new JComboBox<>(new String[]{"Euro (€)", "Dollar ($)", "Livre (£)", "Dirham (MAD)"});
        comboDevise.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboDevise.setBackground(Color.WHITE);
        comboDevise.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(comboDevise, gbc);
        
        // Bouton sauvegarder
        JButton btnSauvegarder = new JButton("Sauvegarder les préférences");
        btnSauvegarder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSauvegarder.setForeground(Color.WHITE);
        btnSauvegarder.setBackground(ACCENT);
        btnSauvegarder.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        btnSauvegarder.setFocusPainted(false);
        btnSauvegarder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSauvegarder);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    /**
     * Crée un label stylisé
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Charge les données du profil
     */
    private void chargerProfil() {
        if (clientConnecte != null) {
            txtNom.setText(clientConnecte.getNom());
            txtPrenom.setText(clientConnecte.getPrenom());
            txtEmail.setText(clientConnecte.getEmail());
            txtTelephone.setText(clientConnecte.getTelephone());
            
            // Charger l'historique des réservations
            chargerHistorique();
        }
    }
    
    /**
     * Charge l'historique des réservations
     */
    private void chargerHistorique() {
        modelReservations.setRowCount(0);
        
        List<Reservation> reservations = serviceReservations.getReservationsClient(clientConnecte);
        
        for (Reservation r : reservations) {
            modelReservations.addRow(new Object[]{
                "RES" + String.format("%04d", r.getIdReservation()),
                r.getVol().getNumeroVol(),
                r.getVol().getDateDepart().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                r.getPassagers().size() + " passager(s)",
                String.format("%.2f €", r.calculerMontantTotal()),
                r.getStatut()
            });
        }
    }
    
    /**
     * Sauvegarde les modifications du profil
     */
    private void sauvegarderProfil() {
        JOptionPane.showMessageDialog(this, 
            "Profil mis à jour avec succès !", 
            "Succès", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Change le mot de passe
     */
    private void changerMotDePasse(JProgressBar strengthBar) {
        String current = new String(txtCurrentPassword.getPassword());
        String nouveau = new String(txtNewPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());
        
        if (current.isEmpty() || nouveau.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!nouveau.equals(confirm)) {
            JOptionPane.showMessageDialog(this, 
                "Les mots de passe ne correspondent pas", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (nouveau.length() < 8) {
            JOptionPane.showMessageDialog(this, 
                "Le mot de passe doit contenir au moins 8 caractères", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "Mot de passe changé avec succès !", 
            "Succès", 
            JOptionPane.INFORMATION_MESSAGE);
        
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
    // ==================== PANEL AVIS & REVIEWS ====================
private class PanelAvis extends JPanel {
    private JComboBox<String> comboVol;
    private JSlider sliderNote;
    private JTextArea txtCommentaire;
    private JTable tableAvis;
    private DefaultTableModel modelAvis;
    private JLabel lblNoteMoyenne;
    private JProgressBar progressBar5, progressBar4, progressBar3, progressBar2, progressBar1;
    private JLabel lblCount5, lblCount4, lblCount3, lblCount2, lblCount1;
    
    public PanelAvis() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        
        // En-tête
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);
        
        // Contenu principal avec split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(BACKGROUND);
        splitPane.setBorder(null);
        splitPane.setDividerSize(10);
        
        // Panel gauche : Formulaire d'avis
        JPanel leftPanel = createAvisFormPanel();
        
        // Panel droit : Statistiques et avis récents
        JPanel rightPanel = createStatsPanel();
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setResizeWeight(0.5);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Charger les avis
        chargerAvis();
    }
    
    /**
     * Crée l'en-tête
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        JLabel lblTitre = new JLabel("Avis et Reviews");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitre.setForeground(TEXT_PRIMARY);
        
        JLabel lblSousTitre = new JLabel("Partagez votre expérience de voyage");
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSousTitre.setForeground(TEXT_SECONDARY);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(lblTitre);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblSousTitre);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        return header;
    }
    
    /**
     * Crée le formulaire de dépôt d'avis
     */
    private JPanel createAvisFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Titre du formulaire
        JLabel lblFormTitre = new JLabel("Donnez votre avis");
        lblFormTitre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFormTitre.setForeground(PRIMARY_DARK);
        lblFormTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblFormTitre);
        card.add(Box.createVerticalStrut(20));
        
        // Sélection du vol
        JLabel lblVol = new JLabel("Vol concerné");
        lblVol.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblVol.setForeground(PRIMARY_DARK);
        lblVol.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblVol);
        card.add(Box.createVerticalStrut(5));
        
        comboVol = new JComboBox<>();
        comboVol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboVol.setBackground(Color.WHITE);
        comboVol.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        comboVol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comboVol.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Remplir avec les vols disponibles
        remplirComboVols();
        
        card.add(comboVol);
        card.add(Box.createVerticalStrut(20));
        
        // Note avec étoiles
        JLabel lblNote = new JLabel("Votre note");
        lblNote.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNote.setForeground(PRIMARY_DARK);
        lblNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblNote);
        card.add(Box.createVerticalStrut(5));
        
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        notePanel.setBackground(Color.WHITE);
        notePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderNote = new JSlider(1, 5, 5);
        sliderNote.setBackground(Color.WHITE);
        sliderNote.setPreferredSize(new Dimension(200, 40));
        sliderNote.setMajorTickSpacing(1);
        sliderNote.setPaintTicks(true);
        sliderNote.setPaintLabels(true);
        
        JLabel lblEtoiles = new JLabel("★★★★★");
        lblEtoiles.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblEtoiles.setForeground(new Color(255, 215, 0)); // Or
        
        sliderNote.addChangeListener(e -> {
            int note = sliderNote.getValue();
            String etoiles = "";
            for (int i = 0; i < 5; i++) {
                etoiles += (i < note) ? "★" : "☆";
            }
            lblEtoiles.setText(etoiles);
        });
        
        notePanel.add(sliderNote);
        notePanel.add(lblEtoiles);
        
        card.add(notePanel);
        card.add(Box.createVerticalStrut(20));
        
        // Commentaire
        JLabel lblCommentaire = new JLabel("Votre commentaire");
        lblCommentaire.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCommentaire.setForeground(PRIMARY_DARK);
        lblCommentaire.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblCommentaire);
        card.add(Box.createVerticalStrut(5));
        
        txtCommentaire = new JTextArea(5, 20);
        txtCommentaire.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCommentaire.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        txtCommentaire.setLineWrap(true);
        txtCommentaire.setWrapStyleWord(true);
        
        JScrollPane scrollCommentaire = new JScrollPane(txtCommentaire);
        scrollCommentaire.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollCommentaire.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollCommentaire.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        card.add(scrollCommentaire);
        card.add(Box.createVerticalStrut(25));
        
        // Bouton Publier
        JButton btnPublier = new JButton("Publier mon avis");
        btnPublier.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPublier.setForeground(Color.WHITE);
        btnPublier.setBackground(ACCENT);
        btnPublier.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btnPublier.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPublier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPublier.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPublier.setBackground(ACCENT_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnPublier.setBackground(ACCENT);
            }
        });
        
        btnPublier.addActionListener(e -> publierAvis());
        
        card.add(btnPublier);
        
        panel.add(card, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crée le panel des statistiques
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(10, new Color(0, 0, 0, 0.1f)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Titre
        JLabel lblStatsTitre = new JLabel("Statistiques des avis");
        lblStatsTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblStatsTitre.setForeground(PRIMARY_DARK);
        lblStatsTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblStatsTitre);
        card.add(Box.createVerticalStrut(20));
        
        // Note moyenne
        JPanel moyennePanel = new JPanel();
        moyennePanel.setLayout(new BoxLayout(moyennePanel, BoxLayout.Y_AXIS));
        moyennePanel.setBackground(new Color(250, 250, 252));
        moyennePanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        moyennePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblMoyenneTitre = new JLabel("Note moyenne");
        lblMoyenneTitre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMoyenneTitre.setForeground(TEXT_SECONDARY);
        lblMoyenneTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblNoteMoyenne = new JLabel("4.5 ★");
        lblNoteMoyenne.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblNoteMoyenne.setForeground(new Color(255, 215, 0));
        lblNoteMoyenne.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        moyennePanel.add(Box.createVerticalStrut(15));
        moyennePanel.add(lblMoyenneTitre);
        moyennePanel.add(lblNoteMoyenne);
        moyennePanel.add(Box.createVerticalStrut(15));
        
        card.add(moyennePanel);
        card.add(Box.createVerticalStrut(25));
        
        // Distribution des notes
        JLabel lblDistribution = new JLabel("Distribution des notes");
        lblDistribution.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDistribution.setForeground(PRIMARY_DARK);
        lblDistribution.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblDistribution);
        card.add(Box.createVerticalStrut(15));
        
        // Barres de progression pour chaque note
        card.add(createNoteLine("5 ★", progressBar5 = new JProgressBar(), lblCount5 = new JLabel("0")));
        card.add(Box.createVerticalStrut(10));
        card.add(createNoteLine("4 ★", progressBar4 = new JProgressBar(), lblCount4 = new JLabel("0")));
        card.add(Box.createVerticalStrut(10));
        card.add(createNoteLine("3 ★", progressBar3 = new JProgressBar(), lblCount3 = new JLabel("0")));
        card.add(Box.createVerticalStrut(10));
        card.add(createNoteLine("2 ★", progressBar2 = new JProgressBar(), lblCount2 = new JLabel("0")));
        card.add(Box.createVerticalStrut(10));
        card.add(createNoteLine("1 ★", progressBar1 = new JProgressBar(), lblCount1 = new JLabel("0")));
        card.add(Box.createVerticalStrut(25));
        
        // Avis récents
        JLabel lblRecents = new JLabel("Avis récents");
        lblRecents.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRecents.setForeground(PRIMARY_DARK);
        lblRecents.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblRecents);
        card.add(Box.createVerticalStrut(15));
        
        // Tableau des avis
        String[] colonnes = {"Client", "Vol", "Note", "Commentaire", "Date"};
        modelAvis = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableAvis = new JTable(modelAvis);
        tableAvis.setRowHeight(50);
        tableAvis.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableAvis.setShowGrid(false);
        tableAvis.setIntercellSpacing(new Dimension(0, 0));
        
        // Renderer pour les notes
        tableAvis.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    int note = Integer.parseInt(value.toString());
                    String etoiles = "";
                    for (int i = 0; i < 5; i++) {
                        etoiles += (i < note) ? "★" : "☆";
                    }
                    setText(etoiles);
                    setForeground(new Color(255, 215, 0));
                    setHorizontalAlignment(CENTER);
                }
                
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                
                return c;
            }
        });
        
        JScrollPane scrollAvis = new JScrollPane(tableAvis);
        scrollAvis.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollAvis.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollAvis.setPreferredSize(new Dimension(400, 200));
        
        card.add(scrollAvis);
        
        panel.add(card, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crée une ligne de distribution des notes
     */
    private JPanel createNoteLine(String label, JProgressBar bar, JLabel count) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(PRIMARY_DARK);
        lbl.setPreferredSize(new Dimension(40, 20));
        
        bar.setBackground(new Color(229, 231, 235));
        bar.setForeground(new Color(255, 215, 0));
        bar.setStringPainted(true);
        bar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        count.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        count.setForeground(TEXT_SECONDARY);
        count.setPreferredSize(new Dimension(30, 20));
        count.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(lbl, BorderLayout.WEST);
        panel.add(bar, BorderLayout.CENTER);
        panel.add(count, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Remplit la combo box avec les vols disponibles
     */
    private void remplirComboVols() {
        comboVol.removeAllItems();
        comboVol.addItem("Sélectionnez un vol");
        
        for (Vol vol : serviceVols.getTousLesVols()) {
            comboVol.addItem(vol.getNumeroVol() + " - " + vol.getAeroportDepart() + " → " + vol.getAeroportArrivee());
        }
    }
    
    /**
     * Publie un avis
     */
    private void publierAvis() {
        if (comboVol.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un vol", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtCommentaire.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir un commentaire", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (clientConnecte == null) {
            JOptionPane.showMessageDialog(this, 
                "Vous devez être connecté pour publier un avis", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer les données
        String volSelectionne = (String) comboVol.getSelectedItem();
        String numeroVol = volSelectionne.split(" - ")[0];
        int note = sliderNote.getValue();
        String commentaire = txtCommentaire.getText().trim();
        
        // Ajouter l'avis
        serviceAvis.ajouterAvis(
            clientConnecte.getId(),
            clientConnecte.getPrenom() + " " + clientConnecte.getNom(),
            numeroVol,
            note,
            commentaire
        );
        
        JOptionPane.showMessageDialog(this, 
            "Votre avis a été publié avec succès !", 
            "Succès", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Réinitialiser le formulaire
        comboVol.setSelectedIndex(0);
        sliderNote.setValue(5);
        txtCommentaire.setText("");
        
        // Recharger les avis
        chargerAvis();
    }
    
    /**
     * Charge les avis
     */
    private void chargerAvis() {
        List<Avis> avisList = serviceAvis.getTousLesAvis();
        
        // Mettre à jour les statistiques
        if (!avisList.isEmpty()) {
            double moyenne = avisList.stream().mapToInt(Avis::getNote).average().orElse(0.0);
            lblNoteMoyenne.setText(String.format("%.1f ★", moyenne));
            
            // Compter les notes
            long count5 = avisList.stream().filter(a -> a.getNote() == 5).count();
            long count4 = avisList.stream().filter(a -> a.getNote() == 4).count();
            long count3 = avisList.stream().filter(a -> a.getNote() == 3).count();
            long count2 = avisList.stream().filter(a -> a.getNote() == 2).count();
            long count1 = avisList.stream().filter(a -> a.getNote() == 1).count();
            
            int total = avisList.size();
            
            lblCount5.setText(String.valueOf(count5));
            lblCount4.setText(String.valueOf(count4));
            lblCount3.setText(String.valueOf(count3));
            lblCount2.setText(String.valueOf(count2));
            lblCount1.setText(String.valueOf(count1));
            
            progressBar5.setValue((int) (count5 * 100 / total));
            progressBar4.setValue((int) (count4 * 100 / total));
            progressBar3.setValue((int) (count3 * 100 / total));
            progressBar2.setValue((int) (count2 * 100 / total));
            progressBar1.setValue((int) (count1 * 100 / total));
        }
        
        // Mettre à jour le tableau
        modelAvis.setRowCount(0);
        for (Avis avis : avisList) {
            modelAvis.addRow(new Object[]{
                avis.getNomClient(),
                avis.getNumeroVol(),
                avis.getNote(),
                avis.getCommentaire().length() > 50 ? 
                    avis.getCommentaire().substring(0, 50) + "..." : 
                    avis.getCommentaire(),
                avis.getDateFormatee()
            });
        }
    }
}
    
    // ==================== POINT D'ENTRÉE ====================
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetrePrincipale());
    }
}
