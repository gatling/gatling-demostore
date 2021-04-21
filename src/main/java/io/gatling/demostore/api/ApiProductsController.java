package io.gatling.demostore.api;

import io.gatling.demostore.api.payload.ProductRequest;
import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.ProductRepository;
import io.gatling.demostore.models.data.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@RestController
@RequestMapping("/api/product")
public class ApiProductsController {

    public ApiProductsController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @GetMapping
    public List<Product> listProducts(@RequestParam(value = "category", required = false) String categoryId) {
        if (categoryId == null) {
            return productRepository.findAll();
        } else {
            return productRepository.findAllByCategoryId(categoryId, Pageable.unpaged());
        }
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Integer id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // TODO - authentication for create/update routes

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Product update(
            @PathVariable Integer id,
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
