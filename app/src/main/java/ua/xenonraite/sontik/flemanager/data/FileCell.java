package ua.xenonraite.sontik.flemanager.data;

/**
 * Created by Sontik on 22.12.2015.
 */
public class FileCell {
    String fileName;
    int fileSize;

    public FileCell(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
