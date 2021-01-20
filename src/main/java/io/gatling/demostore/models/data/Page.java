package io.gatling.demostore.models.data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pages", indexes = {@Index(columnList = "slug")})
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, message = "Title must be at least 2 characters long")
    private String title;

    private String slug;

    @Lob
    @Size(min = 5, message = "Content must be at least 5 characters long")
    private String content;

    private int sorting;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSorting() {
        return this.sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }
}
