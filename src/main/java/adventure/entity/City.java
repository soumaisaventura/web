package adventure.entity;

import static adventure.util.Constants.NAME_SIZE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "CITY")
public class City {

	@Id
	@Column(name = "ID")
	private Integer id;

	@NotEmpty
	@Size(max = NAME_SIZE)
	@Column(name = "NAME")
	@Index(name = "IDX_CITY_NAME")
	private String name;

	@ManyToOne(optional = false)
	@JoinColumn(name = "STATE_ID")
	@ForeignKey(name = "FK_CITY_STATE")
	@Index(name = "IDX_CITY_STATE")
	private State state;

	public City() {
	}

	public City(Integer id, String name, String stateName, String stateAbbreviation, String countryName) {
		setId(id);
		setName(name);
		setState(new State());
		getState().setName(stateName);
		getState().setAbbreviation(stateAbbreviation);
		getState().setCountry(new Country());
		getState().getCountry().setName(countryName);
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
		if (!(obj instanceof City)) {
			return false;
		}
		City other = (City) obj;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
