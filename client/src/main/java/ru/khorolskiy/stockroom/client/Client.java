package ru.khorolskiy.stockroom.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;

public class Client {

    private static ChannelFuture channel;

    public static ChannelFuture getChannel() {
        return channel;
    }

    //Конфигурация клиента и подключение клиента к серверу
    public void start(String initHost, int initPort) throws InterruptedException, IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4),
                                    new LengthFieldPrepender(4),
                                    new ByteArrayDecoder(),
                                    new ByteArrayEncoder(),
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new WorkingWitchAUser(),
                                    new WorkingWithAFile());
                        }
                    });

            ChannelFuture channelFuture = client.connect(initHost, initPort).sync();
            System.out.printf("connect good \nhost: %s || port: %s%n", initHost, initPort);
            channel = channelFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
