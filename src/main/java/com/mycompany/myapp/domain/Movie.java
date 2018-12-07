/**
 * @author DELL
 */

package com.mycompany.myapp.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "movie_actor",
        joinColumns = {@JoinColumn(name = "movie_id")},
        inverseJoinColumns = {@JoinColumn(name = "actor_id")}
    )
    private Set<Actor> actors = new HashSet<>();

    public Movie() {
    }

    public Movie(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterator<Actor> getActorsIterator() {
        return this.actors.iterator();
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;
        return !(movie.getId() == null || getId() == null) && Objects.equals(getId(), movie.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", actors=" + actors +
            '}';
    }
}
