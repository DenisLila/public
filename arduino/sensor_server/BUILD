load("/build-tools/ino", "ino_bin", "ino_lib")

ino_lib(
    name = "common",
    srcs = [ ":common.h" ],
)

ino_bin(
    name = "therm_client",
    src = ":thermometer_client.ino",
    inolibs = [
        "//arduino/setup_printf",
        "//external:rf24",
        "//external:onewire",
        "//external:dallastemp",
    ]
)

ino_bin(
    name = "therm_receiver",
    src = ":thermometer_receiver.ino",
    inolibs = [
        "//arduino/setup_printf",
        "//external:rf24",
        "//external:onewire",
        "//external:dallastemp",
    ]
)
