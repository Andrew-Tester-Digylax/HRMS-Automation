

package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.EmployeeOnboardingPage;

public class EmployeeOnboardingTest extends BaseTest {

    @Test
    public void verifyEmployeeOnboardingFlow() {
        page.navigate("http://digy-hrms-quality-fe.s3-website-us-east-1.amazonaws.com/auth/login");
        LoginPage              login      = new LoginPage(page);
        EmployeeOnboardingPage onboarding = new EmployeeOnboardingPage(page);

        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   HRMS EMPLOYEE ONBOARDING FLOW TEST STARTED    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        // ── TEST CASE 1: Login ────────────────────────────────────────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 1: Login");
        System.out.println("========================================");
        login.enterEmailAndClickNext("andrew.paul@digylax.com");
        login.enterPasswordAndLogin("Andrew@98");
        System.out.println("   ✅ Logged in as andrew.paul@digylax.com");
        System.out.println("   🎉 TEST CASE 1 PASSED: Login ✅");

        // ── TEST CASE 2: Navigate to Employee Module ─────────────────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 2: Navigate to Employee Module");
        System.out.println("========================================");
        onboarding.clickEmployeeModule();
        System.out.println("   🎉 TEST CASE 2 PASSED: Navigate to Employee Module ✅");

        // ── TEST CASE 3: Click Onboard Employee ──────────────────────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 3: Click Onboard Employee");
        System.out.println("========================================");
        onboarding.clickOnboardEmployee();
        System.out.println("   🎉 TEST CASE 3 PASSED: Click Onboard Employee ✅");

        // ── TEST CASE 4: Fill Basic Details (Step 1) ──────────────────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 4: Fill Basic Details");
        System.out.println("========================================");
        onboarding.enterFirstName("Test");
        onboarding.enterLastName("Employee");
        onboarding.enterOfficeEmail("test.employee@digylax.com");
        onboarding.selectGender();
        onboarding.selectDateOfJoining();
        onboarding.selectDateOfBirth();
        onboarding.selectMaritalStatus();
        System.out.println("   🎉 TEST CASE 4 PASSED: Fill Basic Details ✅");

        // ── TEST CASE 5: Fill Employee Details (Step 1 continued) ─────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 5: Fill Employee Details");
        System.out.println("========================================");
        onboarding.enterEmployeeId("EMP-TEST-001");
        onboarding.selectDesignation();
        onboarding.selectRole();
        onboarding.selectPosition();
        onboarding.selectDepartment();
        onboarding.selectContributionLevel();
        onboarding.selectEmployeeType();
        onboarding.selectBaseOfficeLocation();
        onboarding.selectCurrentOfficeLocation();
        onboarding.selectReportingManager();
        System.out.println("   🎉 TEST CASE 5 PASSED: Fill Employee Details ✅");

        // ── TEST CASE 6: Save & Next (Step 1 → Step 2) ────────────────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 6: Save & Next");
        System.out.println("========================================");
        onboarding.clickSaveAndNext();
        System.out.println("   🎉 TEST CASE 6 PASSED: Save & Next ✅");

        // ── TEST CASE 7: Fill Educational Details (Step 2) ────────────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 7: Fill Educational Details");
        System.out.println("========================================");
        onboarding.selectDegree();
        onboarding.selectCourse();
        onboarding.selectEducationStartDate();
        onboarding.selectEducationEndDate();
        onboarding.selectCGPA();
        System.out.println("   🎉 TEST CASE 7 PASSED: Fill Educational Details ✅");

        // ── TEST CASE 8: Save & Exit (Step 2) ─────────────────────────────────
        System.out.println("\n========================================");
        System.out.println("  TEST CASE 8: Save & Exit");
        System.out.println("========================================");
        onboarding.clickSaveAndExit();
        System.out.println("   🎉 TEST CASE 8 PASSED: Save & Exit ✅");

        // ── ALL DONE ──────────────────────────────────────────────────────────
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                                                      ║");
        System.out.println("║   ✅ TEST CASE 1 PASSED : Login                     ║");
        System.out.println("║   ✅ TEST CASE 2 PASSED : Navigate to Employee Module║");
        System.out.println("║   ✅ TEST CASE 3 PASSED : Click Onboard Employee     ║");
        System.out.println("║   ✅ TEST CASE 4 PASSED : Fill Basic Details         ║");
        System.out.println("║   ✅ TEST CASE 5 PASSED : Fill Employee Details      ║");
        System.out.println("║   ✅ TEST CASE 6 PASSED : Save & Next                ║");
        System.out.println("║   ✅ TEST CASE 7 PASSED : Fill Educational Details   ║");
        System.out.println("║   ✅ TEST CASE 8 PASSED : Save & Exit                ║");
        System.out.println("║                                                      ║");
        System.out.println("║       🎉 ALL 8 VALIDATIONS PASSED 🎉                ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");
    }
}
