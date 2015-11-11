package tests.student.general;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import tests.SeleniumBaseTest;

import static org.junit.Assert.*;

/**
 * Created by emunoz on 11/4/15.
 */
public class TutorialTest extends SeleniumBaseTest {

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 12
        driver.findElement(By.cssSelector("option[value=\"12\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID")).getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testTutorial() {
        //Assert the tutorial window is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#dialogs #dialogContent")));

        //Open tutorial
        navigator.selectOptionFromItemMenu("helpItem");

        //Assert the tutorial window is now opened
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#dialogs #dialogContent")));
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#dialogContent.enable")));

        //Switch into the dialog's iframe
        driver.switchToIframe(By.cssSelector("#dialogContent.enable iframe"));
        assertTrue(driver.isElementVisibleNow(By.className("showingDialog")));

        //Switch into the HTML5 tutorial video's iframe
        driver.switchToIframe(By.cssSelector("iframe.frame-anim-html5"));

        //Verify canvas is visible and <audio> element exists
        assertTrue(driver.isElementVisibleNow(By.cssSelector("canvas#canvas")));
        assertNotNull(driver.findElement(By.cssSelector("audio#mySound")));
        driver.switchOutOfIFrame();

        //Close the tutorial window
        driver.findElement(By.cssSelector("#dialogContent #dialogContentClose a")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#dialogs #dialogContent")));
    }
}
