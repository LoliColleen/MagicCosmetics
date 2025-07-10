/*    */ package com.francobm.magicosmetics.nms.v1_19_R1.models;
/*    */ 
/*    */ import com.francobm.magicosmetics.models.PacketReader;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import net.minecraft.server.level.EntityPlayer;
/*    */ import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class PacketReaderHandler
/*    */   extends PacketReader {
/*    */   public void injectPlayer(Player player) {
/* 14 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 15 */     MCChannelHandler cdh = new MCChannelHandler(entityPlayer);
/* 16 */     ChannelPipeline pipeline = entityPlayer.b.b.m.pipeline();
/* 17 */     for (String name : pipeline.toMap().keySet()) {
/* 18 */       if (pipeline.get(name) instanceof net.minecraft.network.NetworkManager) {
/* 19 */         pipeline.addBefore(name, "magic_cosmetics_packet_handler", (ChannelHandler)cdh);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void removePlayer(Player player) {
/* 26 */     Channel channel = (((CraftPlayer)player).getHandle()).b.b.m;
/* 27 */     if (channel == null)
/* 28 */       return;  channel.eventLoop().submit(() -> channel.pipeline().remove("magic_cosmetics_packet_handler"));
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R1\models\PacketReaderHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */