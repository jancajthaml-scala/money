package com.github.jancajthaml.money;

class Serialization {

  public static Object[] fromString(String x) {
    int total = x.length();
    boolean signum = x.charAt(0) == '-';
    int leftOffset = signum ? 1 : 0;
    int rightOffset = total - 1;

    leftscan: while (true) {
      char c = x.charAt(leftOffset++);
      if (c == '.') {
        leftOffset--;
        rightscan: while (true) {
          c = x.charAt(rightOffset--);
          if (rightOffset == -1 || c != '0' || c == '.') {
            rightOffset++;
            break leftscan;
          }
        }
      } else if (leftOffset == total || c != '0') {
        leftOffset--;
        break leftscan;
      }
    }

    if (x.charAt(leftOffset) == '.') {
      String underlying = x.substring(leftOffset + 1, rightOffset + 1);
      //System.out.println("negative exponent underlying -> "+ underlying);
      int exponent = 0;
      int len = underlying.length();
      int[] digits = null;
      if (len > 0) {
        while (exponent < len && underlying.charAt(exponent++) == '0');
        int remain = len - exponent + 1;
        int i = 0;
        digits = new int[remain];
        while (i < remain) {
          digits[i] = (int)(underlying.charAt((len-1) -i++) - 48);
        }
      } else {
        digits = new int[0];
      }

      return new Object[]{
        signum,
        -exponent,
        digits,
        underlying
      };
    } else {
      // TODO/FIXME check if underlying contains decimal here...
      String underlying = x.substring(leftOffset, total);
      int len = underlying.length() - 1;
      int exponent = 0;
      while (exponent < len && underlying.charAt(len-++exponent) == '0');
      int remain = underlying.length() - exponent;

      int i = 0;
      int[] digits = new int[remain];
      while (i < remain) {
        char c = underlying.charAt(i);
        if (c == '.') {
          exponent = i - 1;
          i++;
        } else {
          digits[i++] = (int)(c - 48);
        }
      }

      return new Object[]{
        signum,
        exponent,
        digits,
        underlying
      };
    }
  }
}