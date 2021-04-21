package io.gatling.demostore.api;

import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.data.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class ApiCategoriesController {

    public ApiCategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category get(@PathVariable Integer id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // TODO - authentication for create/update routes

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category create(
            @Valid @RequestBody Category category,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String slug = category.getName().toLowerCase().replace(" ", "-");
        if (categoryRepository.findBySlug(slug) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        category.setSlug(slug);
        category.setSorting(100);

        // DO NO SAVE (readonly)
        return category;
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category update(
            @PathVariable Integer id,
            @Valid @RequestBody Category update,
            BindingResult bindingResult
    ) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String slug = update.getName().toLowerCase().replace(" ", "-");
        category.setSlug(slug);
        category.setName(update.getName());

        // DO NO SAVE (readonly)
        return category;
    }
}
