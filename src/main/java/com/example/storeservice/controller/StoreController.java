package com.example.storeservice.controller;

import com.example.storeservice.model.Store;
import com.example.storeservice.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    //Get stores
    @GetMapping("/stores")
    public List<Store> getAllStore() {
        return this.storeRepository.findAll();
    }

//    @GetMapping("/stores/province/{province}")
//    public List<Store> getAllStoresByProvince(@PathVariable String province) {
//
//        return this.storeRepository.findStoresByProvinceContaining(province);
//    }
//
//    @GetMapping("/store/province/{province")
//    public Store getStoreByProvince(@PathVariable String province) {
//        return storeRepository.findStoreByProvince(province);
//    }

    //Get specific store
    @GetMapping("/store/{storeName}")
    public Store getStoreByStoreName(@PathVariable String storeName) {
        return storeRepository.findStoreByStoreName(storeName);
    }

    //Post store for testing purposes
    @PostMapping("/store")
    public Store createStore(@RequestBody Store store) {

        this.storeRepository.save(store);

        return store;
    }
}
//test