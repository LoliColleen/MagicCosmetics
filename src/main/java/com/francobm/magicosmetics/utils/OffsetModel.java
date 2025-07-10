package com.francobm.magicosmetics.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class OffsetModel {
  private final double x;
  
  private final double y;
  
  private final double z;
  
  private final float yaw;
  
  private final float pitch;
  
  public OffsetModel(Location location, float yaw, float pitch) {
    this(location.getX(), location.getY(), location.getZ(), yaw, pitch);
  }
  
  public OffsetModel(double x, double y, double z, float yaw, float pitch) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }
  
  public double getX() {
    return this.x;
  }
  
  public double getY() {
    return this.y;
  }
  
  public double getZ() {
    return this.z;
  }
  
  public float getYaw() {
    return this.yaw;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public Vector getBukkitVector() {
    return new Vector(this.x, this.y, this.z);
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetic\\utils\OffsetModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */