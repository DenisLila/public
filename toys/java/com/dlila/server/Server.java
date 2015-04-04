package com.dlila.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

  private static final byte[] HTML =
      "<!DOCTYPE html><html><head>head</head><body><p>hello adam</p></html>"
      .getBytes(StandardCharsets.UTF_8);
  private static final String HTTP =
      "HTTP/1.1 200 OK\n" +
      "Accept-Ranges: bytes\n" +
      "Content-Length: " + HTML.length + "\n" +
      "Content-Type: text/html\n" +
      "Date: Mon, 09 Mar 2015 05:35:40 GMT\n\n";
  static {
    System.out.println("html len: " + HTML.length);
  }

  public static void main1(String[] argv) throws IOException, InterruptedException {
    HttpServer server = HttpServer.create();
    server.bind(new InetSocketAddress(9999), 5);
    server.createContext("/", new HttpHandler() {
      @Override
      public void handle(HttpExchange ex) throws IOException {
        InputStream is = ex.getRequestBody();
        byte[] input = ByteStreams.toByteArray(is);
        System.out.println(new String(input, StandardCharsets.UTF_8));

        ex.sendResponseHeaders(200, HTML.length);
        OutputStream os = ex.getResponseBody();
        os.write(HTML);
        os.flush();
        os.close();
      }
    });
    server.start();
    try {
      Thread.sleep(10000);
    } finally {
      server.stop(0);
    }
  }

  public static void main(String[] argv) throws UnknownHostException, IOException {
    ServerSocket socket = new ServerSocket(5901);
    Socket s = socket.accept();

    OutputStream os = s.getOutputStream();
    InputStream is = s.getInputStream();

//    System.out.println("reading");
//    byte[] input = ByteStreams.toByteArray(is);
//    System.out.println(new String(input, StandardCharsets.UTF_8));

    System.out.println("writing");
    os.write(Bytes.concat(HTTP.getBytes(StandardCharsets.UTF_8), HTML));
    os.flush();

    System.out.println("closing socket. reached end");
    os.close();
    is.close();
    s.close();
    socket.close();
  }
}
