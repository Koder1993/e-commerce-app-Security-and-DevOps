package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddToCart_validUser() {
        String username = "Username";
        User user = new User();
        Cart cart = new Cart();
        Item item = createItem(1L, "Item1");
        cart.setItems(new ArrayList<>());
        user.setCart(cart);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    public void testAddToCart_invalidUser() {
        String username = "Username";
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart_validUser() {
        String username = "Username";
        User user = new User();
        Cart cart = new Cart();
        Item item1 = createItem(1L, "Item1");
        Item item2 = createItem(2L, "Item2");
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        cart.setItems(items);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getItems().size());
    }

    @Test
    public void testRemoveFromCart_invalidUser() {
        String username = "Username";
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private Item createItem(long id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(new BigDecimal(10));
        return item;
    }
}
