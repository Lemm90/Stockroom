package ru.khorolskiy.stockroom.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    HBox mainMenu, createMenu, downloadMenu, deleteMenu;
    @FXML
    TextField fileToDownload, fileForDownload, fileToDelete;

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //При инициализации создается клиент и подключается к серверу
        Client client = new Client();
        try {
            client.start("localhost", 9021);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        boxVisible("initialize");
    }


    public void create(ActionEvent actionEvent) {
        setCommand("create"); //При нажатии кнопки срабатывает метод, в интерфейсе меняется окно и в Реквест будет записана комманда в соответствии с методом
        boxVisible(getCommand());
    }

    public void download(ActionEvent actionEvent) {
        setCommand("download"); //При нажатии кнопки срабатывает метод, в интерфейсе меняется окно и в Реквест будет записана комманда в соответствии с методом
        boxVisible(getCommand());
    }

    public void delete(ActionEvent actionEvent) {
        setCommand("delete"); //При нажатии кнопки срабатывает метод, в интерфейсе меняется окно и в Реквест будет записана комманда в соответствии с методом
        boxVisible(getCommand());
    }

    public void sendCreate(ActionEvent actionEvent) {
        Request request = new Request();
        try {
            Client.getChannel().channel().writeAndFlush(fillingRequest(request, getCommand(), fileToDownload.getText())).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileToDownload.clear();
        boxVisible("initialize");
    }

    public void sendDelete(ActionEvent actionEvent) {
        Request request = new Request();
        try {
            Client.getChannel().channel().writeAndFlush(fillingRequest(request, getCommand(), fileToDelete.getText())).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileToDelete.clear();
        boxVisible("initialize");
    }

    public Request fillingRequest(Request request, String command, String file) {
        // Копируемый фаил должен лежать в корне программы
        // Наполнение Реквеста информацией из файла
        try (BufferedInputStream buff = new BufferedInputStream(new FileInputStream(file), 200)) {
            request.setUsername(System.getProperty("user.name"));
            request.setCommand(command);
            request.setPatch(file);
            request.definitionOfExtensionAndFilename(file);
            request.setSize(buff.available());
            request.addArray(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }

    public void sendDownload(ActionEvent actionEvent) {
        Request request = new Request();
        try {
            Client.getChannel().channel().writeAndFlush(fillingRequest(request, getCommand(), fileForDownload.getText())).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileForDownload.clear();
        boxVisible("initialize");
    }

    public void boxVisible(String command) {
        mainMenu.setVisible(false);
        mainMenu.setManaged(false);
        createMenu.setVisible(false);
        createMenu.setManaged(false);
        downloadMenu.setVisible(false);
        downloadMenu.setManaged(false);
        deleteMenu.setVisible(false);
        deleteMenu.setManaged(false);
        switch (command) {
            case "initialize":
                mainMenu.setVisible(true);
                mainMenu.setManaged(true);
                break;
            case "create":
                createMenu.setVisible(true);
                createMenu.setManaged(true);
                break;
            case "download":
                downloadMenu.setVisible(true);
                downloadMenu.setManaged(true);
                break;
            case "delete":
                deleteMenu.setVisible(true);
                deleteMenu.setManaged(true);
        }
    }

}
