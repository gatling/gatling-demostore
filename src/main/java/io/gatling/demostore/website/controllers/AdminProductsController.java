package io.gatling.demostore.website.controllers;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.ProductRepository;
import io.gatling.demostore.models.data.Category;
import io.gatling.demostore.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer p) {
        int perPage = 6;
        int page = (p != null) ? p : 0;

        Pageable pageable = PageRequest.of(page, perPage);

        Page<Product> products = productRepo.findAll(pageable);
        List<Category> categories = categoryRepo.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Category cat : categories) {
            cats.put(cat.getId(), cat.getName());
        }

        model.addAttribute("products", products);
        model.addAttribute("cats", cats);

        long count = productRepo.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        List<Category> categories = categoryRepo.findAll();

        model.addAttribute("categories", categories);

        return "admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product,
                      BindingResult bindingResult,
                      MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {
        List<Category> categories = categoryRepo.findAll();

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            return "admin/products/add";
        }

        boolean fileOK = false;
        String filename = file.getOriginalFilename();

        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepo.findBySlug(slug);

        if (!fileOK) {
            redirectAttributes.addFlashAttribute("message", "Image must be jpg or png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);
            product.setImage(filename);
            // DO NOT SAVE (readonly)

            // Commenting this out to prevent file uploads
            //   Files.write(path, bytes);
        }

        return "redirect:/admin/products/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productRepo.getOne(id);
        List<Category> categories = categoryRepo.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "admin/products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product product,
                       BindingResult bindingResult,
                       MultipartFile file,
                       RedirectAttributes redirectAttributes,
                       Model model) {
        Product currentProduct = productRepo.getOne(product.getId());

        List<Category> categories = categoryRepo.findAll();

        if (bindingResult.hasErrors()) {
            model.addAttribute("productName", currentProduct.getName());
            model.addAttribute("categories", categories);
            return "admin/products/edit";
        }

        boolean fileOK = false;
        String filename = file.getOriginalFilename();

        if (!file.isEmpty()) {
            if (filename.endsWith("jpg") || filename.endsWith("png")) {
                fileOK = true;
            }
        } else {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Product edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepo.findBySlugAndIdNot(slug, product.getId());

        if (!fileOK) {
            redirectAttributes.addFlashAttribute("message", "Image must be jpg or png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);

            if (!file.isEmpty()) {
                //   Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
                //    Files.delete(path2);
                product.setImage(filename);
                //    Files.write(path, bytes);
            } else {
                product.setImage(currentProduct.getImage());
            }

            // DO NOT SAVE (readonly)
        }

        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(RedirectAttributes redirectAttributes) {
        // DO NOT DELETE (readonly)

        redirectAttributes.addFlashAttribute("message", "Product deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/products";
    }
}
