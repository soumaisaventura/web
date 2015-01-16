package adventure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "COUNTRY")
public class Country {

	@Id
	@Column(name = "ID")
	private Long id;

	@NotEmpty
	@Index(name = "IDX_COUNTRY_NAME")
	@Column(name = "NAME")
	private String name;

	@NotEmpty
	@Index(name = "IDX_COUNTRY_ABBREVIATION")
	@Column(name = "ABBREVIATION")
	private String abbreviation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
