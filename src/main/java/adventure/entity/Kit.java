package adventure.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "kit")
public class Kit {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    private String slug;

    private String name;

    private String description;

    @Column(name = "price")
    private BigDecimal price;

    public Kit() {
    }

    public Kit(Integer id, String slug, String name, String description, BigDecimal price, Integer raceId) {
        setId(id);
        setSlug(slug);
        setName(name);
        setDescription(description);
        setPrice(price);

        setRace(new Race());
        getRace().setId(raceId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kit kit = (Kit) o;

        return id.equals(kit.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
