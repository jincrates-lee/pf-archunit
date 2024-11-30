package kr.co.petfriends.sample.payment.service;

import kr.co.petfriends.sample.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final UserService userService;

    // ex. 유저 서비스를 결제 유저 조회
}
