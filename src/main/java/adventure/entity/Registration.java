package adventure.entity;

import static adventure.util.Constants.ENUM_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import br.gov.frameworkdemoiselle.util.Strings;

@Entity
@Table(name = "REGISTRATION")
public class Registration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE, generator = "SEQ_REGISTRATION")
	@SequenceGenerator(name = "SEQ_REGISTRATION", sequenceName = "SEQ_REGISTRATION", allocationSize = 1)
	private Long id;

	@ManyToOne(optional = false)
	@ForeignKey(name = "FK_REGISTRATION_RACE_CATEGORY")
	@JoinColumns({ @JoinColumn(name = "RACE_ID", referencedColumnName = "RACE_ID"),
			@JoinColumn(name = "COURSE_ID", referencedColumnName = "COURSE_ID"),
			@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID") })
	private RaceCategory raceCategory;

	@NotNull
	@Size(max = NAME_SIZE)
	@Column(name = "TEAM_NAME")
	private String teamName;

	@NotNull
	@Column(name = "DATE")
	@Index(name = "IDX_REGISTRATION_DATE")
	private Date date;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SUBMITTER_ID")
	@ForeignKey(name = "FK_REGISTRATION_USER")
	@Index(name = "IDX_REGISTRATION_USER")
	private User submitter;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "STATUS", length = ENUM_SIZE)
	@Index(name = "IDX_REGISTRATION_STATUS")
	private StatusType status;

	public Registration() {
	}

	public Registration(Long registrationId, Date registrationDate, String teamName, Integer submitterId,
			String submitterName, StatusType registrationStatus, Integer raceId, String raceName, Date raceDate,
			Integer categoryId, String categoryName, Integer courseId, Integer courseLength) {
		setId(registrationId);
		setDate(registrationDate);
		setTeamName(teamName);
		setStatus(registrationStatus);

		User submitter = new User();
		setSubmitter(submitter);
		getSubmitter().setId(submitterId);
		getSubmitter().setProfile(new Profile(submitter));
		getSubmitter().getProfile().setName(categoryName);

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

	public String getFormattedId() {
		return this.getId() != null ? Strings.insertZeros(this.getId().toString(), 5) : null;
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
		if (!(obj instanceof Registration)) {
			return false;
		}
		Registration other = (Registration) obj;
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

	public User getSubmitter() {
		return submitter;
	}

	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}
}
