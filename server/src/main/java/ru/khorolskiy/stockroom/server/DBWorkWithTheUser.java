package ru.khorolskiy.stockroom.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

// Класс для запросов к БД в соответствии с командами от клиента
// В клиенте еще не реализовал функцию регистрации/авторизации
// Что бы различать клиентов в Реквесте в поле userName добавляется имя пользователя ПК

public class DBWorkWithTheUser extends SimpleChannelInboundHandler <RequestService> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestService requestService) throws Exception {
        ResponseService responseService = new ResponseService();
        int userID;
        switch (requestService.getCommand()){
            case ("registration"):
                   userID = registration(requestService.getUsername(), requestService.getPassword(), requestService.getNickname());
                   responseService.setCommand("userID: " + userID);
                System.out.println(responseService.getCommand());
                   ctx.channel().writeAndFlush(responseService).sync();
                   break;
            case ("authorization"):
                userID = authorization(requestService.getNickname(), requestService.getPassword());
                responseService.setCommand("userID: " + userID);
                ctx.channel().writeAndFlush(responseService).sync();
                break;
        }
    }
    public static final Logger LOGGER = LogManager.getLogger(DBWorkWithTheUser.class);

    public int authorization(String nickname, String password) throws SQLException {

        String query = String.format("select id from user where nickname = '%s' and password = '%s';", nickname, password);
        LOGGER.info("Команда в DB: " + query);
        try (ResultSet rs = DBConnection.getStmt().executeQuery(query)) {
            while (rs.next())
                return rs.getInt("id");
        }
        return 0;
    }


    public int registration(String username, String password, String nickname) throws SQLException {
        String query = String.format("insert into user (username, password, nickname) values ('%s', '%s', '%s');", username, password, nickname);
        LOGGER.info("Команда в DB: " + query);
        try {
            DBConnection.getStmt().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorization(nickname, password);
    }

    public boolean nicknameCheckForUniqueness(String nickname) throws SQLException {
        String query = String.format("select * from user where nickname = '%s';", nickname);
        LOGGER.info("Команда в DB: " + query);
        try (ResultSet rs = DBConnection.getStmt().executeQuery(query)) {
            while (rs.next())
                return false;
        }
        return true;
    }
}
