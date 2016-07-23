/**
 * 
 */
package com.ibm.lbs.ldap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhaodonglu
 * 
 */
public class LDAPLoginModuleTester extends TestCase {

  private List<User> users = new ArrayList<User>();

  private ApplicationContext context = null;
  private LoginModule loginModule = null;
  private PerformanceMonitor performanceMonitor = null;

  private String loginModuleBeanName = null;

  private LDAPLoginModuleTester() {
    super();
  }

  public LDAPLoginModuleTester(String loginModuleBeanName) {
    super(loginModuleBeanName);
    this.loginModuleBeanName  = loginModuleBeanName;
  }

  public class LDAPThread implements Runnable {
    private String name = null;
    private LoginModule loginModule = null;
    private List<User> users = new ArrayList<User>();

    public LDAPThread(String name, LoginModule loginModule, List<User> users) {
      super();
      this.name = name;
      this.loginModule = loginModule;
      this.users = users;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void run() {
      System.out.println(String.format("Starting thread [%s]", this.getName()));
      if (this.users.isEmpty()) {
        System.err.println("No user data for testing, exit!");
        return;
      }
      while (true) {
        for (int i = 0; i < users.size(); i++) {
          User user = users.get(i);
          try {
            loginModule.login(user.getUsername(), user.getPassword());
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

  }

  public class MonitorThread implements Runnable {
    private PerformanceMonitor performanceMonitor = null;

    public MonitorThread(PerformanceMonitor performanceMonitor) {
      super();
      this.performanceMonitor = performanceMonitor;
    }

    public void run() {
      while (true) {
        try {
          System.out.println(this.performanceMonitor.toString());
          Thread.sleep(5 * 1000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

  }

  /**
   * @throws java.lang.Exception
   */
  public void setUp() throws Exception {
    context = new ClassPathXmlApplicationContext("/applicationContext-config.xml");
    loginModule = context.getBean(loginModuleBeanName, LoginModule.class);
    performanceMonitor = context.getBean("performanceMonitor", PerformanceMonitor.class);
  }

  /**
   * @throws java.lang.Exception
   */
  protected void tearDown() throws Exception {
  }

  public ApplicationContext getContext() {
    return context;
  }

  public void setContext(ApplicationContext context) {
    this.context = context;
  }

  public int getTotalThread() {
    return totalThread;
  }

  public void setTotalThread(int totalThread) {
    this.totalThread = totalThread;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  private List<Thread> ldapThreads = new ArrayList<Thread>();

  private int totalThread = 20;

  /**
   * Test method for
   * {@link com.ibm.lbs.ldap.SpringLDAPComparationLoginModule#login(java.lang.String, byte[])}.
   */
  public void testStressLogin() throws Exception {
    Thread m = new Thread(new MonitorThread(this.performanceMonitor));
    m.start();

    for (int i = 0; i < totalThread; i++) {
      List<User> users4Thread = new ArrayList<User>();
      users4Thread.addAll(this.users);
      Thread t = new Thread(new LDAPThread("LDAPThread#" + i, this.loginModule, users4Thread));
      t.start();
      this.ldapThreads.add(t);
    }

    for (Thread t : this.ldapThreads) {
      t.join();
    }
  }
  
  public void testOnceLogin() throws Exception {
    this.loginModule.login("", "".getBytes());
  }

  public static void main(String[] args) throws Exception {
    LDAPLoginModuleTester test = new LDAPLoginModuleTester();
    test.setUp();
    ApplicationContext context = test.getContext();
    SpringPropertiesHolder propertiesHolder = context.getBean("propertiesHolder", SpringPropertiesHolder.class);
    int totalThread = Integer.parseInt(propertiesHolder.getProperty("max.threads", "10"));
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

    System.out.println(String.format("Starting ldap thread, total mock user: %s", users.size()));
    test.testStressLogin();
  }

}
