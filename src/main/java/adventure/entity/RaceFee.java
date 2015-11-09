package adventure.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(RaceFeePk.class)
@Table(name = "race_fee")
public class RaceFee {

	@Id
	@ManyToOne
	@JoinColumn(name = "race_id")
	private Race race;

	@Id
	@ManyToOne
	@JoinColumn(name = "fee_id")
	private Fee fee;

	public RaceFee() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fee == null) ? 0 : fee.hashCode());
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
		if (!(obj instanceof RaceFee)) {
			return false;
		}
		RaceFee other = (RaceFee) obj;
		if (fee == null) {
			if (other.fee != null) {
				return false;
			}
		} else if (!fee.equals(other.fee)) {
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

	public Fee getModality() {
		return fee;
	}

	public void setModality(Fee fee) {
		this.fee = fee;
	}
}
