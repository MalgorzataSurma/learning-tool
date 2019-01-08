package com.csg.learning.tool.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Date;

@Controller
public class WebAppController {

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("datetime", new Date());
        return "index";
    }
}
