package com.zyz.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 * Created by zyz on 2017/7/10.
 */
public class NioUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(NioUtil.class);

    public static void doAccept(SelectionKey selectionKey,Selector selector) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel;
        try {
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            SelectionKey readSelectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            EchoClientMsg echoClientMsg = new EchoClientMsg();
            readSelectionKey.attach(echoClientMsg);

            InetAddress clientAddress = socketChannel.socket().getInetAddress();
            LOGGER.info("Accepted connection from {}",clientAddress.getHostAddress());
        } catch (Exception e) {
            LOGGER.error("Failed to accept new client {}",e);
        }
    }

    public static void doRead(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;
        try {
            len = socketChannel.read(bb);
            if (len < 0) {
                disconnect(selectionKey);
                return;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to read to client. {}",e);
            disconnect(selectionKey);
        }
        bb.flip();
        EchoClientMsg echoClientMsg = (EchoClientMsg) selectionKey.attachment();
        echoClientMsg.enqueue(bb);
        selectionKey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
    }


    public static void doWriter(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        EchoClientMsg echoClientMsg = (EchoClientMsg) selectionKey.attachment();
        LinkedList<ByteBuffer> outq = echoClientMsg.getOutq();
        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(selectionKey);
                return;
            }
            if (bb.remaining() == 0) {
                outq.removeLast();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to write to client {}" ,e);
            disconnect(selectionKey);
        }
        if (outq.size() == 0) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    public static void disconnect(SelectionKey selectionKey) {
        if (null != selectionKey) {
            try {
                selectionKey.channel().close();
            } catch (IOException e) {
                LOGGER.error("close channel error {}",e);
            }
        }
    }


}
