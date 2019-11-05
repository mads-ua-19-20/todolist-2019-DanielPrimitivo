package madstodolist;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.EquipoController;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(EquipoController.class)
public class EquipoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipoService equipoService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSesion managerUserSesion;

    @Test
    public void nuevoEquipoDevuelveForm() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(get("/equipos/nuevo"))
                .andDo(print())
                .andExpect(content().string(containsString("action=\"/equipos/nuevo\"")));
    }

    @Test
    public void nuevoEquipoDevuelveNotFound() throws Exception {

        when(usuarioService.findById(null)).thenReturn(null);

        this.mockMvc.perform(get("/equipos/nuevo"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void addUsuarioEquipoApuntarme() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);
        Equipo equipo = new Equipo("Proyecto Prueba");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.addUsuarioEquipo(1L, null)).thenReturn(equipo);

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(content().string(containsString("action=\"/equipos/1/usuarios/add\"")));
    }

    @Test
    public void delUsuarioEquipoDesapuntarme() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);
        Equipo equipo = new Equipo("Proyecto Prueba");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.delUsuarioEquipo(1L, null)).thenReturn(equipo);

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(content().string(containsString("action=\"/equipos/1/usuarios/del\"")));
    }

    @Test
    public void equiposDevuelveListado() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);
        Equipo equipo = new Equipo("Proyecto Diamante");
        equipo.setId(1L);
        List<Equipo> equipos = new ArrayList();
        equipos.add(equipo);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findAllOrderedByName()).thenReturn(equipos);

        this.mockMvc.perform(get("/equipos"))
                .andDo(print())
                .andExpect(content().string(containsString("Proyecto Diamante")));
    }

    @Test
    public void equipoDevuelveListadoUsuarios() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);
        List<Usuario> usuariosEquipo = new ArrayList();
        usuariosEquipo.add(usuario);
        Equipo equipo = new Equipo("Proyecto Diamante");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.usuariosEquipo(1L)).thenReturn(usuariosEquipo);

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(content().string(containsString("domingo@ua")));
    }
}
