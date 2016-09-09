package tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import util.navigation.ProctorTestNavigator;

import java.util.List;
import java.util.Set;

/**
 * Created by emunoz on 1/26/16.
 */
public abstract class ProctorBaseTest extends SeleniumBaseTest {
    @Value( "${tds.proctor.baseurl}" )
    protected String BASE_URL;

    @Value( "${tds.proctor.username}" )
    protected String PROCTOR_USERNAME;

    @Value( "${tds.proctor.password}" )
    protected String PROCTOR_PASSWORD;

    @Autowired
    protected ProctorTestNavigator navigator;

    @Before
    public void setup() {
        navigator.setDriver(driver);
        loginAndStartSession();
    }


    private void loginAndStartSession() {
        driver.get(BASE_URL);
        navigator.proctorLogin(PROCTOR_USERNAME, PROCTOR_PASSWORD);
        driver.findElement(By.cssSelector("#customOverlay input[type='radio']")).click();
        driver.findElement(By.cssSelector("#customOverlay input[type='submit']")).click();
        closePopup();
    }

    @After
    public void proctorLogout() {
        driver.findElement(By.id("btnLogout")).click();
        driver.findElement(By.cssSelector(".dialogs a.close")).click();
        driver.quit();
    }

    private void closePopup() {
        String parentWindow = driver.getWindowHandle();
        Set<String> handles =  driver.getWindowHandles();
        for (String windowHandle : handles) {
            if (!windowHandle.equals(parentWindow)) {
                driver.switchTo().window(windowHandle);
                driver.close(); //closing child window
                driver.switchTo().window(parentWindow);
            }
        }
    }
}
