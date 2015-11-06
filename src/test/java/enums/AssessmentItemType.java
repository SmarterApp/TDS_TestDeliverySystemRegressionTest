package enums;

/**
 * Enumeration defining different test item types and their descriptions.
 * <p/>
 * Created by emunoz on 10/20/15.
 */
public enum AssessmentItemType {
    EBSR("EBSR", "format_ebsr", "Evidence-Based Selected Response Item"),

    ER("er", "format_er", "Extended-Response Item"),

    EQ("eq", "format_eq", "Equation Item"),

    GI("gi", "format_gi", "Grid Item"),

    HTQ("htq", "format_htq", "Hot Text Item"),

    MC("mc", "format_mc", "Multiple Choice Item (single selection)"),

    MI("mi", "format_mi", "Match Interaction Item"),

    MS("ms", "format_ms", "Multi-Select Item"),

    NL("nl", "format_nl", "Natural Language Item"), //Deprecated?

    SA("sa", "format_sa", "Short Answer Item"),

    TI("ti", "format_ti", "Table Interaction Item"),

    WER("wer", "format_wer", "Writing Extended Response Item");

    /**
     * The short-form code corresponding to the assessmentment item type.
     */
    private final String code;

    /**
     * The HTML class name that the itemContainer div element contains for this
     * assessment item type.
     */
    private final String className;

    /**
     * A description of the assement item type
     */
    private final String description;

    AssessmentItemType(final String code, final String className, final String description) {
        this.code = code;
        this.className = className;
        this.description = description;
    }

    /**
     * Retrieves an {@link AssessmentItemType} enum corresponding to the specified className string.
     *
     * @param formatStr in the form of "format_<item-type>"
     * @return The corresponding {@link AssessmentItemType}
     */
    public static AssessmentItemType fromString(final String formatStr) {
        if (formatStr != null) {
            for (AssessmentItemType type : AssessmentItemType.values()) {
                if (formatStr.equalsIgnoreCase(type.getClassName())) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("Corresponding AssessmentItemType enum not found for the specified classname");
    }

    public String getCode() {
        return code;
    }

    public String getClassName() { return className; }

    public String getDescription() {
        return description;
    }
}
