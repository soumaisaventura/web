package adventure.entity;

import br.gov.frameworkdemoiselle.util.Strings;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "seq_registration")
    @SequenceGenerator(name = "seq_registration", sequenceName = "seq_registration", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "race_id", referencedColumnName = "race_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id")})
    private RaceCategory raceCategory;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private RegistrationPeriod period;

    @Column(name = "team_name")
    private String teamName;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "submitter_id")
    private User submitter;

    @Enumerated(STRING)
    private RegistrationStatusType status;

    @Column(name = "status_date")
    private Date statusDate;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @Embedded
    private RegistrationPayment payment;

    @Transient
    private List<UserRegistration> userRegistrations;

    public Registration() {
    }

    public Registration(Long id, Integer eventId, String eventName, String eventAlias, Date eventBeginning, Date eventEnd) {
        setId(id);

        setRaceCategory(new RaceCategory());
        getRaceCategory().setRace(new Race());
        getRaceCategory().getRace().setEvent(new Event());
        getRaceCategory().getRace().getEvent().setId(eventId);
        getRaceCategory().getRace().getEvent().setName(eventName);
        getRaceCategory().getRace().getEvent().setAlias(eventAlias);
        getRaceCategory().getRace().getEvent().setBeginning(eventBeginning);
        getRaceCategory().getRace().getEvent().setEnd(eventEnd);
    }

    public Registration(Long registrationId, RegistrationStatusType registrationStatus, String reistrationTeamName,
                        Integer raceId, String raceName, String raceDescription, String raceAlias, Date racePeriodBeginning,
                        Date racePeriodEnd, Integer eventId, String eventName, String eventAlias, Integer cityId, String cityName,
                        String stateAbbreviation) {

        setId(registrationId);
        setStatus(registrationStatus);
        setTeamName(reistrationTeamName);

        setRaceCategory(new RaceCategory());
        getRaceCategory().setRace(new Race());
        getRaceCategory().getRace().setId(raceId);
        getRaceCategory().getRace().setName(raceName);
        getRaceCategory().getRace().setDescription(raceDescription);
        getRaceCategory().getRace().setAlias(raceAlias);

        getRaceCategory().getRace().setPeriod(new Period());
        getRaceCategory().getRace().getPeriod().setBeginning(racePeriodBeginning);
        getRaceCategory().getRace().getPeriod().setEnd(racePeriodEnd);

        getRaceCategory().getRace().setEvent(new Event());
        getRaceCategory().getRace().getEvent().setId(eventId);
        getRaceCategory().getRace().getEvent().setName(eventName);
        getRaceCategory().getRace().getEvent().setAlias(eventAlias);

        getRaceCategory().getRace().getEvent().setCity(new City());
        getRaceCategory().getRace().getEvent().getCity().setId(cityId);
        getRaceCategory().getRace().getEvent().getCity().setName(cityName);
        getRaceCategory().getRace().getEvent().getCity().setState(new State());
        getRaceCategory().getRace().getEvent().getCity().getState().setAbbreviation(stateAbbreviation);
    }

    public Registration(Long id, Date date, String teamName, RegistrationStatusType status, String paymentCheckoutCode,
                        String paymentTransactionCode, Integer periodId, BigDecimal periodPrice, Date periodBeginning, Date periodEnd, Integer submitterId,
                        String submitterEmail, String submitterName, String submitterMobile, Integer raceId, String raceName,
                        String raceDescription, String raceAlias, Integer raceDistance, Integer raceStatusId, String raceStatusName,
                        Date racePeriodBeginning, Date racePeriodEnd, Integer eventId, String eventName, String eventAlias,
                        EventPaymentType eventPaymentType, String eventPaymentInfo, String eventPaymentAccount,
                        String eventPaymentToken, Integer cityId, String cityName, Integer stateId, String stateName, String stateAbbreviation,
                        Integer categoryId, String categoryAlias, String categoryName, Integer categoryTeamSize) {
        setId(id);
        setDate(date);
        setTeamName(teamName);
        setStatus(status);

        setPayment(new RegistrationPayment());
        getPayment().setCheckoutCode(paymentCheckoutCode);
        getPayment().setTransactionCode(paymentTransactionCode);

        setPeriod(new RegistrationPeriod());
        getPeriod().setId(periodId);
        getPeriod().setPrice(periodPrice);
        getPeriod().setBeginning(periodBeginning);
        getPeriod().setEnd(periodEnd);

        setSubmitter(new User());
        getSubmitter().setId(submitterId);
        getSubmitter().setEmail(submitterEmail);
        getSubmitter().setProfile(new Profile());
        getSubmitter().getProfile().setName(submitterName);
        getSubmitter().getProfile().setMobile(submitterMobile);

        setRaceCategory(new RaceCategory());
        getRaceCategory().setRace(new Race());
        getRaceCategory().getRace().setId(raceId);
        getRaceCategory().getRace().setName(raceName);
        getRaceCategory().getRace().setDescription(raceDescription);
        getRaceCategory().getRace().setAlias(raceAlias);
        getRaceCategory().getRace().setDistance(raceDistance);

        getRaceCategory().getRace().setStatus(new Status());
        getRaceCategory().getRace().getStatus().setId(raceStatusId);
        getRaceCategory().getRace().getStatus().setName(raceStatusName);

        getRaceCategory().getRace().setPeriod(new Period());
        getRaceCategory().getRace().getPeriod().setBeginning(racePeriodBeginning);
        getRaceCategory().getRace().getPeriod().setEnd(racePeriodEnd);

        getRaceCategory().getRace().setEvent(new Event());
        getRaceCategory().getRace().getEvent().setId(eventId);
        getRaceCategory().getRace().getEvent().setName(eventName);
        getRaceCategory().getRace().getEvent().setAlias(eventAlias);

        getRaceCategory().getRace().getEvent().setPayment(new EventPayment());
        getRaceCategory().getRace().getEvent().getPayment().setType(eventPaymentType);
        getRaceCategory().getRace().getEvent().getPayment().setInfo(eventPaymentInfo);
        getRaceCategory().getRace().getEvent().getPayment().setAccount(eventPaymentAccount);
        getRaceCategory().getRace().getEvent().getPayment().setToken(eventPaymentToken);

        // getRaceCategory().getRace().getEvent().setCoords(new Coords());
        // getRaceCategory().getRace().getEvent().getCoords().setLatitude(eventCoordsLatitude);
        // getRaceCategory().getRace().getEvent().getCoords().setLongitude(eventCoordsLongitude);

        getRaceCategory().getRace().getEvent().setCity(new City());
        getRaceCategory().getRace().getEvent().getCity().setId(cityId);
        getRaceCategory().getRace().getEvent().getCity().setName(cityName);
        getRaceCategory().getRace().getEvent().getCity().setState(new State());
        getRaceCategory().getRace().getEvent().getCity().getState().setId(stateId);
        getRaceCategory().getRace().getEvent().getCity().getState().setName(stateName);
        getRaceCategory().getRace().getEvent().getCity().getState().setAbbreviation(stateAbbreviation);

        getRaceCategory().setCategory(new Category());
        getRaceCategory().getCategory().setId(categoryId);
        getRaceCategory().getCategory().setAlias(categoryAlias);
        getRaceCategory().getCategory().setName(categoryName);
        getRaceCategory().getCategory().setTeamSize(categoryTeamSize);
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

    public RegistrationPeriod getPeriod() {
        return period;
    }

    public void setPeriod(RegistrationPeriod period) {
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

    public RegistrationPayment getPayment() {
        return payment;
    }

    public void setPayment(RegistrationPayment payment) {
        this.payment = payment;
    }

    public List<UserRegistration> getUserRegistrations() {
        return userRegistrations;
    }

    public void setUserRegistrations(List<UserRegistration> userRegistrations) {
        this.userRegistrations = userRegistrations;
    }
}
