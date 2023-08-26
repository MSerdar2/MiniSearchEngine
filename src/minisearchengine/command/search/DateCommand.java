package minisearchengine.command.search;

import minisearchengine.command.Command;
import minisearchengine.command.SyntaxError;
import minisearchengine.data.BTree;
import minisearchengine.data.FileManifest;

import java.util.LinkedList;
import java.util.List;

public class DateCommand extends Command {

    public DateCommand() {
        super("date");
    }

    @Override
    public String getUsage() {
        return getName() + " [before|after] (unix-timestamp)";
    }

    @Override
    public String getDescription() {
        return "Filters out the files comparing their last modify dates";
    }

    @Override
    public void execute(List<String> arguments) throws SyntaxError {
        if (arguments.size() != 2)
            throw new SyntaxError("Requires 2 arguments.");

        BTree<FileManifest> filesystem = getAttachedSession().getFilesystem();

        if (filesystem == null)
            throw new SyntaxError("No folder were selected.");

        String op = arguments.get(0);

        if (!op.equalsIgnoreCase("before") && !op.equalsIgnoreCase("after")) {
            throw new SyntaxError("First argument must be either 'before' or 'after'");
        }

        long timestamp = Long.parseLong(arguments.get(1));
        List<FileManifest> filtered = new LinkedList<>();

        filesystem.traverse(manifest -> {
            long filetime = manifest.getFiletime().toInstant().getEpochSecond();
            if (op.equalsIgnoreCase("before") && filetime < timestamp) {
                filtered.add(manifest);
            } else if (op.equalsIgnoreCase("after") && filetime > timestamp) {
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
