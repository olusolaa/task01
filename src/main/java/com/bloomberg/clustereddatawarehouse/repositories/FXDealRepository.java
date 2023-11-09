package com.bloomberg.clustereddatawarehouse.repositories;


import com.bloomberg.clustereddatawarehouse.models.FXDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FXDealRepository extends JpaRepository<FXDeal, Long> {
    Optional<FXDeal> findByUniqueId(String dealUniqueId);
}

