package adventure.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
// @Table(name = "sport")
public class Sport implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	// @Column(name = "id")
	private Integer id;

	// @NotEmpty
	// @Size(max = NAME_SIZE)
	// @Column(name = "name")
	// @Index(name = "idx_sport_name")
	private String name;

	// @Size(max = ACRONYM_SIZE)
	// @Column(name = "acronym")
	// @Index(name = "idx_sport_acronym")
	private String acronym;

	public Sport() {
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
		if (!(obj instanceof Sport)) {
			return false;
		}
		Sport other = (Sport) obj;
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

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
}
