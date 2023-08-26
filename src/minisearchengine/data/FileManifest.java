package minisearchengine.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Objects;

// POJO (Plain Old Java Object) to store file manifests in a lightweight manner
public class FileManifest implements Comparable<FileManifest> {

    protected String filename;
    protected String absolutePath;
    protected FileTime filetime;


    public FileManifest(String filename, String absolutePath) {
        this.filename = filename;
        this.absolutePath = absolutePath;
        Path path = Paths.get(absolutePath);
        try {
            this.filetime = Files.getLastModifiedTime(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getFilename() {
        return filename;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public FileTime getFiletime() {
        return filetime;
    }

    /* --------------------------------------- */

    @Override
    public int compareTo(FileManifest other) {
        return this.filename.compareTo(other.filename);
    }

    @Override
    public String toString() {
        return "FileManifest{" +
                "filename='" + filename + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;

        if (other instanceof String)
            return other.equals(this.filename);

        if (getClass() != other.getClass()) return false;

        FileManifest that = (FileManifest) other;
        if (!Objects.equals(filename, that.filename)) return false;
        return Objects.equals(absolutePath, that.absolutePath);
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + (absolutePath != null ? absolutePath.hashCode() : 0);
        return result;
    }

}
