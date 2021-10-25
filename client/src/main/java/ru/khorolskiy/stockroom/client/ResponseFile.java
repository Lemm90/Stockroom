package ru.khorolskiy.stockroom.client;

public class ResponseFile extends Response {
    private String username; // имя пользователя
    private String filename; //имя файла
    private String extension; // расширение
    private int size; // размер??

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getArray() {
        return array;
    }

    public void setArray(byte[] array) {
        this.array = array;
    }

    private byte[] array; // массив байтов файла
}
