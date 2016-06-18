package tests;

import com.googlecode.zohhak.api.runners.ZohhakRunner;
import driver.SmarterBalancedWebDriver;
import driver.impl.SmarterBalancedWebDriverImpl;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Base abstract class to be extended by all other regression tests. Responsibility includes driver setup, configuration,
 * and cleanup.
 *
 * Created by emunoz on 10/20/15.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(ZohhakRunner.class)
@ContextConfiguration("/spring-config.xml")
public abstract class SeleniumBaseTest implements ApplicationContextAware {
    private static final Logger LOG = LogManager.getLogger(SeleniumBaseTest.class);

    private static final Dimension SCREEN_SIZE_DIMENSIONS = new Dimension(1920, 1080);

    private ApplicationContext applicationContext;

    @Rule
    public final SmarterBalancedTestWatcher screenCapturer = new SmarterBalancedTestWatcher();

    //@Autowired
    protected SmarterBalancedWebDriver driver;

    protected JavascriptExecutor jsExecutor;

    /**
     * The {@link BrowserMobProxy} proxy allows us to manage and store all HTTP requests and responses using the
     * proxy's HAR log.
     */
    protected BrowserMobProxy proxy;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
        initializeDriver();
    }

    protected void initializeDriver() {
        proxy = new BrowserMobProxyServer();
        proxy.start(0);    // get the Selenium proxy object
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        driver = new SmarterBalancedWebDriverImpl(capabilities);
        driver.manage().window().setSize(SCREEN_SIZE_DIMENSIONS);
        driver.manage().timeouts().implicitlyWait(SmarterBalancedWebDriver.DEFAULT_WAIT_TIMEOUT_IN_SECS,
                TimeUnit.SECONDS);
        ((RemoteWebDriver) driver).setLogLevel(Level.ALL);

        jsExecutor = (JavascriptExecutor) driver;
        screenCapturer.setDriver(driver);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
