package util.navigation;

import driver.SmarterBalancedWebDriver;
import enums.TestButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * This class provides utility methods for navigation within the SmarterBalanced assessment.
 *
 * Created by emunoz on 10/23/15.
 */
public class TestNavigator {
    private static final Logger LOG = LogManager.getLogger(TestNavigator.class);

    private static final String GUEST_KEY = "GUEST";

    SmarterBalancedWebDriver driver;

    public TestNavigator(SmarterBalancedWebDriver driver) {
        this.driver = driver;
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
     * This method checks to see if a button is available on the current screen based on the
     * {@link TestButton}'s id.
     *
     * @param buttonType
     *          The type of {@link TestButton} to verify.
     * @return
     *          true if the button is currently available and clickable, otherwise false.
     */
    public boolean isButtonAvailable(TestButton buttonType) {
        boolean isAvailable = false;
        List<WebElement> activeButtons = driver.findElements(By.cssSelector("li.active a"));

        for (WebElement button : activeButtons) {
            if (buttonType.getId().equals(button.getAttribute("id"))) {
                isAvailable = true;
                break;
            }
        }
        LOG.info("Next button active and available?: {}", isAvailable);

        return isAvailable;
    }

    public void clickButton(TestButton button) {
        LOG.trace("Clicking the {} button.", button.name());
        driver.findElement(By.cssSelector("#" + button.getId())).click();
    }

    /**
     * Clicks the "Next" button and waits a specified amount of milliseconds before navigating to the
     * last available page.
     *
     * @param timeInMs
     * @throws InterruptedException
     */
    public void clickNextButtonAndWait(final long timeInMs) throws InterruptedException {
        clickButton(TestButton.NEXT);
        Thread.sleep(timeInMs);
        navigateToLastPage();
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
        LOG.trace("Checking to see if a modal dialog is being displayed...");
        boolean isVisible =  driver.isElementVisibleNow(By.cssSelector("#yuiSimpleDialog"));
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

    /**
     * Clicks the "hamburger" button and opens the assessment item menu, and then selects the specified option.
     */
    public void selectOptionFromItemMenu(String itemClassName) {
        driver.findElement(By.cssSelector(".a.itemMenu")).click();
        driver.findElement(By.cssSelector(".yuimenu.visible ." + itemClassName)).click();
    }

    private void clearAndType(final String text, final By by) {
        WebElement input = driver.findElement(by);
        if (input.isEnabled()) {
            input.clear();
            input.sendKeys(text);
        }
    }

    public void setDriver(SmarterBalancedWebDriver driver) {
        this.driver = driver;
    }
}
