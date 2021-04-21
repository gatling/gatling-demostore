package io.gatling.demostore.models.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categories", indexes = {@Index(columnList = "name"), @Index(columnList = "slug")})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    private String slug;

    private int sorting;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getSorting() {
        return this.sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }
}
