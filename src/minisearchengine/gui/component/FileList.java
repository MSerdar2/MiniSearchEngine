package minisearchengine.gui.component;

import minisearchengine.data.FileManifest;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.List;

public class FileList extends JTree {

    public FileList() {
        super(new Object[0]);
    }

    public void loadFiles(List<FileManifest> files) {
        TreeNode root = buildList(files);
        this.setModel(new DefaultTreeModel(root, false));
    }

    private TreeNode buildList(List<FileManifest> files) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();

        for (FileManifest file : files) {
            root.add(new DefaultMutableTreeNode(file.getFilename() + " | " + file.getAbsolutePath()));
        }

        return root;
    }

}
