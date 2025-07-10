package com.francobm.magicosmetics.nms.v1_16_R3.models;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.api.CosmeticType;
import com.francobm.magicosmetics.cache.PlayerData;
import com.francobm.magicosmetics.cache.cosmetics.Hat;
import com.francobm.magicosmetics.cache.cosmetics.WStick;
import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutMount;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public class MCChannelHandler extends ChannelDuplexHandler {
  private final EntityPlayer player;
  
  public MCChannelHandler(EntityPlayer player) {
    this.player = player;
  }
  
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    if (msg instanceof PacketPlayOutSetSlot) {
      PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot)msg;
      CallUpdateInvEvent(packetPlayOutSetSlot);
    } else if (msg instanceof PacketPlayOutNamedEntitySpawn) {
      PacketPlayOutNamedEntitySpawn otherPacket = (PacketPlayOutNamedEntitySpawn)msg;
      handleEntitySpawn(getIntPacket(otherPacket, "a"));
    } else if (msg instanceof PacketPlayOutEntityDestroy) {
      PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
      for (int id : getArrayIntsPacket(otherPacket, "a"))
        handleEntityDespawn(id); 
    } else if (msg instanceof PacketPlayOutMount) {
      PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
      msg = handleEntityMount(otherPacket);
    } 
    super.write(ctx, msg, promise);
  }
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof net.minecraft.server.v1_16_R3.PacketPlayInArmAnimation && 
      checkInZone())
      openMenu(); 
    super.channelRead(ctx, msg);
  }
  
  private boolean checkInZone() {
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
    return playerData.isZone();
  }
  
  private void openMenu() {
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (this.player.activeContainer != this.player.defaultContainer)
      return; 
    plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
  }
  
  private void CallUpdateInvEvent(PacketPlayOutSetSlot packetPlayOutSetSlot) {
    CosmeticInventoryUpdateEvent event;
    PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.buffer());
    try {
      packetPlayOutSetSlot.b(packetDataSerializer);
    } catch (IOException e) {
      return;
    } 
    int containerId = packetDataSerializer.readByte();
    int slot = packetDataSerializer.readShort();
    ItemStack itemStack = packetDataSerializer.n();
    if (containerId != 0)
      return; 
    MagicCosmetics plugin = MagicCosmetics.getInstance();
    if (slot == 5) {
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
      Hat hat = playerData.getHat();
      if (hat == null)
        return; 
      event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
    } else if (slot == 45) {
      PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
      WStick wStick = playerData.getWStick();
      if (wStick == null)
        return; 
      event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
    } else {
      return;
    } 
    plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
  }
  
  private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
    int id = getIntPacket(packetPlayOutMount, "a");
    int[] ids = getArrayIntsPacket(packetPlayOutMount, "b");
    Entity entity = getEntityAsync(this.player.getWorldServer(), id);
    if (!(entity instanceof Player))
      return packetPlayOutMount; 
    Player otherPlayer = (Player)entity;
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
    if (playerData.getBag() == null)
      return packetPlayOutMount; 
    Bag bag = (Bag)playerData.getBag();
    if (bag.getBackpackId() == -1)
      return packetPlayOutMount; 
    int[] newIds = new int[ids.length + 1];
    newIds[0] = bag.getBackpackId();
    for (int i = 0; i < ids.length; i++) {
      if (ids[i] != bag.getBackpackId())
        newIds[i + 1] = ids[i]; 
    } 
    PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
    data.d(id);
    data.a(newIds);
    try {
      packetPlayOutMount.a(data);
    } catch (IOException iOException) {}
    return packetPlayOutMount;
  }
  
  private void handleEntitySpawn(int id) {
    Entity entity = getEntityAsync(this.player.getWorldServer(), id);
    if (!(entity instanceof Player))
      return; 
    Player otherPlayer = (Player)entity;
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
    if (playerData == null)
      return; 
    if (playerData.getBag() == null)
      return; 
    Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
  }
  
  private void handleEntityDespawn(int id) {
    Entity entity = getEntityAsync(this.player.getWorldServer(), id);
    if (!(entity instanceof Player))
      return; 
    Player otherPlayer = (Player)entity;
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
    if (playerData == null)
      return; 
    if (playerData.getBag() == null)
      return; 
    playerData.getBag().despawn((Player)this.player.getBukkitEntity());
  }
  
  protected Entity getEntityAsync(WorldServer world, int id) {
    Entity entity = (Entity)world.entitiesById.get(id);
    return (entity == null) ? null : (Entity)entity.getBukkitEntity();
  }
  
  public int getIntPacket(Object packet, String fieldName) {
    try {
      Field field = packet.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.getInt(packet);
    } catch (IllegalAccessException|NoSuchFieldException e) {
      e.printStackTrace();
      return -1;
    } 
  }
  
  public int[] getArrayIntsPacket(Object packet, String fieldName) {
    try {
      Field field = packet.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      Object arrayObject = field.get(packet);
      if (arrayObject != null && arrayObject.getClass().isArray()) {
        int length = Array.getLength(arrayObject);
        int[] result = new int[length];
        for (int i = 0; i < length; i++)
          result[i] = ((Integer)Array.get(arrayObject, i)).intValue(); 
        return result;
      } 
    } catch (IllegalAccessException|NoSuchFieldException e) {
      e.printStackTrace();
    } 
    return null;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */