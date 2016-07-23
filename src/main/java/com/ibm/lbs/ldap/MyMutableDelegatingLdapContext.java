/**
 * 
 */
package com.ibm.lbs.ldap;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.commons.pool.KeyedObjectPool;
import org.springframework.ldap.pool.DirContextType;
import org.springframework.ldap.pool.MutableDelegatingLdapContext;

/**
 * @author zhaodonglu
 * 
 */
public class MyMutableDelegatingLdapContext extends MutableDelegatingLdapContext {

  /**
   * @param keyedObjectPool
   * @param delegateLdapContext
   * @param dirContextType
   */
  public MyMutableDelegatingLdapContext(KeyedObjectPool keyedObjectPool, LdapContext delegateLdapContext, DirContextType dirContextType) {
    super(keyedObjectPool, delegateLdapContext, dirContextType);
  }

  /**
   * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
   */
  /* (non-Javadoc)
   * @see org.springframework.ldap.pool.DelegatingContext#addToEnvironment(java.lang.String, java.lang.Object)
   */
  public Object addToEnvironment(String propName, Object propVal) throws NamingException {
    Object oldValue = this.getDelegateLdapContext().getEnvironment().get(propName);
    this.getDelegateLdapContext().addToEnvironment(propName, propVal);
    return oldValue;
  }

  /**
   * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
   */
  /* (non-Javadoc)
   * @see org.springframework.ldap.pool.DelegatingContext#removeFromEnvironment(java.lang.String)
   */
  public Object removeFromEnvironment(String propName) throws NamingException {
    Object oldValue = this.getDelegateLdapContext().getEnvironment().get(propName);
    this.getDelegateLdapContext().removeFromEnvironment(propName);
    return oldValue;
  }
}
