package com.practice.api.service;

import com.practice.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void 한번만_응모() {
        applyService.apply(1L);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1L);
    }

}
