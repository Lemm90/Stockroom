package ru.khorolskiy.stockroom.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;

public class WorkingWithAFile extends SimpleChannelInboundHandler<ResponseFile> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseFile responseFile) throws Exception {
        // Прием Респонза от сервера.
        // Создание файла
        File file = new File("копия_" + responseFile.getFilename() + responseFile.getExtension());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //Напролнение файла массивом байтов из поля array Респонза.
        fileOutputStream.write(responseFile.getArray());
        fileOutputStream.close();
    }

}
