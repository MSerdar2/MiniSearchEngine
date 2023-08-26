package minisearchengine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

// Utilities related to File System IO
public class FSUtilities {

    public static void traverseFolder(String path, BiConsumer<String, String> consumer) {
        File folder = new File(path);
        File[] children = folder.listFiles();
        if (children == null) return;
        for (File child : children) {
            if (child.isDirectory()) {
                traverseFolder(child.getAbsolutePath(), consumer);
            } else {
                consumer.accept(child.getName(), child.getAbsolutePath());
            }
        }
    }

    private static void mkdirsIfAbsent(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    /* ---------------------------------------------- */

    public static String readString(String path) {
        return new String(readBytes(path), StandardCharsets.UTF_8);
    }

    public static byte[] readBytes(String path) {
        File file = new File(path);

        try {
            if (file.length() > Integer.MAX_VALUE) {
                throw new IllegalStateException("File size too large to read in Java environment...");
            }

            byte[] bytes = new byte[((int) file.length())];

            FileInputStream fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();

            return bytes;

        } catch (IOException ignored) {
            return new byte[0];
        }
    }

    /* ---------------------------------------------- */

    public static void writeString(String path, String data) {
        writeBytes(path, data.getBytes(StandardCharsets.UTF_8));
    }

    public static void writeBytes(String path, byte[] bytes) {
        File file = new File(path);

        try {
            mkdirsIfAbsent(file);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();

        } catch (IOException ignored) { }
    }

}
