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
@Table(name = "REGISTER")
// @org.hibernate.annotations.Table(appliesTo = "REGISTER", foreignKey = @ForeignKey())
// @AssociationOverride()
// @PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn()})
public class Register implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_REGISTER_AC_RACE")
	@Index(name = "IDX_REGISTER_AC_RACE")
	@ManyToOne(optional = false)
	private Race race;

	@JoinColumn(name = "COURSE_ID")
	@ForeignKey(name = "FK_REGISTER_AC_COURSE")
	@Index(name = "IDX_REGISTER_AC_COURSE")
	@ManyToOne(optional = false)
	private Course course;

	@JoinColumn(name = "CATEGORY_ID")
	@ForeignKey(name = "FK_REGISTER_AC_CATEGORY")
	@Index(name = "IDX_REGISTER_AC_CATEGORY")
	@ManyToOne(optional = false)
	private Category category;

	@NotNull
	@Column(name = "TEAM_NAME")
	private String teamName;

	// @ManyToOne
	// private AvailableCategory availableCategory;

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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}
