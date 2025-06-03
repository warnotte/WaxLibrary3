package io.github.warnotte.waxlib3.waxlibswingcomponents.Swing.Component;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class JTextFildWithIcon_main {
	public static void main(String[] args) {
        // Configuration du Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Test JTextFieldWithIcon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Titre
        JLabel titleLabel = new JLabel("Test des JTextFieldWithIcon", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);
        
        // Reset gridwidth pour les autres composants
        gbc.gridwidth = 1;
        
        // Exemple 1 : Icône d'attention à droite
        JLabel label1 = new JLabel("Champ avec attention (droite) :");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        frame.add(label1, gbc);
        
        
    
        
        
        JTextFieldWithIcon textField1 = new JTextFieldWithIcon(
        		JTextFieldWithIcon.ATTENTION_icon, 
            false, // icône à droite
            "Attention : Vérifiez la saisie"
        ) {
            @Override
            protected void onIconClick() {
                JOptionPane.showMessageDialog(this, 
                    "Attention clicked!\nVeuillez vérifier votre saisie.", 
                    "Attention", 
                    JOptionPane.WARNING_MESSAGE);
            }
        };
        textField1.setText("Attention required");
        textField1.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        frame.add(textField1, gbc);
        
        // Exemple 2 : Icône de succès à gauche
        JLabel label2 = new JLabel("Champ validé (gauche) :");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        frame.add(label2, gbc);
            
        JTextFieldWithIcon textField2 = new JTextFieldWithIcon(
            JTextFieldWithIcon.QUESTION_icon, 
            true, // icône à gauche
            "Validation réussie"
        ) {
            @Override
            protected void onIconClick() {
                JOptionPane.showMessageDialog(this, 
                    "Correct clicked!\nLa validation est réussie.", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        };
        textField2.setText("Valid input");
        textField2.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        frame.add(textField2, gbc);
        
        // Exemple 3 : Icône d'erreur à droite
        JLabel label3 = new JLabel("Champ en erreur (droite) :");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        frame.add(label3, gbc);
        
        JTextFieldWithIcon textField3 = new JTextFieldWithIcon(
            JTextFieldWithIcon.ERROR_icon, 
            false, // icône à droite
            "Erreur : Format invalide"
        ) {
            @Override
            protected void onIconClick() {
                JOptionPane.showMessageDialog(this, 
                    "Error clicked!\nLe format saisi est invalide.", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        };
        textField3.setText("Invalid format");
        textField3.setPreferredSize(new Dimension(200, 60));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;
        frame.add(textField3, gbc);
        
        // Boutons de test
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton toggleVisibilityButton = new JButton("Toggle Visibility");
        toggleVisibilityButton.addActionListener(e -> {
        	
            textField1.setIconVisible(!textField1.isIconVisible());
            textField2.setIconVisible(!textField2.isIconVisible());
            textField3.setIconVisible(!textField3.isIconVisible());
        });
        buttonPanel.add(toggleVisibilityButton);
        
        /*JButton clearFieldsButton = new JButton("Clear Fields");
        clearFieldsButton.addActionListener(e -> {
            textField1.setText("");
            textField2.setText("");
            textField3.setText("");
        });
        buttonPanel.add(clearFieldsButton);*/
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        frame.add(buttonPanel, gbc);
        
        // Instructions
        JTextArea instructions = new JTextArea(
            "Instructions :\n" +
            "• Cliquez sur les icônes pour voir les messages\n" +
            "• Survolez les icônes pour voir les tooltips\n"
            //"• Utilisez les boutons pour tester la visibilité et vider les champs"
        );
        instructions.setEditable(false);
        instructions.setOpaque(false);
        instructions.setFont(new Font("Arial", Font.ITALIC, 12));
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        frame.add(instructions, gbc);
        
        // Afficher la fenêtre
        frame.setVisible(true);
    }
}
