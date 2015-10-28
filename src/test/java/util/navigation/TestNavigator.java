package util.navigation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import util.SeleniumWaiter;

import java.util.List;

/**
 * This class provides utility methods for navigation within the SmarterBalanced assessment.
 *
 * Created by emunoz on 10/23/15.
 */
public class TestNavigator {
    private static final Logger LOG = LogManager.getLogger(TestNavigator.class);

    private static final String GUEST_KEY = "GUEST";

    WebDriver driver;

    SeleniumWaiter waiter;

    public TestNavigator(WebDriver driver) {
        this.driver = driver;
        this.waiter = new SeleniumWaiter(driver);
    }

    /**
     * Logs into the SmarterBalanced test using guest user and session credentials.
     */
    public void loginAsGuest() {
        login(GUEST_KEY, GUEST_KEY, GUEST_KEY, GUEST_KEY, GUEST_KEY);
    }

    /**
     * Logs into the SmarterBalanced test using the specified SSID and firstname.
     * credentials (ignoring session fields).
     *
     * @param ssid
     *          The test user's SSID.
     * @param firstname
     *          The test user's firstname.
     */
    public void login(final String ssid, final String firstname) {
        LOG.info("Attempting to log in using username {} and firstname {}.", ssid, firstname);
        clearAndType(ssid, By.cssSelector("#loginRow_ID"));
        clearAndType(firstname, By.cssSelector("#loginRow_FirstName"));
        driver.findElement(By.cssSelector("#btnLogin button[type=\"submit\"]")).click();
    }

    /**
     * Logs ino the SmarterBalanced test using the specified.
     *
     * @param ssid
     *          The test user's SSID.
     * @param firstname
     *          The test user's first name.
     * @param session1
     *          First session input string
     * @param session2
     *          Second session input string
     * @param session3
     *          Third session input string
     */
    public void login(final String ssid, final String firstname, final String session1,
                      final String session2, final String session3) {
        LOG.info("Attempting to log in using username {} and firstname {} with session {}-{}-{}.",
                ssid, firstname, session1, session2, session3);
        clearAndType(ssid, By.cssSelector("#loginRow_ID"));
        clearAndType(firstname, By.cssSelector("#loginRow_FirstName"));
        clearAndType(session1, By.cssSelector("#loginSessionID1"));
        clearAndType(session2, By.cssSelector("#loginSessionID2"));
        clearAndType(session3, By.cssSelector("#loginSessionID3"));
        driver.findElement(By.cssSelector("#btnLogin button[type=\"submit\"]")).click();
    }

    /**
     * This method checks is the "next" button is available and active.
     *
     * @return
     */
    public boolean nextButtonAvailable() {
        boolean isAvailable = false;
        List<WebElement> activeButtons = driver.findElements(By.cssSelector("li.active a"));

        for (WebElement button : activeButtons) {
            if (button.getAttribute("class").contains("next")) {
                isAvailable = true;
                break;
            }
        }
        LOG.info("Next button active and available?: {}", isAvailable);

        return isAvailable;
    }

    /**
     * This method checks is the "next" button is available and active.
     *
     * @return
     */
    public boolean endButtonAvailable() {
        boolean isAvailable = false;
        List<WebElement> activeButtons = driver.findElements(By.cssSelector("li.active a"));

        for (WebElement button : activeButtons) {
            if (button.getAttribute("class").contains("endTest")) {
                isAvailable = true;
                break;
            }
        }

        LOG.info("End button active and available?: {}", isAvailable);

        return isAvailable;
    }

    /**
     * Clicks the "Next" button and waits a specified amount of milliseconds.
     *
     * @param timeInMs
     * @throws InterruptedException
     */
    public void clickNextButtonAndWait(final long timeInMs) throws InterruptedException {
        LOG.trace ("Clicking the \"NEXT PAGE\" button.");
        driver.findElement(By.cssSelector("#btnNext")).click();
        Thread.sleep(timeInMs);
        navigateToLastPage();
    }

    /**
     * Clicks the "end test" button (assumes it has been checked for aciveness/availability).
     */
    public void clickEndTestButton() {
        LOG.trace ("Clicking the \"END TEST\" button.");
        driver.findElement(By.cssSelector("#btnEnd")).click();
    }

    /**
     * Selects the last option in the questions select dropdown list to navigate to the last available
     * question page.
     */
    private void navigateToLastPage() {
        LOG.trace("Navigating to the last available questions page.");
        List<WebElement> questionOptions = driver.findElements(By.cssSelector("#ddlNavigation option:not(.hidden)"));

        if (questionOptions.size() > 0) {
            WebElement lastEl = questionOptions.get(questionOptions.size() - 1);
            //Go to the last available page
            lastEl.click();
            LOG.info("Now showing question(s): {}", lastEl.getText());
        } else {
            LOG.warn("No question navigation list detected on the current page.");
        }
    }

    /**
     * Checks the DOM <i>immediately</i> for a standard YUI dialog.
     *
     * @return true if a dialog is currently visible, otherwise false.
     */
    public boolean isDialogShown() {
        //WebElement dialogEl = waiter.waitForAndGetElementByLocator(By.cssSelector("#yuiSimpleDialog"), 0);
        LOG.trace("Checking to see if a modal dialog is being displayed...");
        boolean isVisible =  waiter.isElementVisibleNow(By.cssSelector("#yuiSimpleDialog"));
        LOG.trace("Modal dialog visible?: {}", isVisible);

        return isVisible;
    }

    /**
     * Clicks the "OK" button of a currently visible modal dialog.
     */
    public void clickDialogOkButton() {
        LOG.trace ("Clicking the modal dialog 'OK' button");
        driver.findElement(By.cssSelector("#yuiSimpleDialog .yui-button")).click();
    }

    /**
     * Clicks the "YES" button of a currently visible modal dialog.
     */
    public void clickDialogYesButton() {
        LOG.trace ("Clicking the modal dialog 'YES' button");
        List<WebElement> dialogBtns = driver.findElements(By.cssSelector("#yuiSimpleDialog .yui-button"));

        if (dialogBtns.size() == 2) {
            //Click second ("Yes") button
            dialogBtns.get(1).click();
        } else {
            LOG.warn("Less than two buttons were detected on the currently open dialog. Skipping button click");
        }
    }

    /**
     * Clicks the "NO" button of a currently visible modal dialog.
     */
    public void clickDialogNoButton() {
        LOG.trace ("Clicking the modal dialog 'NO' button");
        List<WebElement> dialogBtns = driver.findElements(By.cssSelector("#yuiSimpleDialog .yui-button"));

        if (dialogBtns.size() == 2) {
            //Click second "Yes" button
            dialogBtns.get(0).click();
        }
    }

    private void clearAndType(final String text, final By by) {
        WebElement input = driver.findElement(by);
        if (input.isEnabled()) {
            input.clear();
            input.sendKeys(text);
        }
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void setWaiter(SeleniumWaiter waiter) {
        this.waiter = waiter;
    }
}
