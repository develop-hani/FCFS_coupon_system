package com.practice.api.service;

import com.practice.api.domain.Coupon;
import com.practice.api.producer.CouponCreateProducer;
import com.practice.api.repository.CouponCountRepository;
import com.practice.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    // 쿠폰 발급
    public void apply(Long userId) {
        long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        couponCreateProducer.create(userId);
    }

}
