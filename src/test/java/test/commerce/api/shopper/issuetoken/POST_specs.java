package test.commerce.api.shopper.issuetoken;

import commerce.command.CreateShopperCommand;
import commerce.command.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import test.commerce.api.CommerceApiTest;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.JWTAssertion.conformToJWTFormat;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("POST /shopper/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드와_접근_토큰을_반환한다(
            @Autowired TestRestTemplate client) {
        // Given
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
                "/shopper/signUp",
                new CreateShopperCommand(email, generateUsername(), password),
                Void.class
        );

        // When
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(email, password),
                AccessTokenCarrier.class
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isNotNull();
    }

    @Test
    void 접근_토큰은_JWT_형식을_따른다(
            @Autowired TestRestTemplate client) {
        // Given
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
                "/shopper/signUp",
                new CreateShopperCommand(email, generateUsername(), password),
                Void.class
        );

        // When
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(email, password),
                AccessTokenCarrier.class
        );

        // Then®
        String actual = requireNonNull(response.getBody().accessToken());
        assertThat(actual).satisfies(conformToJWTFormat());
    }

    @Test
    void 존재하지_않는_이메일_주소가_사용되면_400_Bad_Request_상태코드를_반환한다(
            @Autowired TestRestTemplate client) {
        // Given
        String email = generateEmail();
        String password = generatePassword();

        // When
        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(email, password),
                Void.class
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 잘못된_비밀번호가_사용되면_400_Bad_Request_상태코드를_반환한다 (
            @Autowired TestRestTemplate client) {
        // Given
        String email = generateEmail();
        String password = generatePassword();
        String wrongPassword = generatePassword();

        client.postForEntity(
                "/shopper/signUp",
                new CreateShopperCommand(email, generateUsername(), password),
                Void.class
        );

        // When
        ResponseEntity<Void> response = client.postForEntity(
                "/shopper/issueToken",
                new IssueShopperToken(email, wrongPassword),
                Void.class
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
