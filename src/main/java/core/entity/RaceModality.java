package core.entity;

import javax.persistence.*;

@Entity
@IdClass(RaceModalityPk.class)
@Table(name = "race_modality")
public class RaceModality {

    @Id
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Id
    @ManyToOne
    @JoinColumn(name = "modality_id")
    private Modality modality;

    public RaceModality() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((modality == null) ? 0 : modality.hashCode());
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
        if (!(obj instanceof RaceModality)) {
            return false;
        }
        RaceModality other = (RaceModality) obj;
        if (modality == null) {
            if (other.modality != null) {
                return false;
            }
        } else if (!modality.equals(other.modality)) {
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

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }
}
