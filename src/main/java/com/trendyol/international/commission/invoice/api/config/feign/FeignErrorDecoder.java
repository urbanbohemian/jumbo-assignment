package com.trendyol.international.commission.invoice.api.config.feign;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
