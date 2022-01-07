package com.webauthn.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webauthn.app.Utility;
import com.webauthn.app.data.objects.User;
import com.webauthn.app.data.repository.AuthenticatorRepository;
import com.webauthn.app.data.repository.RegistrationRepository;
import com.webauthn.app.data.repository.UserRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.UserIdentity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private final RegistrationRepository registrationRepo;

    private final RelyingParty relyingParty;

    AuthController(RegistrationRepository regisrationRepo, RelyingParty relyingPary) {
        this.relyingParty = relyingPary;
        this.registrationRepo = regisrationRepo;
    }

    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String newRegistration(
        @RequestBody String username,
        @RequestBody String display,
        Model model
    ) throws JsonProcessingException {
        User existingUser = registrationRepo.getUserRepo().findByUsername(username);
        if (existingUser != null) {
            model.addAttribute("message", "Username '" + username + "' already exists. Choose a different name");
            return "register";
        } else {
            UserIdentity user = UserIdentity.builder()
                .name(username)
                .displayName(display)
                .id(Utility.generateRandom(32))
                .build();
            StartRegistrationOptions registrationOptions = StartRegistrationOptions.builder()
                .user(user)
                .build();
            PublicKeyCredentialCreationOptions credentialOptions = relyingParty.startRegistration(registrationOptions);
            String optionsJson = credentialOptions.toCredentialsCreateJson();
            model.addAttribute("options", optionsJson);
            model.addAttribute("user", user);
            return optionsJson;
        }

    }

    @PostMapping("/register/final")
    public String finishRegisration() {
        return "Hello!";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        return "Hello2!";
    }
}
