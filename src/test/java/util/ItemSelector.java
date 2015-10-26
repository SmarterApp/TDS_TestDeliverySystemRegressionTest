package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by emunoz on 10/21/15.
 */
public final class ItemSelector {
    private static final Logger LOG = LogManager.getLogger(ItemSelector.class);

    private static final String ASSESSMENT_ITEM_FORMAT_REGEX = "format_[a-z]+\\s";

    public static List<AssessmentItem> getAssessmentItemsFromPage(WebDriver driver) {
        List<WebElement> containerEls = driver.findElements(By.cssSelector(".itemContainer.showing"));
        List<AssessmentItem> items = new ArrayList<>();

        for (WebElement itemDiv : containerEls) {
            String itemClassStr = getAssessmentItemTypeFromClassStr(itemDiv.getAttribute("class"));
            AssessmentItem item = new AssessmentItem(itemClassStr, itemDiv.getAttribute("id"));
            items.add(item);
        }

        handleTestQuestions(items, driver);

        return items;
    }

    private static void handleTestQuestions (List<AssessmentItem> items, WebDriver driver) {
        //Handle each individual test item
        for (AssessmentItem item : items) {
            switch (item.getType()) {
                case EBSR:
                    handleEvidenceBasedSelectiveResponse(item.getId(), driver);
                    break;
                case EQ:
                    handleEquation(item.getId(), driver);
                    break;
                case ER:
                    handleExtendedResponse(item.getId(), driver);
                    break;
                case GI:
                    handleGridItem(item.getId(), driver);
                    break;
                case HTQ:
                    handleHotText(item.getId(), driver);
                    break;
                case MC:
                    handleMultipleChoice(item.getId(), driver);
                    break;
                case MI:
                    handleMatchInteraction(item.getId(), driver);
                    break;
                case MS:
                    handleMultiSelect(item.getId(), driver);
                    break;
                case PASS:
                    handlePass(item.getId(), driver);
                    break;
                case SA:
                    handleShortAnswer(item.getId(), driver);
                    break;
                case TI:
                    handleTableInteraction(item.getId(), driver);
                    break;
                case WER:
                    handleWritingExtendedResponse(item.getId(), driver);
                    break;
                default:
                    LOG.warn("Unrecognized test item type {}. Cannot handle this question.", item.getType().name());
            }
        }
    }

    private static void handleWritingExtendedResponse(String id, WebDriver driver) {
        WebElement werIFrame = driver.findElement(By.cssSelector("#" + id + " iframe"));
        driver.switchTo().frame(werIFrame);
        WebElement editable = driver.switchTo().activeElement();
        editable.sendKeys("Practice Test");
        //Get out of the iframe
        driver.switchTo().defaultContent();
    }

    private static void handleTableInteraction(String id, WebDriver driver) {

    }

    private static void handleShortAnswer(String id, WebDriver driver) {

    }

    private static void handlePass(String id, WebDriver driver) {

    }

    private static void handleGridItem(String id, WebDriver driver) {

    }

    private static void handleEquation(String id, WebDriver driver) {
        driver.findElement(By.cssSelector("#" + id + " .keypad-item[aria-label='four']")).click();
        driver.findElement(By.cssSelector("#" + id + " .keypad-item[aria-label='two']")).click();
    }

    private static void handleMultiSelect(String id, WebDriver driver) {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("#" + id + " .optionClicker"));

        for (WebElement checkbox : checkboxes) {
            checkbox.click();
        }
    }

    private static void handleMatchInteraction(String id, WebDriver driver) {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("#" + id + " input[type='checkbox']"));

        //Just check em all...
        for (WebElement checkbox : checkboxes) {
            checkbox.click();
        }
    }

    private static void handleMultipleChoice(String id, WebDriver driver) {
        driver.findElement(By.cssSelector("#" + id + " .optionClicker")).click();
    }

    private static void handleHotText(String id, WebDriver driver) {
        boolean orderable = false;
        List<WebElement> itemDivs = driver.findElements(By.cssSelector("#" + id + " .interaction"));

        WebElement firstEl = itemDivs.get(0);

        if (firstEl != null) {
            orderable = firstEl.getAttribute("class").contains("order-group");
        }

        if (orderable) {
            //Ordering question type - drag and drop
            Actions builder = new Actions(driver);

            WebElement srcEl = itemDivs.get(1);
            WebElement destEl = itemDivs.get(3);

            builder.dragAndDrop(srcEl, destEl).perform();

            builder.dragAndDrop(itemDivs.get(2), itemDivs.get(4)).perform();
        } else {
            //Selectable question type - click on all the options
            for(WebElement itemDiv : itemDivs) {
                itemDiv.click();
            }
        }

    }

    private static void handleExtendedResponse(String id, WebDriver driver) {
        WebElement itemDiv = driver.findElement(By.cssSelector("#" + id + " textarea"));
        itemDiv.clear();
        itemDiv.sendKeys("Practice Test");
    }

    /**
     * Usually multi-part, evidence based multiple choice questions
     *
     * @param id
     * @param driver
     */
    private static void handleEvidenceBasedSelectiveResponse(String id, WebDriver driver) {
        List<WebElement> itemDivs = driver.findElements(By.cssSelector("#" + id + " .interactionContainer"));

        for (WebElement itemDiv : itemDivs) {
            itemDiv.findElement(By.cssSelector("span.optionClicker")).click();
        }
    }

    /**
     * This method parses out and returns the html class string corresponding to the assessment item type.
     * Format return is "format_<item-type-shortname>".
     *
     * @param classStr
     * @return
     */
    private static String getAssessmentItemTypeFromClassStr(String classStr) {
        Pattern pattern = Pattern.compile(ASSESSMENT_ITEM_FORMAT_REGEX);
        Matcher matcher = pattern.matcher(classStr);
        String assessmentType = null;

        if (matcher.find()) {
            assessmentType = matcher.group(0).trim();
        }

        return assessmentType;
    }

}
