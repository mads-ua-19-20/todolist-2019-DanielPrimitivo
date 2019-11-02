package madstodolist.model;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "equipos")
public class Equipo implements Serializable {

    private static final long seerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    public Equipo(String nombre) { this.nombre = nombre; }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
}
