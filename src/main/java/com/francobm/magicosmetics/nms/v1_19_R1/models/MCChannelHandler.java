/*     */ package com.francobm.magicosmetics.nms.v1_19_R1.models;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Hat;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.WStick;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import net.minecraft.network.PacketDataSerializer;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.entity.PersistentEntitySectionManager;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class MCChannelHandler extends ChannelDuplexHandler {
/*     */   public MCChannelHandler(EntityPlayer player) {
/*  33 */     this.player = player;
/*     */   }
/*     */   private final EntityPlayer player;
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  37 */     if (msg instanceof PacketPlayOutSetSlot) {
/*  38 */       PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot)msg;
/*  39 */       if (packetPlayOutSetSlot.b() == 0)
/*  40 */         CallUpdateInvEvent(packetPlayOutSetSlot.c(), packetPlayOutSetSlot.d()); 
/*  41 */     } else if (msg instanceof PacketPlayOutNamedEntitySpawn) {
/*  42 */       PacketPlayOutNamedEntitySpawn otherPacket = (PacketPlayOutNamedEntitySpawn)msg;
/*  43 */       handleEntitySpawn(otherPacket.b());
/*  44 */     } else if (msg instanceof PacketPlayOutEntityDestroy) {
/*  45 */       PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
/*  46 */       for (IntListIterator<Integer> intListIterator = otherPacket.b().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  47 */         handleEntityDespawn(id); }
/*     */     
/*  49 */     } else if (msg instanceof PacketPlayOutMount) {
/*  50 */       PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
/*  51 */       msg = handleEntityMount(otherPacket);
/*     */     } 
/*  53 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  58 */     if (msg instanceof net.minecraft.network.protocol.game.PacketPlayInArmAnimation && 
/*  59 */       checkInZone()) {
/*  60 */       openMenu();
/*     */     }
/*     */     
/*  63 */     super.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   private boolean checkInZone() {
/*  67 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  68 */     return playerData.isZone();
/*     */   }
/*     */   
/*     */   private void openMenu() {
/*  72 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  73 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
/*     */   }
/*     */   private void CallUpdateInvEvent(int slot, ItemStack itemStack) {
/*     */     CosmeticInventoryUpdateEvent event;
/*  77 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */     
/*  79 */     if (slot == 5) {
/*  80 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  81 */       Hat hat = playerData.getHat();
/*  82 */       if (hat == null)
/*  83 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
/*  84 */     } else if (slot == 45) {
/*  85 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  86 */       WStick wStick = playerData.getWStick();
/*  87 */       if (wStick == null)
/*  88 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
/*     */     } else {
/*     */       return;
/*     */     } 
/*  92 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
/*     */   }
/*     */   
/*     */   private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
/*  96 */     int id = packetPlayOutMount.c();
/*  97 */     int[] ids = packetPlayOutMount.b();
/*  98 */     Entity entity = getEntityAsync(this.player.x(), id);
/*  99 */     if (!(entity instanceof Player)) return packetPlayOutMount; 
/* 100 */     Player otherPlayer = (Player)entity;
/* 101 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 102 */     if (playerData.getBag() == null) return packetPlayOutMount;
/*     */     
/* 104 */     Bag bag = (Bag)playerData.getBag();
/* 105 */     if (bag.getBackpackId() == -1) return packetPlayOutMount; 
/* 106 */     int[] newIds = new int[ids.length + 1];
/* 107 */     newIds[0] = bag.getBackpackId();
/* 108 */     for (int i = 0; i < ids.length; i++) {
/* 109 */       if (ids[i] != bag.getBackpackId())
/* 110 */         newIds[i + 1] = ids[i]; 
/*     */     } 
/* 112 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 113 */     data.d(id);
/* 114 */     data.a(newIds);
/* 115 */     return new PacketPlayOutMount(data);
/*     */   }
/*     */   
/*     */   private void handleEntitySpawn(int id) {
/* 119 */     Entity entity = getEntityAsync(this.player.x(), id);
/* 120 */     if (!(entity instanceof Player))
/* 121 */       return;  Player otherPlayer = (Player)entity;
/* 122 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 123 */     if (playerData == null)
/* 124 */       return;  if (playerData.getBag() == null)
/* 125 */       return;  Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
/*     */   }
/*     */   
/*     */   private void handleEntityDespawn(int id) {
/* 129 */     Entity entity = getEntityAsync(this.player.x(), id);
/* 130 */     if (!(entity instanceof Player))
/* 131 */       return;  Player otherPlayer = (Player)entity;
/* 132 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 133 */     if (playerData == null)
/* 134 */       return;  if (playerData.getBag() == null)
/* 135 */       return;  playerData.getBag().despawn((Player)this.player.getBukkitEntity());
/*     */   }
/*     */   
/*     */   protected Entity getEntityAsync(WorldServer world, int id) {
/* 139 */     PersistentEntitySectionManager<Entity> entityManager = world.P;
/* 140 */     Entity entity = (Entity)entityManager.d().a(id);
/* 141 */     return (entity == null) ? null : (Entity)entity.getBukkitEntity();
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R1\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */