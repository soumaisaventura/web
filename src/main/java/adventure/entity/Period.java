package adventure.entity;

import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@IdClass(PeriodPk.class)
@Table(name = "PERIOD")
public class Period implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_PERIOD_RACE")
	@Index(name = "IDX_PERIOD_RACE")
	private Race race;

	@Id
	@NotNull
	@Temporal(DATE)
	@Column(name = "BEGINNING")
	@Index(name = "IDX_PERIOD_BEGINNING")
	private Date beginning;

	@NotNull
	@Temporal(DATE)
	@Column(name = "ENDING")
	@Index(name = "IDX_PERIOD_ENDING")
	private Date end;

	@NotNull
	@Column(name = "PRICE", precision = 5, scale = 2)
	private BigDecimal price;

	public Period() {
	}

	public Period(Race race) {
		this.race = race;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginning == null) ? 0 : beginning.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
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
		if (!(obj instanceof Period)) {
			return false;
		}
		Period other = (Period) obj;
		if (beginning == null) {
			if (other.beginning != null) {
				return false;
			}
		} else if (!beginning.equals(other.beginning)) {
			return false;
		}
		if (race == null) {
			if (other.race != null) {
				return false;
			}
		} else if (!race.equals(other.race)) {
			return false;
		}
		return true;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
