package com.webauthn.app;

import com.webauthn.app.data.repository.RegistrationRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {
	private final RelyingParty rp;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
		configureWebAuthServer();
	}

	public static void configureWebAuthServer() {
		RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder()
			.id("example.com")
			.name("Yubi Application")
			.build();
		RelyingParty rp = RelyingParty.builder()
			.identity(rpIdentity)
			.credentialRepository(new RegistrationRepository())
			.validateSignatureCounter(true)
			.build();
	}

}
