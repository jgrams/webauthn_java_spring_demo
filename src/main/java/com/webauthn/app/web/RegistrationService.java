package com.webauthn.app.web;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.webauthn.app.credential.Credential;
import com.webauthn.app.user.AppUser;
import com.webauthn.app.user.UserRepository;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.Getter;

@Repository
@Getter
public class RegistrationService implements CredentialRepository {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CredentialRepository credentialRepo;

@Override
public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
    AppUser user = userRepo.findByUsername(username);
    List<Credential> auth = credentialRepo.findAllByUser(user);
    return auth.stream()
    .map(
        authenticator ->
            PublicKeyCredentialDescriptor.builder()
                .id(new ByteArray(authenticator.getCredentialId()))
                .build())
    .collect(Collectors.toSet());
}

@Override
public Optional<ByteArray> getUserHandleForUsername(String username) {
    AppUser user = userRepo.findByUsername(username);
    return Optional.of(user.getByteArrayHandle());
}

@Override
public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
    AppUser user = userRepo.findByHandle(userHandle.getBytes());
    return Optional.of(user.getUsername());
}

@Override
public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
    Optional<Credential> auth = credentialRepo.findByCredentialId(credentialId.getBytes());
    return auth.map(
        authenticator ->
            RegisteredCredential.builder()
                .credentialId(authenticator.getByteArrayPublicKey())
                .userHandle(authenticator.getUser().getByteArrayHandle())
                .publicKeyCose(authenticator.getByteArrayPublicKey())
                .signatureCount(authenticator.getCount())
                .build()
    );
}

@Override
public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
    List<Credential> auth = credentialRepo.findAllByCredentialId(credentialId.getBytes());
    return auth.stream()
    .map(
        authenticator ->
            RegisteredCredential.builder()
                .credentialId(authenticator.getByteArrayCredentialId())
                .userHandle(authenticator.getUser().getByteArrayHandle())
                .publicKeyCose(authenticator.getByteArrayPublicKey())
                .signatureCount(authenticator.getCount())
                .build())
    .collect(Collectors.toSet());
}
}