package commerce.command.api.controller;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.api.controller.view.SellerMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerMeController(
        SellerRepository repository
) {

    @GetMapping("/seller/me")
    SellerMeView me(Principal user) {
        UUID id = UUID.fromString(user.getName());
        Seller seller = repository.findById(id).orElseThrow();
        return new SellerMeView(id, seller.getEmail(), seller.getUsername());
    }
}

