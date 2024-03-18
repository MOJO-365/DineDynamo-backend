package com.dinedynamo.repositories;
import com.dinedynamo.collections.order_collections.Order;
import com.dinedynamo.controllers.orders_controllers.DineInOrderController;
import com.dinedynamo.repositories.order_repositories.DineInOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {


    @Mock
    private DineInOrderRepository orderRepository;


    @InjectMocks
    private DineInOrderController orderController;

    /**
     * Test the findByRestaurantId method of the OrderRepository.
     * Verifies that the method returns the correct list of orders for a given restaurant ID.
     */
    @Test
    public void testFindByRestaurantId() {
        String restaurantId = "123456789";
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findByRestaurantId(restaurantId)).thenReturn(orders);

        List<Order> retrievedOrders = orderRepository.findByRestaurantId(restaurantId);

        assertEquals(2, retrievedOrders.size());
    }

    /**
     * Test the findByTableId method of the OrderRepository.
     * Verifies that the method returns the correct list of orders for a given table ID.
     */
    @Test
    public void testFindByTableId() {
        // Define test data
        String tableId = "987654321";
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findByTableId(tableId)).thenReturn(orders);

        List<Order> retrievedOrders = orderRepository.findByTableId(tableId);

        assertEquals(2, retrievedOrders.size());
    }
}
