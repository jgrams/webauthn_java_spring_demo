package com.webauthn.app.data.repository;

import java.util.List;
import java.util.Optional;

import com.webauthn.app.data.objects.Authenticator;
import com.webauthn.app.data.objects.User;
import com.yubico.webauthn.data.ByteArray;

import org.springframework.data.repository.CrudRepository;

public interface AuthenticatorRepository extends CrudRepository<Long, Authenticator> {
    Optional<Authenticator> findByCredentialId(ByteArray credentialId);
    List<Authenticator> findAllByUser (User user);
    List<Authenticator> findAllByCredentialId(ByteArray credentialId);
}
