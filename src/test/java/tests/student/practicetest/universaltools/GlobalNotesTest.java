package tests.student.practicetest.universaltools;

import enums.TestButton;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.StudentPracticeTestBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.*;

/**
 * Created by emunoz on 10/30/15.
 */
public class GlobalNotesTest extends StudentPracticeTestBaseTest {

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(cssSelector("option[value=\"11\"]")).click();
        driver.findElement(cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(xpath("//ul[@id='testSelections']/li[6]")).click(); //Performance Test
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(cssSelector("#btnApproveAccommodations button")).click();
        navigator.doSoundCheckAndContinue();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testGlobalNotes() throws InterruptedException {
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
    public void testDragDialog() throws InterruptedException {
        //Open global notes
        navigator.clickButton(TestButton.GLOBAL_NOTES);
        WebElement header = driver.findElement(By.cssSelector(".yui-dialog #global h2.hd"));
        Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector(".yui-dialog #global h2.hd"));
        Point moveTo = new Point(200, -100);

        Actions builder = new Actions(driver);
        builder.moveToElement(header, 10, 10)
                .clickAndHold()
                .moveByOffset(moveTo.getX(), moveTo.getY())
                .release()
                .build().perform();
        //Ensure that the dialog was moved
        assertEquals(header.getLocation(),
                new Point(headerLocation.getX() + moveTo.getX(), headerLocation.getY() + moveTo.getY()));
    }

    @Test
    public void testGlobalNotesMultiQuestion() throws InterruptedException {
        //Open global notes
        navigator.clickButton(TestButton.GLOBAL_NOTES);
        Thread.sleep(1000);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #global h2.hd")).getText());

        //Type some text in the notepad
        WebElement notepadInputEl1 = driver.findElement(
                By.cssSelector(".yui-dialog #global textarea.comment-input"));

        final String comment = "This is quite the comment";

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
        //Confirm navigation to next page
        navigator.clickDialogYesButton();

        //Open notepad for item 2
        navigator.clickButton(TestButton.GLOBAL_NOTES); //Open global notes
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #global")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #global h2.hd")).getText());

        WebElement notepadInputEl2 = driver.findElement(
                By.cssSelector(".yui-dialog #global textarea.comment-input"));
        assertEquals(comment, notepadInputEl2.getAttribute("value"));

    }
}
