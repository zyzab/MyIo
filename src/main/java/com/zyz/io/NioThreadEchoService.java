package com.zyz.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zyz on 2016/11/15.
 */
public class NioThreadEchoService {

    private Selector selector;

    private ExecutorService tp = Executors.newCachedThreadPool();

    private static Map<Socket,Long> timeStartMap = new HashMap<Socket,Long>();

    private void startServer() throws Exception {
        //selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        //InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(),8000);
        ssc.socket().bind(new InetSocketAddress(8000));
        selector = Selector.open();
        ssc.register(selector,SelectionKey.OP_ACCEPT);
        while (true){
            int select = selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isAcceptable()){
                    this.doAccept(selectionKey);
                }else if(selectionKey.isValid()&&selectionKey.isReadable()){
                    Long startTime = timeStartMap.get(((SocketChannel)(selectionKey.channel())).socket());
                    if(null==startTime){
                        timeStartMap.put(((SocketChannel)(selectionKey.channel())).socket(),System.currentTimeMillis());
                    }
                    this.doRead(selectionKey);
                }else if(selectionKey.isValid()&&selectionKey.isWritable()){
                    this.doWriter(selectionKey);
                    System.out.println("spend: "+(System.currentTimeMillis()-timeStartMap.get(((SocketChannel)(selectionKey.channel())).socket()))+"ms");
                }

            }
        }

    }

    private void doAccept(SelectionKey selectionKey){
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel;
        try{
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            SelectionKey readSelectionKey = socketChannel.register(selector,SelectionKey.OP_READ);
            EchoClientMsg echoClientMsg = new EchoClientMsg();
            readSelectionKey.attach(echoClientMsg);

            InetAddress clientAddress = socketChannel.socket().getInetAddress();
            System.out.println("Accepted connection from "+clientAddress.getHostAddress());
        }catch (Exception e){
            System.out.println("Failed to accept new client");
            e.printStackTrace();
        }
    }

    private void doRead(SelectionKey selectionKey){
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;
        try{
            len = socketChannel.read(bb);
            if(len<0){
                this.disconnect(selectionKey);
                return;
            }
        }catch (Exception e){
            System.out.println("Failed to read to client.");
            this.disconnect(selectionKey);
            e.printStackTrace();
        }
        bb.flip();
        tp.execute(new HandleMsg(selectionKey,bb));
    }

    private void doWriter(SelectionKey selectionKey){
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        EchoClientMsg echoClientMsg = (EchoClientMsg) selectionKey.attachment();
        LinkedList<ByteBuffer> outq = echoClientMsg.getOutq();
        ByteBuffer bb = outq.getLast();
        try{
            int len = channel.write(bb);
            if(len==-1){
                this.disconnect(selectionKey);
                return;
            }
            if(bb.remaining()==0){
                outq.removeLast();
            }
        }catch (Exception e){
            System.out.println("Failed to write to client.");
            this.disconnect(selectionKey);
            e.printStackTrace();
        }
        if(outq.size()==0){
            //selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private void disconnect(SelectionKey selectionKey){
        if (null!=selectionKey){
            try {
                selectionKey.channel().close();
            } catch (IOException e) {
                e.printStackTrace();
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
