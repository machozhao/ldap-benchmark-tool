/**
 * 
 */
package com.ibm.lbs.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

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
public class SpringLDAPComparationLoginModule implements LoginModule {

  private LdapTemplate ldapTemplate = null;

  private PerformanceMonitor performanceMonitor = null;

  /**
   * 
   */
  public SpringLDAPComparationLoginModule() {
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

  public void login(String username, byte[] password) throws Exception {
    long beginTime = System.currentTimeMillis();
    DirContext ctx = null;
    try {
      String userDn = this.searchUserDNByAccount(username);
      if (userDn != null) {
        ctx = ldapTemplate.getContextSource().getReadOnlyContext();
        checkPassword(password, ctx, userDn);
      }
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

  private void checkPassword(byte[] password, DirContext ctx, String userDn) throws NamingException {
    checkPasswordInCompareMode(ctx, userDn, password);
  }

  private void checkPasswordInCompareMode(DirContext ctx, String userDn, byte[] password) throws NamingException {
    SearchControls cons = new SearchControls();
    cons.setReturningAttributes(new String[0]); // Return no attrs
    cons.setSearchScope(SearchControls.OBJECT_SCOPE); // Search object only

    NamingEnumeration answer = ctx.search(userDn, "(userpassword={0})", new Object[] { password }, cons);
    if (answer == null || !answer.hasMoreElements()) {
      throw LdapUtils.convertLdapException(new NamingException("the password is wrong"));
    }
    answer.close();
  }
  
}
