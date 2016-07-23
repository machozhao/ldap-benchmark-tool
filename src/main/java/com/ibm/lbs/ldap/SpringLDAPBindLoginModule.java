/**
 * 
 */
package com.ibm.lbs.ldap;

import javax.naming.Context;
import javax.naming.directory.DirContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;

/**
 * @author zhaodonglu
 * 
 */
public class SpringLDAPBindLoginModule implements LoginModule {

  private LdapTemplate ldapTemplate = null;

  private PerformanceMonitor performanceMonitor = null;

  public SpringLDAPBindLoginModule() {
    super();
  }

  public LdapTemplate getLdapTemplate() {
    return ldapTemplate;
  }

  public void setLdapTemplate(LdapTemplate ldapTemplate) {
    this.ldapTemplate = ldapTemplate;
  }

  public PerformanceMonitor getPerformanceMonitor() {
    return performanceMonitor;
  }

  public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
    this.performanceMonitor = performanceMonitor;
  }

  public String searchUserDNByAccount(String userName) {
    AndFilter filter = new AndFilter();

    filter.and(new EqualsFilter("uid", userName));
    try {
      return (String) ldapTemplate.searchForObject("", filter.encode(), new AbstractContextMapper() {

        @Override
        protected Object doMapFromContext(DirContextOperations ctx) {
          return ctx.getDn().toString();
        }
      });
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.lbs.ldap.LoginModule#login(java.lang.String, byte[])
   */
  public void login(String username, byte[] password) throws Exception {
    long beginTime = System.currentTimeMillis();
    DirContext ctx = null;
    try {
      String userDN = this.searchUserDNByAccount(username);
      if (userDN != null) {
        ctx = ldapTemplate.getContextSource().getReadOnlyContext();

        Object oldPrincipal = ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
        Object oldCredential = ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
        
        this.performanceMonitor.incSuccess();
        long elapseTime = System.currentTimeMillis() - beginTime;
        this.performanceMonitor.addElapseTime(elapseTime);
        
        // Restore old value value
        ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, oldPrincipal);
        ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, oldCredential);
        
        return;
        /*
        boolean success = ldapTemplate.authenticate("dc=com", String.format("(&(uid=%s)(objectclass=inetorgperson))", username), new String(password));
        if (success) {
          this.performanceMonitor.incSuccess();
          long elapseTime = System.currentTimeMillis() - beginTime;
          this.performanceMonitor.addElapseTime(elapseTime);
          return;
        }
        */
      }
      throw new RuntimeException(String.format("Could not found user: [%s]", username));
    } catch (Exception e) {
      this.performanceMonitor.incFailure();
      throw e;
    } finally {
      LdapUtils.closeContext(ctx);
    }
  }

}
