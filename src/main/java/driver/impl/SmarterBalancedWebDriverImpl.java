package driver.impl;

import driver.BrowserInteractionType;
import driver.SmarterBalancedWebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by emunoz on 10/29/15.
 */
//@Component
public class SmarterBalancedWebDriverImpl extends FirefoxDriver implements SmarterBalancedWebDriver {
    private static final Logger LOG = LogManager.getLogger(SmarterBalancedWebDriverImpl.class);

    private static final int MAX_TAB_ATTEMPTS = 100;

    public SmarterBalancedWebDriverImpl() {
        super();
    }

    public SmarterBalancedWebDriverImpl(DesiredCapabilities capabilities) {
        super(capabilities);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void pressKey(final Keys key) {
        LOG.trace("Pressing key: {}", key.name());
        Actions builder = new Actions(this);
        builder.sendKeys(key)
                .build()
                .perform();
    }

    /**
     * @inheritDoc
     */
    @Override
    public WebElement findElement(final By locator, final BrowserInteractionType interactionType) {
        WebElement targetEl;

        if (interactionType == BrowserInteractionType.KEYBOARD) {
            targetEl = findElementByKeyNav(locator);
        } else {
            targetEl = findElement(locator);
        }

        return targetEl;
    }

    /**
     * @inheritDoc
     */
    @Override
    public WebElement waitForAndFindElement(final By locator) {
       return waitForAndFindElement(locator, DEFAULT_WAIT_TIMEOUT_IN_SECS);
    }

    /**
     * @inheritDoc
     */
    @Override
    public WebElement waitForAndFindElement(final By locator, final int timeoutInSeconds) {
        final long startTime = System.currentTimeMillis();
        boolean found = false;
        WebElement element = null;

        Wait<SmarterBalancedWebDriverImpl> wait = new FluentWait<>(this)
                .withTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        try {
            //Temporarily disable implicit wait time.
            manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            while((System.currentTimeMillis() - startTime) < TimeUnit.SECONDS.toMillis(timeoutInSeconds)) {
                try {
                    LOG.trace("Starting WAIT with timeout of {} seconds.", timeoutInSeconds);
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    found = true;
                    break;
                } catch ( StaleElementReferenceException e ) {
                    LOG.trace("Stale element: \n" + e.getMessage() + "\n");
                } catch ( NoSuchElementException nse ) {
                    LOG.trace("No such element: \n" + nse.getMessage() + "\n");
                }
            }
        } finally {
            //Re-enable implicit waits
            manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS);
        }

        long totalTime = System.currentTimeMillis() - startTime;

        if (found)
        {
            LOG.info("Located element {} after waiting approximately {} ms", locator.toString(), totalTime);
        } else {
            LOG.info("Failed to locate element {} after {} ms", locator.toString(), totalTime);
        }

        return element;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void waitForTitle(final String expectedTitle, final boolean isContains)
    {
        long startTime = System.currentTimeMillis();
        Wait<SmarterBalancedWebDriverImpl> wait = new FluentWait<>(this)
                .withTimeout(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS);

        if (isContains) {
            wait.until(ExpectedConditions.titleContains(expectedTitle));
        } else {
            wait.until(ExpectedConditions.titleIs(expectedTitle));
        }
        LOG.info("The wait for the title to be {} took approximately {} ms.", expectedTitle,
                System.currentTimeMillis() - startTime);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isElementVisibleNow(final By locator) {
        boolean isVisible = false;
        //Temporarily disable implicit wait time.
        manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        LOG.info("Checking for immediate visiblity of element {}", locator);

        try {
            isVisible = !ExpectedConditions.invisibilityOfElementLocated(locator).apply(this);
        } catch ( StaleElementReferenceException e ) {
            LOG.trace("Stale element: \n" + e.getMessage() + "\n");
        } catch ( NoSuchElementException nse ) {
            LOG.trace( "No such element: \n" + nse.getMessage() + "\n");
        } finally {
            //Re-enable implicit waits
            manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS);
        }

        LOG.info("Element {} {} currently visible at {}.", locator,
                isVisible ? "is" : "is not", new Date(System.currentTimeMillis()));

        return isVisible;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void hoverOver(WebElement el) {
        String javaScript =
                "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(evObj);";

        this.executeScript(javaScript, el);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void switchToIframe(By iFrameSelector) {
        LOG.info("Switching into iframe {}", iFrameSelector);
        WebElement iFrameEl = findElement(iFrameSelector);
        switchTo().frame(iFrameEl);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void switchOutOfIFrame() {
        LOG.info("Switching back to the default content frame");
        switchTo().defaultContent();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean hasQuit () {
        return getSessionId() == null;
    }

    /**
     * An alternative to findElement() that works by attempting to navigate to the specified element
     * via tab key presses. If MAX_TAB_ATTEMPTS
     *
     * @param locator
     * @return
     */
    private WebElement findElementByKeyNav(final By locator) {
        WebElement targetEl = findElement(locator);
        WebElement currEl = findElement(By.cssSelector("body"));

        for (int tabAttempt = 0; tabAttempt <= MAX_TAB_ATTEMPTS && !targetEl.equals(currEl); ++tabAttempt) {
            pressKey(Keys.TAB);
            currEl = switchTo().activeElement();    //Gets the currently focused element
            LOG.trace("Current tabbed element markup: {}", currEl.getAttribute("innerHTML"));

            if (tabAttempt == MAX_TAB_ATTEMPTS) {
                throw new NoSuchElementException("Maximum tab attempts reached: Element was not located via tabbing.");
            }
        }

        return currEl;
    }
}
