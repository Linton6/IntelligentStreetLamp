package io.renren.modules.lamp.communication.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Date 2019/12/31 21:29
 * @
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {

    //每个信息入站都会调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("The server receive msg :" + buf.toString());
        ctx.write(buf);
    }

    //通知处理器最后的channelread()是当前批处理中的最后一条消息时调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    //读操作时捕获到异常时调用
    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
