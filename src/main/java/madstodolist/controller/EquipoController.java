package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
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

    @PostMapping("/equipos/{id}/usuarios/add")
    public String equipoAddUsuario(@PathVariable(value="id") Long equipoId, Model model,
                                   RedirectAttributes flash, HttpSession session) {
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

        try {
            Equipo equipoConUsuario = equipoService.addUsuarioEquipo(equipoId, idUsuarioLogeado);
            flash.addFlashAttribute("mensaje", "Te has apuntado correctamente");
        }
        catch (Exception e) {
            flash.addFlashAttribute("mensaje", "Ya estabas apuntado a este equipo");
        }

        return "redirect:/equipos/" + equipoId + "/usuarios";
    }

    @PostMapping("/equipos/{id}/usuarios/del")
    public String equipoDelUsuario(@PathVariable(value="id") Long equipoId, Model model,
                                   RedirectAttributes flash, HttpSession session) {
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

        try {
            Equipo equipoSinUsuario = equipoService.delUsuarioEquipo(equipoId, idUsuarioLogeado);
            flash.addFlashAttribute("mensaje", "Te has borrado correctamente");
        }
        catch (Exception e) {
            flash.addFlashAttribute("mensaje", "No estabas apuntado a este equipo");
        }

        return "redirect:/equipos/" + equipoId + "/usuarios";
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

    @GetMapping("/equipos/{id}/editar")
    public String formEditarEquipo(@PathVariable(value="id") Long equipoId, @ModelAttribute EquipoData equipoData,
                                   Model model, HttpSession session) {
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

        boolean admin = usuarioService.esAdmin(idUsuarioLogeado);
        if (!admin) {
            throw new UsuarioNoLogeadoException();
        }

        model.addAttribute("equipo", equipo);
        model.addAttribute("usuario", usuario);
        equipoData.setNombre(equipo.getNombre());
        return "formEditarEquipo";
    }

    @PostMapping("/equipos/{id}/editar")
    public String grabaEquipoModificado(@PathVariable(value="id") Long equipoId, @ModelAttribute EquipoData equipoData,
                                        Model model, RedirectAttributes flash, HttpSession session) {
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

        equipoService.modificaEquipo(equipoId, equipoData.getNombre());
        flash.addFlashAttribute("mensaje", "Equipo modificado correctamente");
        return "redirect:/equipos";
    }

    @DeleteMapping("/equipos/{id}")
    @ResponseBody
    public String borrarEquipo(@PathVariable(value="id") Long equipoId, RedirectAttributes flash,
                               HttpSession session) {
        Equipo equipo = equipoService.findById(equipoId);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeadoSession(session);
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");

        Usuario usuario = usuarioService.findById(idUsuarioLogeado);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }



        equipoService.borrarEquipo(equipoId);
        flash.addFlashAttribute("mensaje", "Equipo borrado correctamente");
        return "";
    }
}
