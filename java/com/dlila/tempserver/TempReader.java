package com.dlila.tempserver;

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.io.LineReader;

class TempReader {

  private final InputStream is;
  private final Splitter inputLineSplitter;
  
  // TODO(dlila): how do we export this to the outside world cleanly?
  // (don't forget concurrency).
  private double currentTemp = Double.NaN;

  // TODO(dlila): don't forget to make this buffered.
  public TempReader(InputStream is) {
    this.is = is;
    this.inputLineSplitter = Splitter.on(',');
  }

  public void readTemps() throws IOException {
    LineReader lineReader = new LineReader(new InputStreamReader(is));
    String line = null;
    do {
      List<String> split = inputLineSplitter.splitToList(lineReader.readLine());
      try {
        currentTemp = Double.parseDouble(split.get(split.size() - 1));
      } catch (NumberFormatException e) {
        throw new IOException(e);
      }
    } while (line != null);
  }
}

