package madstodolist.controller;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/about")
    public String loginForm(HttpSession session, Model model) {
        Usuario usuario = null;
        if (session.getAttribute("idUsuarioLogeado") != null) {
            Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

            usuario = usuarioService.findById(idUsuarioLogeado);
        }


        model.addAttribute("usuario", usuario);
        return "about";
    }
}
