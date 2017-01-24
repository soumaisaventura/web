package rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id", "internalId", "status", "name", "description", "distance", "period", "event", "sport",
        "championships", "categories", "kits", "currentPrice", "prices", "modalities"})
public class RaceData {

    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String status;

    public String name;

    public String description;

    public Integer distance;

    public PeriodData period;

    public EventData event;

    public SportData sport;

    public List<ChampionshipData> championships;

    public List<CategoryData> categories;

    public List<KitData> kits;

    @JsonProperty("current_price")
    public PeriodData currentPrice;

    public List<PeriodData> prices;

    public List<ModalityData> modalities;
}
