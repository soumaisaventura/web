package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

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

	@NotNull
	@Temporal(DATE)
	@Column(name = "DATE")
	@Index(name = "IDX_REGISTER_DATE")
	private Date date;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ACCOUNT_ID")
	@ForeignKey(name = "FK_REGISTER_ACCOUNT")
	@Index(name = "IDX_REGISTER_ACCOUNT")
	private Account creator;

	public Register() {
	}

	public Register(Long registerId, Date registerDate, String teamName, Long creatorId, String creatorName,
			Long raceId, String raceName, Date raceDate, Long categoryId, String categoryName, Long courseId, Integer courseLength) {
		setId(registerId);
		setDate(registerDate);
		setTeamName(teamName);

		Account creator = new Account();
		setCreator(creator);
		getCreator().setId(creatorId);
		getCreator().setProfile(new Profile(creator));
		getCreator().getProfile().setName(categoryName);

		setRaceCategory(new RaceCategory());
		getRaceCategory().setCategory(new Category());
		getRaceCategory().getCategory().setId(categoryId);
		getRaceCategory().getCategory().setName(categoryName);

		getRaceCategory().setRace(new Race());
		getRaceCategory().getRace().setId(raceId);
		getRaceCategory().getRace().setName(raceName);
		getRaceCategory().getRace().setDate(raceDate);

		getRaceCategory().setCourse(new Course());
		getRaceCategory().getCourse().setId(courseId);
		getRaceCategory().getCourse().setLength(courseLength);
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}
}
