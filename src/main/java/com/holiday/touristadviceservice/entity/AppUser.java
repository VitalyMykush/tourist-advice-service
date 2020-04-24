package com.holiday.touristadviceservice.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 36, nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String encrytedPassword;

    public AppUser(String username, String encrytedPassword) {
        this.username = username;
        this.encrytedPassword = encrytedPassword;
    }
}
