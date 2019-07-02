package com.naptune.gateway.routes.handler.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/fallback")
@Slf4j
public class GatewayFallBackController {
    @RequestMapping("/timeOut/{routePath}")
    public ResponseEntity<?> timeOutFallBack(@PathVariable String routePath){
        log.error("网关[" + routePath + "]调用超时");
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("网关调用超时");
    }
}
