package com.github.jancajthaml.money;

class Math {

  public static Object[] __add(boolean ls, char[] ld, int le, boolean rs, char[] rd, int re) {
    // 3% slower

    if (ls ^ rs) {
      // TODO/FIXME should _minus
      //r
      return new Object[]{};
    }

    if (ld[0] == 0 || rd[0] == 0) {
      return new Object[]{};
    }

    int exponentDiff = le - re;
    char[] swap = null;

    if (exponentDiff > 0) {
      re = le;
      swap = new char[rd.length + exponentDiff];
      // FIXME subtranction rd.length twice
      System.arraycopy(rd, 0, swap, swap.length - rd.length, rd.length);
      rd = swap;
      swap = null;
    } else {
      swap = new char[ld.length - exponentDiff];
      // FIXME subtranction ld.length twice
      System.arraycopy(ld, 0, swap, swap.length - ld.length, ld.length);
      ld = swap;
      swap = null;
    }

    int i = ld.length - rd.length;

    if (i < 0) {
      swap = new char[rd.length];
      System.arraycopy(ld, 0, swap, 0, ld.length);
      ld = swap;
      swap = null;
    } else if (i > 0) {
      swap = new char[ld.length];
      System.arraycopy(rd, 0, swap, 0, rd.length);
      rd = swap;
      swap = null;
    }

    i = rd.length;

    try {
      while (i-- > 0) {
        ld[i] += rd[i];
        if (ld[i] >= 10) {
          ld[i - 1] += 1;
          ld[i] %= 10;  // TODO/FIXME faster modulus 10
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      swap = new char[ld.length + 1];
      System.arraycopy(rd, 0, swap, 1, ld.length);
      swap[0] = 1;
      ld = swap;
      swap = null;
      re++;
    }

    // TODO/FIXME optimise
    // INFO DROP TRAILING ZEROES
    /*
    i = ld.length - 1;

    while (ld[i] == 0) { i--; }

    if (i != (ld.length - 1)) {
      swap = new int[i + 1];
      System.arraycopy(ld, 0, swap, 0, swap.length);
      ld = swap;
      swap = null;
    }*/

    return new Object[]{ ls, re, ld };
  }
}