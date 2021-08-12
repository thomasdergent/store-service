package com.example.storeservice.controller;

import com.example.storeservice.model.Store;
import com.example.storeservice.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping("/store")
    public List<Store> getAllStore() {
        return this.storeRepository.findAll();
    }

    @PostMapping("store")
    public Store createStore(@RequestBody Store store) {
        return this.storeRepository.save(store);
    }
}
