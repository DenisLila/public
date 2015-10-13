package com.dlila.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.dlila.server.common.ServerBuilder;
import com.dlila.server.temperature.TemperatureService;
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
    // TODO(dlila): Flags. what to do about flags.
    server.bind(new InetSocketAddress(9999), 5);
    server.setExecutor(null);

    ServerBuilder serverBuilder = new ServerBuilder("/")
        .add("temperature", TemperatureService.newBuilder());

    serverBuilder.addToServer(server);

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
