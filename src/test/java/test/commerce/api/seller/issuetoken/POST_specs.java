package test.commerce.api.seller.issuetoken;

import commerce.CommerceApiApp;
import commerce.command.CreateSellerCommand;
import commerce.command.query.IssueSellerToken;
import commerce.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@SpringBootTest(
        classes = CommerceApiApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /seller/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
            @Autowired TestRestTemplate client
    ) {
        // Given
        String email = generateEmail();
        String password = generatePassword();

        client.postForEntity(
                "/seller/signUp",
                new CreateSellerCommand(email, generateUsername(), password),
                Void.class);

        // When
        ResponseEntity<AccessTokenCarrier> response = client.postForEntity(
                "/seller/issueToken",
                new IssueSellerToken(email, password),
                AccessTokenCarrier.class
        );

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}
