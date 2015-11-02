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
public class DictionaryTest extends SeleniumBaseTest {

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
        driver.findElement(By.xpath("//ul[@id='testSelections']/li[6]")).click();
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
