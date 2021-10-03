module ru.khorolskiy.stockroom.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires io.netty.all;
    requires com.fasterxml.jackson.databind;


    opens ru.khorolskiy.stockroom.client to javafx.fxml;
    exports ru.khorolskiy.stockroom.client;
}