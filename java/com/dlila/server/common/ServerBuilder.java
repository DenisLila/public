package com.dlila.server.common;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import com.sun.istack.internal.Nullable;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

// TODO(dlila): support for filters.
public class ServerBuilder {

  private final String prefix;
  // Either this is null or children is empty.
  @Nullable
  private final HttpHandler handler;

  private final Map<String, ServerBuilder> children = new HashMap<>(0);

  public ServerBuilder(String prefix) {
    this(prefix, null);
  }

  private ServerBuilder(String prefix, HttpHandler handler) {
    this.prefix = checkNotNull(prefix);
    this.handler = handler;
  }

  public ServerBuilder add(String urlChunk, ServerBuilder child) {
    checkNotNull(urlChunk, "null urlchunk");
    checkNotNull(child, "null handler");
    ServerBuilder previous = children.put(urlChunk, child);
    if (previous != null) {
      throw new IllegalArgumentException("child already exists for url chunk: " + urlChunk);
    }
    return this;
  }

  public ServerBuilder add(String urlChunk, HttpHandler handler) {
    checkNotNull(urlChunk, "null urlchunk");
    checkNotNull(handler, "null handler");
    ServerBuilder previous = children.put(urlChunk, new ServerBuilder(urlChunk, handler));
    if (previous != null) {
      throw new IllegalArgumentException("handler already exists for url chunk: " + urlChunk);
    }
    return this;
  }

  private Map<String, HttpHandler> build(
      StringBuilder currentPath, Map<String, HttpHandler> map) {
    currentPath.append(prefix);
    if (handler != null) {
      checkNotNull(children);
      String finalUrl = currentPath.toString();
      HttpHandler previous = map.put(finalUrl, handler);
      if (previous != null) {
        throw new IllegalStateException(String.format("duplicate url handler for: %s", finalUrl));
      }
    } else {
      for (Map.Entry<String, ServerBuilder> entry : children.entrySet()) {
        String chunk = entry.getKey();
        currentPath.append(chunk);
        entry.getValue().build(currentPath, map);
        pop(currentPath, chunk.length());
      }
    }
    pop(currentPath, prefix.length());
    return map;
  }

  public Map<String, HttpHandler> addToServer(HttpServer server) {
    Map<String, HttpHandler> map = build(new StringBuilder(), new HashMap<String, HttpHandler>());
    for (Map.Entry<String, HttpHandler> entry : map.entrySet()) {
      server.createContext(entry.getKey(), entry.getValue());
    }
    return map;
  }

  private static void pop(StringBuilder builder, int num) {
    int len = builder.length();
    builder.delete(len - num, len);
  }
}
