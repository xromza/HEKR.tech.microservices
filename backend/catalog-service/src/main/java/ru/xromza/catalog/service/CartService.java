package ru.xromza.catalog.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.catalog.dto.CartItemRequestDto;
import ru.xromza.catalog.dto.CartItemResponseDto;
import ru.xromza.catalog.dto.CartResponseDto;
import ru.xromza.catalog.exceptions.NotFoundException;
import ru.xromza.catalog.mapper.CartItemMapper;
import ru.xromza.catalog.model.Cart;
import ru.xromza.catalog.model.CartItem;
import ru.xromza.catalog.model.Image;
import ru.xromza.catalog.model.Product;
import ru.xromza.catalog.model.ProductVariant;
import ru.xromza.catalog.repository.CartRepository;
import ru.xromza.catalog.utils.ImageType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    private final ProductVariantService productVariantService;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;

    protected Cart getCartByUserId(Long userId) {
        return cartRepository.findById(userId).orElse(Cart.builder().userId(userId).build());
    }

    protected Cart addToCart(Long userId, CartItem newItem) {
        Cart cart = getCartByUserId(userId);
        if (!productVariantService.existsById(newItem.getVariantId())) {
            throw new NotFoundException("Вариант товара с id:" + newItem.getVariantId() + " не найден");
        }
        List<CartItem> items = cart.getItems();

        CartItem existingItem = items.stream()
                .filter(item -> item.getVariantId().equals(newItem.getVariantId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(newItem.getQuantity());
        } else {
            items.add(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeFromCart(Long userId, Long variantId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getVariantId().equals(variantId));
        return cartRepository.save(cart);
    }

    public void clearCart(Long userId) {
        cartRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public CartResponseDto getCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> cartItems = cart.getItems();

        List<Long> variantIds = cartItems.stream().map(CartItem::getVariantId).toList();
        Map<Long, ProductVariant> variants = productVariantService.getAllVariantsByIds(variantIds);
        List<Long> productIds = variants.entrySet().stream().map(entry -> entry.getValue().getProduct().getId())
                .toList();
        Map<Long, Product> products = productService.getProductsByIds(productIds);
        BigDecimal totalPrice = cartItems.stream().map(c -> {
            ProductVariant variant = variants.get(c.getVariantId());
            boolean isWholesale = c.getQuantity() >= variant.getProduct()
                    .getWholesaleThreshold();

            Product product = products.get(variant.getProduct().getId());

            BigDecimal price = isWholesale
                    ? product.getPriceWholesale()
                    : product.getPriceRetail();

            return price.multiply(BigDecimal.valueOf(c.getQuantity()));

        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CartItemResponseDto> dtoItems = cartItemMapper.toListDto(cartItems);
        dtoItems = dtoItems.stream().map(item -> {
            ProductVariant variant = variants.get(item.getVariantId());
            Product product = products.get(variant.getProduct().getId());
            if (variant != null && product != null) {
                item.setBrand(product.getBrand());
                item.setSize(variant.getSize());
                item.setColor(variant.getColor());
                item.setTitle(product.getTitle());
                boolean isWholesale = item.getQuantity() >= product.getWholesaleThreshold();
                String priceType = isWholesale ? "WHOLESALE" : "RETAIL";
                item.setPriceType(priceType);
                BigDecimal appliedPrice = isWholesale
                        ? product.getPriceWholesale()
                        : product.getPriceRetail();
                item.setAppliedPrice(appliedPrice);
                BigDecimal subtotal = appliedPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setSubtotal(subtotal);

                String mainImageUrl = variant.getImages().stream()
                        .filter(img -> img.getType() == ImageType.MAIN)
                        .map(Image::getUrl)
                        .findFirst()
                        .or(() -> variant.getImages().stream()
                                .map(Image::getUrl)
                                .findFirst())
                        .orElse(null);
                item.setImageUrl(mainImageUrl);
            }

            return item;
        }).toList();

        return CartResponseDto.builder()
                .totalPrice(totalPrice)
                .items(dtoItems)
                .build();

    }

    @Transactional
    public CartResponseDto addOrUpdateItem(Long userId, CartItemRequestDto cartItemRequestDto) {
        addToCart(userId, cartItemMapper.toEntity(cartItemRequestDto));
        return getCart(userId);
    }

    @Transactional
    public void deleteItems(Long userId, List<CartItemRequestDto> dtos) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> items = cart.getItems();
        List<Long> ids = dtos.stream().map(CartItemRequestDto::getVariantId).toList();
        items.removeIf(item -> ids.contains(item.getVariantId()));
        cartRepository.save(cart);
    }
}
