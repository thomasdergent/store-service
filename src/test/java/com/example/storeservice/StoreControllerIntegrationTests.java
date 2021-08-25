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

    Store store1 = new Store("IKEA Hasselt", "integration123", 100);
    Store store2 = new Store("IKEA Wilrijk", "integration123", 200);
    Store store3 = new Store("IKEA Hasselt", "integration456", 300);

    private Store storeToBeDeleted = new Store("IKEA Wilrijk", "integration789", 300);

    @BeforeEach
    public void beforeAllTests() {
        storeRepository.deleteAll();
        storeRepository.save(store1);
        storeRepository.save(store2);
        storeRepository.save(store3);
        storeRepository.save(storeToBeDeleted);
    }

    @AfterEach
    public void afterAllTests() {

        storeRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenStore_whenGetStoresByArticleNumber_thenReturnJsonStores() throws Exception {

        List<Store> productList = new ArrayList<>();
        productList.add(store1);
        productList.add(store2);

        mockMvc.perform(get("/stores/product/{articleNumber}", "integration123"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$[0].articleNumber", is("integration123")))
                .andExpect(jsonPath("$[0].stock", is(100)))
                .andExpect(jsonPath("$[1].storeName", is("IKEA Wilrijk")))
                .andExpect(jsonPath("$[1].articleNumber", is("integration123")))
                .andExpect(jsonPath("$[1].stock", is(200)));
    }

    @Test
    public void givenStore_whenGetStoreByArticleNumberAndStoreName_thenReturnJsonStore() throws Exception {

        mockMvc.perform(get("/product/{articleNumber}/store/{storeName}", store1.getArticleNumber(), store1.getStoreName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$.articleNumber", is("integration123")))
                .andExpect(jsonPath("$.stock", is(100)));
    }

    @Test
    public void whenPostStore_thenReturnJsonStore() throws Exception {

        Store store4 = new Store("IKEA Wilrijk", "integration456", 400);

        mockMvc.perform(post("/store")
                .content(mapper.writeValueAsString(store4))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Wilrijk")))
                .andExpect(jsonPath("$.articleNumber", is("integration456")))
                .andExpect(jsonPath("$.stock", is(400)));
    }

    @Test
    public void givenStore_whenPutStoreByArticleNumberAndStoreName_thenReturnJsonStore() throws Exception {

        Store updatedStore = new Store("IKEA Hasselt", "integration123", 800);

        mockMvc.perform(put("/product/{articleNumber}/store/{storeName}", store1.getArticleNumber(), store1.getStoreName())
                .content(mapper.writeValueAsString(updatedStore))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$.articleNumber", is("integration123")))
                .andExpect(jsonPath("$.stock", is(800)));
    }

    @Test
    public void givenStore_whenDeleteStoreByArticleNumberAndStoreName_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/product/{articleNumber}/store/{storeName}", storeToBeDeleted.getArticleNumber(), storeToBeDeleted.getStoreName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoStore_whenDeleteStoreByArticleNumberAndStoreName_thenStatusNotFound() throws Exception {

        mockMvc.perform(delete("/product/{articleNumber}/store/{storeName}", "badArticleNumber", "badStoreName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}