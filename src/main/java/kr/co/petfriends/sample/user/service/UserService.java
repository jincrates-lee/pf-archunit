package kr.co.petfriends.sample.user.service;

import kr.co.petfriends.sample.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final PaymentService paymentService;

    // ex. 결제 서비스를 통해 결제 내역 조회
}
