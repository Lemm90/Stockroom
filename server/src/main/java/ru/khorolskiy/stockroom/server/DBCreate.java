package ru.khorolskiy.stockroom.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCreate {
    public static final Logger LOGGER = LogManager.getLogger(DBCreate.class);
    private static final String Db_Driver = "com.mysql.cj.jdbc.Driver";
    private static final String Db_URL = "jdbc:mysql://localhost";
    private static final String Db_username = "root";
    private static final String Db_password = "MICROLABtechnosp80";
    private static String schemaName = "DBStockRoom";

    public static String getSchemaName() {
        return schemaName;
    }

    private static String tableNameUserRoom = "userRoom";
    private static String tableNameUser = "user";

    public static Statement getStmt() {
        return stmt;
    }

    private static Connection connection;
    private static Statement stmt;

    public void create() {
        try {
            Class.forName(Db_Driver);
            connection = DriverManager.getConnection(Db_URL, Db_username, Db_password);
            stmt = connection.createStatement();
            String createSchema = String.format("CREATE SCHEMA `%s`;", schemaName);
            String createTableUserRoom = String.format("CREATE TABLE `%s`.`%s` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `username` VARCHAR(150) NOT NULL,\n" +
                    "  `fileName` VARCHAR(150) NOT NULL,\n" +
                    "  `extension` VARCHAR(45) NULL DEFAULT NULL,\n" +
                    "  `size` INT NOT NULL,\n" +
                    "  `array` LONGTEXT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`));", schemaName, tableNameUserRoom);
            String createTableUser = String.format("CREATE TABLE `%s`.`%s` (\n" +
                    "        `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "        `username` VARCHAR(150) NOT NULL,\n" +
                    "        `password` VARCHAR(45) NOT NULL,\n" +
                    "        `nickname` VARCHAR(150) NOT NULL,\n" +
                    "        PRIMARY KEY (`id`));", schemaName, tableNameUser);
            stmt.executeUpdate(createSchema); // создаем БД
            stmt.executeUpdate(createTableUserRoom); // создаем необходимую таблицу в БД
            stmt.executeUpdate(createTableUser); // создаем необходимую таблицу в БД
            LOGGER.info(String.format("База данных '%s' создана", schemaName));
            DBConnection dbConnection = new DBConnection();
            dbConnection.connect(); // коннектимся к БД
        } catch (SQLException | ClassNotFoundException throwables) { // Если прилетел Exception значит таблица уже создана.
            DBConnection dbConnection = new DBConnection(); // Значит сразу коннектимся к БД
            dbConnection.connect();
        }
    }
}

