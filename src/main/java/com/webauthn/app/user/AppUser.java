package com.webauthn.app.user;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.UserIdentity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String displayname;

    @Lob
    @Column(nullable = false, length = 64)
    private byte[] handle;

    public AppUser(UserIdentity user) {
        this.handle = user.getId().getBytes();
        this.username = user.getName();
        this.displayname = user.getDisplayName();
    }

    public UserIdentity toUserIdentity() {
        return UserIdentity.builder()
            .name(getUsername())
            .displayName(getDisplayname())
            .id(getByteArrayHandle())
            .build();
    }

    public ByteArray getByteArrayHandle() {
        return new ByteArray(getHandle());
    }
}