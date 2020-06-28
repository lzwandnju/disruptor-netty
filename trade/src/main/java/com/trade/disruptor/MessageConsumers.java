package com.trade.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.trade.common.TranslatorDataWapper;

public abstract class MessageConsumers implements WorkHandler<TranslatorDataWapper> {


    protected String consumserId;

    public MessageConsumers(String consumserId){
        this.consumserId = consumserId;
    }

    public String getConsumserId() {
        return consumserId;
    }

    public void setConsumserId(String consumserId) {
        this.consumserId = consumserId;
    }

}
