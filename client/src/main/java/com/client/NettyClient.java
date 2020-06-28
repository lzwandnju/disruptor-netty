package com.client;

import com.trade.codec.MarshallingCodeCFactory;
import com.trade.common.TranslatorData;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyClient {
    private final static String  ip = "127.0.0.1";
    private final static int port = 8765;
    private Channel channel;
    private EventLoopGroup workGroup;
    private ChannelFuture channelFuture;

    public NettyClient() {
        connect(ip,port);
    }

    private void connect(String ip,int port) {
        workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup);
        bootstrap.channel(NioSocketChannel.class)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel sc) throws Exception {
                                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                                sc.pipeline().addLast(new ClientHandle());
                            }
                 });

        try {
            channelFuture = bootstrap.connect(ip, port).sync();
            System.out.println("client connected");
            this.channel = channelFuture.channel();
            sendData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void sendData() {
        for (int i = 0; i < 10; i++) {
            TranslatorData request = new TranslatorData();
            request.setId("id:"+i);
            request.setName("name:"+i);
            request.setMessage("message:"+i);
            channel.writeAndFlush(request);
        }
    }

    public void close() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
        System.out.println("client shutdown");
    }






















}
