package com.ibm.lbs.ldap;

import java.io.FileOutputStream;

import junit.framework.TestCase;

public class UserLDIFGenerator extends TestCase {

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testCase1() throws Exception {
    FileOutputStream outLdif = new FileOutputStream("c:/temp/iam_users.ldif");
    FileOutputStream outCsv = new FileOutputStream("c:/temp/iam_users.csv");
    for (int i = 2000; i < 10000; i++) {
      String s = "dn: cn=epowers" + i + ", ou=employees, ou=whitepages, DC=JKE,DC=COM\n" +
          "givenname: Erik\n" +
          "sn: Powers" + i + "\n" +
          "userPassword:: c21hcnR3YXk=\n" +
          "displayname: Powers" + i + ", Erik\n" +
          "mail: EPowers" + i + "@iamdemo.tivoli.com\n" +
          "objectclass: inetorgperson\n" +
          "objectclass: organizationalperson\n" +
          "objectclass: person\n" +
          "objectclass: top\n" +
          "uid: epowers" + i + "\n" +
          "cn: epowers" + i + "\n\n";
      outLdif.write(s.getBytes());
      outLdif.flush();
      
      outCsv.write(("epowers" + i + ",smartway\n").getBytes());
      outCsv.flush();
    }
    outLdif.close();
    outCsv.close();
  }

}
