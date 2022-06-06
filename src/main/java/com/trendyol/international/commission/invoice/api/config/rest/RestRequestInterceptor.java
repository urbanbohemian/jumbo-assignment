package com.trendyol.international.commission.invoice.api.config.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.trendyol.international.commission.invoice.api.util.AuditionConstants.*;
import static com.trendyol.international.commission.invoice.api.util.HttpUtils.getClientIp;
import static com.trendyol.international.commission.invoice.api.util.HttpUtils.getRequestBody;


@Component @Slf4j
public class RestRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String airflowRunId = getFromHeader(request, X_AIRFLOW_RUN_ID);
        Map map = new TreeMap<>((Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
        Object airflowRunId = map.get("runId");
        log.info("Run id for airflow task is : {}",airflowRunId);

        String executorUser = getFromHeader(request, X_EXECUTOR_USER);
        MDC.put(X_EXECUTOR_USER, executorUser);

        String agentName = getFromHeader(request, X_AGENTNAME);
        MDC.put(X_AGENTNAME, agentName);

        String correlationId = getFromHeader(request, X_CORRELATION_ID);
        if (StringUtils.isBlank(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(X_CORRELATION_ID, correlationId.concat(X_X_DELIMITER).concat((String) airflowRunId));

        String remoteHost = getClientIp(request);
        MDC.put(X_REMOTE_HOST, remoteHost);

        String requestBody = getRequestBody(request);
        MDC.put(X_REQUEST_BODY, requestBody);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(X_EXECUTOR_USER);
        MDC.remove(X_AGENTNAME);
        MDC.remove(X_CORRELATION_ID);
        MDC.remove(X_REMOTE_HOST);
        MDC.remove(X_REQUEST_BODY);
        MDC.remove(X_STATUS);
        MDC.remove(X_LOG_TYPE);
    }

    private String getFromHeader(HttpServletRequest request, String headerKey) {
        return Optional.ofNullable(request.getHeader(headerKey))
                .map(Object::toString)
                .orElse(Optional
                        .ofNullable(request.getAttribute(headerKey))
                        .map(Object::toString).orElse(StringUtils.EMPTY));
    }
}
