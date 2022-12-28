package com.my.app.service.impl;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.request.ProductListRequest;
import com.epam.app.model.request.ProductRequest;
import com.epam.app.model.request.UpdateCustomerRequest;
import com.epam.app.model.request.UpdateProductListRequest;
import com.epam.app.model.request.UpdateProductRequest;
import com.epam.app.model.response.CustomerResponse;
import com.epam.app.model.response.ProductResponse;
import com.my.app.client.CustomerClient;
import com.my.app.client.ProductClient;
import com.my.app.model.converter.FromCustomerResponseToUserDtoConverter;
import com.my.app.model.converter.FromDtoToEntityOrderConverter;
import com.my.app.model.converter.FromResponseToDtoProductConverter;
import com.my.app.model.dto.OrderDto;
import com.my.app.model.dto.OrderItemDto;
import com.my.app.model.dto.ProductDto;
import com.my.app.service.OrderFacade;
import com.my.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final FromResponseToDtoProductConverter toDtoProductConverter;
    private final FromCustomerResponseToUserDtoConverter toUserDtoConverter;
    private final FromDtoToEntityOrderConverter toEntityOrderConverter;

    @Override
    @NonNull
    public OrderDto getOrderById(@NonNull Long id) {
        // fetching order
        OrderDto order = orderService.getOrderById(id);

        // fetching products
        final Set<String> ids = getProductsIds(List.of(order));
        if (ids.isEmpty()) {
            throw new ObjectNotFoundException("It should be at least 1 products id");
        }
        final Map<String, ProductResponse> productsMap = getProducts(ids);
        populateOrdersByProducts(List.of(order), productsMap);

        //fetching user
        CustomerResponse customerResponse = customerClient.getCustomerById(order.getUser().getId());
        if (customerResponse == null) {
            throw new ObjectNotFoundException("User is not found");
        }
        order.setUser(toUserDtoConverter.convert(customerResponse));

        return order;
    }

    @Override
    @NonNull
    public Collection<OrderDto> getAllOrdersByUserId(@NonNull Long id) {
        // fetching orders
        Collection<OrderDto> orders = orderService.getAllOrdersByUserId(id);

        // fetching products
        final Set<String> ids = getProductsIds(orders);
        if (ids.isEmpty()) {
            throw new ObjectNotFoundException("It should be at least 1 products id");
        }
        final Map<String, ProductResponse> productsMap = getProducts(ids);
        populateOrdersByProducts(orders, productsMap);

        //fetching user
        CustomerResponse customerResponse = customerClient.getCustomerById(id);
        if (customerResponse == null) {
            throw new ObjectNotFoundException("User is not found");
        }
        orders.forEach(order -> order.setUser(toUserDtoConverter.convert(customerResponse)));

        return orders;
    }

    @Override
    @NonNull
    @Transactional
    public OrderDto createOrder(@NonNull OrderDto order) {
        // creating new order
        OrderDto savedNewOrder = orderService.createOrder(order);

        // updating products count;
        final Map<String, ProductResponse> products = buyProducts(order.getOrderItems());
        populateOrdersByProducts(List.of(savedNewOrder), products);

        // updating customer funds;
        final BigDecimal totalProductsCost = products.values().stream()
                .map(ProductResponse::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final UpdateCustomerRequest updateCustomerRequest = UpdateCustomerRequest.builder()
                .id(order.getUser().getId())
                .fund(totalProductsCost)
                .build();
        CustomerResponse customerResponse = customerClient.subtractCustomerFund(updateCustomerRequest);
        if (customerResponse == null) {
            throw new ObjectNotFoundException("User is not found");
        }
        savedNewOrder.setUser(toUserDtoConverter.convert(customerResponse));

        // updating order after all buying operation are finished
        orderService.updateOrder(savedNewOrder);

        return savedNewOrder;
    }

    private Map<String, ProductResponse> getProducts(Set<String> ids) {
        List<ProductRequest> listProductRequest = ids.stream()
                .map(ProductRequest::new)
                .collect(toList());
        final ProductListRequest productsRequest = new ProductListRequest(listProductRequest);

        List<ProductResponse> productsResponse = productClient.getAllProductsByIds(productsRequest);
        if (productsResponse == null) {
            throw new ObjectNotFoundException("products are not found");
        }

        return productsResponse
                .stream()
                .collect(toMap(ProductResponse::getId, Function.identity()));
    }

    private Map<String, ProductResponse> buyProducts(List<OrderItemDto> orderItems) {
        List<UpdateProductRequest> listProductRequest = orderItems.stream()
                .map(orderItem -> {
                    UpdateProductRequest updateProductRequest = new UpdateProductRequest();
                    updateProductRequest.setId(orderItem.getProduct().getId());
                    updateProductRequest.setCount(orderItem.getCount());
                    return updateProductRequest;
                })
                .collect(toList());

        final UpdateProductListRequest productsRequest = new UpdateProductListRequest(listProductRequest);

        List<ProductResponse> productsResponse = productClient.subtractProductsCount(productsRequest);
        if (productsResponse == null) {
            throw new ObjectNotFoundException("products are not found");
        }

        return productsResponse
                .stream()
                .collect(toMap(ProductResponse::getId, Function.identity()));
    }

    private void populateOrdersByProducts(Collection<OrderDto> orderDtoList, final Map<String, ProductResponse> productsMap) {
        for (OrderDto orderDto : orderDtoList) {
            for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
                ProductResponse productResponse = productsMap.get(orderItemDto.getProduct().getId());
                if (productResponse == null) {
                    throw new ObjectNotFoundException("Product is not found");
                }
                orderItemDto.setProduct(toDtoProductConverter.convert(productResponse));
                orderItemDto.setCost(productResponse.getCost());
            }
        }
    }

    private Set<String> getProductsIds(Collection<OrderDto> orders) {
        return orders.stream()
                .map(OrderDto::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItemDto::getProduct)
                .map(ProductDto::getId)
                .collect(toSet());
    }
}
