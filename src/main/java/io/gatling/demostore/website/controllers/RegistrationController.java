package io.gatling.demostore.website.controllers;

import javax.validation.Valid;

import io.gatling.demostore.models.data.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @GetMapping
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping
    public String register(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordMatchProblem", "Passwords do not match!");
            return "register";
        }

        // DO NOT SAVE (readonly)

        return "redirect:/login";
    }
}
