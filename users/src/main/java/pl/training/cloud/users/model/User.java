package pl.training.cloud.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class User implements UserDetails {

    @GeneratedValue
    @Id
    private Long id;
    @NonNull
    private String login;
    @NonNull
    private String password;
    private String firstName;
    private String lastName;
    private Long departmentId;
    @ManyToMany(fetch = FetchType.EAGER, cascade =  CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();
    private boolean active;
    @Transient
    private String departmentName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
        return active;
    }

}