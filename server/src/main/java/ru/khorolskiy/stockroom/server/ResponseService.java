package ru.khorolskiy.stockroom.server;

public class ResponseService extends Response {

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
