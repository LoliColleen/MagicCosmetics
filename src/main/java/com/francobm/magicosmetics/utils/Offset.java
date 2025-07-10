package com.francobm.magicosmetics.utils;

import org.bukkit.util.Vector;

public class Offset {
  public static Vector rotateYaw(Vector vec, double yaw) {
    double sin = Math.sin(yaw);
    double cos = Math.cos(yaw);
    double x = vec.getX() * cos - vec.getZ() * sin;
    double z = vec.getX() * sin + vec.getZ() * cos;
    return vec.setX(x).setZ(z);
  }
  
  public static Vector rotatePitch(Vector vec, double pitch) {
    double sin = Math.sin(pitch);
    double cos = Math.cos(pitch);
    double y = vec.getY() * cos - vec.getZ() * sin;
    double z = vec.getY() * sin + vec.getZ() * cos;
    return vec.setY(y).setZ(z);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\Offset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */