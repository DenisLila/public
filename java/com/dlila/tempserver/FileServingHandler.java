package com.dlila.tempserver;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class FileServingHandler implements HttpHandler {

  private final String path;

  public FileServingHandler(String path) {
    this.path = path;
  }

  // Reads the file on every request. Less efficient than the html-in-binary method, but simpler
  // and allows us to change the html without restarting the server (this can also be optimized
  // of course, by keeping the file in memory and listening on changes).
  @Override
  public void handle(HttpExchange ex) throws IOException {
    try {
      byte[] file = Files.toByteArray(new File(path));
      ex.sendResponseHeaders(200, file.length);
      ex.getResponseBody().write(file);
    } catch (IOException e) {
      System.out.println("exception: " + e);
      throw e;
    } finally {
      ex.close();
    }
  }
}

