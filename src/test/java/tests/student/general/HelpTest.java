package tests.student.general;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/27/15.
 */
public class HelpTest extends SeleniumBaseTest {
    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();
    }

    @Test
    public void testHelp() {
        final String HELP_DIALOG_CLASS = "tool-helpguide-container";
        driver.findElement(By.cssSelector("a#btnHelp")).click();
        waitHelper.waitForAndGetElementByLocator(By.className(HELP_DIALOG_CLASS));
        assertTrue(waitHelper.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
        WebElement helpContainer = driver.findElement(By.className(HELP_DIALOG_CLASS));
        assertTrue(helpContainer.findElement(By.cssSelector("h2.hd")).getText().contains("Help Guide"));

        //Switch into "help" iframe
        WebElement helpFrame = driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS+ " iframe"));
        driver.switchTo().frame(helpFrame);

        //Ensure HELP links are available and visible
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#one']")).getText(),
                "Overview of the Student Testing Site");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#two']")).getText(),
                "Test Rules");
        assertEquals(driver.findElement(By.cssSelector("#helpContent a[href='#three']")).getText(),
                "About Print-on-Demand and Text-to-Speech");

        // Switch back to main document context
        driver.switchTo().defaultContent();

        // Close help dialog
        driver.findElement(By.cssSelector("." + HELP_DIALOG_CLASS + " a.container - close")).click();
                assertFalse(waitHelper.isElementVisibleNow(By.className(HELP_DIALOG_CLASS)));
    }
}
