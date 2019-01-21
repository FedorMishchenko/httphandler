package ex.http.server5;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import sun.net.httpserver.DefaultHttpServerProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Sever {
    public static void main(String[] args) throws IOException {
        final int backlog = 64;
        final InetSocketAddress socketAddress = new InetSocketAddress(80);

        HttpServerProvider provider = DefaultHttpServerProvider.provider();
        HttpServer server = provider.createHttpServer(socketAddress,backlog);

        HttpContext baseContext = server.createContext("/");
        baseContext.setHandler(new PageHttpHandler("" +
                "<html>" +
                "   <body>" +
                "       <p><a href=\"/REF\">REF</a></p>" +
                "       <p><b href=\"/PAGE\">PAGE</b></p>" +
                "   </body>" +
                "</html>"
        ));
        HttpContext aContext = server.createContext("/REF");
        aContext.setHandler(new PageHttpHandler("" +
                "<html>" +
                "   <body>" +
                "       <p><b href=\"/PAGE\">PAGE</b></p>" +
                "   </body>" +
                "</html>"
        ));
        HttpContext bContext = server.createContext("/PAGE");
        bContext.setHandler(new PageHttpHandler("" +
                "<html>" +
                "   <body>" +
                "       <p><a href=\"/REF\">REF</a></p>" +
                "   </body>" +
                "</html>"
        ));
        server.start();
    }

    private static class PageHttpHandler implements HttpHandler {
        private String htmlPage;
        public PageHttpHandler(String htmlPage){
            this.htmlPage = htmlPage;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200,htmlPage.length());
            OutputStream os = exchange.getResponseBody();
            os.write(htmlPage.getBytes(StandardCharsets.US_ASCII));
            os.close();
        }
    }
}
