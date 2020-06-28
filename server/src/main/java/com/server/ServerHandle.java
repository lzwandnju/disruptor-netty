package com.server;

import com.trade.common.TranslatorData;
import com.trade.disruptor.MessageProducer;
import com.trade.disruptor.RingBufferWorkPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData request = (TranslatorData)msg;
        RingBufferWorkPoolFactory factory = RingBufferWorkPoolFactory.getInstance();
        String producerId = "sessionID:001";
        MessageProducer producer = factory.getMessage(producerId);
        producer.onData(request,ctx);
    }
}
