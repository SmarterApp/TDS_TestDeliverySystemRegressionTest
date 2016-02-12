package tests.proctor;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.ProctorBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 1/26/16.
 */
public class ProctorHelpTest extends ProctorBaseTest {
    private static final String FIELD_TEST_LINK_TEXT = "Access the TA User Guide for the Field Tests";

    private static final String USER_GUIDE_LINK_TEXT = "Access the Practice and Training Test User Guide";

    @Test
    public void testHelpLinks() {
        driver.findElement(By.id("btnHelp")).click();
        // Enter the help frame
        driver.switchToIframe(By.id("iframe_help"));
        //assertEquals("TA Help", driver.getTitle());
        assertEquals(driver.findElement(By.cssSelector("form .topHalf h1")).getText(), "Help");
        WebElement fieldTestLinkEl = driver.findElement(By.linkText(FIELD_TEST_LINK_TEXT));
        assertTrue(fieldTestLinkEl.getAttribute("href").contains("field-test/resources/"));
        assertEquals(FIELD_TEST_LINK_TEXT, driver.findElement(By.linkText(FIELD_TEST_LINK_TEXT)).getText());
        assertEquals(USER_GUIDE_LINK_TEXT, driver.findElement(By.linkText(USER_GUIDE_LINK_TEXT)).getText());
        assertEquals("Sample BRF file", driver.findElement(By.linkText("Sample BRF file")).getText());
        assertEquals("Sample PRN file", driver.findElement(By.linkText("Sample PRN file")).getText());
        driver.findElement(By.id("btnHelpClose")).click();
        driver.switchOutOfIFrame();
    }
}
