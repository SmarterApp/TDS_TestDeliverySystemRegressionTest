package enums;

/**
 * This enum maps test button types to their HTML ids
 *
 * Created by emunoz on 10/29/15.
 */
public enum TestButton {
    BACK("btnBack"),

    NEXT("btnNext"),

    SAVE("btnSave"),

    PAUSE("btnPause"),

    END("btnEnd"),

    ITEM_SCORE("btnItemScore"),

    RESULTS("btnResults"),

    RUBRIC("btnRubric"),

    PROTRACTOR("btnProtractor"),

    RULER("btnRuler"),

    MASK("btnMask"),

    DICTIONARY("btnDictionary"),

    PERIODIC_TABLE("btnPeriodic"),

    FORMULA("btnFormula"),

    CALCULATOR("btnCalculator"),

    GLOBAL_NOTES("btnGlobalNotes"),

    LINE_READER("btnLineReader"),

    PRINT("btnPrint"),

    PAGE_PRINT("btnPagePrint"),

    ZOOM_IN("btnZoomIn"),

    ZOOM_OUT("btnZoomOut"),

    CONTEXT("btnContext");


    /**
     * The button's html id value
     */
    private String id;

    TestButton(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}
