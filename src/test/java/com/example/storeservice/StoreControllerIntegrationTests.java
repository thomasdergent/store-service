package com.example.storeservice;

import com.example.storeservice.model.Store;
import com.example.storeservice.repository.StoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    Store store1 = new Store("IKEA Hasselt", "Limburg", "Hasselt", "teststraat", 1);
    Store store2 = new Store("IKEA Wilrijk", "Antwerpen", "Wilrijk", "teststraat", 2);

    @BeforeEach
    public void beforeAllTests() {
        storeRepository.deleteAll();
        storeRepository.save(store1);
        storeRepository.save(store2);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database
        storeRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenStore_whenGetStores_thenReturnJsonStores() throws Exception {

        List<Store> productList = new ArrayList<>();
        productList.add(store1);
        productList.add(store2);

        mockMvc.perform(get("/stores"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$[0].province", is("Limburg")))
                .andExpect(jsonPath("$[0].city", is("Hasselt")))
                .andExpect(jsonPath("$[0].street", is("teststraat")))
                .andExpect(jsonPath("$[0].number", is(1)))
                .andExpect(jsonPath("$[1].storeName", is("IKEA Wilrijk")))
                .andExpect(jsonPath("$[1].province", is("Antwerpen")))
                .andExpect(jsonPath("$[1].city", is("Wilrijk")))
                .andExpect(jsonPath("$[1].street", is("teststraat")))
                .andExpect(jsonPath("$[1].number", is(2)));
    }

    @Test
    public void givenStore_whenGetStore_thenReturnJsonStore() throws Exception {

        mockMvc.perform(get("/store/{storeName}", store1.getStoreName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$.province", is("Limburg")))
                .andExpect(jsonPath("$.city", is("Hasselt")))
                .andExpect(jsonPath("$.street", is("teststraat")))
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Test
    public void whenPostStore_thenReturnJsonStore() throws Exception {

        Store store3 = new Store("IKEA Brugge", "West-Vlaanderen", "Brugge", "teststraat", 3);

        mockMvc.perform(post("/store")
                .content(mapper.writeValueAsString(store3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Brugge")))
                .andExpect(jsonPath("$.province", is("West-Vlaanderen")))
                .andExpect(jsonPath("$.city", is("Brugge")))
                .andExpect(jsonPath("$.street", is("teststraat")))
                .andExpect(jsonPath("$.number", is(3)));
    }
}