package driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by emunoz on 10/29/15.
 */
public interface SmarterBalancedWebDriver extends WebDriver{
    int DEFAULT_WAIT_TIMEOUT_IN_SECS = 30;
    /**
     * This helper method waits for an element to appear on the DOM and returns
     * the {@link WebElement} object that was located. Timeout is set to default.
     *
     * @param locator
     *         element locator object
     *
     * @return the {@link WebElement} object that was located
     */
    WebElement waitForAndGetElementByLocator(final By locator);

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
    WebElement waitForAndGetElementByLocator(final By locator, final int timeoutInSeconds);

    /**
     *  This helper method waits for the title of page to match the provided string.
     *
     * @param expectedTitle
     *               The expected title of the page
     * @param isContains
     *               If true, title must only contain the expected string
     *               otherwise, an exact title match must be made.
     */
    void waitForTitleAndAssert(final String expectedTitle, final boolean isContains);

    /**
     * Checks for the immediate visibility of an element specified by the {@link By} locator.
     *
     * @param locator the {@link By} locator that is used to locate the {@link WebElement}
     * @return true if the element is immediately visible, otherwise false.
     */
    boolean isElementVisibleNow(final By locator);

    /**
     * Performs a "hover" action on the sepecified {@link WebElement}
     *
     * @param el the {@link WebElement} to hover over.
     */
    void hoverOver(WebElement el);

    /**
     * Convenience method that switches the {@link WebDriver}s context into the iframe defined
     * by the {@link By} iframe selector
     *
     * @param iFrameSelector
     *          The {@link By} selector object used to locate the iFrame on the DOM.
     */
    void switchToIframe(By iFrameSelector);

    /**
     * Convenience method that switches the context back to the default content/DOM.
     */
    void switchOutOfIFrame();
}
