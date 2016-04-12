package adventure.entity;

import java.io.Serializable;

public class ChampionshipRacePk implements Serializable {

    private static final long serialVersionUID = 1L;

    Integer championship;

    Integer race;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((race == null) ? 0 : race.hashCode());
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
        if (!(obj instanceof ChampionshipRacePk)) {
            return false;
        }
        ChampionshipRacePk other = (ChampionshipRacePk) obj;
        if (race == null) {
            if (other.race != null) {
                return false;
            }
        } else if (!race.equals(other.race)) {
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
