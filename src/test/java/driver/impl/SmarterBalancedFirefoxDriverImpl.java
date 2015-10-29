package driver.impl;

import driver.SmarterBalancedFirefoxDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/29/15.
 */
public class SmarterBalancedFirefoxDriverImpl extends FirefoxDriver implements SmarterBalancedFirefoxDriver {
    private static final Logger LOG = LogManager.getLogger(SmarterBalancedFirefoxDriverImpl.class);

    private static final int DEFAULT_WAIT_TIMEOUT_IN_SECS = 30;

    /**
     * @inheritDoc
     */
    public WebElement waitForAndGetElementByLocator(final By locator) {
        final long startTime = System.currentTimeMillis();
        boolean found = false;
        WebElement element = null;

        Wait<SmarterBalancedFirefoxDriverImpl> wait = new FluentWait<>(this)
                .withTimeout(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        while((System.currentTimeMillis() - startTime) < TimeUnit.SECONDS.toMillis(DEFAULT_WAIT_TIMEOUT_IN_SECS)) {
            LOG.trace("Starting WAIT with timeout of {} seconds.", DEFAULT_WAIT_TIMEOUT_IN_SECS);
            try {
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                found = true;
                break;
            } catch ( StaleElementReferenceException e ) {
                LOG.info("Stale element: \n" + e.getMessage() + "\n");
            } catch ( NoSuchElementException nse ) {
                LOG.info("No such element: \n" + nse.getMessage() + "\n");
            }
        }

        long totalTime = System.currentTimeMillis() - startTime;

        if (found)
        {
            LOG.debug("Located element {} after waiting approximately {} ms", locator.toString(), totalTime);
        } else {
            LOG.debug("Failed to locate element {} after {} ms", locator.toString(), totalTime);
        }

        return element;
    }

    /**
     * @inheritDoc
     */
    public WebElement waitForAndGetElementByLocator(final By locator, final int timeoutInSeconds) {
        final long startTime = System.currentTimeMillis();
        boolean found = false;
        WebElement element = null;

        Wait<SmarterBalancedFirefoxDriverImpl> wait = new FluentWait<>(this)
                .withTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        //Temporarily disable implicit wait time.
        this.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        try {
            while((System.currentTimeMillis() - startTime) < TimeUnit.SECONDS.toMillis(timeoutInSeconds)) {
                try {
                    LOG.trace("Starting WAIT with timeout of {} seconds.", timeoutInSeconds);
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    found = true;
                    break;
                } catch ( StaleElementReferenceException e ) {
                    LOG.info("Stale element: \n" + e.getMessage() + "\n");
                } catch ( NoSuchElementException nse ) {
                    LOG.info( "No such element: \n" + nse.getMessage() + "\n");
                }
            }
        } finally {
            //Re-enable implicit waits
            this.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS);
        }


        long totalTime = System.currentTimeMillis() - startTime;

        if (found)
        {
            LOG.debug("Located element {} after waiting approximately {} ms", locator.toString(), totalTime);
        } else {
            LOG.debug("Failed to locate element {} after {} ms", locator.toString(), totalTime);
        }

        return element;
    }

    /**
     * @inheritDoc
     */
    public void waitForTitleAndAssert(final String expectedTitle, final boolean isContains)
    {
        long startTime = System.currentTimeMillis();
        Wait<SmarterBalancedFirefoxDriverImpl> wait = new FluentWait<>(this)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS);

        if (isContains) {
            wait.until(ExpectedConditions.titleContains(expectedTitle));
            assertTrue(this.getTitle().contains(expectedTitle));
        } else {
            wait.until(ExpectedConditions.titleIs(expectedTitle));
            assertEquals(expectedTitle, this.getTitle());
        }
        LOG.debug("The wait for the title to be {} took approximately {} ms.", expectedTitle,
                System.currentTimeMillis() - startTime);
    }

    /**
     * @inheritDoc
     */
    public boolean isElementVisibleNow(final By locator) {
        boolean isVisible = false;
        //Temporarily disable implicit wait time.
        this.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        try {
            isVisible = !ExpectedConditions.invisibilityOfElementLocated(locator).apply(this);
        } catch ( StaleElementReferenceException e ) {
            LOG.info("Stale element: \n" + e.getMessage() + "\n");
        } catch ( NoSuchElementException nse ) {
            LOG.info( "No such element: \n" + nse.getMessage() + "\n");
        } finally {
            //Re-enable implicit waits
            this.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS);
        }

        return isVisible;
    }

}
