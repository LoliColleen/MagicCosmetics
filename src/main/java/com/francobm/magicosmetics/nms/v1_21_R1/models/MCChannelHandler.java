/*     */ package com.francobm.magicosmetics.nms.v1_21_R1.models;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.CosmeticType;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Hat;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.WStick;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
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
/*  34 */     for (Method method : WorldServer.class.getMethods()) {
/*  35 */       if (LevelEntityGetter.class.isAssignableFrom(method.getReturnType()) && method.getReturnType() != LevelEntityGetter.class) {
/*  36 */         entityGetter = method;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static Method entityGetter;
/*     */   private final EntityPlayer player;
/*     */   
/*     */   public MCChannelHandler(EntityPlayer player) {
/*  45 */     this.player = player;
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  49 */     if (msg instanceof PacketPlayOutSetSlot) {
/*  50 */       PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot)msg;
/*  51 */       if (packetPlayOutSetSlot.b() == 0)
/*  52 */         CallUpdateInvEvent(packetPlayOutSetSlot.e(), packetPlayOutSetSlot.f()); 
/*  53 */     } else if (msg instanceof ClientboundBundlePacket) {
/*  54 */       ClientboundBundlePacket packet = (ClientboundBundlePacket)msg;
/*  55 */       for (Packet<?> subPacket : (Iterable<Packet<?>>)packet.b()) {
/*  56 */         if (subPacket instanceof PacketPlayOutSpawnEntity) {
/*  57 */           PacketPlayOutSpawnEntity otherPacket = (PacketPlayOutSpawnEntity)subPacket;
/*  58 */           handleEntitySpawn(otherPacket.b()); continue;
/*  59 */         }  if (subPacket instanceof PacketPlayOutEntityDestroy) {
/*  60 */           PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)subPacket;
/*  61 */           for (IntListIterator<Integer> intListIterator = otherPacket.b().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  62 */             handleEntityDespawn(id); }
/*     */         
/*     */         } 
/*     */       } 
/*  66 */     } else if (msg instanceof PacketPlayOutSpawnEntity) {
/*  67 */       PacketPlayOutSpawnEntity otherPacket = (PacketPlayOutSpawnEntity)msg;
/*  68 */       handleEntitySpawn(otherPacket.b());
/*  69 */     } else if (msg instanceof PacketPlayOutEntityDestroy) {
/*  70 */       PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
/*  71 */       for (IntListIterator<Integer> intListIterator = otherPacket.b().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  72 */         handleEntityDespawn(id); }
/*     */     
/*  74 */     } else if (msg instanceof PacketPlayOutMount) {
/*  75 */       PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
/*  76 */       msg = handleEntityMount(otherPacket);
/*     */     } 
/*  78 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  83 */     if (msg instanceof net.minecraft.network.protocol.game.PacketPlayInArmAnimation && 
/*  84 */       checkInZone()) {
/*  85 */       openMenu();
/*     */     }
/*     */     
/*  88 */     super.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   private boolean checkInZone() {
/*  92 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  93 */     return playerData.isZone();
/*     */   }
/*     */   
/*     */   private boolean isOffHand() {
/*  97 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  98 */     return (playerData.getWStick() != null);
/*     */   }
/*     */   
/*     */   private void openMenu() {
/* 102 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 103 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
/*     */   }
/*     */   private void CallUpdateInvEvent(int slot, ItemStack itemStack) {
/*     */     CosmeticInventoryUpdateEvent event;
/* 107 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */     
/* 109 */     if (slot == 5) {
/* 110 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/* 111 */       Hat hat = playerData.getHat();
/* 112 */       if (hat == null)
/* 113 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
/* 114 */     } else if (slot == 45) {
/* 115 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/* 116 */       WStick wStick = playerData.getWStick();
/* 117 */       if (wStick == null)
/* 118 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
/*     */     } else {
/*     */       return;
/*     */     } 
/* 122 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
/*     */   }
/*     */ 
/*     */   
/*     */   private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
/* 127 */     int id = packetPlayOutMount.e();
/* 128 */     int[] ids = packetPlayOutMount.b();
/* 129 */     Entity entity = getEntityAsync(this.player.A(), id);
/* 130 */     if (!(entity instanceof Player)) return packetPlayOutMount; 
/* 131 */     Player otherPlayer = (Player)entity;
/* 132 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 133 */     if (playerData.getBag() == null) return packetPlayOutMount;
/*     */     
/* 135 */     Bag bag = (Bag)playerData.getBag();
/* 136 */     if (bag.getBackpackId() == -1) return packetPlayOutMount; 
/* 137 */     int[] newIds = new int[ids.length + 1];
/* 138 */     newIds[0] = bag.getBackpackId();
/* 139 */     for (int i = 0; i < ids.length; i++) {
/* 140 */       if (ids[i] != bag.getBackpackId())
/* 141 */         newIds[i + 1] = ids[i]; 
/*     */     } 
/* 143 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 144 */     data.c(id);
/* 145 */     data.a(newIds);
/* 146 */     return (PacketPlayOutMount)PacketPlayOutMount.a.decode(data);
/*     */   }
/*     */   
/*     */   private void handleEntitySpawn(int id) {
/* 150 */     Entity entity = getEntityAsync(this.player.A(), id);
/* 151 */     if (!(entity instanceof Player))
/* 152 */       return;  Player otherPlayer = (Player)entity;
/* 153 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 154 */     if (playerData == null)
/* 155 */       return;  if (playerData.getBag() == null)
/* 156 */       return;  Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
/*     */   }
/*     */   
/*     */   private void handleEntityDespawn(int id) {
/* 160 */     Entity entity = getEntityAsync(this.player.A(), id);
/* 161 */     if (!(entity instanceof Player))
/* 162 */       return;  Player otherPlayer = (Player)entity;
/* 163 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 164 */     if (playerData == null)
/* 165 */       return;  if (playerData.getBag() == null)
/* 166 */       return;  playerData.getBag().despawn((Player)this.player.getBukkitEntity());
/*     */   }
/*     */   
/*     */   protected Entity getEntityAsync(WorldServer world, int id) {
/* 170 */     Entity entity = (Entity)getEntityGetter(world).a(id);
/* 171 */     return (entity == null) ? null : (Entity)entity.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public static LevelEntityGetter<Entity> getEntityGetter(WorldServer level) {
/* 175 */     if (entityGetter == null)
/* 176 */       return level.N.d(); 
/*     */     try {
/* 178 */       return (LevelEntityGetter<Entity>)entityGetter.invoke(level, new Object[0]);
/* 179 */     } catch (Throwable ignored) {
/* 180 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_21_R1\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */