package com.gitee.usl.grammar.utils;

import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.type.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Properties;

/**
 * Some helper methods.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class Utils {
  private static final String CURRENT_VERSION = "5.0.0";


  private Utils() {

  }

  private static final ThreadLocal<MessageDigest> MESSAGE_DIGEST_LOCAL =
      new ThreadLocal<MessageDigest>() {

        @Override
        protected MessageDigest initialValue() {
          try {
            return MessageDigest.getInstance("md5");
          } catch (Exception e) {
            throw Reflector.sneakyThrow(e);
          }
        }

      };

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(final byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static String md5sum(final String s) {
    MessageDigest md = MESSAGE_DIGEST_LOCAL.get();
    md.reset();
    byte[] messageDigest = md.digest(s.getBytes());
    return bytesToHex(messageDigest);
  }

  public static String readFully(final Reader reader) throws IOException {
    final char[] arr = new char[16 * 1024];
    final StringBuilder buf = new StringBuilder();
    int numChars;

    while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
      buf.append(arr, 0, numChars);
    }

    return buf.toString();
  }

  public static AviatorNumber exponent(final Number base, final Number exp,
                                       final Map<String, Object> env) {
    final int expInt = exp.intValue();
    if (base instanceof BigInteger) {
      return new AviatorBigInt(((BigInteger) base).pow(expInt));
    } else if (base instanceof BigDecimal) {
      return new AviatorDecimal(((BigDecimal) base).pow(expInt, RuntimeUtils.getMathContext(env)));
    } else {
      final double ret = Math.pow(base.doubleValue(), exp.doubleValue());
      if (TypeUtils.isDouble(base) || TypeUtils.isDouble(exp) || exp.doubleValue() < 0) {
        return new AviatorDouble(ret);
      } else {
        return AviatorLong.valueOf((long) ret);
      }
    }
  }

  public static String getAviatorScriptVersion() {
    Properties prop = new Properties();
    InputStream in = null;
    try {
      in = Utils.class
          .getResourceAsStream("/META-INF/maven/com.googlecode.aviator/aviator/pom.properties");
      prop.load(in);
      return prop.getProperty("version", CURRENT_VERSION);
    } catch (Throwable e) {
      // ignore
    } finally {
      try {
        in.close();
      } catch (Exception ex) {
      }
    }
    return CURRENT_VERSION;
  }

  public static boolean isAndroid() {
    try {
      Class.forName("android.os.Build");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
