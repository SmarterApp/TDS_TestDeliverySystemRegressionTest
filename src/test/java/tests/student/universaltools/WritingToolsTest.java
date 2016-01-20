package tests.student.universaltools;

import enums.WritingToolsButton;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.SeleniumBaseTest;
import util.ItemHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by emunoz on 11/3/15.
 */
public class WritingToolsTest extends SeleniumBaseTest {
    //The selector for the WYSIWYG editor's iFrame
    private static final By IFRAME_SELECTOR = By.cssSelector("iframe.cke_wysiwyg_frame");

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL);

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
                driver.waitForAndFindElement(By.id("lblVerifySessionID")).getText());
        driver.waitForAndFindElement(By.cssSelector("#btnApproveAccommodations button")).click();

        //Instructions
        driver.waitForTitle("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
        driver.waitForTitle("Student: Test", true);
    }

    @Test
    public void testWritingTextTools() throws InterruptedException {
        //Get to question 2
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(2000);
        driver.switchToIframe(IFRAME_SELECTOR);
        WebElement editable = driver.switchTo().activeElement();

        //Test normal
        final String normalText = "Test";
        editable.sendKeys(normalText);
        assertEquals(normalText, driver.findElement(By.cssSelector("body")).getText());
        editable.clear();

        //Test bold (<strong>)
        final String boldText = "BOLD ME";
        clickToolButtonAndEnterEditor(WritingToolsButton.BOLD); //Toggle on
        editable.sendKeys(boldText);
        assertEquals(boldText, driver.findElement(By.cssSelector("body strong")).getText());
        editable.clear();
        clickToolButtonAndEnterEditor(WritingToolsButton.BOLD); //Toggle bold off

        //Test Italic <em>
        final String italicText = "Ital THIS";
        clickToolButtonAndEnterEditor(WritingToolsButton.ITALIC); //Toggle on
        editable.sendKeys(italicText);
        assertEquals(italicText, driver.findElement(By.cssSelector("body em")).getText());
        editable.clear();
        clickToolButtonAndEnterEditor(WritingToolsButton.ITALIC); //Toggle italic off

        //Test Underline <u>
        final String underlineText = "underline";
        clickToolButtonAndEnterEditor(WritingToolsButton.UNDERLINE); //Toggle on
        editable.sendKeys(underlineText);
        assertEquals(underlineText, driver.findElement(By.cssSelector("body u")).getText());
        editable.clear();

        //Test clear button
        final String longNormalText = "This is a fairly long sentence that should prompt for confirmation of deletion.";
        clickToolButtonAndEnterEditor(WritingToolsButton.REMOVE_FORMAT);
        //Ensure no longer underlined
        editable.sendKeys(longNormalText);
        assertFalse(driver.isElementVisibleNow(By.cssSelector("body u")));
        assertEquals(longNormalText, driver.findElement(By.cssSelector("body")).getText());
        editable.clear();
        clickToolButtonAndEnterEditor(WritingToolsButton.REMOVE_FORMAT); //Unfocus out of text area to trigger confirmation
        driver.switchOutOfIFrame();
        //Assert that the confirmation dialog appears
        assertTrue(navigator.isDialogShown());
        navigator.clickDialogYesButton(); //Confirm deletion
    }

    @Test
    public void testWritingSpellcheckTool() throws InterruptedException {
        //Get to question 2
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(2000);
        driver.switchToIframe(IFRAME_SELECTOR);
        WebElement editable = driver.switchTo().activeElement();

        String incorrect = "wrongg";
        editable.sendKeys(incorrect);
        clickToolButtonAndEnterEditor(WritingToolsButton.SPELLCHECKER);
        assertNotNull(driver.findElement(By.cssSelector("body a.spellcheck-word")));
        driver.findElement(By.cssSelector("body a.spellcheck-word")).click();
        driver.switchOutOfIFrame();
        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yuimenu.visible")));
        WebElement suggestionEl = driver.findElement(By.cssSelector(".visible.yuimenu ul li.first-of-type a"));
        assertEquals("wrong", suggestionEl.getText());
        //Select the first suggestion from the spellcheck menu
        suggestionEl.click();
        // Switch back into editor to validate spellcheck change
        driver.switchToIframe(IFRAME_SELECTOR);
        editable = driver.switchTo().activeElement();
        assertEquals("wrong", editable.getText());
    }

    @Test
    public void testWritingFormatTools() throws InterruptedException {
        //Get to question 2
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(2000);
        driver.switchToIframe(IFRAME_SELECTOR);
        WebElement editable = driver.switchTo().activeElement();

        //Test numbered list <ol> <li>
        final String numberedListText = "numbered";
        clickToolButtonAndEnterEditor(WritingToolsButton.NUMBERED_LIST);
        editable.sendKeys(numberedListText);
        assertEquals(numberedListText, driver.findElement(By.cssSelector("body ol li")).getText());
        editable.clear();

        //Test bullet list <ol> <li>
        final String bulletListText = "bulleted";
        clickToolButtonAndEnterEditor(WritingToolsButton.BULLETED_LIST);
        editable.sendKeys(bulletListText);
        assertEquals(bulletListText, driver.findElement(By.cssSelector("body ul li")).getText());
        clickToolButtonAndEnterEditor(WritingToolsButton.BULLETED_LIST); //Toggle bullet off
        editable.clear();

        //Test indent (click twice)
        final String indentedText = "indented";
        editable.sendKeys(indentedText);
        Thread.sleep(500);
        clickToolButtonAndEnterEditor(WritingToolsButton.INDENT);
        clickToolButtonAndEnterEditor(WritingToolsButton.INDENT);
        assertEquals(indentedText, driver.findElement(By.cssSelector("body p[style='margin-left: 80px;']")).getText());

        //Test outdent
        clickToolButtonAndEnterEditor(WritingToolsButton.OUTDENT);
        assertEquals(indentedText, driver.findElement(By.cssSelector("body p[style='margin-left: 40px;']")).getText());
    }

    private void clickToolButtonAndEnterEditor(WritingToolsButton button) {
        driver.switchOutOfIFrame();
        driver.findElement(By.cssSelector(".cke a." + button.getClassName())).click();
        driver.switchToIframe(IFRAME_SELECTOR);
    }
}
