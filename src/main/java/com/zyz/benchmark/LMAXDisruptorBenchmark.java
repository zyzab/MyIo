package com.zyz.benchmark;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations=2)
@Measurement(iterations = 5)
public class LMAXDisruptorBenchmark {

    public EventHandler<LongEvent> handler;

    public Disruptor<LongEvent> disruptor;

    public RingBuffer<LongEvent> ringBuffer;

    public AtomicInteger eventCount;

    public ThreadFactory threadFactory;

    public static final Long LONGVAL = 0xba53ba1L;

    @Setup
    public void setup(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        new ThreadPoolExecutor(20, 40,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.AbortPolicy());

        int a = 121;
        int b = 100+40;

        executorService.submit(new Runnable() {
            @Override
            public void run() {

            }
        });

        new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                threadFactory);


        threadFactory = new DisruptorThreadFactory();
        executorService = Executors.newCachedThreadPool(threadFactory);



        disruptor = new Disruptor<LongEvent>(LongEvent.EVENT_FACTORY,1024,threadFactory, ProducerType.MULTI,new BusySpinWaitStrategy());
        handler = new MessageEventHandler();
        disruptor.handleEventsWith(handler);
        ringBuffer = disruptor.start();
    }

    @Benchmark
    public void addOneM() {
        long seq = ringBuffer.next();
        LongEvent longEvent = ringBuffer.get(seq);
        longEvent.setValue(LONGVAL);
        ringBuffer.publish(seq);
    }

    @TearDown
    public void tearDown() {
        disruptor.shutdown();
    }

    /**
     * 消息事件处理类，这里只打印消息
     */
    private static class MessageEventHandler implements EventHandler<LongEvent>{
        @Override
        public void onEvent(LongEvent messageEvent, long l, boolean b) throws Exception {
            System.out.println(messageEvent.getValue());
        }
    }

    private static final class DisruptorThreadFactory implements ThreadFactory{

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    }


    private static final class LongEvent
    {
        private Long value;

        public Long getValue()
        {
            return value;
        }

        public void setValue(final Long value)
        {
            this.value = value;
        }

        public final static EventFactory<LongEvent> EVENT_FACTORY = new EventFactory<LongEvent>()
        {
            @Override
            public LongEvent newInstance()
            {
                return new LongEvent();
            }
        };
    }
}
