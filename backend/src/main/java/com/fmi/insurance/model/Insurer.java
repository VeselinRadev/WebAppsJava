package com.fmi.insurance.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insurer")
@Builder
public class Insurer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(nullable = false)
    @NotNull
    private String password;

    @Column(nullable = true)
    @NotNull
    private String phoneNumber;

    @Column
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "insurer")
    private Set<Insurance> insurances;

    void addInsurance(Insurance insurance) {
        insurances.add(insurance);
        insurance.setInsurer(this);
    }

    void removeInsurance(Insurance insurance) {
        insurances.remove(insurance);
        insurance.setInsurer(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ADMIN");
        return Collections.singletonList(authority);
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
