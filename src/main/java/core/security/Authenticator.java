package core.security;

import br.gov.frameworkdemoiselle.security.InvalidCredentialsException;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.Profile;
import core.entity.User;
import core.persistence.UserDAO;
import core.util.ApplicationConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import temp.security.PasswordNotDefinedException;
import temp.security.Passwords;
import temp.security.UnconfirmedUserException;

import javax.enterprise.context.RequestScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class Authenticator {

    private User loggedIn;

    public static Authenticator getInstance() {
        return Beans.getReference(Authenticator.class);
    }

    public String authenticate(String username, String password) {
        String token = null;
        User user = UserDAO.getInstance().loadForAuthentication(username);

        if (user != null && doesPasswordMatch(user, password)) {
            setLoggedIn(user);
            token = createToken(user);
        }

        if (token == null) {
            throw new InvalidCredentialsException();
        }

        return token;
    }

    public String authenticate(User user) {
        String token = null;

        if (user != null) {
            user = UserDAO.getInstance().loadForAuthentication(user.getEmail());
            setLoggedIn(user);
            token = createToken(user);
        }

        if (token == null) {
            throw new InvalidCredentialsException();
        }

        return token;
    }

    public void authenticate(String token) {
        try {
            ApplicationConfig config = Beans.getReference(ApplicationConfig.class);

            Claims claims = Jwts.parser()
                    .setSigningKey(config.getJwtSignKey().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            setLoggedIn(parse(claims));

        } catch (JwtException cause) {
            throw new InvalidCredentialsException("Token inválido");
        }
    }

    public User getLoggedIn() {
        return loggedIn;
    }

    private void setLoggedIn(User user) {
        this.loggedIn = user;
    }

    private boolean doesPasswordMatch(User user, String password) {
        boolean result;

        if (user.getPassword() == null) {
            throw new PasswordNotDefinedException();

        } else if (user.getActivation() == null) {
            throw new UnconfirmedUserException();

        } else {
            String hash = Passwords.hash(password, user.getEmail());
            result = user.getPassword().equals(hash);
        }

        return result;
    }

    private String createToken(User user) {
        String token;

        Map<String, Boolean> roles = new HashMap();
        roles.put("admin", user.getAdmin());
        roles.put("organizer", user.getOrganizer());

        Map<String, Integer> pendencies = new HashMap();
        pendencies.put("profile", user.getProfile().getPendencies());
        pendencies.put("health", user.getHealth().getPendencies());

        ApplicationConfig config = Beans.getReference(ApplicationConfig.class);

        token = Jwts.builder()
                .setClaims(parse(user))
                .signWith(SignatureAlgorithm.HS256, config.getJwtSignKey().getBytes()).compact();

        return token;
    }

    private User parse(Claims claims) {
        User result = null;

        if (claims != null) {
            result = new User();
            result.setId(Integer.parseInt(claims.getSubject()));
            result.setAdmin(Boolean.parseBoolean(claims.get("roles", Map.class).get("admin").toString()));
            result.setOrganizer(Boolean.parseBoolean(claims.get("roles", Map.class).get("organizer").toString()));

            result.setProfile(new Profile());
            result.getProfile().setName(claims.get("name", String.class));
        }

        return result;
    }

    private Claims parse(User user) {
        Claims claims = Jwts.claims();

        if (user != null) {
            Map<String, Boolean> roles = new HashMap();
            roles.put("admin", user.getAdmin());
            roles.put("organizer", user.getOrganizer());

            Map<String, Integer> pendencies = new HashMap();
            pendencies.put("profile", user.getProfile().getPendencies());
            pendencies.put("health", user.getHealth().getPendencies());

            claims.setSubject(user.getId().toString());
            claims.setIssuedAt(new Date());
            claims.put("name", user.getProfile().getShortName());
            claims.put("roles", roles);
            claims.put("pendencies", pendencies);
        }

        return claims;
    }
}
