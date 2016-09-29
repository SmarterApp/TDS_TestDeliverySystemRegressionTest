package enums;

/**
 * Represents the mappings between writing WYSIWYG buttons and their respective HTML
 * classname to facilitate click actions.
 *
 * Created by emunoz on 11/3/15.
 */
public enum WritingToolsButton {
    BOLD("cke_button__bold"),
    ITALIC("cke_button__italic"),
    UNDERLINE("cke_button__underline"),
    REMOVE_FORMAT("cke_button__removeformat"),
    NUMBERED_LIST("cke_button__numberedlist"),
    BULLETED_LIST("cke_button__bulletedlist"),
    OUTDENT("cke_button__outdent"),
    INDENT("cke_button__indent"),
    CUT("cke_button__cut"),
    COPY("cke_button__copy"),
    PASTE("cke_button__paste"),
    UNDO("cke_button__undo"),
    REDO("cke_button__redo"),
    SPELLCHECKER("cke_button__spellchecker"),
    SPECIAL_CHAR("cke_button__tdsspecialchar");

    /**
     * The button's html id value
     */
    private final String className;

    WritingToolsButton(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}
