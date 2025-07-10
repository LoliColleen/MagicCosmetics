/*     */ package com.francobm.magicosmetics.cache.inventories.menus;
/*     */ import com.francobm.magicosmetics.MagicCosmetics;
/*     */ import com.francobm.magicosmetics.cache.Color;
/*     */ import com.francobm.magicosmetics.cache.PlayerData;
/*     */ import com.francobm.magicosmetics.cache.SecondaryColor;
/*     */ import com.francobm.magicosmetics.cache.Sound;
/*     */ import com.francobm.magicosmetics.cache.inventories.ActionType;
/*     */ import com.francobm.magicosmetics.cache.inventories.ContentMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.Menu;
/*     */ import com.francobm.magicosmetics.cache.inventories.PaginatedMenu;
/*     */ import com.francobm.magicosmetics.cache.inventories.SlotMenu;
/*     */ import com.francobm.magicosmetics.cache.items.Items;
/*     */ import com.francobm.magicosmetics.utils.Utils;
/*     */ import com.francobm.magicosmetics.utils.XMaterial;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryType;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class FreeColoredMenu extends PaginatedMenu {
/*     */   private Color color;
/*     */   private SecondaryColor secondaryColor;
/*     */   
/*     */   public FreeColoredMenu(String id, ContentMenu contentMenu, int startSlot, int endSlot, Set<Integer> backSlot, Set<Integer> nextSlot, int pagesSlot, List<Integer> slotsUnavailable, Items containItem, List<String> unavailableColors) {
/*  32 */     super(id, contentMenu, startSlot, endSlot, backSlot, nextSlot, pagesSlot, slotsUnavailable);
/*  33 */     this.containItem = containItem;
/*  34 */     this.unavailableColors = unavailableColors;
/*     */   }
/*     */   private ItemStack itemStack; private Items containItem; private List<String> unavailableColors;
/*     */   public FreeColoredMenu(String id, ContentMenu contentMenu) {
/*  38 */     super(id, contentMenu);
/*     */   }
/*     */   
/*     */   public FreeColoredMenu(PlayerData playerData, Menu menu, Color color) {
/*  42 */     super(playerData, menu);
/*  43 */     this.color = color;
/*  44 */     this.containItem = ((FreeColoredMenu)menu).getContainItem();
/*  45 */     this.secondaryColor = color.getSecondaryColors().get(0);
/*     */   }
/*     */   
/*     */   public FreeColoredMenu getClone(PlayerData playerData, Color color, ItemStack itemStack) {
/*  49 */     FreeColoredMenu freeColoredMenu = new FreeColoredMenu(getId(), getContentMenu().getClone(), getStartSlot(), getEndSlot(), getBackSlot(), getNextSlot(), getPagesSlot(), getSlotsUnavailable(), getContainItem(), getUnavailableColors());
/*  50 */     freeColoredMenu.playerData = playerData;
/*  51 */     freeColoredMenu.setColor(color);
/*  52 */     freeColoredMenu.itemStack = itemStack;
/*  53 */     freeColoredMenu.secondaryColor = color.getSecondaryColors().get(0);
/*  54 */     return freeColoredMenu;
/*     */   }
/*     */   
/*     */   public FreeColoredMenu getClone(PlayerData playerData, Color color) {
/*  58 */     FreeColoredMenu freeColoredMenu = new FreeColoredMenu(getId(), getContentMenu().getClone(), getStartSlot(), getEndSlot(), getBackSlot(), getNextSlot(), getPagesSlot(), getSlotsUnavailable(), getContainItem(), getUnavailableColors());
/*  59 */     freeColoredMenu.playerData = playerData;
/*  60 */     freeColoredMenu.setColor(color);
/*  61 */     freeColoredMenu.secondaryColor = color.getSecondaryColors().get(0);
/*  62 */     return freeColoredMenu;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleMenu(InventoryClickEvent event) {
/*  68 */     Player player = (Player)event.getWhoClicked();
/*  69 */     if (event.getClickedInventory() == null)
/*  70 */       return;  int slot = event.getSlot();
/*  71 */     if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
/*  72 */       if (event.getClick() != ClickType.LEFT) {
/*  73 */         event.setCancelled(true);
/*     */         return;
/*     */       } 
/*     */       return;
/*     */     } 
/*  78 */     if (event.getClick() != ClickType.LEFT) {
/*  79 */       event.setCancelled(true);
/*     */       return;
/*     */     } 
/*  82 */     if (getContentMenu().getPreviewSlot() == slot) {
/*  83 */       if (event.getCursor().getType() != XMaterial.AIR.parseMaterial()) {
/*  84 */         if (this.containItem != null) {
/*  85 */           if (this.containItem.isColored(event.getCursor())) {
/*  86 */             this.itemStack = event.getCursor().clone();
/*  87 */             SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), new Items(this.itemStack), "", new ActionType[0]);
/*  88 */             getContentMenu().addSlotMenu(slotMenu1);
/*  89 */             setResultItem();
/*     */             return;
/*     */           } 
/*  92 */           event.setCancelled(true);
/*     */           return;
/*     */         } 
/*  95 */         if (Utils.isDyeable(event.getCursor()) && this.color.hasPermission(player)) {
/*  96 */           this.itemStack = event.getCursor().clone();
/*  97 */           SlotMenu slotMenu1 = new SlotMenu(getContentMenu().getPreviewSlot(), new Items(this.itemStack), "", new ActionType[0]);
/*  98 */           getContentMenu().addSlotMenu(slotMenu1);
/*  99 */           setResultItem();
/*     */           return;
/*     */         } 
/* 102 */         event.setCancelled(true);
/*     */         return;
/*     */       } 
/* 105 */       if (event.getCurrentItem() == null)
/* 106 */         return;  this.itemStack = null;
/* 107 */       getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 108 */       getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/* 109 */       event.getClickedInventory().setItem(getContentMenu().getResultSlot(), XMaterial.AIR.parseItem());
/*     */       return;
/*     */     } 
/* 112 */     if (getContentMenu().getResultSlot() == slot) {
/* 113 */       if (event.getCurrentItem() == null) {
/* 114 */         event.setCancelled(true);
/*     */         return;
/*     */       } 
/* 117 */       this.itemStack = null;
/* 118 */       getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 119 */       getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/* 120 */       event.getClickedInventory().setItem(getContentMenu().getPreviewSlot(), XMaterial.AIR.parseItem());
/*     */       
/*     */       return;
/*     */     } 
/* 124 */     event.setCancelled(true);
/* 125 */     if (event.getCurrentItem() == null)
/* 126 */       return;  SlotMenu slotMenu = getContentMenu().getSlotMenuBySlot(slot);
/* 127 */     if (slotMenu == null)
/* 128 */       return;  if (slotMenu.getItems().getId().endsWith("_colored")) {
/* 129 */       setSecondaryColor(slotMenu.getItems().getColor());
/* 130 */       setItems();
/*     */     } 
/* 132 */     if (getBackSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/* 133 */       slotMenu.playSound(player);
/* 134 */       if (this.page == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 138 */       this.page--;
/* 139 */       setSecondaryColor((SecondaryColor)null);
/* 140 */       setItems();
/*     */       return;
/*     */     } 
/* 143 */     if (getNextSlot().contains(Integer.valueOf(slotMenu.getSlot()))) {
/* 144 */       slotMenu.playSound(player);
/* 145 */       if (this.index + 1 >= this.color.getSecondaryColors().size()) {
/*     */         return;
/*     */       }
/*     */       
/* 149 */       this.page++;
/* 150 */       setSecondaryColor((SecondaryColor)null);
/* 151 */       setItems();
/*     */       return;
/*     */     } 
/* 154 */     slotMenu.action(player);
/*     */   }
/*     */   
/*     */   public void setResultItem() {
/* 158 */     if (this.itemStack == null)
/* 159 */       return;  Items resultItem = (new Items(this.itemStack.clone())).coloredItem(this.secondaryColor.getColor());
/* 160 */     SlotMenu result = new SlotMenu(getContentMenu().getResultSlot(), resultItem, "", new ActionType[0]);
/* 161 */     result.setSound(Sound.getSound("on_click_cosmetic_preview"));
/* 162 */     getContentMenu().addSlotMenu(result);
/* 163 */     setItemInMenu(result);
/*     */     
/* 165 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 166 */       if (!slotMenu.getItems().getId().endsWith("_colored"))
/* 167 */         continue;  Color color = slotMenu.getSlotMenu().getItems().getDyeColor();
/* 168 */       slotMenu.getSlotMenu().setItems((new Items(this.itemStack.clone())).coloredItem(color));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems() {
/* 177 */     resetItems(Arrays.asList(new Integer[] { Integer.valueOf(getContentMenu().getPreviewSlot()), Integer.valueOf(getContentMenu().getResultSlot()) }));
/* 178 */     getContentMenu().getSlots().resetSlots();
/* 179 */     if (!getBackSlot().isEmpty())
/*     */     {
/* 181 */       for (Iterator<Integer> iterator = getBackSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/* 182 */         if (this.page == 0) {
/* 183 */           s = new SlotMenu(slot, Items.getItem("back-button-cancel-template"), this.id, new ActionType[] { ActionType.REFRESH });
/*     */         } else {
/* 185 */           s = new SlotMenu(slot, Items.getItem("back-button-template"), this.id, new ActionType[] { ActionType.REFRESH });
/*     */         } 
/* 187 */         s.setSound(Sound.getSound("on_click_back_page"));
/* 188 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 191 */     if (getPagesSlot() != -1) {
/* 192 */       getContentMenu().addSlotMenu(new SlotMenu(getPagesSlot(), new Items(Items.getItem("pages-template").addVariableItem("%pages%", this.page + 1)), this.id, new ActionType[] { ActionType.CLOSE_MENU }));
/*     */     }
/* 194 */     String sPrimaryColor = setup();
/* 195 */     StringBuilder title = new StringBuilder();
/* 196 */     title.append(getContentMenu().getTitle());
/* 197 */     title.append(sPrimaryColor);
/* 198 */     String[] selected = getSelectedList();
/* 199 */     if (!this.color.getSecondaryColors().isEmpty()) {
/* 200 */       int a = 0;
/* 201 */       for (int i = 0; i < getMaxItemsPerPage(); i++) {
/* 202 */         this.index = getMaxItemsPerPage() * this.page + i;
/* 203 */         if (this.index >= this.color.getSecondaryColors().size())
/* 204 */           break;  SecondaryColor dyeColor = this.color.getSecondaryColors().get(this.index);
/* 205 */         int slot = getStartSlot() + i + a;
/* 206 */         if (dyeColor != null) {
/* 207 */           Items resultItem; if (getSecondaryColor() == null && 
/* 208 */             i == 0) {
/* 209 */             setSecondaryColor(dyeColor);
/*     */           }
/*     */           
/* 212 */           while (this.slotsUnavailable.contains(Integer.valueOf(slot))) {
/* 213 */             slot++;
/* 214 */             a++;
/*     */           } 
/* 216 */           Items items = new Items("" + getPage() + this.index + "_colored", Items.getItem("color-template").colorItem(this.playerData.getOfflinePlayer().getPlayer(), dyeColor, this.secondaryColor));
/* 217 */           items.addPlaceHolder(this.playerData.getOfflinePlayer().getPlayer());
/* 218 */           if (dyeColor.getColor().asRGB() == this.secondaryColor.getColor().asRGB())
/*     */           {
/* 220 */             title.append(selected[i]);
/*     */           }
/*     */           
/* 223 */           if (this.itemStack == null) {
/* 224 */             resultItem = (new Items(XMaterial.AIR.parseItem())).coloredItem(dyeColor.getColor());
/*     */           } else {
/* 226 */             resultItem = (new Items(this.itemStack.clone())).coloredItem(dyeColor.getColor());
/*     */           } 
/* 228 */           SlotMenu result = new SlotMenu(getContentMenu().getResultSlot(), resultItem, "", new ActionType[0]);
/* 229 */           result.setSound(Sound.getSound("on_click_cosmetic_preview"));
/* 230 */           if (i == 0) {
/* 231 */             getContentMenu().addSlotMenu(result);
/*     */           }
/* 233 */           SlotMenu slotMenu = new SlotMenu(slot, items, result, new ActionType[] { ActionType.ADD_ITEM_MENU });
/* 234 */           slotMenu.setSound(Sound.getSound("on_click_item_colored"));
/* 235 */           getContentMenu().addSlotMenu(slotMenu);
/* 236 */           setItemInPaginatedMenu(slotMenu, getPage(), this.index, "_colored");
/*     */         } 
/*     */       } 
/* 239 */     }  if (!getNextSlot().isEmpty())
/*     */     {
/* 241 */       for (Iterator<Integer> iterator = getNextSlot().iterator(); iterator.hasNext(); ) { SlotMenu s; int slot = ((Integer)iterator.next()).intValue();
/* 242 */         if (this.index + 1 >= this.color.getSecondaryColors().size()) {
/* 243 */           s = new SlotMenu(slot, Items.getItem("next-button-cancel-template"), this.id, new ActionType[] { ActionType.REFRESH });
/*     */         } else {
/*     */           
/* 246 */           s = new SlotMenu(slot, Items.getItem("next-button-template"), this.id, new ActionType[] { ActionType.REFRESH });
/*     */         } 
/* 248 */         s.setSound(Sound.getSound("on_click_next_page"));
/* 249 */         getContentMenu().addSlotMenu(s); }
/*     */     
/*     */     }
/* 252 */     for (SlotMenu slotMenu : getContentMenu().getSlotMenu().values()) {
/* 253 */       setItemInPaginatedMenu(slotMenu, -1, -1, "_colored");
/*     */     }
/* 255 */     MagicCosmetics plugin = MagicCosmetics.getInstance();
/* 256 */     if (plugin.isPlaceholderAPI()) {
/* 257 */       plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), plugin.getPlaceholderAPI().setPlaceholders(this.playerData.getOfflinePlayer().getPlayer(), title.toString()));
/*     */       return;
/*     */     } 
/* 260 */     plugin.getVersion().updateTitle(this.playerData.getOfflinePlayer().getPlayer(), title.toString());
/*     */   }
/*     */   
/*     */   private String setup() {
/* 264 */     String title = "";
/*     */ 
/*     */     
/* 267 */     for (Color color : Color.colors.values()) {
/* 268 */       Items items; if (this.unavailableColors.contains(color.getId()))
/* 269 */         continue;  if (color.isPrimaryItem()) {
/* 270 */         items = new Items(color.getId(), Items.getItem("color-template").copyItem(color, this.color));
/*     */       } else {
/* 272 */         items = new Items(color.getId(), Items.getItem("color-template").colorItem(color, this.color));
/*     */       } 
/* 274 */       if (color.getId().equalsIgnoreCase(this.color.getId())) {
/* 275 */         title = color.getSelect();
/*     */       }
/* 277 */       if (!color.getName().isEmpty()) {
/* 278 */         ItemMeta itemMeta = items.getItemStack().getItemMeta();
/* 279 */         if (itemMeta != null) {
/* 280 */           itemMeta.setDisplayName(color.getName());
/*     */         }
/* 282 */         items.getItemStack().setItemMeta(itemMeta);
/*     */       } 
/* 284 */       SlotMenu slotMenu = new SlotMenu(color.getSlot(), items, getId() + "|" + getId(), new ActionType[] { ActionType.OPEN_MENU });
/* 285 */       slotMenu.setSound(Sound.getSound("on_click_item_colored"));
/* 286 */       getContentMenu().addSlotMenu(slotMenu);
/*     */     } 
/* 288 */     return title;
/*     */   }
/*     */   
/*     */   public void setSecondaryColor(Color secondaryColor) {
/* 292 */     this.secondaryColor = new SecondaryColor(secondaryColor);
/*     */   }
/*     */   
/*     */   public void setSecondaryColor(SecondaryColor secondaryColor) {
/* 296 */     this.secondaryColor = secondaryColor;
/*     */   }
/*     */   
/*     */   public ItemStack getItemStack() {
/* 300 */     return this.itemStack;
/*     */   }
/*     */   
/*     */   public Items getContainItem() {
/* 304 */     return this.containItem;
/*     */   }
/*     */   
/*     */   public Color getColor() {
/* 308 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 312 */     this.color = color;
/*     */   }
/*     */   
/*     */   public SecondaryColor getSecondaryColor() {
/* 316 */     return this.secondaryColor;
/*     */   }
/*     */   
/*     */   public String[] getSelectedList() {
/* 320 */     return this.color.getRow().getSelected().split(",");
/*     */   }
/*     */   
/*     */   public void returnItem() {
/* 324 */     if (this.itemStack == null)
/* 325 */       return;  Player player = this.playerData.getOfflinePlayer().getPlayer();
/* 326 */     if (player == null)
/* 327 */       return;  if (player.getInventory().firstEmpty() == -1) {
/* 328 */       player.getWorld().dropItem(player.getLocation(), this.itemStack);
/* 329 */       this.itemStack = null;
/* 330 */       getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 331 */       getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/*     */       return;
/*     */     } 
/* 334 */     player.getInventory().addItem(new ItemStack[] { this.itemStack });
/* 335 */     this.itemStack = null;
/* 336 */     getContentMenu().removeSlotMenu(getContentMenu().getPreviewSlot());
/* 337 */     getContentMenu().removeSlotMenu(getContentMenu().getResultSlot());
/*     */   }
/*     */   
/*     */   public List<String> getUnavailableColors() {
/* 341 */     return this.unavailableColors;
/*     */   }
/*     */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\inventories\menus\FreeColoredMenu.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */