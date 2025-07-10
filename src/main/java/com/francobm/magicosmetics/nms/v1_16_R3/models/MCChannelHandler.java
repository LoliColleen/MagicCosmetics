/*     */ package com.francobm.magicosmetics.nms.v1_16_R3.models;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.Hat;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.WStick;
/*     */ import com.francobm.magicosmetics.cache.cosmetics.backpacks.Bag;
/*     */ import com.francobm.magicosmetics.events.CosmeticInventoryUpdateEvent;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import net.minecraft.server.v1_16_R3.Entity;
/*     */ import net.minecraft.server.v1_16_R3.EntityPlayer;
/*     */ import net.minecraft.server.v1_16_R3.ItemStack;
/*     */ import net.minecraft.server.v1_16_R3.PacketDataSerializer;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutMount;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
/*     */ import net.minecraft.server.v1_16_R3.PacketPlayOutSetSlot;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class MCChannelHandler extends ChannelDuplexHandler {
/*     */   public MCChannelHandler(EntityPlayer player) {
/*  27 */     this.player = player;
/*     */   }
/*     */   private final EntityPlayer player;
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/*  31 */     if (msg instanceof PacketPlayOutSetSlot) {
/*  32 */       PacketPlayOutSetSlot packetPlayOutSetSlot = (PacketPlayOutSetSlot)msg;
/*  33 */       CallUpdateInvEvent(packetPlayOutSetSlot);
/*  34 */     } else if (msg instanceof PacketPlayOutNamedEntitySpawn) {
/*  35 */       PacketPlayOutNamedEntitySpawn otherPacket = (PacketPlayOutNamedEntitySpawn)msg;
/*  36 */       handleEntitySpawn(getIntPacket(otherPacket, "a"));
/*  37 */     } else if (msg instanceof PacketPlayOutEntityDestroy) {
/*  38 */       PacketPlayOutEntityDestroy otherPacket = (PacketPlayOutEntityDestroy)msg;
/*  39 */       for (int id : getArrayIntsPacket(otherPacket, "a")) {
/*  40 */         handleEntityDespawn(id);
/*     */       }
/*  42 */     } else if (msg instanceof PacketPlayOutMount) {
/*  43 */       PacketPlayOutMount otherPacket = (PacketPlayOutMount)msg;
/*  44 */       msg = handleEntityMount(otherPacket);
/*     */     } 
/*  46 */     super.write(ctx, msg, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*  51 */     if (msg instanceof net.minecraft.server.v1_16_R3.PacketPlayInArmAnimation && 
/*  52 */       checkInZone()) {
/*  53 */       openMenu();
/*     */     }
/*     */     
/*  56 */     super.channelRead(ctx, msg);
/*     */   }
/*     */   
/*     */   private boolean checkInZone() {
/*  60 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  61 */     return playerData.isZone();
/*     */   }
/*     */   
/*     */   private void openMenu() {
/*  65 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  66 */     if (this.player.activeContainer != this.player.defaultContainer)
/*  67 */       return;  plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getCosmeticsManager().openMenu((Player)this.player.getBukkitEntity(), plugin.getMainMenu()));
/*     */   }
/*     */   private void CallUpdateInvEvent(PacketPlayOutSetSlot packetPlayOutSetSlot) {
/*     */     CosmeticInventoryUpdateEvent event;
/*  71 */     PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.buffer());
/*     */     try {
/*  73 */       packetPlayOutSetSlot.b(packetDataSerializer);
/*  74 */     } catch (IOException e) {
/*     */       return;
/*     */     } 
/*  77 */     int containerId = packetDataSerializer.readByte();
/*  78 */     int slot = packetDataSerializer.readShort();
/*  79 */     ItemStack itemStack = packetDataSerializer.n();
/*  80 */     if (containerId != 0)
/*  81 */       return;  MagicCosmetics plugin = MagicCosmetics.getInstance();
/*     */     
/*  83 */     if (slot == 5) {
/*  84 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  85 */       Hat hat = playerData.getHat();
/*  86 */       if (hat == null)
/*  87 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.HAT, (Cosmetic)hat, CraftItemStack.asBukkitCopy(itemStack));
/*  88 */     } else if (slot == 45) {
/*  89 */       PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)this.player.getBukkitEntity());
/*  90 */       WStick wStick = playerData.getWStick();
/*  91 */       if (wStick == null)
/*  92 */         return;  event = new CosmeticInventoryUpdateEvent((Player)this.player.getBukkitEntity(), CosmeticType.WALKING_STICK, (Cosmetic)wStick, CraftItemStack.asBukkitCopy(itemStack));
/*     */     } else {
/*     */       return;
/*     */     } 
/*  96 */     plugin.getServer().getScheduler().runTask((Plugin)plugin, () -> plugin.getServer().getPluginManager().callEvent((Event)event));
/*     */   }
/*     */   
/*     */   private PacketPlayOutMount handleEntityMount(PacketPlayOutMount packetPlayOutMount) {
/* 100 */     int id = getIntPacket(packetPlayOutMount, "a");
/* 101 */     int[] ids = getArrayIntsPacket(packetPlayOutMount, "b");
/* 102 */     Entity entity = getEntityAsync(this.player.getWorldServer(), id);
/* 103 */     if (!(entity instanceof Player)) return packetPlayOutMount; 
/* 104 */     Player otherPlayer = (Player)entity;
/* 105 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 106 */     if (playerData.getBag() == null) return packetPlayOutMount;
/*     */     
/* 108 */     Bag bag = (Bag)playerData.getBag();
/* 109 */     if (bag.getBackpackId() == -1) return packetPlayOutMount; 
/* 110 */     int[] newIds = new int[ids.length + 1];
/* 111 */     newIds[0] = bag.getBackpackId();
/* 112 */     for (int i = 0; i < ids.length; i++) {
/* 113 */       if (ids[i] != bag.getBackpackId())
/* 114 */         newIds[i + 1] = ids[i]; 
/*     */     } 
/* 116 */     PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
/* 117 */     data.d(id);
/* 118 */     data.a(newIds);
/*     */     try {
/* 120 */       packetPlayOutMount.a(data);
/* 121 */     } catch (IOException iOException) {}
/* 122 */     return packetPlayOutMount;
/*     */   }
/*     */   
/*     */   private void handleEntitySpawn(int id) {
/* 126 */     Entity entity = getEntityAsync(this.player.getWorldServer(), id);
/* 127 */     if (!(entity instanceof Player))
/* 128 */       return;  Player otherPlayer = (Player)entity;
/* 129 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 130 */     if (playerData == null)
/* 131 */       return;  if (playerData.getBag() == null)
/* 132 */       return;  Bukkit.getServer().getScheduler().runTask((Plugin)MagicCosmetics.getInstance(), () -> playerData.getBag().spawn((Player)this.player.getBukkitEntity()));
/*     */   }
/*     */   
/*     */   private void handleEntityDespawn(int id) {
/* 136 */     Entity entity = getEntityAsync(this.player.getWorldServer(), id);
/* 137 */     if (!(entity instanceof Player))
/* 138 */       return;  Player otherPlayer = (Player)entity;
/* 139 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)otherPlayer);
/* 140 */     if (playerData == null)
/* 141 */       return;  if (playerData.getBag() == null)
/* 142 */       return;  playerData.getBag().despawn((Player)this.player.getBukkitEntity());
/*     */   }
/*     */   
/*     */   protected Entity getEntityAsync(WorldServer world, int id) {
/* 146 */     Entity entity = (Entity)world.entitiesById.get(id);
/* 147 */     return (entity == null) ? null : (Entity)entity.getBukkitEntity();
/*     */   }
/*     */   
/*     */   public int getIntPacket(Object packet, String fieldName) {
/*     */     try {
/* 152 */       Field field = packet.getClass().getDeclaredField(fieldName);
/* 153 */       field.setAccessible(true);
/* 154 */       return field.getInt(packet);
/* 155 */     } catch (IllegalAccessException|NoSuchFieldException e) {
/* 156 */       e.printStackTrace();
/*     */       
/* 158 */       return -1;
/*     */     } 
/*     */   }
/*     */   public int[] getArrayIntsPacket(Object packet, String fieldName) {
/*     */     try {
/* 163 */       Field field = packet.getClass().getDeclaredField(fieldName);
/* 164 */       field.setAccessible(true);
/* 165 */       Object arrayObject = field.get(packet);
/* 166 */       if (arrayObject != null && arrayObject.getClass().isArray()) {
/* 167 */         int length = Array.getLength(arrayObject);
/* 168 */         int[] result = new int[length];
/* 169 */         for (int i = 0; i < length; i++) {
/* 170 */           result[i] = ((Integer)Array.get(arrayObject, i)).intValue();
/*     */         }
/* 172 */         return result;
/*     */       } 
/* 174 */     } catch (IllegalAccessException|NoSuchFieldException e) {
/* 175 */       e.printStackTrace();
/*     */     } 
/* 177 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\models\MCChannelHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */