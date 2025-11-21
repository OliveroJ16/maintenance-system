package com.maintenancesystem.maintenanceSystem.controller;

import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String users(Model model) {
        List<User> users = userService.getAllUser();
        model.addAttribute("users", users);
        model.addAttribute("newUser", new User());
        return "users";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        boolean deleted = userService.deleteUser(id);
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("newUser", new User());

        return "users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("userEdit") User data, Model model) {
        userService.updateUser(id, data);

        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("newUser", new User());

        return "users";
    }

}

