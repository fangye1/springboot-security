package com.springboot.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qizenan
 * @date 2020/10/08 13:12
 */
@RestController
@RequestMapping("")
public class IndexController extends BaseController {

    @GetMapping(value = "")
    public ResponseEntity helloWord() {
        return success("hello world");
    }
}
