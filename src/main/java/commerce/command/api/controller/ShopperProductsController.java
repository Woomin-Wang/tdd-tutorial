package commerce.command.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.command.api.controller.view.ProductView;
import commerce.result.PageCarrier;
import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.*;

@RestController
public record ShopperProductsController(
        ProductRepository repository,
        EntityManager entityManager
) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts(
            @RequestParam(required = false) String continuationToken) {

        String queryString = """
                SELECT new commerce.command.api.controller.ProductSellerTuple(p,s)
                FROM Product p
                JOIN Seller s ON p.sellerId = s.id
                WHERE :cursor IS NULL OR p.dataKey <= :cursor
                ORDER BY p.dataKey DESC
                """;

        int pageSize = 10;

        List<ProductSellerTuple> results = entityManager
                .createQuery(queryString, ProductSellerTuple.class)
                .setParameter("cursor", decodeCursor(continuationToken))
                .setMaxResults(pageSize + 1)
                .getResultList();

        ProductView[] items = results
                .stream()
                .limit(pageSize)
                .map(ProductSellerTuple::toView)
                .toArray(ProductView[]::new);

        Long next = results.size() <= pageSize
                ? null
                : results.getLast().product().getDataKey();

        return new PageCarrier<>(items, encodeCursor(next));
    }

    private Long decodeCursor(String continuationToken) {
        if (continuationToken == null || continuationToken.isBlank()) {
            return null;
        }

        byte[] data = Base64.getUrlDecoder().decode(continuationToken);
        return Long.parseLong(new String(data, UTF_8));
    }

    private String encodeCursor(Long cursor) {
        if (cursor == null) {
            return null;
        }

        byte[] data = cursor.toString().getBytes(UTF_8);
        return Base64.getUrlEncoder().encodeToString(data);
    }
}
