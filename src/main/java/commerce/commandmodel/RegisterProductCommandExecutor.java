package commerce.commandmodel;

import commerce.Product;

import java.util.function.Consumer;

public class RegisterProductCommandExecutor {

    private final Consumer<Product> saveProduct;

    public RegisterProductCommandExecutor(Consumer<Product> saveProduct) {
        this.saveProduct = saveProduct;
    }
}
