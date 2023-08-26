package minisearchengine.gui.scene;

import minisearchengine.SearchEngine;
import minisearchengine.auth.PasswordEncrypter;
import minisearchengine.gui.adapter.DocumentChangeAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginScene extends ProgramScene {

    protected JLabel passwordLabel;
    protected JPasswordField passwordField;
    protected JButton proceedButton;

    public LoginScene() {
        super();
        this.setSize(500, 400);
        this.changeTitle("Enter the Password");

        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
        masterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelWithText = new JLabel();
        labelWithText.setText(toWrappedText("Before you proceed to the program, you need to enter the password you set."));
        masterPanel.add(labelWithText);

        masterPanel.add(Box.createRigidArea(new Dimension(0, 120)));

        passwordLabel = new JLabel("Password:");
        passwordLabel.setSize(passwordLabel.getPreferredSize());
        masterPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setSize(230, 27);
        masterPanel.add(passwordField);

        masterPanel.add(Box.createRigidArea(new Dimension(0, 120)));

        proceedButton = new JButton("Proceed");
        proceedButton.setEnabled(false);
        proceedButton.setSize(proceedButton.getPreferredSize());
        masterPanel.add(proceedButton);

        passwordField.getDocument().addDocumentListener((DocumentChangeAdapter) this::onPasswordChanged);
        proceedButton.addActionListener(this::onProceedClicked);

        this.add(masterPanel);
    }

    /*
     * Handler method of the passwordField field.
     * It is invoked, each time input password is changed.
     * It disables proceedButton, whenever the password is empty
     */
    protected void onPasswordChanged(DocumentEvent e) {
        char[] password = this.passwordField.getPassword();
        this.proceedButton.setEnabled(password.length != 0);
    }

    /*
     * Handler method of the proceedButton field.
     * It is invoked, each time proceedButton is pressed.
     * Compares input password, with saved password.
     * If they match, proceeds to the main scene.
     * Else, shows an information dialog
     */
    protected void onProceedClicked(ActionEvent e) {
        byte[] targetPassword = PasswordEncrypter.loadCurrentPassword();
        String inputPassword = new String(this.passwordField.getPassword());

        if (PasswordEncrypter.compare(inputPassword, targetPassword)) {
            SearchEngine.switchScene(new SearchScene());

        } else {
            JOptionPane.showMessageDialog(this,
                    "Incorrect password!\nPlease try to re-enter your password.",
                    "Error Dialog",
                    JOptionPane.ERROR_MESSAGE);
            this.passwordField.setText("");
        }

    }


}
