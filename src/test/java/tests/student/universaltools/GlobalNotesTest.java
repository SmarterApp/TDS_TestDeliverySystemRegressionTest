package tests.student.universaltools;

import com.thoughtworks.selenium.Selenium;
import enums.TestButton;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/30/15.
 */
public class GlobalNotesTest extends SeleniumBaseTest {

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(By.cssSelector("option[value=\"11\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[6]")).click(); //Performance Test
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
        WebElement verifySessionIdEl = driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
        assertEquals("GUEST SESSION", verifySessionIdEl.getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Sound check dialog
        driver.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();
    }

    @Test
    public void testGlobalNotes() throws InterruptedException {
        driver.waitForTitleAndAssert("Student: Test", true);
        navigator.clickButton(TestButton.GLOBAL_NOTES); //Open global notes
        Thread.sleep(1000);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #global h2.hd")).getText());

        //Type some text in the notepad
        WebElement notepadInputEl = driver.findElement(
                By.cssSelector(".yui-dialog #global textarea.comment-input"));
        final String comment = "This is quite the comment";
        notepadInputEl.clear();
        notepadInputEl.sendKeys(comment);

        //Click "Save and Close"
        driver.findElement(By.cssSelector(".yui-dialog #global .yui-button.default button")).click();
        //Ensure the dialog is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));

        //Wait for save to finish
        Thread.sleep(1000);
        //Reopen notepad
        navigator.clickButton(TestButton.GLOBAL_NOTES); //Open global notes
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));

        //Ensure the text was saved (check value, not HTML inner text)
        assertEquals(comment, notepadInputEl.getAttribute("value"));

        //Enter some new text
        final String cancelledComment = "This comment is just not quite as good...";
        notepadInputEl.clear();
        notepadInputEl.sendKeys(cancelledComment);

        //Click cancel (first button)
        driver.findElement(By.cssSelector(".yui-dialog #global .yui-button button")).click();

        //Ensure the dialog is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));

        //Reopen notepad
        navigator.clickButton(TestButton.GLOBAL_NOTES); //Open global notes
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));
        //Ensure the original text was preserved and not overwritten
        assertEquals(comment, notepadInputEl.getAttribute("value"));
    }

    @Test
    public void testGlobalNotesMultiQuestion() throws InterruptedException {
        driver.waitForTitleAndAssert("Student: Test", true);
        navigator.clickButton(TestButton.GLOBAL_NOTES); //Open global notes
        Thread.sleep(1000);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #global h2.hd")).getText());

        //Type some text in the notepad
        WebElement notepadInputEl1 = driver.findElement(
                By.cssSelector(".yui-dialog #global textarea.comment-input"));

        final String comment = "This quite the comment";

        notepadInputEl1.clear();
        notepadInputEl1.sendKeys(comment);

        //Click "Save and Close"
        driver.findElement(By.cssSelector(".yui-dialog #global .yui-button.default button")).click();
        //Ensure the dialog is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));

        //Answer question and go to next question
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);
        navigator.clickButton(TestButton.NEXT);
        navigator.clickDialogYesButton();

        // driver.waitForAndGetElementByLocator(By.className(NOTEPAD_ITEM_MENU_CLASSNAME));
        //Open notepad for item 2
        navigator.clickButton(TestButton.GLOBAL_NOTES); //Open global notes
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #global h2.hd")).getText());

        WebElement notepadInputEl2 = driver.findElement(
                By.cssSelector(".yui-dialog #global textarea.comment-input"));
        assertEquals(comment, notepadInputEl2.getAttribute("value"));

    }
}
