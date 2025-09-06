package test.commerce.api.seller.signup;

import commerce.CommerceApiApp;
import commerce.command.CreateSellerCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(
        classes = CommerceApiApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@DisplayName("POST /seller/signup")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_No_Content_상태코드를_반환한다(@Autowired TestRestTemplate client) {
        // Given
        CreateSellerCommand command = new CreateSellerCommand(
                "seller@test.com",
                "seller",
                "password"
        );

        // When
        ResponseEntity<Void> response = client.postForEntity(
                "/seller/signUp",
                command,
                Void.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
}
