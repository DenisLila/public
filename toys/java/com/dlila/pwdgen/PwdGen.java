import java.util.HashSet;
import java.util.Set;
import java.security.SecureRandom;

public class PwdGen {
  private static final int LEN = 16; 
  private static final String CHARS;

  static {
    String chars = "abcdefghijklmnopqrstuvwxyz";
    chars += chars.toUpperCase();
    chars += "1234567890!@#$%^&*()=+-[]{}<>,.`~/?|'";
    CHARS = chars;

    // verify that there are no duplicates in CHARS
    Set<Character> charSet = new HashSet<>();
    for (Character c : CHARS.toCharArray()) {
      charSet.add(c);
    }   
    if (charSet.size() != CHARS.length()) {
      throw new IllegalStateException();
    }   
  }

  public static void main(String[] argv) {
    SecureRandom random = new SecureRandom();
    StringBuilder b = new StringBuilder(LEN);
    for (int i = 0; i < LEN; i++) {
      b.append(CHARS.charAt(random.nextInt(CHARS.length())));
    }   
    System.out.println(b.toString());
  }
}
