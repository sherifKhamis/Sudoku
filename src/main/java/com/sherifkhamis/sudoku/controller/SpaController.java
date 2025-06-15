package com.sherifkhamis.sudoku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    /**
     * Leitet alle Anfragen, die nicht explizit gemappt sind und keine Dateierweiterung haben
     * (und nicht unter /api oder /static liegen), an /index.html weiter.
     * Dies ermöglicht es dem Vue-Router, die Routen clientseitig zu handhaben.
     */
    @GetMapping(value = {"/", "/{path:^(?!api|static|.*\\.).*$}/**", "/{path:^(?!api|static|.*\\.).*}"})
    public String forwardAsPath() {
        return "forward:/index.html";
    }
}

