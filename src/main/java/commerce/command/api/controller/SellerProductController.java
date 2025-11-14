package commerce.command.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.command.RegisterProductCommand;
import commerce.command.api.controller.view.ArrayCarrier;
import commerce.command.api.controller.view.SellerProductView;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static java.util.Comparator.reverseOrder;

@RestController
public record SellerProductController(ProductRepository repository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProduct(
            @RequestBody RegisterProductCommand command,
            Principal user
    ) {
        if (isValidUri(command.imageUri()) == false) {
            return ResponseEntity.badRequest().build();
        }

        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setSellerId(UUID.fromString(user.getName()));

        product.setName(command.name());
        product.setImageUri(command.imageUri());
        product.setDescription(command.description());
        product.setPriceAmount(command.priceAmount());
        product.setStockQuantity(command.stockQuantity());
        product.setRegisteredTimeUtc(LocalDateTime.now(UTC));

        repository.save(product);
        URI location = URI.create("/seller/products/" + id);
        return ResponseEntity.created(location).build();
    }

    private boolean isValidUri(String value) {
        try {
            URI uri = URI.create(value);
            return uri.getHost() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @GetMapping("/seller/products/{id}")
    ResponseEntity<?> findProduct(@PathVariable UUID id, Principal user) {
        UUID sellerId = UUID.fromString(user.getName());

        return repository
                .findById(id)
                .filter(product -> product.getSellerId().equals(sellerId))
                .map(SellerProductController::convertToView)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/products")
    ResponseEntity<?> getProducts(Principal user) {
        UUID sellerId = UUID.fromString(user.getName());
        SellerProductView[] items = repository
                .findBySellerId(sellerId)
                .stream()
                .sorted(Comparator.comparing(Product::getRegisteredTimeUtc, reverseOrder()))
                .map(SellerProductController::convertToView)
                .toArray(SellerProductView[]::new);
        return ResponseEntity.ok(new ArrayCarrier<SellerProductView>(items));
    }

    private static SellerProductView convertToView(Product product) {
        return new SellerProductView(
                product.getId(),
                product.getName(),
                product.getImageUri(),
                product.getDescription(),
                product.getPriceAmount(),
                product.getStockQuantity(),
                product.getRegisteredTimeUtc()
        );
    }
}
