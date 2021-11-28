package hu.bme.itsec.simnyi.backend.security;

import hu.bme.itsec.simnyi.backend.model.Role;
import hu.bme.itsec.simnyi.backend.repository.UserRepository;
import hu.bme.itsec.simnyi.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger("TokenSecurity");

    private final UserRepository userRepository;
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${springdoc.api-docs.path}")
    private String restApiDocPath;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;
    @Value("${server.servlet.context-path}")
    private String apiPath;
    @Value("${security.enabled}")
    private boolean securityEnabled;
    @Value("#{'${security.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    public SecurityConfig(UserRepository userRepository, JwtTokenFilter jwtTokenFilter) {
        this.userRepository = userRepository;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((username) ->
                userRepository.findUserByUsername(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(String.format("User: %s, not found", username)))
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            logger.error("Unauthorized request - {}", ex.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                        }
                )
                .and();

        List<String> enabledEndpoints = List.of(
                String.format("%s/**", restApiDocPath),
                String.format("%s/**", swaggerPath),
                "/public/**"
        );

//      Set permissions on endpoints
        if (securityEnabled) {
            http.authorizeRequests()
                    .antMatchers(enabledEndpoints.toArray(String[]::new)).permitAll()
                    .antMatchers("/admin/delete/**").hasAuthority(Role.ADMIN.getAuthority())
                    .antMatchers("/admin/register").hasAuthority(Role.ADMIN.getAuthority())
                    // Our private endpoints
                    .anyRequest().authenticated();
        } else {
            http.authorizeRequests().anyRequest().permitAll();
        }

        // Add JWT token filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addExposedHeader("*");
        config.addExposedHeader("Authorization");
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(allowedOrigins);
        config.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        config.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Expose authentication manager bean
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
