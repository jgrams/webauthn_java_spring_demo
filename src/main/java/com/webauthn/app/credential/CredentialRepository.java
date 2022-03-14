package com.webauthn.app.credential;

import java.util.List;
import java.util.Optional;

import com.webauthn.app.user.AppUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends CrudRepository<Credential, Long> {
    Optional<Credential> findByCredentialId(byte[] credentialId);
    List<Credential> findAllByUser (AppUser user);
    List<Credential> findAllByCredentialId(byte[] credentialId);
}
