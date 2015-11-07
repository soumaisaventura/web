package adventure.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(ChampionshipFeePk.class)
@Table(name = "championship_fee")
public class ChampionshipFee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "championship_id")
	private Championship championship;

	@Id
	@ManyToOne
	@JoinColumn(name = "fee_id")
	private Fee fee;

	public ChampionshipFee() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fee == null) ? 0 : fee.hashCode());
		result = prime * result + ((championship == null) ? 0 : championship.hashCode());
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
		if (!(obj instanceof ChampionshipFee)) {
			return false;
		}
		ChampionshipFee other = (ChampionshipFee) obj;
		if (fee == null) {
			if (other.fee != null) {
				return false;
			}
		} else if (!fee.equals(other.fee)) {
			return false;
		}
		if (championship == null) {
			if (other.championship != null) {
				return false;
			}
		} else if (!championship.equals(other.championship)) {
			return false;
		}
		return true;
	}

	public Championship getChampionship() {
		return championship;
	}

	public void setChampionship(Championship championship) {
		this.championship = championship;
	}

	public Fee getFee() {
		return fee;
	}

	public void setFee(Fee fee) {
		this.fee = fee;
	}
}
