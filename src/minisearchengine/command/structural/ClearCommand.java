package minisearchengine.command.structural;

import minisearchengine.command.Command;
import minisearchengine.command.SyntaxError;

import java.util.List;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear");
    }

    @Override
    public String getUsage() {
        return getName();
    }

    @Override
    public String getDescription() {
        return "Clears previously logged messages on the console";
    }

    @Override
    public void execute(List<String> arguments) throws SyntaxError {
        getAttachedSession().getGui().clearConsole();
    }

}
