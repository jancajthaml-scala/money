package com.github.jancajthaml.money;

class Serialization {

  public static Object[] fromString(String x) {
    System.out.println("parsing from string ... " + x);

    boolean signum = x.charAt(0) == '-';
    int exponent = 1;
    
    int total = x.length();

    int[] left = new int[total];
    int[] right = new int[total];

    int li = 0;
    int ri = total - 1;

//    boolean rightOffsetFound = false;

    int leftPass = 0;
    int rightPass = 0;

    int i = signum ? 1 : 0;
    int decimal = 0;
    boolean decimalFound = false;

    while (i < total && x.charAt(i++) == '0');

    --i;

    scan1: while (i < total) {
      switch (x.charAt(i++)) {
        case '.': {
          decimal = li;
          i = total;
          while (--i >= 0 && x.charAt(i) == '0');
          i++;
          scan2: while (i > decimal) {
            switch (x.charAt(i--)) {
              case '0': {
                right[ri--] = 0;
                rightPass++;
                break;
              }
              case '1': {
                right[ri--] = 1;
                break;
              }
              case '2': {
                right[ri--] = 2;
                break;
              }
              case '3': {
                right[ri--] = 3;
                break;
              }
              case '4': {
                right[ri--] = 4;
                break;
              }
              case '5': {
                right[ri--] = 5;
                break;
              }
              case '6': {
                right[ri--] = 6;
                break;
              }
              case '7': {
                right[ri--] = 7;
                break;
              }
              case '8': {
                right[ri--] = 8;
                break;
              }
              case '9': {
                right[ri--] = 9;
                break;
              }
            }
          }
          break scan1;
        }
        case '0': {
          left[li++] = 0;
          leftPass++;
          break;
        }
        case '1': {
          left[li++] = 1;
          break;
        }
        case '2': {
          left[li++] = 2;
          break;
        }
        case '3': {
          left[li++] = 3;
          break;
        }
        case '4': {
          left[li++] = 4;
          break;
        }
        case '5': {
          left[li++] = 5;
          break;
        }
        case '6': {
          left[li++] = 6;
          break;
        }
        case '7': {
          left[li++] = 7;
          break;
        }
        case '8': {
          left[li++] = 8;
          break;
        }
        case '9': {
          left[li++] = 9;
          break;
        }
      }
    }

    System.out.println("L >> " + li + " of " + left.length + " without " + leftPass + " ... decimal >> " + decimal + " ... R >> " + (right.length - ri) + " of " + right.length + " without " + rightPass);

    int[] digits = new int[10];

    return new Object[]{ signum, exponent, digits };
  }


}