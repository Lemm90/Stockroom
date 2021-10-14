package ru.khorolskiy.stockroom.client;

import java.io.BufferedInputStream;
import java.io.IOException;

public class Request {
    private String username; // имя пользователя
    private String command; //комманда
    private String filename; //имя файла
    private String patch; //путь файла
    private String extension; // расширение
    private int size; // размер??
    private byte[] array; // массив байтов файла

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getArray() {
        return array;
    }

    public void setArray(byte[] array) {
        this.array = array;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void definitionOfExtensionAndFilename(String name) {
        int i = name.lastIndexOf('.');
        if (i > 0) {
            setExtension(name.substring(i));
            setFilename(name.substring(0, i));
        }
    }

    public void addArray(BufferedInputStream bufferedInputStream) {
        array = new byte[getSize()];
        try {
            bufferedInputStream.read(array);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
