package com.strongfriends.controller;

import com.strongfriends.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RmController {


    @RequestMapping(path = {"/rm"}, method = {RequestMethod.GET})
    public String rmCalculator() {
        return "rm";
    }


}
