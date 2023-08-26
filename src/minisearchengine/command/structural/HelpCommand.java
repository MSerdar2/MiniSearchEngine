package minisearchengine.command.structural;

import minisearchengine.command.Command;
import minisearchengine.command.SyntaxError;
import minisearchengine.util.Logger;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public String getUsage() {
        return getName() + " [command_name]";
    }

    @Override
    public String getDescription() {
        return "Prompts this guide";
    }

    @Override
    public void execute(List<String> arguments) throws SyntaxError {
        Logger logger = getAttachedSession().getLogger();

        if (arguments.size() == 0) {
            Collection<Command> commands = getAttachedSession().getCommands()
                    .stream()
                    .sorted(Comparator.comparing(Command::getName))
                    .collect(Collectors.toList());

            for (Command command : commands) {
                logger.println("");
                logger.println("# " + command.getName() + " | " + command.getDescription());
                logger.println("Usage: " + command.getUsage());
            }

        } else {
            String commandName = arguments.get(0);
            Command command = getAttachedSession().getCommand(commandName);

            if (command == null) {
                logger.warn("Unknown command > {}", commandName);
            } else {
                logger.println("# " + command.getName() + " | " + command.getDescription());
                logger.println("Usage: " + command.getUsage());
            }
        }
    }

}
