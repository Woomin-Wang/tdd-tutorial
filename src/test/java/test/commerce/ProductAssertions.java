package test.commerce;

import commerce.command.RegisterProductCommand;
import commerce.command.api.controller.view.SellerProductView;
import org.assertj.core.api.ThrowingConsumer;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static java.util.regex.Pattern.matches;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProductAssertions {

    public static ThrowingConsumer<? super SellerProductView> isDerivedFrom(
            RegisterProductCommand command) {
        return product -> {
            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.imageUri()).isEqualTo(command.imageUri());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount()).matches(equals(command.priceAmount()));
            assertThat(product.stockQuantity()).isEqualTo(command.stockQuantity());
        };
    }

    private static Predicate<? super BigDecimal> equals(BigDecimal expected) {
        return actual -> actual.compareTo(expected) == 0;
    }
}
