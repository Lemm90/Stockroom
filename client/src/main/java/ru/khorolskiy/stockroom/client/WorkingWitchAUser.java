package ru.khorolskiy.stockroom.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class WorkingWitchAUser extends SimpleChannelInboundHandler <ResponseService> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseService responseService) throws Exception {
        if(responseService.getCommand().startsWith("userID: ")){
            String str = responseService.getCommand();
            String[] temp = str.split("\\s+");
            UserInfo.setUserID(Integer.parseInt(temp[1]));
            System.out.println(UserInfo.getUserID());
        }
    }
}
