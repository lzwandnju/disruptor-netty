package com.trade.disruptor;

import com.trade.common.TranslatorData;
import com.trade.common.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageConsumer4Server extends MessageConsumers{

    public MessageConsumer4Server(String consumserId) {
        super(consumserId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        ;
        ChannelHandlerContext ctx = event.getCtx();
        TranslatorData request = event.getData();
        System.out.println("Server side receive info: "+request);
        TranslatorData data = new TranslatorData();
        data.setId("response: "+request.getId());
        data.setName("response: "+request.getName());
        data.setMessage("response: "+request.getMessage());
        ctx.writeAndFlush(data);
    }
}
