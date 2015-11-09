package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;

public class LayoutData {

	@JsonProperty("text_color")
	public String textColor;

	@JsonProperty("background_color")
	public String backgroundColor;

	@JsonProperty("button_color")
	public String buttonColor;
}
