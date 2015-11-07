package adventure.entity;

import static adventure.util.Constants.ACRONYM_SIZE;
import static adventure.util.Constants.NAME_SIZE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "modality")
public class Modality implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Integer id;

	@NotEmpty
	@Size(max = NAME_SIZE)
	@Column(name = "name")
	private String name;

	@Size(max = ACRONYM_SIZE)
	@Column(name = "acronym")
	private String acronym;

	public Modality() {
	}

	public Modality(Integer id, String name, String acronym) {
		setId(id);
		setName(name);
		setAcronym(acronym);
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
		if (!(obj instanceof Modality)) {
			return false;
		}
		Modality other = (Modality) obj;
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
