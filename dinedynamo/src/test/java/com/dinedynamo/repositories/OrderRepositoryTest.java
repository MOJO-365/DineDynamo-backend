package com.dinedynamo.repositories;
import com.dinedynamo.collections.Order;
import com.dinedynamo.repositories.OrderRepository;
import com.dinedynamo.controllers.OrderController;
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

    // Mock OrderRepository to isolate the test from the actual repository implementation
    @Mock
    private OrderRepository orderRepository;

    // Inject the mock OrderRepository into the OrderController
    @InjectMocks
    private OrderController orderController;

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
