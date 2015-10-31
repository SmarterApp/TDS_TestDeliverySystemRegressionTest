package tests.student.designatedsupports;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Masking tool
 *
 * Created by emunoz on 10/28/15.
 */
public class MaskingTest extends SeleniumBaseTest {

    private static final String MASKING_ON_VALUE = "TDS_Masking1";

    @Test
    public void testMasking() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 3
        driver.findElement(By.cssSelector("option[value=\"3\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);

        //Turn masking on and continue
        driver.findElement(By.cssSelector(
                "select[id*='masking'] option[value='" + MASKING_ON_VALUE + "']")).click();

        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();

        //Assert masking is not turned on
        assertFalse(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains("msk-enabled"));

        //Enable masking
        driver.findElement(By.cssSelector("a#btnMask")).click();

        //Assert masking is turned on
        assertTrue(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains("msk-enabled"));

        drawMask(new Point(500, 300), new Point(700, 400));
        drawMask(new Point(550, 250), new Point(650, 700));

        assertTrue(driver.isElementVisibleNow(By.cssSelector("#tds_mask_0")));
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#tds_mask_1")));

        //Close first mask
        driver.findElement(By.cssSelector("#tds_mask_0 button.tds_mask_close")).click();

        assertFalse(driver.isElementVisibleNow(By.cssSelector("#tds_mask_0")));
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#tds_mask_1")));

        // Close second mask
        driver.findElement(By.cssSelector("#tds_mask_1 button.tds_mask_close")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#tds_mask_0")));
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#tds_mask_1")));

        // Turn off masking
        driver.findElement(By.cssSelector("a#btnMask")).click();

        //Assert masking is turned on
        assertFalse(driver.findElement(By.cssSelector("body#htmlBody")).getAttribute("class").contains("msk-enabled"));

        // This shouldn't actually draw anything, since we have disabled masking.
        drawMask(new Point(500, 300), new Point(700, 400));
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#tds_mask_0")));
    }

    private void drawMask(Point start, Point end) {
        Actions builder = new Actions(driver);
        WebElement origin = driver.findElement(By.cssSelector("#htmlBody"));

        builder.moveToElement(origin, 0, 0)
                .moveByOffset(start.getX(), start.getY())
                .clickAndHold()
                .moveByOffset(end.getX() - start.getX(),
                              end.getY() - start.getY())
                .release()
                .build()
                .perform();
    }
}
