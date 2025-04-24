package com.game.srpg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "2D 스타일 SRPG 게임");
        return "index";
    }
    
    @GetMapping("/game")
    public String game(Model model) {
        model.addAttribute("title", "게임 플레이");
        return "game";
    }
}
