package io.gatling.demostore.api.controllers;

import io.gatling.demostore.api.payloads.ProductRequest;
import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.ProductRepository;
import io.gatling.demostore.models.data.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static io.gatling.demostore.api.SwaggerConfig.SECURITY_SCHEME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "products", description = "Products")
@RestController
@RequestMapping("/api/product")
public class ApiProductsController {

    public ApiProductsController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Operation(summary = "List all products")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Product> listProducts(
            @RequestParam(value = "category", required = false)
            @Parameter(description = "Filter by category ID")
                    String categoryId
    ) {
        if (categoryId == null) {
            return productRepository.findAll();
        } else {
            return productRepository.findAllByCategoryId(categoryId, Pageable.unpaged());
        }
    }

    @Operation(summary = "Get a product")
    @ApiResponses(
            @ApiResponse(responseCode = "404", description = "Product not found")
    )
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Product getProduct(
            @PathVariable @Parameter(description = "Product ID") Integer id
    ) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create a product", security = @SecurityRequirement(name = SECURITY_SCHEME))
    @ApiResponses(
            @ApiResponse(responseCode = "400", description = "Invalid request content or duplicate of an existing product")
    )
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Product create(
            @Valid @RequestBody ProductRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String slug = request.getName().toLowerCase().replace(" ", "-");
        boolean productExists = productRepository.findBySlug(slug) != null;
        boolean categoryExists = categoryRepository.findById(request.getCategoryId()).isPresent();
        if (productExists || !categoryExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(slug);
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setPrice(request.getPrice());
        product.setCategoryId(request.getCategoryId().toString());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // DO NO SAVE (readonly)
        return product;
    }

    @Operation(summary = "Update a product", security = @SecurityRequirement(name = SECURITY_SCHEME))
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid request content"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Product update(
            @PathVariable @Parameter(description = "Product ID") Integer id,
            @Valid @RequestBody ProductRequest request,
            BindingResult bindingResult
    ) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        boolean categoryExists = categoryRepository.findById(request.getCategoryId()).isPresent();
        if (!categoryExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String slug = request.getName().toLowerCase().replace(" ", "-");
        product.setName(request.getName());
        product.setSlug(slug);
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setPrice(request.getPrice());
        product.setCategoryId(request.getCategoryId().toString());
        product.setUpdatedAt(LocalDateTime.now());

        // DO NO SAVE (readonly)
        return product;
    }
}
