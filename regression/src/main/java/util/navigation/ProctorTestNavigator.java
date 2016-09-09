package util.navigation;

import driver.SmarterBalancedWebDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

/**
 * Created by emunoz on 2/5/16.
 */
@Service
public class ProctorTestNavigator {
    private SmarterBalancedWebDriver driver;

    public void proctorLogin(String username, String password) {
        driver.findElement(By.id("IDToken1")).sendKeys(username);
        driver.findElement(By.id("IDToken2")).sendKeys(password);
        driver.findElement(By.name("Login.Submit")).click();
    }

    public void setDriver(SmarterBalancedWebDriver driver) {
        this.driver = driver;
    }

}
