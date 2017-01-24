package temp.security;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class ActivationSession {

    private String token;

    public boolean isEmpty() {
        return getToken() == null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
