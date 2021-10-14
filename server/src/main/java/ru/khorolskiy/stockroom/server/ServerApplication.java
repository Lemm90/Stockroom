package ru.khorolskiy.stockroom.server;

public class ServerApplication {
    public static void main(String[] args) {
        try {
            new Server().start(9021);
        } catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
    }
}
