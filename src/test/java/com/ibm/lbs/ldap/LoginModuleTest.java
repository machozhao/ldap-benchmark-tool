package com.ibm.lbs.ldap;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoginModuleTest extends TestCase {

  private ApplicationContext applicationContext = null;

  protected void setUp() throws Exception {
    super.setUp();
    applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:applicationContext-config*.xml"});
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testSpringAuthen() throws Exception {
    SpringLDAPAuthenLoginModule lm = this.applicationContext.getBean("loginModuleSpringLDAPAuthen", SpringLDAPAuthenLoginModule.class);
    lm.login("epowers", "smartway".getBytes());
    lm.login("jhill", "smartway".getBytes());
    lm.login("jrivers", "smartway".getBytes());
  }

}
