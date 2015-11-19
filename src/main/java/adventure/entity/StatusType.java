package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum StatusType {

	SOON(0, "soon"), OPEN(1, "open"), CLOSED(2, "closed"), END(3, "end");

	private final Integer order;

	private final String value;

	StatusType(Integer order, String value) {
		this.order = order;
		this.value = value;
	}

	public Integer getOrder() {
		return order;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static StatusType fromValue(String value) {
		StatusType result = null;

		for (StatusType statusType : values()) {
			if (statusType.toString().equalsIgnoreCase(value)) {
				result = statusType;
				break;
			}
		}

		return result;
	}
}
