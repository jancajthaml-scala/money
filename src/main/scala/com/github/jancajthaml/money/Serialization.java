package com.github.jancajthaml.money;

class Serialization {

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
        System.out.println(e + " / " + java.util.Arrays.toString(d));
        while (i <= e) {
          r += d[i++];
        }
        r += '.';
        while (i < d.length) {
          r += d[i++];
        }
      } else {
        int len = d.length;
        while (i < len) {
          r += d[i++];
        }
        len += e;
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
      underlying = x.substring(leftOffset - 1, rightOffset + 1);
      digits = underlying.toCharArray();
      len = digits.length - 1;
      if (len > 0) {
        while (exponent <= len && digits[exponent++] == 0x30);
        int remain = len - exponent + 1;
        exponent--;
        char[] swap = new char[remain];
        System.arraycopy(digits, digits.length - remain, swap, 0, remain);
        digits = swap;
        swap = null;
      } else {
        digits = new char[0];
      }
      exponent = -exponent;
    } else {
      underlying = x.substring(leftOffset, total);
      len = underlying.length() - 1;

      char sc;
      int em = 0;
      while (exponent <= len && ((sc = underlying.charAt(len-exponent++)) == 0x30 || (sc < 0x30 && (em = exponent) > 0)));
      exponent -= (em > 0 ? (em + 1) : 1);
      int remain = underlying.length() - (em > 0 ? (exponent + em) : exponent);
      int i = 0;
      digits = new char[remain];
      System.out.println("this case "+ exponent);
      while (i < remain) {
        System.out.println("this case 2 "+ exponent);
        char c = underlying.charAt(i);
        if (c < 0x30) {
          exponent = i - 1;
          remain--;
          char[] swap = new char[remain];
          System.arraycopy(digits, 0, swap, 0, i);
          digits = swap;
          swap = null;
          i++;
          //FIXME arraycopy
          while (i < remain) {
            digits[i] = underlying.charAt(++i); //(int)(underlying.charAt(++i) - 48);
          }
          break;
        } else {
          digits[i++] = c; //(int)(c - 48);
        }
      }
    }

    return new Object[]{ signum, exponent, digits, signum ? ('-' + underlying) : underlying };
  }
}