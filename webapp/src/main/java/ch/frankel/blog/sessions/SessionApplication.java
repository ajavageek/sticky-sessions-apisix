package ch.frankel.blog.sessions;

import com.hazelcast.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.MapSession;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.HazelcastSessionSerializer;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;
import org.springframework.web.context.annotation.SessionScope;

@SpringBootApplication
@EnableHazelcastHttpSession
public class SessionApplication {

    @Bean
    @SessionScope
    public Counter counter() {
        return new Counter();
    }

    @Bean
    public UserDetailsManager userDetailsManager(UserRepository repo) {
        return new InMemoryDemoUserManager(repo);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(UserDetailsService service, HttpSecurity http) throws Exception {
        return http.userDetailsService(service)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                                    .anyRequest().authenticated()
                    ).formLogin(form -> form
                                .permitAll()
                                .defaultSuccessUrl("/")
                    ).build();
    }

    @Bean
    public Config hazelcastConfig() {
        var config = new Config();
        var networkConfig = config.getNetworkConfig();
        networkConfig.setPort(0);
        networkConfig.getJoin().getAutoDetectionConfig().setEnabled(true);
        var attributeConfig = new AttributeConfig()
                .setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                .setExtractorClassName(PrincipalNameExtractor.class.getName());
        config.getMapConfig(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)
                .addAttributeConfig(attributeConfig)
                .addIndexConfig(new IndexConfig(IndexType.HASH, HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE));
        var serializerConfig = new SerializerConfig();
        serializerConfig.setImplementation(new HazelcastSessionSerializer()).setTypeClass(MapSession.class);
        config.getSerializationConfig().addSerializerConfig(serializerConfig);
        return config;
    }

    public static void main(String[] args) {
        SpringApplication.run(SessionApplication.class, args);
    }
}
