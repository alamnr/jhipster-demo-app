/**
 * @author DELL
 */

package com.mycompany.myapp.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

public class Actor {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private  String name;

    @ManyToMany(mappedBy = "actor")
    private Set<Movie> movies = new HashSet<>();

    public Actor() {
    }

    public Actor(String name) {
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

    public Set<Movie> getMovies() {
        return movies;
    }

    public void addMovies(Movie movie) {
        this.movies.add(movie);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        if (!id.equals(actor.id)) return false;
        if (!name.equals(actor.name)) return false;
        return movies != null ? movies.equals(actor.movies) : actor.movies == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Actor{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", movies=" + movies +
            '}';
    }
}
