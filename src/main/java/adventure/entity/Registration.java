package adventure.entity;

import static adventure.util.Constants.ENUM_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import br.gov.frameworkdemoiselle.util.Strings;

@Entity
@Table(name = "registration", uniqueConstraints = {
		@UniqueConstraint(name = "uk_registration_payment_code", columnNames = { "payment_code" }),
		@UniqueConstraint(name = "uk_registration_payment_transaction", columnNames = { "payment_transaction" }) })
public class Registration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_registration")
	@SequenceGenerator(name = "seq_registration", sequenceName = "seq_registration", allocationSize = 1)
	private Long id;

	@ManyToOne(optional = false)
	@ForeignKey(name = "fk_registration_race_category")
	@JoinColumns({ @JoinColumn(name = "race_id", referencedColumnName = "race_id"),
			@JoinColumn(name = "course_id", referencedColumnName = "course_id"),
			@JoinColumn(name = "category_id", referencedColumnName = "category_id") })
	private RaceCategory raceCategory;

	@ManyToOne(optional = false)
	@JoinColumn(name = "period_id")
	@ForeignKey(name = "fk_registration_period")
	@Index(name = "idx_registration_period")
	private Period period;

	@NotNull
	@Size(max = NAME_SIZE)
	@Column(name = "team_name")
	private String teamName;

	@NotNull
	@Column(name = "date")
	@Index(name = "idx_registration_date")
	private Date date;

	@ManyToOne(optional = false)
	@JoinColumn(name = "submitter_id")
	@ForeignKey(name = "fk_registration_submitter")
	@Index(name = "idx_registration_submitter")
	private User submitter;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "status", length = ENUM_SIZE)
	@Index(name = "idx_registration_status")
	private RegistrationStatusType status;

	@NotNull
	@Column(name = "status_date")
	private Date statusDate;

	@ManyToOne(optional = true)
	@JoinColumn(name = "approver_id")
	@ForeignKey(name = "fk_registration_approver")
	@Index(name = "idx_registration_approver")
	private User approver;

	@Column(name = "payment_code")
	private String paymentCode;

	@Column(name = "payment_transaction")
	private String paymentTransaction;

	@Transient
	private List<TeamFormation> teamFormations;

	public Registration() {
	}

	public Registration(Long registrationId, RegistrationStatusType registrationStatus, String reistrationTeamName,
			Integer raceId, String raceName, Date raceDate, Integer cityId, String cityName, String stateAbbreviation) {
		setId(registrationId);
		setStatus(registrationStatus);
		setTeamName(reistrationTeamName);
		setRaceCategory(new RaceCategory());
		getRaceCategory().setRace(new Race());
		getRaceCategory().getRace().setId(raceId);
		getRaceCategory().getRace().setName(raceName);
		getRaceCategory().getRace().setDate(raceDate);

		getRaceCategory().getRace().setCity(new City());
		getRaceCategory().getRace().getCity().setId(cityId);
		getRaceCategory().getRace().getCity().setName(cityName);
		getRaceCategory().getRace().getCity().setState(new State());
		getRaceCategory().getRace().getCity().getState().setAbbreviation(stateAbbreviation);
	}

	public Registration(Long registrationId, Date registrationDate, String teamName, String paymentCode,
			String paymentTransaction, Integer submitterId, String submitterEmail, String submitterName,
			RegistrationStatusType registrationStatus, Integer raceId, String raceName, Date raceDate,
			PaymentType racePaymentType, String racePaymentInfo, String racePaymentAccount, String racePaymentToken,
			Integer periodId, BigDecimal periodPrice, Integer cityId, String cityName, Integer stateId,
			String stateName, String stateAbbreviation, Integer categoryId, String categoryName, Integer courseId,
			String courseName, Date registrationPeriodBeginning, Date registrationPeriodEnd) {
		setId(registrationId);
		setDate(registrationDate);
		setTeamName(teamName);
		setStatus(registrationStatus);
		setPaymentCode(paymentCode);
		setPaymentTransaction(paymentTransaction);

		User submitter = new User();
		setSubmitter(submitter);
		getSubmitter().setId(submitterId);
		getSubmitter().setEmail(submitterEmail);
		getSubmitter().setProfile(new Profile());
		getSubmitter().getProfile().setName(submitterName);

		setRaceCategory(new RaceCategory());
		getRaceCategory().setRace(new Race());
		getRaceCategory().getRace().setId(raceId);
		getRaceCategory().getRace().setName(raceName);
		getRaceCategory().getRace().setDate(raceDate);
		getRaceCategory().getRace().setPaymentType(racePaymentType);
		getRaceCategory().getRace().setPaymentInfo(racePaymentInfo);
		getRaceCategory().getRace().setPaymentAccount(racePaymentAccount);
		getRaceCategory().getRace().setPaymentToken(racePaymentToken);

		getRaceCategory().getRace().setCity(new City());
		getRaceCategory().getRace().getCity().setId(cityId);
		getRaceCategory().getRace().getCity().setName(cityName);
		getRaceCategory().getRace().getCity().setState(new State());
		getRaceCategory().getRace().getCity().getState().setId(stateId);
		getRaceCategory().getRace().getCity().getState().setName(stateName);
		getRaceCategory().getRace().getCity().getState().setAbbreviation(stateAbbreviation);

		getRaceCategory().getRace().setRegistrationPeriod(new Period());
		getRaceCategory().getRace().getRegistrationPeriod().setBeginning(registrationPeriodBeginning);
		getRaceCategory().getRace().getRegistrationPeriod().setEnd(registrationPeriodEnd);

		setPeriod(new Period());
		getPeriod().setId(periodId);
		getPeriod().setPrice(periodPrice);

		getRaceCategory().setCourse(new Course());
		getRaceCategory().getCourse().setId(courseId);
		getRaceCategory().getCourse().setName(courseName);

		getRaceCategory().setCategory(new Category());
		getRaceCategory().getCategory().setId(categoryId);
		getRaceCategory().getCategory().setName(categoryName);
	}

	public String getFormattedId() {
		return this.getId() != null ? Strings.insertZeros(this.getId().toString(), 4) : null;
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

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
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

	public RegistrationStatusType getStatus() {
		return status;
	}

	public void setStatus(RegistrationStatusType status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public User getApprover() {
		return approver;
	}

	public void setApprover(User approver) {
		this.approver = approver;
	}

	public String getPaymentTransaction() {
		return paymentTransaction;
	}

	public void setPaymentTransaction(String paymentTransaction) {
		this.paymentTransaction = paymentTransaction;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public List<TeamFormation> getTeamFormations() {
		return teamFormations;
	}

	public void setTeamFormations(List<TeamFormation> teamFormations) {
		this.teamFormations = teamFormations;
	}
}
