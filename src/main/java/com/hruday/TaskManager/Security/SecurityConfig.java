package com.hruday.TaskManager.Security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserSecurity userSecurity;

    @Autowired
    private TaskSecurity taskSecurity;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Will use empId
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable for now; enable in prod with token in form
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/html/**", "/reset-password", "/password-reset-mail", "/change-password").permitAll()
                        .requestMatchers("/admin-dashboard/**", "/register").hasRole("ADMIN")
                        .requestMatchers("/dashboard/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/user/{empId}").access(authorizeUserAccess())
                        .requestMatchers(HttpMethod.POST, "/api/tasks/create", "/api/auth/register", "/password-reset-mail").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/update/{taskId}").access(authorizeTaskAccess())
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/delete/{taskId}").access(authorizeTaskAccess())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler )
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login?session=invalid")
                        .maximumSessions(1)
                )
                .authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> authorizeUserAccess() {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();
            String empId = extractPathVariable(request, "/api/tasks/user/{empId}", "empId");
            boolean allowed = userSecurity.checkUserId(authentication.get(), empId);
            return new AuthorizationDecision(allowed);
        };
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> authorizeTaskAccess() {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();
            String taskIdStr = extractPathVariable(request, "/api/tasks/update/{taskId}", "taskId");
            boolean allowed = false;
            try {
                Long taskId = Long.parseLong(taskIdStr);
                allowed = taskSecurity.isOwnerOrAdmin(authentication.get(), taskId);
            } catch (NumberFormatException ignored) {}
            return new AuthorizationDecision(allowed);
        };
    }

    private String extractPathVariable(HttpServletRequest request, String pattern, String variableName) {
        String uri = request.getRequestURI(); // e.g., /api/tasks/user/EMP123
        String[] uriParts = uri.split("/");
        String[] patternParts = pattern.split("/");
        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].equals("{" + variableName + "}")) {
                if (i < uriParts.length) {
                    return uriParts[i];
                }
            }
        }
        return null;
    }
}
