/*    */ package com.francobm.magicosmetics.nms.v1_16_R3.models;
/*    */ 
/*    */ import com.francobm.magicosmetics.models.PacketReader;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import net.minecraft.server.v1_16_R3.EntityPlayer;
/*    */ import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class PacketReaderHandler extends PacketReader {
/*    */   public void injectPlayer(Player player) {
/* 13 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 14 */     MCChannelHandler cdh = new MCChannelHandler(entityPlayer);
/* 15 */     ChannelPipeline pipeline = entityPlayer.playerConnection.networkManager.channel.pipeline();
/* 16 */     for (String name : pipeline.toMap().keySet()) {
/* 17 */       if (pipeline.get(name) instanceof net.minecraft.server.v1_16_R3.NetworkManager) {
/* 18 */         pipeline.addBefore(name, "magic_cosmetics_packet_handler", (ChannelHandler)cdh);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void removePlayer(Player player) {
/* 25 */     Channel channel = (((CraftPlayer)player).getHandle()).playerConnection.networkManager.channel;
/* 26 */     if (channel == null)
/* 27 */       return;  channel.eventLoop().submit(() -> channel.pipeline().remove("magic_cosmetics_packet_handler"));
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_16_R3\models\PacketReaderHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */