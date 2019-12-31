//package io.renren.modules.lamp.communication.netty;
//
//
///**
// * @Date 2019/12/31 21:27
// * @
// */
//
//class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx){
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8));
//    }
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//        System.out.println("Client received: "+in.toString(CharsetUtil.UTF_8));
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx,
//                                Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//
//
//}
//
