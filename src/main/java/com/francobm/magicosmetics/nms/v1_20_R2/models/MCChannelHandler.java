/*     */ package com.francobm.magicosmetics.nms.v1_20_R2.models;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Hat;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.WStick;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import java.lang.reflect.Method;
/*     */ import net.minecraft.network.PacketDataSerializer;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBundlePacket;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class MCChannelHandler extends ChannelDuplexHandler {
/*     */   static {
/*  32 */     for (Method method : WorldServer.class.getMethods()) {
/*  33 */       if (LevelEntityGetter.class.isAssignableFrom(method.getReturnType()) && method.getReturnType() != LevelEntityGetter.class) {
/*  34 */         entityGetter = method;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static Method entityGetter;
/*     */   private final EntityPlayer player;
/*     */   
/*     */   public MCChannelHandler(EntityPlayer player) {
/*  43 */     this.player = player;
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  47 */     if (msg instanceof PacketPlayOutSetSlot) {
/*  48 */       PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot)msg;
/*  49 */       if (packetPlayOutSetSlot.a() == 0)
/*  50 */         CallUpdateInvEvent(packetPlayOutSetSlot.d(), packetPlayOutSetSlot.e()); 
/*  51 */     } else if (msg instanceof ClientboundBundlePacket) {
/*  52 */       ClientboundBundlePacket packet = (ClientboundBundlePacket)msg;
/*  53 */       for (Packet<?> subPacket : (Iterable<Packet<?>>)packet.a()) {
/*  54 */         if (subPacket instanceof PacketPlayOutSpawnEntity) {
/*  55 */           PacketPlayOutSpawnEntity otherPacket = (PacketPlayOutSpawnEntity)subPacket;
/*  56 */           handleEntitySpawn(otherPacket.a()); continue;
/*  57 */         }  if (subPacket instanceof PacketPlayOutEntityDestroy) {
/*  58 */           PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)subPacket;
/*  59 */           for (IntListIterator<Integer> intListIterator = otherPacket.a().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  60 */             handleEntityDespawn(id); }
/*     */         
/*     */         } 
/*     */       } 
/*  64 */     } else if (msg instanceof PacketPlayOutSpawnEntity) {
/*  65 */       PacketPlayOutSpawnEntity otherPacket = (PacketPlayOutSpawnEntity)msg;
/*  66 */       handleEntitySpawn(otherPacket.a());
/*  67 */     } else if (msg instanceof PacketPlayOutEntityDestroy) {
/*  68 */       PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
/*  69 */       for (IntListIterator<Integer> intListIterator = otherPacket.a().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  70 */         handleEntityDespawn(id); }
/*     */     
/*  72 */     } else if (msg instanceof PacketPlayOutMount) {
/*  73 */       PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
/*  74 */       msg = handleEntityMount(otherPacket);
/*     */     } 
/*  76 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  81 */     if (msg instanceof net.minecraft.network.protocol.game.PacketPlayInArmAnimation && 
/*  82 */       checkInZone()) {
/*  83 */       openMenu();
/*     */     }
/*     */     
/*  86 */     super.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   private boolean checkInZone() {
/*  90 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  91 */     return playerData.isZone();
/*     */   }
/*     */   
/*     */   private void openMenu() {
/*  95 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  96 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
/*     */   }
/*     */   private void CallUpdateInvEvent(int slot, ItemStack itemStack) {
/*     */     CosmeticInventoryUpdateEvent event;
/* 100 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */     
/* 102 */     if (slot == 5) {
/* 103 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/* 104 */       Hat hat = playerData.getHat();
/* 105 */       if (hat == null)
/* 106 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
/* 107 */     } else if (slot == 45) {
/* 108 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/* 109 */       WStick wStick = playerData.getWStick();
/* 110 */       if (wStick == null)
/* 111 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
/*     */     } else {
/*     */       return;
/*     */     } 
/* 115 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
/*     */   }
/*     */   
/*     */   private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
/* 119 */     int id = packetPlayOutMount.d();
/* 120 */     int[] ids = packetPlayOutMount.a();
/* 121 */     Entity entity = getEntityAsync(this.player.x(), id);
/* 122 */     if (!(entity instanceof Player)) return packetPlayOutMount; 
/* 123 */     Player otherPlayer = (Player)entity;
/* 124 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 125 */     if (playerData.getBag() == null) return packetPlayOutMount;
/*     */     
/* 127 */     Bag bag = (Bag)playerData.getBag();
/* 128 */     if (bag.getBackpackId() == -1) return packetPlayOutMount; 
/* 129 */     int[] newIds = new int[ids.length + 1];
/* 130 */     newIds[0] = bag.getBackpackId();
/* 131 */     for (int i = 0; i < ids.length; i++) {
/* 132 */       if (ids[i] != bag.getBackpackId())
/* 133 */         newIds[i + 1] = ids[i]; 
/*     */     } 
/* 135 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 136 */     data.c(id);
/* 137 */     data.a(newIds);
/* 138 */     return new PacketPlayOutMount(data);
/*     */   }
/*     */   
/*     */   private void handleEntitySpawn(int id) {
/* 142 */     Entity entity = getEntityAsync(this.player.x(), id);
/* 143 */     if (!(entity instanceof Player))
/* 144 */       return;  Player otherPlayer = (Player)entity;
/* 145 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 146 */     if (playerData == null)
/* 147 */       return;  if (playerData.getBag() == null)
/* 148 */       return;  Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
/*     */   }
/*     */   
/*     */   private void handleEntityDespawn(int id) {
/* 152 */     Entity entity = getEntityAsync(this.player.x(), id);
/* 153 */     if (!(entity instanceof Player))
/* 154 */       return;  Player otherPlayer = (Player)entity;
/* 155 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 156 */     if (playerData == null)
/* 157 */       return;  if (playerData.getBag() == null)
/* 158 */       return;  playerData.getBag().despawn((Player)this.player.getBukkitEntity());
/*     */   }
/*     */   
/*     */   protected Entity getEntityAsync(WorldServer world, int id) {
/* 162 */     Entity entity = (Entity)getEntityGetter(world).a(id);
/* 163 */     return (entity == null) ? null : (Entity)entity.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public static LevelEntityGetter<Entity> getEntityGetter(WorldServer level) {
/* 167 */     if (entityGetter == null)
/* 168 */       return level.M.d(); 
/*     */     try {
/* 170 */       return (LevelEntityGetter<Entity>)entityGetter.invoke(level, new Object[0]);
/* 171 */     } catch (Throwable ignored) {
/* 172 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R2\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */