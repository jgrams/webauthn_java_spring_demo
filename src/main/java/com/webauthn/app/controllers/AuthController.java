package com.webauthn.app.controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webauthn.app.EhCache;
import com.webauthn.app.Utility;
import com.webauthn.app.data.objects.Credential;
import com.webauthn.app.data.objects.User;
import com.webauthn.app.data.repository.RegistrationRepository;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.FinishAssertionOptions;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartAssertionOptions;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.UserIdentity;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private RelyingParty relyingParty;
    private RegistrationRepository registrationRepo;
    private EhCache cache;

    AuthController(RegistrationRepository regisrationRepo, RelyingParty relyingPary) {
        this.relyingParty = relyingPary;
        this.registrationRepo = regisrationRepo;
        this.cache = new EhCache();
    }

    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String newUserRegistration(
        @RequestParam String username,
        @RequestParam String display
    ) {
        User existingUser = registrationRepo.getUserRepo().findByUsername(username);
        if (existingUser == null) {
            UserIdentity userIdentity = UserIdentity.builder()
                .name(username)
                .displayName(display)
                .id(Utility.generateRandom(32))
                .build();
            User saveUser = new User(userIdentity);
            registrationRepo.getUserRepo().save(saveUser);
            String response = newAuthRegistration(saveUser);
            return response;
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username " + username + " already exists. Choose a new name.");
        }
    }

    @PostMapping("/registerauth")
    @ResponseBody
    public String newAuthRegistration(
        @RequestParam User user
    ) {
        User existingUser = registrationRepo.getUserRepo().findByHandle(user.getHandle());
        if (existingUser != null) {
            UserIdentity userIdentity = user.toUserIdentity();
            return initiateCredential(userIdentity);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User " + user.getUsername() + " does not exist. Please register.");
        }
    }

    @PostMapping("/finishauth")
    @ResponseBody
    public ModelAndView finishRegisration(
        @RequestParam String credential,
        @RequestParam String username,
        @RequestParam String credname
    ) {
            try {
                User user = registrationRepo.getUserRepo().findByUsername(username);
                if (cache.getCredentialCache().containsKey(user.getByteArrayHandle())) {
                    PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc =
                    PublicKeyCredential.parseRegistrationResponseJson(credential);
                    PublicKeyCredentialCreationOptions requestOptions = cache.getCredentialCache().get(user.getByteArrayHandle());
                    cache.getCredentialCache().remove(user.getByteArrayHandle());
                    FinishRegistrationOptions options = FinishRegistrationOptions.builder()
                        .request(requestOptions)
                        .response(pkc)
                        .build();
                    RegistrationResult result = relyingParty.finishRegistration(options);
                    Credential savedAuth = new Credential(result, pkc.getResponse(), user, credname);
                    registrationRepo.getAuthRepo().save(savedAuth);
                    return new ModelAndView("redirect:/login", HttpStatus.SEE_OTHER);
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cached request expired. Try to register again!");
                }
            } catch (RegistrationFailedException e) {
                System.out.println(e.getMessage());
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Registration failed.", e);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save credenital, please try again!", e);
            }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String startLogin(
        @RequestParam String username
    ) {
        AssertionRequest request = relyingParty.startAssertion(StartAssertionOptions.builder()
            .username(username)
            .build());
        try {
            cache.getRequestCache().put(username, request);
            return request.toCredentialsGetJson();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/welcome")
    public String finishLogin(
        @RequestParam String credential,
        @RequestParam String username
    ) {
        try {
            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc;
            pkc = PublicKeyCredential.parseAssertionResponseJson(credential);
            AssertionRequest request = cache.getRequestCache().get(username);
            cache.getRequestCache().remove(username);
            AssertionResult result = relyingParty.finishAssertion(FinishAssertionOptions.builder()
                .request(request)
                .response(pkc)
                .build());
            if (result.isSuccess()) {
                return "welcome";
            } else {
                return "index";
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            return e1.getMessage();
        } catch (AssertionFailedException e) {
            throw new RuntimeException("Authentication failed", e);
        }

    }

    private String initiateCredential(UserIdentity user) {
        StartRegistrationOptions registrationOptions = StartRegistrationOptions.builder()
            .user(user)
            .build();
        PublicKeyCredentialCreationOptions registration = relyingParty.startRegistration(registrationOptions);
        cache.getCredentialCache().put(user.getId(), registration);
        try {
            return registration.toCredentialsCreateJson();
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing JSON.", e);
        }
    }
}
