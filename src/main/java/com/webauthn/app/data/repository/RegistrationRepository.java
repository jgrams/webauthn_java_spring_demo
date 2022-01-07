package com.webauthn.app.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.webauthn.app.data.objects.Authenticator;
import com.webauthn.app.data.objects.User;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Getter;

@Repository
@Getter
public class RegistrationRepository implements CredentialRepository {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AuthenticatorRepository authRepo;

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        User user = userRepo.findByUsername(username);
        List<Authenticator> auth = authRepo.findAllByUser(user);
        return auth.stream()
        .map(
            authenticator ->
                PublicKeyCredentialDescriptor.builder()
                    .id(authenticator.getCredentialId())
                    .build())
        .collect(Collectors.toSet());
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        User user = userRepo.findByUsername(username);
        return Optional.of(user.getHandle());
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        User user = userRepo.findByHandle(userHandle);
        return Optional.of(user.getUsername());
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        Optional<Authenticator> auth = authRepo.findByCredentialId(credentialId);
        return auth.map(
            authenticator ->
                RegisteredCredential.builder()
                    .credentialId(authenticator.getCredentialId())
                    .userHandle(authenticator.getUser().getHandle())
                    .publicKeyCose(authenticator.getPublicKey())
                    .signatureCount(authenticator.getCount())
                    .build()
        );
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        List<Authenticator> auth = authRepo.findAllByCredentialId(credentialId);
        return auth.stream()
        .map(
            authenticator ->
                RegisteredCredential.builder()
                .credentialId(authenticator.getCredentialId())
                .userHandle(authenticator.getUser().getHandle())
                .publicKeyCose(authenticator.getPublicKey())
                .signatureCount(authenticator.getCount())
                .build())
        .collect(Collectors.toSet());
    }
}