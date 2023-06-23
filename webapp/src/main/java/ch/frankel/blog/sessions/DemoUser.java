package ch.frankel.blog.sessions;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

public class DemoUser extends User {

    private final String label;

    public DemoUser(String username, String password, String label) {
        super(username, password, new ArrayList<>());
        this.label = label;
    }

    @SuppressWarnings("Unused")
    public String getLabel() {
        return label;
    }
}