package tests;

import driver.SmarterBalancedWebDriver;
import driver.impl.SmarterBalancedWebDriverImpl;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
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
    protected void proctorApproveStudent(boolean approveAll) throws Exception {
        Thread.sleep(1000);
        proctorDriver.findElement(By.id("btnRefresh")).click();
        Thread.sleep(1000);
        if (Integer.parseInt(proctorDriver.findElement(By.id("lblWaitingForApprovalsCount")).getText()) > 0) {
            proctorDriver.findElement(By.id("btnApprovals")).click();
            if (approveAll) {
                proctorDriver.findElement(By.id("btnApprovalAll")).click();
                proctorDriver.findElement(By.cssSelector(".dialogs a.confirm")).click();
            } else {
                proctorDriver.findElement(By.cssSelector(".approvals_holder td.approve a")).click();
            }
        }
    }

    protected void proctorDenyStudent(String reason) throws Exception {
        Thread.sleep(30000);
        if (Integer.parseInt(proctorDriver.findElement(By.id("lblWaitingForApprovalsCount")).getText()) > 0) {
            proctorDriver.findElement(By.id("btnApprovals")).click();
            proctorDriver.findElement(By.cssSelector(".approvals_holder td.deny a")).click();
            proctorDriver.findElement(By.id("txtReasonForDenial")).sendKeys(reason);
            proctorDriver.findElement(By.id("btnDenialOK")).click();
        }
    }

    @Before
    public void setup() throws InterruptedException {
        proctorDriver = new SmarterBalancedWebDriverImpl();
        navigator.setDriver(driver);
        proctorNavigator.setDriver(proctorDriver);

        sessionId = loginAndStartSession();
    }

    private String loginAndStartSession() throws InterruptedException {
        String sessionId;
        proctorDriver.get(PROCTOR_BASE_URL);
        proctorNavigator.proctorLogin(PROCTOR_USERNAME, PROCTOR_PASSWORD);
        proctorDriver.findElement(By.cssSelector("#customOverlay input[type='radio']")).click();
        proctorDriver.findElement(By.cssSelector("#customOverlay input[type='submit']")).click();
        closePopup();
        //assertEquals("TA Training Site", driver.getTitle());

        List<WebElement> testCheckBoxEls = proctorDriver.findElements(By.cssSelector("#selTestsListBox input[type='checkbox']"));
        for (WebElement checkBox : testCheckBoxEls) {
            // scroll to checkbox
            Actions actions = new Actions(proctorDriver);
            actions.moveToElement(checkBox);
            actions.click();
            actions.perform();
        }

        proctorDriver.findElement(By.id("btnStartSession")).click();
        Thread.sleep(1000);
        sessionId = proctorDriver.findElement(By.id("lblSessionID")).getText();

        return sessionId;
    }

    @After
    public void proctorLogout() throws InterruptedException {
        proctorDriver.findElement(By.id("btnStopSession")).click();
        //Confirm close session
        proctorDriver.findElement(By.cssSelector(".dialogs .action a.confirm")).click();
        proctorDriver.findElement(By.id("btnLogout")).click();
        Thread.sleep(1000);
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
