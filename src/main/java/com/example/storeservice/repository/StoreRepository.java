package com.example.storeservice.repository;

import com.example.storeservice.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
        List<Store> findStoresByProvinceContaining(String province);
        List<Store> findStoresByStoreNameContaining(String storeName);
        Store findStoreByStoreName(String storeName);
        Store findStoreByProvince(String provence);
}
