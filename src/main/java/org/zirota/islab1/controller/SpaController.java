package org.zirota.islab1.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
            "/persons",
            "/coordinates",
            "/locations",
            "/special"
    })
    public String forwardAngularRoutes() {
        return "forward:/index.html";
    }
}
