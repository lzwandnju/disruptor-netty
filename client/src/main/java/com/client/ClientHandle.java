package com.client;

import com.trade.common.TranslatorData;
import com.trade.disruptor.MessageProducer;
import com.trade.disruptor.RingBufferWorkPoolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData reponse = (TranslatorData)msg;
        String producerId = "code:sessionID:001";
        MessageProducer producer = RingBufferWorkPoolFactory.getInstance().getMessage(producerId);
        producer.onData(reponse,ctx);
    }
}
