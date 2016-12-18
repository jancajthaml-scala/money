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

  public static Object[] __subtract(boolean ls, char[] ld, int le, boolean rs, char[] rd, int re) {
    if (rs) {
      return __add(!rs, rd, re, ls, ld, le);
    } else if (ls ^ rs) {
      return __add(ls, ld, le, !rs, rd, re);
    }

    char[] swap = null;

    int exponentDiff = le - re;
    int j = 0;
    if (exponentDiff > 0) {
      //System.out.println(">>>> ld(3) / " + java.util.Arrays.toString(ld) + " / " + le);

      //System.out.println("case A");

      re = le;
      //int len = exponentDiff;
      j = exponentDiff;
      swap = fillZeroes(new char[rd.length + exponentDiff], 0, j);
      System.arraycopy(rd, 0, swap, j, rd.length);
      rd = swap;
    } else if (exponentDiff < 0) {

      //System.out.println("case B");

      j = -exponentDiff;
      swap = fillZeroes(new char[ld.length - exponentDiff], 0, j);
      System.arraycopy(ld, 0, swap, j, ld.length);

      //if (l) {
      swap = ld;
      ld = rd;
      rd = swap;
        //y.s = -y.s;
      //}
      ld = swap;
    } else {
      //System.out.println("case same exponents");

    //System.out.println(">>>> ld(0) / " + java.util.Arrays.toString(ld) + " / " + le);
    //System.out.println(">>>> rd(0) / " + java.util.Arrays.toString(rd) + " / " + re);
      // Exponents equal. Check digit by digit.
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
        //System.out.println("swapp ld and rd");
        swap = ld;
        ld = rd;
        rd = swap;
        rs = !rs;
      }
    }

    //System.out.println(">>>> ld(1) / " + java.util.Arrays.toString(ld) + " / " + le);
    //System.out.println(">>>> rd(1) / " + java.util.Arrays.toString(rd) + " / " + re);

    int i = ld.length - rd.length;

    if (i < 0) {
      //System.out.println("case A " + java.util.Arrays.toString(ld));
      swap = fillZeroes(new char[rd.length], 0, rd.length);
      System.arraycopy(ld, 0, swap, 0, ld.length);
      ld = swap;
    } else if (i > 0) {
      //System.out.println("case B");
      swap = fillZeroes(new char[ld.length], 0, ld.length);
      System.arraycopy(rd, 0, swap, 0, rd.length);
      rd = swap;
    }

    //System.out.println(">>>> ld(2) / " + java.util.Arrays.toString(ld) + " / " + le);
    //System.out.println(">>>> rd(2) / " + java.util.Arrays.toString(rd) + " / " + re);

    int b = 0;
    i = 0;

    /*
     * Append zeros to xc if shorter. No need to add zeros to yc if shorter
     * as subtraction only needs to start at yc.length.
     */
    /*if ((b = (j = rd.length) - (i = ld.length)) > 0) {
      for (; b--; ld[i++] = '0');
    }*/

    int m = rd.length;

    // Subtract yc from xc.
    for (b = ld.length; m > j;) {
      m--;
      //System.out.println("comparing "+(int)(ld[m] - 48) + " with " + (int)(rd[m] - 48));

      if (ld[m] < rd[m]) {
        //System.out.println("case less " + ld[m] + " < " + rd[m]);
        //System.out.println(">>>> before / " + java.util.Arrays.toString(ld));

        try {
          for (i = m; i > 0 && ld[--i] == '0'; ld[i] = '9') {
            //System.out.println("swapped 0 to 9 at " + i + " in " + ld[i]);
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          //System.out.println("underflow");
          swap = new char[ld.length + 1];
          System.arraycopy(ld, 0, swap, 1, ld.length);
          swap[0] = '0';
          ld = swap;
          //re++;
        }

        //System.out.println(">>>> after1 / " + java.util.Arrays.toString(ld));

        //System.out.println(">>>> after2 / " + ld[m] + " j: " + j + " m: " + m);
        --ld[i];
        //System.out.println(">>>> after3 / " + ld[m]);
        ld[m] = (char)(((int)(ld[m] - 48) + 10) + 48);
        //ld[m] += 10;
        //System.out.println(">>>> after4 / " + ld[m]);
      } /*else {
        System.out.println("case greater of same " + ld[m] + " >= " + rd[m]);
      }*/

      //System.out.println("before " + ld[m]);
      ld[m] = (char)(((int)(ld[m] - 48) - (int)(rd[m] - 48)) + 48);
      //System.out.println("after " + ld[m]);
      
      //System.out.println(">>>> after-X / " + java.util.Arrays.toString(ld));
    }

    //System.out.println(">>>> after-ld / " + java.util.Arrays.toString(ld));
    return new Object[]{ rs, re, ld };
  }

  public static Object[] __add(boolean ls, char[] ld, int le, boolean rs, char[] rd, int re) {
    if (ls ^ rs) {
      return __subtract(ls, ld, le, !rs, rd, re);
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