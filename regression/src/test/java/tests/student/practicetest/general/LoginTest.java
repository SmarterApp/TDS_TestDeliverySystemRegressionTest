package tests.student.practicetest.general;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.StudentBaseTest;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the login functionality of the Test Delivery System
 *
 * Created by emunoz on 10/21/15.
 */
public class LoginTest extends StudentBaseTest {

    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
    }
    @Test
    public void testGuestLogin() {
        navigator.loginAsGuest();
        driver.waitForTitle("Is This You?", true);
        assertEquals("GUEST",
                driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[1]/span[2]")).getText());
        assertEquals("GUEST",
                driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[3]/span[2]")).getText());
    }

    @Test
    public void testInvalidNoInput() {
        navigator.login("", "", "", "", "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testInvalidOnlySessionId() {
        navigator.login("", "", sessionId.split("-")[0], sessionId.split("-")[1], "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testInvalidOnlyFirstName() {
        navigator.login("", STUDENT_FIRSTNAME, "", "", "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testInvalidNoSSID() {
        navigator.login("", STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testInvalidNoSessionId() {
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, "", "", "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testInvalidSSID() {
        navigator.login("Wrong", STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testInvalidSessionId() {
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, "TEST", "WRONG", "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testInvalidFirstName() {
        navigator.login(STUDENT_SSID, "Ernie", sessionId.split("-")[0], sessionId.split("-")[1], "");
        checkInvalidDialogPopup();
    }

    @Test
    public void testValidLogin() {
        navigator.login(STUDENT_SSID, STUDENT_FIRSTNAME, sessionId.split("-")[0], sessionId.split("-")[1], "");
        driver.waitForTitle("Is This You", true);
        assertEquals(STUDENT_SSID, driver.findElement(By.xpath("//div[@id='sectionLoginVerify']/div/div/ul/li[2]/span[2]")).getText());
        assertEquals(STUDENT_FIRSTNAME, driver.findElement(By.cssSelector("span.confirmData")).getText());
    }

    private void checkInvalidDialogPopup() {
        driver.waitForAndFindElement(By.cssSelector("#yuiSimpleDialog"), 2000);
        assertTrue(navigator.isDialogShown());
        WebElement dialogHeader = driver.findElement(By.cssSelector("#yuiSimpleDialog h2"));
        WebElement dialogBody = driver.findElement(By.cssSelector("#yuiSimpleDialog div.bd"));

        assertEquals("Warning", dialogHeader.getText());
        assertTrue(
                dialogBody.getText().contains("Your Student ID is not entered correctly.") ||
                dialogBody.getText().contains("could not find session") ||
                dialogBody.getText().contains("Please check that your information is entered correctly.")
        );

        //Close dialog
        navigator.clickDialogOkButton();
        assertTrue(!navigator.isDialogShown());
    }

}