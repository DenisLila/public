package com.dlila.server.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

class Util {
  private Util() {}

  public static String readAndClose(InputStream is) throws IOException {
    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    try {
      return CharStreams.toString(reader);
    } finally {
      Closeables.closeQuietly(reader);
    }
  }

  public static class Pair<F, S> {
    private final F f;
    private final S s;

    public Pair(F f, S s) {
      this.f = f;
      this.s = s;
    }

    public static <F, S> Pair<F, S> of(F f, S s) {
      return new Pair<F, S>(f, s);
    }

    public F f() {
      return f;
    }

    public S s() {
      return s;
    }
  }
}
