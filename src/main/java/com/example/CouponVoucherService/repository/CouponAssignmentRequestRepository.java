package com.example.CouponVoucherService.repository;

import com.example.CouponVoucherService.entity.CouponAssignmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponAssignmentRequestRepository
        extends JpaRepository<CouponAssignmentRequest, Long> {

    List<CouponAssignmentRequest> findBySellerId(Long sellerId);
}
