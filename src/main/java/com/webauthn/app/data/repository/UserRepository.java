package com.webauthn.app.data.repository;

import com.webauthn.app.data.objects.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String name);
    User findByHandle(byte[] handle);
}
