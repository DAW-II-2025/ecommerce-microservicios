package pe.edu.cibertec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null)
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
        if (logout != null)
            model.addAttribute("info", "Has cerrado sesión correctamente.");
        return "login";
    }
}
