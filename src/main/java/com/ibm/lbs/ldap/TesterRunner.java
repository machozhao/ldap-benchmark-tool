/**
 * 
 */
package com.ibm.lbs.ldap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * @author zhaodonglu
 *
 */
public class TesterRunner {

  /**
   * 
   */
  public TesterRunner() {
    // TODO Auto-generated constructor stub
  }

  /**
   * -Dcom.sun.jndi.ldap.connect.pool.debug=fine UsePool
   * @param args
   */
  public static void main(String[] args) throws Exception {
    //LDAPLoginModuleTester test = new LDAPLoginModuleTester("loginModuleLDAPBind");
    LDAPLoginModuleTester test = new LDAPLoginModuleTester("loginModuleSpringLDAPBind");
    //LDAPLoginModuleTester test = new LDAPLoginModuleTester("loginModuleSpringLDAPComp");
    //LDAPLoginModuleTester test = new LDAPLoginModuleTester("loginModuleSpringLDAPAuthen");
    // Setup tester
    test.setUp();
    // Get Testing parameter
    ApplicationContext context = test.getContext();
    SpringPropertiesHolder propertiesHolder = context.getBean("propertiesHolder", SpringPropertiesHolder.class);
    int totalThread = Integer.parseInt(propertiesHolder.getProperty("max.threads", "1"));
    test.setTotalThread(totalThread);

    // Load user from file
    List<User> users = new ArrayList<User>();
    test.setUsers(users);
    String userFile = propertiesHolder.getProperty("mock.user.file");
    BufferedReader in = new BufferedReader(new FileReader(userFile));
    String line = in.readLine();
    while (line != null) {
      String ss[] = StringUtils.split(line, ",");
      if (ss.length == 2) {
        users.add(new User(ss[0], ss[1].getBytes("iso8859-1")));
      }
      line = in.readLine();
    }
    in.close();

    // Run testing
    System.out.println(String.format("Starting ldap thread, total mock user: %s", users.size()));
    test.testStressLogin();
  }

}
