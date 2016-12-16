package com.github.jancajthaml.money;

class Math {

  public static Object[] __add(boolean ls, char[] ld, int le, boolean rs, char[] rd, int re) {
    // 3% slower

    // TODO/FIXME align by exponent

    //System.out.println(">>>> ld(1) / " + java.util.Arrays.toString(ld));
    //System.out.println(">>>> rd(1) / " + java.util.Arrays.toString(rd));

    if (ls ^ rs) {
      // TODO/FIXME should _minus
      //r
      return new Object[]{};
    }

    char[] swap = null;
/*
    if (le > 0 && le >= ld.length - 1) {
      int zeroes = le - (ld.length - 1);
      swap = new char[ld.length + zeroes];
      java.util.Arrays.fill(swap, '0');
      System.arraycopy(ld, 0, swap, 0, ld.length);
      ld = swap;
      swap = null;
      //le = 0;
      //System.out.println("LE >>>>>> value + zeroes: " + zeroes + " / " + java.util.Arrays.toString(ld));
    } else if (le < 0 && ld.length - 1 >= le) {
      int zeroes = (ld.length - 1) - (le + 1);
      swap = new char[ld.length + zeroes];
      java.util.Arrays.fill(swap, '0');

      System.out.println("LE >>>>>> " + le + " " + zeroes + " " + java.util.Arrays.toString(ld) + " / " + java.util.Arrays.toString(swap));

      System.arraycopy(ld, 0, swap, swap.length - ld.length, ld.length);
      ld = swap;
      swap = null;
      //le = 0;
      //System.out.println("LE >>>>>> zeroes + value: " + zeroes + " / " + java.util.Arrays.toString(ld));
    }*/

    /*if (re > 0 && re >= rd.length - 1) {
      int zeroes = re - (rd.length - 1);
      swap = new char[rd.length + zeroes];
      java.util.Arrays.fill(swap, '0');
      System.arraycopy(rd, 0, swap, 0, rd.length);
      rd = swap;
      swap = null;
      //re = 0;
      //System.out.println("LE >>>>>> value + zeroes: " + zeroes + " / " + java.util.Arrays.toString(ld));
    } else if (re < 0 && rd.length - 1 >= re) {
      int zeroes = re - (rd.length - 1);
      swap = new char[rd.length + zeroes];
      java.util.Arrays.fill(swap, '0');
      System.arraycopy(rd, 0, swap, swap.length - rd.length, rd.length);
      rd = swap;
      swap = null;
      //re = 0;
    }*/

    //System.out.println(">>>> ld(1) / " + java.util.Arrays.toString(ld) + " / " + le);
    //System.out.println(">>>> rd(1) / " + java.util.Arrays.toString(rd) + " / " + re);

    int exponentDiff = le - re;

    if (exponentDiff > 0) {
      re = le;
      swap = new char[rd.length + exponentDiff];
      java.util.Arrays.fill(swap, '0');
      // FIXME subtranction rd.length twice
      System.arraycopy(rd, 0, swap, swap.length - rd.length, rd.length);
      rd = swap;
      swap = null;
    } else {
      swap = new char[ld.length - exponentDiff];
      //System.out.println("B --> " + swap.length);
      java.util.Arrays.fill(swap, '0');
      // FIXME subtranction ld.length twice
      System.arraycopy(ld, 0, swap, swap.length - ld.length, ld.length);
      ld = swap;
      swap = null;
    }

    int i = ld.length - rd.length;

    if (i < 0) {
      swap = new char[rd.length];
      java.util.Arrays.fill(swap, '0');
      System.arraycopy(ld, 0, swap, 0, ld.length);
      ld = swap;
      swap = null;
    } else if (i > 0) {
      swap = new char[ld.length];
      java.util.Arrays.fill(swap, '0');
      System.arraycopy(rd, 0, swap, 0, rd.length);
      rd = swap;
      swap = null;
    } 

    i = rd.length;

    try {
      while (i-- > 0) {
        int kf = (((int) ld[i] - 48) + ((int) rd[i] - 48));
        //System.out.println("adding " + ld[i] + " with " + rd[i] + " ––> " + kf);
        if (kf >= 10) {
          //System.out.println("carry to left at " + i);
          ld[i] = (char)((kf % 10) + 48);
          //System.out.println("modulus done "+ ld[i] + " at " + i);
          ld[i - 1] += 1;
          //System.out.println("left decrement");
        } else {
          ld[i] = (char)(kf + 48);
        }
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      //System.out.println("overflow");
      swap = new char[ld.length + 1];
      System.arraycopy(ld, 0, swap, 1, ld.length);
      swap[0] = '1';
      ld = swap;
      swap = null;
      re++;
    }

    //System.out.println(">>>> ld(4) / " + java.util.Arrays.toString(ld));
    //System.out.println(">>>> re: " + re);

    return new Object[]{ ls, re, ld };
  }
}