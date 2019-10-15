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
        Usuario usuario;
        Usuario usuarioVer;
        if (session.getAttribute("idUsuarioLogeado") != null) {
            Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");
            usuario = usuarioService.findById(idUsuarioLogeado);

            usuarioVer = usuarioService.findById(idUsuario);
            if (usuarioVer == null) {
                throw new UsuarioNotFoundException();
            }
        }
        else {
            throw new UsuarioNoLogeadoException();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioVer", usuarioVer);

        return "descripcionUsuario";
    }
}
