package ru.xromza.catalog.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xromza.catalog.dto.CartItemRequestDto;
import ru.xromza.catalog.dto.CartItemResponseDto;
import ru.xromza.catalog.dto.CartResponseDto;
import ru.xromza.catalog.dto.OrderItemRequestDto;
import ru.xromza.catalog.exceptions.NotFoundException;
import ru.xromza.catalog.interfaces.UserProvider;
import ru.xromza.catalog.mapper.CartItemResponseMapper;
import ru.xromza.catalog.model.Cart;
import ru.xromza.catalog.model.CartItemId;
import ru.xromza.catalog.model.Category;
import ru.xromza.catalog.model.Product;
import ru.xromza.catalog.model.ProductVariant;
import ru.xromza.catalog.model.User;
import ru.xromza.catalog.repository.CartRepository;
import ru.xromza.catalog.repository.ProductVariantsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    private final UserProvider userProvider;
    private final ProductVariantsRepository productVariantsRepository;
    private final CartItemResponseMapper cartItemResponseMapper;

    @Transactional(readOnly = true)
    public CartResponseDto getCart(UserDetails userDetails) {
        User user = userProvider.getApprovedUserByLogin(userDetails.getUsername());

        List<Cart> cart = cartRepository.findByIdUserId(user.getId());
        List<CartItemResponseDto> cartItems = cartItemResponseMapper.toResponseList(cart);

        boolean canCheckout = cartItems.stream().allMatch(c -> c.getAvailableStock() >= c.getQuantity());

        BigDecimal totalPrice = cart.stream().map(c -> {
            boolean isWholesale = c.getQuantity() >= c.getProductVariant().getProduct().getWholesaleThreshold();

            ProductVariant variant = c.getProductVariant();
            Product product = variant.getProduct();

            BigDecimal price = isWholesale
                    ? product.getPriceWholesale()
                    : product.getPriceRetail();

            return price.multiply(BigDecimal.valueOf(c.getQuantity()));

        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean discountApplied = cart.stream().anyMatch(c -> {
            Category category = c.getProductVariant().getProduct().getCategory();

            if (category == null || category.getDiscount() == null) {
                return false;
            }

            return category.getDiscount().getDiscount().compareTo(BigDecimal.ZERO) > 0;
        });

        return CartResponseDto.builder()
                .canCheckout(canCheckout)
                .discountApplied(discountApplied)
                .totalPrice(totalPrice)
                .items(cartItems)
                .build();

    }

    @Transactional
    public CartItemResponseDto addOrUpdateItem(UserDetails userDetails, CartItemRequestDto cartItemRequestDto) {
        User user = userProvider.getApprovedUserByLogin(userDetails.getUsername());

        ProductVariant variant = getProductVariantById(cartItemRequestDto.getVariantId());
        CartItemId id = CartItemId
                .builder()
                .userId(user.getId())
                .variantId(cartItemRequestDto.getVariantId())
                .build();
        Cart cartItem = Cart.builder()
                .productVariant(variant)
                .quantity(cartItemRequestDto.getQuantity())
                .user(user)
                .id(id)
                .build();
        Cart saved = cartRepository.save(cartItem);
        return cartItemResponseMapper.toDto(saved);

    }

    @Transactional
    public void deleteItem(UserDetails userDetails, Long variantId) {
        User user = userProvider.getApprovedUserByLogin(userDetails.getUsername());
        cartRepository.deleteByIdUserIdAndIdVariantId(user.getId(), variantId);
    }

    @Transactional
    public void deleteAll(UserDetails userDetails) {
        User user = userProvider.getApprovedUserByLogin(userDetails.getUsername());
        cartRepository.deleteByIdUserId(user.getId());
    }

    @Transactional
    public void deleteItems(UserDetails userDetails, List<OrderItemRequestDto> dto) {
        User user = userProvider.getApprovedUserByLogin(userDetails.getUsername());
        List<CartItemId> ids = dto.stream()
                .map(item -> CartItemId.builder()
                        .variantId(item.getVariantId())
                        .userId(user.getId())
                        .build())
                .toList();
        cartRepository.deleteAllByIdInBatch(ids);

    }

    public List<Cart> findByUserId(Long userId) {
        return cartRepository.findByIdUserId(userId);
    }

    @Transactional
    public CartResponseDto migrateCart(UserDetails userDetails, List<CartItemRequestDto> dto) {
        User user = userProvider.getApprovedUserByLogin(userDetails.getUsername());

        dto.stream().forEach((item) -> {
            ProductVariant variant = getProductVariantById(item.getVariantId());
            CartItemId id = CartItemId
                    .builder()
                    .userId(user.getId())
                    .variantId(item.getVariantId())
                    .build();
            Cart cartItem = Cart.builder()
                    .productVariant(variant)
                    .quantity(item.getQuantity())
                    .user(user)
                    .id(id)
                    .build();
            cartRepository.save(cartItem);
        });

        return getCart(userDetails);

    }

    private ProductVariant getProductVariantById(Long variantId) {
        return productVariantsRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Вариант товара не найден"));
    }

}
