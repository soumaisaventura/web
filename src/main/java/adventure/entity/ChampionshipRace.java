package adventure.entity;

import javax.persistence.*;

@Entity
@IdClass(ChampionshipRacePk.class)
@Table(name = "championship_race")
public class ChampionshipRace {

    @Id
    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    @Id
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    public ChampionshipRace() {
    }

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
        if (!(obj instanceof ChampionshipRace)) {
            return false;
        }
        ChampionshipRace other = (ChampionshipRace) obj;
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

    public Championship getChampionship() {
        return championship;
    }

    public void setChampionship(Championship championship) {
        this.championship = championship;
    }

    public Race getModality() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
