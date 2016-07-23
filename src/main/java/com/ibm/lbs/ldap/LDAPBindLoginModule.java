/**
 * 
 */
package com.ibm.lbs.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.ldap.support.LdapUtils;

/**
 * @author zhaodonglu
 * 
 */
public class LDAPBindLoginModule implements LoginModule {
  
  private PerformanceMonitor performanceMonitor = null;

  /**
   * 
   */
  public LDAPBindLoginModule() {
    super();
  }

  public PerformanceMonitor getPerformanceMonitor() {
    return performanceMonitor;
  }

  public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
    this.performanceMonitor = performanceMonitor;
  }

  public void login(String username, byte[] password) throws Exception {
    long beginTime = System.currentTimeMillis();
    DirContext ctx = null;
    try {
      // Set up the environment for creating the initial context
      Hashtable env = new Hashtable();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.PROVIDER_URL, "ldap://10.5.81.211:389/dc=com");
      env.put("com.sun.jndi.ldap.connect.pool", "true");
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, "cn=root");
      env.put(Context.SECURITY_CREDENTIALS, "Pass1234");

      // Create the initial context
      ctx = new InitialDirContext(env);
      SearchControls cons = new SearchControls();
      cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
      NamingEnumeration<SearchResult> users = ctx.search("", String.format("(&(uid=%s)(objectclass=inetorgperson))", username), cons );
      String userDn = null;
      if (users != null && users.hasMore()) {
        SearchResult result = users.next();
        userDn = result.getName();
      }
      
      ctx.removeFromEnvironment(Context.SECURITY_PRINCIPAL);
      ctx.removeFromEnvironment(Context.SECURITY_CREDENTIALS);

      ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDn);
      ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);

      this.performanceMonitor.incSuccess();
      long elapseTime = System.currentTimeMillis() - beginTime;
      this.performanceMonitor.addElapseTime(elapseTime);
    } catch (NamingException e) {
      this.performanceMonitor.incFailure();
      throw LdapUtils.convertLdapException(e);
    } catch (Exception e) {
      this.performanceMonitor.incFailure();
      throw e;
    } finally {
      LdapUtils.closeContext(ctx);
    }
  }
  
  public static void main(String[] args) throws Exception {
    LDAPBindLoginModule module = new LDAPBindLoginModule();
    module.setPerformanceMonitor(new PerformanceMonitor());
    module.login("fangzy", "000000".getBytes());
  }

}
