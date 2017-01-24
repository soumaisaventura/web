package core.entity;

import javax.persistence.*;

@Entity
@Table(name = "hotspot")
public class Hotspot {

    @Id
    private Integer id;

    private String name;

    private String description;

    @Embedded
    private Coord coord;

    @Column(name = "sequence")
    private Integer order;

    private Boolean main;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Hotspot() {
    }

    public Hotspot(Integer id, String name, String description, Coord coord, Integer order, Boolean main,
                   Integer eventId) {
        setId(id);
        setName(name);
        setDescription(description);
        setCoord(coord);
        setOrder(order);
        setMain(main);
        setEvent(new Event());
        getEvent().setId(eventId);
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
        if (!(obj instanceof Hotspot)) {
            return false;
        }
        Hotspot other = (Hotspot) obj;
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

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
