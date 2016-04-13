package adventure.entity;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

@Entity
@IdClass(EventRegistrationStatusByDayPk.class)
@Table(name = "registration_status_by_day")
public class EventRegistrationStatusByDay {

    @Id
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Id
    @Temporal(DATE)
    private Date date;

    @Column(name = "pendent")
    private Integer pendentCount;

    @Column(name = "confirmed")
    private Integer confirmedCount;

    @Column(name = "cancelled")
    private Integer cancelledCount;

    public EventRegistrationStatusByDay() {
    }

    public EventRegistrationStatusByDay(Integer eventId, String eventName, Date date, Integer pendentCount,
                                        Integer confirmedCount, Integer cancelledCount) {
        setEvent(new Event());
        getEvent().setId(eventId);
        getEvent().setName(eventName);
        setDate(date);

        setPendentCount(pendentCount);
        setConfirmedCount(confirmedCount);
        setCancelledCount(cancelledCount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((event == null) ? 0 : event.hashCode());
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
        if (!(obj instanceof EventRegistrationStatusByDay)) {
            return false;
        }
        EventRegistrationStatusByDay other = (EventRegistrationStatusByDay) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (event == null) {
            if (other.event != null) {
                return false;
            }
        } else if (!event.equals(other.event)) {
            return false;
        }
        return true;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPendentCount() {
        return pendentCount;
    }

    public void setPendentCount(Integer pendentCount) {
        this.pendentCount = pendentCount;
    }

    public Integer getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(Integer confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    public Integer getCancelledCount() {
        return cancelledCount;
    }

    public void setCancelledCount(Integer cancelledCount) {
        this.cancelledCount = cancelledCount;
    }
}
