package com.example.CouponVoucherService.service;

import com.example.CouponVoucherService.dto.request.CreateCouponRequest;
import com.example.CouponVoucherService.dto.response.CouponResponse;
import com.example.CouponVoucherService.entity.Coupon;
import com.example.CouponVoucherService.exception.BusinessException;
import com.example.CouponVoucherService.exception.ErrorCode;
import com.example.CouponVoucherService.mapper.CouponMapper;
import com.example.CouponVoucherService.repository.CouponAssignmentRepository;
import com.example.CouponVoucherService.repository.CouponRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepo couponRepository;
    private final CouponMapper couponMapper;
    private final CouponAssignmentRepository assignmentRepository;

    /* ===== CREATE ===== */
    @Transactional
    public CouponResponse createCoupon(CreateCouponRequest request, Long sellerId) {
        LocalDateTime now = LocalDateTime.now();

        Coupon coupon = Coupon.builder()
                .sellerId(sellerId)
                .name(request.getName())
                .description(request.getDescription())
                .couponCode(request.getCouponCode())
                .expiryDate(request.getExpiryDate())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Coupon saved = couponRepository.save(coupon);
        return couponMapper.toDto(saved, false);  
    }

    /* ===== GET BY ID ===== */
   @Transactional(readOnly = true)
public CouponResponse getCouponById(Long couponId, Long buyerId) {

    Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new BusinessException(
                    ErrorCode.COUPON_NOT_FOUND, "Coupon not found"));
 
    boolean isAssignedToBuyer =
            assignmentRepository.existsByCouponIdAndBuyerId(couponId, buyerId);

     
    return couponMapper.toDto(coupon, isAssignedToBuyer);
}


    /* ===== GET ALL ===== */
    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCouponsForSeller(boolean showCode) {
        List<Coupon> coupons = couponRepository.findAll();
        return couponMapper.toDtoList(coupons, showCode);
    }

    /* ===== UPDATE ===== */
    @Transactional
    public CouponResponse updateCoupon(Long couponId, CreateCouponRequest request, Long sellerId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND, "Coupon not found"));

        // Only seller who created coupon can update
        if (!coupon.getSellerId().equals(sellerId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACTION, "Seller mismatch");
        }

        coupon = Coupon.builder()
                .id(coupon.getId())
                .sellerId(coupon.getSellerId())
                .name(request.getName())
                .description(request.getDescription())
                .couponCode(request.getCouponCode())
                .expiryDate(request.getExpiryDate())
                .createdAt(coupon.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Coupon updated = couponRepository.save(coupon);
        return couponMapper.toDto(updated, false);
    }

    /* ===== DELETE ===== */
    @Transactional
    public void deleteCoupon(Long couponId, Long sellerId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COUPON_NOT_FOUND, "Coupon not found"));

        // Only seller can delete
        if (!coupon.getSellerId().equals(sellerId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACTION, "Seller mismatch");
        }

        couponRepository.delete(coupon);
    }
}
