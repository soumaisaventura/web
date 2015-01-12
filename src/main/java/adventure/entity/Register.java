package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "REGISTER")
public class Register implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@ManyToOne(optional = false)
	@ForeignKey(name = "FK_REGISTER_RACE_CATEGORY")
	@JoinColumns({ @JoinColumn(name = "RACE_ID", referencedColumnName = "RACE_ID"),
			@JoinColumn(name = "COURSE_ID", referencedColumnName = "COURSE_ID"),
			@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID") })
	private RaceCategory raceCategory;

	@NotNull
	@Column(name = "TEAM_NAME")
	private String teamName;

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
		if (!(obj instanceof Register)) {
			return false;
		}
		Register other = (Register) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RaceCategory getRaceCategory() {
		return raceCategory;
	}

	public void setRaceCategory(RaceCategory raceCategory) {
		this.raceCategory = raceCategory;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}
