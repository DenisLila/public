#!/usr/bin/python

import sys

"""
Implements the data model of the sensor server. Reads from the receiving
arduino's serial output (see thermometer_receiver.ino) and prints to a file
(temp_data). TODO(dlila): consider printing this to a named pipe.

Run this using: (assuming the receiving arduino is connected to /dev/ttyACM0)
ino serial -b 57600 -p /dev/ttyACM0 | ./temperature_monitor.py
"""
while True:
  line = sys.stdin.readline()
  split = line.split(',')
  if len(split) > 0:
    with open('temp_data', 'w') as f:
      f.write(split[-1])

