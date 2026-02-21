package com.example.CouponVoucherService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "coupon_assignments",
    uniqueConstraints = @UniqueConstraint(columnNames = {"coupon_id", "buyer_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CouponAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    /* ===== Domain Method ===== */
    
    public boolean isAssignedTo(Long buyerId) {
        return this.buyerId.equals(buyerId);
    }
}
