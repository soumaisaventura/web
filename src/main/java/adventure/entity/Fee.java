package adventure.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Fee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String name;

	private BigDecimal value;

	private Boolean percentage;

	private Boolean optional;

	@Transient
	private Boolean once;

	public Fee() {
	}

	public Fee(Integer id, String name, BigDecimal value, Boolean percentage, Boolean optional) {
		setId(id);
		setName(name);
		setValue(value);
		setPercentage(percentage);
		setOptional(optional);
	}

	public Fee(Integer id, String name, BigDecimal value, Boolean percentage, Boolean optional, Boolean once) {
		this(id, name, value, percentage, optional);
		setOnce(once);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Fee)) {
			return false;
		}
		Fee other = (Fee) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Boolean getPercentage() {
		return percentage;
	}

	public void setPercentage(Boolean percentage) {
		this.percentage = percentage;
	}

	public Boolean getOptional() {
		return optional;
	}

	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

	public Boolean getOnce() {
		return once;
	}

	public void setOnce(Boolean once) {
		this.once = once;
	}
}
