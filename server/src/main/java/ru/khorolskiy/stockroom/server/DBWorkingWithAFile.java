package ru.khorolskiy.stockroom.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class DBWorkingWithAFile extends SimpleChannelInboundHandler<Request> {
    public static final Logger LOGGER = LogManager.getLogger(DBWorkingWithAFile.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) {
        switch (msg.getCommand()) { // Принимаем Реквест. В зависимости от поля command посылаем команды в БД.
            case ("create"):
                // Проверка на уникальность файла в БД. Если уникальный, то записываем в БД. Если нет, то обновляем имеющуюся запись
                if (fileUniquenessCheck(msg.getUsername(), msg.getFilename(), msg.getExtension())) {
                    downloadFileIntoBD(msg.getUsername(), msg.getFilename(), msg.getExtension(), msg.getSize(), bytesEncoder(msg.getArray()));
                } else {
                    updateFileIntoDB(bytesEncoder(msg.getArray()), msg.getSize(), msg.getUsername(), msg.getFilename());
                }
                break;
            case ("delete"):
                // если не уникальный, то удалить запись из БД
                //todo надо подумать. Может надо в БД сделать флаг у файла. При удалении файла, снимать флаг с файла
                // todo и не отображать файлы без флага. Таким образом можно восстанавливать файлы.
                if (!fileUniquenessCheck(msg.getUsername(), msg.getFilename(), msg.getExtension())) {
                    deletingFileFromDB(msg.getUsername(), msg.getFilename());
                }
                break;
            case ("download"):
                // Скачивание файла. Создаем объект Респонз. Наполняем его данными из БД и отправляем клиенту.
                Response response = new Response();
                fillingResponse(response, msg);
                ctx.channel().writeAndFlush(response);
        }
    }

    public boolean fileUniquenessCheck(String username, String fileName, String extension) {
        String queryCheck = String.format("select id from userRoom where username = '%s' and fileName = '%s' and extension = '%s';", username, fileName, extension);
        try (
                ResultSet rs = DBConnection.getStmt().executeQuery(queryCheck)) {
            while (rs.next())
                return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public void downloadFileIntoBD(String username, String filename, String extension, Integer size, String array) {
        String queryCreate = String.format("insert into userRoom (username, fileName, extension, size, array) " +
                "values ('%s', '%s', '%s', '%d', '%s');", username, filename, extension, size, array);
        try {
            DBConnection.getStmt().executeUpdate(queryCreate);
            LOGGER.info(String.format("Загрузка файла '%s' в БД прошла успешно", filename));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String bytesEncoder(byte[] bytes) {
        // преобразуем массив байтов из реквеста к стандарту base64
        String base64Encoded = Base64.getEncoder().encodeToString(bytes);
        return base64Encoded;
    }

    public byte[] bytesDecoder(String string) {
        // преобразуем массив стандарт base64  в массив байтов
        byte[] base64Decoded = Base64.getDecoder().decode(string);
        return base64Decoded;
    }

    public void deletingFileFromDB(String username, String filename) {
        String queryDelete = String.format("delete from userRoom where username = '%s' and fileName = '%s';", username, filename);
        try {
            DBConnection.getStmt().executeUpdate(queryDelete);
            LOGGER.info(String.format("Удаление файла '%s' из БД прошло успешно", filename));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFileIntoDB(String array, Integer size, String username, String filename) {
        String queryUpdate = String.format("update userRoom set array = '%s', size = '%d' where username = '%s' and fileName = '%s';", array, size, username, filename);
        try {
            DBConnection.getStmt().executeUpdate(queryUpdate);
            LOGGER.info(String.format("Обновление файла '%s' в БД прошло успешно", filename));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int setSizeFromDB(String username, String fileName, String extension) {
        String query = String.format("select size from userRoom where username = '%s' and fileName = '%s' and extension = '%s';", username, fileName, extension);
        try (ResultSet rs = DBConnection.getStmt().executeQuery(query)) {
            while (rs.next())
                return rs.getInt("size");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public byte[] setArrayFromDB(String username, String fileName, String extension) {
        String query = String.format("select array from userRoom where username = '%s' and fileName = '%s' and extension = '%s';", username, fileName, extension);
        try (ResultSet rs = DBConnection.getStmt().executeQuery(query)) {
            while (rs.next()) {
                String array = rs.getString("array"); // вычитываем строку из БД
                return bytesDecoder(array); // преобразуем строку к массиву
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void fillingResponse(Response response, Request msg) {
        // Заполняем Респонз данными для отправки их клиенту
        response.setFilename(msg.getFilename());
        response.setExtension(msg.getExtension());
        response.setSize(setSizeFromDB(msg.getUsername(), msg.getFilename(), msg.getExtension()));
        response.setArray(setArrayFromDB(msg.getUsername(), msg.getFilename(), msg.getExtension()));
    }
}

