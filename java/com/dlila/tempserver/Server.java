package com.dlila.tempserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.net.URL;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.primitives.Bytes;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Filter;

public class Server {

  private static String readAndClose(InputStream is) throws IOException {
    // hardcoding utf8? TODO(dlila): this is wrong.
    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    try {
      return CharStreams.toString(reader);
    } finally {
      Closeables.closeQuietly(reader);
    }
  }

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
    Filter loggingFilter = new Filter() {
      @Override
      public String description() {
        return "logging";
      }

      @Override
      public void doFilter(HttpExchange ex, Filter.Chain chain) throws IOException {
        try {
          String method = ex.getRequestMethod();
          Headers headers = ex.getRequestHeaders();
          String body = readAndClose(ex.getRequestBody());
          // TODO(dlila): use logger here.
          System.out.println(String.format("method: %s", method));
          System.out.println(String.format("headers: %s", headers));
          System.out.println(String.format("body: %s",  body));
        } finally {
          chain.doFilter(ex);
        }
      }
    };

    HttpContext temperaturePage = server.createContext("/temp_page", new HttpHandler() {
      @Override
      public void handle(HttpExchange ex) throws IOException {
        try {
          Headers headers = ex.getResponseHeaders();
          ex.sendResponseHeaders(200, html.length);
          OutputStream os = ex.getResponseBody();
          os.write(html);
        } finally {
          ex.close();
        }
      }
    });

    HttpContext temperatureRequest = server.createContext("/temperature", new HttpHandler() {
      @Override
      public void handle(HttpExchange ex) throws IOException {
        try {
          byte[] response = "25.3".getBytes(StandardCharsets.UTF_8);
          ex.sendResponseHeaders(200, response.length);
          ex.getResponseBody().write(response);
        } finally {
          ex.close();
        }
      }
    });
    
    temperaturePage.getFilters().add(loggingFilter);
    temperatureRequest.getFilters().add(loggingFilter);

    server.start();
    try {
      while (true) {
        Thread.sleep(10000);
      }
    } finally {
      server.stop(0);
    }
  }
}

