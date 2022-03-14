package com.webauthn.app;

import com.webauthn.app.configuration.WebAuthProperties;
import com.webauthn.app.web.RegistrationService;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	@Autowired
	public RelyingParty relyingParty(RegistrationService regisrationRepository, WebAuthProperties properties) {
		RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder()
			.id(properties.getHostName())
			.name(properties.getDisplay())
			.build();

		return RelyingParty.builder()
			.identity(rpIdentity)
			.credentialRepository(regisrationRepository)
			.origins(properties.getOrigin())
			.build();
	}
}
