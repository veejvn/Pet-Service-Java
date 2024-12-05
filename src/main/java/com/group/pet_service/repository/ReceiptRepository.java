package com.group.pet_service.repository;

import com.group.pet_service.model.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String> {
    List<Receipt> findAllByUserIdOrderByCreatedAtDesc(String id);

    List<Receipt> findDistinctByItems_Staff_Id(String staffId);

    Page<Receipt> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
