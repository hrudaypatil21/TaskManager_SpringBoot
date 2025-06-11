package com.hruday.TaskManager.Entity;

import com.hruday.TaskManager.Password.PasswordUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {  // Implement UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "emp_id", nullable = false, unique = true)
    private String empId;

    @Column(name = "emp_email", nullable = false, unique = true)
    private String email;

    @Column(name = "emp_name", nullable = false)
    private String name;

    @Column(name = "user_password", nullable = false)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }

    // UserDetails methods implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.empId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
//    @Enumerated(EnumType.STRING)
//    @Column(name = "roles", nullable = false)
//    private Role role = Role.USER; // Default role is USER
//


//    public void setPassword(String password) {
//        this.password = PasswordUtil.hashPassword(password);
//    }
//
//    public boolean verifyPassword(String password) {
//        return password.isEmpty() || PasswordUtil.verifyPassword(password, this.password);
//    }
