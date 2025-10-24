package test.commerce.api;

import commerce.command.CreateShopperCommand;
import commerce.command.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

public record TestFixture(TestRestTemplate client) {

    public void createShopper(String email, String username, String password) {
        CreateShopperCommand command = new CreateShopperCommand(email, username, password);
        client().postForEntity("/shopper/signUp", command, Void.class);
    }

    public String issueShopperToken(String email, String password) {
        AccessTokenCarrier carrier = client().postForObject(
                "/shopper/issueToken",
                new IssueShopperToken(email, password),
                AccessTokenCarrier.class
        );
        return carrier.accessToken();
    }

    public String createShopperThenIssueToken() {
        String email = generateEmail();
        String password = generatePassword();
        createShopper(email, generateUsername(), password);
        return issueShopperToken(email, password);
    }

    public void setShopperAsDefaultUser(String email, String password) {
        String token = issueShopperToken(email, password);
        RestTemplate template = client().getRestTemplate();
        template.getInterceptors().add((request, body, execution) -> {
            if (request.getHeaders().containsKey("Authorization") == false) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });
    }
}
