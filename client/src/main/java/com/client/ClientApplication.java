package com.client;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.trade.disruptor.MessageConsumer4Client;
import com.trade.disruptor.MessageConsumers;
import com.trade.disruptor.RingBufferWorkPoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
        MessageConsumers[]messageConsumers = new MessageConsumers[4];
        for (int i = 0; i < 4; i++) {
            MessageConsumer4Client client = new MessageConsumer4Client("client:code:session:0001");
            messageConsumers[i]=client;
        }
        RingBufferWorkPoolFactory.getInstance().initAndStart(
                ProducerType.MULTI,
                1024*1024,
                new YieldingWaitStrategy(),
                messageConsumers
        );
        new NettyClient();
    }
}
