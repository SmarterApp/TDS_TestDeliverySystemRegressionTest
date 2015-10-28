package tests.student.general;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/21/15.
 */
public class UserTimeoutTest extends SeleniumBaseTest {
    @Test
    public void testTimeoutWarning() throws Exception {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Select grade level and continue
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();     //Select 12th Grade
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();

        // Test Configuration
        waitHelper.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select ELA HS Test
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        waitHelper.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        // Continue with default settings
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = waitHelper.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        waitHelper.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", true);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Sleep 10 minutes.
        Thread.sleep(601000);

        assertTrue(navigator.isDialogShown());

        //Wait another 30 seconds for the dialog to disappear.
        Thread.sleep(30000);

        //Back to login screen
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
    }
}
