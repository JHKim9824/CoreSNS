package com.jhkim9824.coresns.entity;

import com.jhkim9824.coresns.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "User")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "phone_number", length = 11)
    private String phoneNumber;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birthdate")
    private LocalDateTime birthdate;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "removed_date")
    private LocalDateTime removedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return this.removedDate == null;
    }

    public UserResponseDto toResponseDto() {
        return new UserResponseDto(
                this.id,
                this.email,
                this.nickname,
                this.name,
                this.phoneNumber,
                this.gender,
                this.birthdate,
                this.introduce,
                this.profileImage,
                this.createdDate
        );
    }
}

