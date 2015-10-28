package tds.tests.student.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import tds.tests.SeleniumBaseTest;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

/**
 * Created by emunoz on 10/20/15.
 */
public class FullEndTest extends SeleniumBaseTest {
    private static final Logger LOG = LogManager.getLogger(FullEndTest.class);

    @Test
    public void testGrade12ELAHSTest() throws Exception {
        LOG.debug("Starting SmarterBalanced practice test run at {}", new Timestamp(System.currentTimeMillis()));
        driver.get(BASE_URL + "/student/Pages/LoginShell.xhtml");

        try {
            // Login Phase (GUEST)
            assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
            driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
            driver.findElement(By.cssSelector("option[value=\"12\"]")).click();     //Select 12th Grade
            driver.findElement(By.cssSelector("#btnVerifyApprove > span > button[type=\"button\"]")).click();
            // Test Configuration
            waitHelper.waitForTitleAndAssert("Student: Login Shell Your Tests", false);
            // Select ELA HS Test
            driver.findElement(By.xpath("//ul[@id='testSelections']/li[2]")).click();
            waitHelper.waitForTitleAndAssert("Student: Login Shell Choose Settings:", false);
            // Continue with default settings
            driver.findElement(By.cssSelector("#btnAccSelect > span > button[type=\"button\"]")).click();
            WebElement verifySessionIdEl = waitHelper.waitForAndGetElementByLocator(By.id("lblVerifySessionID"));
            assertEquals("GUEST SESSION", verifySessionIdEl.getText());
            waitHelper.waitForAndGetElementByLocator(By.cssSelector("#btnApproveAccommodations > span > button[type=\"button\"]")).click();
            waitHelper.waitForAndGetElementByLocator(By.cssSelector(".sound_repeat")).click();
            waitHelper.waitForAndGetElementByLocator(By.cssSelector(".playing_done"));
            driver.findElement(By.cssSelector("#btnSoundYes > span > button[type=\"button\"]")).click();
            waitHelper.waitForTitleAndAssert("Student: Login Shell Test Instructions and Help", false);
            driver.findElement(By.cssSelector("#btnStartTest > span > button[type=\"button\"]")).click();
            driver.findElement(By.id("Item_Response_1")).clear();
            driver.findElement(By.id("Item_Response_1")).sendKeys("Test");
            driver.findElement(By.cssSelector("span.optionClicker")).click();
            driver.findElement(By.id("Item_OptionContainer_Response_EBSR_2_Part2_A")).click();
            driver.findElement(By.cssSelector("#btnNext > span.icon")).click();
            driver.findElement(By.id("Item_Response_3")).clear();
            driver.findElement(By.id("Item_Response_3")).sendKeys("Test");
            driver.findElement(By.cssSelector("#btnNext > span.icon")).click();
            driver.findElement(By.cssSelector("#Item_OptionContainer_Response_MC_4_A > span.optionClicker")).click();
            driver.findElement(By.cssSelector("#btnNext > span.icon")).click();
            driver.findElement(By.cssSelector("#Item_OptionContainer_Response_MC_5_A > span.optionClicker")).click();
            driver.findElement(By.cssSelector("#btnNext > span.icon")).click();
            driver.findElement(By.id("micell-1689-1-a")).click();
            driver.findElement(By.id("micell-1689-1-b")).click();
            driver.findElement(By.id("micell-1689-2-c")).click();
            driver.findElement(By.id("micell-1689-2-d")).click();
            driver.findElement(By.cssSelector("#btnSave > span.icon")).click();
            driver.findElement(By.cssSelector("#btnEnd > span.icon")).click();
            driver.findElement(By.id("yui-gen9-button")).click();
            waitHelper.waitForAndGetElementByLocator(By.cssSelector("button[type=\"button\"]")).click();
            driver.findElement(By.id("yui-gen3-button")).click();
        }
        catch (Exception e) {
            String picPath = System.getProperty("user.dir") + "/failure-snapshots/exception-"+ System.currentTimeMillis() + ".png";
            savePhantomScreenshot(picPath);
            LOG.error("A timeout error occurred at {}. Saving screenshot of PhantomBrowser at {}.",
                    new Timestamp(System.currentTimeMillis()), picPath);
            throw e;
        }

    }
}
