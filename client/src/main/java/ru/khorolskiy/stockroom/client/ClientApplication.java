package ru.khorolskiy.stockroom.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("/window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setTitle("Stockroom");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
