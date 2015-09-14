package com.dlila.tempserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.net.URL;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;
import com.google.common.primitives.Bytes;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

  public static void main(String[] argv) throws IOException, InterruptedException {
    URL htmlUrl = Server.class.getResource("temperature.html");
    Preconditions.checkState(htmlUrl != null);
    System.out.println(Resources.toString(htmlUrl, StandardCharsets.UTF_8));
    final byte[] html = Resources.toByteArray(htmlUrl);

    HttpServer server = HttpServer.create();
    server.bind(new InetSocketAddress(9999), 5);
    server.setExecutor(null);
    // TODO(dlila): this seems to handle /temp_data as well (which breaks our page).
    // TODO(dlila): finish this.
    server.createContext("/", new HttpHandler() {
      @Override
      public void handle(HttpExchange ex) throws IOException {
        InputStream is = ex.getRequestBody();
        byte[] input = ByteStreams.toByteArray(is);
        System.out.println(new String(input, StandardCharsets.UTF_8));

        ex.sendResponseHeaders(200, html.length);
        OutputStream os = ex.getResponseBody();
        os.write(html);
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
}

