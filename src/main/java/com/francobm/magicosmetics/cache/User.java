/*    */ package com.francobm.magicosmetics.cache;
/*    */ 
/*    */ public class User {
/*    */   private final String id;
/*    */   private final String name;
/*    */   private final String version;
/*    */   private final String resource;
/*    */   private final String token;
/*    */   private final String nonce;
/*    */   private final String agent;
/*    */   private final String time;
/*    */   
/*    */   public User() {
/* 14 */     this.id = "ROwROw";
/* 15 */     this.name = "";
/* 16 */     this.version = "";
/* 17 */     this.resource = "";
/* 18 */     this.token = "";
/* 19 */     this.nonce = "";
/* 20 */     this.agent = "";
/* 21 */     this.time = "";
/*    */   }
/*    */   
/*    */   public User(String id, String name, String version, String resource, String token, String nonce, String agent, String time) {
/* 25 */     this.id = id;
/* 26 */     this.name = name;
/* 27 */     this.version = version;
/* 28 */     this.resource = resource;
/* 29 */     this.token = token;
/* 30 */     this.nonce = nonce;
/* 31 */     this.agent = agent;
/* 32 */     this.time = time;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 36 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 40 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getVersion() {
/* 44 */     return this.version;
/*    */   }
/*    */   
/*    */   public String getResource() {
/* 48 */     return this.resource;
/*    */   }
/*    */   
/*    */   public String getToken() {
/* 52 */     return this.token;
/*    */   }
/*    */   
/*    */   public String getNonce() {
/* 56 */     return this.nonce;
/*    */   }
/*    */   
/*    */   public String getAgent() {
/* 60 */     return this.agent;
/*    */   }
/*    */   
/*    */   public String getTime() {
/* 64 */     return this.time;
/*    */   }
/*    */ }


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\User.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */