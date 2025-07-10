package com.francobm.magicosmetics.cache;

public class User {
  private final String id;
  
  private final String name;
  
  private final String version;
  
  private final String resource;
  
  private final String token;
  
  private final String nonce;
  
  private final String agent;
  
  private final String time;
  
  public User() {
    this.id = "ROwROw";
    this.name = "";
    this.version = "";
    this.resource = "";
    this.token = "";
    this.nonce = "";
    this.agent = "";
    this.time = "";
  }
  
  public User(String id, String name, String version, String resource, String token, String nonce, String agent, String time) {
    this.id = id;
    this.name = name;
    this.version = version;
    this.resource = resource;
    this.token = token;
    this.nonce = nonce;
    this.agent = agent;
    this.time = time;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getVersion() {
    return this.version;
  }
  
  public String getResource() {
    return this.resource;
  }
  
  public String getToken() {
    return this.token;
  }
  
  public String getNonce() {
    return this.nonce;
  }
  
  public String getAgent() {
    return this.agent;
  }
  
  public String getTime() {
    return this.time;
  }
}


/* Location:              D:\下载\MagicCosmetics-3.1.0[tinksp.com].jar!\com\francobm\magicosmetics\cache\User.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */