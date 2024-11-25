package kr.co.petfriends.sample.common.utils;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerator {

    public static String generateId(String prefix) {
        return prefix + UUID.randomUUID();
    }
}
