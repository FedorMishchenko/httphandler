package ex.http.server2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class Server2 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        while (true) {
            System.out.println("wait for TCP connection...");
            Socket socket = serverSocket.accept();
            System.out.println("accept one");
            try (InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream()) {
                byte[] request = HttpUtils.readRequestFully(in);
                System.out.println("--------------------------");
                System.out.print(new String(request, US_ASCII));
                System.out.println("--------------------------");
                byte[] response = new Date().toString().getBytes(US_ASCII);
                out.write(response);
            }
            finally {
                socket.close();
            }
        }
    }
}
