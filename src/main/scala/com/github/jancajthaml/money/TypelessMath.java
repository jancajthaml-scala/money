package com.github.jancajthaml.money;

class TypelessMath {

  private final static char[] ZEROES;
  private final static String ZEROES_STRING;

  static {
    ZEROES = new char[1000];
    java.util.Arrays.fill(ZEROES, '0');
    ZEROES_STRING = new String(ZEROES);
  }
  
  private static char[] fillZeroes(char[] dest, int from, int to) {
    int offset = from;
    int len = to - from;
    int remain = len;

    if (ZEROES.length < len) {
      while (remain > 0) {
        System.arraycopy(ZEROES, 0, dest, offset, remain);
        offset += remain;
        remain -= ZEROES.length;
      }
    } else {
      while (remain > 0) {
        System.arraycopy(ZEROES, 0, dest, offset, remain);
        offset += remain;
        remain -= len;
      }
    }
    return dest;
  }

  private static String prependZeroesString(String dest, int length) {
    System.out.println("prepending " + length + " zeroes to " + dest);
    //int offset = from;
    //int len = to - from;
    int tl = ZEROES_STRING.length();
    int remain = length;

    if (tl < length) {
      while (remain > 0) {
        dest = ZEROES_STRING.substring(0, tl) + dest;
        remain -= tl;
      }
    } else {
      while (remain > 0) {
        dest = ZEROES_STRING.substring(0, tl) + dest;
        //offset += remain;
        remain -= length;
      }
    }
    return dest;
  }



  private static String toString(boolean s, char[] d, int e) {
    if (s) {
      char[] swap = new char[d.length + 2];
      System.arraycopy(d, 0, swap, 1, e);
      System.arraycopy(d, e - 1, swap, e + 1, d.length - e + 1);
      swap[0] = '-';
      swap[e + 1] = '.';
      return new String(swap, 0, swap.length);
    } else {
      char[] swap = new char[d.length + 1];
      System.arraycopy(d, 0, swap, 0, e);
      System.arraycopy(d, e - 1, swap, e, d.length - e + 1);
      swap[e] = '.';
      return new String(swap, 0, swap.length);
    }
  }

  public static Object[] plus(boolean ls, String ld_, int le, boolean rs, String rd_, int re) {
    if (ls ^ rs) {
      return minus(ls, ld_, le, !rs, rd_, re);
    }

    char[] ld = ld_.replaceAll("[^0-9]", "").toCharArray();
    char[] rd = rd_.replaceAll("[^0-9]", "").toCharArray();

    char[] swap = null;

    int exponentDiff = le - re;

    if (exponentDiff > 0) {
      System.out.println("case A");
      System.out.println("before [case A] ld: " + ld_ + " ( " + java.util.Arrays.toString(ld) + " ) rd: " + rd_ + " ( " + java.util.Arrays.toString(rd) + " )");
      re = le;
      int len = exponentDiff;
      rd_ = prependZeroesString(ld_, len);
      swap = fillZeroes(new char[rd.length + exponentDiff], 0, len);
      System.arraycopy(rd, 0, swap, len, rd.length);
      rd = swap;
      System.out.println("after [case A] ld: " + ld_ + " ( " + java.util.Arrays.toString(ld) + " ) rd: " + rd_ + " ( " + java.util.Arrays.toString(rd) + " )");
    } else if (exponentDiff < 0) {
      System.out.println("case B");
      System.out.println("before [case B] ld: " + ld_ + " ( " + java.util.Arrays.toString(ld) + " ) rd: " + rd_ + " ( " + java.util.Arrays.toString(rd) + " )");
      int len = -exponentDiff;
      ld_ = prependZeroesString(ld_, len);
      swap = fillZeroes(new char[ld.length - exponentDiff], 0, len);
      System.arraycopy(ld, 0, swap, len, ld.length);
      ld = swap;
      System.out.println("after [case B] ld: " + ld_ + " ( " + java.util.Arrays.toString(ld) + " ) rd: " + rd_ + " ( " + java.util.Arrays.toString(rd) + " )");
    }

    int i = ld.length - rd.length;

    if (i < 0) {
      swap = fillZeroes(new char[rd.length], 0, rd.length);
      System.arraycopy(ld, 0, swap, 0, ld.length);
      ld = swap;
    } else if (i > 0) {
      swap = fillZeroes(new char[ld.length], 0, ld.length);
      System.arraycopy(rd, 0, swap, 0, rd.length);
      rd = swap;
    } 

    i = rd.length;

    try {
      while (i-- > 0) {
        int kf = (((int) ld[i] - 48) + ((int) rd[i] - 48));
        if (kf >= 10) {
          ld[i] = (char)((kf % 10) + 48);
          ld[i - 1] += 1;
        } else {
          ld[i] = (char)(kf + 48);
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      swap = new char[ld.length + 1];
      System.arraycopy(ld, 0, swap, 1, ld.length);
      swap[0] = '1';
      ld = swap;
      re++;
    }

    return new Object[]{ ls, re, toString(ls, ld, re) };
  }
  
  public static Object[] minus(boolean ls, String ld_, int le, boolean rs, String rd_, int re) {
    if (rs) {
      return plus(!rs, rd_, re, ls, ld_, le);
    } else if (ls ^ rs) {
      return plus(ls, ld_, le, !rs, rd_, re);
    }

    char[] ld = ld_.replaceAll("[^0-9]", "").toCharArray();
    char[] rd = rd_.replaceAll("[^0-9]", "").toCharArray();

    char[] swap = null;

    int exponentDiff = le - re;
    int j = 0;

    if (exponentDiff > 0) {
      re = le;
      j = exponentDiff;
      swap = fillZeroes(new char[rd.length + j], 0, j);
      System.arraycopy(rd, 0, swap, j, rd.length);
      rd = swap;
    } else if (exponentDiff < 0) {
      j = -exponentDiff;
      swap = fillZeroes(new char[ld.length + j], 0, j);
      System.arraycopy(ld, 0, swap, j, ld.length);
      
      ld = rd;
      rd = swap;
      rs = !rs;
    } else {
      boolean l = rd.length < ld.length;
      int x = (l ? rd : ld).length;
      int b = 0;

      for (j = b = 0; b < x; b++) {
        if (ld[b] != rd[b]) {
          l = ld[b] < rd[b];
          break;
        }
      }
      if (l) {
        swap = ld;
        ld = rd;
        rd = swap;
        rs = !rs;
      }
    }

    int i = ld.length - rd.length;

    if (i < 0) {
      swap = fillZeroes(new char[rd.length], 0, rd.length);
      System.arraycopy(ld, 0, swap, 0, ld.length);
      ld = swap;
    } else if (i > 0) {
      swap = fillZeroes(new char[ld.length], 0, ld.length);
      System.arraycopy(rd, 0, swap, 0, rd.length);
      rd = swap;
    }

    int b = 0;
    i = 0;

    int m = rd.length;

    for (b = ld.length; m-- > j;) {
      if (ld[m] < rd[m]) {
        try {
          for (i = m; i > 0 && ld[--i] == '0'; ld[i] = '9');
        } catch (ArrayIndexOutOfBoundsException e) {
          swap = new char[ld.length + 1];
          System.arraycopy(ld, 0, swap, 1, ld.length);
          swap[0] = '0';
          ld = swap;
          re--;
        }
        --ld[i];
        ld[m] += 10;
      }

      ld[m] = (char)(((int)(ld[m] - 48) - (int)(rd[m] - 48)) + 48);
    }

    return new Object[]{ rs, re, toString(rs, ld, re) };
  }

}