package tds.enums;

/**
 * Enumeration defining different test item types and their descriptions.
 *
 * Created by emunoz on 10/20/15.
 */
public enum AssessmentItemType {
    EBSR("EBSR", "Evidence-Based Selected Response Item"),

    EQ("eq", "Equation Item"),

    GI("gi", "Grid Item"),

    HTQ("htq", "Hot Text Item"),

    MC("mc", "Multiple Choice Item (single selection)"),

    MI("mi", "Match Interaction Item"),

    MS("ms", "Multi-Select Item"),

    PASS("pass", "Passage Item"),

    SA("sa", "Short Answer Item"),

    TI("ti", "Table Interaction Item"),

    WER("wer", "Writing Extended Response Item");

    private String code;

    private String description;

    AssessmentItemType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
