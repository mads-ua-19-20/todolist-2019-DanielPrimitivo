package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class EquipoService {
    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    private EquipoRepository equipoRepository;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public EquipoService(EquipoRepository equipoRepository, UsuarioRepository usuarioRepository) {
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Equipo nuevoEquipo(String nombreEquipo) {
        Equipo equipo = new Equipo(nombreEquipo);
        equipoRepository.save(equipo);
        return equipo;
    }

    @Transactional
    public Equipo addUsuarioEquipo(Long equipoId, Long usuarioId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + equipoId + " no existe");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("Usuario " + usuarioId + " no existe al añadir a equipo");
        }

        if (equipo.getUsuarios().contains(usuario)) {
            throw new EquipoServiceException("Equipo " + equipoId + " ya contiene al usuario " + usuarioId);
        }

        equipo.addUsuario(usuario);
        equipoRepository.save(equipo);
        return equipo;
    }

    @Transactional
    public Equipo delUsuarioEquipo(Long equipoId, Long usuarioId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + equipoId + " no existe");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("Usuario " + usuarioId + " no existe al añadir a equipo");
        }

        if (!equipo.getUsuarios().contains(usuario)) {
            throw new EquipoServiceException("Equipo " + equipoId + " no contiene al usuario " + usuarioId);
        }

        equipo.delUsuario(usuario);
        equipoRepository.save(equipo);
        return equipo;
    }

    @Transactional(readOnly = true)
    public List<Equipo> findAllOrderedByName() {
        List<Equipo> equipos = new ArrayList(equipoRepository.findAll());
        Collections.sort(equipos, Comparator.comparing(Equipo::getNombre));
        return equipos;
    }

    @Transactional(readOnly = true)
    public Equipo findById(Long equipoId) { return equipoRepository.findById(equipoId).orElse(null); }

    @Transactional(readOnly = true)
    public List<Usuario> usuariosEquipo(Long equipoId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + equipoId + " no existe");
        }

        List<Usuario> usuarios = new ArrayList(equipo.getUsuarios());
        return usuarios;
    }

    @Transactional
    public void borrarEquipo(Long equipoId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("No existe equipo con id " + equipoId);
        }
        equipoRepository.delete(equipo);
    }
}
