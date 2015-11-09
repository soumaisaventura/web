package adventure.entity;

public class RaceAnalytic {

	private String label;

	private Integer value;

	public RaceAnalytic(String label, Long value) {
		setLabel(label);
		setValue(value.intValue());
	}

	public RaceAnalytic(RegistrationStatusType status, Long value) {
		setLabel(status.name());
		setValue(value.intValue());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RaceAnalytic)) {
			return false;
		}
		RaceAnalytic other = (RaceAnalytic) obj;
		if (label == null) {
			if (other.label != null) {
				return false;
			}
		} else if (!label.equals(other.label)) {
			return false;
		}
		return true;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
