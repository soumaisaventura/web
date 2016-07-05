package adventure.business;

import adventure.entity.*;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserDAO;
import adventure.persistence.UserRegistrationDAO;
import adventure.util.ApplicationConfig;
import adventure.util.Dates;
import adventure.util.Misc;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.io.IOUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.*;

import static javax.mail.Message.RecipientType.TO;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

@Stateless
public class MailBusiness implements Serializable {

    private static final long serialVersionUID = 1L;

    public static MailBusiness getInstance() {
        return Beans.getReference(MailBusiness.class);
    }

    @Asynchronous
    public void sendUserActivation(final String email, final URI baseUri) throws Exception {
        beforeAsync();
        User user = UserDAO.getInstance().loadForAuthentication(email);
        String token = UserBusiness.getInstance().updateActivationToken(email);

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);
        context.put("token", token);
        context.put("url", baseUri.resolve("atleta/ativacao?token=" + token).toString());

        String content = parse("mail-templates/account-activation.txt", context);
        send("Confirmação de e-mail", content, "text/plain", email);
    }

    @Asynchronous
    public void sendWelcome(String email, URI baseUri) throws Exception {
        beforeAsync();
        User user = UserDAO.getInstance().loadForAuthentication(email);

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);
        context.put("url1", baseUri.toString().endsWith("/") ? baseUri.toString().substring(0, baseUri.toString().length() - 1)
                : baseUri.toString());
        context.put("url2", baseUri.resolve("atleta/pessoal").toString());

        String content = parse("mail-templates/welcome.txt", context);
        send("Seja bem-vindo!", content, "text/plain", email);
    }

    @Asynchronous
    public void sendPasswordCreationMail(final String email, final URI baseUri) throws Exception {
        beforeAsync();
        User user = UserDAO.getInstance().loadForAuthentication(email);
        String token = UserBusiness.getInstance().updateResetToken(email);

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);
        context.put("url", baseUri.resolve("senha/redefinicao?token=" + token).toString());

        String content = parse("mail-templates/password-creation.txt", context);
        send("Criação de senha", content, "text/plain", email);
    }

    @Asynchronous
    public void sendResetPasswordMail(final String email, final URI baseUri) throws Exception {
        beforeAsync();
        User user = UserDAO.getInstance().loadForAuthentication(email);
        String token = UserBusiness.getInstance().updateResetToken(email);

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);
        context.put("url", baseUri.resolve("senha/redefinicao?token=" + token).toString());

        String content = parse("mail-templates/password-recovery.txt", context);
        send("Recuperação de senha", content, "text/plain", email);
    }

    @Asynchronous
    public void sendAccountRemoval(final String email, final String dupEmail) throws Exception {
        beforeAsync();
        User user = UserDAO.getInstance().loadForAuthentication(email);

        Map<String, Object> context = new HashMap<>();
        context.put("user", user);
        context.put("dupEmail", dupEmail);

        String content = parse("mail-templates/account-removal.txt", context);
        send("Remoção de conta", content, "text/plain", email);
    }

    @Asynchronous
    public void sendRegistrationCreation(Registration registration, URI baseUri) throws Exception {
        beforeAsync();
        List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
        registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
        Race race = registration.getRaceCategory().getRace();

        String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/registration-creation.html"));
        content = clearContent(content);
        content = content.replace("{appName}", "Sou+ Aventura");
        content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
        content = content.replace("{registrationTeamName}", escapeHtml(registration.getTeamName()));
        content = content.replace("{raceName}", escapeHtml(race.getEvent().getName()));
        content = content.replace("{raceCity}", escapeHtml(race.getEvent().getCity().getName()));
        content = content.replace("{raceState}", race.getEvent().getCity().getState().getAbbreviation());
        content = content.replace("{raceDate}", Dates.parse(race.getPeriod().getBeginning()));
        content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
                "$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
        content = content.replace("{registrationId}", registration.getFormattedId());
        content = content.replace("{registrationDate}", Dates.parse(registration.getDate()));
        content = content.replace("{categoryName}", escapeHtml(registration.getRaceCategory().getCategory().getName()));
        content = content.replace("{courseName}", registration.getRaceCategory().getRace().getName());
        content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));

        String replacement = "";
        for (User organizer : UserDAO.getInstance().findOrganizers(race.getEvent())) {
            replacement += "\n$1" + organizer.getProfile().getName() + "; tel: " + organizer.getProfile().getMobile()
                    + "; " + organizer.getEmail() + "$2\r";
        }
        content = content.replaceAll("(<ul.+)\\{organizerInfo\\}(.+ul>)", replacement);

        String subject = "Pedido de inscrição";
        subject += " #" + registration.getFormattedId();
        subject += " – " + race.getEvent().getName();

        for (UserRegistration member : members) {
            send(subject, content, "text/html", member.getUser().getEmail());
        }
    }

    @Asynchronous
    public void sendRegistrationFailed(User member, User submitter, RaceCategory raceCategory, String teamName,
                                       URI baseUri) throws Exception {
        beforeAsync();
        Race race = raceCategory.getRace();
        String pendency = (member.getProfile().getPendencies() > 0 ? "pessoal" : "saude");

        String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/registration-failed.html"));
        content = clearContent(content);
        content = content.replace("{appName}", "Sou+ Aventura");
        content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
        content = content.replace("{name}", escapeHtml(member.getProfile().getName()));
        content = content.replace("{registrationSubmitter}",
                escapeHtml(member.getId().equals(submitter.getId()) ? "você" : submitter.getProfile().getName()));
        content = content.replace("{registrationTeamName}", escapeHtml(teamName));
        content = content.replace("{what}", member.getId().equals(submitter.getId()) ? "se inscrever"
                : "inscrever você");
        content = content.replace("{raceName}", escapeHtml(race.getEvent().getName()));
        content = content.replace("{raceDate}", Dates.parse(race.getPeriod().getBeginning()));
        content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
                "$1" + baseUri.resolve("atleta/" + pendency + "$2"));

        String subject = "Tentativa de inscrição";
        subject += " – " + race.getEvent().getName();

        send(subject, content, "text/html", member.getEmail());
    }

    @Asynchronous
    public void sendRegistrationPeriodChanging(Registration registration, Date newPeriodBegining, Date newPeriodEnd,
                                               URI baseUri) throws Exception {
        beforeAsync();
        List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
        registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
        Race race = registration.getRaceCategory().getRace();

        String content = Strings.parse(Reflections
                .getResourceAsStream("mail-templates/registration-period-changing.html"));
        content = clearContent(content);
        content = content.replace("{appName}", "Sou+ Aventura");
        content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
        content = content.replace("{raceName}", escapeHtml(race.getEvent().getName()));
        content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
                "$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
        content = content.replace("{registrationId}", registration.getFormattedId());
        content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));
        content = content.replace("{newPeriodBegining}", Dates.parse(newPeriodBegining));
        content = content.replace("{newPeriodEnd}", Dates.parse(newPeriodEnd));
        content = content
                .replaceAll("(<ul.+)\\{organizerInfo\\}(.+ul>)", escapeHtml(getOrganizerInfo(race.getEvent())));

        String subject = "Reajuste no pedido";
        subject += " #" + registration.getFormattedId();
        subject += " – " + race.getEvent().getName();

        for (UserRegistration member : members) {
            send(subject, content, "text/html", member.getUser().getEmail());
        }
    }

    @Asynchronous
    public void sendRegistrationCancellation(Registration registration, URI baseUri) throws Exception {
        beforeAsync();
        List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
        registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
        Race race = registration.getRaceCategory().getRace();

        String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/registration-cancelled.html"));
        content = clearContent(content);
        content = content.replace("{appName}", "Sou+ Aventura");
        content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
        content = content.replace("{raceName}", escapeHtml(race.getEvent().getName()));
        content = content.replace("{raceDate}", Dates.parse(race.getPeriod().getBeginning()));
        content = content.replace("{raceCity}", escapeHtml(race.getEvent().getCity().getName()));
        content = content.replace("{raceState}", race.getEvent().getCity().getState().getAbbreviation());
        content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
                "$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
        content = content.replace("{registrationId}", registration.getFormattedId());
        content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));
        content = content
                .replaceAll("(<ul.+)\\{organizerInfo\\}(.+ul>)", escapeHtml(getOrganizerInfo(race.getEvent())));

        String subject = "Cancelamento da inscrição";
        subject += " #" + registration.getFormattedId();
        subject += " – " + race.getEvent().getName();

        for (UserRegistration member : members) {
            send(subject, content, "text/html", member.getUser().getEmail());
        }
    }

    private String getOrganizerInfo(Event event) {
        String replacement = "";
        for (User organizer : UserDAO.getInstance().findOrganizers(event)) {
            replacement += "\n$1" + organizer.getProfile().getName() + "; tel: " + organizer.getProfile().getMobile()
                    + "; " + organizer.getEmail() + "$2\r";
        }

        return replacement;
    }

    @Asynchronous
    public void sendRegistrationConfirmation(Registration registration, URI baseUri) throws Exception {
        beforeAsync();
        // User creator = userDAO.loadBasics(registration.getSubmitter().getEmail());
        List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
        registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
        Race race = registration.getRaceCategory().getRace();

        String content = Strings
                .parse(Reflections.getResourceAsStream("mail-templates/registration-confirmation.html"));
        content = clearContent(content);
        content = content.replace("{appName}", "Sou+ Aventura");
        content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
        content = content.replace("{registrationTeamName}", escapeHtml(registration.getTeamName()));
        content = content.replace("{raceName}", escapeHtml(race.getEvent().getName()));
        content = content.replace("{raceCity}", escapeHtml(race.getEvent().getCity().getName()));
        content = content.replace("{raceState}", race.getEvent().getCity().getState().getAbbreviation());
        content = content.replace("{raceDate}", Dates.parse(race.getPeriod().getBeginning()));
        content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
                "$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
        content = content.replace("{registrationId}", registration.getFormattedId());
        content = content.replace("{categoryName}", escapeHtml(registration.getRaceCategory().getCategory().getName()));
        content = content.replace("{courseName}", registration.getRaceCategory().getRace().getName());
        content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));

        String subject = "Confirmação da inscrição";
        subject += " #" + registration.getFormattedId();
        subject += " – " + race.getEvent().getName();

        for (UserRegistration member : members) {
            send(subject, content, "text/html", member.getUser().getEmail());
        }

        // if (!members.contains(creator)) {
        // send(subject, content, "text/html", creator.getEmail());
        // }
    }

    private String clearContent(String content) {
        return content.replaceAll("<div id=\"campaign\">.+</div>", "");
    }

    private void send(final String subject, final String content, final String type, final String to) throws Exception {
        ApplicationConfig config = getConfig();
        final String PREFIX = config.getAppTitle() + " – ";
        final String SUFIX = "";

        MimeMessage message = new MimeMessage(getSession());
        message.setFrom(new InternetAddress("contato@soumaisaventura.com.br"));
        message.setSubject(PREFIX + subject + SUFIX, "UTF-8");
        message.setRecipients(TO, to);
        message.setContent(content, type);

        Transport.send(message);
    }

    private Session getSession() {
        final ApplicationConfig config = getConfig();
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getMailSmtpHost());
        props.put("mail.smtp.auth", "true");

        if (config.getMailSmtpPort() != null) {
            props.put("mail.smtp.port", config.getMailSmtpPort());
            props.put("mail.smtp.socketFactory.port", config.getMailSmtpPort());
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        if (config.getMailSmtpTls() != null) {
            props.put("mail.smtp.starttls.enable", config.getMailSmtpTls());
        }

        Authenticator authenticator = new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(config.getMailSmtpUser(), config.getMailSmtpPassword());
            }
        };

        return Session.getInstance(props, authenticator);
    }

    private ApplicationConfig getConfig() {
        return Beans.getReference(ApplicationConfig.class);
    }

    private void beforeAsync() throws Exception {
        Thread.sleep(300);
    }

    private String parse(String file, Map<String, Object> context) throws IOException {
        context.put("config", getConfig());

        InputStream inputStream = Reflections.getResourceAsStream(file);
        String source = IOUtils.toString(inputStream, "UTF-8");
        Template template = Mustache.compiler().compile(source);

        return template.execute(context);
    }
}
