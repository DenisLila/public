# Makes a LED blink on the raspberry pi. Pin 2.

import RPi.GPIO as gio
import time

def runBlinker():
  for i in range(10):
    for j in range(3):
      gio.output(2, 1)
      time.sleep(1.0 / 6)
      gio.output(2, 0)
      time.sleep(1.0 / 6)
    time.sleep(0.8)

def setupAndRun(f):
  try:
    gio.setmode(gio.BCM)
    gio.setup(2, gio.OUT)

    f()
  finally:
    gio.cleanup()
