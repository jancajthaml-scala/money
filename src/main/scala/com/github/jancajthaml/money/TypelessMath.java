package com.github.jancajthaml.money;

class TypelessMath {

  final static char[] ZEROES;

  static {
    ZEROES = new char[1000];
    java.util.Arrays.fill(ZEROES, '0');
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

  public static Object[] minus(boolean ls, char[] ld, int le, boolean rs, char[] rd, int re) {
    if (rs) {
      return plus(!rs, rd, re, ls, ld, le);
    } else if (ls ^ rs) {
      return plus(ls, ld, le, !rs, rd, re);
    }

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

    return new Object[]{ rs, re, ld };
  }

  public static Object[] plus(boolean ls, char[] ld, int le, boolean rs, char[] rd, int re) {
    if (ls ^ rs) {
      return minus(ls, ld, le, !rs, rd, re);
    }

    char[] swap = null;

    int exponentDiff = le - re;

    if (exponentDiff > 0) {
      re = le;
      int len = exponentDiff;
      swap = fillZeroes(new char[rd.length + exponentDiff], 0, len);
      System.arraycopy(rd, 0, swap, len, rd.length);
      rd = swap;
    } else if (exponentDiff < 0) {
      int len = -exponentDiff;
      swap = fillZeroes(new char[ld.length - exponentDiff], 0, len);
      System.arraycopy(ld, 0, swap, len, ld.length);
      ld = swap;
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

    return new Object[]{ ls, re, ld };
  }
}