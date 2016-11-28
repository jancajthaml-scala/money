package com.github.jancajthaml.money;

class PreciseNumber { // implements Cloneable {

  private static int precision_current = 0;

  static int round = 0;
  static int precision_digits_current = 0;
  static int precision_words = 0;
  static int precision_digits = 0;
  static int precision_digits_out = 0;
  static int ellog10 = 0;
  static int mp2 = 0;
  static int mp21 = 0;
  static int nw = 0;
  static Complex[] uu1 = null;
  static Complex[] uu2 = null;

  static {
    setMaximumPrecision(100);
  }

  public static final void setMaximumPrecision(int p) {
    if (p != precision_current) {
      precision_current = Math.min(Math.abs(p), 3600);
      round = 1;
      precision_digits = precision_current;
      precision_digits_out = 10000000;
      ellog10 = 10 - precision_digits;
      precision_words = (int)((precision_digits / 7.224719896) + 1);
      mp2 = precision_words + 2;
      mp21 = mp2 + 1;
      precision_digits_current = precision_digits_out;
      nw = precision_words;
      int n = nw + 1;
      double ti = 0.0;
      int j = 0;
      int i = 0;
      double t1 = 0.75 * n;
      int m = (int)(1.4426950408889633 * Math.log(t1) + 1.0 - 5.6843418860808015e-14);
      int mq = m + 2;
      int nq = (int)(Math.pow(2, mq));
      uu1 = new Complex[nq];
      uu1[0] = new Complex(mq, 0);
      int ku = 1;
      int ln = 1;

      for (j = 1; j <= mq; j++) {
        t1 = Math.PI / ln;
        for (i = 0; i <= ln - 1; i++) {
          ti = i * t1;
          uu1[i + ku] = new Complex(Math.cos(ti), Math.sin(ti));
        }
        ku += ln;
        ln <<= 1;
      }

      double tpn = 0.0;
      int k = 0;
      t1 = 0.75 * n;
      uu2 = new Complex[mq + nq];
      ku = mq + 1;
      uu2[0] = new Complex(mq, 0);
      int mm = 0;
      int nn = 0;
      int mm1 = 0;
      int mm2 = 0;
      int nn1 = 0;
      int nn2 = 0;
      int iu = 0;

      for (k = 2; k <= mq - 1; k++) {
        uu2[k - 1] = new Complex(ku, 0);
        mm = k;
        // TODO/FIXME use fast integral power
        nn = (int)(Math.pow(2, mm));
        mm1 = (mm + 1) / 2;
        mm2 = mm - mm1;
        // TODO/FIXME use fast integral power
        nn1 = (int)(Math.pow(2, mm1));
        // TODO/FIXME use fast integral power
        nn2 = (int)(Math.pow(2, mm2));
        tpn = 2.0 * Math.PI / nn;

        for (j = 0; j < nn2; j++) {
          for (i = 0; i < nn1; i++) {
            iu = ku + i + j * nn1;
            t1 = tpn * i * j;
            uu2[iu - 1] = new Complex(Math.cos(t1), Math.sin(t1));
          }
        }

        ku += nn;
      }
    }
  }

  static final int precisionToSize(int precision) {
    int maxnw = (int)((precision / 7.224719896) + 4);
    return maxnw < 8 ? 8 : maxnw;
  }

  int maxnw = 0;
  boolean sign = false;
  int number_words = 0;
  int exponent = 0;
  public float mantissa[] = null;
  static double t30 = 0;
  static double r30 = 0;
  static double sd = 314159265;
  static Integer randMutex = new Integer(0);
  
  private PreciseNumber(PreciseNumber in) {
    maxnw = in.maxnw;

    if (maxnw > 0) {
      mantissa = new float[maxnw];
      _eq(in , this, nw);
    } else {
      exponent = in .exponent;
      sign = in .sign;
      number_words = in .number_words;
      mantissa = null;
    }
  }

  // TODO/INFO mostly used
  PreciseNumber(int maxNW, boolean b) {
    maxnw = maxNW;
    sign = true;
    number_words = 0;
    exponent = 0;

    if (maxnw >= 1) {
      mantissa = new float[maxnw];
      mantissa[0] = 0;
    } else {
      mantissa = null;
    }
  }

  PreciseNumber(boolean b, int precision) {
    maxnw = precisionToSize(precision);
    mantissa = new float[maxnw];
    mantissa[0] = 0;
    sign = true;
    number_words = 0;
    exponent = 0;
  }

  // TODO/INFO mostly used
  PreciseNumber(double ia, int precision) {
    maxnw = precisionToSize(precision);
    mantissa = new float[maxnw];
    dmc(new Chunk(ia, 0), this);
  }

  PreciseNumber(String str) {
    char temp[] = str.toCharArray();
    maxnw = precisionToSize(precision_digits);
    mantissa = new float[maxnw];
    fromString(temp, temp.length, this, Math.min(nw, maxnw - 2));
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    try {
      return 0 == compare(this, (PreciseNumber) o, nw);
    } catch (ClassCastException cce) {
      return false;
    }
  }

  public int compareTo(Object o) {
    return compare(this, (PreciseNumber) o, nw);
  }

  public static int getPrecisionInDigits() {
    return precision_digits;
  }

  private static void _add(PreciseNumber a, PreciseNumber b, PreciseNumber c, int lnw) {
    int i = 0;

    int na = (a.number_words > lnw) ? lnw : a.number_words;
    int nb = (b.number_words > lnw) ? lnw : b.number_words;

    double d[] = new double[lnw + 5];
    double db = a.sign == b.sign ? 1.0 : -1.0;
    int ixa = a.exponent;
    int ixb = b.exponent;
    int ish = ixa - ixb;
    int nd = 0;
    int ixd = 0;

    if (ish >= 0) {
      int m1 = Math.min(na, ish);
      int m2 = Math.min(na, nb + ish);
      int m3 = na;
      int m4 = Math.min(Math.max(na, ish), lnw + 1);
      int m5 = Math.min(Math.max(na, nb + ish), lnw + 1);
      d[0] = 0;
      d[1] = 0;

      // TODO/FIXME use System.arraycopy
      for (i = 0; i < m1; i++) {
        d[i + 2] = a.mantissa[i];
      }
      for (i = m1; i < m2; i++) {
        d[i + 2] = a.mantissa[i] + db * b.mantissa[i - ish];
      }
      // TODO/FIXME use System.arraycopy
      for (i = m2; i < m3; i++) {
        d[i + 2] = a.mantissa[i];
      }
      // TODO/FIXME use System.arraycopy
      for (i = m3; i < m4; i++) {
        d[i + 2] = 0.0;
      }
      for (i = m4; i < m5; i++) {
        d[i + 2] = db * b.mantissa[i - ish];
      }

      nd = m5;
      ixd = ixa;
      d[nd + 2] = 0.0;
      d[nd + 3] = 0.0;
    } else {
      // TODO/FIXME delete -ish and use approprielty below
      int nsh = -ish;
      int m1 = nb < nsh ? nb : nsh;
      int m2 = Math.min(nb, na + nsh);
      int m3 = nb;
      int m4 = Math.min(nb > nsh ? nb : nsh, lnw + 1);
      int m5 = Math.min(Math.max(nb, na + nsh), lnw + 1);
      d[0] = 0;
      d[1] = 0;

      for (i = 0; i < m1; i++) {
        d[i + 2] = db * b.mantissa[i];
      }
      for (i = m1; i < m2; i++) {
        d[i + 2] = a.mantissa[i - nsh] + db * b.mantissa[i];
      }
      for (i = m2; i < m3; i++) {
        d[i + 2] = db * b.mantissa[i];
      }
      for (i = m3; i < m4; i++) {
        d[i + 2] = 0.0;
      }
      for (i = m4; i < m5; i++) {
        d[i + 2] = a.mantissa[i - nsh];
      }

      nd = m5;
      ixd = ixb;
      d[nd + 2] = 0.0;
      d[nd + 3] = 0.0;
    }

    d[0] = a.sign ? (nd < 0 ? -nd : nd) : (nd < 0 ? nd : -nd);
    d[1] = ixd;

    mpnorm(d, c, lnw);
  }

  static void cbrt(PreciseNumber a, PreciseNumber b, int lnw) {
    int na = Math.min(a.number_words, lnw);

    if (na == 0) {
      b.number_words = 0;
      b.sign = true;
      b.exponent = 0;
      return;
    }

    if (!a.sign) {
      throw new ArithmeticException("mpcbrt: argument is negative --> " + a);
    }

    int _sk = lnw + 3;
    PreciseNumber f = new PreciseNumber(6, false);
    PreciseNumber sk0 = new PreciseNumber(_sk, false);
    PreciseNumber sk1 = new PreciseNumber(_sk, false);
    PreciseNumber sk2 = new PreciseNumber(_sk, false);

    Chunk t1 = new Chunk(0, 0);
    t1.sa(lnw);
    int mq = (int)(1.4426950408889633 * Math.log(t1.ga()) + 1.0 - 5.6843418860808015e-14);

    lnw++;

    mul(a, a, sk0, lnw);
    mdc(a, t1);

    Chunk t2 = new Chunk(0, 0);
    t2.sn(-(t1.gn() << 1) / 3);
    t2.sa(Math.pow((t1.ga() * Math.pow(2.0, (t1.gn() + 3.0 * t2.gn() / 2.0))), (-2.0 / 3.0)));

    dmc(t2, b);

    f.sign = true;
    f.number_words = 1;
    f.exponent = 0;
    f.mantissa[0] = 1;
    f.mantissa[1] = 0;
    lnw = 3;
    int iq = 0;

    for (int k = 2; k <= mq - 1; k++) {
      int nw1 = lnw;
      lnw = Math.min((lnw << 1) - 2, lnw) + 1;
      int nw2 = lnw;
      boolean no_stop = true;

      while (no_stop) {
        mul(b, b, sk1, lnw);
        mul(b, sk1, sk2, lnw);
        mul(sk0, sk2, sk1, lnw);
        _sub(f, sk1, sk2, lnw);

        lnw = nw1;

        mul(b, sk2, sk1, lnw);
        mpdivd(sk1, new Chunk(3.0, 0), sk2, lnw);

        lnw = nw2;

        _add(b, sk2, sk1, lnw);
        _eq(sk1, b, lnw);

        if (k == mq - 3 && iq == 0) {
          iq = 1;
        } else {
          no_stop = false;
        }
      }
    }

    mul(a, b, sk0, lnw);

    int nw1 = lnw;
    lnw = Math.min((lnw << 1) - 2, lnw) + 1;
    int nw2 = lnw;

    mul(sk0, sk0, sk1, lnw);
    mul(sk0, sk1, sk2, lnw);
    _sub(a, sk2, sk1, lnw);

    lnw = nw1;

    mul(sk1, b, sk2, lnw);
    mpdivd(sk2, new Chunk(3.0, 0), sk1, lnw);

    lnw = nw2;

    _add(sk0, sk1, sk2, lnw);
    _eq(sk2, b, lnw);
    round(b, lnw);
  }

  static int compare(PreciseNumber a, PreciseNumber b, int lnw) {
    int i = 0;
    int ic = 0;
    
    // TODO/FIXME to boolean
    int ia = a.sign ? 1 : -1;

    if (a.number_words == 0.0) {
      ia = 0;
    }

    // TODO/FIXME to boolean
    int ib = b.sign ? 1 : -1;

    if (b.number_words == 0.0) {
      ib = 0;
    }

    int na = a.number_words < lnw ? a.number_words : lnw;
    int nb = b.number_words < lnw ? b.number_words : lnw;

    if (ia != ib) {
      ic = ia >= ib ? 1 : -1;
    } else if (a.exponent != b.exponent) {
      ic = (int)(ia * (a.exponent >= b.exponent ? 1 : -1));
    } else {
      boolean sameMantissas = true;

      for (i = 0; i < Math.min(na, nb); i++) {
        if (a.mantissa[i] != b.mantissa[i]) {
          ic = (int)(ia * (a.mantissa[i] >= b.mantissa[i] ? 1 : -1));
          sameMantissas = false;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }

      if (sameMantissas) {
        ic = (na != nb) ? (int)(ia * (na >= nb ? 1 : -1)) : 0;
      }
    }
    return ic;
  }

  static void mpdiv(PreciseNumber a, PreciseNumber b, PreciseNumber c, int lnw) {
    int i = 0;
    int j = 0;
    double rb = 0.0;
    double ss = 0.0;
    double t0 = 0.0;
    double t1 = 0.0;
    double t2 = 0.0;
    int na = Math.min(a.number_words, lnw);
    int nb = Math.min(b.number_words, lnw);

    if (na == 0) {
      c.number_words = 0;
      c.sign = true;
      c.exponent = 0;
      return;
    }

    if (nb == 1 && b.mantissa[0] == 1.0) {
      c.number_words = na;
      c.sign = !(a.sign ^ b.sign);
      c.exponent = a.exponent - b.exponent;

      for (i = 0; i < na; i++) {
        c.mantissa[i] = a.mantissa[i];
      }
      return;
    }

    if (nb == 0) throw new ArithmeticException("mpdiv: Divisor is zero.");

    double d[] = new double[lnw + 4];
    t0 = 1.6777216e7 * b.mantissa[0];

    if (nb >= 2) {
      t0 += b.mantissa[1];
    }
    if (nb >= 3) {
      t0 += 5.9604644775390625e-8 * b.mantissa[2];
    }
    if (nb >= 4) {
      t0 += 3.552713678800501e-15 * b.mantissa[3];
    }

    rb = 1.0 / t0;
    int md = Math.min(na + nb, lnw);
    d[0] = 0.0;

    for (i = 1; i <= na; i++) {
      d[i] = a.mantissa[i - 1];
    }
    for (i = na + 1; i < md + 4; i++) {
      d[i] = 0.0;
    }
    for (j = 2; j <= na + 1; j++) {
      t1 = 2.81474976710656e14 * d[j - 2] + 1.6777216e7 * d[j - 1] + d[j] + 5.9604644775390625e-8 * d[j + 1];
      t0 = (int)(rb * t1);
      int j3 = j - 3;
      int i2 = Math.min(nb, lnw + 2 - j3) + 2;
      int ij = i2 + j3;

      for (i = 2; i < i2; i++)
          d[i + j3] -= t0 * b.mantissa[i - 2];

      if (((j - 1) % 32) == 0) {
        for (i = j; i < ij; i++) {
          t1 = d[i];
          t2 = (int)(5.9604644775390625e-8 * t1);
          d[i] = t1 - 1.6777216e7 * t2;
          d[i - 1] += t2;
        }
      }

      d[j - 1] += 1.6777216e7 * d[j - 2];
      d[j - 2] = t0;
    }

    boolean no_stop = true;

    for (j = na + 2; j <= lnw + 3; j++) {
      t1 = 2.81474976710656e14 * d[j - 2] + 1.6777216e7 * d[j - 1] + d[j];

      if (j <= lnw + 2) t1 = t1 + 5.9604644775390625e-8 * d[j + 1];

      t0 = (int)(rb * t1);
      int j3 = j - 3;
      int i2 = Math.min(nb, lnw + 2 - j3) + 2;
      int ij = i2 + j3;
      ss = 0.0;
      int i3 = 0;

      for (i = 2; i < i2; i++) {
        i3 = i + j3;
        d[i3] -= t0 * b.mantissa[i - 2];
        ss = ss + Math.abs(d[i3]);
      }

      if (((j - 1) % 32) == 0) {
        for (i = j; i < ij; i++) {
          t1 = d[i];
          t2 = (int)(5.9604644775390625e-8 * t1);
          d[i] = t1 - 1.6777216e7 * t2;
          d[i - 1] += t2;
        }
      }

      d[j - 1] += 1.6777216e7 * d[j - 2];
      d[j - 2] = t0;

      if (ss == 0.0) {
        no_stop = false;
        // TODO/FIXME cannot be converted into scala
        break;
      }

      if (ij <= lnw + 1) {
        d[ij + 2] = 0.0;
      }
    }

    if (no_stop) {
      j = lnw + 3;
    }

    d[j - 1] = 0.0;
    int is = (d[0] == 0.0) ? 1 : 2;
    int nc = Math.min(j - 1, lnw);
    d[nc + 2] = 0.0;
    d[nc + 3] = 0.0;

    // System.arraycopy(src, srcPos, dest, destPos, length);

    for (i = j; i >= 2; i--) {
      d[i] = d[i - is];
    }

    d[0] = a.sign ^ b.sign ? (nc < 0 ? nc : -nc) : (nc < 0 ? -nc : nc);
    d[1] = a.exponent - b.exponent + is - 2;

    mpnorm(d, c, lnw);
  }

  static void mpdivd(PreciseNumber a, Chunk b, PreciseNumber c, int lnw) {
    int na = a.number_words < lnw ? a.number_words : lnw;

    if (na == 0) {
      c.number_words = 0;
      c.sign = true;
      c.exponent = 0;
      return;
    }

    if (b.ga() == 0.0) {
      throw new ArithmeticException("mpdivd: Divisor is zero");
    }

    int j = 0;
    int k = 0;
    double bb = 0.0;
    double br = 0.0;
    double dd = 0.0;
    double t1 = 0.0;
    PreciseNumber f = new PreciseNumber(6, false);

    int ib = b.ga() >= 0 ? 1 : -1;

    int n1 = b.gn() / 24;
    int n2 = b.gn() - 24 * n1;

    bb = Math.abs(b.ga()) * Math.pow(2.0, n2);

    if (bb >= 1.6777216e7) {
      for (k = 1; k <= 100; k++) {
        bb = 5.9604644775390625e-8 * bb;

        if (bb < 1.6777216e7) {
          n1 += k;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
    } else if (bb < 1.0) {
      for (k = 1; k <= 100; k++) {
        bb = 1.6777216e7 * bb;

        if (bb >= 1.0) {
          n1 -= k;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
    }

    if (bb != (int)(bb)) {
      dmc(new Chunk((b.ga() >= 0 ? (bb < 0 ? -bb : bb) : (bb < 0 ? bb : -bb)), n1 * 24), f);
      mpdiv(a, f, c, lnw);
      return;
    }

    double d[] = new double[lnw + 4];
    br = 1.0 / bb;
    dd = a.mantissa[0];
    boolean skipJ = false;

    for (j = 2; j <= lnw + 3; j++) {
      t1 = (int)(br * dd);
      d[j] = t1;
      dd = 1.6777216e7 * (dd - t1 * bb);

      if (j <= na) {
        dd = dd + a.mantissa[j - 1];
      } else if (dd == 0.0) {
        skipJ = true;
        // TODO/FIXME cannot be converted into scala
        break;
      }
    }

    if (!skipJ) {
      j = lnw + 3;
    }

    int nc = Math.min(j - 1, lnw);
    d[0] = ((a.sign ? 1 : -1) * ib) >= 0 ? (nc < 0 ? -nc : nc) : (nc < 0 ? nc : -nc);
    d[1] = a.exponent - n1;

    if (j <= lnw + 2) {
      d[j + 1] = 0.0;
    }
    
    if (j <= lnw + 1) {
      d[j + 2] = 0.0;
    }

    mpnorm(d, c, lnw);
  }

  static void dmc(Chunk a, PreciseNumber b) {
    if (a.ga() == 0.0) {
      b.number_words = 0;
      b.sign = true;
      b.exponent = 0;
      return;
    }

    int i = 0;
    int k = 0;
    double aa = 0.0;

    int n1 = a.gn() / 24;
    int n2 = a.gn() - 24 * n1;
    aa = Math.abs(a.ga()) * Math.pow(2.0, n2);

    if (aa >= 1.6777216e7) {
      for (k = 1; k <= 100; k++) {
        aa *= 5.9604644775390625e-8;
        if (aa < 1.6777216e7) {
          n1 += k;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
    } else if (aa < 1.0) {
      for (k = 1; k <= 100; k++) {
        aa *= 1.6777216e7;
        if (aa >= 1.0) {
          n1 -= k;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
    }

    b.exponent = n1;
    b.mantissa[0] = (float)((int)(aa));
    aa = 1.6777216e7 * (aa - b.mantissa[0]);
    b.mantissa[1] = (float)((int)(aa));
    aa = 1.6777216e7 * (aa - b.mantissa[1]);
    b.mantissa[2] = (float)((int)(aa));
    aa = 1.6777216e7 * (aa - b.mantissa[2]);
    b.mantissa[3] = (float)((int)(aa));
    b.mantissa[4] = 0;
    b.mantissa[5] = 0;

    for (i = 3; i >= 0; i--) {
      if (b.mantissa[i] != 0.0) {
        // TODO/FIXME cannot be converted into scala
        break;
      }
    }

    aa = i + 1;
    b.sign = a.ga() >= 0;
    b.number_words = (int)(aa);
  }

  private static void _eq(PreciseNumber a, PreciseNumber b, int lnw) {
    int na = a.number_words < lnw ? a.number_words : lnw;

    if (na == 0) {
      b.number_words = 0;
      b.sign = true;
      b.exponent = 0;
      return;
    }

    b.number_words = na;
    b.sign = a.sign;
    b.exponent = a.exponent;

    System.arraycopy(a.mantissa, 0, b.mantissa, 0, na + 1);
  }

  static void infr(PreciseNumber a, PreciseNumber b, PreciseNumber c, int lnw) {
    int na = a.number_words < lnw ? a.number_words : lnw;
    int ma = a.exponent;

    if (na == 0) {
      b.number_words = c.number_words = 0;
      b.sign = c.sign = true;
      c.exponent = c.exponent = 0;
    }

    if (ma >= lnw - 1) {
      throw new ArithmeticException("infr: Argument is too large -->" + a);
    }

    int i = 0;
    
    int nb = Math.min(ma > -1 ? (ma + 1) : 0, na);

    if (nb == 0) {
      b.number_words = 0;
      b.sign = true;
      b.exponent = 0;
    } else {
      b.number_words = nb;
      b.sign = a.sign;
      b.exponent = ma;
      b.mantissa[nb] = 0;
      b.mantissa[nb + 1] = 0;

      System.arraycopy(a.mantissa, 0, b.mantissa, 0, nb);
    }

    int nc = na - nb;

    if (nc <= 0) {
      c.number_words = 0;
      c.sign = true;
      c.exponent = 0;
    } else {
      c.number_words = nc;
      c.sign = a.sign;
      c.exponent = ma - nb;
      c.mantissa[nc] = 0;
      c.mantissa[nc + 1] = 0;

      System.arraycopy(a.mantissa, nb, c.mantissa, 0, nc);
    }

    round(b, lnw);
    round(c, lnw);
  }

  static void mdc(PreciseNumber a, Chunk b) {
    if (a.number_words == 0) {
      b.sa(0.0);
      b.sn(0);
    } else {
      double aa = a.mantissa[0];

      if (a.number_words >= 2) {
        aa += 5.9604644775390625e-8 * a.mantissa[1];
      }
      if (a.number_words >= 3) {
        aa += 3.552713678800501e-15 * a.mantissa[2];
      }
      if (a.number_words >= 4) {
        aa += 2.117582368e-22 * a.mantissa[3];
      }

      b.sn(24 * a.exponent);
      b.sa(a.sign ? (aa < 0 ? -aa : aa) : (aa < 0 ? aa : -aa));
    }
  }

  static void mul(PreciseNumber a, PreciseNumber b, PreciseNumber c, int lnw) {
    int na = a.number_words < lnw ? a.number_words : lnw;

    if (na == 0) {
      c.number_words = 0;
      c.sign = true;
      c.exponent = 0;
      return;
    }

    int nb = b.number_words < lnw ? b.number_words : lnw;

    if (nb == 0) {
      c.number_words = 0;
      c.sign = true;
      c.exponent = 0;
      return;
    }

    int i = 0;
    int j = 0;
    double t1 = 0.0;
    double t2 = 0.0;

    if (na == 1 && a.mantissa[0] == 1) {
      c.sign = !(a.sign ^ b.sign);
      c.number_words = nb;
      c.exponent = a.exponent + b.exponent;

      //System.arraycopy(src, srcPos, dest, destPos, length);

      System.arraycopy(b.mantissa, 0, c.mantissa, 0, nb);
      return;
    } else if (nb == 1 && b.mantissa[0] == 1.0) {
      c.sign = !(a.sign ^ b.sign);
      c.number_words = na;
      c.exponent = a.exponent + b.exponent;

      System.arraycopy(a.mantissa, 0, c.mantissa, 0, na);
      return;
    }

    double d[] = new double[lnw + 4];
    int nc = Math.min(na + nb, lnw);

    System.arraycopy(new double[nc + 4], 0, d, 0, nc + 4);

    for (j = 3; j <= na + 2; j++) {
      t1 = a.mantissa[j - 3];
      int j3 = j - 3;
      int n2 = Math.min(nb + 2, lnw + 4 - j3);

      for (i = 2; i < n2; i++) {
        d[i + j3] += t1 * b.mantissa[i - 2];
      }

      if (((j - 2) % 32) == 0) {
        int i1 = Math.max(3, j - 32);
        int i2 = n2 + j3;

        for (i = i1 - 1; i < i2; i++) {
          t1 = d[i];
          t2 = (int)(5.9604644775390625e-8 * t1);
          d[i] = t1 - 1.6777216e7 * t2;
          d[i - 1] += t2;
        }
      }
    }

    double d2 = a.exponent + b.exponent;

    if (d[1] != 0.0) {
      d2 += 1.0;

      for (i = nc + 3; i >= 2; i--) {
        d[i] = d[i - 1];
      }
    }

    d[0] = a.sign ^ b.sign ? (nc < 0 ? nc : -nc) : (nc < 0 ? -nc : nc);
    d[1] = d2;

    mpnorm(d, c, lnw);
  }

  static void muld(PreciseNumber a, Chunk b, PreciseNumber c, int lnw) {
    int na = a.number_words < lnw ? a.number_words : lnw;

    if (na == 0 || b.ga() == 0.0) {
      c.number_words = 0;
      c.sign = true;
      c.exponent = 0;
      return;
    }

    int i = 0;
    int k = 0;
    double bb = 0.0;

    PreciseNumber f = new PreciseNumber(6, false);

    int ib = b.ga() >= 0 ? 1 : -1;

    int n1 = b.gn() / 24;
    int n2 = b.gn() - 24 * n1;
    bb = Math.abs(b.ga()) * Math.pow(2.0, n2);

    if (bb >= 1.6777216e7) {
      for (k = 1; k <= 100; k++) {
        bb = 5.9604644775390625e-8 * bb;

        if (bb < 1.6777216e7) {
          n1 += k;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
    } else if (bb < 1.0) {
      for (k = 1; k <= 100; k++) {
        bb = 1.6777216e7 * bb;

        if (bb >= 1.0) {
          n1 -= k;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
    }

    if (bb != (int)(bb)) {
      dmc(new Chunk(b.ga() >= 0 ? (bb < 0 ? -bb : bb) : (bb < 0 ? bb : -bb), n1 * 24), f);
      mul(f, a, c, lnw);
      return;
    }

    double d[] = new double[lnw + 4];

    // TODO/FIXME system.arraycopy

    // System.arraycopy(src, srcPos, dest, destPos, length);

    for (i = 2; i < na + 2; i++) {
      d[i] = bb * a.mantissa[i - 2];
    }

    d[0] = ((a.sign ? 1 : -1) * ib) >= 0 ? (na < 0 ? -na : na) : (na < 0 ? na : -na);
    d[1] = a.exponent + n1;
    d[na + 2] = 0.0;
    d[na + 3] = 0.0;

    mpnorm(d, c, lnw);
  }

  static void nint(PreciseNumber a, PreciseNumber b, int lnw) {
    int na = Math.min(a.number_words, lnw);

    if (na == 0) {
      b.number_words = 0;
      b.sign = true;
      b.exponent = 0;
      return;
    }

    if (a.exponent >= lnw) {
      throw new ArithmeticException("nint: Argument is too large --> " + a);
    }

    int i = 0;
    PreciseNumber f = new PreciseNumber(6, false);
    PreciseNumber s = new PreciseNumber(lnw + 2, false);

    f.number_words = 1;
    f.sign = true;
    f.exponent = -1;
    f.mantissa[0] = 8388608f;
    f.mantissa[1] = 0;

    if (a.sign) {
      _add(a, f, s, lnw);
    } else {
      _sub(a, f, s, lnw);
    }

    int nc = s.number_words;
    int mc = s.exponent;
    int nb = Math.min(mc > -1 ? (mc + 1) : 0, nc);

    if (nb == 0) {
      b.number_words = 0;
      b.sign = true;
      b.exponent = 0;
    } else {
      b.number_words = nb;
      b.sign = s.sign;
      b.exponent = mc;
      b.mantissa[nb] = 0;
      b.mantissa[nb + 1] = 0;

      System.arraycopy(s.mantissa, 0, b.mantissa, 0, nb);
    }
  }

  private static void mpnorm(double d[], PreciseNumber a, int lnw) {
    double t1 = 0.0;
    double t2 = 0.0;
    double t3 = 0.0;
    int i = 0;
    boolean ia = d[0] >= 0 ? true : false;
    int na = Math.min((int)(d[0] < 0 ? -d[0] : d[0]), lnw);

    if (na == 0) {
      a.number_words = 0;
      a.sign = true;
      a.exponent = 0;
      return;
    }

    int n4 = na + 4;
    double a2 = d[1];
    d[1] = 0.0;
    boolean needToNormalize = true;

    while (needToNormalize) {
      t1 = 0;

      for (i = n4 - 1; i >= 2; i--) {
        t3 = t1 + d[i];
        t2 = 5.9604644775390625e-8 * (t3);
        t1 = (int)(t2);

        if (t2 < 0.0 && t1 != t2) t1--;

        d[i] = t3 - t1 * 1.6777216e7;
      }
      d[1] += t1;

      if (d[1] < 0.0) {
        ia = !ia;
        d[2] += 1.6777216e7 * d[1];
        d[1] = 0.0;

        for (i = 1; i < n4; i++) {
          d[i] = -d[i];
        }
      } else if (d[1] > 0.0) {
        for (i = n4 - 2; i >= 1; i--) {
          a.mantissa[i - 1] = (float) d[i];
        }

        na = Math.min(na + 1, lnw);
        a2++;
        needToNormalize = false;
      } else {
        for (i = 2; i < n4; i++) {
          a.mantissa[i - 2] = (float) d[i];
        }
        needToNormalize = false;
      }
    }

    a.number_words = na;
    a.sign = ia;
    a.exponent = (int)(a2);

    round(a, lnw);
  }

  static void mpnpwr(PreciseNumber a, int n, PreciseNumber b, int lnw) {
    int j = 0;
    double t1 = 0.0;
    int lnw3 = lnw + 3;

    PreciseNumber f1 = new PreciseNumber(6, false);
    PreciseNumber sk0 = new PreciseNumber(lnw3, false);
    PreciseNumber sk1 = new PreciseNumber(lnw3, false);

    int na = Math.min(a.number_words, lnw);

    if (na == 0) {
      if (n >= 0) {
        b.number_words = 0;
        b.sign = true;
        b.exponent = 0;
        return;
      } else {
        throw new ArithmeticException("mpnpwr: Argument is zero and n is negative or zero --> " + a + "\n" + n);
      }
    }

    int nws = lnw;

    lnw++;

    int nn = Math.abs(n);
    f1.sign = true;
    f1.number_words = 1;
    f1.exponent = 0;
    f1.mantissa[0] = 1;
    f1.mantissa[1] = 0;
    boolean skip = false;

    switch (nn) {
      case 0: {
        _eq(f1, b, lnw);
        return;
      }
      case 1: {
        _eq(a, b, lnw);
        skip = true;
        // TODO/FIXME cannot be converted into scala
        break;
      }
      case 2: {
        mul(a, a, sk0, lnw);
        _eq(sk0, b, lnw);
        skip = true;
        // TODO/FIXME cannot be converted into scala
        break;
      }
    }

    if (!skip) {
      t1 = nn;
      int mn = (int)(1.4426950408889633 * Math.log(t1) + 1.0 + 5.6843418860808015e-14);

      _eq(f1, b, lnw);
      _eq(a, sk0, lnw);

      int kn = nn;

      for (j = 1; j <= mn; j++) {
        int kk = kn >> 1;

        if (kn != kk << 1) {
          mul(b, sk0, sk1, lnw);
          _eq(sk1, b, lnw);
        }
        kn = kk;
        if (j < mn) {
          mul(sk0, sk0, sk1, lnw);
          _eq(sk1, sk0, lnw);
        }
      }
    }

    if (n < 0) {
      mpdiv(f1, b, sk0, lnw);
      _eq(sk0, b, lnw);
    }

    round(b, nws);
  }

  static void round(PreciseNumber a, int lnw) {
    int i = 0;
    int a2 = a.exponent;
    a.exponent = 0;
    int na = a.number_words < lnw ? a.number_words : lnw;
    int n1 = na + 1;

    if (a.mantissa[0] == 0) {
      boolean allZero = true;

      for (i = 1; i <= n1; i++) {
        if (a.mantissa[i] != 0) {
          allZero = false;
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
      if (allZero) {
        a.number_words = 0;
        a.sign = true;
        a.exponent = 0;
        // TODO/FIXME cannot be converted into scala
        return;
      }

      int k = i;

      for (i = 0; i <= n1 - k; i++) {
        a.mantissa[i] = a.mantissa[i + k];
      }

      a2 -= k;
      na -= k > 2 ? (k - 2) : 0;
    }

    // TODO/FIXME make round = 1
    if (na == lnw && round >= 1) {
      if ((round == 1) && (a.mantissa[na] >= 8388608f) || (round == 2) && (a.mantissa[na] >= 1)) {
        a.mantissa[na - 1]++;
      }

      boolean no_stop = true;

      for (i = na - 1; i >= 0; i--) {
        if (a.mantissa[i] < 16777216f) {
          no_stop = false;
          // TODO/FIXME cannot be converted into scala
          break;
        }
        a.mantissa[i] -= 16777216f;

        if (i == 0) {
          a.exponent++;
        } else {
          a.mantissa[i - 1]++;
        }
      }

      if (no_stop) {
        a.mantissa[0] = (float) a.exponent;
        na = 1;
        a2++;
      }
    }

    try {
      if (a.mantissa[na - 1] == 0) {
        boolean allZero = true;

        for (i = na - 1; i >= 0; i--) {
          if (a.mantissa[i] != 0) {
            allZero = false;
            // TODO/FIXME cannot be converted into scala
            break;
          }
        }
        if (allZero) {
          a.number_words = 0;
          a.sign = true;
          a.exponent = 0;
          return;
        }
        na = i + 1;
      }
    } catch (ArrayIndexOutOfBoundsException e) {}

    if (a2 < -2.e6) {
      throw new ArithmeticException("round: Exponent underflow.");
    } else if (a2 > 2.e6) {
      throw new ArithmeticException("round: Exponent overflow.");
    }

    if (a.mantissa[0] == 0) {
      a.number_words = 0;
      a.sign = true;
      a.exponent = 0;
    } else {
      a.number_words = na;
      a.exponent = a2;
      a.mantissa[na] = 0;
      a.mantissa[na + 1] = 0;
    }
  }

  private static void _sub(PreciseNumber a, PreciseNumber b, PreciseNumber c, int lnw) {
    PreciseNumber bb = new PreciseNumber(0, false);

    bb.sign = !b.sign;
    bb.number_words = b.number_words;
    bb.exponent = b.exponent;
    bb.mantissa = b.mantissa;

    _add(a, bb, c, lnw);

    bb.mantissa = null;
  }

  // INFO dumps to storagble/readable format
  static int mpoutc(PreciseNumber a, char b[], int lnw) {
    // TODO/FIXME fix output ad normal decimal number

    int i = 0;
    int j = 0;
    int k = 0;
    int nn = 0;
    int n = 0;
    double aa = 0.0;
    double t1 = 0.0;
    int nw3 = lnw + 3;

    PreciseNumber f = new PreciseNumber(6, false);
    PreciseNumber sk0 = new PreciseNumber(nw3, false);
    PreciseNumber sk1 = new PreciseNumber(nw3, false);

    int na = Math.min(a.number_words, lnw);

    lnw++;

    f.sign = true;
    f.number_words = 1;
    f.exponent = 0;
    f.mantissa[0] = 10;
    int nx = 0;

    if (na != 0) {
      aa = a.mantissa[0];

      if (na >= 2) {
        aa += 5.9604644775390625e-8 * a.mantissa[1];
      }
      if (na >= 3) {
        aa += 3.552713678800501e-15 * a.mantissa[2];
      }
      if (na >= 4) {
        aa += 2.117582368e-22 * a.mantissa[3];
      }

      t1 = 7.2247198959 * a.exponent + (Math.log(aa) / 2.302585092994046);

      nx = (t1 >= 0.0) ? ((int) t1) : ((int)(t1 - 1.0));

      mpnpwr(f, nx, sk0, lnw);
      mpdiv(a, sk0, sk1, lnw);

      boolean no_stop = true;

      while (no_stop) {
        if (sk1.exponent < 0) {
          nx = nx - 1;

          muld(sk1, new Chunk(10.0, 0), sk0, lnw);
          _eq(sk0, sk1, lnw);
        } else if (sk1.mantissa[0] >= 10) {
          nx++;

          mpdivd(sk1, new Chunk(10.0, 0), sk0, lnw);
          _eq(sk0, sk1, lnw);
        } else {
          no_stop = false;
        }
      }
      sk1.sign = true;
    } else nx = 0;

    b[0] = '1';
    b[1] = '0';
    b[2] = ' ';
    b[3] = '^';
    char ca[] = String.valueOf(nx).toCharArray();
    int len = ca.length;
    int blank = 14 - len;

    for (i = 4; i < blank; i++) {
      b[i] = ' ';
    }
    for (i = 0; i < len; i++) {
      b[blank + i] = ca[i];
    }

    b[14] = ' ';
    b[15] = 'x';
    b[16] = ' ';
    b[17] = a.sign ? ' ' : '-';
    nn = (na != 0) ? ((int) sk1.mantissa[0]) : 0;
    ca = String.valueOf(nn).toCharArray();
    b[18] = ca[0];
    b[19] = '.';
    int ix = 20;

    if (na == 0) {
      b[ix] = '\0';
      return ix;
    }

    f.mantissa[0] = (float) nn;

    _sub(sk1, f, sk0, lnw);

    if (sk0.number_words == 0) {
      b[ix] = '\0';
      return ix;
    }

    muld(sk0, new Chunk(1e6, 0), sk1, lnw);

    int nl = (int)(Math.max(lnw * 0.2041199827, 1.0));
    boolean skip = false;

    for (j = 1; j <= nl; j++) {
      if (sk1.exponent == 0.0) {
        nn = (int)(sk1.mantissa[0]);
        f.number_words = 1;
        f.sign = true;
        f.mantissa[0] = (float) nn;
      } else {
        f.number_words = 0;
        f.sign = true;
        nn = 0;
      }

      ca = String.valueOf(nn).toCharArray();

      for (i = 0; i < 6 - ca.length; i++) {
        b[i + ix] = '0';
      }

      k = 0;

      for (; i < 6; i++) {
        b[i + ix] = ca[k++];
      }

      ix += 6;

      _sub(sk1, f, sk0, lnw);
      muld(sk0, new Chunk(1e6, 0), sk1, lnw);

      if (sk1.number_words == 0) {
        skip = true;
        // TODO/FIXME cannot be converted into scala
        break;
      }
    }

    if (!skip) {
      j = nl + 1;
    }

    int l = --ix;

    if (b[l] == '0' || (j > nl && b[l - 1] == '0' && b[l - 2] == '0' && b[l - 3] == '0')) {
      b[l] = '\0';
      boolean loopbreak = false;

      for (i = l - 1; i >= 20; i--) {
        if (b[i] != '0') {
          ix = i;
          loopbreak = true;
          // TODO/FIXME cannot be converted into scala
          break;
        }
        b[i] = '\0';
      }

      if (!loopbreak) {
        ix = 20;
      }
    } else if (j > nl && b[l - 1] == '9' && b[l - 2] == '9' && b[l - 3] == '9') {
      b[l] = '\0';
      skip = false;

      for (i = l - 1; i >= 20; i--) {
        if (b[i] != '9') {
          skip = true;
          // TODO/FIXME cannot be converted into scala
          break;
        }
        b[i] = '\0';
      }

      if (!skip) {
        ix = 20;

        if (b[18] == '9') {
          b[18] = '1';
          // TODO/FIXME avoid creating String parse smarter
          ca = String.valueOf(nx + 1).toCharArray();
          k = 0;

          for (i = 0; i < 10 - ca.length; i++) {
            b[i + 4] = ' ';
          }
          for (; i < 10; i++) {
            b[i + 4] = ca[k++];
          }
        } else {
          ca[0] = b[18];
          ca[1] = '\0';
          // TODO/FIXME avoid creating String parse smarter
          nn = Integer.parseInt(new String(ca, 0, 1));
          // TODO/FIXME avoid creating String parse smarter
          ca = String.valueOf(nn + 1).toCharArray();
          b[18] = ca[0];
        }
      } else {
        ca[0] = b[i];
        ca[1] = '\0';
        // TODO/FIXME avoid creating String parse smarter
        nn = Integer.parseInt(new String(ca, 0, 1));
        ca = String.valueOf(nn + 1).toCharArray();
        b[i] = ca[0];
        ix = i;
      }
    }

    n = ix;
    b[++n] = '\0';

    return n;
  }

  public String toString() {
    // TODO/FIXME problem with exponent

    // TODO/FIXME byte array
    StringBuffer res = new StringBuffer();
    char az[] = new char[precision_digits_current];

    int nd = mpoutc(this, az, nw);
    int i = 0;

    // TODO/FIXME system.arraycopy

    // System.arraycopy(src, srcPos, dest, destPos, length);
    for (i = 0; i < nd; i++) {
      res.append(az[i]);
    }

    String old = res.toString();

    int xx = old.indexOf("x");

    String exponent = old.substring(old.indexOf("^") + 1, xx).trim();

    //TODO/FIXME Long.parseLong ???
    int ex = Integer.parseInt(exponent);

    //System.out.println(">>>> ex position at " + ex);

    return old.substring(xx + 1).trim() + (ex != 0 ? ("e" + ((ex < 0) ? "" : "+") + exponent) : "");

  }

  static void fromString(char a[], int n, PreciseNumber b, int lnw) {
    // TODO fixme use byte instead of char array here

    int i = 0;
    int j = 0;
    int is = 0;
    int id = 0;
    double bi = 0.0;
    char ai = 0;
    char ca[] = new char[81];
    int nw3 = lnw + 3;

    PreciseNumber f = new PreciseNumber(6, false);
    PreciseNumber sk0 = new PreciseNumber(nw3, false);
    PreciseNumber sk1 = new PreciseNumber(nw3, false);
    PreciseNumber sk2 = new PreciseNumber(nw3, false);

    int nws = lnw++;
    int i1 = 0;
    int nn = 0;

    for (i = 0; i < n; i++) {
      ai = a[i];
      if (ai == '.' || ai == '+' || ai == '-') {
        // TODO/FIXME cannot be converted into scala
        break;
      }
    }

    for (j = 0; j < 81; j++) {
      ca[j] = '\0';
    }

    boolean exit = true;

    for (i = i1; i < n; i++) {
      if (a[i] != ' ') {
        exit = false;
        // TODO/FIXME cannot be converted into scala
        break;
      }
    }

    if (exit) {
      throw new NumberFormatException("inp_complex: Syntax error in literal string.");
    }

    i1 = i;

    if (a[i1] == '+') {
      i1++;
      is = 1;
    } else if (a[i1] == '-') {
      i1 = i1 + 1;
      is = -1;
    } else {
      is = 1;
    }

    int ib = 0;
    id = 0;
    int ip = 0;

    sk2.number_words = 0;
    sk2.sign = true;
    sk2.exponent = 0;

    f.number_words = 1;
    f.sign = true;
    f.exponent = 0;

    int it = 0;
    int mm = 0;
    boolean no_stop = true;

    while (no_stop) {
      ip = 0;

      for (mm = 0; mm < 6; mm++) {
        ca[mm] = '0';
      }

      for (i = i1; i < n; i++) {
        ai = a[i];

        if (ai == '.') {
          if (ip != 0) throw new NumberFormatException("inp_complex: Syntax error in literal string.");
          ip = id;
        } else if (ai != ' ' && !Character.isDigit(ai)) {
          throw new NumberFormatException("inp_complex: Syntax error in literal string.");
        } else if (ai != ' ') {
          id++;
          ca[ib++] = ai;
        }
        
        if (ib == 6 || i == (n - 1) && ib != 0) {
          if (it != 0) {
            ca[ib] = '\0';
            bi = Integer.parseInt(new String(ca, 0, ib));

            muld(sk2, new Chunk(1e6, 0), sk0, lnw);

            if (bi != 0) {
              f.number_words = 1;
              f.sign = true;
              f.mantissa[0] = (float) bi;
            } else {
              f.number_words = 0;
              f.sign = true;
            }

            _add(sk0, f, sk2, lnw);

            for (mm = 0; mm < 6; mm++) {
              ca[mm] = '0';
            }
          }
          if ((i + 1) != n) {
            ib = 0;
          }
        }
      }

      if (it == 0) {
        ib = 6 - ib;

        if (ib == 6) {
          ib = 0;
        }
        it = 1;
      } else {
        no_stop = false;
      }
    }

    if (is == -1) {
      sk2.sign = !sk2.sign;
    }

    if (ip == 0) {
      ip = id;
    }

    nn += ip - id;
    f.number_words = 1;
    f.sign = true;
    f.mantissa[0] = 10;

    mpnpwr(f, nn, sk0, lnw);
    mul(sk2, sk0, sk1, lnw);
    _eq(sk1, b, lnw);
  }

  static void mpfftcr(int is, int m, int n, Complex x[], double y[]) {
    int k = 0;
    final Complex pointFive = new Complex(0.5, 0.0);
    final Complex zeroOne = new Complex(0.0, 1.0);
    int mx = (int) uu1[0].real();

    if ((is != 1 && is != -1) || m < 3 || m > mx) {
      throw new ArithmeticException("mpfftcr: Either the UU arrays have not been initialized or one of the input parameters is invalid: " + is + "\t" + m + "\t" + mx);
    }

    Complex dc1[] = new Complex[n >> 1];
    Complex a1 = null;
    Complex a2 = null;
    Complex x1 = null;
    Complex x2 = null;
    int n1 = (int)(Math.pow(2, m >> 1));
    int n2 = n >> 1;
    int n4 = n >> 2;
    dc1[0] = pointFive.multiply(new Complex((x[0].add(x[n2])).real(), (x[0].subtract(x[n2])).real()));
    
    dc1[n4] = (is == 1) ? x[n4].conjg() : new Complex(x[n4].real(), x[n4].aimag());
    int ku = n2;

    if (is == 1) {
      for (k = 1; k < n4; k++) {
        x1 = x[k];
        x2 = x[n2 - k].conjg();
        a1 = x1.add(x2);
        a2 = zeroOne.multiply(uu1[k + ku]).multiply(x1.subtract(x2));
        dc1[k] = pointFive.multiply(a1.add(a2));
        dc1[n2 - k] = pointFive.multiply(a1.subtract(a2).conjg());
      } 
    } else {
      for (k = 1; k < n4; k++) {
        x1 = x[k];
        x2 = x[n2 - k].conjg();
        a1 = x1.add(x2);
        a2 = zeroOne.multiply(uu1[k + ku].conjg()).multiply(x1.subtract(x2));
        dc1[k] = pointFive.multiply(a1.add(a2));
        dc1[n2 - k] = pointFive.multiply(a1.subtract(a2).conjg());
      }
    }

    mpfft1(is, m - 1, n1, n2 / n1, dc1, x);

    for (k = 0; k < n >> 1; k++) {
      y[k << 1] = dc1[k].real();
      y[(k << 1) + 1] = dc1[k].aimag();
    }
  }

  //DEPENDS ON mpfft1
  static void mpfftrc(int is, int m, int n, double x[], Complex y[]) {
    int k = 0;
    int mx = (int) uu1[0].real();

    if ((is != 1 && is != -1) || m < 3 || m > mx) {
      throw new ArithmeticException("mpfftrc: Either the UU arrays have not been initialized or one of the input parameters is invalid: " + is + "\t" + m + "\t" + mx);
    }

    Complex dc1[] = new Complex[n >> 1];
    Complex a1 = null;
    Complex a2 = null;
    Complex z1 = null;
    Complex z2 = null;

    /*
      INFO

      m -> int
      result -> int

      implement faster using int aritmetics hacks
    */
    int n1 = (int) Math.pow(2, m >> 1);
    int n2 = n >> 1;
    int n4 = n >> 2;

    for (k = 0; k < n2; k++) {
      dc1[k] = new Complex(x[k << 1], x[(k << 1) + 1]);
    }

    mpfft1(is, m - 1, n1, n2 / n1, dc1, y);

    y[0] = new Complex(2.0 * (dc1[0].real() + dc1[0].aimag()), 0.0);
    y[n4] = ((is == 1) ? (dc1[n4]) : ((dc1[n4].conjg()))).multiply(new Complex(2.0, 0.0));
    y[n2] = new Complex(2.0 * (dc1[0].real() - dc1[0].aimag()), 0.0);
    int ku = n2;
    final Complex zeroMinOne = new Complex(0.0, -1.0);

    if (is == 1) {
      for (k = 1; k < n4; k++) {
        z1 = dc1[k];
        z2 = dc1[n2 - k].conjg();
        a1 = z1.add(z2);
        a2 = zeroMinOne.multiply(uu1[k + ku]).multiply(z1.subtract(z2));
        y[k] = a1.add(a2);
        y[n2 - k] = a1.subtract(a2).conjg();
      }
    } else {
      for (k = 1; k < n4; k++) {
        z1 = dc1[k];
        z2 = dc1[n2 - k].conjg();
        a1 = z1.add(z2);
        a2 = zeroMinOne.multiply(uu1[k + ku].conjg()).multiply(z1.subtract(z2));
        y[k] = a1.add(a2);
        y[n2 - k] = a1.subtract(a2).conjg();
      }
    }
  }

  //DEPENDS ON mpfft2
  static void mpfft1(int is, int m, int n1, int n2, Complex x[], Complex y[]) {
    int i = 0;
    int j = 0;
    int k = 0;
    Complex z1[][] = new Complex[18][n1];
    Complex z2[][] = new Complex[18][n1];
    int yrow = n2 + 2;
    int m1 = (m + 1) >> 1;
    int m2 = m - m1;

    int nr1 = n1 < 16 ? n1 : 16;
    int nr2 = n2 < 16 ? n2 : 16;

    int ku = (int) uu2[m - 1].real();

    for (i = 0; i < n1; i += nr1) {
      for (k = 0; k < nr1; k++) {
        for (j = 0; j < n2; j++) {
          z1[k][j] = x[j * n1 + i + k];
        }
      }

      mpfft2(is, nr1, m2, n2, z1, z2);

      int iu = i + ku - n1 - 1;

      if (is == 1) {
        for (k = 0; k < nr1; k++) {
          for (j = 0; j < n2; j++) {
            y[(i + k) * yrow + j] = uu2[iu + k + (j + 1) * n1].multiply(z1[k][j]);
          }
        }
      } else {
        for (k = 0; k < nr1; k++) {
          for (j = 0; j < n2; j++) {
            y[(i + k) * yrow + j] = uu2[iu + k + (j + 1) * n1].conjg().multiply(z1[k][j]);
          }
        }
      }
    }

    for (i = 0; i < n2; i += nr2) {
      for (k = 0; k < nr2; k++) {
        for (j = 0; j < n1; j++) {
          z2[k][j] = y[j * yrow + i + k];
        }
      }

      mpfft2(is, nr2, m1, n1, z2, z1);

      if ((m % 2) == 0) {
        for (k = 0; k < nr2; k++) {
          for (j = 0; j < n1; j++) {
            x[i + k + j * n1] = z2[k][j];
          }
        }
      } else {
        int j2 = 0;

        for (j = 0; j < n1 >> 1; j++) {
          j2 = (j << 1);

          for (k = 0; k < nr2; k++) {
            x[i + k + j * n1] = z2[k][j2];
            x[i + k + n2 + n1 * j] = z2[k][j2 + 1];
          }
        }
      }
    }
  }

  static void mpfft2(int is, int ns, int m, int n, Complex x[][], Complex y[][]) {
    int l = 0;
    int j = 0;
    int i = 0;

    for (l = 1; l <= m; l += 2) {
      mpfft3(is, l, ns, m, n, x, y);

      if (l == m) {
        for (i = 0; i < ns; i++) {
          for (j = 0; j < n; j++) {
            x[i][j] = y[i][j];
          }
        }
        return;
      }

      mpfft3(is, l + 1, ns, m, n, y, x);
    }
  }

  static void mpfft3(int is, int l, int ns, int m, int n, Complex x[][], Complex y[][]) {
    Complex u1 = null;
    Complex x1 = null;
    Complex x2 = null;
    int i = 0;
    int j = 0;
    int k = 0;
    int n1 = n >> 1;

    /*
      TODO/FIXME
      implement faster using int aritmetics hacks
    */
    int lk = (int) Math.pow(2, (l - 1));
    /*
      TODO/FIXME
      implement faster using int aritmetics hacks
    */
    int li = (int) Math.pow(2, (m - l));
    int lj = lk << 1;
    int ku = li;
    int i11 = 0;
    int i12 = 0;
    int i21 = 0;
    int i22 = 0;

    for (i = 0; i <= li - 1; i++) {
      i11 = i * lk + 1;
      i12 = i11 + n1;
      i21 = i * lj + 1;
      i22 = i21 + lk;
      u1 = is == 1 ? uu1[i + ku] : uu1[i + ku].conjg();

      for (k = -1; k < lk - 1; k++) {
        for (j = 0; j < ns; j++) {
          x1 = x[j][i11 + k];
          x2 = x[j][i12 + k];
          y[j][i21 + k] = x1.add(x2);
          y[j][i22 + k] = u1.multiply(x1.subtract(x2));
        }
      }
    }
  }

  //DEPENDS on mpfftcr
  static void mplconv(int iq, int n, int nsq, double a[], double b[], double c[]) {
    int i = 0;
    int j = 0;
    int k = 0;
    double an = 0.0;
    double t1 = 0.0;
    double t2 = 0.0;
    int ncr1 = 64;
    int n1 = 0;
    int n2 = 0;

    if (n < ncr1) {
      switch (iq) {
        case 1: {
          for (k = 0; k < (n << 1); k++) {
            t1 = 0.0;
            n1 = k - n + 2;
            n1 = n1 < 1 ? 1 : n1;
            n2 = (k + 1) < n ? k + 1 : n;

            for (j = n1 - 1; j < n2; j++) {
              t1 += a[j] * a[k - j];
            }

            c[k] = t1;
          }
          // TODO/FIXME cannot be converted into scala
          break;
        }

        case 2: {
          for (k = 0; k < (n << 1); k++) {
            t1 = 0.0;
            n1 = k - n + 2;
            n1 = n1 < 1 ? 1 : n1;
            n2 = (k + 1) < n ? k + 1 : n;

            for (j = n1 - 1; j < n2; j++) {
              t1 += a[j] * b[k - j];
            }

            c[k] = t1;
          }
          // TODO/FIXME cannot be converted into scala
          break;
        }

        case -1: {
          for (k = 0; k < n - 1; k++) {
            c[k] = 0.0;
          }
          for (k = n - 1; k < (n << 1); k++) {
            t1 = 0.0;
            n1 = k - n + 2;
            n2 = n;

            for (j = n1 - 1; j < n2; j++) {
              t1 += a[j] * a[k - j];
            }

            c[k] = t1;
          }
          // TODO/FIXME cannot be converted into scala
          break;
        }

        case -2: {
          for (k = 0; k < n - 1; k++) {
            c[k] = 0.0;
          }
          for (k = n - 1; k < (n << 1); k++) {
            t1 = 0.0;
            n1 = k - n + 2;
            n2 = n;

            for (j = n1 - 1; j < n2; j++) {
              t1 += a[j] * b[k - j];
            }

            c[k] = t1;
          }
          // TODO/FIXME cannot be converted into scala
          break;
        }
      }
      return;
    }

    int _d = 3 * n + 2;
    int _dc = 3 * (n >> 1) + (nsq << 1) + 3;

    double d1[] = new double[_d];
    double d2[] = new double[_d];
    double d3[] = new double[_d];
    Complex dc1[] = new Complex[_dc];
    Complex dc2[] = new Complex[_dc];

    int m1 = (int)(1.4426950408889633 * Math.log(0.75 * n) + 1.0 - 5.6843418860808015e-14);
    int m2 = m1 + 1;

    int pow = 2;

    while (m1 > 0) {
      if ((m1 & 1) == 0) {
        pow *= pow;
        m1 >>>= 1;
      } else {
        n1 *= pow;
        m1--;
      }
    }

    int n4 = n1 << 2;
    int nm = (n < n1 ? n : n1) << 1;

    if (iq < 0 ? iq == -1 : iq == 1) {
      System.arraycopy(a, 0, d1, 0, n);
      System.arraycopy(new double[n2], 0, d1, n, n2);

      mpfftrc(1, m2, n2, d1, dc1);

      for (i = 0; i < n1 + 1; i++) {
        dc1[i] = dc1[i].multiply(dc1[i]);
      }
    } else {
      for (i = 0; i < n; i++) {
        d1[i] = a[i];
        d2[i] = b[i];
      }

      for (i = n; i < n2; i++) {
        d1[i] = 0.0;
        d2[i] = 0.0;
      }

      mpfftrc(1, m2, n2, d1, dc1);
      mpfftrc(1, m2, n2, d2, dc2);

      for (i = 0; i <= n1; i++) {
        dc1[i] = dc1[i].multiply(dc2[i]);
      }
    }

    mpfftcr(-1, m2, n2, dc1, d3);

    an = 1.0 / n4;

    for (i = 0; i < nm; i++) {
      t1 = an * d3[i];
      t2 = t1 < 0 ? Math.ceil(t1 - 0.50) : Math.floor(t1 + 0.50);
      c[i] = t2;
    }

    int m = 0;
    int m21 = 0;
    int ms = 0;

    if (n > n1) {
      m = n - n1;
      m2 = (m << 1);
      m21 = m2 - 1;
      ms = (int)(Math.sqrt(3.0 * m21) + 5.6843418860808015e-14);
      k = n1 - m + 1;

      if (iq < 0 ? iq == -1 : iq == 1) {
        System.arraycopy(a, k, d1, 0, m21);
        mplconv(-1, m21, ms, d1, d2, d3);
      } else {
        System.arraycopy(a, k, d1, 0, m21);
        System.arraycopy(a, k, d2, 0, m21);
        mplconv(-2, m21, ms, d1, d2, d3);
      }

      int ii;

      for (i = 0; i < m2; i++) {
        ii = i + m2 - 2;
        c[i] -= d3[ii];
        c[i + n2] = d3[ii];
      }
    }
  }

  // INFO division operation
  static void _div(PreciseNumber a, PreciseNumber b, PreciseNumber c, int lnw) {
    int nb = b.number_words > lnw ? lnw : b.number_words;

    if (nb == 0) {
      throw new ArithmeticException("_div: Divisor is zero");
    }

    if (nb <= 128) {
      mpdiv(a, b, c, lnw);
      return;
    }

    int k = 0;
    int _sk = lnw + 3;

    PreciseNumber f = new PreciseNumber(6, false);
    PreciseNumber sk0 = new PreciseNumber(_sk, false);
    PreciseNumber sk1 = new PreciseNumber(_sk, false);
    PreciseNumber sk2 = new PreciseNumber(_sk, false);

    int nws = lnw;

    int mq = (int)(1.4426950408889633 * Math.log((double)lnw) + 1.0 - 5.6843418860808015e-14);
    lnw = 129;
    f.number_words = 1;
    f.sign = true;
    f.exponent = 0;
    f.mantissa[0] = 1;
    f.mantissa[1] = 0;

    mpdiv(f, b, c, lnw);

    int iq = 0;
    int nw1 = 0;
    int nw2 = 0;

    for (k = 8; k <= mq - 1; k++) {
      nw1 = lnw;
      // TODO/FIXME use ternar
      lnw = Math.min((lnw << 1) - 2, nws) + 1;
      nw2 = lnw;
      boolean no_stop = true;

      while (no_stop) {
        _mul(b, c, sk0, lnw);
        _sub(f, sk0, sk1, lnw);
        lnw = nw1;
        _mul(c, sk1, sk0, lnw);
        lnw = nw2;
        _add(c, sk0, sk1, lnw);
        _eq(sk1, c, lnw);

        if (k == mq - 3 && iq == 0) {
          iq = 1;
        } else {
          no_stop = false;
        }
      }
    }

    _mul(a, c, sk0, lnw);

    nw1 = lnw;
    // TODO/FIXME use ternar
    lnw = Math.min((lnw << 1) - 2, nws) + 1;
    nw2 = lnw;

    _mul(sk0, b, sk1, lnw);
    _sub(a, sk1, sk2, lnw);

    lnw = nw1;

    _mul(sk2, c, sk1, lnw);

    lnw = nw2;

    _add(sk0, sk1, sk2, lnw);
    _eq(sk2, c, lnw);
    round(c, nws);
  }

  // INFO multiplication operation
  // DEPENDS ON mplconv
  static void _mul(PreciseNumber a, PreciseNumber b, PreciseNumber c, int lnw) {
    int na = a.number_words > lnw ? lnw : a.number_words;

    if (na <= 128) {
      mul(a, b, c, lnw);
      return;
    }

    int nb = b.number_words > lnw ? lnw : b.number_words;

    if (nb <= 128) {
      mul(a, b, c, lnw);
      return;
    }

    double t1 = 0.0;
    double t2 = 0.0;
    double t3 = 0.0;
    double t4 = 0.0;
    int i = 0;

    int _d1 = (lnw + 2) << 1;

    double d1[] = new double[_d1];
    double d2[] = new double[_d1];
    double d3[] = new double[(lnw + 3) << 2];
    int i2 = 0;

    for (i = 0; i < na; i++) {
      i2 = i << 1;
      t1 = a.mantissa[i];
      t2 = (int)(2.44140625e-4 * t1);
      d1[i2] = t2;
      d1[i2 + 1] = t1 - 4096.0 * t2;
    }

    System.arraycopy(new double[nb << 1], na << 1, d1, 0, nb << 1);

    for (i = 0; i < nb; i++) {
      i2 = i << 1;
      t1 = b.mantissa[i];
      t2 = (int)(2.44140625e-4 * t1);
      d2[i2] = t2;
      d2[i2 + 1] = t1 - 4096.0 * t2;
    }

    System.arraycopy(new double[nb << 1], na << 1, d2, 0, nb << 1);

    int nn = na < nb ? (nb << 1) : (na << 1);
    int nx = (int)(Math.sqrt(3.0 * nn) + 5.6843418860808015e-14);

    mplconv(2, nn, nx, d1, d2, d3);

    int nc = (na + nb < lnw) ? (na + nb) : lnw;
    int nc1 = ((lnw + 1) < (na + nb - 1)) ? (lnw + 1) : (na + nb - 1);

    d1[0] = a.sign ^ b.sign ? (nc < 0 ? nc : -nc) : (nc < 0 ? -nc : nc);
    d1[1] = a.exponent + b.exponent + 1;
    d1[2] = d3[0];
    d1[nc + 2] = 0.0;
    d1[nc + 3] = 0.0;

    for (i = 1; i <= nc1; i++) {
      i2 = i << 1;
      t3 = d3[i2 - 1];
      t4 = d3[i2];
      t1 = (int)(5.9604644775390625e-8 * t3);
      t2 = t3 - 1.6777216e7 * t1;
      t3 = (int)(5.9604644775390625e-8 * t4);
      t4 -= 1.6777216e7 * t3;
      d1[i + 2] = 4096.0 * t2 + t4;
      d1[i + 1] += 4096.0 * t1 + t3;
    }
    mpnorm(d1, c, lnw);
  }

  /////// public API

  public PreciseNumber add(PreciseNumber r) {
    PreciseNumber res = new PreciseNumber(true, precision_digits);
    _add(this, r, res, nw);
    return res;
  }

  public PreciseNumber subtract(PreciseNumber r) {
    PreciseNumber res = new PreciseNumber(true, precision_digits);
    _sub(this, r, res, nw);
    return res;
  }

  public PreciseNumber negate() {
    PreciseNumber res = new PreciseNumber(true, precision_digits);
    _eq(this, res, nw);
    res.sign = !this.sign;
    return res;
  }

  public PreciseNumber multiply(PreciseNumber r) {
    PreciseNumber res = new PreciseNumber(true, precision_digits);
    _mul(this, r, res, nw);
    return res;
  }

  public PreciseNumber divide(PreciseNumber r) {
    PreciseNumber res = new PreciseNumber(true, precision_digits);
    _div(this, r, res, nw);
    return res;
  }

}