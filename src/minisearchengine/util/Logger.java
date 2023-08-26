package minisearchengine.util;

import java.io.PrintStream;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logger {

    public static final Pattern REPLACE_PATTERN = Pattern.compile("\\{}");

    protected boolean debugMode;
    protected PrintStream printStream;

    public Logger(boolean debugMode) {
        this.debugMode = debugMode;
        this.printStream = System.out;
    }

    public Logger withPrintStream(PrintStream printStream) {
        this.printStream = printStream;
        return this;
    }

    public void println(String format, Object... args) {
        printStream.println(replaceFormat(format, args));
    }

    public void success(String format, Object... args) {
        printStream.println("[SUCCESS] " + replaceFormat(format, args));
    }

    public void info(String format, Object... args) {
        printStream.println("[INFO] " + replaceFormat(format, args));
    }

    public void error(String format, Object... args) {
        printStream.println("[ERROR] " + replaceFormat(format, args));
    }

    public void warn(String format, Object... args) {
        printStream.println("[WARN] " + replaceFormat(format, args));
    }

    public void debug(String format, Object... args) {
        if (!debugMode) return;
        printStream.println("[DEBUG] " + replaceFormat(format, args));
    }

    public static String replaceFormat(String format, Object... args) {
        return replaceFormat(format, index -> args[index].toString());
    }

    public static String replaceFormat(String format, Function<Integer, String> evaluator) {
        Matcher matcher = REPLACE_PATTERN.matcher(format);
        StringBuilder builder = new StringBuilder();
        int start = 0, index = 0;

        while (matcher.find()) {
            // Append previous part
            builder.append(format, start, matcher.start());
            start = matcher.end();

            // Evaluate and append new value
            builder.append(evaluator.apply(index++));
        }

        // Append trailing chars
        builder.append(format, start, format.length());

        return builder.toString();
    }

}
