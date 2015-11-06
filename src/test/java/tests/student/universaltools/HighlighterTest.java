package tests.student.universaltools;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 10/30/15.
 */
public class HighlighterTest extends SeleniumBaseTest {
    private static final String HIGHLIGHT_REMOVE_CLASS = "highlightremove";
    private static final String HIGHLIGHT_TEXT_CLASS = "highlighttext";
    private static final String HIGHLIGHT_RESET_CLASS = "highlightclear";

    @Before
    public void loginAndBeginTest() {
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");
        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
        navigator.loginAsGuest();

        //Grade 11
        driver.findElement(By.cssSelector("option[value=\"11\"]")).click();
        driver.findElement(By.cssSelector("#btnVerifyApprove button")).click();
        // Test Configuration
        driver.waitForTitleAndAssert("Student: Login Shell Your Tests", false);

        // Select Test Type
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[6]")).click();
        driver.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
        driver.findElement(By.cssSelector("#btnAccSelect button")).click();
        assertEquals("GUEST SESSION",
                driver.waitForAndGetElementByLocator(By.id("lblVerifySessionID")).getText());
        driver.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();

        //Sound check dialog
        driver.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
        driver.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
        driver.findElement(By.cssSelector("#btnSoundYes button")).click();

        //Instructions
        driver.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
        driver.findElement(By.cssSelector("#btnStartTest button")).click();
    }

    @Test
    public void testHighlighterContextMenu() throws InterruptedException {
        WebElement wordEl = driver.findElement(By.cssSelector(".showing .contextArea p > span"));
        final String text = wordEl.getText();
        Actions builder = new Actions(driver);
        // Highlight word
        builder.moveToElement(wordEl, 0, 0)
                .clickAndHold()
                .moveByOffset(wordEl.getSize().getWidth(), 0)
                .release()
                .contextClick()
                .build()
                .perform();
        // Click "highlight text" menu option
        driver.findElement(By.cssSelector(".yuimenu.visible ." + HIGHLIGHT_TEXT_CLASS)).click();
        //Ensure that the selected element was highlighted
        assertEquals(text, driver.findElement(By.cssSelector(".showing .contextArea p > span .highlight")).getText());
        builder.moveToElement(wordEl, wordEl.getSize().getWidth() / 2, 15)
                .contextClick().build().perform();
        driver.findElement(By.cssSelector(".yuimenu.visible ." + HIGHLIGHT_RESET_CLASS)).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".highlight")));
    }

    @Test
    public void testHighlighterItemMenu() {
        WebElement paragraphEl = driver.findElement(By.cssSelector(".itemContainer .questionContainer p"));
        final String text = paragraphEl.getText();
        Actions builder = new Actions(driver);
        // Highlight the entire paragraph
        highlightText(paragraphEl, builder);
        // Click "highlight text" menu option
        navigator.selectOptionFromItemMenu(HIGHLIGHT_TEXT_CLASS);

        assertTrue(driver.isElementVisibleNow(By.cssSelector(".itemContainer .questionContainer .highlight")));
        List<WebElement> highlightedTextSpans = driver.findElements(By.cssSelector(".itemContainer .questionContainer .highlight"));

        //Iterate over each "highlighted" element and assert that the original text contained all these pieces of text.
        //Highlighting appears to be broken into multiple span elements within the paragraph.
        for (WebElement highlightedText : highlightedTextSpans) {
            assertTrue(text.contains(highlightedText.getText()));
        }

        // "Remove Highlight" only appears in context menu after a user has clicked on the highlighted text...
        builder.moveToElement(paragraphEl, 100, 10)
                .clickAndHold().release().build().perform();
        //Clear the highlighted text ("Remove Highlight")
        navigator.selectOptionFromItemMenu(HIGHLIGHT_REMOVE_CLASS);

        assertFalse(driver.isElementVisibleNow(By.cssSelector(".itemContainer .questionContainer .highlight")));

        highlightText(paragraphEl, builder);
        // Click "highlight text" menu option
        navigator.selectOptionFromItemMenu(HIGHLIGHT_TEXT_CLASS);

        highlightedTextSpans = driver.findElements(By.cssSelector(".itemContainer .questionContainer .highlight"));

        for (WebElement highlightedText : highlightedTextSpans) {
            assertTrue(text.contains(highlightedText.getText()));
        }

        assertTrue(driver.isElementVisibleNow(By.cssSelector(".itemContainer .questionContainer .highlight")));

        // Click "Reset Highlight" from item menu
        navigator.selectOptionFromItemMenu(HIGHLIGHT_RESET_CLASS);
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".itemContainer .questionContainer .highlight")));
    }

    private void highlightText(WebElement paragraphEl, Actions builder) {
        builder.moveToElement(paragraphEl, 0, 0)
                .clickAndHold()
                .moveByOffset(paragraphEl.getSize().getWidth(), paragraphEl.getSize().getHeight() - 10)
                .release()
                .build()
                .perform();
    }
}
