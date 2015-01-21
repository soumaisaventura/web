package adventure.entity;

import static adventure.util.Constants.ABBREVIATION_SIZE;
import static adventure.util.Constants.NAME_SIZE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "COUNTRY")
public class Country {

	@Id
	@Column(name = "ID")
	private Integer id;

	@NotEmpty
	@Size(max = NAME_SIZE)
	@Column(name = "NAME")
	@Index(name = "IDX_COUNTRY_NAME")
	private String name;

	@NotEmpty
	@Size(max = ABBREVIATION_SIZE)
	@Column(name = "ABBREVIATION")
	@Index(name = "IDX_COUNTRY_ABBREVIATION")
	private String abbreviation;

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
		if (!(obj instanceof Country)) {
			return false;
		}
		Country other = (Country) obj;
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

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
}
