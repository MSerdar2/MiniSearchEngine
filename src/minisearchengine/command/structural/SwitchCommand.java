package minisearchengine.command.structural;

import minisearchengine.command.Command;
import minisearchengine.command.SyntaxError;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class SwitchCommand extends Command {

    public SwitchCommand() {
        super("switch");
    }

    @Override
    public String getUsage() {
        return getName() + " [folder_path]";
    }

    @Override
    public String getDescription() {
        return "Switches current working directory.";
    }

    @Override
    public void execute(List<String> arguments) throws SyntaxError {
        String folderPath;

        if (arguments.size() == 0) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setDialogTitle("Select a Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            int result = chooser.showOpenDialog(getAttachedSession().getGui());

            if (result == JFileChooser.CANCEL_OPTION)
                throw new SyntaxError("No folder selected..");

            folderPath = chooser.getSelectedFile().getPath();

        } else {
            folderPath = arguments.get(0);
        }

        if (!getAttachedSession().switchToFolder(folderPath)) {
            throw new SyntaxError("Non-existing folder path");
        }

        getAttachedSession().getLogger().success("Switched to {}", folderPath);
        getAttachedSession().getGui().syncFolderTree();
    }

}
