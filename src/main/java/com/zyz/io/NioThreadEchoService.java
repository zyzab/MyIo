package com.zyz.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NIO Server
 * Created by zyz on 2016/11/15.
 */
public class NioThreadEchoService {

    Logger LOGGER = LoggerFactory.getLogger(NioThreadEchoService.class);

    private Selector selector;

    private ExecutorService tp = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static Map<Socket, Long> timeStartMap = new HashMap<Socket, Long>();

    private void startServer() throws Exception {
        LOGGER.info("NioThreadEchoService start ...");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(8000));
        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    NioUtil.doAccept(selectionKey,selector);
                } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                    Long startTime = timeStartMap.get(((SocketChannel) (selectionKey.channel())).socket());
                    if (null == startTime) {
                        timeStartMap.put(((SocketChannel) (selectionKey.channel())).socket(), System.currentTimeMillis());
                    }
                    NioUtil.doRead(selectionKey);
                } else if (selectionKey.isValid() && selectionKey.isWritable()) {
                    NioUtil.doWriter(selectionKey);
                    LOGGER.info("spend: =>{} ms", (System.currentTimeMillis() - timeStartMap.get(((SocketChannel) (selectionKey.channel())).socket())));
                }

            }
        }

    }

    public static void main(String[] args) {
        NioThreadEchoService nioThreadEchoService = new NioThreadEchoService();
        try {
            nioThreadEchoService.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
