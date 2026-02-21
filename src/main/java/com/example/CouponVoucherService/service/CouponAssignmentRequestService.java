package com.example.CouponVoucherService.service;
 
import com.example.CouponVoucherService.dto.response.CouponAssignmentResponseDTO;
import com.example.CouponVoucherService.entity.Coupon;
import com.example.CouponVoucherService.entity.CouponAssignmentRequest;
import com.example.CouponVoucherService.repository.CouponAssignmentRequestRepository;
import com.example.CouponVoucherService.repository.CouponRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponAssignmentRequestService {

    private final CouponRepo couponRepo;
    private final CouponAssignmentRequestRepository requestRepository;
    private final CouponAssignmentService assignmentService;

    /* ===== BUYER SEND REQUEST ===== */
public void createRequest(Long couponId, Long buyerId) {

    Coupon coupon = couponRepo.findById(couponId)
            .orElseThrow(() -> new RuntimeException("Coupon not found"));

    CouponAssignmentRequest request = CouponAssignmentRequest.builder()
            .couponId(couponId)
            .buyerId(buyerId)
            .sellerId(coupon.getSellerId())
            .createdAt(LocalDateTime.now())
            .build();

    requestRepository.save(request);
}


    /* ===== SELLER VIEW REQUESTS ===== */
    public List<CouponAssignmentResponseDTO> getRequestsForSeller(Long sellerId) {

        return requestRepository.findBySellerId(sellerId)
                .stream()
                .map(req -> CouponAssignmentResponseDTO.builder()
                        .requestId(req.getId())
                        .couponId(req.getCouponId())
                        .buyerId(req.getBuyerId())
                        .sellerId(req.getSellerId())
                        .createdAt(req.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
 
    public void acceptRequest(Long requestId, Long sellerId) {

        CouponAssignmentRequest request =
                requestRepository.findById(requestId)
                        .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized seller");
        }
 
        assignmentService.assignCoupon(
                request.getCouponId(),
                request.getBuyerId()
        );

        // Remove request after accept
        requestRepository.delete(request);
    }
}
