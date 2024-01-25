package com.practice.api.service;

import com.practice.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Test
    public void 여러명_응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서의 작업 기다리기

        for (int i = 0; i < threadCount; ++i) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000); // consumer가 데이터를 수신해서 처리할 때까지의 대기 시간

        long count = couponRepository.count();

        assertThat(count).isEqualTo(100);
    }

    @Test
    public void 한명당_한개의_쿠폰만_발급() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서의 작업 기다리기

        for (int i = 0; i < threadCount; ++i) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(1L); // 1번 유저가 쿠폰발급 요청을 1000번 보내도록
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000); // consumer가 데이터를 수신해서 처리할 때까지의 대기 시간

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1);
    }

}
