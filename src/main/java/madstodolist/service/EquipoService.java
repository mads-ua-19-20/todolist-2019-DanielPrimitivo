package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
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

    @Autowired
    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Transactional
    public Equipo nuevoEquipo(String nombreEquipo) {
        Equipo equipo = new Equipo(nombreEquipo);
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
}
