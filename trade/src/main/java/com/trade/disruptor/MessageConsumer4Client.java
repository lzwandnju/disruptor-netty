package com.trade.disruptor;

import com.trade.common.TranslatorData;
import com.trade.common.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class MessageConsumer4Client extends MessageConsumers {

    public MessageConsumer4Client(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        ChannelHandlerContext ctx = event.getCtx();
        TranslatorData request = event.getData();
        try {
            System.out.println("Client 端接收到的消息是: "+request);
        } finally {
            ReferenceCountUtil.release(event);
        }
    }
}
