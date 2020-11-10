package com.gatling.demostore.models;

import java.util.List;

import com.gatling.demostore.models.data.Page;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PageRepository extends JpaRepository<Page, Integer> {

    Page findBySlug(String slug);

    // @Query("SELECT p FROM Page p WHERE p.id != :id and p.slug = :slug")
    // Page findBySlug(int id, String slug);

    // this does the same as above
    Page findBySlugAndIdNot(String slug, int id);

    List<Page> findAllByOrderBySortingAsc();
    

}
