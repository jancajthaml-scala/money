package com.github.jancajthaml.money;

class Serialization {

  public static Object[] fromString(String x) {
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

    if (x.charAt(leftOffset) == 0x2e) {
      String underlying = x.substring(leftOffset + 1, rightOffset + 1);
      int exponent = 0;
      int len = underlying.length();
      int[] digits = null;
      if (len > 0) {
        while (exponent < len && underlying.charAt(exponent++) == 0x30);
        int remain = len - exponent + 1;
        int i = 0;
        digits = new int[remain];
        while (i < remain) {
          digits[i] = (int)(underlying.charAt((len-1) -i++) - 48);
        }
      } else {
        digits = new int[0];
      }

      return new Object[]{ signum, -exponent, digits, underlying };
    } else {
      String underlying = x.substring(leftOffset, total);
      int len = underlying.length() - 1;
      int exponent = 0;
      while (exponent < len && underlying.charAt(len-++exponent) == 0x30);
      int remain = underlying.length() - exponent;
      int i = 0;
      int[] digits = new int[remain];
      while (i < remain) {
        char c = underlying.charAt(i);
        if (c < 0x30) {
          exponent = i - 1;
          remain--;
          int[] swap = new int[remain];
          System.arraycopy(digits, 0, swap, 0, i);
          digits = swap;
          swap = null;
          i++;
          while (i < remain) {
            digits[i] = (int)(underlying.charAt(++i) - 48);
          }
          break;
        } else {
          digits[i++] = (int)(c - 48);
        }
      }

      return new Object[]{ signum, exponent, digits, underlying };
    }
  }
}