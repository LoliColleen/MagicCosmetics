/*     */ package com.francobm.magicosmetics.nms.v1_20_R3.models;
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
/*     */ import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class MCChannelHandler extends ChannelDuplexHandler {
/*     */   static {
/*  33 */     for (Method method : WorldServer.class.getMethods()) {
/*  34 */       if (LevelEntityGetter.class.isAssignableFrom(method.getReturnType()) && method.getReturnType() != LevelEntityGetter.class) {
/*  35 */         entityGetter = method;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static Method entityGetter;
/*     */   private final EntityPlayer player;
/*     */   
/*     */   public MCChannelHandler(EntityPlayer player) {
/*  44 */     this.player = player;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  49 */     if (msg instanceof PacketPlayOutSetSlot) {
/*  50 */       PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot)msg;
/*  51 */       if (packetPlayOutSetSlot.a() == 0)
/*  52 */         CallUpdateInvEvent(packetPlayOutSetSlot.d(), packetPlayOutSetSlot.e()); 
/*  53 */     } else if (msg instanceof ClientboundBundlePacket) {
/*  54 */       ClientboundBundlePacket packet = (ClientboundBundlePacket)msg;
/*  55 */       for (Packet<?> subPacket : (Iterable<Packet<?>>)packet.a()) {
/*  56 */         if (subPacket instanceof PacketPlayOutSpawnEntity) {
/*  57 */           PacketPlayOutSpawnEntity otherPacket = (PacketPlayOutSpawnEntity)subPacket;
/*  58 */           handleEntitySpawn(otherPacket.a()); continue;
/*  59 */         }  if (subPacket instanceof PacketPlayOutEntityDestroy) {
/*  60 */           PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)subPacket;
/*  61 */           for (IntListIterator<Integer> intListIterator = otherPacket.a().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  62 */             handleEntityDespawn(id); }
/*     */         
/*     */         } 
/*     */       } 
/*  66 */     } else if (msg instanceof PacketPlayOutSpawnEntity) {
/*  67 */       PacketPlayOutSpawnEntity otherPacket = (PacketPlayOutSpawnEntity)msg;
/*  68 */       handleEntitySpawn(otherPacket.a());
/*  69 */     } else if (msg instanceof PacketPlayOutEntityDestroy) {
/*  70 */       PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
/*  71 */       for (IntListIterator<Integer> intListIterator = otherPacket.a().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
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
/*     */   private void openMenu() {
/*  97 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  98 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
/*     */   }
/*     */   private void CallUpdateInvEvent(int slot, ItemStack itemStack) {
/*     */     CosmeticInventoryUpdateEvent event;
/* 102 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */     
/* 104 */     if (slot == 5) {
/* 105 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/* 106 */       Hat hat = playerData.getHat();
/* 107 */       if (hat == null)
/* 108 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
/* 109 */     } else if (slot == 45) {
/* 110 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/* 111 */       WStick wStick = playerData.getWStick();
/* 112 */       if (wStick == null)
/* 113 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
/*     */     } else {
/*     */       return;
/*     */     } 
/* 117 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
/*     */   }
/*     */   
/*     */   private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
/* 121 */     int id = packetPlayOutMount.d();
/* 122 */     int[] ids = packetPlayOutMount.a();
/* 123 */     Entity entity = getEntityAsync(this.player.z(), id);
/* 124 */     if (!(entity instanceof Player)) return packetPlayOutMount; 
/* 125 */     Player otherPlayer = (Player)entity;
/* 126 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 127 */     if (playerData.getBag() == null) return packetPlayOutMount;
/*     */     
/* 129 */     Bag bag = (Bag)playerData.getBag();
/* 130 */     if (bag.getBackpackId() == -1) return packetPlayOutMount; 
/* 131 */     int[] newIds = new int[ids.length + 1];
/* 132 */     newIds[0] = bag.getBackpackId();
/* 133 */     for (int i = 0; i < ids.length; i++) {
/* 134 */       if (ids[i] != bag.getBackpackId())
/* 135 */         newIds[i + 1] = ids[i]; 
/*     */     } 
/* 137 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 138 */     data.c(id);
/* 139 */     data.a(newIds);
/* 140 */     return new PacketPlayOutMount(data);
/*     */   }
/*     */   
/*     */   private void handleEntitySpawn(int id) {
/* 144 */     Entity entity = getEntityAsync(this.player.z(), id);
/* 145 */     if (!(entity instanceof Player))
/* 146 */       return;  Player otherPlayer = (Player)entity;
/* 147 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 148 */     if (playerData == null)
/* 149 */       return;  if (playerData.getBag() == null)
/* 150 */       return;  Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
/*     */   }
/*     */   
/*     */   private void handleEntityDespawn(int id) {
/* 154 */     Entity entity = getEntityAsync(this.player.z(), id);
/* 155 */     if (!(entity instanceof Player))
/* 156 */       return;  Player otherPlayer = (Player)entity;
/* 157 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 158 */     if (playerData == null)
/* 159 */       return;  if (playerData.getBag() == null)
/* 160 */       return;  playerData.getBag().despawn((Player)this.player.getBukkitEntity());
/*     */   }
/*     */   
/*     */   protected Entity getEntityAsync(WorldServer world, int id) {
/* 164 */     Entity entity = (Entity)getEntityGetter(world).a(id);
/* 165 */     return (entity == null) ? null : (Entity)entity.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public static LevelEntityGetter<Entity> getEntityGetter(WorldServer level) {
/* 169 */     if (entityGetter == null)
/* 170 */       return level.M.d(); 
/*     */     try {
/* 172 */       return (LevelEntityGetter<Entity>)entityGetter.invoke(level, new Object[0]);
/* 173 */     } catch (Throwable ignored) {
/* 174 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R3\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */