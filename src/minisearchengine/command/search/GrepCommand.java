package minisearchengine.command.search;

import minisearchengine.command.Command;
import minisearchengine.command.SyntaxError;
import minisearchengine.data.BTree;
import minisearchengine.data.FileManifest;

import java.util.LinkedList;
import java.util.List;

public class GrepCommand extends Command {

    public GrepCommand() {
        super("grep");
    }

    @Override
    public String getUsage() {
        return getName() + " <regex>";
    }

    @Override
    public String getDescription() {
        return "Filters out the files which match given regex";
    }

    @Override
    public void execute(List<String> arguments) throws SyntaxError {
        if (arguments.size() == 0)
            throw new SyntaxError("Requires at least one argument.");

        BTree<FileManifest> filesystem = getAttachedSession().getFilesystem();

        if (filesystem == null)
            throw new SyntaxError("No folder were selected.");

        String regex = arguments.get(0);
        List<FileManifest> filtered = new LinkedList<>();

        filesystem.traverse(manifest -> {
            if (manifest.getFilename().matches(regex)) {
                filtered.add(manifest);
            }
        });

        getAttachedSession().getGui()
                .getFilteredFiles()
                .loadFiles(filtered);

        getAttachedSession().getLogger()
                .success(filtered.size() + " file(s) filtered!");
    }

}
