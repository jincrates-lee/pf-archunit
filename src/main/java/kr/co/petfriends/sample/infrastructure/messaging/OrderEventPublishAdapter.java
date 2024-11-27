package kr.co.petfriends.sample.infrastructure.messaging;

import kr.co.petfriends.sample.common.annotation.Adapter;
import kr.co.petfriends.sample.domain.port.OrderEventPublishPort;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
class OrderEventPublishAdapter implements OrderEventPublishPort {

    @Override
    public void publish() {
        // do something
    }
}
