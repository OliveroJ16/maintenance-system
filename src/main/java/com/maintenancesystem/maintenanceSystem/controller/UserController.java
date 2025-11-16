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
    public String saveUser(@ModelAttribute("nuevoUsuario") User user) {
        userService.saveUser(user);
        return "usuarios";
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

    @PostMapping("/usuarios/update/{id}")
    public String updateUser(@PathVariable Integer id,
                             @ModelAttribute("usuarioEditado") User data,
                             Model model) {

        boolean updated = userService.updateUser(id, data);

        if (!updated) {
            model.addAttribute("error", "No se pudo actualizar el usuario");
        } else {
            model.addAttribute("mensaje", "Usuario actualizado correctamente");
        }

        model.addAttribute("usuarios", userService.getAllUser());
        model.addAttribute("nuevoUsuario", new User());

        return "usuarios";
    }

}

