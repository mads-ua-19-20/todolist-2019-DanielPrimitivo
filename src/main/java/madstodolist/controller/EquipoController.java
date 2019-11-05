package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model,
                                  HttpSession session) {
        managerUserSesion.comprobarUsuarioLogeadoSession(session);
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

        Usuario usuario = usuarioService.findById(idUsuarioLogeado);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        model.addAttribute("usuario", usuario);
        return "formNuevoEquipo";
    }

    @PostMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model,
                                  RedirectAttributes flash, HttpSession session) {
        managerUserSesion.comprobarUsuarioLogeadoSession(session);
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

        Usuario usuario = usuarioService.findById(idUsuarioLogeado);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        equipoService.nuevoEquipo(equipoData.getNombre());
        flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        return "redirect:/equipos";
    }

    @GetMapping("/equipos")
    public String listadoEquipos(Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeadoSession(session);
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

        Usuario usuario = usuarioService.findById(idUsuarioLogeado);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        List<Equipo> equipos = equipoService.findAllOrderedByName();
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipos", equipos);
        return "listaEquipos";
    }

    @GetMapping("/equipos/{id}/usuarios")
    public String listadoUsuarios(@PathVariable(value="id") Long equipoId, Model model, HttpSession session) {
        managerUserSesion.comprobarUsuarioLogeadoSession(session);
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

        Usuario usuario = usuarioService.findById(idUsuarioLogeado);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        Equipo equipo = equipoService.findById(equipoId);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }

        List<Usuario> listaUsuarios = equipoService.usuariosEquipo(equipoId);
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("listaUsuarios", listaUsuarios);
        return "equipoListaUsuarios";
    }
}
