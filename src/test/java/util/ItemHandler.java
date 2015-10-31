package util;

import driver.SmarterBalancedWebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class with the primary function of reading assessment items from the current assessment page and handling
 * the questions according to their {@link AssessmentItemType}.
 *
 * Created by emunoz on 10/21/15.
 */
public final class ItemHandler {
    private static final Logger LOG = LogManager.getLogger(ItemHandler.class);

    private static final String ASSESSMENT_ITEM_FORMAT_REGEX = "format_[a-z]+\\s";

    public static List<AssessmentItem> getAndHandleAssessmentItems(final SmarterBalancedWebDriver driver) {
        //Get the HTML element of each assessment item
        List<WebElement> containerEls = driver.findElements(By.cssSelector(".itemContainer.showing"));
        List<AssessmentItem> items = new ArrayList<>();

        //Iterate through each assessment item on the current page and construct the list of AssessmentItems to handle.
        for (WebElement itemDiv : containerEls) {
            String itemClassStr = getAssessmentItemTypeFromClassStr(itemDiv.getAttribute("class"));
            AssessmentItem item = new AssessmentItem(itemClassStr, itemDiv.getAttribute("id"));
            items.add(item);
        }

        handleTestQuestions(items, driver);

        return items;
    }

    private static void handleTestQuestions (final List<AssessmentItem> items, final SmarterBalancedWebDriver driver) {
        //Handle each individual test item
        for (AssessmentItem item : items) {
            LOG.info("Handling an assessment item with the item type {}", item.getType().name());
            switch (item.getType()) {
                case EBSR:
                    handleEvidenceBasedSelectiveResponse(item.getId(), driver);
                    break;
                case EQ:
                    handleEquation(item.getId(), driver);
                    break;
                case ER:
                case NL:
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
                case TI:
                    handleTableInteraction(item.getId(), driver);
                    break;
                case SA:
                case WER:
                    handleShortAnswerAndWritingExtendedResponse(item.getId(), driver);
                    break;
                default:
                    LOG.warn("Unrecognized test item type {}. Cannot handle this question.", item.getType().name());
            }
        }
    }

    /**
     * Handles grid assessment item types. These item types can require either drag-and-drop, click an item, or draw an
     * arrow/line functionality.
     *
     * @param id
     * @param driver
     */
    private static void handleGridItem(final String id, final SmarterBalancedWebDriver driver) {
        WebElement objectTag = driver.findElement(By.cssSelector("#" + id + " object"));
        WebElement origin = driver.findElement(By.cssSelector("#htmlBody"));
        JavascriptExecutor jsDriver =(JavascriptExecutor) driver;
        Actions builder = new Actions(driver);

        Point dropContainer = getDropContainerCoordinates(jsDriver, objectTag);
        Point sourceContainer = getSourceContainerCoordinates(jsDriver, objectTag);

        Map<String, Object> gridOffset = (Map) jsDriver.executeScript(
                "return ($(arguments[0].contentDocument).find('#groupWrapper').offset())", objectTag);

        if (sourceContainer == null && dropContainer == null) { // This must be a "draw an arrow/line" question.
            //get the arrow button coordinates
            Map<String, Long> arrowPoint = (Map) jsDriver.executeScript(
                    "return ($(arguments[0].contentDocument).find('#button_arrow').offset())", objectTag);

            builder.moveToElement(origin, 0, 0)
                    .moveByOffset(
                            arrowPoint.get("left").intValue() + objectTag.getLocation().getX() + 60,
                            arrowPoint.get("top").intValue() + objectTag.getLocation().getY() + 15)
                    .clickAndHold().release()
                    .moveByOffset(0, 200)
                    .clickAndHold()
                    .moveByOffset(0, 100)
                    .release().click()
                    .build()
                    .perform();
        } else if (sourceContainer == null ||   //No drag/drop required - just click on target
                (sourceContainer.getX() == 0 && sourceContainer.getY() == 0)) {
            int dropAbsX = ((Long) gridOffset.get("left")).intValue() + dropContainer.getX()
                    + objectTag.getLocation().getX() + 20;
            int dropAbsY = ((Long) gridOffset.get("top")).intValue() + dropContainer.getY()
                    + objectTag.getLocation().getY() + 20;

            builder.moveToElement(origin, 0, 0)
                    .moveByOffset(dropAbsX, dropAbsY)
                    .clickAndHold().release()
                    .build()
                    .perform();
        } else {//Otherwise, drag and drop
            int srcAbsX = sourceContainer.getX() + objectTag.getLocation().getX() + 20;
            int srcAbsY = sourceContainer.getY() + objectTag.getLocation().getY() + 20;
            int dropAbsX = ((Long) gridOffset.get("left")).intValue() + dropContainer.getX()
                    + objectTag.getLocation().getX() + 20;
            int dropAbsY = ((Long) gridOffset.get("top")).intValue() + dropContainer.getY()
                    + objectTag.getLocation().getY() + 20;

            builder.moveToElement(origin, 0, 0)
                    .moveByOffset(srcAbsX, srcAbsY)
                    //.click()
                    .clickAndHold()
                    .moveByOffset(
                            dropAbsX - srcAbsX,
                            dropAbsY - srcAbsY)
                    .release()
                    .click()
                    .build()
                    .perform();

        }
    }
    private static Point getSourceContainerCoordinates(JavascriptExecutor jsDriver, WebElement objectTag) {
        Point sourcePt = null;
        Map<String, Object> srcPos = (Map) jsDriver.executeScript(
                "return ($(arguments[0].contentDocument).find('image').eq(1).offset())", objectTag);

        if (srcPos != null) {
            Double srcLeft = null;
            Double srcTop = null;

            if (srcPos.get("left") instanceof Long) {
                srcLeft = ((Long) srcPos.get("left")).doubleValue();
            } else {
                srcLeft = (Double) srcPos.get("left");
            }
            if (srcPos.get("top") instanceof Long) {
                srcTop = ((Long) srcPos.get("top")).doubleValue();
            } else {
                srcTop = (Double) srcPos.get("top");
            }

            sourcePt = new Point(srcLeft.intValue(), srcTop.intValue());
        }

        return sourcePt;
    }

    private static Point getDropContainerCoordinates(JavascriptExecutor driver, WebElement objectTag) {
        Point dropPt = null;
        String dropX = (String) driver.executeScript( // assuming its a circle container, this returns something...
                "return ($(arguments[0].contentDocument).find('#shapes').children().eq(2).attr('cx'))", objectTag);
        String dropY = (String) driver.executeScript(
                "return ($(arguments[0].contentDocument).find('#shapes').children().eq(2).attr('cy'))", objectTag);

        if (dropX == null || dropY == null) { // this must be a "rect" container, so get the "x" attr instead
            dropX = (String) driver.executeScript(
                    "return ($(arguments[0].contentDocument).find('#shapes').children().eq(2).attr('x'))", objectTag);
            dropY = (String) driver.executeScript(
                    "return ($(arguments[0].contentDocument).find('#shapes').children().eq(2).attr('y'))", objectTag);
        }

        if (dropX != null && dropY != null) {
            dropPt = new Point((int) Double.parseDouble(dropX), (int) Double.parseDouble(dropY));
        }

        return dropPt;
    }

    /**
     * Handles short answer and WER item types by entering text into the text-editor iframe widget.
     * @param id
     * @param driver
     */
    private static void handleShortAnswerAndWritingExtendedResponse(final String id, final SmarterBalancedWebDriver driver) {
        driver.switchToIframe(By.cssSelector("#" + id + " iframe"));
        WebElement editable = driver.switchTo().activeElement();
        editable.sendKeys("Practice Test");
        driver.switchOutOfIFrame();
    }

    /**
     * Handles table interaction item types by entering numerical text into the item's input field.
     * @param id
     * @param driver
     */
    private static void handleTableInteraction(final String id, final SmarterBalancedWebDriver driver) {
        WebElement tiInput = driver.findElement(By.cssSelector("#" + id + " input.ti-input"));
        tiInput.clear();
        tiInput.sendKeys("42");
    }

    /**
     * Handles equation item types by clicking on keypad buttons.
     *
     * @param id
     * @param driver
     */
    private static void handleEquation(final String id, final SmarterBalancedWebDriver driver) {
        driver.findElement(By.cssSelector("#" + id + " .keypad-item[aria-label='four']")).click();
        driver.findElement(By.cssSelector("#" + id + " .keypad-item[aria-label='two']")).click();
    }

    /**
     * Handles multi select item types by clicking on every checkbox available.
     * @param id
     * @param driver
     */
    private static void handleMultiSelect(final String id, final SmarterBalancedWebDriver driver) {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("#" + id + " .optionClicker"));

        for (WebElement checkbox : checkboxes) {
            checkbox.click();
        }
    }

    /**
     * Handles match interaction assessment item types by clicking every checkbox available.
     *
     * @param id
     * @param driver
     */
    private static void handleMatchInteraction(final String id, final SmarterBalancedWebDriver driver) {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("#" + id + " input[type='checkbox']"));

        //Just check em all...
        for (WebElement checkbox : checkboxes) {
            checkbox.click();
        }
    }

    /**
     * Handles a basic multiple choice question by click on the first option.
     *
     * @param id
     * @param driver
     */
    private static void handleMultipleChoice(final String id, final SmarterBalancedWebDriver driver) {
        driver.findElement(By.cssSelector("#" + id + " .optionClicker")).click();
    }

    /**
     * Handles hot text items - can be orderable list of selections, or a set of clickable sentences/words.
     *
     * @param id
     * @param driver
     */
    private static void handleHotText(final String id, final SmarterBalancedWebDriver driver) {
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

    /**
     * Handles extended response items by entering text into the item's textarea
     *
     * @param id
     * @param driver
     */
    private static void handleExtendedResponse(String id, SmarterBalancedWebDriver driver) {
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
    private static void handleEvidenceBasedSelectiveResponse(String id, SmarterBalancedWebDriver driver) {
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
    private static String getAssessmentItemTypeFromClassStr(final String classStr) {
        Pattern pattern = Pattern.compile(ASSESSMENT_ITEM_FORMAT_REGEX);
        Matcher matcher = pattern.matcher(classStr);
        String assessmentType = null;

        if (matcher.find()) {
            assessmentType = matcher.group(0).trim();
        }

        return assessmentType;
    }

}
