package madstodolist.model;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "equipos")
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;

    @ManyToMany
        @JoinTable(name = "equipo_usuario",
            joinColumns = { @JoinColumn(name = "fk_equipo") },
            inverseJoinColumns = {@JoinColumn(name = "fk_usuario")})
    Set<Usuario> usuarios = new HashSet<>();

    // Constructor vacío necesario para JPA/Hibernate.
    // Lo hacemos privado para que no se pueda usar desde el código de la aplicación. Para crear un
    // equipo en la aplicación habrá que llamar al constructor público. Hibernate sí que lo puede usar, a pesar
    // de ser privado.
    private Equipo() {}

    public Equipo(String nombre) { this.nombre = nombre; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public Set<Usuario> getUsuarios() { return usuarios; }

    public void addUsuario(Usuario usuario) { usuarios.add(usuario); }

    public void delUsuario(Usuario usuario) { usuarios.remove(usuario); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        if (id != null && equipo.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, equipo.id);
        // sino comparamos por campos obligatorios
        return nombre.equals(equipo.nombre);
    }

    @Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(nombre);
    }
}
