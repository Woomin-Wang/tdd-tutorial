package commerce.command.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerIssueTokenController {

    @PostMapping("/seller/issueToken")
    void issueTokens() {
        // Implementation for issuing tokens to sellers would go here.
    }

}
