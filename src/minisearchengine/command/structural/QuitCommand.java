package minisearchengine.command.structural;

import minisearchengine.command.Command;

import java.util.List;

public class QuitCommand extends Command {

    public QuitCommand() {
        super("quit");
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getDescription() {
        return "Terminates and quits the program.";
    }

    @Override
    public void execute(List<String> arguments) {
        getAttachedSession().getLogger().info("- Quitting the session");
        getAttachedSession().stopSession();
        System.exit(0);
    }

}
