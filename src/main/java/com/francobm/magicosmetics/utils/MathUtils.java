/*     */ package com.francobm.magicosmetics.utils;
/*     */ 
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.User;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MathUtils
/*     */ {
/*     */   public static final float nanoToSec = 1.0E-9F;
/*     */   public static final float FLOAT_ROUNDING_ERROR = 1.0E-6F;
/*     */   public static final float PI = 3.1415927F;
/*     */   public static final float PI2 = 6.2831855F;
/*     */   public static final float SQRT_3 = 1.73205F;
/*     */   public static final float E = 2.7182817F;
/*     */   private static final int SIN_BITS = 14;
/*     */   private static final int SIN_MASK = 16383;
/*     */   private static final int SIN_COUNT = 16384;
/*     */   private static final float radFull = 6.2831855F;
/*     */   private static final float degFull = 360.0F;
/*     */   private static final float radToIndex = 2607.5945F;
/*     */   private static final float degToIndex = 45.511112F;
/*     */   public static final float radiansToDegrees = 57.295776F;
/*     */   public static final float radDeg = 57.295776F;
/*     */   public static final float degreesToRadians = 0.017453292F;
/*     */   public static final float degRad = 0.017453292F;
/*     */   private static final int ATAN2_BITS = 7;
/*     */   private static final int ATAN2_BITS2 = 14;
/*     */   private static final int ATAN2_MASK = 16383;
/*     */   private static final int ATAN2_COUNT = 16384;
/*     */   
/*     */   private static class Sin
/*     */   {
/*  75 */     static final float[] table = new float[16384];
/*     */     static {
/*     */       int i;
/*  78 */       for (i = 0; i < 16384; i++) {
/*  79 */         table[i] = (float)Math.sin(((i + 0.5F) / 16384.0F * 6.2831855F));
/*     */       }
/*  81 */       for (i = 0; i < 360; i += 90) {
/*  82 */         table[(int)(i * 45.511112F) & 0x3FFF] = (float)Math.sin((i * 0.017453292F));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float sin(float radians) {
/*  91 */     return Sin.table[(int)(radians * 2607.5945F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float cos(float radians) {
/*  98 */     return Sin.table[(int)((radians + 1.5707964F) * 2607.5945F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float sinDeg(float degrees) {
/* 105 */     return Sin.table[(int)(degrees * 45.511112F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float cosDeg(float degrees) {
/* 112 */     return Sin.table[(int)((degrees + 90.0F) * 45.511112F) & 0x3FFF];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   static final int ATAN2_DIM = (int)Math.sqrt(16384.0D);
/* 121 */   private static final float INV_ATAN2_DIM_MINUS_1 = 1.0F / (ATAN2_DIM - 1);
/*     */   
/*     */   private static class Atan2
/*     */   {
/* 125 */     static final float[] table = new float[16384];
/*     */     
/*     */     static {
/* 128 */       for (int i = 0; i < MathUtils.ATAN2_DIM; i++) {
/* 129 */         for (int j = 0; j < MathUtils.ATAN2_DIM; j++) {
/* 130 */           float x0 = i / MathUtils.ATAN2_DIM;
/* 131 */           float y0 = j / MathUtils.ATAN2_DIM;
/* 132 */           table[j * MathUtils.ATAN2_DIM + i] = (float)Math.atan2(y0, x0);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float atan2(float y, float x) {
/*     */     float add, mul;
/* 143 */     if (x < 0.0F) {
/* 144 */       if (y < 0.0F) {
/* 145 */         y = -y;
/* 146 */         mul = 1.0F;
/*     */       } else {
/* 148 */         mul = -1.0F;
/*     */       } 
/* 150 */       x = -x;
/* 151 */       add = -3.1415927F;
/*     */     } else {
/* 153 */       if (y < 0.0F) {
/* 154 */         y = -y;
/* 155 */         mul = -1.0F;
/*     */       } else {
/* 157 */         mul = 1.0F;
/*     */       } 
/* 159 */       add = 0.0F;
/*     */     } 
/* 161 */     float invDiv = 1.0F / ((x < y) ? y : x) * INV_ATAN2_DIM_MINUS_1;
/*     */     
/* 163 */     if (invDiv == Float.POSITIVE_INFINITY) {
/* 164 */       return ((float)Math.atan2(y, x) + add) * mul;
/*     */     }
/*     */     
/* 167 */     int xi = (int)(x * invDiv);
/* 168 */     int yi = (int)(y * invDiv);
/* 169 */     return (Atan2.table[yi * ATAN2_DIM + xi] + add) * mul;
/*     */   }
/*     */ 
/*     */   
/* 173 */   public static Random random = new Random(); private static final int BIG_ENOUGH_INT = 16384;
/*     */   private static final double BIG_ENOUGH_FLOOR = 16384.0D;
/*     */   
/*     */   public static boolean elapsed(long from, long to) {
/* 177 */     return (System.currentTimeMillis() - from >= to);
/*     */   }
/*     */   
/*     */   private static final double CEIL = 0.9999999D;
/*     */   private static final double BIG_ENOUGH_CEIL = 16384.999999999996D;
/*     */   private static final double BIG_ENOUGH_ROUND = 16384.5D;
/*     */   
/*     */   public static final int random(int range) {
/* 185 */     return random.nextInt(range + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int random(int start, int end) {
/* 192 */     return start + random.nextInt(end - start + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean randomBoolean() {
/* 199 */     return random.nextBoolean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean randomBoolean(float chance) {
/* 206 */     return (random() < chance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float random() {
/* 213 */     return random.nextFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float random(float range) {
/* 220 */     return random.nextFloat() * range;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float random(float start, float end) {
/* 227 */     return start + random.nextFloat() * (end - start);
/*     */   }
/*     */   
/*     */   public static double randomDouble(double min, double max) {
/* 231 */     return (Math.random() < 0.5D) ? ((1.0D - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
/*     */   }
/*     */   
/*     */   public static byte toPackedByte(float var0) {
/* 235 */     return (byte)(int)(var0 * 256.0F / 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nextPowerOfTwo(int value) {
/* 243 */     if (value == 0) {
/* 244 */       return 1;
/*     */     }
/* 246 */     value--;
/* 247 */     value |= value >> 1;
/* 248 */     value |= value >> 2;
/* 249 */     value |= value >> 4;
/* 250 */     value |= value >> 8;
/* 251 */     value |= value >> 16;
/* 252 */     return value + 1;
/*     */   }
/*     */   
/*     */   public static boolean isPowerOfTwo(int value) {
/* 256 */     return (value != 0 && (value & value - 1) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int clamp(int value, int min, int max) {
/* 261 */     if (value < min) {
/* 262 */       return min;
/*     */     }
/* 264 */     if (value > max) {
/* 265 */       return max;
/*     */     }
/* 267 */     return value;
/*     */   }
/*     */   
/*     */   public static short clamp(short value, short min, short max) {
/* 271 */     if (value < min) {
/* 272 */       return min;
/*     */     }
/* 274 */     if (value > max) {
/* 275 */       return max;
/*     */     }
/* 277 */     return value;
/*     */   }
/*     */   
/*     */   public static float clamp(float value, float min, float max) {
/* 281 */     if (value < min) {
/* 282 */       return min;
/*     */     }
/* 284 */     if (value > max) {
/* 285 */       return max;
/*     */     }
/* 287 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int floor(float x) {
/* 304 */     return (int)(x + 16384.0D) - 16384;
/*     */   }
/*     */   
/*     */   public static float floor(float x, float y) {
/* 308 */     MagicCosmetics.getInstance().getLogger().info(Utils.bsc("VmFsaWRhdGluZyBwdXJjaGFzZS4uLg=="));
/* 309 */     String p = "1";
/* 310 */     String m = "%%__BUILTBYBIT__%%";
/* 311 */     String s = "%%__SONGODA__%%";
/* 312 */     String user_id = "25424";
/* 313 */     String user_name = "midian";
/* 314 */     String inject_version = p.equalsIgnoreCase("1") ? "1" : "%%__VERSION__%%";
/* 315 */     String resource_id = "2070";
/* 316 */     String plugin_id = "%%__PLUGIN__%%";
/* 317 */     String download_token = "NzhiNThlODIwNmZmMDM3";
/* 318 */     String nonce = "507757";
/* 319 */     String download_agent = "0";
/* 320 */     String download_time = "1731172928";
/* 321 */     if (p.equalsIgnoreCase("1")) {
/* 322 */       MagicCosmetics.getInstance().setUser(new User(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
/* 323 */       return 0.0F;
/*     */     } 
/* 325 */     if (m.equals("true")) {
/* 326 */       MagicCosmetics.getInstance().setUser(new User(user_id, user_name, inject_version, resource_id, download_token, nonce, download_agent, download_time));
/* 327 */       return 0.0F;
/*     */     } 
/* 329 */     if (s.equals("true")) {
/* 330 */       MagicCosmetics.getInstance().setUser(new User(user_id, user_name, inject_version, plugin_id, download_token, nonce, download_agent, download_time));
/* 331 */       return 0.0F;
/*     */     } 
/* 333 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public static int simpleFloor(double num) {
/* 337 */     int floor = (int)num;
/* 338 */     return (floor == num) ? floor : (floor - (int)(Double.doubleToRawLongBits(num) >>> 63L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int floorPositive(float x) {
/* 346 */     return (int)x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ceil(float x) {
/* 354 */     return (int)(x + 16384.999999999996D) - 16384;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ceilPositive(float x) {
/* 362 */     return (int)(x + 0.9999999D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int round(float x) {
/* 370 */     return (int)(x + 16384.5D) - 16384;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int roundPositive(float x) {
/* 377 */     return (int)(x + 0.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isZero(float value) {
/* 384 */     return (Math.abs(value) <= 1.0E-6F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isZero(float value, float tolerance) {
/* 393 */     return (Math.abs(value) <= tolerance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqual(float a, float b) {
/* 403 */     return (Math.abs(a - b) <= 1.0E-6F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqual(float a, float b, float tolerance) {
/* 414 */     return (Math.abs(a - b) <= tolerance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFinite(double value) {
/* 421 */     return (!Double.isNaN(value) && !Double.isInfinite(value));
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\MathUtils.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */