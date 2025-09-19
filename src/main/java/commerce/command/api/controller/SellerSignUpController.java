package commerce.command.api.controller;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.CreateSellerCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerSignUpController(
        PasswordEncoder passwordEncoder,
        SellerRepository repository) {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{3,}$";

    @PostMapping("/seller/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateSellerCommand command) {

        if (isCommandValid(command) == false) {
            return ResponseEntity.badRequest().build();
        }

        String hashedPassword = passwordEncoder.encode(command.password());

        var seller = new Seller();
        seller.setEmail(command.email());
        seller.setUsername(command.username());
        seller.setHashedPassword(hashedPassword);

        try {
            repository.save(seller);
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateSellerCommand command) {
        return isEmailValid(command.email())
                && isUsernameValid(command.username())
                && isPasswordValid(command.password());
    }

    private static boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    private static boolean isUsernameValid(String username) {
        return username != null && username.matches(USERNAME_REGEX);
    }

    private static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }
}
