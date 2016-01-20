package tests.student.universaltools;

import enums.TestButton;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;
import tests.categories.UniversalToolsTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Digital Notepad tool
 *
 * Created by emunoz on 10/30/15.
 */
@Category(UniversalToolsTest.class)
public class DigitalNotepadTest extends SeleniumBaseTest {
    private static final String NOTEPAD_ON_OPTION = "TDS_SCNotepad";
    private static final String NOTEPAD_ITEM_MENU_CLASSNAME = "comment";

    @Before
    public void loginAndConfigureNotepad() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(By.cssSelector("option[value=\"11\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitle("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[1]")).click();
        driver.waitForTitle("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("select[id*='-studentcomments'] option[value='" + NOTEPAD_ON_OPTION + "']")).click();
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();

    }

    @Test
    public void testDragDialog() throws InterruptedException {
        driver.waitForTitle("Student: Test", true);
        navigator.selectOptionFromItemMenu(NOTEPAD_ITEM_MENU_CLASSNAME);
        WebElement header = driver.findElement(By.cssSelector(".yui-dialog h2.hd"));
        Point headerLocation = header.getLocation();
        driver.findElement(By.cssSelector(".yui-dialog h2.hd"));

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
    public void testNotepadSaveAndCancel() {
        navigator.selectOptionFromItemMenu(NOTEPAD_ITEM_MENU_CLASSNAME);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #comment h2.hd")).getText());

        //Type some text in the notepad
        WebElement notepadInputEl = driver.findElement(
                By.cssSelector(".yui-dialog #comment textarea.comment-input"));

        final String comment = "This is quite the comment";

        notepadInputEl.clear();
        notepadInputEl.sendKeys(comment);

        //Click "Save and Close"
        driver.findElement(By.cssSelector(".yui-dialog #comment .yui-button.default button")).click();
        //Ensure the dialog is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));

        //Reopen notepad
        navigator.selectOptionFromItemMenu(NOTEPAD_ITEM_MENU_CLASSNAME);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));

        //Ensure the text was saved (check value, not HTML inner text)
        assertEquals(comment, notepadInputEl.getAttribute("value"));

        //Enter some new text
        final String cancelledComment = "This comment is just not quite as good...";
        notepadInputEl.clear();
        notepadInputEl.sendKeys(cancelledComment);

        //Click cancel (first button)
        driver.findElement(By.cssSelector(".yui-dialog #comment .yui-button button")).click();

        //Ensure the dialog is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));

        //Reopen notepad
        navigator.selectOptionFromItemMenu(NOTEPAD_ITEM_MENU_CLASSNAME);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));
        //Ensure the original text was preserved and not overwritten
        assertEquals(comment, notepadInputEl.getAttribute("value"));
    }

    @Test
    public void testNotepadMultiQuestion() throws InterruptedException {
        navigator.selectOptionFromItemMenu(NOTEPAD_ITEM_MENU_CLASSNAME);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #comment h2.hd")).getText());

        //Type some text in the notepad
        WebElement notepadInputEl1 = driver.findElement(
                By.cssSelector(".yui-dialog #comment textarea.comment-input"));

        final String comment = "This quite the comment";

        notepadInputEl1.clear();
        notepadInputEl1.sendKeys(comment);

        //Click "Save and Close"
        driver.findElement(By.cssSelector(".yui-dialog #comment .yui-button.default button")).click();
        //Ensure the dialog is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));

        //Answer question and go to next question
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);

       // driver.waitForAndFindElement(By.className(NOTEPAD_ITEM_MENU_CLASSNAME));
        //Open notepad for item 2
        navigator.selectOptionFromItemMenu(NOTEPAD_ITEM_MENU_CLASSNAME);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));
        assertEquals("Notepad", driver.findElement(By.cssSelector(".yui-dialog #comment h2.hd")).getText());

        WebElement notepadInputEl2 = driver.findElement(
                By.cssSelector(".yui-dialog #comment textarea.comment-input"));
        assertEquals("", notepadInputEl2.getAttribute("value"));
        //Click "Save and Close"
        driver.findElement(By.cssSelector(".yui-dialog #comment .yui-button.default button")).click();
        //Ensure the dialog is closed
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));

        //Navigate back
        navigator.clickButton(TestButton.BACK);
        //Open notepad for item 1
        navigator.selectOptionFromItemMenu(NOTEPAD_ITEM_MENU_CLASSNAME);
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog #comment")));

        //Ensure the text was saved (check value, not HTML inner text)
        assertEquals(comment, notepadInputEl1.getAttribute("value"));

    }
}
