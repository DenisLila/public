package com.dlila.tempserver;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Closeables;
import com.google.common.io.CharStreams;

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
}

