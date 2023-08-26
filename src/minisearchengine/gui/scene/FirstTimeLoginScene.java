package minisearchengine.gui.scene;

import minisearchengine.SearchEngine;
import minisearchengine.auth.PasswordEncrypter;
import minisearchengine.gui.adapter.DocumentChangeAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FirstTimeLoginScene extends ProgramScene {

    protected JLabel passwordLabel;
    protected JPasswordField passwordField;
    protected JButton acceptButton;

    public FirstTimeLoginScene() {
        super();
        this.setSize(500, 400);
        this.changeTitle("Set a Password");

        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
        masterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel labelWithText = new JLabel();
        labelWithText.setText(toWrappedText("This is the first time you launched the application. You have to set a password in order to proceed.\n\nDo not forget that password, as you'll need for your next sessions!"));
        masterPanel.add(labelWithText);

        masterPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        passwordLabel = new JLabel("Password:");
        passwordLabel.setSize(passwordLabel.getPreferredSize());
        masterPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setSize(230, 27);
        masterPanel.add(passwordField);

        masterPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        acceptButton = new JButton("Set Password");
        acceptButton.setEnabled(false);
        acceptButton.setSize(acceptButton.getPreferredSize());
        masterPanel.add(acceptButton);

        passwordField.getDocument().addDocumentListener((DocumentChangeAdapter) this::onPasswordChanged);
        acceptButton.addActionListener(this::onAcceptClicked);

        this.add(masterPanel);
    }

    /*
     * Handler method of the passwordField field.
     * It is invoked, each time input password is changed.
     * It disables acceptButton, whenever the password is empty
     */
    protected void onPasswordChanged(DocumentEvent e) {
        char[] password = this.passwordField.getPassword();
        this.acceptButton.setEnabled(password.length != 0);
    }

    /*
     * Handler method of the acceptButton field.
     * It is invoked, each time acceptButton is pressed.
     * Displays a confirm dialog to proceed further.
     */
    protected void onAcceptClicked(ActionEvent e) {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure to set this value as the program password?",
                "Confirmation Dialog",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            char[] passwordCharacters = passwordField.getPassword();
            onPasswordConfirmed(new String(passwordCharacters));
        }
    }

    /*
     * Handler method of the acceptButton field.
     * It is invoked, whenever YES option is selected on the confirm dialog
     * It overwrites/sets existing password
     * And then redirects you to Login window
     */
    protected void onPasswordConfirmed(String password) {
        PasswordEncrypter.overwritePassword(password);

        JOptionPane.showMessageDialog(this,
                "Program Password set successfully!\nPress OK to proceed to the next screen.",
                "Success Dialog",
                JOptionPane.INFORMATION_MESSAGE);

        SearchEngine.switchScene(new LoginScene());
    }

}
