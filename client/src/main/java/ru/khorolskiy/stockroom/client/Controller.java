package ru.khorolskiy.stockroom.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    HBox preMenu, mainMenu, createMenu, downloadMenu, deleteMenu, signInMenu, regMenu;

    @FXML
    TextField fileToDownload, fileForDownload, fileToDelete, nickname, password, newLogin, newNickname;

    @FXML
    PasswordField newPassword;

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
        setCommand("create"); //При нажатии кнопки срабатывает метод, в интерфейсе меняется окно и в РеквестФаил будет записана комманда в соответствии с методом
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
        RequestFile requestFile = new RequestFile();
        try {
            Client.getChannel().channel().writeAndFlush(fillingRequestFile(requestFile, getCommand(), fileToDownload.getText())).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileToDownload.clear();
        boxVisible("mainMenu");
    }

    public void sendDelete(ActionEvent actionEvent) {
        RequestFile requestFile = new RequestFile();
        try {
            Client.getChannel().channel().writeAndFlush(fillingRequestFile(requestFile, getCommand(), fileToDelete.getText())).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileToDelete.clear();
        boxVisible("mainMenu");
    }

    public RequestFile fillingRequestFile(RequestFile requestFile, String command, String file) {
        // Копируемый фаил должен лежать в корне программы
        // Наполнение Реквеста информацией из файла
        try (BufferedInputStream buff = new BufferedInputStream(new FileInputStream(file), 200)) {
            requestFile.setUserID(UserInfo.getUserID());
            requestFile.setCommand(command);
            requestFile.setPatch(file);
            requestFile.definitionOfExtensionAndFilename(file);
            requestFile.setSize(buff.available());
            requestFile.addArray(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestFile;
    }

    public RequestService fillingRequestService (RequestService requestService, String command, String username, String password, String nickname) {
        requestService.setCommand(command);
        requestService.setUsername(username);
        requestService.setPassword(password);
        requestService.setNickname(nickname);
        return requestService;
    }

    public void sendDownload(ActionEvent actionEvent) {
        RequestFile requestFile = new RequestFile();
        try {
            Client.getChannel().channel().writeAndFlush(fillingRequestFile(requestFile, getCommand(), fileForDownload.getText())).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fileForDownload.clear();
        boxVisible("mainMenu");
    }

    public void boxVisible(String command) {
        preMenu.setVisible(false);
        preMenu.setManaged(false);
        signInMenu.setVisible(false);
        signInMenu.setManaged(false);
        regMenu.setVisible(false);
        regMenu.setManaged(false);
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
                preMenu.setVisible(true);
                preMenu.setManaged(true);
                break;
            case "signIn":
                signInMenu.setVisible(true);
                signInMenu.setManaged(true);
                break;
            case "reg":
                regMenu.setVisible(true);
                regMenu.setManaged(true);
                break;
            case "mainMenu":
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
                break;
        }
    }


    public void buttonSignUp(ActionEvent actionEvent) { boxVisible("reg"); }

    public void buttonSignIn(ActionEvent actionEvent) {
        boxVisible("signIn");
    }

    public void AComeIn(ActionEvent actionEvent) {
        RequestService requestService = new RequestService();
        fillingRequestService(requestService, "authorization", null, password.getText(), nickname.getText());
        try {
            Client.getChannel().channel().writeAndFlush(requestService).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearingBox(signInMenu);
        boxVisible("mainMenu");
    }

    public void registration(ActionEvent actionEvent) {
        RequestService requestService = new RequestService();
        try {
            Client.getChannel().channel().writeAndFlush(fillingRequestService(requestService,"registration", newLogin.getText(), newPassword.getText(), newNickname.getText())).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearingBox(regMenu);
        boxVisible("mainMenu");
    }

   public void clearingBox(HBox hBox){
       if (regMenu.equals(hBox)) {
           newLogin.clear();
           newPassword.clear();
           newNickname.clear();
       } else if (signInMenu.equals(hBox)){
           nickname.clear();
           password.clear();
       }

   }


}
