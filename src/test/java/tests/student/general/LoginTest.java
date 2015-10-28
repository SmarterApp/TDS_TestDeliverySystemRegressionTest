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
public class LoginTest extends SeleniumBaseTest {

    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
    }
    @Test
    public void testGuestLogin() {
        navigator.loginAsGuest();
        waitHelper.waitForTitleAndAssert("Is This You?", true);
        assertEquals("GUEST",
                driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[1]/span[2]")).getText());
        assertEquals("GUEST",
                driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[3]/span[2]")).getText());
    }

    @Test
    public void testInvalidLogin() {
        driver.findElement(By.cssSelector("#loginForm1 input#cbUser")).click();
        navigator.login("Bad", "Credentials");

        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#yuiSimpleDialog"), 2000);
        assertTrue(navigator.isDialogShown());

        WebElement dialogHeader = driver.findElement(By.cssSelector("#yuiSimpleDialog h2"));
        WebElement dialogBody = driver.findElement(By.cssSelector("#yuiSimpleDialog div.bd"));

        assertEquals("Warning", dialogHeader.getText());
        assertTrue(dialogBody.getText().contains("Your Student ID is not entered correctly."));

        //Close dialog
        navigator.clickDialogOkButton();
        assertTrue(!navigator.isDialogShown());
    }

    @Test
    public void testInvalidSessionLogin() {
        driver.findElement(By.cssSelector("#loginForm2 input#cbSession")).click();
        navigator.login("GUEST", "GUEST", "TESTS", "TESTS", "TESTS");

        waitHelper.waitForAndGetElementByLocator(By.cssSelector("#yuiSimpleDialog"), 2000);
        assertTrue(navigator.isDialogShown());

        WebElement dialogHeader = driver.findElement(By.cssSelector("#yuiSimpleDialog h2"));
        WebElement dialogBody = driver.findElement(By.cssSelector("#yuiSimpleDialog div.bd"));

        assertEquals("Warning", dialogHeader.getText());
        assertTrue(dialogBody.getText().contains("could not find session"));

        //Close dialog
        navigator.clickDialogOkButton();
        assertTrue(!navigator.isDialogShown());
    }

    @Test
    public void testValidLogin() {
        //TODO: Write this test once a TDS system is staged.
    }
}