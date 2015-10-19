package com.dlila.server.fileupload;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.dlila.server.common.FileServingHandler;
import com.dlila.server.common.ServerBuilder;
import com.dlila.server.common.Util;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FileUploadService {
  public static ServerBuilder newBuilder() {
    return new ServerBuilder("/")
        .add("fileupload", new FileServingHandler(
            "/home/dlila/repos/public/java/com/dlila/server/fileupload/fileupload.html"))
        .add("upload_action", new HttpHandler() {
          @Override
          public void handle(HttpExchange ex) throws IOException {
            try {
              log(ex);
              ex.sendResponseHeaders(200, 0);
              ex.getResponseBody().write(new byte[0]);
            } catch (IOException e) {
              System.out.println("exception: " + e);
              throw e;
            } finally {
              ex.close();
            }
          }
        });
  }

  // TODO(dlila): re-add the filters
  private static void log(HttpExchange ex) throws IOException {
    String method = ex.getRequestMethod();
    Headers headers = ex.getRequestHeaders();
    String body = Util.readAndClose(ex.getRequestBody());
    // TODO(dlila): use logger here.
    System.out.println(String.format("method: %s", method));
    System.out.println(String.format("headers: {", headers));
    for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
      System.out.println(String.format("    %s: %s", entry.getKey(), entry.getValue()));
    }
    System.out.println("}");
    System.out.println(String.format("body: %s",  body));
  }
}
