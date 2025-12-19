package commerce.command.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.command.RegisterProductCommand;
import commerce.command.api.controller.view.ArrayCarrier;
import commerce.command.api.controller.view.SellerProductView;
import commerce.command.query.FindSellerProduct;
import commerce.commandmodel.RegisterProductCommandExecutor;
import commerce.querymodel.FindSellerProductQueryProcessor;
import commerce.querymodel.ProductMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Comparator.reverseOrder;

@RestController
public record SellerProductController(ProductRepository repository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProduct(
            @RequestBody RegisterProductCommand command,
            Principal user
    ) {
        UUID id = UUID.randomUUID();
        var executor = new RegisterProductCommandExecutor(repository::save);
        executor.execute(id, UUID.fromString(user.getName()), command);
        URI location = URI.create("/seller/products/" + id);
        return ResponseEntity.created(location).build();
    }

    private static UUID getSellerId(Principal user) {
        return UUID.fromString(user.getName());
    }

    @GetMapping("/seller/products/{id}")
    ResponseEntity<?> findProduct(@PathVariable UUID id, Principal user) {
        UUID sellerId = UUID.fromString(user.getName());
        Function<UUID, Optional<Product>> findProduct = repository::findById;
        var processor = new FindSellerProductQueryProcessor(findProduct);
        var query = new FindSellerProduct(sellerId, id);

        return process(processor, query)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static Optional<SellerProductView> process(FindSellerProductQueryProcessor processor, FindSellerProduct query) {
        return processor.findProduct.apply(query.productId())
                .filter(product -> product.getSellerId().equals(query.sellerId()))
                .map(ProductMapper::convertToView);
    }

    @GetMapping("/seller/products")
    ResponseEntity<?> getProducts(Principal user) {
        UUID sellerId = UUID.fromString(user.getName());
        SellerProductView[] items = repository
                .findBySellerId(sellerId)
                .stream()
                .sorted(Comparator.comparing(Product::getRegisteredTimeUtc, reverseOrder()))
                .map(ProductMapper::convertToView)
                .toArray(SellerProductView[]::new);
        return ResponseEntity.ok(new ArrayCarrier<SellerProductView>(items));
    }
}
