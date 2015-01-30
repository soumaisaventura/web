package adventure.util;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class HtmlConfig {

	@Inject
	private ApplicationConfig applicationConfig;

	public String getAnalyticsGoogleId() {
		return applicationConfig.getAnalyticsGoogleId();
	}
}
