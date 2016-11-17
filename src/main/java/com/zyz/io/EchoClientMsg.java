package com.zyz.io;

import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Created by zyz on 2016/11/15.
 */
public class EchoClientMsg {

    private LinkedList<ByteBuffer> outq;

    EchoClientMsg(){
        outq = new LinkedList<ByteBuffer>();
    }

    public LinkedList<ByteBuffer> getOutq() {
        return outq;
    }

    public void setOutq(LinkedList<ByteBuffer> outq) {
        this.outq = outq;
    }

    public void enqueue(ByteBuffer byteBuffer){
        outq.addFirst(byteBuffer);
    }
}
