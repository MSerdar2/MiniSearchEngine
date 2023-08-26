package minisearchengine.command.search;

import minisearchengine.command.Command;
import minisearchengine.command.SyntaxError;
import minisearchengine.data.BTree;
import minisearchengine.data.FileManifest;

import java.util.LinkedList;
import java.util.List;

public class StartsWithCommand extends Command {

    public StartsWithCommand() {
        super("starts-with");
    }

    @Override
    public String getUsage() {
        return getName() + " <text> [-casesensitive]";
    }

    @Override
    public String getDescription() {
        return "Filters out the files whose names start with given argument.";
    }

    @Override
    public void execute(List<String> arguments) throws SyntaxError {
        if (arguments.size() == 0)
            throw new SyntaxError("Requires at least one argument.");

        BTree<FileManifest> filesystem = getAttachedSession().getFilesystem();

        if (filesystem == null)
            throw new SyntaxError("No folder were selected.");

        String filter = arguments.get(0);
        List<FileManifest> filtered = new LinkedList<>();

        boolean casesensitive = containsFlag(arguments, "casesensitive");

        filesystem.traverse(manifest -> {
            if (casesensitive) {
                if (manifest.getFilename().startsWith(filter)) {
                    filtered.add(manifest);
                }

            } else {
                if (manifest.getFilename().toLowerCase().startsWith(filter.toLowerCase())) {
                    filtered.add(manifest);
                }
            }
        });

        getAttachedSession().getGui()
                .getFilteredFiles()
                .loadFiles(filtered);

        getAttachedSession().getLogger()
                .success(filtered.size() + " file(s) filtered!");
    }

}
