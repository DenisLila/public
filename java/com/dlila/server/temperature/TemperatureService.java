package com.dlila.server.temperature;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.dlila.server.common.FileServingHandler;
import com.dlila.server.common.ServerBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TemperatureService {
  
  private TemperatureService() {
    // static methods only. This class is similar to a guice module. I wonder how long I'll hold
    // out before guicifying/daggerifying this whole thing.
  }

  public static ServerBuilder newBuilder() {
    // TODO(dlila): feels wrong to get the server's data from System.in. Maybe the temperature
    // parser should upload it through some API that is only exposed on the local machine.
    // (use grpc).

    // TODO(dlila): a better way of handling this type of thing (background services in non-request
    // threads) might be to use guava's service manager in the concurrency package (or maybe the
    // event manager).
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

    // TODO(dlila): we're serving the initial page without the temperature data. This is
    // not necessary - we could just put it in there. But then we need some kind of templating.

    // TODO(dlila): package up the files in blaze runfiles. How the hell does one access those?
    return new ServerBuilder("/")
        .add("temp_page", new FileServingHandler(
            "/home/dlila/repos/public/java/com/dlila/server/temperature/temperature.html"))
        .add("temperature", new HttpHandler() {
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
  }
}
