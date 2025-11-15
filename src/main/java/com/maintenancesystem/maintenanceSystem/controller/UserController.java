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
public class UserController {

    private final UserService userService;

    @GetMapping("/usuarios")
    public String users(Model model) {
        List<User> users = userService.getAllUser();
        model.addAttribute("usuarios", users);
        model.addAttribute("nuevoUsuario", new User());
        return "usuarios";
    }

    @PostMapping("/usuarios")
    @ResponseBody
    public String saveUser(@ModelAttribute("nuevoUsuario") User user) {
        userService.saveUser(user);
        return "OK";
    }

    @GetMapping("/usuarios/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            model.addAttribute("error", "El usuario no existe");
        } else {
            model.addAttribute("mensaje", "Usuario eliminado correctamente");
        }
        model.addAttribute("usuarios", userService.getAllUser());
        model.addAttribute("nuevoUsuario", new User());

        return "usuarios";
    }
}

