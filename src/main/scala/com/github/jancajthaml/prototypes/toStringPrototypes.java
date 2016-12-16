package com.github.jancajthaml.prototypes;

class toStringPrototypes {
  
  // TODO/FIXME add stream

  // TODO/FIXME add stream parallel

  public static String charArrayCopyInsert(boolean s, char[] d, int e) {
    String result = new String(d);

    return s ? ('-' + result.substring(0, e) + '.' + result.substring(e, d.length)) : (result.substring(0, e) + '.' + result.substring(e, d.length));
  }

  public static String charArrayCopyTwice(boolean s, char[] d, int e) {
    char[] swap = new char[d.length + 1];
    System.arraycopy(d, 0, swap, 0, e);
    System.arraycopy(d, e-1, swap, e, d.length - e + 1);
    swap[e] = '.';
    return s ? ('-' + new String(swap, 0, swap.length)) : new String(swap, 0, swap.length);
  }

  public static String StringBuilderAppend(boolean s, char[] d, int e) {
    StringBuilder result = new StringBuilder(d.length + (s ? 2 : 1));

    if (s) {
      result.append('-');
    }


    int i = -1;
    while (++i < e) {
      result.append(d[i]);
    }

    result.append('.');

    if (i >= d.length) {
      result.append('0');
    } else {
      while (++i < d.length) {
        result.append(d[i]);
      }
    }

    return result.toString();
  }


  public static String StringConcat(boolean s, char[] d, int e) {
    String result = "";

    if (s) {
      result += '-';
    }


    int i = -1;
    while (++i < e) {
      result += d[i];
    }

    result += '.';

    if (i >= d.length) {
      result += '0';
    } else {
      while (++i < d.length) {
        result += d[i];
      }
    }

    return result;
  }
}