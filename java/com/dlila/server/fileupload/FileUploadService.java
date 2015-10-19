package com.dlila.server.fileupload;

import com.dlila.server.common.FileServingHandler;
import com.dlila.server.common.ServerBuilder;

public class FileUploadService {
  public static ServerBuilder newBuilder() {
    return new ServerBuilder("/")
        .add("", new FileServingHandler(
            "/home/dlila/repos/public/java/com/dlila/server/fileupload/fileupload.html"));
  }
}
