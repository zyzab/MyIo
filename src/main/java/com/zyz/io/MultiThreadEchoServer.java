package com.zyz.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zyz on 2016/11/15.
 */
public class MultiThreadEchoServer {

    private static ExecutorService tp = Executors.newCachedThreadPool();

    static class HandleMsg implements Runnable {
        Socket clientSocket;
        public HandleMsg(Socket clientSocket){
            this.clientSocket = clientSocket;
        }

        public void run(){
            BufferedReader is = null;
            PrintWriter os = null;
            try{
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintWriter(clientSocket.getOutputStream(),true);
                String inputLine = null;
                long startTimeMillis = System.currentTimeMillis();
                while (null!=(inputLine=is.readLine())){
                    os.println(inputLine);
                }
                System.out.println("spend:"+(System.currentTimeMillis()-startTimeMillis));
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try{
                    if(null!=is){
                        is.close();
                    }
                    if(null!=os){
                        os.close();
                    }
                    clientSocket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        ServerSocket echoService = null;
        Socket clientSocket = null;
        try{
            echoService = new ServerSocket(8000);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("echoService init ...");
        while (true){
            try{
                clientSocket = echoService.accept();
                System.out.println(clientSocket.getRemoteSocketAddress()+"connect!");
                tp.execute(new HandleMsg(clientSocket));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
