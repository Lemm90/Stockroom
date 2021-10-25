package ru.khorolskiy.stockroom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class JsonDecoderService extends MessageToMessageDecoder<byte[]> {
    ObjectMapper om = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        RequestService requestService = om.readValue(msg, RequestService.class);
        out.add(requestService);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("trouble JsonDecoderService");
        super.exceptionCaught(ctx, cause);
    }
}
