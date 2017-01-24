package core.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "period")
public class RegistrationPeriod {

    @Id
    private Integer id;

    @Temporal(DATE)
    @Column(name = "beginning")
    private Date beginning;

    @Temporal(DATE)
    @Column(name = "ending")
    private Date end;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Column(name = "price")
    private BigDecimal price;

    public RegistrationPeriod() {
    }

    public RegistrationPeriod(Date beginning, Date end, BigDecimal price) {
        setBeginning(beginning);
        setEnd(end);
        setPrice(price);
    }

    // TODO: OLD

    public RegistrationPeriod(Race race) {
        this.race = race;
    }

    public RegistrationPeriod(Date beginning, Date end) {
        setBeginning(beginning);
        setEnd(end);
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
        if (!(obj instanceof RegistrationPeriod)) {
            return false;
        }
        RegistrationPeriod other = (RegistrationPeriod) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBeginning() {
        return beginning;
    }

    public void setBeginning(Date beginning) {
        this.beginning = beginning;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
