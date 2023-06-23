package ch.frankel.blog.sessions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final PasswordEncoder encoder;

    private final List<DemoUser> users = new ArrayList<>();

    public UserRepository(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @PostConstruct
    private void initialize() {
        users.add(new DemoUser("john", encoder.encode("john"), "John Doe"));
        users.add(new DemoUser("jane", encoder.encode("jane"), "Jane Doe"));
    }

    public List<DemoUser> findAll() {
        return Collections.unmodifiableList(users);
    }
}
