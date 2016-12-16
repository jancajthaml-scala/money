package com.github.jancajthaml.money;

class Serialization {

  public static String toString(boolean s, char[] d, int e) {
    char[] swap = new char[d.length + 1];
    System.arraycopy(d, 0, swap, 0, e);
    System.arraycopy(d, e-1, swap, e, d.length - e + 1);
    swap[e] = '.';
    return s ? ('-' + new String(swap, 0, swap.length)) : new String(swap, 0, swap.length);
  }

  public static Object[] fromString(String x) {
    if (x == "0") {
      return new Object[]{ false, 1, new char[]{ '0', '0' }, "0" };
    }

    char[] characters = x.toCharArray();
    boolean sign = false;

    if (characters[0] == '-') {
      sign = true;
      char[] swap = new char[characters.length - 1];
      System.arraycopy(characters, 1, swap, 0, swap.length);
      characters = swap;
      swap = null;
    }

    int len = characters.length;
    int i = -1;

    while (++i < len) {
      if (characters[i] == '.') {
        int left = i;
        int right = characters.length - i - 1;
        char[] swap = new char[left + right];
        System.arraycopy(characters, 0, swap, 0, left);
        System.arraycopy(characters, left + 1, swap, left, right);
        return new Object[]{ sign, i, swap, toString(sign, swap, i) };
      }
    }

    char[] swap = new char[characters.length + 1];
    swap[swap.length - 1] = '0';
    System.arraycopy(characters, 0, swap, 0, characters.length);

    return new Object[]{ sign, swap.length - 1, swap, toString(sign, swap, swap.length - 1) };
  }

  /*
  public static String toString(boolean s, char[] d, int e) {
    String r = "";

    if (d.length == 0) {
      r = "0";
    } else if (e < 0) {
      int i = 0;
      r += "0.";
      while (e < -1) {
        r += '0';
        e++;
      }
      while (i < d.length) {
        r += d[i++];
      }
    } else {
      int i = 0;
      if (e < d.length - 1) {
        while (i <= e) {
          r += d[i++];
        }
        r += '.';
        while (i < d.length) {
          r += d[i++];
        }
      } else {
        int f = e - d.length;
        int len = d.length;
        while (i < len) {
          r += d[i++];
        }
        len += f;
        while (i++ < len) {
          r += '0';
        }
      }
    }
    return s ? ('-' + r) : r;
  }

  public static Object[] fromString(String x) {
    // TODO/FIXME wrong exponent

    // TODO/FIXME toCharArray and System.arraycopy try performance 
    int total = x.length();
    boolean signum = x.charAt(0) == '-';
    int leftOffset = signum ? 1 : 0;
    int rightOffset = total - 1;

    leftscan: while (true) {
      char c = x.charAt(leftOffset++);
      if (c == 0x2e) {
        leftOffset--;
        while (true) {
          c = x.charAt(rightOffset--);
          if (rightOffset == -1 || c > 0x30 || c == 0x2e) {
            rightOffset++;
            break leftscan;
          }
        }
      } else if (leftOffset == total || c > 0x30) {
        leftOffset--;
        break leftscan;
      }
    }

    String underlying = null;
    int exponent = 0;
    int len = 0;
    char[] digits = null;

    if (x.charAt(leftOffset) == 0x2e) {
      //System.out.println("hit-0");
      underlying = x.substring(leftOffset - 1, rightOffset + 1);
      digits = underlying.toCharArray();
      len = digits.length - 1;
      if (len > 0) {
        while (exponent <= len && digits[exponent++] == 0x30);
        int remain = len - exponent;
        exponent--;
        char[] swap = new char[remain];
        System.arraycopy(digits, digits.length - remain, swap, 0, remain);
        digits = swap;
        swap = null;
      } else {
        digits = new char[0];
      }
      exponent = -exponent - 1;
    } else {
      //System.out.println("hit-1");
      underlying = x.substring(leftOffset, total);
      len = underlying.length() - 1;
      char sc;
      int em = 0;
      while (exponent <= len && ((sc = underlying.charAt(len-exponent++)) == 0x30 || (sc < 0x30 && (em = exponent) > 0)));
      exponent -= (em > 0 ? (em + 1) : 1);
      
      int remain = underlying.length() - (em > 0 ? (exponent + em) : exponent);
      int i = 0;
      char[] digits2 = underlying.toCharArray();
      digits = new char[remain];
      exponent++;

      while (i < remain) {
        char c = digits2[i];
        if (c < 0x30) {
          exponent = i - 1;
          digits[i] = '0';
          System.arraycopy(digits2, i + 1, digits, i, remain - i - 1);
          digits = java.util.Arrays.copyOf(digits, digits.length - 1);
          break;
        } else {
          digits[i++] = c;
        }
      }
    }

    return new Object[]{ signum, exponent, digits, signum ? ('-' + underlying) : underlying };
  }
  */

}