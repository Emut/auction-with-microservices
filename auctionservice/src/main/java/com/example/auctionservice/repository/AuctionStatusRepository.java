package com.example.auctionservice.repository;

import com.example.auctionservice.entity.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionStatusRepository extends JpaRepository<AuctionStatus, Long> {
    Optional<AuctionStatus> findDistinctTopByOrderByIdDesc();
}
