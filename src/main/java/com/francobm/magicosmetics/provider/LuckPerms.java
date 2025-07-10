/*    */ package com.francobm.magicosmetics.provider;
/*    */ 
/*    */ import com.francobm.magicosmetics.MagicCosmetics;
/*    */ import com.francobm.magicosmetics.api.Cosmetic;
/*    */ import com.francobm.magicosmetics.cache.PlayerData;
/*    */ import java.util.UUID;
/*    */ import net.luckperms.api.LuckPermsProvider;
/*    */ import net.luckperms.api.event.node.NodeRemoveEvent;
/*    */ import net.luckperms.api.model.user.User;
/*    */ import net.luckperms.api.node.Node;
/*    */ import net.luckperms.api.node.types.PermissionNode;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public class LuckPerms {
/*    */   private final net.luckperms.api.LuckPerms luckPermsAPI;
/* 19 */   private final MagicCosmetics plugin = MagicCosmetics.getInstance();
/*    */   
/*    */   public LuckPerms() {
/* 22 */     this.luckPermsAPI = LuckPermsProvider.get();
/* 23 */     this.luckPermsAPI.getEventBus().subscribe(this.plugin, NodeRemoveEvent.class, this::onNodeRemove);
/*    */   }
/*    */   
/*    */   public void addPermission(UUID uniqueId, String permission) {
/* 27 */     User user = this.luckPermsAPI.getUserManager().getUser(uniqueId);
/* 28 */     if (user == null)
/* 29 */       return;  if (this.plugin.getLuckPermsServer() == null || this.plugin.getLuckPermsServer().isEmpty()) {
/* 30 */       user.data().add((Node)PermissionNode.builder(permission).build());
/* 31 */       this.luckPermsAPI.getUserManager().saveUser(user);
/*    */       return;
/*    */     } 
/* 34 */     user.data().add((Node)((PermissionNode.Builder)PermissionNode.builder(permission).withContext("server", this.plugin.getLuckPermsServer())).build());
/* 35 */     this.luckPermsAPI.getUserManager().saveUser(user);
/*    */   }
/*    */   
/*    */   public void removePermission(UUID uniqueId, String permission) {
/* 39 */     this.luckPermsAPI.getUserManager().modifyUser(uniqueId, user -> {
/*    */           if (this.plugin.getLuckPermsServer() == null || this.plugin.getLuckPermsServer().isEmpty()) {
/*    */             PermissionNode permissionNode1 = (PermissionNode)PermissionNode.builder(permission).build();
/*    */             user.data().remove((Node)permissionNode1);
/*    */             return;
/*    */           } 
/*    */           PermissionNode permissionNode = (PermissionNode)((PermissionNode.Builder)PermissionNode.builder(permission).withContext("server", this.plugin.getLuckPermsServer())).build();
/*    */           user.data().remove((Node)permissionNode);
/*    */         });
/*    */   }
/*    */   
/*    */   public boolean isExpirePermission(UUID uniqueId, String permission) {
/* 51 */     User user = this.luckPermsAPI.getUserManager().getUser(uniqueId);
/* 52 */     if (user == null) return false; 
/* 53 */     boolean hasPermission = user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
/* 54 */     if (hasPermission) return false; 
/* 55 */     removePermission(uniqueId, permission);
/* 56 */     return true;
/*    */   }
/*    */   
/*    */   private void onNodeRemove(NodeRemoveEvent event) {
/* 60 */     if (!event.isUser())
/* 61 */       return;  Node node = event.getNode();
/* 62 */     if (!(node instanceof PermissionNode))
/* 63 */       return;  PermissionNode permissionNode = (PermissionNode)node;
/* 64 */     User user = (User)event.getTarget();
/* 65 */     Player player = Bukkit.getPlayer(user.getUniqueId());
/* 66 */     if (player == null)
/* 67 */       return;  PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/* 68 */     for (Cosmetic cosmetic : playerData.cosmeticsInUse()) {
/* 69 */       if (!permissionNode.getPermission().equalsIgnoreCase(cosmetic.getPermission()))
/* 70 */         continue;  this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> playerData.removeCosmetic(cosmetic.getId()));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\LuckPerms.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */