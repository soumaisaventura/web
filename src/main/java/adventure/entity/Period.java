package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "period", uniqueConstraints = {
		@UniqueConstraint(name = "uk_period_beginning", columnNames = { "race_id", "beginning" }),
		@UniqueConstraint(name = "uk_period_ending", columnNames = { "race_id", "ending" }) })
public class Period implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_period")
	@SequenceGenerator(name = "seq_period", sequenceName = "seq_period", allocationSize = 1)
	private Integer id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "race_id")
	@ForeignKey(name = "fk_period_race")
	@Index(name = "idx_period_race")
	private Race race;

	@NotNull
	@Temporal(DATE)
	@Column(name = "beginning")
	@Index(name = "idx_period_beginning")
	private Date beginning;

	@NotNull
	@Temporal(DATE)
	@Column(name = "ending")
	@Index(name = "idx_period_ending")
	private Date end;

	@NotNull
	@Column(name = "price", precision = 7, scale = 2)
	private BigDecimal price;

	public Period() {
	}

	public Period(Date beginning, Date end, BigDecimal price) {
		setBeginning(beginning);
		setEnd(end);
		setPrice(price);
	}

	// TODO: OLD

	public Period(Race race) {
		this.race = race;
	}

	public Period(Date beginning, Date end) {
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
		if (!(obj instanceof Period)) {
			return false;
		}
		Period other = (Period) obj;
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
