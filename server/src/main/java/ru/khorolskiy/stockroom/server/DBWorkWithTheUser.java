package ru.khorolskiy.stockroom.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

// Класс для запросов к БД в соответствии с командами от клиента
// В клиенте еще не реализовал функцию регистрации/авторизации
// Что бы различать клиентов в Реквесте в поле userName добавляется имя пользователя ПК

public class DBWorkWithTheUser {
    public static final Logger LOGGER = LogManager.getLogger(DBWorkWithTheUser.class);

    public int authorization(String login, String password) throws SQLException {

        String query = String.format("select id from user where login = '%s' and password = '%s';", login, password);
        LOGGER.info("Команда в DB: " + query);
        try (ResultSet rs = DBConnection.getStmt().executeQuery(query)) {
            while (rs.next())
                return rs.getInt("id");
        }
        return 0;
    }


    public int registration(String firstname, String lastname, String password, String login) throws SQLException {
        String query = String.format("insert into user (firstname, lastname, password, login) values ('%s', '%s', '%s', '%s');", firstname, lastname, password, login);
        LOGGER.info("Команда в DB: " + query);
        try {
            DBConnection.getStmt().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorization(login, password);
    }

    public boolean checkForUniqueness(String login) throws SQLException {
        String query = String.format("select * from user where login = '%s';", login);
        LOGGER.info("Команда в DB: " + query);
        try (ResultSet rs = DBConnection.getStmt().executeQuery(query)) {
            while (rs.next())
                return true;
        }
        return false;
    }

}
