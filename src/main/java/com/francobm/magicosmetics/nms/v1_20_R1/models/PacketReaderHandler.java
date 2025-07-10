/*    */ package com.francobm.magicosmetics.nms.v1_20_R1.models;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.models.PacketReader;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.server.level.EntityPlayer;
/*    */ import net.minecraft.server.network.PlayerConnection;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class PacketReaderHandler
/*    */   extends PacketReader
/*    */ {
/*    */   public void injectPlayer(Player player) {
/* 21 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 22 */     MCChannelHandler cdh = new MCChannelHandler(entityPlayer);
/* 23 */     ChannelPipeline pipeline = getPrivateChannelPipeline(entityPlayer.c);
/* 24 */     if (pipeline == null)
/* 25 */       return;  for (String name : pipeline.toMap().keySet()) {
/* 26 */       if (pipeline.get(name) instanceof NetworkManager) {
/* 27 */         pipeline.addBefore(name, "magic_cosmetics_packet_handler", (ChannelHandler)cdh);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void removePlayer(Player player) {
/* 34 */     CraftPlayer craftPlayer = (CraftPlayer)player;
/* 35 */     Channel channel = getPrivateChannel((craftPlayer.getHandle()).c);
/* 36 */     if (channel == null)
/* 37 */       return;  channel.eventLoop().submit(() -> channel.pipeline().remove("magic_cosmetics_packet_handler"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 43 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 44 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 45 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 46 */       String methodName = "getConnection";
/*    */       try {
/* 48 */         Class<?> clazz = Class.forName(className);
/* 49 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 50 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 51 */         Object[] parameters = { playerConnection.b };
/* 52 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 53 */         return result.m.pipeline();
/* 54 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException exception) {
/*    */         
/* 56 */         throw new RuntimeException(exception);
/*    */       } 
/*    */     } 
/*    */     try {
/* 60 */       Field privateNetworkManager = playerConnection.getClass().getDeclaredField("h");
/* 61 */       privateNetworkManager.setAccessible(true);
/* 62 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 63 */       return networkManager.m.pipeline();
/* 64 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 65 */       Bukkit.getLogger().severe("Error: Channel Pipeline not found");
/* 66 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private Channel getPrivateChannel(PlayerConnection playerConnection) {
/* 71 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 72 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 73 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 74 */       String methodName = "getConnection";
/*    */       try {
/* 76 */         Class<?> clazz = Class.forName(className);
/* 77 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 78 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 79 */         Object[] parameters = { playerConnection.b };
/* 80 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 81 */         return result.m;
/* 82 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException exception) {
/*    */         
/* 84 */         throw new RuntimeException(exception);
/*    */       } 
/*    */     } 
/*    */     try {
/* 88 */       Field privateNetworkManager = playerConnection.getClass().getDeclaredField("h");
/* 89 */       privateNetworkManager.setAccessible(true);
/* 90 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 91 */       return networkManager.m;
/* 92 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 93 */       Bukkit.getLogger().severe("Error: Channel not found");
/* 94 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R1\models\PacketReaderHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */