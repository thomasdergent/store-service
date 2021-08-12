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

    @GetMapping("/store")
    public List<Store> getAllStore() {
        return this.storeRepository.findAll();
    }

    @GetMapping("/store/name/{name}")
    public List<Store> getStoreByName(@PathVariable String name){
        return storeRepository.findStoreByNameContaining(name);
    }

    @PostMapping("store")
    public Store createStore(@RequestBody Store store) {
        return this.storeRepository.save(store);
    }
}
