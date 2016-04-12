package adventure.entity;

import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

public class EventRegistrationStatusByDayPk implements Serializable {

    private static final long serialVersionUID = 1L;

    Integer event;

    @Temporal(DATE)
    Date date;

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
        if (!(obj instanceof EventRegistrationStatusByDayPk)) {
            return false;
        }
        EventRegistrationStatusByDayPk other = (EventRegistrationStatusByDayPk) obj;
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
}
