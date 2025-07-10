package com.francobm.magicosmetics.nms.v1_18_R1.cache;

import com.francobm.magicosmetics.cache.RotationType;
import com.francobm.magicosmetics.nms.balloon.PlayerBalloon;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.core.Vector3f;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutAttachEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.animal.EntityPufferFish;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
    Location location = player.getLocation().clone().add(0.0D, space, 0.0D);
    location = location.clone().add(player.getLocation().clone().getDirection().multiply(-1));
    this.armorStand = new EntityArmorStand(EntityTypes.c, (World)world);
    this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch());
    this.armorStand.j(true);
    this.armorStand.m(true);
    this.armorStand.t(true);
    this.bigHead = bigHead;
    if (isBigHead())
      this.armorStand.d(new Vector3f(this.armorStand.cj.b(), 0.0F, 0.0F)); 
    this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.at, (World)world);
    this.leashed.collides = false;
    this.leashed.j(true);
    this.leashed.m(true);
    this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
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
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
    connection.a((Packet)new PacketPlayOutSpawnEntityLiving((EntityLiving)this.armorStand));
    connection.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
    connection.a((Packet)new PacketPlayOutSpawnEntityLiving(this.leashed));
    if (!this.invisibleLeash)
      connection.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle())); 
    connection.a((Packet)new PacketPlayOutEntityMetadata(this.leashed.ae(), this.leashed.ai(), true));
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
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      remove(player);
    } 
    playerBalloons.remove(this.uuid);
  }
  
  public void remove(Player player) {
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
    connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.ae() }));
    connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { this.leashed.ae() }));
    this.viewers.remove(player.getUniqueId());
  }
  
  public void setItem(ItemStack itemStack) {
    if (isBigHead()) {
      setItemBigHead(itemStack);
      return;
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
      ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
      list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
      connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list));
    } 
  }
  
  public void setItemBigHead(ItemStack itemStack) {
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
      ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
      list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
      connection.a((Packet)new PacketPlayOutEntityEquipment(this.armorStand.ae(), list));
    } 
  }
  
  public void lookEntity(float yaw, float pitch) {
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
      connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
      connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
      connection.a((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F)));
      connection.a((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.ae(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
    } 
  }
  
  protected void teleport(Location location) {
    Location newLocation = location.add(0.0D, this.space, 0.0D);
    this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
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
      this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
      if (this.rot > 0.2D)
        this.rotateLoop = true; 
    } else {
      this.rot -= 0.02D;
      this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
      if (this.rot < -0.2D)
        this.rotateLoop = false; 
    } 
    if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
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
        p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle())); 
      p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
      p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
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
      this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
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
        this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
        if (this.rot > 0.2D)
          this.rotateLoop = true; 
      } else {
        this.rot -= 0.01D;
        this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.5F, this.armorStand.u().c(), this.armorStand.u().d() + this.rotate));
        if (this.rot < -0.2D)
          this.rotateLoop = false; 
      } 
      Location newLocation = standToLoc.clone();
      this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      EntityPlayer p = ((CraftPlayer)player).getHandle();
      if (!this.invisibleLeash)
        p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle())); 
      p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
      p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      if (!this.heightLoop) {
        this.height += 0.01D;
        this.armorStand.a(new Vector3f(this.armorStand.u().b() - 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
        if (this.height > 0.1D)
          this.heightLoop = true; 
      } 
    } else if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.a(new Vector3f(this.armorStand.u().b() + 0.8F, this.armorStand.u().c(), this.armorStand.u().d()));
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
      this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
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
        this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
        if (this.rot > 0.2D)
          this.rotateLoop = true; 
      } else {
        this.rot -= 0.01D;
        this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.5F, this.armorStand.cj.c(), this.armorStand.cj.d() + this.rotate));
        if (this.rot < -0.2D)
          this.rotateLoop = false; 
      } 
      Location newLocation = standToLoc.clone();
      this.leashed.a(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.a(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      EntityPlayer p = ((CraftPlayer)player).getHandle();
      if (!this.invisibleLeash)
        p.b.a((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, (this.lendEntity == null) ? (Entity)((CraftPlayer)owner).getHandle() : (Entity)((CraftLivingEntity)this.lendEntity).getHandle())); 
      p.b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
      p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.b.a((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      if (!this.heightLoop) {
        this.height += 0.01D;
        this.armorStand.d(new Vector3f(this.armorStand.cj.b() - 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
        if (this.height > 0.1D)
          this.heightLoop = true; 
      } 
    } else if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.d(new Vector3f(this.armorStand.cj.b() + 0.8F, this.armorStand.cj.c(), this.armorStand.cj.d()));
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
        this.armorStand.a(new Vector3f(this.armorStand.u().b(), this.armorStand.u().c() + rotate, this.armorStand.u().d()));
        break;
      case UP:
        this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c(), this.armorStand.u().d()));
        break;
      case ALL:
        this.armorStand.a(new Vector3f(this.armorStand.u().b() + rotate, this.armorStand.u().c() + rotate, this.armorStand.u().d()));
        break;
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
    } 
  }
  
  public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
    if (!rotation)
      return; 
    switch (rotationType) {
      case RIGHT:
        this.armorStand.d(new Vector3f(this.armorStand.cj.b(), this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
        break;
      case UP:
        this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c(), this.armorStand.cj.d()));
        break;
      case ALL:
        this.armorStand.d(new Vector3f(this.armorStand.cj.b() + rotate, this.armorStand.cj.c() + rotate, this.armorStand.cj.d()));
        break;
    } 
    for (UUID uuid : this.viewers) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.viewers.remove(uuid);
        continue;
      } 
      (((CraftPlayer)player).getHandle()).b.a((Packet)new PacketPlayOutEntityMetadata(this.armorStand.ae(), this.armorStand.ai(), true));
    } 
  }
  
  public double getDistance() {
    return this.distance;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\cache\PlayerBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */