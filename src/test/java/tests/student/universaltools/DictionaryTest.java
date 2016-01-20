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
import static org.openqa.selenium.By.*;

/**
 * Created by emunoz on 10/30/15.
 */
@Category(UniversalToolsTest.class)
public class DictionaryTest extends SeleniumBaseTest {

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
        driver.findElement(xpath("//ul[@id='testSelections']/li[6]")).click();
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
    public void testDragDialog() throws InterruptedException {
        // Answer questions
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);
        navigator.clickNextButtonAndWait(1000);
        // Confirmation comes up since items have not been reviews -> click next
        navigator.clickDialogYesButton();
        navigator.clickButton(TestButton.DICTIONARY);
        WebElement header = driver.findElement(By.cssSelector(".tool-dictionary-container h2.hd"));
        Point headerLocation = header.getLocation();
        Actions builder = new Actions(driver);
        Point moveTo = new Point(200, -100);
        Thread.sleep(500);
        builder.moveToElement(header, 10, 10)
                .clickAndHold()
                .moveByOffset(moveTo.getX(), moveTo.getY())
                .release()
                .build().perform();

        //Ensure that the dialog was moved
        assertEquals(header.getLocation(), new Point(headerLocation.getX() + moveTo.getX(), headerLocation.getY() + moveTo.getY()));

    }

    @Test
    public void testEnglishDictionary() throws InterruptedException {
        final String searchTerm = "dog";
        // Answer questions
        ItemHandler.getAndHandleAssessmentItems(driver);
        navigator.clickNextButtonAndWait(1000);
        navigator.clickNextButtonAndWait(1000);
        // Confirmation comes up since items have not been reviews -> click next
        navigator.clickDialogYesButton();
        navigator.clickButton(TestButton.DICTIONARY);

        assertTrue(driver.isElementVisibleNow(By.cssSelector(".yui-dialog.tool-dictionary-container")));
        assertEquals("Dictionary", driver.findElement(
                By.cssSelector(".yui-dialog.tool-dictionary-container h2.hd")).getText());
        driver.switchToIframe(By.cssSelector("iframe[id*='frame-tds-dict']"));
        //Search for definition
        driver.findElement(By.cssSelector("#dictionary-search-box")).sendKeys(searchTerm);
        driver.findElement(By.cssSelector("#dictionary_btn")).click();
        //Confirm that the search was successful
        assertTrue(driver.findElement(
                By.cssSelector("h2.def-header > span > em")).getText().contains(searchTerm.toUpperCase() + "[1]"));

        //Search for synonym
        driver.findElement(By.cssSelector("#thesaurus_btn")).click();
        assertTrue(driver.findElement(By.cssSelector(".thesaurus .mwEntryData")).getText().contains(searchTerm));

        driver.switchOutOfIFrame();

        //Close dialog
        driver.findElement(By.cssSelector(".tool-dictionary-container a.container-close")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".yui-dialog.tool-dictionary-container")));
    }


}
