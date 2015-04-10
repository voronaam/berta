package ca.vorona.berta.api;

import ca.vorona.berta.StaticLinker;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * The text-based API implementation
 * @author avorona
 */
public class AgentApiHandler extends ChannelHandlerAdapter {
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)
        ctx.channel().writeAndFlush("Welcome to Berta!\n");
        // TODO Print some information to identify the current JVM
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            String command = (String) msg;
            String response = null;
            switch (command) {
            case "":
                // Ignore empty lines
                break;
            case "BMAGIC":
                // A command to verify that we are still functional here
                response = "Magic is here!\n";
                break;
            default:
                // Anything else must be the test name 
                response = StaticLinker.getHandler().setTest(command);
                break;
            }
            if(response != null) {
                ctx.channel().writeAndFlush(response);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
