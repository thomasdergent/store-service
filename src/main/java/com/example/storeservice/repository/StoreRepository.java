package com.example.storeservice.repository;

import com.example.storeservice.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {
        List<Store> findStoresByArticleNumber(String articleNumber);
        Store findStoreByArticleNumberAndAndStoreName(String articleNumber, String storeName);
}
