package ru.khorolskiy.stockroom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class JsonDecoder extends MessageToMessageDecoder<byte[]> {
    ObjectMapper om = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
          try {
              RequestFile requestFile = om.readValue(msg, RequestFile.class);
              out.add(requestFile);
          } catch (Exception e) {
              JsonDecoderService jsonDecoderService = new JsonDecoderService();
              jsonDecoderService.decode(ctx, msg, out);
          }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("trouble JsonDecoder");
        super.exceptionCaught(ctx, cause);
    }
}
