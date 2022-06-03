package com.trendyol.international.commission.invoice.api.util;

import com.trendyol.international.commission.invoice.api.config.rest.CachedRequestWrapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class HttpUtils {

    public static final String HTTP_X_REAL_IP = "X-Real-IP";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private HttpUtils() {
    }

    public static String getClientIp(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HTTP_X_REAL_IP))
                .orElse(
                        Optional.ofNullable(request.getHeader(X_FORWARDED_FOR))
                                .orElse(request.getRemoteAddr())
                );
    }

    public static String getFromHeader(HttpServletRequest request, String headerKey) {
        return Optional.ofNullable(request.getHeader(headerKey))
                .map(Object::toString)
                .orElse(Optional
                        .ofNullable(request.getAttribute(headerKey))
                        .map(Object::toString).orElse(StringUtils.EMPTY));
    }

    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder requestBody = new StringBuilder();
        requestBody.append(request.getServletPath());

        String parameterMapAsString = convertWithStream(request.getParameterMap());

        if (StringUtils.isNotEmpty(parameterMapAsString)) {
            requestBody.append(" | ");
            requestBody.append(parameterMapAsString);
        }

        CachedRequestWrapper requestWrapper = new CachedRequestWrapper(request);
        String fileName = "filename=";
        try {
            String jsonBody = requestWrapper.getReader().lines().collect(Collectors.joining(", ")).replaceAll("\"", " ");
            if (StringUtils.isNotEmpty(jsonBody)) {
                if (jsonBody.contains(fileName)) {
                    int filenameIndex = jsonBody.indexOf(fileName);
                    jsonBody = jsonBody.substring(filenameIndex + fileName.length(), jsonBody.indexOf(", Content-Type:")).trim();
                }
                requestBody.append(" | ");
                requestBody.append(jsonBody);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return requestBody.toString();
    }

    private static String convertWithStream(Map<String, ?> map) {
        if (map.isEmpty()) return "";
        return map.keySet().stream()
                .map(key -> key + "=" + String.join("|", (String[]) map.get(key)))
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
