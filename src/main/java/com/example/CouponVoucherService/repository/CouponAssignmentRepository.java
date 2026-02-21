package com.example.CouponVoucherService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CouponVoucherService.entity.CouponAssignment;

@Repository
public interface CouponAssignmentRepository extends JpaRepository<CouponAssignment, Long> {

    // Check if a coupon is already assigned to a buyer
    boolean existsByCouponIdAndBuyerId(Long couponId, Long buyerId);

}
