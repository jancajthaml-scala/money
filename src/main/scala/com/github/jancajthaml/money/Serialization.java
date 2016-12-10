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
          // INFO right trim now saved in i
          i++;
          while (i > decimal && i < total) {
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

    //System.arraycopy(new double[m4], 0, d, m3 + 2, m4 - m3);
/*
    int possibleRight = ri - rightDrop;
    int possibleLeft = li - leftDrop;

    System.out.println("ri: "+(total - ri)+" li: "+li+" rightPass: "+rightPass+" leftPass: "+leftPass+ " decimal: "+decimal+" leftDrop: "+leftDrop+" rightDrop: "+rightDrop);
    System.out.println("possible right length "+ possibleRight);
    System.out.println("possible left length "+ possibleLeft);
    System.out.println("trimmed length "+(total - leftDrop - rightDrop - (decimal == -1 ? 0 : 1)));
*/
    //System.out.println("L >> " + li + " of " + left.length + " without " + leftPass + " ... decimal >> " + decimal + " ... R >> " + (right.length - ri) + " of " + right.length + " without " + rightPass);

    int leftBound = leftDrop;
    int rightBound = (total - rightDrop);

    int[] digits = new int[10];

    //System.out.println("leftDrop "+leftDrop+" decimal "+decimal+" rightDrop "+rightDrop+" total "+total+" of "+x);
    
    if ((decimal + rightDrop) == total) {
      decimal = leftPass;
      rightBound = (leftBound + li);
    } else if ((li + leftDrop) == total) {
      decimal = leftPass;
      rightBound = (leftBound + li);
    } else if ((leftDrop + 1) == decimal) {
      decimal = -rightPass - 1;
      leftBound = leftBound + rightPass + decimal + 2;
    } else {
      decimal = decimal - leftDrop - 1;
    }

    //System.out.


    String value = null;

    if (leftBound == rightBound) {
      value = signum ? "-0" : "0";
    } else {
      if (decimal < 0) {
        //System.out.println("this case");
        value = signum ? ("-0." + x.substring(leftBound, rightBound)) : ("0." + x.substring(leftBound, rightBound));
      } else {
        value = signum ? ('-' + x.substring(leftBound, rightBound)) : x.substring(leftBound, rightBound);  
      }
      
    }

    System.out.println(x + " -----> " + value + " ... " + decimal);

    /*
    if (ri == 0) {
      System.out.println(">>> nothing on right side (2) of " + x);
    }*/


    /*
    if (decimal == -1 || rightDrop + 1 == (leftBound - (signum ? 1 : 0))) {
      //System.out.println("right empty");
      // EMPTY RIGHT
      rightBound = (leftBound + li - leftPass);
      decimal = leftPass;
    } else {
      System.out.println("right non-empty");
      System.out.println("leftBound "+leftBound+" rightBound "+rightBound+" rightPass "+rightPass+" decimal "+decimal+" of "+x);
      // NON_EMPTY RIGHT
      //digits = new int[0];
    }*/

    //System.out.println("LB "+leftBound+" RB "+rightBound+ " D " + (leftBound+decimal)+" RD " + rightDrop);
    //System.out.println(x + " ---> " + x.substring(leftBound, rightBound) + " * 10 ^ " + decimal);

    //System.out.println("boundaries ... " + leftDrop + " - " + (total - rightDrop) + " --> " + (x.substring(leftDrop, total - rightDrop)));
    //int[] digits = new int[10];

    return new Object[]{ signum, decimal, digits };
  }


}