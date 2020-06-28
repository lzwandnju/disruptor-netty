package com.trade.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.trade.common.TranslatorData;
import com.trade.common.TranslatorDataWapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class RingBufferWorkPoolFactory {

    static class SingletonHolder{
        final static RingBufferWorkPoolFactory instance = new RingBufferWorkPoolFactory();
    }

    private RingBufferWorkPoolFactory(){}

    public static RingBufferWorkPoolFactory getInstance(){
        return SingletonHolder.instance;
    }

    private static Map<String,MessageProducer> producers = new ConcurrentHashMap<>();


    private static Map<String,MessageConsumers> consumers = new ConcurrentHashMap<>();

    private RingBuffer<TranslatorDataWapper> ringBuffer;

    private WorkerPool<TranslatorDataWapper> workPool;

    private SequenceBarrier sequenceBarrier;

    public void initAndStart(ProducerType producerType, int bufferSize, WaitStrategy waitStrategy,MessageConsumers[] messageConsumers){
        ringBuffer = RingBuffer.create(producerType,
                new TranslatorDataWapperEventFactory(),
                bufferSize,
                waitStrategy
                );
        sequenceBarrier = ringBuffer.newBarrier();
        workPool = new WorkerPool<TranslatorDataWapper>(ringBuffer,
                sequenceBarrier,
                new EventExceptionHandle(),
               messageConsumers);
        for (MessageConsumers consumer: messageConsumers) {
            consumers.put(consumer.getConsumserId(),consumer);
        }
        ringBuffer.addGatingSequences(workPool.getWorkerSequences());
        workPool.start(Executors.newFixedThreadPool(4));
    }

    public MessageProducer getMessage(String producerId){
        MessageProducer producer = producers.get(producerId);
        if(null==producer){
            producer = new MessageProducer(producerId, ringBuffer);
            producers.put(producerId,producer);
        }
        return producer;
    }

    static class TranslatorDataWapperEventFactory implements EventFactory<TranslatorDataWapper> {
        @Override
        public TranslatorDataWapper newInstance() {
            return new TranslatorDataWapper();
        }
    }

    static class EventExceptionHandle implements ExceptionHandler<TranslatorDataWapper> {
        @Override
        public void handleEventException(Throwable throwable, long l, TranslatorDataWapper translatorDataWapper) {

        }

        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }
    }
}
