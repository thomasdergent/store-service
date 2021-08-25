package com.example.storeservice.controller;

import com.example.storeservice.model.Store;
import com.example.storeservice.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    //Get stores
    @GetMapping("/stores/product/{articleNumber}")
    public List<Store> getAllStore(@PathVariable String articleNumber) {
        return this.storeRepository.findStoresByArticleNumber(articleNumber);
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
    @GetMapping("/product/{articleNumber}/store/{storeName}")
    public Store getStoreByStoreName(@PathVariable String articleNumber, @PathVariable String storeName) {
        return storeRepository.findStoreByArticleNumberAndAndStoreName(articleNumber, storeName);
    }

    //Post store for testing purposes
    @PostMapping("/store")
    public Store createStore(@RequestBody Store store) {

        this.storeRepository.save(store);

        return store;
    }

    //Update specific product from specific store
    @PutMapping("/product/{articleNumber}/store/{storeName}")
    public Store edit(@PathVariable String articleNumber, @PathVariable String storeName, @RequestBody Store updatedStore) {
        Store retrivedStore = storeRepository.findStoreByArticleNumberAndAndStoreName(articleNumber, storeName);

        retrivedStore.setStoreName(((updatedStore.getStoreName())));
        retrivedStore.setArticleNumber(updatedStore.getArticleNumber());
        retrivedStore.setStock(updatedStore.getStock());

        storeRepository.save(retrivedStore);

        return retrivedStore;
    }

    //Delete specific product from specific store
    @DeleteMapping("/product/{articleNumber}/store/{storeName}")
    public ResponseEntity deleteStore(@PathVariable String articleNumber, @PathVariable String storeName) {
        Store store = storeRepository.findStoreByArticleNumberAndAndStoreName(articleNumber, storeName);

        if (store != null) {
            storeRepository.delete(store);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

