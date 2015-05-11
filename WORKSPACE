new_local_repository(
    name = "external-rf24",
    build_file = "third_party/rf24-BUILD",
    path = "/home/dlila/repos/public/third_party/RF24",
)

bind(
    name = "rf24",
    actual = "@external-rf24//:rf24",
)

new_local_repository(
    name = "local-libs",
    build_file = "third_party/local-libs-BUILD",
    path = "/home/dlila/repos/local-only-libs",
)

bind(
    name = "onewire",
    actual = "@local-libs//:onewire",
)

bind(
    name = "dallastemp",
    actual = "@local-libs//:dallastemp",
)

