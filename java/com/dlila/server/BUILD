java_binary(
    name = "server_bin",
    runtime_deps = [":server"],
    main_class = "com.dlila.server.Server",
)

java_library(
    name = "server",
    srcs = glob(["**/*.java"]),
    #    resources = ["temperature.html"],
    deps = [
        "//third_party:guava",
    ],
)
