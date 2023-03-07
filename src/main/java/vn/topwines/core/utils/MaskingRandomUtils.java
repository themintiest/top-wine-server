package vn.topwines.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaskingRandomUtils {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    public static String generateCode(Long id) {
        long mask = SECURE_RANDOM.nextLong();
        long uuid = 0x0000000000000000L;
        long seed = id;
        for (int i = 0; i < 5; i++) {
            // mask: 0x01011101'...'01101010'01100010
            // seed: 0x00001000'...'00010101'11000100
            //  1. ((mask & 0xffL) | 0x0f) = ((0x01100010 & 0x11111111) | 0x00001111) = 0x01101111
            //  2. ((seed & 0x0fL) | 0xf0) = ((0x11000100 & 0x00001111) | 0x11110000) = 0x11110100
            //  3. uuid = uuid | (0x01101111 & 0x11110100) = (uuid | 0x01100100)
            uuid = uuid << 8;
            uuid |= ((mask & 0xffL) | 0x0f) & ((seed & 0x0fL) | 0xf0);
            mask >>= 8;
            seed >>= 4;
        }
        uuid &= 0x3fffffffffffffffL; // Make sure it's a positive number
        return Long.toString(uuid, Character.MAX_RADIX).toUpperCase();
    }

    public static Long getNumberFromCode(String code) {
        long number = Long.parseLong(code, Character.MAX_RADIX);
        long seed = 0;
        for (int i = 0; i < 5; i++) {
            seed <<= 4;
            seed |= (number & 0x0f);
            number >>= 8;
        }
        return seed;
    }
}
