package com.holiday.touristadviceservice.repository;

import com.holiday.touristadviceservice.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser,Long> {
    Optional<AppUser> findByUsername(String username);
}
