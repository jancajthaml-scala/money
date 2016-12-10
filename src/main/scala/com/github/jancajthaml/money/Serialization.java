package com.github.jancajthaml.money;

class Serialization {

  public static Object[] fromString(String x) {
    boolean signum = x.charAt(0) == '-';
    int exponent = 1;
    
    int total = x.length();

    int leftDrop = 0;
    int rightDrop = 0;

    int[] left = new int[total];
    int[] right = new int[total];

    int li = 0;
    int ri = total - 1;

    int leftPass = 0;
    int rightPass = 0;

    int i = signum ? 1 : 0;
    int decimal = -1;

    while (i < total && x.charAt(i++) == '0');
    --i;
    //System.out.println("leftDrop "+(i));
    leftDrop = i;
    // INFO left skip now saved in i
    scan1: while (i < total) {
      switch (x.charAt(i++)) {
        case '.': {
          decimal = i;
          if (i == total) {
            break scan1;
          }
          
          i = total;
          while (--i >= 0 && x.charAt(i) == '0');
          rightDrop = total - i - 1;
          //i++;
          while (i >= decimal && i < total) {
            // FIXME
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

    int leftBound = leftDrop;
    int rightBound = (total - rightDrop);

    int [] digits = null;

    if ((decimal + rightDrop) == total) {
      int f = left.length;
      while (--f > 0 && left[f] == 0);
      f++;
      digits = new int[f];
      System.arraycopy(left, 0, digits, 0, digits.length);
      digits = new int[li + ri];
      decimal = leftPass;
      rightBound = (leftBound + li);
    } else if ((li + leftDrop) == total) {
      int f = left.length;
      while (--f > 0 && left[f] == 0);
      f++;
      digits = new int[f];
      System.arraycopy(left, 0, digits, 0, digits.length);
      decimal = leftPass;
      rightBound = (leftBound + li);
    } else if ((leftDrop + 1) == decimal) {
      System.out.println("C " + rightPass);
      int f = 0;
      while (f < right.length && right[f++] == 0);
      f--;
      digits = new int[right.length - f];
      System.arraycopy(right, f, digits, 0, digits.length);
      decimal = -rightPass;
      leftBound = leftBound + rightPass + decimal + 1;
    } else {
      // TODO/FIXME implement
      digits = new int[li + ri];
      decimal = decimal - leftDrop - 1;
    }

    String value = null;

    if (leftBound == rightBound) {
      value = signum ? "-0" : "0";
    } else {
      if (decimal < 0) {
        value = signum ? ("-0." + x.substring(leftBound, rightBound)) : ("0." + x.substring(leftBound, rightBound));
      } else {
        value = signum ? ('-' + x.substring(leftBound, rightBound)) : x.substring(leftBound, rightBound);  
      }
      
    }

    System.out.println(x + " -----> value: [" + value + "] exponent: [" + decimal + "] signum: [" + signum + "] digits: " + java.util.Arrays.toString(digits));

    return new Object[]{ signum, decimal, digits, value };
  }


}