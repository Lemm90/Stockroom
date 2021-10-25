package ru.khorolskiy.stockroom.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class JsonDecoderService extends MessageToMessageDecoder<byte[]> {
    ObjectMapper om = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        ResponseService responseService = om.readValue(msg, ResponseService.class);
        out.add(responseService);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("trouble JsonDecoderService");
        super.exceptionCaught(ctx, cause);
    }
}
