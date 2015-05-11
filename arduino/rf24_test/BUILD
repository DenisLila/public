load("/build-tools/ino", "ino_bin")

ino_bin(
    name = "rf24_test_rcv",
    src = ":rf24_test_rcv.ino",
    inolibs = [
        "//arduino/setup_printf",
        "//external:rf24",
    ]
)

ino_bin(
    name = "rf24_test_snd",
    src = ":rf24_test_snd.ino",
    inolibs = [
        "//arduino/setup_printf",
        "//external:rf24",
    ]
)
