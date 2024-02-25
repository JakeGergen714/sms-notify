package com.jake.basiccloudgateway.controller;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/csrf")
    public CsrfToken csrfToken(CsrfToken csrf) {
        return csrf;
    }
}
