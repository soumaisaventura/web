package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "COURSE")
public class Course implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_COURSE_RACE")
	@Index(name = "IDX_COURSE_RACE")
	private Race race;

	@NotNull
	@Column(name = "LENGTH")
	private Integer length;

	public Course(Race race) {
		this.race = race;
	}

	public Course() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
}
