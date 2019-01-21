package ex.http.server;

import java.io.*;
import java.net.Socket;

public class SocketProcessor implements Runnable {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    public SocketProcessor(Socket s) throws IOException {
        this.socket = s;
        this.in = s.getInputStream();
        this.out = s.getOutputStream();
    }

    @Override
    public void run() {
        try {
            readInputHeaders();
            writeResponse("<html><body><h1>HELLO WORLD</h1></body></html>");
        } catch (IOException ignore) {
            /*NOP*/
        } finally {
            try {
                socket.close();
            } catch (IOException ignore) {
                /*NOP*/
            }
        }
        System.out.println("Client processing finished");
    }

    private void writeResponse(String s) throws IOException {
        String response = response(s);
        String result = response + s;
        out.write(result.getBytes());
        out.flush();
    }

    private String response(String s) {
        return "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
    }

    private void readInputHeaders() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        while(true) {
            String s = br.readLine();
            if(s == null || s.trim().length() == 0) {
                break;
            }
        }
    }
}
