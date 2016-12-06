package com.github.jancajthaml.money;

class Mapping {

  private static char[] _int2char = new char[]{'0','1','2','3','4','5','6','7','8','9'};

  public static int char2int(char p) {
    switch (p) {
      case '1': return 1;
      case '2': return 2;
      case '3': return 3;
      case '4': return 4;
      case '5': return 5;
      case '6': return 6;
      case '7': return 7;
      case '8': return 8;
      case '9': return 9;
      default: return 0;
    }
  }

  public static char int2char(int p) {
    return Mapping._int2char[p];
  }
}