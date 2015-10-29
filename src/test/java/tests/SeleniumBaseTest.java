package tests;

import driver.SmarterBalancedFirefoxDriver;
import driver.impl.SmarterBalancedFirefoxDriverImpl;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import util.navigation.TestNavigator;
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

    protected SmarterBalancedFirefoxDriver driver;

    protected final String BASE_URL = "https://practice.smarterbalanced.org/";

    protected TestNavigator navigator;

    @Rule
    public SmarterBalancedTestWatcher screenCapturer = new SmarterBalancedTestWatcher();

    @Before
    public void setUp() throws Exception {
        // Used for PhantomJS WebDriver setup
        DesiredCapabilities dCaps = new DesiredCapabilities();
        dCaps.setJavascriptEnabled(true);
        dCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                PHANTOMJS_EXECUTABLE_PATH);
        dCaps.setCapability("takesScreenshot", true);

        //driver = new PhantomJSDriver(dCaps);
        driver = new SmarterBalancedFirefoxDriverImpl();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //TODO: Wire this guy up with Spring
        navigator = new TestNavigator(driver);
        screenCapturer.setDriver(driver);
    }
}
