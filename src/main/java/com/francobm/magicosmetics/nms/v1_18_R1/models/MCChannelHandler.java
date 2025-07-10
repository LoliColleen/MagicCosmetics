package com.francobm.magicosmetics.nms.v1_18_R1.models;

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
import it.unimi.dsi.fastutil.ints.IntListIterator;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
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
      if (packetPlayOutSetSlot.b() == 0)
        CallUpdateInvEvent(packetPlayOutSetSlot.c(), packetPlayOutSetSlot.d()); 
    } else if (msg instanceof PacketPlayOutNamedEntitySpawn) {
      PacketPlayOutNamedEntitySpawn otherPacket = (PacketPlayOutNamedEntitySpawn)msg;
      handleEntitySpawn(otherPacket.b());
    } else if (msg instanceof PacketPlayOutEntityDestroy) {
      PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
      for (IntListIterator<Integer> intListIterator = otherPacket.b().iterator(); intListIterator.hasNext(); ) {
        int id = ((Integer)intListIterator.next()).intValue();
        handleEntityDespawn(id);
      } 
    } else if (msg instanceof PacketPlayOutMount) {
      PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
      msg = handleEntityMount(otherPacket);
    } 
    super.write(ctx, msg, promise);
  }
  
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof net.minecraft.network.protocol.game.PacketPlayInArmAnimation && 
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
    plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
  }
  
  private void CallUpdateInvEvent(int slot, ItemStack itemStack) {
    CosmeticInventoryUpdateEvent event;
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
    int id = packetPlayOutMount.c();
    int[] ids = packetPlayOutMount.b();
    Entity entity = getEntityAsync(this.player.x(), id);
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
    return new PacketPlayOutMount(data);
  }
  
  private void handleEntitySpawn(int id) {
    Entity entity = getEntityAsync(this.player.x(), id);
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
    Entity entity = getEntityAsync(this.player.x(), id);
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
    PersistentEntitySectionManager<Entity> entityManager = world.P;
    Entity entity = (Entity)entityManager.d().a(id);
    return (entity == null) ? null : (Entity)entity.getBukkitEntity();
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */