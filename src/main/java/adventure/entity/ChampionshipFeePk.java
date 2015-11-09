package adventure.entity;

import java.io.Serializable;

public class ChampionshipFeePk implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer championship;

	Integer fee;

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
		if (!(obj instanceof ChampionshipFeePk)) {
			return false;
		}
		ChampionshipFeePk other = (ChampionshipFeePk) obj;
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
}
