package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTests extends BaseTest {

    @Test
    public void loginFlow_validUser() {

        page.navigate("http://digy-hrms-quality-fe.s3-website-us-east-1.amazonaws.com/auth/login");

        LoginPage login = new LoginPage(page);

        // Step 1
        login.enterEmailAndClickNext("andrew.paul@digylax.com");

        // Step 2
        login.enterPasswordAndLogin("Andrew@98");

        // FINAL VALIDATION
        if (!page.url().contains("dashboard")) {
            throw new RuntimeException("Login failed - dashboard not loaded");
        }

        System.out.println(" Login Successful");
    }
}