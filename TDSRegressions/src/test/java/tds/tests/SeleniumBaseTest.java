package tds.tests;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import tds.util.SeleniumTestWaiter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Base abstract class to be extended by all other regression tests. Responsibility includes driver setup, configuration,
 * and cleanup.
 *
 * Created by emunoz on 10/20/15.
 */
public abstract class SeleniumBaseTest {
    private static final Logger LOG = LogManager.getLogger(SeleniumBaseTest.class);

    //TODO: Load this up from properties file once we begin using Spring in this project
    private static final String PHANTOMJS_EXECUTABLE_PATH = "/Users/emunoz/Documents/dev/phantomjs-2.0.0-macosx/bin/phantomjs";

    protected WebDriver driver;

    protected WebDriverWait webDriverWait;

    protected final String BASE_URL = "https://practice.smarterbalanced.org/";

    protected SeleniumTestWaiter waitHelper;

    @Before
    public void setUp() throws Exception {
        // Used for PhantomJS WebDriver setup
        DesiredCapabilities dCaps = new DesiredCapabilities();
        dCaps.setJavascriptEnabled(true);
        dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                PHANTOMJS_EXECUTABLE_PATH);
        dCaps.setCapability("takesScreenshot", true);

        //driver = new PhantomJSDriver(dCaps);
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        waitHelper = new SeleniumTestWaiter();
        waitHelper.setDriver(driver);
        //webDriverWait = new WebDriverWait(driver, 30);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        LOG.info("Finished test execution. Quitting driver...");
    }

    protected void savePhantomScreenshot(String path) throws IOException {
        LOG.debug("Saving PhantomJS screenshot to {}", path);
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(path));
    }
}
