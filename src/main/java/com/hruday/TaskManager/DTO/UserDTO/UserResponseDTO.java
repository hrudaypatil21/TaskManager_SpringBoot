package com.hruday.TaskManager.DTO.UserDTO;

import com.hruday.TaskManager.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements UserDetails {

    private String empId;
    private String email;
    private String name;
    private Set<User.Role> roles;

    public UserResponseDTO(User user) {
        this.empId = user.getEmpId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.roles = user.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null; // or the actual password if needed
    }

    @Override
    public String getUsername() {
        return empId;
    }
}
