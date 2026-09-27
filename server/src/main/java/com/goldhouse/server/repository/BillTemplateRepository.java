package com.goldhouse.server.repository;

import com.goldhouse.server.model.bill.BillTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillTemplateRepository extends JpaRepository<BillTemplate, String> {

    Optional<BillTemplate> findByIsDefaultTrue();

    long count();

    @Modifying
    @Query("UPDATE BillTemplate b SET b.isDefault = false WHERE b.isDefault = true")
    void resetAllDefaults();
}

