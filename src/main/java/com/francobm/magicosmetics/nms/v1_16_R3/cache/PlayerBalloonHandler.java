package com.francobm.magicosmetics.nms.v1_16_R3.cache;

import com.francobm.magicosmetics.cache.RotationType;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EntityPufferFish;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_16_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.minecraft.server.v1_16_R3.Vector3f;
import net.minecraft.server.v1_16_R3.World;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerBalloonHandler extends PlayerBalloon {
  private final EntityArmorStand armorStand;
  
  private final EntityLiving leashed;
  
  private final double distance;
  
  private final double SQUARED_WALKING;
  
  private final double SQUARED_DISTANCE;
  
  private final double CATCH_UP_INCREMENTS = 0.27D;
  
  private double CATCH_UP_INCREMENTS_DISTANCE;
  
  public PlayerBalloonHandler(Player p, double space, double distance, boolean bigHead, boolean invisibleLeash) {
    this.CATCH_UP_INCREMENTS = 0.27D;
    this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
    this.viewers = new CopyOnWriteArrayList(new ArrayList());
    this.hideViewers = new CopyOnWriteArrayList(new ArrayList());
    this.uuid = p.getUniqueId();
    this.distance = distance;
    this.invisibleLeash = invisibleLeash;
    playerBalloons.put(this.uuid, this);
    Player player = getPlayer();
    WorldServer world = ((CraftWorld)player.getWorld()).getHandle();
    Location location = getPlayer().getLocation().clone().add(0.0D, space, 0.0D);
    location = location.clone().add(getPlayer().getLocation().getDirection().multiply(-1));
    this.armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, (World)world);
    this.armorStand.setInvulnerable(true);
    this.armorStand.setInvisible(true);
    this.armorStand.setMarker(true);
    this.armorStand.setPositionRotation(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch());
    this.bigHead = bigHead;
    if (isBigHead())
      this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX(), 0.0F, 0.0F)); 
    this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.PUFFERFISH, (World)world);
    this.leashed.setInvulnerable(true);
    this.leashed.setInvisible(true);
    this.leashed.setSilent(true);
    this.leashed.collides = false;
    this.leashed.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    ((EntityInsentient)this.leashed).setNoAI(true);
    this.space = space;
    this.SQUARED_WALKING = 5.5D * space;
    this.SQUARED_DISTANCE = 10.0D * space;
  }
  
  public void spawn(Player player) {
    if (this.hideViewers.contains(player.getUniqueId()))
      return; 
    Player owner = getPlayer();
    if (owner == null)
      return; 
    if (this.viewers.contains(player.getUniqueId())) {
      if (!owner.getWorld().equals(player.getWorld())) {
        remove(player);
        return;
      } 
      if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance)
        remove(player); 
      return;
    } 
    if (!owner.getWorld().equals(player.getWorld()))
      return; 
    if (owner.getLocation().distanceSquared(player.getLocation()) > this.distance)
      return; 
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
    connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand));
    connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    connection.sendPacket((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed));
    connection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.leashed.getId(), this.leashed.getDataWatcher(), true));
    if (!this.invisibleLeash)
      connection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle())); 
    this.viewers.add(player.getUniqueId());
  }
  
  public void spawn(boolean exception) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (exception && player.getUniqueId().equals(this.uuid))
        continue; 
      spawn(player);
    } 
  }
  
  public void remove() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!this.viewers.contains(player.getUniqueId()))
        continue; 
      remove(player);
    } 
    playerBalloons.remove(this.uuid);
  }
  
  public void remove(Player player) {
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
    connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.getId() }));
    connection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.getId() }));
    this.viewers.remove(player.getUniqueId());
  }
  
  public void setItem(ItemStack itemStack) {
    if (isBigHead()) {
      setItemBigHead(itemStack);
      return;
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null)
        continue; 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
      ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
      list.add(new Pair(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack)));
      connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list));
    } 
  }
  
  public void setItemBigHead(ItemStack itemStack) {
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
      ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
      list.add(new Pair(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(itemStack)));
      connection.sendPacket((Packet)new PacketPlayOutEntityEquipment(this.armorStand.getId(), list));
    } 
  }
  
  public void lookEntity(float yaw, float pitch) {
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null)
        continue; 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).playerConnection;
      connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
      connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
      connection.sendPacket((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F)));
      connection.sendPacket((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.getId(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
    } 
  }
  
  protected void teleport(Location location) {
    Location newLocation = location.add(0.0D, this.space, 0.0D);
    this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
  }
  
  protected void instantUpdate() {
    Player owner = getPlayer();
    if (owner == null)
      return; 
    if (this.armorStand == null)
      return; 
    if (this.leashed == null)
      return; 
    if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
      spawn(false);
      return;
    } 
    Location playerLoc = owner.getLocation().clone();
    Location stand = this.leashed.getBukkitEntity().getLocation().clone();
    Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
    if (!standDir.equals(new Vector()))
      standDir.normalize(); 
    Location standToLoc = playerLoc.setDirection(standDir.setY(0));
    if (!this.floatLoop) {
      this.y += 0.01D;
      standToLoc.add(0.0D, 0.01D, 0.0D);
      if (this.y > 0.1D)
        this.floatLoop = true; 
    } else {
      this.y -= 0.01D;
      standToLoc.subtract(0.0D, 0.01D, 0.0D);
      if (this.y < -0.11D) {
        this.floatLoop = false;
        this.rotate *= -1.0F;
      } 
    } 
    teleport(standToLoc);
    if (!this.rotateLoop) {
      this.rot += 0.02D;
      this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
      if (this.rot > 0.2D)
        this.rotateLoop = true; 
    } else {
      this.rot -= 0.02D;
      this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
      if (this.rot < -0.2D)
        this.rotateLoop = false; 
    } 
    if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ()));
      if (this.height < -0.1D)
        this.heightLoop = false; 
      return;
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      EntityPlayer p = ((CraftPlayer)player).getHandle();
      if (!this.invisibleLeash)
        p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (Entity)((CraftPlayer)owner).getHandle())); 
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
    } 
  }
  
  public void update(boolean instantFollow) {
    if (isBigHead()) {
      updateBigHead();
      return;
    } 
    if (instantFollow) {
      instantUpdate();
      return;
    } 
    Player owner = getPlayer();
    if (this.armorStand == null)
      return; 
    if (this.leashed == null)
      return; 
    if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
      spawn(false);
      return;
    } 
    Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
    Location stand = this.leashed.getBukkitEntity().getLocation();
    Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
    Location distance2 = stand.clone();
    Location distance1 = owner.getLocation().clone();
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
      if (!standDir.equals(new Vector()))
        standDir.normalize(); 
      Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
      Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
      Location newLocation = standTo.clone();
      this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    } else {
      Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
      if (!standDir.equals(new Vector()))
        standDir.normalize(); 
      Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
      double distY = distVec.getY();
      if (owner.isSneaking())
        distY -= 0.13D; 
      Location standToLoc = stand.clone().setDirection(standDir.setY(0)).add(0.0D, distY, 0.0D);
      if (!this.floatLoop) {
        this.y += 0.01D;
        standToLoc.add(0.0D, 0.01D, 0.0D);
        if (this.y > 0.1D)
          this.floatLoop = true; 
      } else {
        this.y -= 0.01D;
        standToLoc.subtract(0.0D, 0.01D, 0.0D);
        if (this.y < -0.11D) {
          this.floatLoop = false;
          this.rotate *= -1.0F;
        } 
      } 
      if (!this.rotateLoop) {
        this.rot += 0.01D;
        this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
        if (this.rot > 0.2D)
          this.rotateLoop = true; 
      } else {
        this.rot -= 0.01D;
        this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.5F, this.armorStand.r().getY(), this.armorStand.r().getZ() + this.rotate));
        if (this.rot < -0.2D)
          this.rotateLoop = false; 
      } 
      Location newLocation = standToLoc.clone();
      this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null)
        continue; 
      EntityPlayer p = ((CraftPlayer)player).getHandle();
      if (!this.invisibleLeash)
        p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle())); 
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      if (!this.heightLoop) {
        this.height += 0.01D;
        this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() - 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ()));
        if (this.height > 0.1D)
          this.heightLoop = true; 
      } 
    } else if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + 0.8F, this.armorStand.r().getY(), this.armorStand.r().getZ()));
      if (this.height < -0.1D)
        this.heightLoop = false; 
      return;
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
      this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
    } else {
      this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
    } 
  }
  
  public void updateBigHead() {
    Player owner = getPlayer();
    if (this.armorStand == null)
      return; 
    if (this.leashed == null)
      return; 
    if (!owner.getWorld().equals(this.leashed.getBukkitEntity().getWorld())) {
      spawn(false);
      return;
    } 
    Location playerLoc = owner.getLocation().clone().add(0.0D, this.space, 0.0D);
    Location stand = this.leashed.getBukkitEntity().getLocation();
    Vector standDir = owner.getEyeLocation().clone().subtract(stand).toVector();
    Location distance2 = stand.clone();
    Location distance1 = owner.getLocation().clone();
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      Vector lineBetween = playerLoc.clone().subtract(stand).toVector();
      if (!standDir.equals(new Vector()))
        standDir.normalize(); 
      Vector distVec = lineBetween.clone().normalize().multiply(this.CATCH_UP_INCREMENTS_DISTANCE);
      Location standTo = stand.clone().setDirection(standDir.setY(0)).add(distVec.clone());
      Location newLocation = standTo.clone();
      this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    } else {
      if (!standDir.equals(new Vector()))
        standDir.normalize(); 
      Location standToLoc = stand.clone().setDirection(standDir.setY(0));
      if (!this.floatLoop) {
        this.y += 0.01D;
        standToLoc.add(0.0D, 0.01D, 0.0D);
        if (this.y > 0.1D)
          this.floatLoop = true; 
      } else {
        this.y -= 0.01D;
        standToLoc.subtract(0.0D, 0.01D, 0.0D);
        if (this.y < -0.11D) {
          this.floatLoop = false;
          this.rotate *= -1.0F;
        } 
      } 
      if (!this.rotateLoop) {
        this.rot += 0.01D;
        this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() - 0.5F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ() + this.rotate));
        if (this.rot > 0.2D)
          this.rotateLoop = true; 
      } else {
        this.rot -= 0.01D;
        this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + 0.5F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ() + this.rotate));
        if (this.rot < -0.2D)
          this.rotateLoop = false; 
      } 
      Location newLocation = standToLoc.clone();
      this.leashed.setLocation(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.setLocation(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      EntityPlayer p = ((CraftPlayer)player).getHandle();
      if (!this.invisibleLeash)
        p.playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle())); 
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      if (!this.heightLoop) {
        this.height += 0.01D;
        this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() - 0.8F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
        if (this.height > 0.1D)
          this.heightLoop = true; 
      } 
    } else if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + 0.8F, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
      if (this.height < -0.1D)
        this.heightLoop = false; 
      return;
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_DISTANCE) {
      this.CATCH_UP_INCREMENTS_DISTANCE += 0.01D;
    } else {
      this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
    } 
  }
  
  public void rotate(boolean rotation, RotationType rotationType, float rotate) {
    if (isBigHead()) {
      rotateBigHead(rotation, rotationType, rotate);
      return;
    } 
    if (!rotation)
      return; 
    switch (rotationType) {
      case RIGHT:
        this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX(), this.armorStand.r().getY() + rotate, this.armorStand.r().getZ()));
        break;
      case UP:
        this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + rotate, this.armorStand.r().getY(), this.armorStand.r().getZ()));
        break;
      case ALL:
        this.armorStand.setHeadPose(new Vector3f(this.armorStand.r().getX() + rotate, this.armorStand.r().getY() + rotate, this.armorStand.r().getZ()));
        break;
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null)
        continue; 
      (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    } 
  }
  
  public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
    if (!rotation)
      return; 
    switch (rotationType) {
      case RIGHT:
        this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX(), this.armorStand.rightArmPose.getY() + rotate, this.armorStand.rightArmPose.getZ()));
        break;
      case UP:
        this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + rotate, this.armorStand.rightArmPose.getY(), this.armorStand.rightArmPose.getZ()));
        break;
      case ALL:
        this.armorStand.setRightArmPose(new Vector3f(this.armorStand.rightArmPose.getX() + rotate, this.armorStand.rightArmPose.getY() + rotate, this.armorStand.rightArmPose.getZ()));
        break;
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null)
        continue; 
      (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(this.armorStand.getId(), this.armorStand.getDataWatcher(), true));
    } 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */