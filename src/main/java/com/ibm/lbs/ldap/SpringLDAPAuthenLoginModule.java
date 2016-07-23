/**
 * 
 */
package com.ibm.lbs.ldap;

import java.text.MessageFormat;

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
public class SpringLDAPAuthenLoginModule implements LoginModule {

  private LdapTemplate ldapTemplate = null;

  private PerformanceMonitor performanceMonitor = null;

  private String userFilter = "(&(uid={0})(objectclass=organizationalPerson))";
  //private String userFilter = "(&(sAMAccountName={0})(objectclass=organizationalPerson))";
  /**
   * 
   */
  public SpringLDAPAuthenLoginModule() {
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

  public String getUserFilter() {
    return userFilter;
  }

  public void setUserFilter(String passwordFilter) {
    this.userFilter = passwordFilter;
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
        boolean ok = ldapTemplate.authenticate(userDn, MessageFormat.format(this.userFilter, username), new String(password));
        if (!ok) {
          throw new Exception(String.format("Wrong password, {%s} for filter: [%s]", userDn, this.userFilter));
        }
      } else {
        throw new Exception("Wrong uid");
      }
      this.performanceMonitor.incSuccess();
      long elapseTime = System.currentTimeMillis() - beginTime;
      this.performanceMonitor.addElapseTime(elapseTime);
    } catch (Exception e) {
      this.performanceMonitor.incFailure();
      throw e;
    } finally {
      LdapUtils.closeContext(ctx);
    }
  }

}
