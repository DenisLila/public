#!/usr/bin/python
import argparse, sys, time

"""
This program mimics the operation of the arduino receiver:
it emits temperatures measurements using the same formatting as the receiver.
Fake temperature measurements can be generated in whatever way.
"""

def delaying_iterator(iterator, interval=0.5):
  for thing in iterator:
    yield thing
    time.sleep(interval)

def emit(iterator):
  for line in iterator:
    sys.stdout.write(line)
    sys.stdout.flush()

def alternating(v1, v2):
  idx = 0
  values = [v1, v2]
  while True:
    yield "%d,%d,%d,%f\n" % (idx, idx, idx, values[idx % 2])
    idx = idx + 1

def format_temp(temp):
  return 

def main(args):
  if args.fake_input_file:
    with open(args.fake_input_file, 'r') as fake_input:
      emit(delaying_iterator(iter(fake_input)))
  if args.alternating:
    emit(delaying_iterator(alternating(10, 20)))

if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument('--fake_input_file', help='file from which to read fake inputs')
  parser.add_argument('--alternating', help='made up data that just alternates between two values', action='store_true')
  main(parser.parse_args())
