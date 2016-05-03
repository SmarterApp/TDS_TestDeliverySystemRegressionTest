package tests.proctor;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.ProctorBaseTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 2/11/16.
 */
public class StartSessionTest extends ProctorBaseTest {

    @Test
    public void startSession() throws Exception {
        //Disabled button markup
        driver.isElementVisibleNow(By.className("session-nope"));

        List<WebElement> testCheckBoxEls = driver.findElements(By.cssSelector("#selTestsListBox input[type='checkbox']"));
        for (WebElement checkBox : testCheckBoxEls) {
            checkBox.click();
        }

        assertTrue(driver.isElementVisibleNow(By.id("btnStartSession")));
        assertEquals("---- - ----", driver.findElement(By.id("lblSessionID")).getText());
        driver.findElement(By.id("btnStartSession")).click();
        Thread.sleep(2000);
        String sessionIdPrefix = driver.findElement(By.cssSelector(".clientsystem #spanUserName"))
                .getText().substring(0, 3);
        assertTrue(driver.findElement(By.id("lblSessionID")).getText().startsWith(sessionIdPrefix));

        //Stop session
        WebElement stopSessionBtn = driver.findElement(By.id("btnStopSession"));
        assertTrue(driver.isElementVisibleNow(By.id("btnStopSession")));
        stopSessionBtn.click();
        //Confirm close session
        driver.findElement(By.cssSelector(".dialogs .action a.confirm")).click();
        Thread.sleep(2000);
        assertEquals("---- - ----", driver.findElement(By.id("lblSessionID")).getText());
    }
}
