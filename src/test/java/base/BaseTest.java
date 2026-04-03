package base;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.nio.file.Paths;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setup() {

        playwright = Playwright.create();

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(headless)
                        .setArgs(java.util.List.of("--start-maximized"))
        );

        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(null) // Maximized — no fixed viewport
        );

        page = context.newPage();

        // BaseUrl can be overridden via -DbaseUrl=... in Maven/TestNG
        String baseUrl = System.getProperty("baseUrl",
                "http://digy-hrms-quality-fe.s3-website-us-east-1.amazonaws.com/auth/login");
        page.navigate(baseUrl);
        page.waitForLoadState();

        System.out.println("🚀 Browser launched in MAXIMIZED mode");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        if (page != null && ITestResult.FAILURE == result.getStatus()) {

            File dir = new File("screenshots");
            if (!dir.exists()) dir.mkdirs();

            String fileName = "screenshots/"
                    + result.getName() + "_FAILED_"
                    + System.currentTimeMillis() + ".png";

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(fileName))
                    .setFullPage(true));

            System.out.println("📸 Failure screenshot saved: " + fileName);
        }

        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();

        System.out.println("🔒 Browser closed");
    }
}