/*     */ package com.francobm.magicosmetics.nms.v1_19_R2.models;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
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
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutMount;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
/*     */ import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
/*     */ import net.minecraft.server.level.EntityPlayer;
/*     */ import net.minecraft.server.level.WorldServer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
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
/*  49 */       if (packetPlayOutSetSlot.b() == 0)
/*  50 */         CallUpdateInvEvent(packetPlayOutSetSlot.c(), packetPlayOutSetSlot.d()); 
/*  51 */     } else if (msg instanceof PacketPlayOutNamedEntitySpawn) {
/*  52 */       PacketPlayOutNamedEntitySpawn otherPacket = (PacketPlayOutNamedEntitySpawn)msg;
/*  53 */       handleEntitySpawn(otherPacket.b());
/*  54 */     } else if (msg instanceof PacketPlayOutEntityDestroy) {
/*  55 */       PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
/*  56 */       for (IntListIterator<Integer> intListIterator = otherPacket.b().iterator(); intListIterator.hasNext(); ) { int id = ((Integer)intListIterator.next()).intValue();
/*  57 */         handleEntityDespawn(id); }
/*     */     
/*  59 */     } else if (msg instanceof PacketPlayOutMount) {
/*  60 */       PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
/*  61 */       msg = handleEntityMount(otherPacket);
/*     */     } 
/*  63 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  68 */     if (msg instanceof net.minecraft.network.protocol.game.PacketPlayInArmAnimation && 
/*  69 */       checkInZone()) {
/*  70 */       openMenu();
/*     */     }
/*     */     
/*  73 */     super.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   private boolean checkInZone() {
/*  77 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  78 */     return playerData.isZone();
/*     */   }
/*     */   
/*     */   private void openMenu() {
/*  82 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  83 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
/*     */   }
/*     */   private void CallUpdateInvEvent(int slot, ItemStack itemStack) {
/*     */     CosmeticInventoryUpdateEvent event;
/*  87 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */     
/*  89 */     if (slot == 5) {
/*  90 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  91 */       Hat hat = playerData.getHat();
/*  92 */       if (hat == null)
/*  93 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
/*  94 */     } else if (slot == 45) {
/*  95 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  96 */       WStick wStick = playerData.getWStick();
/*  97 */       if (wStick == null)
/*  98 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
/*     */     } else {
/*     */       return;
/*     */     } 
/* 102 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
/*     */   }
/*     */   
/*     */   private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
/* 106 */     int id = packetPlayOutMount.c();
/* 107 */     int[] ids = packetPlayOutMount.b();
/* 108 */     Entity entity = getEntityAsync(this.player.y(), id);
/* 109 */     if (!(entity instanceof Player)) return packetPlayOutMount; 
/* 110 */     Player otherPlayer = (Player)entity;
/* 111 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 112 */     if (playerData.getBag() == null) return packetPlayOutMount;
/*     */     
/* 114 */     Bag bag = (Bag)playerData.getBag();
/* 115 */     if (bag.getBackpackId() == -1) return packetPlayOutMount; 
/* 116 */     int[] newIds = new int[ids.length + 1];
/* 117 */     newIds[0] = bag.getBackpackId();
/* 118 */     for (int i = 0; i < ids.length; i++) {
/* 119 */       if (ids[i] != bag.getBackpackId())
/* 120 */         newIds[i + 1] = ids[i]; 
/*     */     } 
/* 122 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 123 */     data.d(id);
/* 124 */     data.a(newIds);
/* 125 */     return new PacketPlayOutMount(data);
/*     */   }
/*     */   
/*     */   private void handleEntitySpawn(int id) {
/* 129 */     Entity entity = getEntityAsync(this.player.y(), id);
/* 130 */     if (!(entity instanceof Player))
/* 131 */       return;  Player otherPlayer = (Player)entity;
/* 132 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 133 */     if (playerData == null)
/* 134 */       return;  if (playerData.getBag() == null)
/* 135 */       return;  Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
/*     */   }
/*     */   
/*     */   private void handleEntityDespawn(int id) {
/* 139 */     Entity entity = getEntityAsync(this.player.y(), id);
/* 140 */     if (!(entity instanceof Player))
/* 141 */       return;  Player otherPlayer = (Player)entity;
/* 142 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 143 */     if (playerData == null)
/* 144 */       return;  if (playerData.getBag() == null)
/* 145 */       return;  playerData.getBag().despawn((Player)this.player.getBukkitEntity());
/*     */   }
/*     */   
/*     */   protected Entity getEntityAsync(WorldServer world, int id) {
/* 149 */     Entity entity = (Entity)getEntityGetter(world).a(id);
/* 150 */     return (entity == null) ? null : (Entity)entity.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public static LevelEntityGetter<Entity> getEntityGetter(WorldServer level) {
/* 154 */     if (entityGetter == null)
/* 155 */       return level.P.d(); 
/*     */     try {
/* 157 */       return (LevelEntityGetter<Entity>)entityGetter.invoke(level, new Object[0]);
/* 158 */     } catch (Throwable ignored) {
/* 159 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R2\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */