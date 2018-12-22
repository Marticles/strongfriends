package com.strongfriends.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WilksController {

    @RequestMapping(path = {"/wilks"}, method = {RequestMethod.GET})
    public String wilksCalculator() {
        return "wilks";
    }
}
