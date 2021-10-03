package ru.khorolskiy.stockroom.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// класс для подключения-отключения БД

public class DBConnection {
    public static final Logger LOGGER = LogManager.getLogger(DBConnection.class);
    private static final String Db_Driver = "com.mysql.cj.jdbc.Driver";
    private static final String Db_URL = String.format("jdbc:mysql://localhost/%s", DBCreate.getSchemaName());
    private static final String Db_username = "root";
    private static final String Db_password = "MICROLABtechnosp80";

    public static Statement getStmt() {
        return stmt;
    }

    private static Connection connection;
    private static Statement stmt;

    public void connect() { // Коннектимся к БД
        try {
            Class.forName(Db_Driver);
            connection = DriverManager.getConnection(Db_URL, Db_username, Db_password);
            stmt = connection.createStatement();
            if (!connection.isClosed()) {
                LOGGER.info("БД подключена успешно");
                LOGGER.info("URL БД: " + Db_URL);
            }
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Невозможно подключиться к БД");
            e.printStackTrace();
            throw new RuntimeException("Невозможно подключиться к БД");
        }
    }

    public static void disconnect() {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
