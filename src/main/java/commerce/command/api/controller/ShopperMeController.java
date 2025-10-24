package commerce.command.api.controller;

import commerce.Shopper;
import commerce.ShopperRepository;
import commerce.command.api.controller.view.ShopperMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeController(ShopperRepository repository) {

    @GetMapping("/shopper/me")
    ShopperMeView me(Principal user) {
        UUID id = UUID.fromString(user.getName());
        Shopper shopper = repository.findById(id).orElseThrow();
        return new ShopperMeView(id, shopper.getEmail(), shopper.getUsername());
    }
}
