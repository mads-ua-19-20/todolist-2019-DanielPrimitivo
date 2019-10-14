package madstodolist.controller;
import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/usuarios")
    public String listadoUsuarios(Model model, HttpSession session) {
        Usuario usuario = null;
        List<Usuario> listaUsuarios = null;

        if (session.getAttribute("idUsuarioLogeado") != null) {
            Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

            usuario = usuarioService.findById(idUsuarioLogeado);
            listaUsuarios = usuarioService.allUsuarios();
        }
        else {
            throw new UsuarioNoLogeadoException();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("listaUsuarios", listaUsuarios);

        return "listaUsuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String descripcionUsuario(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session) {
        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        model.addAttribute("usuario", usuario);

        return "descripcionUsuario";
    }
}
