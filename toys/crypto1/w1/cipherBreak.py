import cryptoutils

""" messages encrypted with the same "one" time pad. Need to decrypt these """
messages = [x.decode('hex') for x in ["315c4eeaa8b5f8aaf9174145bf43e1784b8fa00dc71d885a804e5ee9fa40b16349c146fb778cdf2d3aff021dfff5b403b510d0d0455468aeb98622b137dae857553ccd8883a7bc37520e06e515d22c954eba5025b8cc57ee59418ce7dc6bc41556bdb36bbca3e8774301fbcaa3b83b220809560987815f65286764703de0f3d524400a19b159610b11ef3e",
"234c02ecbbfbafa3ed18510abd11fa724fcda2018a1a8342cf064bbde548b12b07df44ba7191d9606ef4081ffde5ad46a5069d9f7f543bedb9c861bf29c7e205132eda9382b0bc2c5c4b45f919cf3a9f1cb74151f6d551f4480c82b2cb24cc5b028aa76eb7b4ab24171ab3cdadb8356f",
"32510ba9a7b2bba9b8005d43a304b5714cc0bb0c8a34884dd91304b8ad40b62b07df44ba6e9d8a2368e51d04e0e7b207b70b9b8261112bacb6c866a232dfe257527dc29398f5f3251a0d47e503c66e935de81230b59b7afb5f41afa8d661cb",
"32510ba9aab2a8a4fd06414fb517b5605cc0aa0dc91a8908c2064ba8ad5ea06a029056f47a8ad3306ef5021eafe1ac01a81197847a5c68a1b78769a37bc8f4575432c198ccb4ef63590256e305cd3a9544ee4160ead45aef520489e7da7d835402bca670bda8eb775200b8dabbba246b130f040d8ec6447e2c767f3d30ed81ea2e4c1404e1315a1010e7229be6636aaa",
"3f561ba9adb4b6ebec54424ba317b564418fac0dd35f8c08d31a1fe9e24fe56808c213f17c81d9607cee021dafe1e001b21ade877a5e68bea88d61b93ac5ee0d562e8e9582f5ef375f0a4ae20ed86e935de81230b59b73fb4302cd95d770c65b40aaa065f2a5e33a5a0bb5dcaba43722130f042f8ec85b7c2070",
"32510bfbacfbb9befd54415da243e1695ecabd58c519cd4bd2061bbde24eb76a19d84aba34d8de287be84d07e7e9a30ee714979c7e1123a8bd9822a33ecaf512472e8e8f8db3f9635c1949e640c621854eba0d79eccf52ff111284b4cc61d11902aebc66f2b2e436434eacc0aba938220b084800c2ca4e693522643573b2c4ce35050b0cf774201f0fe52ac9f26d71b6cf61a711cc229f77ace7aa88a2f19983122b11be87a59c355d25f8e4",
"32510bfbacfbb9befd54415da243e1695ecabd58c519cd4bd90f1fa6ea5ba47b01c909ba7696cf606ef40c04afe1ac0aa8148dd066592ded9f8774b529c7ea125d298e8883f5e9305f4b44f915cb2bd05af51373fd9b4af511039fa2d96f83414aaaf261bda2e97b170fb5cce2a53e675c154c0d9681596934777e2275b381ce2e40582afe67650b13e72287ff2270abcf73bb028932836fbdecfecee0a3b894473c1bbeb6b4913a536ce4f9b13f1efff71ea313c8661dd9a4ce",
"315c4eeaa8b5f8bffd11155ea506b56041c6a00c8a08854dd21a4bbde54ce56801d943ba708b8a3574f40c00fff9e00fa1439fd0654327a3bfc860b92f89ee04132ecb9298f5fd2d5e4b45e40ecc3b9d59e9417df7c95bba410e9aa2ca24c5474da2f276baa3ac325918b2daada43d6712150441c2e04f6565517f317da9d3",
"271946f9bbb2aeadec111841a81abc300ecaa01bd8069d5cc91005e9fe4aad6e04d513e96d99de2569bc5e50eeeca709b50a8a987f4264edb6896fb537d0a716132ddc938fb0f836480e06ed0fcd6e9759f40462f9cf57f4564186a2c1778f1543efa270bda5e933421cbe88a4a52222190f471e9bd15f652b653b7071aec59a2705081ffe72651d08f822c9ed6d76e48b63ab15d0208573a7eef027",
"466d06ece998b7a2fb1d464fed2ced7641ddaa3cc31c9941cf110abbf409ed39598005b3399ccfafb61d0315fca0a314be138a9f32503bedac8067f03adbf3575c3b8edc9ba7f537530541ab0f9f3cd04ff50d66f1d559ba520e89a2cb2a83",
"32510ba9babebbbefd001547a810e67149caee11d945cd7fc81a05e9f85aac650e9052ba6a8cd8257bf14d13e6f0a803b54fde9e77472dbff89d71b57bddef121336cb85ccb8f3315f4b52e301d16e9f52f904"]]

def highest_pairs(m):
    """ m is a map with numeric values. Return a list of keys that have the largest value. """
    maxv = max(m.values())
    return filter(lambda k: m[k] == maxv, m.keys())

def matrix_from_vectors(op, v):
    """
    Given vector v, build matrix
    [[op(v1, v1), ..., op(v1, vn)],
                  ...
     [op(v2, v1), ..., op(vn, vn)]].
     Note that if op is commutative, this is redundant: the matrix will be equal to its transpose.
     The matrix is represented as a list of lists.
    """
    return [[op(vi, vj) for vj in v] for vi in v]

def matrix_half_iter(m):
    # Iterate upper right of a matrix. Exclude diagonal.
    for i in xrange(len(m)):
        for j in xrange(i + 1, len(m[i])):
            yield ((i, j), m[i][j])

def insert_into_histogram_dict(hist_dict, value):
    if hist_dict.has_key(value):
        hist_dict[value] += 1
    else:
        hist_dict[value] = 1

def decrypt_with_fuzzy_key(fuzzy_key, message):
    # zip will discard extra items from the longer message.
    zipped = zip(fuzzy_key, message)
    def decrypt_byte(fuzzy_key_byte, message_byte):
        return map(lambda x: ord(message_byte) ^ x, fuzzy_key_byte)
    return [decrypt_byte(fuzzy_key_byte, message_byte) for (fuzzy_key_byte, message_byte) in zipped]

def main():
    mlength = max(map(len, messages))
    print "length of longest message " + str(mlength)

    # the key-cracking idea here is that English text contains *a lot* of spaces. It turns out that
    # if you xor an ascii space and all other ascii letters, the result is >= 65. So, go through
    # all xored plaintexts, and if any bytes are >= 65, we record that as a potential space.

    # We xor all pairs of messages. That gives us the xored plaintexts.
    plaintext_xors = matrix_from_vectors(cryptoutils.barxor, messages)
    # For each byte position k in the key, this keeps a list of ciphertext bytes at position k that
    # are possibly spaces (see above). 
    keyData = {x : {} for x in range(mlength)}
    for ((i, j), plaini_xor_plainj) in matrix_half_iter(plaintext_xors):
        for k in xrange(len(plaini_xor_plainj)):
            if plaini_xor_plainj[k] >= 65:
                # Now, as the comment above explained, we are confident that character k of either
                # plaintext i is space, or character k of plaintext j is space. We don't know which
                # one yet, so record character k of *ciphertext* i and j. If a ciphertext byte is
                # already in the dictionary for position k, we just increment a counter.
                insert_into_histogram_dict(keyData[k], messages[i][k])
                insert_into_histogram_dict(keyData[k], messages[j][k])

    # at this point, for each key position k, keyData contains counts of ciphertext bytes. If our
    # assumption that each inserted pair contained one space is correct, the most common byte will
    # be the space.
    key = [[] for x in range(len(keyData))]
    for k in xrange(mlength):
        possible_space_counts = keyData[k]
        print "for key position {0}, here are the space counts {1}".format(k, possible_space_counts)
        if len(possible_space_counts) == 0:
            continue
        # most of the time this will be a list of exactly one, but it's possible to have more than
        # one "most common" byte (for example, if there was only one pair with space, we inserted
        # the space cipher text and the other character's ciphertext with counts 1).
        space_ciphertexts = highest_pairs(possible_space_counts)
        key[k] = map(lambda x: ord(x) ^ ord(' '), space_ciphertexts)

    # at this point, key[k] is a list that contains our guess of the the kth byte of the key. If we
    # have no guess, it's empty. We may have more than one guess.
    print "key: ", key
    fuzzy_decryption = decrypt_with_fuzzy_key(key, messages[0])
    def pretty_print_fuzzy_decryption(dec):
        return "".join(map(lambda x: chr(x[0]) if len(x) == 1 else '_', dec))
    print pretty_print_fuzzy_decryption(fuzzy_decryption)

if __name__ == "__main__":
# TODO(dlila): translate this to haskell at some point (after fixing it)
    main()
#  c = range(65, 91) + range(97, 123)
#  spaceXors = list(set(map(lambda (x, y): x ^ y, zip([32]*len(c), c))))
#  print spaceXors, len(spaceXors)
#  cxors = map(lambda ci: map(lambda cj: ci ^ cj, c), c)
#  cxors = set([x for l in cxors for x in l])
#  print cxors, len(cxors)

