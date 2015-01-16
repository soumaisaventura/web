package adventure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "CITY")
public class City {

	@Id
	@Column(name = "ID")
	private Long id;

	@NotEmpty
	@Index(name = "IDX_CITY_NAME")
	@Column(name = "NAME")
	private String name;

	@ManyToOne(optional = false)
	@JoinColumn(name = "STATE_ID")
	@ForeignKey(name = "FK_CITY_STATE")
	@Index(name = "IDX_CITY_STATE")
	private State state;

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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
