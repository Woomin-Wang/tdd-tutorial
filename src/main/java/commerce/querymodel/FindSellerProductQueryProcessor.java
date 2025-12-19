package commerce.querymodel;

import commerce.Product;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class FindSellerProductQueryProcessor {

    public final Function<UUID, Optional<Product>> findProduct;

    public FindSellerProductQueryProcessor(
            Function<UUID,Optional<Product>> findProduct) {
        this.findProduct = findProduct;
    }
}
