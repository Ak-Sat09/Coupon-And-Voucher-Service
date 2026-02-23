package com.example.CouponVoucherService.controller;

import com.example.CouponVoucherService.common.ApiResponse; 
import com.example.CouponVoucherService.dto.response.CouponAssignmentResponseDTO;
import com.example.CouponVoucherService.service.CouponAssignmentRequestService;
import com.example.CouponVoucherService.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupon-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CouponAssignmentRequestController {

    private final CouponAssignmentRequestService requestService;
    private final JwtUtils jwtUtils;

    // ===== BUYER SEND REQUEST =====
  @PostMapping("/{couponId}")
public ResponseEntity<ApiResponse<String>> createRequest(
        @PathVariable Long couponId,
        @RequestHeader("Authorization") String authHeader) {

    Long buyerId = extractUserIdFromHeader(authHeader);
    requestService.createRequest(couponId, buyerId);
    return ResponseEntity.ok(ApiResponse.success("Request sent"));
}


    // ===== SELLER FETCH REQUESTS =====
    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<List<CouponAssignmentResponseDTO>>> getRequestsForSeller(
            @RequestHeader("Authorization") String authHeader) {

        Long sellerId = extractUserIdFromHeader(authHeader);
        List<CouponAssignmentResponseDTO> requests = requestService.getRequestsForSeller(sellerId);
        return ResponseEntity.ok(ApiResponse.success(requests));
    }
@PostMapping("/seller/accept/{requestId}")
public ResponseEntity<ApiResponse<String>> acceptRequest(
        @PathVariable Long requestId,
        @RequestHeader("Authorization") String authHeader) {

    Long sellerId = extractUserIdFromHeader(authHeader);
    requestService.acceptRequest(requestId, sellerId);

    return ResponseEntity.ok(ApiResponse.success("Coupon assigned successfully"));
}

    

    // ===== HELPER =====
    private Long extractUserIdFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Unauthorized: Missing or invalid Authorization header");
        }
        String token = authorizationHeader.substring(7).trim();
        return jwtUtils.getUserIdFromToken(token);
    }
}
