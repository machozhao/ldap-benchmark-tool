package com.ibm.lbs.ldap;

public interface LoginModule {

  public abstract void login(String username, byte[] password) throws Exception;

}