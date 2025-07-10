package com.francobm.magicosmetics.nms.v1_20_R4.cache;

import com.francobm.magicosmetics.cache.RotationType;
import com.francobm.magicosmetics.nms.balloon.EntityBalloon;
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
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
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
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EntityBalloonHandler extends EntityBalloon {
  private final EntityArmorStand armorStand;
  
  private final EntityLiving leashed;
  
  private final double distance;
  
  private final double SQUARED_WALKING;
  
  private final double SQUARED_DISTANCE;
  
  private final double CATCH_UP_INCREMENTS = 0.27D;
  
  private double CATCH_UP_INCREMENTS_DISTANCE;
  
  public EntityBalloonHandler(Entity entity, double space, double distance, boolean bigHead, boolean invisibleLeash) {
    this.CATCH_UP_INCREMENTS = 0.27D;
    this.CATCH_UP_INCREMENTS_DISTANCE = 0.27D;
    this.players = new CopyOnWriteArrayList(new ArrayList());
    this.uuid = entity.getUniqueId();
    this.distance = distance;
    this.invisibleLeash = invisibleLeash;
    entitiesBalloon.put(this.uuid, this);
    this.entity = (LivingEntity)entity;
    WorldServer world = ((CraftWorld)entity.getWorld()).getHandle();
    Location location = entity.getLocation().clone().add(0.0D, space, 0.0D);
    location = location.clone().add(entity.getLocation().clone().getDirection().multiply(-1));
    this.armorStand = new EntityArmorStand(EntityTypes.d, (World)world);
    this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch());
    this.armorStand.k(true);
    this.armorStand.n(true);
    this.armorStand.u(true);
    this.bigHead = bigHead;
    if (isBigHead())
      this.armorStand.d(new Vector3f(this.armorStand.D().b(), 0.0F, 0.0F)); 
    this.leashed = (EntityLiving)new EntityPufferFish(EntityTypes.aF, (World)world);
    this.leashed.collides = false;
    this.leashed.k(true);
    this.leashed.n(true);
    this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    this.space = space;
    this.SQUARED_WALKING = 5.5D * space;
    this.SQUARED_DISTANCE = 10.0D * space;
  }
  
  public void spawn(Player player) {
    if (this.players.contains(player.getUniqueId())) {
      if (!getEntity().getWorld().equals(player.getWorld())) {
        remove(player);
        return;
      } 
      if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
        remove(player); 
      return;
    } 
    if (!getEntity().getWorld().equals(player.getWorld()))
      return; 
    if (getEntity().getLocation().distanceSquared(player.getLocation()) > this.distance)
      return; 
    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
    entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.armorStand));
    entityPlayer.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.al(), this.armorStand.ap().c()));
    entityPlayer.c.b((Packet)new PacketPlayOutSpawnEntity((Entity)this.leashed));
    entityPlayer.c.b((Packet)new PacketPlayOutEntityMetadata(this.leashed.al(), this.leashed.ap().c()));
    if (!this.invisibleLeash)
      entityPlayer.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle())); 
    this.players.add(player.getUniqueId());
  }
  
  public void spawn(boolean exception) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (exception && player.getUniqueId().equals(this.uuid))
        continue; 
      spawn(player);
    } 
  }
  
  public void remove() {
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      remove(player);
    } 
    entitiesBalloon.remove(this.uuid);
  }
  
  public void remove(Player player) {
    PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
    connection.b((Packet)new PacketPlayOutEntityDestroy(new int[] { this.armorStand.al(), this.leashed.al() }));
    this.players.remove(player.getUniqueId());
  }
  
  public void setItem(ItemStack itemStack) {
    if (isBigHead()) {
      setItemBigHead(itemStack);
      return;
    } 
    for (UUID uuid : this.players) {
      ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
      list.add(new Pair(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemStack)));
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
      connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.al(), list));
    } 
  }
  
  public void setItemBigHead(ItemStack itemStack) {
    ArrayList<Pair<EnumItemSlot, ItemStack>> list = new ArrayList<>();
    list.add(new Pair(EnumItemSlot.a, CraftItemStack.asNMSCopy(itemStack)));
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
      connection.b((Packet)new PacketPlayOutEntityEquipment(this.armorStand.al(), list));
    } 
  }
  
  public void lookEntity() {
    float yaw = getEntity().getLocation().getYaw();
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      PlayerConnection connection = (((CraftPlayer)player).getHandle()).c;
      connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.armorStand, (byte)(int)(yaw * 256.0F / 360.0F)));
      connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.armorStand.al(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
      connection.b((Packet)new PacketPlayOutEntityHeadRotation((Entity)this.leashed, (byte)(int)(yaw * 256.0F / 360.0F)));
      connection.b((Packet)new PacketPlayOutEntity.PacketPlayOutEntityLook(this.leashed.al(), (byte)(int)(yaw * 256.0F / 360.0F), (byte)0, true));
    } 
  }
  
  protected void teleport(Location location) {
    this.leashed.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    this.armorStand.b(location.getX(), location.getY() - 1.3D, location.getZ(), location.getYaw(), location.getPitch());
  }
  
  public void update() {
    if (isBigHead()) {
      updateBigHead();
      return;
    } 
    LivingEntity owner = getEntity();
    if (this.armorStand == null)
      return; 
    if (this.leashed == null)
      return; 
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
      teleport(newLocation);
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
        this.armorStand.a(new Vector3f(this.armorStand.A().b() - 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
        if (this.rot > 0.2D)
          this.rotateLoop = true; 
      } else {
        this.rot -= 0.01D;
        this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.5F, this.armorStand.A().c(), this.armorStand.A().d() + this.rotate));
        if (this.rot < -0.2D)
          this.rotateLoop = false; 
      } 
      Location newLocation = standToLoc.clone();
      teleport(newLocation);
    } 
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      EntityPlayer p = ((CraftPlayer)player).getHandle();
      if (!this.invisibleLeash)
        p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle())); 
      p.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.al(), this.armorStand.ap().c()));
      p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      if (!this.heightLoop) {
        this.height += 0.01D;
        this.armorStand.a(new Vector3f(this.armorStand.A().b() - 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
        if (this.height > 0.1D)
          this.heightLoop = true; 
      } 
    } else if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.a(new Vector3f(this.armorStand.A().b() + 0.8F, this.armorStand.A().c(), this.armorStand.A().d()));
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
    LivingEntity owner = getEntity();
    if (this.armorStand == null)
      return; 
    if (this.leashed == null)
      return; 
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
      this.leashed.b(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.b(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
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
        this.armorStand.d(new Vector3f(this.armorStand.D().b() - 0.5F, this.armorStand.D().c(), this.armorStand.D().d() + this.rotate));
        if (this.rot > 0.2D)
          this.rotateLoop = true; 
      } else {
        this.rot -= 0.01D;
        this.armorStand.d(new Vector3f(this.armorStand.D().b() + 0.5F, this.armorStand.D().c(), this.armorStand.D().d() + this.rotate));
        if (this.rot < -0.2D)
          this.rotateLoop = false; 
      } 
      Location newLocation = standToLoc.clone();
      this.leashed.b(newLocation.getX(), newLocation.getY(), newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
      this.armorStand.b(newLocation.getX(), newLocation.getY() - 1.3D, newLocation.getZ(), newLocation.getYaw(), newLocation.getPitch());
    } 
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      EntityPlayer p = ((CraftPlayer)player).getHandle();
      if (!this.invisibleLeash)
        p.c.b((Packet)new PacketPlayOutAttachEntity((Entity)this.leashed, ((CraftEntity)getEntity()).getHandle())); 
      p.c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.al(), this.armorStand.ap().c()));
      p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.leashed));
      p.c.b((Packet)new PacketPlayOutEntityTeleport((Entity)this.armorStand));
    } 
    if (distance1.distanceSquared(distance2) > this.SQUARED_WALKING) {
      if (!this.heightLoop) {
        this.height += 0.01D;
        this.armorStand.d(new Vector3f(this.armorStand.D().b() - 0.8F, this.armorStand.D().c(), this.armorStand.D().d()));
        if (this.height > 0.1D)
          this.heightLoop = true; 
      } 
    } else if (this.heightLoop) {
      this.height -= 0.01D;
      this.armorStand.d(new Vector3f(this.armorStand.D().b() + 0.8F, this.armorStand.D().c(), this.armorStand.D().d()));
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
        this.armorStand.a(new Vector3f(this.armorStand.A().b(), this.armorStand.A().c() + rotate, this.armorStand.A().d()));
        break;
      case UP:
        this.armorStand.a(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c(), this.armorStand.A().d()));
        break;
      case ALL:
        this.armorStand.a(new Vector3f(this.armorStand.A().b() + rotate, this.armorStand.A().c() + rotate, this.armorStand.A().d()));
        break;
    } 
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      (((CraftPlayer)player).getHandle()).c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.al(), this.armorStand.ap().c()));
    } 
  }
  
  public void rotateBigHead(boolean rotation, RotationType rotationType, float rotate) {
    if (!rotation)
      return; 
    switch (rotationType) {
      case RIGHT:
        this.armorStand.d(new Vector3f(this.armorStand.D().b(), this.armorStand.D().c() + rotate, this.armorStand.D().d()));
        break;
      case UP:
        this.armorStand.d(new Vector3f(this.armorStand.D().b() + rotate, this.armorStand.D().c(), this.armorStand.D().d()));
        break;
      case ALL:
        this.armorStand.d(new Vector3f(this.armorStand.D().b() + rotate, this.armorStand.D().c() + rotate, this.armorStand.D().d()));
        break;
    } 
    for (UUID uuid : this.players) {
      Player player = Bukkit.getPlayer(uuid);
      if (player == null) {
        this.players.remove(uuid);
        continue;
      } 
      (((CraftPlayer)player).getHandle()).c.b((Packet)new PacketPlayOutEntityMetadata(this.armorStand.al(), this.armorStand.ap().c()));
    } 
  }
  
  public double getDistance() {
    return this.distance;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R4\cache\EntityBalloonHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */