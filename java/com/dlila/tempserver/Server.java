package com.dlila.tempserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.io.LineReader;
import com.google.common.io.Resources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;


// TODO(dlila): working at this low a level is proving to be a waste of time. Start using
// a real web server framework. Things to try: google web toolkit, django, haskell.
// TODO(dlila): I'm a bit weary about running this stuff on my personal machine. Probably better to
// put the site on appengine, and just have a local privileged app that uploads the current
// temperature there.

// IMPORTANT: this will not work if run using bazel run :tempserver. Must be run from the generated
// script in bazel-bin.
public class Server {

  public static void main(String[] argv) throws IOException, InterruptedException {
    HttpServer server = HttpServer.create();
    server.bind(new InetSocketAddress(9999), 5);
    server.setExecutor(null);

    // TODO(dlila): lots of boilerplate in the HttpHandler definition. Make a request type.

    // TODO(dlila): we're serving the initial page without the temperature data. This is
    // not necessary - we could just put it in there.

    // TODO(dlila): package up the files in blaze runfiles. How the hell does one do that?
    HttpContext temperaturePage = server.createContext("/temp_page", new FileServingHandler(
        "/home/dlila/repos/public/java/com/dlila/tempserver/temperature.html"));

    // TODO(dlila): this is pretty awful. We're just initializing services and resources needed by
    // the handlers in the main() function. Something neater is needed.

    // TODO(dlila): feels wrong to get the server's data from System.in. Maybe the temperature
    // parser should upload it through some API that is only exposed on the local machine.
    // (use grpc).
    final TempReader tempReader = new TempReader(new BufferedInputStream(System.in));
    Thread tempReaderThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          tempReader.readTemps();
        } catch (IOException e) {
          System.out.println("temp reader thread shutting down");
          System.out.println(e);
        }
      }
    });
    tempReaderThread.start();

    HttpContext temperatureRequest = server.createContext("/temperature", new HttpHandler() {
      @Override
      public void handle(HttpExchange ex) throws IOException {
        try {
          double temp = tempReader.getCurrentTemp();
          byte[] response = Double.toString(temp).getBytes(StandardCharsets.UTF_8);
          ex.sendResponseHeaders(200, response.length);
          ex.getResponseBody().write(response);
        } finally {
          ex.close();
        }
      }
    });
    
    LoggingFilter filter = new LoggingFilter();
    temperaturePage.getFilters().add(filter);
    temperatureRequest.getFilters().add(filter);

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

