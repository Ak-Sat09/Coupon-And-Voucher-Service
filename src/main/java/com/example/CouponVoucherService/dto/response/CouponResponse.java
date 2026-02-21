package com.example.CouponVoucherService.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CouponResponse {

    private Long id;
    private String name;
    private String description;
    private String couponCode; // can be masked
    private LocalDateTime expiryDate;

}
