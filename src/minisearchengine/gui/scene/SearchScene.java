package minisearchengine.gui.scene;

import minisearchengine.SearchSession;
import minisearchengine.gui.component.FileList;
import minisearchengine.gui.component.FolderTree;
import minisearchengine.gui.console.ConsoleCapturer;
import minisearchengine.util.Logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SearchScene extends ProgramScene {

    protected FolderTree folderTreeComponent;
    protected FileList filteredFiles;
    protected JTextArea commandOutput;
    protected JTextField commandInput;

    protected SearchSession searchSession;

    private boolean clearFlag;

    public SearchScene() {
        super();
        this.setSize(1000, 600);
        this.changeTitle("Search Console");
        this.setLayout(new GridLayout(1, 3, 10, 10));

        // Console
        {
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel heading = new JLabel("Command Panel");
            rightPanel.add(heading);

            JScrollPane outputViewport = new JScrollPane();
            commandOutput = new JTextArea();
            commandOutput.setEnabled(false);
            commandOutput.setDisabledTextColor(new Color(0xFB101928, true));
            commandOutput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            commandOutput.setFont(new Font("Consolas", Font.PLAIN, 12));
            ((DefaultCaret) commandOutput.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            outputViewport.getViewport().add(commandOutput);
            rightPanel.add(outputViewport);

            rightPanel.add(Box.createVerticalStrut(10));

            commandInput = new JTextField();
            commandInput.setMaximumSize(new Dimension(1000, 100));
            rightPanel.add(commandInput);

            this.add(rightPanel);

            commandInput.addActionListener(this::onCommandInput);
        }

        this.searchSession = SearchSession.guiSession(this, new ConsoleCapturer(this::onCommandOutput));
        this.searchSession.getLogger().println("Session initiated.\n ");
        this.searchSession.getLogger().println("At first, before typing any search command,\nplease type the 'switch' command.");
        this.searchSession.getLogger().println("\n-------------------------------------\nCommands:");
        this.searchSession.onInput("help");

        // Left Tree
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel leftTreeHeading = new JLabel("Current Directory Tree");
            panel.add(leftTreeHeading);

            JScrollPane folderTreeViewport = new JScrollPane();
            folderTreeComponent = new FolderTree();
            folderTreeComponent.setSize(folderTreeComponent.getPreferredSize());
            folderTreeViewport.getViewport().add(folderTreeComponent);
            panel.add(folderTreeViewport);

            this.add(panel, 0);
        }

        // Right Tree
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel leftTreeHeading = new JLabel("Filtered Files");
            panel.add(leftTreeHeading);

            JScrollPane folderTreeViewport = new JScrollPane();
            filteredFiles = new FileList();
            filteredFiles.setSize(filteredFiles.getPreferredSize());
            folderTreeViewport.getViewport().add(filteredFiles);
            panel.add(folderTreeViewport);

            this.add(panel);
        }
    }

    /**
     * Invoked whenever a print statement is called via ConsoleCapturer.
     * It appends received line to the command output window
     *
     * @param line Line to be appended
     */
    protected void onCommandOutput(String line) {
        commandOutput.append(line);
    }

    /**
     * Invoked whenever a command in input, and entered from user keyboard
     */
    protected void onCommandInput(ActionEvent e) {
        String command = commandInput.getText();

        if (command.isEmpty()) return;

        Logger logger = searchSession.getLogger();
        logger.println("> {}", command);

        searchSession.onInput(command);
        logger.println("\n-------------------------------------\n");

        commandInput.setText("");

        if (clearFlag) {
            commandOutput.setText("");
            clearFlag = false;
        }
    }

    public FileList getFilteredFiles() {
        return filteredFiles;
    }

    /**
     * Synchronizes the tree from the session with the GUI component
     */
    public void syncFolderTree() {
        folderTreeComponent.loadFolder(searchSession.getCurrentFolder());
    }

    /**
     * Sets the clear flag.
     * In the next update, the console will be cleared
     */
    public void clearConsole() {
        clearFlag = true;
    }

}
