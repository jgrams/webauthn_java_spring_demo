package com.webauthn.app.authenticator;

import java.util.List;
import java.util.Optional;

import com.webauthn.app.user.AppUser;
import com.yubico.webauthn.data.ByteArray;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatorRepository extends CrudRepository<Authenticator, Long> {
    Optional<Authenticator> findByCredentialId(ByteArray credentialId);
    List<Authenticator> findAllByUser (AppUser user);
    List<Authenticator> findAllByCredentialId(ByteArray credentialId);
}
