package cm.trixobase.sharenda.common;

/**
 * Created by noumianguebissie on 7/9/18.
 */

public enum TranslateElement {

    Category(AttributeName.Element_Category),
    Date(AttributeName.Element_Date);

    private final String name;

    @Override
    public String toString() {
        return name;
    }

    TranslateElement(String type) {
        name = type;
    }
}
