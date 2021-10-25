package ru.khorolskiy.stockroom.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    public static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static ChannelFuture channel;

    public static ChannelFuture getChannel() {
        return channel;
    }

    public void start(int port) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1); // Создание основного канала для приема подключения клиента
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(); // Создание рабочего канала для обмена данными между клиентом и сервером

        // создание/подключение сервера к БД
        DBCreate dbCreate = new DBCreate();
        dbCreate.create();

        //конфигурация сервера.
        try {
            ServerBootstrap server = new ServerBootstrap();
            server
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4),
                                    new LengthFieldPrepender(4),
                                    new ByteArrayDecoder(),
                                    new ByteArrayEncoder(),
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new DBWorkWithTheUser(),
                                    new DBWorkingWithAFile());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = server.bind(port).sync();
            channel = channelFuture.sync();
            LOGGER.info("Сервер запущен! Порт: " + port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            LOGGER.error("Сервер отключен! Порт: " + port + " свободен");
        }

    }


}


