package com.github.jancajthaml.money;

class Serialization {

  public static String toString(boolean s, char[] d, int e) {
    char[] swap = new char[d.length + 1];
    System.arraycopy(d, 0, swap, 0, e);
    System.arraycopy(d, e - 1, swap, e, d.length - e + 1);
    swap[e] = '.';
    return s ? ('-' + new String(swap, 0, swap.length)) : new String(swap, 0, swap.length);
  }

  public static Object[] fromString(String x) {
    if (x == "0") {
      return new Object[]{ false, 1, new char[]{ '0', '0' }, "0.0" };
    }

    char[] characters = x.toCharArray();
    boolean sign = false;
    char[] swap = null;
    int len = characters.length;;

    if (characters[0] == '-') {
      sign = true;
      int i = 0;

      // TODO/FIXME change to indexOf maybe
      while (++i < len) {
        if (characters[i] == '.') {
          int left = i;

          int right = characters.length - i - 2;
          swap = new char[left + right];
          System.arraycopy(characters, 1, swap, 0, left - 1);
          System.arraycopy(characters, left + 1, swap, left - 1, right + 1);

          return new Object[]{ sign, i - 1, swap, x };
        }
      }

      swap = new char[characters.length];
      swap[swap.length - 1] = '0';
      System.arraycopy(characters, 1, swap, 0, characters.length - 1);

      return new Object[]{ sign, swap.length - 1, swap, x };
    } else {
      int i = -1;

      // TODO/FIXME change to indexOf maybe
      while (++i < len) {
        if (characters[i] == '.') {
          int left = i;
          int right = characters.length - i - 1;
          swap = new char[left + right];
          System.arraycopy(characters, 0, swap, 0, left);
          System.arraycopy(characters, left + 1, swap, left, right);
          return new Object[]{ sign, i, swap, toString(sign, swap, i) };
        }
      }

      swap = new char[characters.length + 1];
      swap[swap.length - 1] = '0';
      System.arraycopy(characters, 0, swap, 0, characters.length);

      return new Object[]{ sign, swap.length - 1, swap, toString(sign, swap, swap.length - 1) };
    }
  }
}
