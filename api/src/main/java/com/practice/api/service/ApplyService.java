package com.practice.api.service;

import com.practice.api.producer.CouponCreateProducer;
import com.practice.api.repository.AppliedUserRepository;
import com.practice.api.repository.CouponCountRepository;
import com.practice.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    // 쿠폰 발급
    public void apply(Long userId) {
        // 요구사항 추가: 1인당 1개의 쿠폰만 발급할 수 있다.
        Long apply = appliedUserRepository.add(userId);

        if (apply != 1) { // 이미 쿠폰을 발급 받았다면 쿠폰을 발급하지 않고 return한다.
            return;
        }

        long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        couponCreateProducer.create(userId);
    }

}
