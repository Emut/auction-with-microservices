package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("update Item i set i.price=:price, i.userId=:userId where i.id=:id and i.price>:price")
    void customUpdatePrice(Long id, double price, Long userId);
}
