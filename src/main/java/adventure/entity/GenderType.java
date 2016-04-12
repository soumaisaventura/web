package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum GenderType {

    MALE("M"), FEMALE("F");

    private final String value;

    GenderType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static GenderType fromValue(String value) {
        GenderType result = null;

        for (GenderType type : values()) {
            if (type.toString().equalsIgnoreCase(value)) {
                result = type;
                break;
            }
        }

        return result;
    }

    @JsonValue
    public String toString() {
        return this.value;
    }
}
