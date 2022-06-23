package com.my.app.service.impl;

import com.epam.app.model.Laptop;
import com.epam.app.model.Product;
import com.epam.app.model.Smartphone;
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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FromEntityToDtoUserConverter userConverter;
    private final FromResponseToDtoProductConverter productConverter;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public UserDto getUserById(@NonNull Long id) {
        final User userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User is not founded"));

        final Map<Long, Product> productsMap = userEntity.getOrders().stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItem::getProduct)
                .map(ProductId::getProductId)
                .map(this::fetchProduct)
                .collect(toMap(Product::getId, Function.identity(), (x1, x2) -> x1));

        final UserDto userDto = userConverter.convert(userEntity);

        populateUsers(List.of(userDto), productsMap);

        return userDto;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @NonNull
    public Collection<UserDto> getAllUsers() {
        final List<User> userEntities = userRepository.findAll();

        final Map<Long, Product> productsMap = userEntities.stream()
                .map(User::getOrders)
                .flatMap(Collection::stream)
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItem::getProduct)
                .map(ProductId::getProductId)
                .map(this::fetchProduct)
                .collect(toMap(Product::getId, Function.identity(), (x1, x2) -> x1));

        final List<UserDto> userDtoList = userEntities.stream().map(userConverter::convert).collect(toList());

        populateUsers(userDtoList, productsMap);

        return userDtoList;
    }

    private void populateUsers(List<UserDto> userDtoList, Map<Long, Product> productsMap) {
        for (UserDto userDto : userDtoList) {
            for (OrderDto orderDto : userDto.getOrders()) {
                for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                    orderItemDto.setProduct(productConverter.convert(productsMap.get(orderItemDto.getProductId())));
                }
            }
        }
    }

    private Product fetchProduct(Long id) {
        final long random = Math.round(Math.random());
        if (1 == random) {
            return Laptop.builder()
                    .id(id)
                    .name("Macbook PRO")
                    .description("ultrabook for professionals")
                    .cost(BigDecimal.valueOf(5500))
                    .processor("Intel Core i7")
                    .ram("16 Gb")
                    .storage("500 Gb")
                    .build();
        } else {
            return Smartphone.builder()
                    .id(id)
                    .name("Iphone X")
                    .description("smartphone for all")
                    .cost(BigDecimal.valueOf(3500))
                    .storage("500 Gb")
                    .hasBluetooth(true)
                    .build();
        }
    }
}
