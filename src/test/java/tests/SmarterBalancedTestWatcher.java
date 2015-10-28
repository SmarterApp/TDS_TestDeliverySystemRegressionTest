package tests;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * This class is responsible for handling failed test cases and error screen capturing.
 *
 * Created by emunoz on 10/21/15.
 */
public class SmarterBalancedTestWatcher extends TestWatcher implements TestRule {
    private static final Logger LOG = LogManager.getLogger(SmarterBalancedTestWatcher.class);

    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/failure-snapshots/";

    private static final String SCREENSHOT_FILE_TYPE = ".png";

    private WebDriver driver;

    @Override
    protected void starting(Description description) {
        LOG.info("Starting SmarterBalanced {} run at {}", description.getMethodName(), new Date(System.currentTimeMillis()));
    }

    @Override
    protected void failed(final Throwable e, final Description description) {
        try {
            String picPath = getPicturePath(description);
            saveScreenshot(picPath);
            LOG.error("An error occurred at {}. Saving screenshot of PhantomBrowser at {}.",
                    new Date(System.currentTimeMillis()), picPath);
        }
        catch (IOException ioe) {
            LOG.error("An error occurred at {}. Unable to save the screenshot due to an IOException");
        }
    }

    @Override
    public void finished (final Description description) {
        LOG.info("Test method {} finished executing. Quitting driver...", description.getMethodName());
        driver.quit();
    }

    private String getPicturePath(final Description description) {
        String path = SCREENSHOT_DIR + description.getMethodName() + "_"
                + new Date(System.currentTimeMillis()) + SCREENSHOT_FILE_TYPE;

        return path;
    }

    private void saveScreenshot(final String path) throws IOException {
        LOG.debug("Saving screenshot to {}", path);
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(path));
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
