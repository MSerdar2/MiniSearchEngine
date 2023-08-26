package minisearchengine;

import minisearchengine.command.*;
import minisearchengine.command.search.*;
import minisearchengine.command.structural.ClearCommand;
import minisearchengine.command.structural.HelpCommand;
import minisearchengine.command.structural.QuitCommand;
import minisearchengine.command.structural.SwitchCommand;
import minisearchengine.data.BTree;
import minisearchengine.data.FileManifest;
import minisearchengine.gui.scene.SearchScene;
import minisearchengine.gui.console.ConsoleCapturer;
import minisearchengine.util.CommandParser;
import minisearchengine.util.FSUtilities;
import minisearchengine.util.Logger;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchSession {

    protected boolean running;
    protected String currentFolder;
    protected BTree<FileManifest> filesystem;
    protected Map<String, Command> registeredCommands;
    protected Logger logger;
    protected SearchScene gui;

    /**
     * This constructor is private.
     * Because this one uses Builder Pattern.
     * And it can only be instantiated by the static building functions
     */
    private SearchSession() {
        this.running = true;
        this.registeredCommands = new HashMap<>();
        this.logger = new Logger(false);
    }

    public boolean isRunning() {
        return running;
    }

    public Logger getLogger() {
        return logger;
    }

    public BTree<FileManifest> getFilesystem() {
        return filesystem;
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public SearchScene getGui() {
        return gui;
    }

    public Collection<Command> getCommands() {
        return registeredCommands.values();
    }

    public Command getCommand(String name) {
        return registeredCommands.get(name);
    }

    /* --------------------------------------- */

    /**
     * Takes in a command, and registers it into the registry
     *
     * @param command Command to be registered into the session
     */
    public void registerCommand(Command command) {
        command.attachSession(this);
        this.registeredCommands.put(command.getName(), command);
    }

    /**
     * Traverses files and subfolders in given folder path.
     * And stores a manifest of traversed files in the filesystem tree
     * to be used later by the GUI and searching algorithms.
     *
     * @param folderPath Folder to be switched in
     * @return Whether succeeded or not
     */
    public boolean switchToFolder(String folderPath) {
        if (!new File(folderPath).exists()) {
            return false;
        }

        this.currentFolder = folderPath;
        this.filesystem = new BTree<>();
        FSUtilities.traverseFolder(folderPath, (fileName, path) -> {
            filesystem.insert(new FileManifest(fileName, path));
        });
        return true;
    }

    /**
     * Stops the session
     * and detaches all the registered commands
     */
    public void stopSession() {
        this.running = false;
        // Detach from every command
        for (Command command : this.registeredCommands.values()) {
            command.attachSession(null);
        }
    }

    /**
     * Invoked once an input is received.
     * Tries to find command with given name.
     * If exists, executes it with given parameters
     * Else, displays an error message.
     *
     * @param input Input command to be executed
     */
    public void onInput(String input) {
        CommandParser parser = new CommandParser(input);
        String name = parser.getName();
        List<String> arguments = parser.getArguments();

        Command command = registeredCommands.get(name);
        if (command == null) {
            logger.error("Unknown command: {}", name);
            return;
        }

        try {
            command.execute(arguments);

        } catch (SyntaxError syntaxError) {
            String reason = syntaxError.getMessage();
            if (reason == null) logger.error("Unknown exception occurred while executing.");
            else logger.error("Exception occurred: {}", reason);
            logger.warn("Expected usage -> {}", command.getUsage());
        }
    }

    /* -----------Builder-Patterns------------ */

    /**
     * Creates a simple session with all the commands.
     * Best for console-only applications
     *
     * @return A SearchSession with simple configurations
     */
    public static SearchSession simpleSession() {
        SearchSession session = new SearchSession();
        session.registerCommand(new SwitchCommand());
        session.registerCommand(new HelpCommand());
        session.registerCommand(new ClearCommand());
        session.registerCommand(new StartsWithCommand());
        session.registerCommand(new GrepCommand());
        session.registerCommand(new QuitCommand());
        // TODO: Search by ID?
        session.registerCommand(new DateCommand());
        return session;
    }

    /**
     * Creates a session to be used with a GUI.
     * Best for GUI applications.
     *
     * @return A SearchSession with gui-related configurations.
     */
    public static SearchSession guiSession(SearchScene gui, ConsoleCapturer capturer) {
        SearchSession simpleSession = simpleSession();
        simpleSession.gui = gui;
        simpleSession.logger = new Logger(false).withPrintStream(new PrintStream(capturer));
        return simpleSession;
    }

}
