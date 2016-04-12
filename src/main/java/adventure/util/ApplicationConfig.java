package adventure.util;

import br.gov.frameworkdemoiselle.annotation.Name;
import br.gov.frameworkdemoiselle.configuration.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

@Configuration(resource = "application")
public class ApplicationConfig {

    @NotEmpty
    @Name("app.title")
    private String appTitle = "Adventure";

    @NotEmpty
    @Name("mail.smtp.host")
    private String mailSmtpHost;

    @Name("mail.smtp.port")
    private Integer mailSmtpPort;

    @NotEmpty
    @Name("mail.smtp.user")
    private String mailSmtpUser;

    @NotEmpty
    @Name("mail.smtp.password")
    private String mailSmtpPassword;

    @Name("mail.smtp.starttls.enable")
    private Boolean mailSmtpTls;

    @NotEmpty
    @Name("oauth.facebook.id")
    private String oAuthFacebookId;

    @NotEmpty
    @Name("oauth.facebook.secret")
    private String oAuthFacebookSecret;

    @NotEmpty
    @Name("oauth.google.id")
    private String oAuthGoogleId;

    @NotEmpty
    @Name("oauth.google.secret")
    private String oAuthGoogleSecret;

    @NotEmpty
    @Name("analytics.google.id")
    private String analyticsGoogleId;

    public String getAppTitle() {
        return appTitle;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public Integer getMailSmtpPort() {
        return mailSmtpPort;
    }

    public String getMailSmtpUser() {
        return mailSmtpUser;
    }

    public String getMailSmtpPassword() {
        return mailSmtpPassword;
    }

    public Boolean getMailSmtpTls() {
        return mailSmtpTls;
    }

    public String getOAuthFacebookId() {
        return oAuthFacebookId;
    }

    public String getOAuthFacebookSecret() {
        return oAuthFacebookSecret;
    }

    public String getOAuthGoogleId() {
        return oAuthGoogleId;
    }

    public String getOAuthGoogleSecret() {
        return oAuthGoogleSecret;
    }

    public String getAnalyticsGoogleId() {
        return analyticsGoogleId;
    }
}
