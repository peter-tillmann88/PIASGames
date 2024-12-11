package com.eecs4413final.demo.service;

import com.eecs4413final.demo.model.Product;
import com.eecs4413final.demo.model.ShoppingCart;
import com.eecs4413final.demo.model.ShoppingCartItems;
import com.eecs4413final.demo.repository.ShoppingCartItemRepository;
import com.eecs4413final.demo.repository.ShoppingCartRepository;
import com.eecs4413final.demo.service.ShoppingCartService;
import com.eecs4413final.demo.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class ShoppingCartItemServiceImpl implements ShoppingCartItemService {

    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;

    public ShoppingCartItemServiceImpl(ShoppingCartItemRepository shoppingCartItemRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCartService shoppingCartService, ProductService productService) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }

    @Override
    public void addItemToCart(Long userId, Long productId, int quantity) {
        ShoppingCart cart = shoppingCartService.getCartByUser(userId);
        Product product = productService.getById(productId);


        if (cart == null) {
            throw new RuntimeException("Shopping cart not found for user id: " + userId);
        }

        if (product == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        Set<ShoppingCartItems> cartItems = cart.getShoppingCartItems();
        ShoppingCartItems item = new ShoppingCartItems();

        for (ShoppingCartItems items : cartItems) {
            if (items.getProduct().equals(product)) {
                item = items;
                break;
            }
        }

        if (item.getCartItemId() != null) {
            item.setQuantity(item.getQuantity() + quantity);
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            item = new ShoppingCartItems(
                    cart,
                    product,
                    quantity,
                    product.getPrice(),
                    product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.addShoppingCartItems(item);
        }


        shoppingCartItemRepository.save(item);
        shoppingCartRepository.save(cart);

    }

    @Override
    public void removeItemFromCart(Long userId, Long productId, int quantity) {
        ShoppingCart cart = shoppingCartService.getCartByUser(userId);
        Product product = productService.getById(productId);

        if (cart == null) {
            throw new RuntimeException("Shopping cart not found for user id: " + userId);
        }

        if (product == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        Set<ShoppingCartItems> cartItems = cart.getShoppingCartItems();
        ShoppingCartItems item = null;

        for (ShoppingCartItems items : cartItems) {
            if (items.getProduct().equals(product)) {
                item = items;
                break;
            }
        }

        if (item != null) {
            if (item.getQuantity() >= quantity) {
                item.setQuantity(item.getQuantity() - quantity);
                item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                shoppingCartItemRepository.save(item);
            } else {
                throw new RuntimeException("Insufficient quantity to remove from cart");
            }
        } else {
            throw new RuntimeException("Item not found in shopping cart");
        }

        shoppingCartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long userId, Long productId) {
        ShoppingCart cart = shoppingCartService.getCartByUser(userId);
        Product product = productService.getById(productId);

        if (cart == null) {
            throw new RuntimeException("Shopping cart not found for user id: " + userId);
        }

        if (product == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        Set<ShoppingCartItems> cartItems = cart.getShoppingCartItems();
        ShoppingCartItems item = null;

        for (ShoppingCartItems items : cartItems) {
            if (items.getProduct().equals(product)) {
                item = items;
                break;
            }
        }

        if (item != null) {
            cart.removeShoppingCartItems(item);
            shoppingCartItemRepository.delete(item);
        } else {
            throw new RuntimeException("Item not found in shopping cart");
        }

        shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCartItems getCartItem(Long userId, Long productId) {
        ShoppingCart cart = shoppingCartService.getCartByUser(userId);
        Product product = productService.getById(productId);

        if (cart == null) {
            throw new RuntimeException("Shopping cart not found for user id: " + userId);
        }

        if (product == null) {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        Set<ShoppingCartItems> cartItems = cart.getShoppingCartItems();
        ShoppingCartItems item = null;

        for (ShoppingCartItems items : cartItems) {
            if (items.getProduct().equals(product)) {
                item = items;
                break;
            }
        }

        if (item == null) {
            throw new RuntimeException("Item not found in shopping cart");
        }

        return item;
    }
}
