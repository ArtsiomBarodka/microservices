package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.ProductListRequest;
import com.epam.app.model.ProductResponse;
import com.my.app.client.ProductClient;
import com.my.app.model.converter.FromEntityToDtoUserConverter;
import com.my.app.model.converter.FromResponseToDtoProductConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.UserDto;
import com.my.app.model.entity.Order;
import com.my.app.model.entity.OrderItem;
import com.my.app.model.entity.ProductId;
import com.my.app.model.entity.User;
import com.my.app.repository.UserRepository;
import com.my.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProductClient productClient;
    private final FromEntityToDtoUserConverter userConverter;
    private final FromResponseToDtoProductConverter productConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public UserDto getUserById(@NonNull Long id) {
        final User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User is not found"));

        final UserDto userDto = userConverter.convert(userEntity);

        final Set<String> ids = getProductsIds(List.of(userEntity));
        if (!ids.isEmpty()) {
            final Map<String, ProductResponse> productsMap = getProducts(ids);
            populateUsers(List.of(userDto), productsMap);
        }

        return userDto;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<UserDto> getAllUsers() {
        final List<User> userEntities = userRepository.findAll();

        final List<UserDto> userDtoList = userEntities.stream()
                .map(userConverter::convert)
                .collect(toList());

        final Set<String> ids = getProductsIds(userEntities);
        if (!ids.isEmpty()) {
            final Map<String, ProductResponse> productsMap = getProducts(ids);
            populateUsers(userDtoList, productsMap);
        }

        return userDtoList;
    }

    private Set<String> getProductsIds(List<User> userEntities) {
        return userEntities.stream()
                .map(User::getOrders)
                .flatMap(Collection::stream)
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItem::getProduct)
                .map(ProductId::getProductId)
                .collect(toSet());
    }

    private Map<String, ProductResponse> getProducts(Set<String> ids) {
        final ProductListRequest productListRequest = ProductListRequest.builder().ids(ids).build();

        return productClient
                .getAllProductsByIds(productListRequest)
                .stream()
                .collect(toMap(ProductResponse::getId, Function.identity()));
    }

    private void populateUsers(List<UserDto> userDtoList, Map<String, ProductResponse> productsMap) {
        for (UserDto userDto : userDtoList) {
            for (OrderDto orderDto : userDto.getOrders()) {
                for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                    ProductResponse productResponse = productsMap.get(orderItemDto.getProductId());
                    if (productResponse == null) {
                        throw new ObjectNotFoundException("Order item does not have a product");
                    }
                    orderItemDto.setProduct(productConverter.convert(productResponse));
                }
            }
        }
    }
}
