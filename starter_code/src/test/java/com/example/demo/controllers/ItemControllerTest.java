package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    private Item firstItem;

    private Item secondItem;

    @Before
    public void setUp() {
        firstItem = createItem(1L, "FirstItem");
        secondItem = createItem(2L, "SecondItem");
    }

    @Test
    public void testGetItems() {
        Mockito.when(itemRepository.findAll()).thenReturn(Arrays.asList(firstItem, secondItem));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().size(), 2);
    }

    @Test
    public void testGetItemById() {
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(firstItem));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getName(), "FirstItem");
    }

    @Test
    public void testGetItemsByName_valid() {
        Mockito.when(itemRepository.findByName("FirstItem")).thenReturn(Collections.singletonList(firstItem));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("FirstItem");
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().get(0), firstItem);
    }

    @Test
    public void testGetItemsByName_invalid() {
        Mockito.when(itemRepository.findByName("ThirdItem")).thenReturn(new ArrayList<>());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("ThirdItem");
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }


    private Item createItem(Long id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription("Item");
        item.setPrice(new BigDecimal("3.15"));
        return item;
    }
}
