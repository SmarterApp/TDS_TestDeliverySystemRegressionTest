package tests.student.practicetest.general;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.StudentBaseTest;
import tests.StudentPracticeTestBaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emunoz on 2/10/16.
 */
public class SettingsTest extends StudentPracticeTestBaseTest {
    @Before
    public void openHomeAndLogin() {
        driver.get(BASE_URL);

        // Login Phase (GUEST)
        assertEquals("Student: Login Shell Please Sign In", driver.getTitle());
    }

    @Test
    public void testSettings() {
        driver.findElement(By.id("btnAccGlobal")).click();

        assertEquals("Choose Settings:", driver.findElement(By.cssSelector("#globalAccDialog .hd")).getText());
        assertTrue(driver.isElementVisibleNow(By.cssSelector("#globalAccDialog .ft .first-child button")));
        assertEquals("Submit", driver.findElement(By.cssSelector("#globalAccDialog .ft .default .first-child button")).getText());
        // Click submit
        driver.findElement(By.cssSelector("#globalAccDialog .ft .default .first-child button")).click();
        assertFalse(driver.isElementVisibleNow(By.cssSelector("#globalAccDialog")));
    }
}
