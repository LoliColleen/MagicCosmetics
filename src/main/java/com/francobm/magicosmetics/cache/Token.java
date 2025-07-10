/*     */ package com.francobm.magicosmetics.cache;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.api.Cosmetic;
/*     */ import com.francobm.magicosmetics.api.TokenType;
/*     */ import com.francobm.magicosmetics.files.FileCreator;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.attribute.Attribute;
/*     */ import org.bukkit.attribute.AttributeModifier;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class Token {
/*  23 */   public static Map<String, Token> tokens = new HashMap<>();
/*     */   private final String id;
/*     */   private String tokenNBT;
/*     */   private final ItemStack itemStack;
/*     */   private final String cosmetic;
/*     */   private final TokenType tokenType;
/*     */   private final boolean exchangeable;
/*     */   
/*     */   public Token(String id, ItemStack itemStack, String cosmetic, TokenType tokenType, boolean exchangeable) {
/*  32 */     this.id = id;
/*  33 */     this.itemStack = itemStack;
/*  34 */     this.cosmetic = cosmetic;
/*  35 */     this.exchangeable = exchangeable;
/*  36 */     this.tokenType = tokenType;
/*     */   }
/*     */   
/*     */   public static Token getToken(String id) {
/*  40 */     return tokens.get(id);
/*     */   }
/*     */   
/*     */   public static Token getTokenByCosmetic(String cosmeticId) {
/*  44 */     for (Token token : tokens.values()) {
/*  45 */       if (token.getCosmetic().equalsIgnoreCase(cosmeticId)) {
/*  46 */         return token;
/*     */       }
/*     */     } 
/*  49 */     return null;
/*     */   }
/*     */   
/*     */   public static Token getTokenByItem(ItemStack itemStack) {
/*  53 */     for (Token token : tokens.values()) {
/*  54 */       if (!token.isNewToken(itemStack))
/*  55 */         continue;  return token;
/*     */     } 
/*  57 */     return null;
/*     */   }
/*     */   
/*     */   public static Token getOldTokenByItem(ItemStack itemStack) {
/*  61 */     for (Token token : tokens.values()) {
/*  62 */       if (!token.isOldToken(itemStack))
/*  63 */         continue;  return token;
/*     */     } 
/*  65 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean removeToken(Player player, ItemStack itemStack) {
/*  69 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  70 */     Token token = getTokenByItem(itemStack);
/*  71 */     if (token == null) return false; 
/*  72 */     PlayerData playerData = PlayerData.getPlayer((OfflinePlayer)player);
/*  73 */     if (itemStack.getAmount() < token.getItemStack().getAmount()) {
/*  74 */       Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/*  75 */       return false;
/*     */     } 
/*  77 */     if (plugin.isPermissions()) {
/*  78 */       Cosmetic cosmetic = Cosmetic.getCosmetic(token.getCosmetic());
/*  79 */       if (!cosmetic.hasPermission(player)) return true; 
/*  80 */       Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/*  81 */       return false;
/*     */     } 
/*  83 */     if (playerData.getCosmeticById(token.getCosmetic()) != null) {
/*  84 */       Utils.sendMessage((CommandSender)player, plugin.prefix + plugin.prefix);
/*  85 */       return false;
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public static void loadTokens() {
/*  91 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/*  92 */     tokens.clear();
/*  93 */     FileCreator token = plugin.getTokens();
/*  94 */     int tokens_count = 0;
/*  95 */     for (String key : token.getConfigurationSection("tokens").getKeys(false)) {
/*  96 */       String display = "";
/*  97 */       int amount = 1;
/*  98 */       String material = "";
/*  99 */       ItemStack itemStack = null;
/* 100 */       List<String> lore = new ArrayList<>();
/* 101 */       boolean unbreakable = false;
/* 102 */       boolean glow = false;
/* 103 */       boolean hide_attributes = false;
/* 104 */       int modelData = 0;
/* 105 */       String cosmetic = "";
/* 106 */       TokenType type = null;
/* 107 */       boolean exchangeable = true;
/* 108 */       if (token.contains("tokens." + key + ".item.display")) {
/* 109 */         display = token.getString("tokens." + key + ".item.display");
/* 110 */         if (plugin.isItemsAdder())
/* 111 */           display = plugin.getItemsAdder().replaceFontImages(display); 
/* 112 */         if (plugin.isOraxen())
/* 113 */           display = plugin.getOraxen().replaceFontImages(display); 
/*     */       } 
/* 115 */       if (token.contains("tokens." + key + ".item.amount")) {
/* 116 */         amount = token.getInt("tokens." + key + ".item.amount");
/*     */       }
/* 118 */       if (token.contains("tokens." + key + ".item.material")) {
/* 119 */         material = token.getString("tokens." + key + ".item.material");
/*     */         try {
/* 121 */           itemStack = XMaterial.valueOf(material.toUpperCase()).parseItem();
/* 122 */         } catch (IllegalArgumentException exception) {
/* 123 */           plugin.getLogger().warning("Item '" + key + "' material: " + material + " Not Found.");
/*     */         } 
/*     */       } 
/* 126 */       if (token.contains("tokens." + key + ".item.lore")) {
/* 127 */         lore = token.getStringList("tokens." + key + ".item.lore");
/* 128 */         if (plugin.isItemsAdder()) {
/* 129 */           List<String> lore2 = new ArrayList<>();
/* 130 */           for (String l : lore) {
/* 131 */             lore2.add(plugin.getItemsAdder().replaceFontImages(l));
/*     */           }
/* 133 */           lore.clear();
/* 134 */           lore.addAll(lore2);
/*     */         } 
/* 136 */         if (plugin.isOraxen()) {
/* 137 */           List<String> lore2 = new ArrayList<>();
/* 138 */           for (String l : lore) {
/* 139 */             lore2.add(plugin.getOraxen().replaceFontImages(l));
/*     */           }
/* 141 */           lore.clear();
/* 142 */           lore.addAll(lore2);
/*     */         } 
/*     */       } 
/* 145 */       if (token.contains("tokens." + key + ".item.unbreakable")) {
/* 146 */         unbreakable = token.getBoolean("tokens." + key + ".item.unbreakable");
/*     */       }
/* 148 */       if (token.contains("tokens." + key + ".item.glow")) {
/* 149 */         glow = token.getBoolean("tokens." + key + ".item.glow");
/*     */       }
/* 151 */       if (token.contains("tokens." + key + ".item.hide-attributes")) {
/* 152 */         hide_attributes = token.getBoolean("tokens." + key + ".item.hide-attributes");
/*     */       }
/* 154 */       if (token.contains("tokens." + key + ".item.modeldata")) {
/* 155 */         modelData = token.getInt("tokens." + key + ".item.modeldata");
/*     */       }
/* 157 */       if (token.contains("tokens." + key + ".item.item-adder")) {
/* 158 */         if (!plugin.isItemsAdder()) {
/* 159 */           plugin.getLogger().warning("Item Adder plugin Not Found skipping Token Item '" + key + "'");
/*     */           continue;
/*     */         } 
/* 162 */         String id = token.getString("tokens." + key + ".item.item-adder");
/* 163 */         ItemStack ia = plugin.getItemsAdder().getCustomItemStack(id);
/* 164 */         if (ia == null) {
/* 165 */           plugin.getLogger().warning("Item Adder '" + id + "' Not Found skipping...");
/*     */           continue;
/*     */         } 
/* 168 */         itemStack = ia.clone();
/* 169 */         modelData = -1;
/*     */       } 
/* 171 */       if (token.contains("tokens." + key + ".item.oraxen")) {
/* 172 */         if (!plugin.isOraxen()) {
/* 173 */           plugin.getLogger().warning("Oraxen plugin Not Found skipping Token Item '" + key + "'");
/*     */           continue;
/*     */         } 
/* 176 */         String id = token.getString("tokens." + key + ".item.oraxen");
/* 177 */         ItemStack oraxen = plugin.getOraxen().getItemStackById(id);
/* 178 */         if (oraxen == null) {
/* 179 */           plugin.getLogger().warning("Oraxen '" + id + "' Not Found skipping...");
/*     */           continue;
/*     */         } 
/* 182 */         itemStack = oraxen.clone();
/* 183 */         modelData = -1;
/*     */       } 
/* 185 */       if (token.contains("tokens." + key + ".cosmetic")) {
/* 186 */         cosmetic = token.getString("tokens." + key + ".cosmetic");
/*     */       }
/* 188 */       if (token.contains("tokens." + key + ".type")) {
/* 189 */         String tokenType = token.getString("tokens." + key + ".type");
/*     */         try {
/* 191 */           type = TokenType.valueOf(tokenType.toUpperCase());
/* 192 */         } catch (IllegalArgumentException exception) {
/* 193 */           plugin.getLogger().warning("The token type you entered does not exist!");
/*     */         } 
/*     */       } 
/* 196 */       if (token.contains("tokens." + key + ".exchangeable")) {
/* 197 */         exchangeable = token.getBoolean("tokens." + key + ".exchangeable");
/*     */       }
/* 199 */       if (itemStack == null)
/* 200 */         return;  itemStack.setAmount(amount);
/* 201 */       ItemMeta itemMeta = itemStack.getItemMeta();
/* 202 */       if (itemMeta == null)
/* 203 */         return;  itemMeta.setDisplayName(display);
/* 204 */       itemMeta.setLore(lore);
/* 205 */       if (glow) {
/* 206 */         itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
/* 207 */         itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
/*     */       } 
/* 209 */       if (hide_attributes) {
/* 210 */         itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE });
/*     */       }
/* 212 */       itemMeta.setUnbreakable(unbreakable);
/* 213 */       if (modelData != -1) {
/* 214 */         itemMeta.setCustomModelData(Integer.valueOf(modelData));
/*     */       }
/* 216 */       if (Utils.isNewerThan1206()) {
/* 217 */         itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("foo", 0.0D, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
/*     */       }
/* 219 */       itemStack.setItemMeta(itemMeta);
/* 220 */       itemStack = plugin.getVersion().setNBTCosmetic(itemStack, "key:" + key);
/* 221 */       Token tk = new Token(key, itemStack, cosmetic, type, exchangeable);
/* 222 */       tk.tokenNBT = "key:" + key;
/* 223 */       tokens.put(key, tk);
/* 224 */       tokens_count++;
/*     */     } 
/* 226 */     plugin.getLogger().info("Registered tokens: " + tokens_count);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 230 */     return this.id;
/*     */   }
/*     */   
/*     */   public ItemStack getItemStack() {
/* 234 */     return this.itemStack;
/*     */   }
/*     */   
/*     */   public boolean isNewToken(ItemStack itemStack) {
/* 238 */     if (itemStack == null) return false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     String key = MagicCosmetics.getInstance().getVersion().isNBTCosmetic(itemStack);
/* 250 */     return (key != null && !key.isEmpty() && key.equalsIgnoreCase(this.tokenNBT));
/*     */   }
/*     */   
/*     */   public boolean isOldToken(ItemStack itemStack) {
/* 254 */     if (itemStack == null) return false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     String key = MagicCosmetics.getInstance().getVersion().isNBTCosmetic(itemStack);
/* 266 */     return (key != null && !key.isEmpty() && key.equalsIgnoreCase(this.id));
/*     */   }
/*     */   
/*     */   public TokenType getTokenType() {
/* 270 */     return this.tokenType;
/*     */   }
/*     */   
/*     */   public String getCosmetic() {
/* 274 */     return this.cosmetic;
/*     */   }
/*     */   
/*     */   public boolean isExchangeable() {
/* 278 */     return this.exchangeable;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\Token.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */