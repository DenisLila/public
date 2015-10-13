package com.dlila.server.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

class LoggingFilter extends Filter {

  @Override
  public String description() {
    return "logging";
  }

  @Override
  public void doFilter(HttpExchange ex, Filter.Chain chain) throws IOException {
    try {
      String method = ex.getRequestMethod();
      Headers headers = ex.getRequestHeaders();
      String body = Util.readAndClose(ex.getRequestBody());
      // TODO(dlila): use logger here.
      System.out.println(String.format("method: %s", method));
      System.out.println(String.format("headers: {", headers));
      for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
        System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
      }
      System.out.println("}");
      System.out.println(String.format("body: %s",  body));
    } finally {
      chain.doFilter(ex);
    }
  }
}

