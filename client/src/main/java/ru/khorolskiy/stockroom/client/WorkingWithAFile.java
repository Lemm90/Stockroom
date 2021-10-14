package ru.khorolskiy.stockroom.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;

public class WorkingWithAFile extends SimpleChannelInboundHandler<Response> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
        // Прием Респонза от сервера.
        // Создание файла
        File file = new File("копия_" + response.getFilename() + response.getExtension());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //Напролнение файла массивом байтов из поля array Респонза.
        fileOutputStream.write(response.getArray());
        fileOutputStream.close();
    }

}
