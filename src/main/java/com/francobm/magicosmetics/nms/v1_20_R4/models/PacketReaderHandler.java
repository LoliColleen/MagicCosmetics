/*    */ package com.francobm.magicosmetics.nms.v1_20_R4.models;
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
/*    */ import net.minecraft.server.network.ServerCommonPacketListenerImpl;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class PacketReaderHandler
/*    */   extends PacketReader
/*    */ {
/*    */   public void injectPlayer(Player player) {
/* 22 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 23 */     MCChannelHandler cdh = new MCChannelHandler(entityPlayer);
/* 24 */     ChannelPipeline pipeline = getPrivateChannelPipeline(entityPlayer.c);
/* 25 */     if (pipeline == null)
/* 26 */       return;  for (String name : pipeline.toMap().keySet()) {
/* 27 */       if (pipeline.get(name) instanceof NetworkManager) {
/* 28 */         pipeline.addBefore(name, "magic_cosmetics_packet_handler", (ChannelHandler)cdh);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void removePlayer(Player player) {
/* 35 */     CraftPlayer craftPlayer = (CraftPlayer)player;
/* 36 */     Channel channel = getPrivateChannel((craftPlayer.getHandle()).c);
/* 37 */     if (channel == null)
/* 38 */       return;  channel.eventLoop().submit(() -> channel.pipeline().remove("magic_cosmetics_packet_handler"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ChannelPipeline getPrivateChannelPipeline(PlayerConnection playerConnection) {
/* 44 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 45 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 46 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 47 */       String methodName = "getConnection";
/*    */       try {
/* 49 */         Class<?> clazz = Class.forName(className);
/* 50 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 51 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 52 */         Object[] parameters = { playerConnection.f };
/* 53 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 54 */         return result.n.pipeline();
/* 55 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException exception) {
/*    */         
/* 57 */         throw new RuntimeException(exception);
/*    */       } 
/*    */     } 
/*    */     try {
/* 61 */       Field privateNetworkManager = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
/* 62 */       privateNetworkManager.setAccessible(true);
/* 63 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 64 */       return networkManager.n.pipeline();
/* 65 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 66 */       Bukkit.getLogger().severe("Error: Channel Pipeline not found");
/* 67 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private Channel getPrivateChannel(PlayerConnection playerConnection) {
/* 72 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 73 */     if (plugin.getServer().getPluginManager().isPluginEnabled("Denizen")) {
/* 74 */       String className = "com.denizenscript.denizen.nms.v1_20.impl.network.handlers.DenizenNetworkManagerImpl";
/* 75 */       String methodName = "getConnection";
/*    */       try {
/* 77 */         Class<?> clazz = Class.forName(className);
/* 78 */         Class<?>[] typeParameters = new Class[] { EntityPlayer.class };
/* 79 */         Method method = clazz.getMethod(methodName, typeParameters);
/* 80 */         Object[] parameters = { playerConnection.f };
/* 81 */         NetworkManager result = (NetworkManager)method.invoke(null, parameters);
/* 82 */         return result.n;
/* 83 */       } catch (ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException exception) {
/*    */         
/* 85 */         throw new RuntimeException(exception);
/*    */       } 
/*    */     } 
/*    */     try {
/* 89 */       Field privateNetworkManager = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
/* 90 */       privateNetworkManager.setAccessible(true);
/* 91 */       NetworkManager networkManager = (NetworkManager)privateNetworkManager.get(playerConnection);
/* 92 */       return networkManager.n;
/* 93 */     } catch (NoSuchFieldException|IllegalAccessException e) {
/* 94 */       Bukkit.getLogger().severe("Error: Channel not found");
/* 95 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_20_R4\models\PacketReaderHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */