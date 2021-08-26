package com.example.storeservice;

import com.example.storeservice.model.Store;
import com.example.storeservice.repository.StoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreRepository storeRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenGetStoresByArticleNumber_thenReturnStores() throws Exception {

        Store store1 = new Store("IKEA Hasselt", "integration123", 100);
        Store store2 = new Store("IKEA Wilrijk", "integration123", 200);

        List<Store> storeList = new ArrayList<>();
        storeList.add(store1);
        storeList.add(store2);

        given(storeRepository.findStoresByArticleNumber("integration123")).willReturn(storeList);

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
    public void whenGetStoreByArticleNumberAndStoreName_thenReturnJsonStore() throws Exception {

        Store store1 = new Store("IKEA Hasselt", "integration123", 100);

        given(storeRepository.findStoreByArticleNumberAndAndStoreName("integration123", "IKEA Hasselt")).willReturn(store1);

        mockMvc.perform(get("/product/{articleNumber}/store/{storeName}", store1.getArticleNumber(), store1.getStoreName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$.articleNumber", is("integration123")))
                .andExpect(jsonPath("$.stock", is(100)));
    }

    @Test
    public void whenPostStore_thenReturnJsonStore() throws Exception {

        Store store1 = new Store("IKEA Hasselt", "integration123", 100);

        mockMvc.perform(post("/store")
                .content(mapper.writeValueAsString(store1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$.articleNumber", is("integration123")))
                .andExpect(jsonPath("$.stock", is(100)));
    }

    @Test
    public void whenPutStoreByArticleNumberAndStoreName_thenReturnJsonStore() throws Exception {

        Store store1 = new Store("IKEA Hasselt", "integration123", 100);

        given(storeRepository.findStoreByArticleNumberAndAndStoreName("integration123", "IKEA Hasselt")).willReturn(store1);

        Store updatedStore = new Store("IKEA Hasselt", "integration123", 400);

        mockMvc.perform(put("/product/{articleNumber}/store/{storeName}", store1.getArticleNumber(), store1.getStoreName())
                .content(mapper.writeValueAsString(updatedStore))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName", is("IKEA Hasselt")))
                .andExpect(jsonPath("$.articleNumber", is("integration123")))
                .andExpect(jsonPath("$.stock", is(400)));
    }

    @Test
    public void whenDeleteStoreByArticleNumberAndStoreName_thenStatusOk() throws Exception {

        Store storeToBeDeleted = new Store("IKEA Hasselt", "integration123", 100);

        given(storeRepository.findStoreByArticleNumberAndAndStoreName("integration123", "IKEA Hasselt")).willReturn(storeToBeDeleted);

        mockMvc.perform(delete("/product/{articleNumber}/store/{storeName}", storeToBeDeleted.getArticleNumber(), storeToBeDeleted.getStoreName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void henDeleteStoreByArticleNumberAndStoreName_thenStatusNotFound() throws Exception {

        given(storeRepository.findStoreByArticleNumberAndAndStoreName("badArticleNumber", "badStoreName")).willReturn(null);

        mockMvc.perform(delete("/product/badArticleNumber/store/badStoreName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
