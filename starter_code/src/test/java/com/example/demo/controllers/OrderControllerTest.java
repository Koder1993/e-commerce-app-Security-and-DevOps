package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    private OrderController orderController;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void testSubmit_validUser() {
        String username = "Username";
        User user = new User();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        user.setCart(cart);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(username);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testSubmit_invalidUser() {
        String username = "Username";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit(username);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrders_validUser() {
        String username = "Username";
        User user = Mockito.mock(User.class);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(new ArrayList<>());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetOrders_invalidUser() {
        String username = "Username";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
