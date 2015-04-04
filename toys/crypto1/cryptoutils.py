def barxor(a, b):     # xor two strings of different lengths
  """XOR the given byte strings. If they are not of equal lengths, right pad the shorter
  one with zeroes.
  """
  if len(a) > len(b):
    return [ord(x) ^ ord(y) for (x, y) in zip(a[:len(b)], b)]
  else:
    return [ord(x) ^ ord(y) for (x, y) in zip(a, b[:len(a)])]

