package com.zyz.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zyz on 2017/7/10.
 */
public class Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private static final ExecutorService service =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Selector selector;

    public Processor() throws IOException {
        this.selector = Selector.open();
    }

    public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
        SelectionKey readSelectionKey = socketChannel.register(this.selector, SelectionKey.OP_READ);
        EchoClientMsg echoClientMsg = new EchoClientMsg();
        readSelectionKey.attach(echoClientMsg);
    }

    public void wakeup() {
        this.selector.wakeup();
        start();
    }


    static class HandleMsg implements Runnable {
        Selector selector;

        public HandleMsg(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            while (true) {
                try {
                    selector.select();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isValid() && selectionKey.isReadable()) {
                        NioUtil.doRead(selectionKey);
                    }else if (selectionKey.isValid() && selectionKey.isWritable()) {
                        NioUtil.doWriter(selectionKey);
                    }
                }
            }
        }
    }


    public void start() {
        service.execute(new HandleMsg(this.selector));
    }


}
