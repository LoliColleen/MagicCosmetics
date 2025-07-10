/*     */ package com.francobm.magicosmetics.nms.v1_18_R1.models;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Hat;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.WStick;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class MCChannelHandler extends ChannelDuplexHandler {
/*     */   public MCChannelHandler(EntityPlayer player) {
/*  29 */     this.player = player;
/*     */   }
/*     */   private final EntityPlayer player;
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  33 */     if (msg instanceof PacketPlayOutSetSlot) {
/*  34 */       PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot)msg;
/*  35 */       if (packetPlayOutSetSlot.b() == 0)
/*  36 */         CallUpdateInvEvent(packetPlayOutSetSlot.c(), packetPlayOutSetSlot.d()); 
/*  37 */     } else if (msg instanceof PacketPlayOutNamedEntitySpawn) {
/*  38 */       PacketPlayOutNamedEntitySpawn otherPacket = (PacketPlayOutNamedEntitySpawn)msg;
/*  39 */       handleEntitySpawn(otherPacket.b());
/*  40 */     } else if (msg instanceof PacketPlayOutEntityDestroy) {
/*  41 */       PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
/*  42 */       for (IntListIterator<Integer> intListIterator = otherPacket.b().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  43 */         handleEntityDespawn(id); }
/*     */     
/*  45 */     } else if (msg instanceof PacketPlayOutMount) {
/*  46 */       PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
/*  47 */       msg = handleEntityMount(otherPacket);
/*     */     } 
/*  49 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  54 */     if (msg instanceof net.minecraft.network.protocol.game.PacketPlayInArmAnimation && 
/*  55 */       checkInZone()) {
/*  56 */       openMenu();
/*     */     }
/*     */     
/*  59 */     super.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   private boolean checkInZone() {
/*  63 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  64 */     return playerData.isZone();
/*     */   }
/*     */   
/*     */   private void openMenu() {
/*  68 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  69 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
/*     */   }
/*     */   private void CallUpdateInvEvent(int slot, ItemStack itemStack) {
/*     */     CosmeticInventoryUpdateEvent event;
/*  73 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */     
/*  75 */     if (slot == 5) {
/*  76 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  77 */       Hat hat = playerData.getHat();
/*  78 */       if (hat == null)
/*  79 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
/*  80 */     } else if (slot == 45) {
/*  81 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  82 */       WStick wStick = playerData.getWStick();
/*  83 */       if (wStick == null)
/*  84 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
/*     */     } else {
/*     */       return;
/*     */     } 
/*  88 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
/*     */   }
/*     */   
/*     */   private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
/*  92 */     int id = packetPlayOutMount.c();
/*  93 */     int[] ids = packetPlayOutMount.b();
/*  94 */     Entity entity = getEntityAsync(this.player.x(), id);
/*  95 */     if (!(entity instanceof Player)) return packetPlayOutMount; 
/*  96 */     Player otherPlayer = (Player)entity;
/*  97 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/*  98 */     if (playerData.getBag() == null) return packetPlayOutMount;
/*     */     
/* 100 */     Bag bag = (Bag)playerData.getBag();
/* 101 */     if (bag.getBackpackId() == -1) return packetPlayOutMount; 
/* 102 */     int[] newIds = new int[ids.length + 1];
/* 103 */     newIds[0] = bag.getBackpackId();
/* 104 */     for (int i = 0; i < ids.length; i++) {
/* 105 */       if (ids[i] != bag.getBackpackId())
/* 106 */         newIds[i + 1] = ids[i]; 
/*     */     } 
/* 108 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 109 */     data.d(id);
/* 110 */     data.a(newIds);
/* 111 */     return new PacketPlayOutMount(data);
/*     */   }
/*     */   
/*     */   private void handleEntitySpawn(int id) {
/* 115 */     Entity entity = getEntityAsync(this.player.x(), id);
/* 116 */     if (!(entity instanceof Player))
/* 117 */       return;  Player otherPlayer = (Player)entity;
/* 118 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 119 */     if (playerData == null)
/* 120 */       return;  if (playerData.getBag() == null)
/* 121 */       return;  Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
/*     */   }
/*     */   
/*     */   private void handleEntityDespawn(int id) {
/* 125 */     Entity entity = getEntityAsync(this.player.x(), id);
/* 126 */     if (!(entity instanceof Player))
/* 127 */       return;  Player otherPlayer = (Player)entity;
/* 128 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 129 */     if (playerData == null)
/* 130 */       return;  if (playerData.getBag() == null)
/* 131 */       return;  playerData.getBag().despawn((Player)this.player.getBukkitEntity());
/*     */   }
/*     */   
/*     */   protected Entity getEntityAsync(WorldServer world, int id) {
/* 135 */     PersistentEntitySectionManager<Entity> entityManager = world.P;
/* 136 */     Entity entity = (Entity)entityManager.d().a(id);
/* 137 */     return (entity == null) ? null : (Entity)entity.getBukkitEntity();
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_18_R1\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */