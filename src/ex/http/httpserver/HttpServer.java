package ex.http.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket s = serverSocket.accept();
            System.out.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }

    }
}
//todo: описать попакетно handshake
//todo: что именно происходит при accept() и socket.close()