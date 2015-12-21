package tailminuseff.fx;

import java.io.File;

public class FileOpenCommand {

    public FileOpenCommand(File file) {
        this.file = file;
    }

    private final File file;

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "FileOpenCommand{" + "file=" + file + '}';
    }

}
