/**
 * 
 */
package com.ibm.lbs.ldap;

/**
 * @author zhaodonglu
 * 
 */
public class User {

  private String username = null;
  private byte[] password = null;

  /**
   * 
   */
  public User() {
    super();
  }

  public User(String username, byte[] password) {
    super();
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public byte[] getPassword() {
    return password;
  }

  public void setPassword(byte[] password) {
    this.password = password;
  }

}
