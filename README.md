# Java Spring Boot Implementation of WebAuthn

This example app demonstrates how WebAuthn works using Spring Boot.

Please read [Building a WebAuthn Application with Java][blog-post] to see how this app was created.

**Prerequisites:**

**Java 17**: This project uses Java 17. If you don't have Java 17, you can install OpenJDK. Instructions are found on the [OpenJDK website](https://openjdk.java.net/install/).

* [Getting Started](#getting-started)
* [Start the Apps](#start-the-apps)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example application, run the following commands:

```bash
git clone https://github.com/oktadev/webauthn-java-example.git
cd webauthn-java-example
```

This will get a copy of the project installed locally.

## Start the Apps

To install all of its dependencies and the app, run:

```bash
./mvnw spring-boot:run
```

You can now test the application by opening http://localhost:8080

## Links

This example uses the following open source libraries:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Yubico's Server-side Web Authentication Library](https://developers.yubico.com/java-webauthn-server/)

## Help

Please post any questions as comments on the [blog post], or visit our [Okta Developer Forums](https://devforum.okta.com/).

## License

Apache 2.0, see [LICENSE](LICENSE).

[blog-post]: https://developer.okta.com/blog/2022/04/26/webauthn-java
