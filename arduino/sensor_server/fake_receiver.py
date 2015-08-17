#!/usr/bin/python
import argparse, sys, time

"""
This program mimics the operation of the arduino receiver:
it emits temperatures measurements using the same formatting as the receiver.
Fake temperature measurements can be generated in whatever way.
"""

def delaying_iterator(iterator, interval=1.5):
  for thing in iterator:
    yield thing
    time.sleep(interval)

def emit(iterator):
  for line in iterator:
    sys.stdout.write(line)
    sys.stdout.flush()

def main(args):
  if not args.fake_input_file:
    raise ValueError("need fake input file")
  with open(args.fake_input_file, 'r') as fake_input:
    emit(delaying_iterator(iter(fake_input)))

if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument('--fake_input_file', help='file from which to read fake inputs')
  main(parser.parse_args())
