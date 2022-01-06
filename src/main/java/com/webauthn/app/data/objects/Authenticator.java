package com.webauthn.app.data.objects;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;

import lombok.Builder;
import lombok.Value;

@Entity
@Value
@Builder
public class Authenticator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private ByteArray credentialId;

    @Lob
    @Column(nullable = false)
    private ByteArray publicKey;

    @Column(nullable = false)
    private Long count;

    @Lob
    @Column(nullable = false)
    private ByteArray aaguid;

    @ManyToOne
    private User user;

    public Optional<RegisteredCredential> map() {
        return null;
    }
}
