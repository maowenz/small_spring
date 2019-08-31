package com.mwz.controller;

import beans.AutoWired;
import com.mwz.service.TestService;

import web.mvc.Controller;
import web.mvc.RequestMapping;

/**
 * @author wzm
 * @date 2019年08月30日 16:50
 */
@Controller
@RequestMapping("/001")
public class TestController {

    @AutoWired
    private TestService testService;

    @RequestMapping("/test1")
    public String test() {
        return testService.test();
    }
}
