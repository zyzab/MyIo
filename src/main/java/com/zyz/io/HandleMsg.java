package com.zyz.io;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Created by zyz on 2016/11/15.
 */
public class HandleMsg implements  Runnable{

    private  SelectionKey selectionKey;

    private ByteBuffer byteBuffer;

    public HandleMsg(SelectionKey selectionKey,ByteBuffer byteBuffer){
        this.selectionKey = selectionKey;
        this.byteBuffer = byteBuffer;
    }

    public void run() {
        EchoClientMsg echoClientMsg = (EchoClientMsg) selectionKey.attachment();
        echoClientMsg.enqueue(byteBuffer);
        selectionKey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
    }
}
