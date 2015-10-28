package util;

import com.google.common.base.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Helper class for time-sensitive selections. Use this helper class to wait on dynamic
 * content, page loads after a button click or similar action.
 *
 * Created by emunoz on 10/20/15.
 */
public class SeleniumWaiter {
    private static final Logger LOG = LogManager.getLogger(SeleniumWaiter.class);

    private static final int DEFAULT_WAIT_TIMEOUT_IN_SECS = 30;

    /**
     * The {@link WebDriver} driving the regression test.
     */
    private WebDriver driver;

    public SeleniumWaiter(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * This helper method waits for an element to appear on the DOM and returns
     * the {@link WebElement} object that was located. Timeout is set to default.
     *
     * @param locator
     *         element locator object
     *
     * @return the {@link WebElement} object that was located
     */
    public WebElement waitForAndGetElementByLocator(final By locator) {
        final long startTime = System.currentTimeMillis();
        boolean found = false;
        WebElement element = null;

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        while((System.currentTimeMillis() - startTime) < 61000) {
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
     * This helper method waits for an element to appear on the DOM and returns
     * the {@link WebElement} object that was located using the specified timeout in seconds.
     *
     * @param locator
     *         element locator object
     * @param timeoutInSeconds
     *         the amount of seconds to wait before timing out.
     *
     * @return the {@link WebElement} object that was located
     */
    public WebElement waitForAndGetElementByLocator(final By locator, final int timeoutInSeconds) {
        final long startTime = System.currentTimeMillis();
        boolean found = false;
        WebElement element = null;

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        //Temporarily disable implicit wait time.
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        try {
            while((System.currentTimeMillis() - startTime) < 61000) {
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
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS);
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
     *  This helper method waits for the title of page to match the provided string.
     *
     * @param expectedTitle
     *               The expected title of the page
     * @param isContains
     *               If true, title must only contain the expected string
     *               otherwise, an exact title match must be made.
     */
    public void waitForTitleAndAssert(final String expectedTitle, final boolean isContains)
    {
        long startTime = System.currentTimeMillis();
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS);

        if (isContains) {
            wait.until(ExpectedConditions.titleContains(expectedTitle));
            assertTrue(driver.getTitle().contains(expectedTitle));
        } else {
            wait.until(ExpectedConditions.titleIs(expectedTitle));
            assertEquals(expectedTitle, driver.getTitle());
        }
        LOG.debug("The wait for the title to be {} took approximately {} ms.", expectedTitle,
                System.currentTimeMillis() - startTime);
    }

    /**
     * Checks for the immediate visibility of an element specified by the {@link By} locator.
     *
     * @param locator
     * @return true if the element is immediately visible, otherwise false.
     */
    public boolean isElementVisibleNow(final By locator) {
        boolean isVisible = false;
        //Temporarily disable implicit wait time.
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        try {
            isVisible = !ExpectedConditions.invisibilityOfElementLocated(locator).apply(driver);
        } catch ( StaleElementReferenceException e ) {
            LOG.info("Stale element: \n" + e.getMessage() + "\n");
        } catch ( NoSuchElementException nse ) {
            LOG.info( "No such element: \n" + nse.getMessage() + "\n");
        } finally {
            //Re-enable implicit waits
            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT_IN_SECS, TimeUnit.SECONDS);
        }

        return isVisible;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
