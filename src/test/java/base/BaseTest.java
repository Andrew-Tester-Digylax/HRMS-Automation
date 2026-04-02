package base;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setup() {

        playwright = Playwright.create();

        // 🔥 Detect CI environment (GitHub Actions / Docker)
        boolean isCI = System.getenv("CI") != null;

        System.out.println("Running in CI: " + isCI);

        // 🔥 Launch browser properly for both local + CI
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(isCI) // ✅ Headless in CI, headed locally
                        .setArgs(Arrays.asList(
                                "--no-sandbox",
                                "--disable-dev-shm-usage"
                        ))
        );

        // 🔥 Context setup
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(null) // maximize-like behavior
        );

        page = context.newPage();

        // 🔥 Base URL (can override using -DbaseUrl)
        String baseUrl = System.getProperty("baseUrl",
                "http://digy-hrms-quality-fe.s3-website-us-east-1.amazonaws.com/auth/login");

        page.navigate(baseUrl);
        page.waitForLoadState();

        System.out.println("🚀 Browser launched successfully");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        // 📸 Take screenshot on failure
        if (page != null && result.getStatus() == ITestResult.FAILURE) {

            File dir = new File("screenshots");
            if (!dir.exists()) dir.mkdirs();

            String fileName = "screenshots/"
                    + result.getName() + "_FAILED_"
                    + System.currentTimeMillis() + ".png";

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(fileName))
                    .setFullPage(true));

            System.out.println("📸 Screenshot saved: " + fileName);
        }

        // 🔒 Cleanup
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();

        System.out.println("🔒 Browser closed");
    }
}