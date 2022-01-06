package com.webauthn.app.data.repository;

import com.webauthn.app.data.objects.User;
import com.yubico.webauthn.data.ByteArray;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Long, User> {
    User findByUsername(String username);
    User findByUserhandle(ByteArray userHandle);
}
