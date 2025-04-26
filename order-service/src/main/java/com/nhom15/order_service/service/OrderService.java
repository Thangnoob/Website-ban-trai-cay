    package com.nhom15.order_service.service;

    import com.nhom15.order_service.dto.InventoryResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import com.nhom15.order_service.dto.OrderLineItemsDto;
    import com.nhom15.order_service.dto.OrderRequest;
    import com.nhom15.order_service.model.Order;
    import com.nhom15.order_service.model.OrderLineItems;
    import com.nhom15.order_service.repository.OrderRepository;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.reactive.function.client.WebClient;

    import java.util.Arrays;
    import java.util.List;
    import java.util.UUID;

    @Service
    @Transactional
    public class OrderService {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private WebClient.Builder webClientBuilder;

        public void placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());

            List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                    .stream()
                    .map(this::mapToDto)
                    .toList();

            order.setOrderLineItemsList(orderLineItems);

            List<String> skuCodes = order.getOrderLineItemsList().stream()
                    .map(OrderLineItems::getSkuCode)
                    .toList();

            //call inventory service, and place order if product is in
            InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
            } else {
                throw new IllegalAccessException("Product is not in stock, please try again later");
            }


        }

        private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
            OrderLineItems orderLineItems = new OrderLineItems();
            orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
            orderLineItems.setPrice(orderLineItemsDto.getPrice());
            orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
            return orderLineItems;
        }
    }
