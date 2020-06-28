package com.trade.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.trade.common.TranslatorData;
import com.trade.common.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducer {

    private String producerId;

    private RingBuffer<TranslatorDataWapper> ringBuffer;

    public MessageProducer(String producerId, RingBuffer<TranslatorDataWapper> ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }
    public void onData(TranslatorData data, ChannelHandlerContext ctx){
        long next = ringBuffer.next();
        try {
            TranslatorDataWapper wapper = ringBuffer.get(next);
            wapper.setData(data);
            wapper.setCtx(ctx);
        } finally {
            ringBuffer.publish(next);
        }
    }

}
