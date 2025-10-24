package commerce.command.api.controller;

import commerce.Shopper;
import commerce.ShopperRepository;
import commerce.command.CreateShopperCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static commerce.UserPropertyValidator.*;

@RestController
public record ShopperSignUpController(
        PasswordEncoder passwordEncoder,
        ShopperRepository repository) {

    @PostMapping("/shopper/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateShopperCommand command) {
        if (isCommandValid(command) == false) {
            return ResponseEntity.badRequest().build();
        }

        var shopper = new Shopper();
        UUID id = UUID.randomUUID();
        shopper.setId(id);
        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(passwordEncoder.encode(command.password()));
        repository.save(shopper);

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateShopperCommand command) {
        return isEmailValid(command.email())
                && isUsernameValid(command.username())
                && isPasswordValid(command.password());
    }
}
