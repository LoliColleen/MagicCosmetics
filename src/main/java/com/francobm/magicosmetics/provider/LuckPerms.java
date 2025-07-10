package com.francobm.magicosmetics.provider;

import com.francobm.magicosmetics.MagicCosmetics;
import com.francobm.magicosmetics.api.Cosmetic;
import com.francobm.magicosmetics.cache.PlayerData;
import java.util.UUID;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LuckPerms {
  private final net.luckperms.api.LuckPerms luckPermsAPI;
  
  private final MagicCosmetics plugin = MagicCosmetics.getInstance();
  
  public LuckPerms() {
    this.luckPermsAPI = LuckPermsProvider.get();
    this.luckPermsAPI.getEventBus().subscribe(this.plugin, NodeRemoveEvent.class, this::onNodeRemove);
  }
  
  public void addPermission(UUID uniqueId, String permission) {
    User user = this.luckPermsAPI.getUserManager().getUser(uniqueId);
    if (user == null)
      return; 
    if (this.plugin.getLuckPermsServer() == null || this.plugin.getLuckPermsServer().isEmpty()) {
      user.data().add((Node)PermissionNode.builder(permission).build());
      this.luckPermsAPI.getUserManager().saveUser(user);
      return;
    } 
    user.data().add((Node)((PermissionNode.Builder)PermissionNode.builder(permission).withContext("server", this.plugin.getLuckPermsServer())).build());
    this.luckPermsAPI.getUserManager().saveUser(user);
  }
  
  public void removePermission(UUID uniqueId, String permission) {
    this.luckPermsAPI.getUserManager().modifyUser(uniqueId, user -> {
          if (this.plugin.getLuckPermsServer() == null || this.plugin.getLuckPermsServer().isEmpty()) {
            PermissionNode permissionNode1 = (PermissionNode)PermissionNode.builder(permission).build();
            user.data().remove((Node)permissionNode1);
            return;
          } 
          PermissionNode permissionNode = (PermissionNode)((PermissionNode.Builder)PermissionNode.builder(permission).withContext("server", this.plugin.getLuckPermsServer())).build();
          user.data().remove((Node)permissionNode);
        });
  }
  
  public boolean isExpirePermission(UUID uniqueId, String permission) {
    User user = this.luckPermsAPI.getUserManager().getUser(uniqueId);
    if (user == null)
      return false; 
    boolean hasPermission = user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    if (hasPermission)
      return false; 
    removePermission(uniqueId, permission);
    return true;
  }
  
  private void onNodeRemove(NodeRemoveEvent event) {
    if (!event.isUser())
      return; 
    Node node = event.getNode();
    if (!(node instanceof PermissionNode))
      return; 
    PermissionNode permissionNode = (PermissionNode)node;
    User user = (User)event.getTarget();
    Player player = Bukkit.getPlayer(user.getUniqueId());
    if (player == null)
      return; 
    PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
    for (Cosmetic cosmetic : playerData.cosmeticsInUse()) {
      if (!permissionNode.getPermission().equalsIgnoreCase(cosmetic.getPermission()))
        continue; 
      this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> playerData.removeCosmetic(cosmetic.getId()));
    } 
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\provider\LuckPerms.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */