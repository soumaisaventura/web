package core.security;

import br.gov.frameworkdemoiselle.security.InvalidCredentialsException;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;
import core.entity.User;
import core.persistence.UserDAO;
import core.util.ApplicationConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import javax.enterprise.context.RequestScoped;

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
        boolean failed = false;

        if (!Strings.isEmpty(token)) {
            try {
                ApplicationConfig config = Beans.getReference(ApplicationConfig.class);

                Claims claims = Jwts.parser()
                        .setSigningKey(config.getJwtSignKey().getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                setLoggedIn(parse(claims));

            } catch (JwtException cause) {
                failed = true;
            }
        } else {
            failed = true;
        }

        if (failed) {
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
        ApplicationConfig config = Beans.getReference(ApplicationConfig.class);
        return Jwts.builder()
                .setClaims(parse(user))
                .signWith(SignatureAlgorithm.HS256, config.getJwtSignKey().getBytes()).compact();
    }

    private User parse(Claims claims) {
        User result = null;

        if (claims != null) {
            Integer id = Integer.parseInt(claims.getSubject());
            result = UserDAO.getInstance().loadForAuthentication(id);
        }

        return result;
    }

    private Claims parse(User user) {
        Claims claims = Jwts.claims();

        if (user != null) {
            DateTime now = new DateTime();
            claims.setIssuedAt(now.toDate());
            claims.setExpiration(now.plusDays(30).toDate());
            claims.setSubject(user.getId().toString());
            claims.setIssuer("soumaisaventura.com.br");
        }

        return claims;
    }
}
