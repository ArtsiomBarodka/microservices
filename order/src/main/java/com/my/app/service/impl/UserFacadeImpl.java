package com.my.app.service.impl;

import com.epam.app.model.ProductListRequest;
import com.epam.app.model.ProductRequest;
import com.epam.app.model.ProductResponse;
import com.my.app.client.ProductWebClient;
import com.my.app.model.converter.FromResponseToDtoProductConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.UserDto;
import com.my.app.service.UserFacade;
import com.my.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;
    private final ProductWebClient productWebClient;
    private final FromResponseToDtoProductConverter toDtoProductConverter;

    @Override
    @NonNull
    public UserDto getUserById(@NonNull Long id) {
        final UserDto user = userService.getUserById(id);

        final Set<String> ids = getProductsIds(List.of(user));
        if (!ids.isEmpty()) {
            final Map<String, ProductResponse> productsMap = getProducts(ids);
            populateUsers(List.of(user), productsMap);
        }

        return user;
    }

    @Override
    @NonNull
    public Collection<UserDto> getAllUsers() {
        final Collection<UserDto> users = userService.getAllUsers();

        final Set<String> ids = getProductsIds(users);
        if (!ids.isEmpty()) {
            final Map<String, ProductResponse> productsMap = getProducts(ids);
            populateUsers(users, productsMap);
        }

        return users;
    }

    private Set<String> getProductsIds(Collection<UserDto> users) {
        return users.stream()
                .map(UserDto::getOrders)
                .flatMap(Collection::stream)
                .map(OrderDto::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItemDto::getProductId)
                .collect(toSet());
    }

    private Map<String, ProductResponse> getProducts(Set<String> ids) {
        List<ProductRequest> products = ids.stream()
                .map(ProductRequest::new)
                .collect(toList());
        final ProductListRequest productsRequest = new ProductListRequest(products);

        return productWebClient
                .getAllProductsByIds(productsRequest)
                .stream()
                .collect(toMap(ProductResponse::getId, Function.identity()));
    }

    private void populateUsers(Collection<UserDto> users, Map<String, ProductResponse> productsMap) {
        for (UserDto user : users) {
            for (OrderDto order : user.getOrders()) {
                for (OrderItemDto orderItemDto : order.getOrderItems()) {
                    ProductResponse productResponse = productsMap.get(orderItemDto.getProductId());
                    orderItemDto.setProduct(toDtoProductConverter.convert(productResponse));
                }
            }
        }
    }
}
