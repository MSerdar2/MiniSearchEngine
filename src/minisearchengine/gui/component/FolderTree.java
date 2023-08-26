package minisearchengine.gui.component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.Vector;

public class FolderTree extends JTree {

    private String loadedFolder;

    public FolderTree() {
        super(new Object[0]);
    }

    public void loadFolder(String folderPath) {
        if (folderPath != null && folderPath.equals(loadedFolder)) return;
        TreeNode root = addNodes(null, new File(folderPath));
        this.setModel(new DefaultTreeModel(root, false));
        this.loadedFolder = folderPath;
    }

    private TreeNode addNodes(DefaultMutableTreeNode root, File directory) {
        String directoryPath = directory.getPath();

        DefaultMutableTreeNode directoryNode = new DefaultMutableTreeNode(directory.getName());

        if (root != null) {
            root.add(directoryNode);
        }

        Vector<String> fileNames = new Vector<>();

        String[] underlyingFiles = directory.list();

        if (underlyingFiles == null) return root;

        for (String fileName : underlyingFiles) fileNames.addElement(fileName);
        fileNames.sort(String.CASE_INSENSITIVE_ORDER);

        File currentFile;
        Vector<String> files = new Vector<>();
        for (int i = 0; i < fileNames.size(); i++) {
            String thisObject = fileNames.elementAt(i);
            String newPath;

            if (directoryPath.equals(".")) {
                newPath = thisObject;
            } else {
                newPath = directoryPath + File.separator + thisObject;
            }

            if ((currentFile = new File(newPath)).isDirectory()) {
                addNodes(directoryNode, currentFile);
            } else {
                files.addElement(thisObject);
            }
        }

        for (int i = 0; i < files.size(); i++) {
            directoryNode.add(new DefaultMutableTreeNode(files.elementAt(i)));
        }

        return directoryNode;
    }

}
