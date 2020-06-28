package com.server;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.trade.disruptor.MessageConsumer4Server;
import com.trade.disruptor.MessageConsumers;
import com.trade.disruptor.RingBufferWorkPoolFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);

        MessageConsumers[] messageConsumers = new MessageConsumers[4];
        for (int i = 0; i < 4; i++) {
            MessageConsumer4Server consumer4Server = new MessageConsumer4Server("code:sessionId:003");
            messageConsumers[i] = consumer4Server;
        }

        RingBufferWorkPoolFactory.getInstance().initAndStart(
                ProducerType.MULTI,
                1024*1024,
                new YieldingWaitStrategy(),
                messageConsumers
        );




    }

}
