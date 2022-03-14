package com.webauthn.app.data.objects;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.AttestedCredentialData;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ByteArray;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Lob
    @Column(nullable = false)
    private byte[] credentialId;

    @Lob
    @Column(nullable = false)
    private byte[] publicKey;

    @Column(nullable = false)
    private Long count;

    @Lob
    @Column(nullable = true)
    private byte[] aaguid;

    @ManyToOne
    private AppUser user;

    public Credential(RegistrationResult result, AuthenticatorAttestationResponse response, AppUser user, String name) {
        Optional<AttestedCredentialData> attestationData = response.getAttestation().getAuthenticatorData().getAttestedCredentialData();
        this.credentialId = result.getKeyId().getId().getBytes();
        this.publicKey = result.getPublicKeyCose().getBytes();
        this.aaguid = attestationData.get().getAaguid().getBytes();
        this.count = result.getSignatureCount();
        this.name = name;
        this.user = user;
    }


    public ByteArray getByteArrayPublicKey() {
        return new ByteArray(this.getPublicKey());
    }

    public ByteArray getByteArrayCredentialId() {
        return new ByteArray(this.getCredentialId());
    }

    public ByteArray getByteArrayAaguide() {
        return new ByteArray(this.getAaguid());
    }
}
