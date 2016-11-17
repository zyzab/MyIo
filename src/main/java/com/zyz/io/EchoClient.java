package com.zyz.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zyz on 2016/11/15.
 */
public class EchoClient {

    private static ExecutorService tp = Executors.newCachedThreadPool();

     class SimpleClient implements Runnable{

         private String clientName;

         public SimpleClient(String clientName){
            this.clientName = clientName;
         }

        public void run() {
            Socket client = null;
            PrintWriter writer = null;
            BufferedReader reader = null;
            try {
                client = new Socket();
                client.connect(new InetSocketAddress("localhost",8000));
                writer = new PrintWriter(client.getOutputStream());
                writer.print("Hello!");
                this.sleep(1000L);
                writer.print(" I ");
                this.sleep(1000L);
                writer.print(" am ");
                this.sleep(1000L);
                writer.print(this.clientName);
                writer.println();
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("from service: "+ reader.readLine());
            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(null!=writer){
                    writer.close();
                }
                if(null!=reader){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(null!=client){
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

         private void sleep(Long millis){
             try {
                 Thread.sleep(millis);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }
    public  void buildSimpleClient(){
        for (int i=0;i<1;i++){
            tp.execute(new SimpleClient("client"+i));
        }
    }



    public static void main(String[] args) {
        EchoClient client = new EchoClient();
        client.buildSimpleClient();
    }
}
