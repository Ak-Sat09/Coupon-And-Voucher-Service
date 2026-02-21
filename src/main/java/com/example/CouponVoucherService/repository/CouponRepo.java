package com.example.CouponVoucherService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CouponVoucherService.entity.Coupon;

public interface CouponRepo extends JpaRepository<Coupon , Long>{
    // Get all coupons created by a seller
    List<Coupon> findBySellerId(Long sellerId);

    // Optional: get all active (not expired) coupons
    List<Coupon> findBySellerIdAndExpiryDateAfter(Long sellerId, LocalDateTime now);
}
