package com.example.CouponVoucherService.controller;

import com.example.CouponVoucherService.common.ApiResponse;
import com.example.CouponVoucherService.dto.request.CreateCouponRequest;
import com.example.CouponVoucherService.dto.response.CouponResponse;
import com.example.CouponVoucherService.service.CouponService;
import com.example.CouponVoucherService.utils.JwtUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for seller to manage coupons
 * Testing version: no authentication, no headers required
 */
@RestController
@RequestMapping("/api/seller/coupons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class SellerCouponController {

    private final CouponService couponService;
    private final JwtUtils jwtUtils;
 
    // ===== Helping Meathod ====
    
     private Long extractUserIdFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Unauthorized: Missing or invalid Authorization header");
        }
        String token = authorizationHeader.substring(7).trim();
        return jwtUtils.getUserIdFromToken(token);
    }

    // ===== CREATE =====


    @PostMapping
    public ApiResponse<CouponResponse> createCoupon(  @RequestBody CreateCouponRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {
        Long sellerId = extractUserIdFromHeader(authorizationHeader);
        CouponResponse response = couponService.createCoupon(request, sellerId);
        return ApiResponse.success(response);
    }

    // ===== GET BY ID =====
   @GetMapping("/{couponId}")
public ApiResponse<CouponResponse> getCouponById(
        @PathVariable Long couponId, @RequestHeader("Authorization") String authorizationHeader) {
            Long buyerId = extractUserIdFromHeader(authorizationHeader);
    return ApiResponse.success(
            couponService.getCouponById(couponId, buyerId)
    );
}


    // ===== GET ALL =====
    @GetMapping
    public ApiResponse<List<CouponResponse>> getAllCoupons(
            @RequestParam(value = "showCode", defaultValue = "false") boolean showCode) {
        List<CouponResponse> response = couponService.getAllCouponsForSeller( showCode);
        return ApiResponse.success(response);
    }

    // ===== UPDATE =====
    @PutMapping("/{id}")
    public ApiResponse<CouponResponse> updateCoupon(@PathVariable("id") Long couponId,
                                                    @RequestBody CreateCouponRequest request ,  @RequestHeader("Authorization") String authorizationHeader) {
        Long SellerId = extractUserIdFromHeader(authorizationHeader);
        CouponResponse response = couponService.updateCoupon(couponId, request, SellerId);
        return ApiResponse.success(response);
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoupon(@PathVariable("id") Long couponId ,  @RequestHeader("Authorization") String authorizationHeader) {
        Long sellerId = extractUserIdFromHeader(authorizationHeader);
        couponService.deleteCoupon(couponId, sellerId);
    }
}
