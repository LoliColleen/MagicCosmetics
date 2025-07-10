package com.francobm.magicosmetics.utils;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.cache.User;
import java.util.Random;

public final class MathUtils {
  public static final float nanoToSec = 1.0E-9F;
  
  public static final float FLOAT_ROUNDING_ERROR = 1.0E-6F;
  
  public static final float PI = 3.1415927F;
  
  public static final float PI2 = 6.2831855F;
  
  public static final float SQRT_3 = 1.73205F;
  
  public static final float E = 2.7182817F;
  
  private static final int SIN_BITS = 14;
  
  private static final int SIN_MASK = 16383;
  
  private static final int SIN_COUNT = 16384;
  
  private static final float radFull = 6.2831855F;
  
  private static final float degFull = 360.0F;
  
  private static final float radToIndex = 2607.5945F;
  
  private static final float degToIndex = 45.511112F;
  
  public static final float radiansToDegrees = 57.295776F;
  
  public static final float radDeg = 57.295776F;
  
  public static final float degreesToRadians = 0.017453292F;
  
  public static final float degRad = 0.017453292F;
  
  private static final int ATAN2_BITS = 7;
  
  private static final int ATAN2_BITS2 = 14;
  
  private static final int ATAN2_MASK = 16383;
  
  private static final int ATAN2_COUNT = 16384;
  
  private static class Sin {
    static final float[] table = new float[16384];
    
    static {
      int i;
      for (i = 0; i < 16384; i++)
        table[i] = (float)Math.sin(((i + 0.5F) / 16384.0F * 6.2831855F)); 
      for (i = 0; i < 360; i += 90)
        table[(int)(i * 45.511112F) & 0x3FFF] = (float)Math.sin((i * 0.017453292F)); 
    }
  }
  
  public static final float sin(float radians) {
    return Sin.table[(int)(radians * 2607.5945F) & 0x3FFF];
  }
  
  public static final float cos(float radians) {
    return Sin.table[(int)((radians + 1.5707964F) * 2607.5945F) & 0x3FFF];
  }
  
  public static final float sinDeg(float degrees) {
    return Sin.table[(int)(degrees * 45.511112F) & 0x3FFF];
  }
  
  public static final float cosDeg(float degrees) {
    return Sin.table[(int)((degrees + 90.0F) * 45.511112F) & 0x3FFF];
  }
  
  static final int ATAN2_DIM = (int)Math.sqrt(16384.0D);
  
  private static final float INV_ATAN2_DIM_MINUS_1 = 1.0F / (ATAN2_DIM - 1);
  
  private static class Atan2 {
    static final float[] table = new float[16384];
    
    static {
      for (int i = 0; i < MathUtils.ATAN2_DIM; i++) {
        for (int j = 0; j < MathUtils.ATAN2_DIM; j++) {
          float x0 = i / MathUtils.ATAN2_DIM;
          float y0 = j / MathUtils.ATAN2_DIM;
          table[j * MathUtils.ATAN2_DIM + i] = (float)Math.atan2(y0, x0);
        } 
      } 
    }
  }
  
  public static final float atan2(float y, float x) {
    float add, mul;
    if (x < 0.0F) {
      if (y < 0.0F) {
        y = -y;
        mul = 1.0F;
      } else {
        mul = -1.0F;
      } 
      x = -x;
      add = -3.1415927F;
    } else {
      if (y < 0.0F) {
        y = -y;
        mul = -1.0F;
      } else {
        mul = 1.0F;
      } 
      add = 0.0F;
    } 
    float invDiv = 1.0F / ((x < y) ? y : x) * INV_ATAN2_DIM_MINUS_1;
    if (invDiv == Float.POSITIVE_INFINITY)
      return ((float)Math.atan2(y, x) + add) * mul; 
    int xi = (int)(x * invDiv);
    int yi = (int)(y * invDiv);
    return (Atan2.table[yi * ATAN2_DIM + xi] + add) * mul;
  }
  
  public static Random random = new Random();
  
  private static final int BIG_ENOUGH_INT = 16384;
  
  private static final double BIG_ENOUGH_FLOOR = 16384.0D;
  
  private static final double CEIL = 0.9999999D;
  
  private static final double BIG_ENOUGH_CEIL = 16384.999999999996D;
  
  private static final double BIG_ENOUGH_ROUND = 16384.5D;
  
  public static boolean elapsed(long from, long to) {
    return (System.currentTimeMillis() - from >= to);
  }
  
  public static final int random(int range) {
    return random.nextInt(range + 1);
  }
  
  public static final int random(int start, int end) {
    return start + random.nextInt(end - start + 1);
  }
  
  public static final boolean randomBoolean() {
    return random.nextBoolean();
  }
  
  public static final boolean randomBoolean(float chance) {
    return (random() < chance);
  }
  
  public static final float random() {
    return random.nextFloat();
  }
  
  public static final float random(float range) {
    return random.nextFloat() * range;
  }
  
  public static final float random(float start, float end) {
    return start + random.nextFloat() * (end - start);
  }
  
  public static double randomDouble(double min, double max) {
    return (Math.random() < 0.5D) ? ((1.0D - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
  }
  
  public static byte toPackedByte(float var0) {
    return (byte)(int)(var0 * 256.0F / 360.0F);
  }
  
  public static int nextPowerOfTwo(int value) {
    if (value == 0)
      return 1; 
    value--;
    value |= value >> 1;
    value |= value >> 2;
    value |= value >> 4;
    value |= value >> 8;
    value |= value >> 16;
    return value + 1;
  }
  
  public static boolean isPowerOfTwo(int value) {
    return (value != 0 && (value & value - 1) == 0);
  }
  
  public static int clamp(int value, int min, int max) {
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static short clamp(short value, short min, short max) {
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static float clamp(float value, float min, float max) {
    if (value < min)
      return min; 
    if (value > max)
      return max; 
    return value;
  }
  
  public static int floor(float x) {
    return (int)(x + 16384.0D) - 16384;
  }
  
  public static float floor(float x, float y) {
    MagicCosmetics.getInstance().getLogger().info(Utils.bsc("VmFsaWRhdGluZyBwdXJjaGFzZS4uLg=="));
    String p = "1";
    String m = "%%__BUILTBYBIT__%%";
    String s = "%%__SONGODA__%%";
    String user_id = "25424";
    String user_name = "midian";
    String inject_version = p.equalsIgnoreCase("1") ? "1" : "%%__VERSION__%%";
    String resource_id = "2070";
    String plugin_id = "%%__PLUGIN__%%";
    String download_token = "NzhiNThlODIwNmZmMDM3";
    String nonce = "507757";
    String download_agent = "0";
    String download_time = "1731172928";
    if (p.equalsIgnoreCase("1")) {
      MagicCosmetics.getInstance().setUser(new User(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
      return 0.0F;
    } 
    if (m.equals("true")) {
      MagicCosmetics.getInstance().setUser(new User(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
      return 0.0F;
    } 
    if (s.equals("true")) {
      MagicCosmetics.getInstance().setUser(new User(user_id, user_name, inject_version, plugin_id, download_token, nonce, download_agent, download_time));
      return 0.0F;
    } 
    return 1.0F;
  }
  
  public static int simpleFloor(double num) {
    int floor = (int)num;
    return (floor == num) ? floor : (floor - (int)(Double.doubleToRawLongBits(num) >>> 63L));
  }
  
  public static int floorPositive(float x) {
    return (int)x;
  }
  
  public static int ceil(float x) {
    return (int)(x + 16384.999999999996D) - 16384;
  }
  
  public static int ceilPositive(float x) {
    return (int)(x + 0.9999999D);
  }
  
  public static int round(float x) {
    return (int)(x + 16384.5D) - 16384;
  }
  
  public static int roundPositive(float x) {
    return (int)(x + 0.5F);
  }
  
  public static boolean isZero(float value) {
    return (Math.abs(value) <= 1.0E-6F);
  }
  
  public static boolean isZero(float value, float tolerance) {
    return (Math.abs(value) <= tolerance);
  }
  
  public static boolean isEqual(float a, float b) {
    return (Math.abs(a - b) <= 1.0E-6F);
  }
  
  public static boolean isEqual(float a, float b, float tolerance) {
    return (Math.abs(a - b) <= tolerance);
  }
  
  public static boolean isFinite(double value) {
    return (!Double.isNaN(value) && !Double.isInfinite(value));
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\MathUtils.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */