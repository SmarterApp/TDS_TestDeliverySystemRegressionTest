package tests;

import driver.SmarterBalancedWebDriver;
import driver.impl.SmarterBalancedWebDriverImpl;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import util.navigation.ProctorTestNavigator;
import util.navigation.StudentTestNavigator;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by emunoz on 1/26/16.
 */
public abstract class StudentBaseTest extends SeleniumBaseTest {
    @Value( "${tds.student.baseurl}" )
    protected String BASE_URL;

    @Value( "${tds.student.ssid}" )
    protected String STUDENT_SSID;

    @Value( "${tds.student.firstname}" )
    protected String STUDENT_FIRSTNAME;

    @Value( "${tds.proctor.baseurl}" )
    protected String PROCTOR_BASE_URL;

    @Value( "${tds.proctor.username}" )
    protected String PROCTOR_USERNAME;

    @Value( "${tds.proctor.password}" )
    protected String PROCTOR_PASSWORD;

    @Autowired
    protected StudentTestNavigator navigator;

    @Autowired
    protected ProctorTestNavigator proctorNavigator;

    protected SmarterBalancedWebDriver proctorDriver;

    protected String sessionId;

    /**
     * This method causes the proctor to approve any students pending test approval after a 30 second pause.
     *
     * @throws Exception
     */
    protected void proctorApproveStudent() throws Exception {
        Thread.sleep(30000);
        if (Integer.parseInt(proctorDriver.findElement(By.id("lblWaitingForApprovalsCount")).getText()) > 0) {
            proctorDriver.findElement(By.id("btnApprovals")).click();
            proctorDriver.findElement(By.id("btnApprovalAll")).click();
            proctorDriver.findElement(By.cssSelector(".dialogs a.confirm")).click();
        }
    }

    @Before
    public void setup() {
        proctorDriver = new SmarterBalancedWebDriverImpl();
        navigator.setDriver(driver);
        proctorNavigator.setDriver(proctorDriver);

        sessionId = loginAndStartSession();
    }

    private String loginAndStartSession() {
        String sessionId;
        proctorDriver.get(PROCTOR_BASE_URL);
        proctorNavigator.proctorLogin(PROCTOR_USERNAME, PROCTOR_PASSWORD);
        proctorDriver.findElement(By.cssSelector("#customOverlay input[type='radio']")).click();
        proctorDriver.findElement(By.cssSelector("#customOverlay input[type='submit']")).click();
        closePopup();
        //assertEquals("TA Training Site", driver.getTitle());

        List<WebElement> testCheckBoxEls = proctorDriver.findElements(By.cssSelector("#selTestsListBox input[type='checkbox']"));
        for (WebElement checkBox : testCheckBoxEls) {
            checkBox.click();
        }

        proctorDriver.findElement(By.id("btnStartSession")).click();
        sessionId = proctorDriver.findElement(By.id("lblSessionID")).getText();

        return sessionId;
    }

    @After
    public void proctorLogout() {
        proctorDriver.findElement(By.id("btnStopSession")).click();
        //Confirm close session
        proctorDriver.findElement(By.cssSelector(".dialogs .action a.confirm")).click();
        proctorDriver.findElement(By.id("btnLogout")).click();
        proctorDriver.waitForAndFindElement(By.cssSelector(".dialogs a.close")).click();
        proctorDriver.quit();
    }

    private void closePopup() {
        String parentWindow = proctorDriver.getWindowHandle();
        Set<String> handles =  proctorDriver.getWindowHandles();
        for (String windowHandle : handles) {
            if (!windowHandle.equals(parentWindow)) {
                proctorDriver.switchTo().window(windowHandle);
                proctorDriver.close(); //closing child window
                proctorDriver.switchTo().window(parentWindow);
            }
        }
    }
}
