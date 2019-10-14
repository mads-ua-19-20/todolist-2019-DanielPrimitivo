package madstodolist;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.UsuarioController;
import madstodolist.model.Usuario;
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

@RunWith(SpringRunner.class)
@WebMvcTest(UsuarioController.class)
public class DescripcionUsuarioWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ManagerUserSesion managerUserSesion;

    @Test
    public void servicioDescripcionUsuario() throws Exception {
        Usuario usuario = new Usuario("juan.lopez@gmail.com");
        usuario.setId(1L);

        when(usuarioService.findById(1L)).thenReturn(usuario);

        this.mockMvc.perform(get("/usuarios/1"))
                .andDo(print())
                .andExpect(content().string(containsString("juan.lopez@gmail.com")));
    }
}
