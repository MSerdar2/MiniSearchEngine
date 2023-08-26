package minisearchengine.gui.console;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public class ConsoleCapturer extends OutputStream {

    private StringBuilder buffer;
    protected Consumer<String> lineConsumer;

    public ConsoleCapturer(Consumer<String> lineConsumer) {
        this.buffer = new StringBuilder();
        this.lineConsumer = lineConsumer;
    }

    @Override
    public void write(int b) throws IOException {
        char character = (char) b;

        buffer.append(character);

        if (character == '\n') {
            this.lineConsumer.accept(buffer.toString());
            buffer.delete(0, buffer.length());
        }
    }

}
