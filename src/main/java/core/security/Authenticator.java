package core.security;

import br.gov.frameworkdemoiselle.security.InvalidCredentialsException;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.User;
import core.persistence.UserDAO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import temp.security.PasswordNotDefinedException;
import temp.security.Passwords;
import temp.security.UnconfirmedUserException;

import javax.enterprise.context.RequestScoped;
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

        if (user != null) {
            if (doesPasswordMatch(user, password)) {
                loggedIn = user;
                token = createToken(user);
            }
        } else {
            throw new InvalidCredentialsException();
        }

        return token;
    }

    public void authenticate(String token) {
        //UserDAO.getInstance().lo
    }

    public User getLoggedIn() {
        return loggedIn;
    }

//    public void setLoggedIn(User user) {
//        loggedIn = user;
//    }

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

//        try {
        Map<String, Boolean> roles = new HashMap();
        roles.put("admin", user.getAdmin());
        roles.put("organizer", user.getOrganizer());

        Map<String, Integer> pendencies = new HashMap();
        pendencies.put("profile", user.getProfile().getPendencies());
        pendencies.put("health", user.getHealth().getPendencies());

        token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("name", user.getProfile().getShortName())
                .claim("roles", roles)
                .claim("pendencies", pendencies)
                .signWith(SignatureAlgorithm.HS256, "secret".getBytes()).compact();

//            token = JWT.create()
//                    .withSubject(user.getId().toString())
//                    .withClaim("name", user.getProfile().getShortName())
//                    .withClaim("admin", user.getAdmin())
//                    .withClaim("organizer", user.getOrganizer())
//                    .withClaim("profile_pendencies", user.getProfile().getPendencies())
//                    .withClaim("health_pendencies", user.getHealth().getPendencies())
//                    .sign(Algorithm.HMAC256("secret"));

//        } catch (UnsupportedEncodingException cause) {
//            throw new AuthenticationException(cause);
//        }

        return token;
    }
}
