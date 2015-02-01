package adventure.helper;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import adventure.util.ApplicationConfig;

@Named
@RequestScoped
public class ApplicationConfigHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationConfig config;

	public String getOAuthFacebookId() {
		return config.getOAuthFacebookId();
	}

	public String getAppTitle() {
		return config.getAppTitle();
	}

	public String getAnalyticsGoogleId() {
		return config.getAnalyticsGoogleId();
	}
}
