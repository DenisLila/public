import sys
sys.path.append('/home/dlila/courses/crypto1')
import cryptoutils

# The solution to question 4. in1 and in2 are the left halves of the plaintext.
# out1 and out2 are the left halves of the ciphertext. The right halves of the
# plaintext are omitted, and they must be equal.
def testFeistel(in1, out1, in2, out2):
    # If the couple of samples came from the double feistel network, and the right
    # halves of the cipher texts are equal, then x1 == x2, because
    # L2 = F(k, R0) xor L0, and our R0's are equal.
    x1 = cryptoutils.barxor(out1, in1)
    x2 = cryptoutils.barxor(out2, in2)
    return (x1, x2)

# These are just the left halves of the outputs. The left halves of the inputs
# are 0^32 and 1^32, respectively. The right halves of the inputs don't matter.
# We only know that they are equal, and that is enough.
def q4():
    samples = [
("9f970f4e",    "6068f0b1"),
("5f67abaf",    "bbe033c0"),
("7c2822eb",    "325032a9"),
("7b50baab",    "ac343a22")
]

    samples = map(lambda (x, y): (x.decode('hex'), y.decode('hex')), samples)
    z = "00000000".decode('hex') # 32 zero bits, hex encoded
    o = "ffffffff".decode('hex')
    print map(lambda (x, y): testFeistel(z, x, o, y), samples)

