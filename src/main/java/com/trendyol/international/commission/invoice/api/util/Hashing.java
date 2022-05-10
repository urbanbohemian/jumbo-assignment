package com.trendyol.international.commission.invoice.api.util;

import com.trendyol.international.commission.invoice.api.model.exception.TrendyolException;
import org.apache.commons.lang3.StringUtils;
import org.hashids.Hashids;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Hashing {
    private static final String DEFAULT_HASH_SALT = "7266983812839641025";
    private static final String HASH_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final Integer MIN_HASH_LENGTH = 8;
    private static final Hashids HASHIDS = new Hashids(DEFAULT_HASH_SALT, MIN_HASH_LENGTH, HASH_ALPHABET);

    public static String encode(long... values) {
        return HASHIDS.encode(values);
    }

    public static Long decode(String value) {
        long[] resultArray = HASHIDS.decode(value);
        if (resultArray.length <= 0) {
            throw new TrendyolException("trendyol.starter.common.hashing.wrong.value.to.decode");
        }

        return resultArray[0];
    }

    public static long[] decodeMultipleValues(String value) {
        long[] resultArray = HASHIDS.decode(value);
        if (resultArray.length <= 1) {
            throw new TrendyolException("trendyol.starter.common.hashing.wrong.value.to.decode");
        }

        return resultArray;
    }

    public static String base64Encode(String... values) {
        return Base64.getEncoder().withoutPadding()
                .encodeToString(StringUtils.join(values, ":").getBytes(StandardCharsets.UTF_8));
    }

    public static String md5(byte[] value) {
        return DigestUtils.md5DigestAsHex(value).toUpperCase();
    }

    public static String md5(String value) {
        return md5(value.getBytes(StandardCharsets.UTF_8));
    }
}
