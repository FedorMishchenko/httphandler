package ex.http.httpserver;

import com.sun.net.httpserver.*;
import com.sun.net.httpserver.spi.HttpServerProvider;
import org.jetbrains.annotations.NotNull;
import sun.net.httpserver.DefaultHttpServerProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class Server {
    private static String protocol;
    private static String requestMethod;
    private static URI requestURI;
    private static Headers requestHeaders;

    public static void main(String[] args) throws IOException {
        final int backlog = 64;
        final InetSocketAddress socketAddress = new InetSocketAddress(80);

        HttpServerProvider provider = DefaultHttpServerProvider.provider();
        HttpServer server = provider.createHttpServer(socketAddress,backlog);
        server.setExecutor(Executors.newCachedThreadPool());

        HttpContext baseContext = server.createContext("/");
        baseContext.setHandler(exchange -> {
            protocol = exchange.getProtocol();
            requestMethod = exchange.getRequestMethod();
            requestURI = exchange.getRequestURI();
            requestHeaders = exchange.getRequestHeaders();

            String htmlPage
                    = createResponsePage(protocol,requestMethod,requestURI, requestHeaders);
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.add("HEADER-","1");
            responseHeaders.add("HEADER-","2");
            responseHeaders.add("HEADER-","3");
            responseHeaders.add("HEADER-","4");
            exchange.sendResponseHeaders(200,htmlPage.length());

            OutputStream os = exchange.getResponseBody();
            os.write(htmlPage.getBytes(StandardCharsets.US_ASCII));
            os.close();
        });
        server.start();
    }
    private static String createResponsePage(String protocol,
                                             String requestMethod,
                                             URI requestURI,
                                             @NotNull Headers requestHeaders) {
        Server.protocol = protocol;
        Server.requestMethod = requestMethod;
        Server.requestURI = requestURI;
        Server.requestHeaders = requestHeaders;
        String htmlPage = response(protocol, requestMethod, requestURI);
        htmlPage = getHeaders(requestHeaders, htmlPage);
        return htmlPage;
    }

    private static String getHeaders(@NotNull Headers requestHeaders, String htmlPage) {
        for (Map.Entry<String, List<String>>header: requestHeaders.entrySet()){
            String key = header.getKey();
            List<String> values = header.getValue();
            htmlPage += "<br>" + key + ": ";
            for (String value: values){
                htmlPage += value + ", ";
            }
            htmlPage += "</br>";
        }
        return htmlPage;
    }

    @NotNull
    private static String response(String protocol, String requestMethod, URI requestURI) {
        String htmlPage = "<html><body>";
        htmlPage += "<br>requestMethod: " + requestMethod + "</br>";
        htmlPage += "<br>requestURI: " + requestURI + "</br>";
        htmlPage += "<br>protocol: " + protocol + "</br>";
        return htmlPage;
    }
}
