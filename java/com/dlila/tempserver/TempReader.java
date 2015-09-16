package com.dlila.tempserver;

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.io.LineReader;

class TempReader {

  private final InputStream is;
  
  // TODO(dlila): how do we export this to the outside world cleanly?
  // (don't forget concurrency).
  private double currentTemp = Double.NaN;

  // TODO(dlila): don't forget to make this buffered.
  public TempReader(InputStream is) {
    this.is = is;
  }

  public void readTemps() throws IOException {
    LineReader lineReader = new LineReader(new InputStreamReader(is));
    String line = null;
    do {
      line = lineReader.readLine();
      String[] split = line.split(',');
      currentTemp = split[split.length - 1];
    } while (line != null);
  }
}

