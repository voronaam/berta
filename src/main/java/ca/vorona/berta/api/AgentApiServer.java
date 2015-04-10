package ca.vorona.berta.api;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class AgentApiServer {
    
    private int port;
    
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public AgentApiServer(int port) {
        this.port = port;
    }
    
    public void start() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .childHandler(new ChannelInitializer<SocketChannel>() {
             @Override
             public void initChannel(SocketChannel ch) throws Exception {
                 // Decoders
                 ch.pipeline().addLast("frameDecoder", new LineBasedFrameDecoder(80));
                 ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                 // Handler
                 ch.pipeline().addLast(new AgentApiHandler());
                 // Encoder
                 ch.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
             }
         })
         .option(ChannelOption.SO_BACKLOG, 128)
         .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        b.bind(port).sync();
    }
    
    public void stop() {
        if(workerGroup != null) {
            workerGroup.shutdownGracefully().syncUninterruptibly();
        }
        if(bossGroup != null) {
            bossGroup.shutdownGracefully().syncUninterruptibly();
        }
    }

}
