package com.webauthn.app.data.repository;

import com.webauthn.app.data.objects.AppUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String name);
    AppUser findByHandle(byte[] handle);
}
