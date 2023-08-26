package minisearchengine.command;

import minisearchengine.SearchSession;

import java.util.List;

public abstract class Command {

    protected final String name;
    protected SearchSession attachedSession;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getUsage();

    public abstract String getDescription();

    public SearchSession getAttachedSession() {
        return attachedSession;
    }

    public void attachSession(SearchSession session) {
        this.attachedSession = session;
    }

    public abstract void execute(List<String> arguments) throws SyntaxError;

    /**
     * Checks whether given flagName exists within given arguments
     * @param arguments Arguments
     * @param flagName Flag name to be searched
     * @return Whether given flagName exists within given arguments or not
     */
    protected boolean containsFlag(List<String> arguments, String flagName) {
        for (String argument : arguments) {
            if (argument.equals("-" + flagName)) {
                return true;
            }
        }

        return false;
    }

}
