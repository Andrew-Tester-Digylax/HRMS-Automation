package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.OrganizationPage;
import pages.SignupPage;
import pages.CompanyDetailsPage;

public class SignupTests extends BaseTest {

    @Test
    public void signupFlow_withDomainValidation() {

        page.navigate("http://digy-hrms-quality-fe.s3-website-us-east-1.amazonaws.com/auth/signup");

        // Organization
        OrganizationPage org = new OrganizationPage(page);
        org.selectITOrganization();

        page.waitForSelector("//input[@placeholder='First Name']");

        // Signup
        SignupPage signup = new SignupPage(page);

        String email = generateEmail();

        signup.fillSignupForm("Andrew", "Paul", email, "Andrew@98");
        signup.clickSignup();

        // Company Page
        CompanyDetailsPage company = new CompanyDetailsPage(page);
        company.waitForPage();

        // Extract domain
        String domain = email.split("@")[1].split("\\.")[0];

        // Fill company details FIRST
        company.fillCompanyDetails("Oreo Pvt Ltd", "9656543210", domain);

        // Validate AFTER fill
        String accessUrl = company.getAccessUrl();

        System.out.println("Expected: " + domain);
        System.out.println("Actual: " + accessUrl);

        Assert.assertTrue(
                accessUrl != null && accessUrl.contains(domain),
                "Access URL not matching domain"
        );

        // Click next
        company.clickGetStarted();
    }

    private String generateEmail() {
        return "andrew" + System.currentTimeMillis() + "@green.com";
    }
}