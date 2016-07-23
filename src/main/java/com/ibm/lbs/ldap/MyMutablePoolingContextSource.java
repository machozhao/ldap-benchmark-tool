/**
 * 
 */
package com.ibm.lbs.ldap;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.pool.DelegatingDirContext;
import org.springframework.ldap.pool.DirContextType;
import org.springframework.ldap.pool.factory.MutablePoolingContextSource;

/**
 * @author zhaodonglu
 *
 */
public class MyMutablePoolingContextSource extends MutablePoolingContextSource {

  /**
   * 
   */
  public MyMutablePoolingContextSource() {
    super();
  }

  protected DirContext getContext(DirContextType dirContextType) {
    final DirContext dirContext;
    try {
      dirContext = (DirContext) this.keyedObjectPool.borrowObject(dirContextType);
    }
    catch (Exception e) {
      throw new DataAccessResourceFailureException("Failed to borrow DirContext from pool.", e);
    }

    if (dirContext instanceof LdapContext) {
      return new MyMutableDelegatingLdapContext(this.keyedObjectPool, (LdapContext) dirContext, dirContextType);
    }

    return new DelegatingDirContext(this.keyedObjectPool, dirContext, dirContextType);
  }

  public DirContext getContext(String principal, String credentials) throws NamingException {
    final DirContext dirContext;
    DirContextType dirContextType = DirContextType.READ_ONLY;
    try {
      dirContext = (DirContext) this.keyedObjectPool.borrowObject(dirContextType);
      dirContext.addToEnvironment(Context.SECURITY_PRINCIPAL, principal);
      dirContext.addToEnvironment(Context.SECURITY_CREDENTIALS, credentials);
    } catch (Exception e) {
      throw new DataAccessResourceFailureException("Failed to borrow DirContext from pool.", e);
    }

    if (dirContext instanceof LdapContext) {
      return new MyMutableDelegatingLdapContext(this.keyedObjectPool, (LdapContext) dirContext, dirContextType);
    }

    return new DelegatingDirContext(this.keyedObjectPool, dirContext, dirContextType);
  }
}
