package com.github.jancajthaml.money;

class Math {

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

  public static Object[] __add(boolean ls, char[] ld, int le, boolean rs, char[] rd, int re) {

    if (ls ^ rs) {
      // TODO/FIXME should _minus
      //r
      return new Object[]{};
    }

    char[] swap = null;

    int exponentDiff = le - re;

    if (exponentDiff > 0) {
      re = le;
      swap = new char[rd.length + exponentDiff];
      swap = fillZeroes(swap, 0, swap.length - rd.length); // FIXME
      System.arraycopy(rd, 0, swap, swap.length - rd.length, rd.length);
      rd = swap;
      swap = null;
    } else {
      swap = new char[ld.length - exponentDiff];
      swap = fillZeroes(swap, 0, swap.length - ld.length); // FIXME
      System.arraycopy(ld, 0, swap, swap.length - ld.length, ld.length);
      ld = swap;
      swap = null;
    }

    int i = ld.length - rd.length;

    if (i < 0) {
      swap = new char[rd.length];
      swap = fillZeroes(swap, ld.length, swap.length); // FIXME
      System.arraycopy(ld, 0, swap, 0, ld.length);
      ld = swap;
      swap = null;
    } else if (i > 0) {
      swap = new char[ld.length];
      swap = fillZeroes(swap, rd.length, swap.length);  // FIXME
      System.arraycopy(rd, 0, swap, 0, rd.length);
      rd = swap;
      swap = null;
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
      swap = null;
      re++;
    }

    return new Object[]{ ls, re, ld };
  }
}