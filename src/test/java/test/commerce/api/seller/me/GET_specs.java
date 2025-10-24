package test.commerce.api.seller.me;

import commerce.command.CreateSellerCommand;
import commerce.command.api.controller.view.SellerMeView;
import commerce.command.query.IssueSellerToken;
import commerce.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import test.commerce.api.CommerceApiTest;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.get;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("GET /seller/me")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // Given
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();

        CreateSellerCommand command = new CreateSellerCommand(email, username, password);
        client.postForEntity(("/seller/signUp"), command, Void.class);

        AccessTokenCarrier carrier = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );
        String token = carrier.accessToken();

        // When
        ResponseEntity<SellerMeView> response = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token)
                        .build(),
                SellerMeView.class
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // When
        ResponseEntity<Void> response = client.getForEntity(
                "/seller/me",
                Void.class
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }


    @Test
    void 서로_다른_판매자의_식별자는_서로_다르다(
            @Autowired TestRestTemplate client) {
        // Given
        String email1 = generateEmail();
        String username1 = generateUsername();
        String password1 = generatePassword();

        CreateSellerCommand command1 = new CreateSellerCommand(email1, username1, password1);
        client.postForEntity(("/seller/signUp"), command1, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email1, password1),
                AccessTokenCarrier.class
        );
        String token1 = carrier1.accessToken();

        String email2 = generateEmail();
        String username2 = generateUsername();
        String password2 = generatePassword();

        CreateSellerCommand command2 = new CreateSellerCommand(email2, username2, password2);
        client.postForEntity(("/seller/signUp"), command2, Void.class);

        AccessTokenCarrier carrier2 = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email2, password2),
                AccessTokenCarrier.class
        );
        String token2 = carrier2.accessToken();

        // When
        ResponseEntity<SellerMeView> response1 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token1)
                        .build(),
                SellerMeView.class
        );

        ResponseEntity<SellerMeView> response2 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token2)
                        .build(),
                SellerMeView.class
        );

        // Then
        assertThat(requireNonNull(response1.getBody()).id())
                .isNotEqualTo(requireNonNull(response2.getBody()).id());
    }

    @Test
    void 같은_판매자의_식별자는_항상_같다(
            @Autowired TestRestTemplate client) {
        // Given
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();

        var command = new CreateSellerCommand(email, username, password);
        client.postForEntity(("/seller/signUp"), command, Void.class);

        AccessTokenCarrier carrier1 = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );
        String token1 = carrier1.accessToken();

        AccessTokenCarrier carrier2 = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );
        String token2 = carrier2.accessToken();

        // When
        ResponseEntity<SellerMeView> response1 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token1)
                        .build(),
                SellerMeView.class
        );

        ResponseEntity<SellerMeView> response2 = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token2)
                        .build(),
                SellerMeView.class
        );

        // Then
        assertThat(requireNonNull(response1.getBody()).id())
                .isEqualTo(requireNonNull(response2.getBody()).id());
    }

    @Test
    void 판매자의_기본_정보가_올바르게_설정된다(
            @Autowired TestRestTemplate client
    ) {
        // Given
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();

        CreateSellerCommand command = new CreateSellerCommand(email, username, password);
        client.postForEntity(("/seller/signUp"), command, Void.class);

        AccessTokenCarrier carrier = client.postForObject(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );
        String token = carrier.accessToken();

        // When
        ResponseEntity<SellerMeView> response = client.exchange(
                get("/seller/me")
                        .header("Authorization", "Bearer " + token)
                        .build(),
                SellerMeView.class
        );

        // Then
        SellerMeView actual = requireNonNull(response.getBody());
        assertThat(actual.email()).isEqualTo(email);
        assertThat(actual.username()).isEqualTo(username);
    }
}
