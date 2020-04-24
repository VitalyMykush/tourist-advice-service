package com.holiday.touristadviceservice.security;

import com.holiday.touristadviceservice.entity.AppUser;
import com.holiday.touristadviceservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(
                        ()->new UsernameNotFoundException("User " + username + " was not found in the database"));

        return new User(appUser.getUsername(), appUser.getEncrytedPassword(), new ArrayList<>());
    }
}
