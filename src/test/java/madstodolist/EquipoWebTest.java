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
}
