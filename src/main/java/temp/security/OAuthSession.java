package temp.security;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class OAuthSession {

    private boolean active = false;

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }
}
