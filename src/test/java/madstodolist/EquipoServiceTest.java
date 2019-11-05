package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.EquipoServiceException;
import madstodolist.service.UsuarioService;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipoServiceTest {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void obtenerListadoEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Equipo> equipos = equipoService.findAllOrderedByName();

        // THEN
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto Adamantium");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    public void obtenerEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoService.findById(1L);

        // THEN
        assertThat(equipo.getNombre()).isEqualTo("Proyecto Cobalto");
        // Comprobamos que la relación con Usuarios es lazy: al
        // intentar acceder a la colección de usuarios se debe lanzar una
        // excepción de tipo LazyInitializationException.
        assertThatThrownBy(() -> {
            equipo.getUsuarios().size();
        }).isInstanceOf(LazyInitializationException.class);
    }

    @Test
    public void comprobarRelacionUsuarioEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);

        // THEN

        assertThat(usuario.getEquipos()).hasSize(1);
    }

    @Test
    public void obtenerUsuariosEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Usuario> usuarios = equipoService.usuariosEquipo(1L);

        // THEN
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("ana.garcia@gmail.com");
        // Comprobamos que la relación entre usuarios y equipos es eager
        // Primero comprobamos que la colección de equipos tiene 1 elemento
        assertThat(usuarios.get(0).getEquipos()).hasSize(1);
        // Y después que el elemento es el equipo Proyecto Cobalto
        assertThat(usuarios.get(0).getEquipos().stream().findFirst().get().getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    @Transactional
    public void testNuevoEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoService.nuevoEquipo("Equipo Prueba");

        // THEN
        List<Equipo> equipos = equipoService.findAllOrderedByName();
        assertThat(equipos).contains(equipo);
    }

    @Test
    @Transactional
    public void testEquipoAddUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoService.addUsuarioEquipo(1L, 2L);

        // THEN
        Usuario usuario = usuarioService.findById(2L);
        assertThat(equipo.getUsuarios()).contains(usuario);
    }

    @Test(expected = EquipoServiceException.class)
    public void testEquipoErroneoAddUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        equipoService.addUsuarioEquipo(-1L, 1L);

        // THEN
        // Se produce una excepción comprobada con el expected del test
    }
}
