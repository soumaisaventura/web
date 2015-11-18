package adventure.rest.data;

import java.math.BigDecimal;
import java.util.List;

import adventure.entity.RaceStatusType;

public class RaceData {

	public String id;

	public String name;

	public String description;

	public SportData sport;

	public List<ChampionshipData> championships;

	public PeriodData period;

	public List<CategoryData> categories;

	public BigDecimal currentPrice;

	public List<PeriodData> prices;

	public List<ModalityData> modalities;

	public RaceStatusType status;
}
