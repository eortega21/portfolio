package com.zybooks.projectlist;

import junit.framework.TestCase;

public class LoginScreenTest extends TestCase {


    public void testConfirmPWmatch() {
        String test1="ed";
        String test2="eda";

        LoginScreen testing1 = new LoginScreen();

        boolean output = testing1.confirmPWmatch(test1,test2);

        assertEquals(true,output);
    }
    public void testConfirmPWmatch3() {
        String test1="ed";
        String test2="ed";

        LoginScreen testing1 = new LoginScreen();

        boolean output = testing1.confirmPWmatch(test1,test2);
        assertEquals(true,output);
    }
}