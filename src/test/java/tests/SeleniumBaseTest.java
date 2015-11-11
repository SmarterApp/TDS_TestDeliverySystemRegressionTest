package tests;

import driver.SmarterBalancedWebDriver;
import driver.impl.SmarterBalancedWebDriverImpl;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.navigation.TestNavigator;
import java.util.concurrent.TimeUnit;

/**
 * Base abstract class to be extended by all other regression tests. Responsibility includes driver setup, configuration,
 * and cleanup.
 *
 * Created by emunoz on 10/20/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-config.xml")
public abstract class SeleniumBaseTest implements ApplicationContextAware {
    private static final Logger LOG = LogManager.getLogger(SeleniumBaseTest.class);

    private static final Dimension SCREEN_SIZE_DIMENSIONS = new Dimension(1920, 1080);

    private ApplicationContext applicationContext;

    @Value( "${tds.baseurl}" )
    protected String BASE_URL;

    @Rule
    public final SmarterBalancedTestWatcher screenCapturer = new SmarterBalancedTestWatcher();

    //@Autowired
    protected SmarterBalancedWebDriver driver;

    @Autowired
    protected TestNavigator navigator;

    @Before
    public void setUp() throws Exception {
        driver = new SmarterBalancedWebDriverImpl();
        driver.manage().window().setSize(SCREEN_SIZE_DIMENSIONS);
        driver.manage().timeouts().implicitlyWait(SmarterBalancedWebDriver.DEFAULT_WAIT_TIMEOUT_IN_SECS,
                TimeUnit.SECONDS);
        ((RemoteWebDriver) driver).setLogLevel(Level.ALL);
        screenCapturer.setDriver(driver);
        navigator.setDriver(driver);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
