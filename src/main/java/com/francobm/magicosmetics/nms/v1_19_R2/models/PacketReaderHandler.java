/*    */ package com.francobm.magicosmetics.nms.v1_19_R2.models;
/*    */ 
/*    */ import com.francobm.magicosmetics.models.PacketReader;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import net.minecraft.server.level.EntityPlayer;
/*    */ import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class PacketReaderHandler
/*    */   extends PacketReader
/*    */ {
/*    */   public void injectPlayer(Player player) {
/* 15 */     EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
/* 16 */     MCChannelHandler cdh = new MCChannelHandler(entityPlayer);
/* 17 */     ChannelPipeline pipeline = entityPlayer.b.b.m.pipeline();
/* 18 */     for (String name : pipeline.toMap().keySet()) {
/* 19 */       if (pipeline.get(name) instanceof net.minecraft.network.NetworkManager) {
/* 20 */         pipeline.addBefore(name, "magic_cosmetics_packet_handler", (ChannelHandler)cdh);
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void removePlayer(Player player) {
/* 27 */     Channel channel = (((CraftPlayer)player).getHandle()).b.b.m;
/* 28 */     if (channel == null)
/* 29 */       return;  channel.eventLoop().submit(() -> channel.pipeline().remove("magic_cosmetics_packet_handler"));
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\nms\v1_19_R2\models\PacketReaderHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */