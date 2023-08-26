package minisearchengine.command;

public class SyntaxError extends Exception {

    public SyntaxError() {
        super();
    }

    public SyntaxError(String reason) {
        super(reason);
    }

}
