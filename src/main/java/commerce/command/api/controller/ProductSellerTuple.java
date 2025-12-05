package commerce.command.api.controller;

import commerce.Product;
import commerce.Seller;
import commerce.command.api.controller.view.ProductView;
import commerce.command.api.controller.view.SellerView;

record ProductSellerTuple(
        Product product,
        Seller seller
) {
    ProductView toView() {
        return new ProductView(
                product().getId(),
                new SellerView(seller().getId(), seller().getUsername()),
                product().getName(),
                product().getImageUri(),
                product().getDescription(),
                product().getPriceAmount(),
                product().getStockQuantity()
        );
    }
}
