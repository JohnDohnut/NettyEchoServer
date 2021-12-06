package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import server.handler.NettyEchoServerHandler;


import java.net.InetSocketAddress;

public class NettyEchoServer {
    private static final int port = 8899;

    public static void main (String[] args){
        new NettyEchoServer().start();
    }
    public void start(){
        final NettyEchoServerHandler serverHandler = new NettyEchoServerHandler();
        io.netty.channel.EventLoopGroup bossGroup = new NioEventLoopGroup();
        io.netty.channel.EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{

            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        public void initChannel (SocketChannel ch) {

                            ch.pipeline()
                                    .addLast(serverHandler);

                        }

                    });

            ChannelFuture f = sb.bind().sync();
            f.channel().closeFuture().sync();

        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        finally{

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }
}
