package io.gatling.demostore.api.controllers;

import io.gatling.demostore.api.payloads.CategoryRequest;
import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.data.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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

import static io.gatling.demostore.api.SwaggerConfig.SECURITY_SCHEME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "categories", description = "Categories")
@RestController
@RequestMapping("/api/category")
public class ApiCategoriesController {

    public ApiCategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private final CategoryRepository categoryRepository;

    @Operation(summary = "List all categories")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @Operation(summary = "Get a category")
    @ApiResponses(
            @ApiResponse(responseCode = "404", description = "Category not found")
    )
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Category get(@PathVariable @Parameter(description = "Category ID") Integer id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a category", security = @SecurityRequirement(name = SECURITY_SCHEME))
    @ApiResponses(
            @ApiResponse(responseCode = "400", description = "Invalid request content or duplicate of an existing category")
    )
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Category create(
            @Valid @RequestBody CategoryRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String slug = request.getName().toLowerCase().replace(" ", "-");
        if (categoryRepository.findBySlug(slug) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(slug);
        category.setSorting(100);

        // DO NO SAVE (readonly)
        return category;
    }

    @Operation(summary = "Update a category", security = @SecurityRequirement(name = SECURITY_SCHEME))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid request content"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Category update(
            @PathVariable @Parameter(description = "Category ID") Integer id,
            @Valid @RequestBody CategoryRequest request,
            BindingResult bindingResult
    ) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String slug = request.getName().toLowerCase().replace(" ", "-");
        category.setSlug(slug);
        category.setName(request.getName());

        // DO NO SAVE (readonly)
        return category;
    }
}
