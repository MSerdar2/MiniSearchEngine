package minisearchengine.util;

import java.util.LinkedList;
import java.util.List;

public class CommandParser {

    protected String name;
    protected List<String> arguments;

    /**
     * Both constructs the object
     * and parses given input into arguments and command name
     * @param input
     */
    public CommandParser(String input) {
        this.arguments = new LinkedList<>();

        StringBuilder builder = new StringBuilder();

        boolean quoting = false;
        boolean escaping = false;

        for (char character : input.toCharArray()) {
            if (!quoting && character == '\\') {
                if (escaping) {
                    builder.append(character);
                    escaping = false;
                    continue;
                }
                escaping = true;
                continue;
            }

            if (character == '"') {
                if (escaping) {
                    builder.append(character);
                    escaping = false;
                    continue;
                }
                if (quoting) {
                    quoting = false;
                    push(builder.toString());
                    builder = new StringBuilder();
                    continue;
                }
                quoting = true;
                continue;
            }

            if (!quoting && character == ' ') {
                if (escaping) throw new IllegalArgumentException("Invalid escaping...");
                push(builder.toString());
                builder = new StringBuilder();
                continue;
            }

            builder.append(character);
            escaping = false;
        }

        push(builder.toString());
    }

    /**
     * Pushes a new token into the argument list
     * @param token Token to be pushed
     */
    private void push(String token) {
        if(token.isEmpty()) return;

        if (name == null) {
            this.name = token.trim();
            return;
        }

        this.arguments.add(token.trim());
    }

    public String getName() {
        return name;
    }

    public List<String> getArguments() {
        return arguments;
    }

}
