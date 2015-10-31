package tests.student.universaltools;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import tests.SeleniumBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by emunoz on 10/30/15.
 */
public class HighlighterTest extends SeleniumBaseTest {
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
    public void testHighlighter() throws InterruptedException {
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
        driver.findElement(By.cssSelector(".yuimenu.visible .highlighttext")).click();
        //Ensure that the selected element was highlighted
        assertEquals(text, driver.findElement(By.cssSelector(".showing .contextArea p > span .highlight")).getText());
        builder.moveToElement(wordEl, wordEl.getSize().getWidth() / 2, 10)
                .contextClick().build().perform();
        driver.findElement(By.cssSelector(".yuimenu.visible .highlightclear")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector(".highlight")));
    }
}
