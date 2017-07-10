package com.zyz.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO Server
 * Created by zyz on 2016/11/15.
 */
public class MultiThreadEchoServer {

    private static Logger LOGGER = LoggerFactory.getLogger(NioThreadEchoService.class);

    private static ExecutorService tp = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static ServerSocket echoService = null;

    static class HandleMsg implements Runnable {
        Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            BufferedReader is = null;
            PrintWriter os = null;
            try {

                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintWriter(clientSocket.getOutputStream(), true);
                String inputLine = null;
                long startTimeMillis = System.currentTimeMillis();
                //当对输出流进行读取时，会阻塞直到读完
                while (null != (inputLine = is.readLine())) {
                    os.println(inputLine);
                }
                LOGGER.info("spend:{}", (System.currentTimeMillis() - startTimeMillis));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                    if (null != os) {
                        os.close();
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    LOGGER.error("close Sockect error　{}", e);
                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        Socket clientSocket = null;
        try {
            echoService = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("BIO Service init ...");
        try {
            while (true) {
                try {
                    clientSocket = echoService.accept();
                    LOGGER.info("{} connect!", clientSocket.getRemoteSocketAddress());
                    tp.execute(new HandleMsg(clientSocket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            LOGGER.info("BIO Service stop ...");
            if (null != echoService) {
                echoService.close();
            }
        }
    }

}
