package adventure.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(RaceCategoryPk.class)
@Table(name = "race_category")
public class RaceCategory {

    @Id
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public RaceCategory() {
    }

    public RaceCategory(Integer categoryId, String categoryName, String categoryDescription, Integer categoryTeamSize,
                        Integer categoryMinMaleMembers, Integer categoryMinFemaleMembers, Integer raceId, String raceName,
                        Date racePeriodBeginning, Date racePeriodEnd) {
        setCategory(new Category());
        getCategory().setId(categoryId);
        getCategory().setName(categoryName);
        getCategory().setDescription(categoryDescription);
        getCategory().setTeamSize(categoryTeamSize);
        getCategory().setMinMaleMembers(categoryMinMaleMembers);
        getCategory().setMinFemaleMembers(categoryMinFemaleMembers);

        setRace(new Race());
        getRace().setId(raceId);
        getRace().setName(raceName);
        getRace().setPeriod(new Period());
        getRace().getPeriod().setBeginning(racePeriodBeginning);
        getRace().getPeriod().setEnd(racePeriodEnd);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
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
        if (!(obj instanceof RaceCategory)) {
            return false;
        }
        RaceCategory other = (RaceCategory) obj;
        if (category == null) {
            if (other.category != null) {
                return false;
            }
        } else if (!category.equals(other.category)) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
