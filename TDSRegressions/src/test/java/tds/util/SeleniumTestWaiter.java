package tds.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Helper class for time-sensitive selections. Use this helper class to wait on dynamic
 * content, page loads after a button click or similar action.
 *
 * Created by emunoz on 10/20/15.
 */
public class SeleniumTestWaiter {
    private static final Logger LOG = LogManager.getLogger(SeleniumTestWaiter.class);

    /**
     * The {@link WebDriver} driving the regression test.
     */
    private WebDriver driver;

    /**
     * This helper method waits for an element to appear on the DOM and returns
     * the {@link WebElement} object that was located
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
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        while((System.currentTimeMillis() - startTime) < 61000) {
            try {
                element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                found = true;
                break;
            } catch ( StaleElementReferenceException e ) {
                LOG.info("Stale element: \n" + e.getMessage() + "\n");
            } catch ( NoSuchElementException nse ) {
                LOG.info( "No such element: \n" + nse.getMessage() + "\n");
            }
        }

        long totalTime = System.currentTimeMillis() - startTime;

        if (found)
        {
            LOG.debug("Located element after waiting approximately {} ms", totalTime);
        } else {
            LOG.debug("Failed to locate element after {} ms", totalTime);
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
    public void waitForTitleAndAssert(String expectedTitle, boolean isContains)
    {
        long startTime = System.currentTimeMillis();
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS);

        if (isContains) {
            wait.until(ExpectedConditions.titleContains(expectedTitle));
        } else {
            wait.until(ExpectedConditions.titleIs(expectedTitle));
        }
        assertEquals(expectedTitle, driver.getTitle());
        LOG.debug("The wait for the title to be {} took approximately {} ms.", expectedTitle,
                System.currentTimeMillis() - startTime);
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
