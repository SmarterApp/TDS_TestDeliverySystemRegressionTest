package tests.student.accomodations;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by emunoz on 10/29/15.
 */
public class ASLTest extends SeleniumBaseTest {
    private static final String ASL_SELECT_CSS_SELECTOR = "select[id*='americansignlanguage']";
    private static final String ASL_ON_OPTION = "TDS_ASL1";

    @Before
    public void loginAndEnableAsl() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 3
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector(
                ASL_SELECT_CSS_SELECTOR + " option[value='" + ASL_ON_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID")).getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testASL() {
        navigator.selectOptionFromItemMenu("ASL");
        // Assert that the video is open and visible
        driver.waitForAndGetElementByLocator(By.cssSelector(".tool-video-container div[id*='_jwplayer_display']"), 1);

        // Close ASL video by clicking close button on top right corner
        driver.findElement(By.cssSelector(".tool-video-container a.container-close")).click();

        //Assert the video is now closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".tool-video-container div[id*='_jwplayer_display']")));
    }
}
